package com.summarizer.services

import com.google.inject.{ImplementedBy, Singleton}
import com.summarizer.domain.Chain
import com.twitter.inject.Logging

@ImplementedBy(classOf[DefaultChainScoresService])
trait ChainScoresService extends Logging {

  def calculateChainScores(chains: Seq[Chain]): Seq[Chain]

  def calculateChainStrengths(chains: Seq[Chain]): Seq[Chain]

  def getStrongChains(chains: Seq[Chain]): Seq[Chain]

}

@Singleton
class DefaultChainScoresService extends ChainScoresService with Logging {
  /*
    We define the score of an interpretation as the sum of its chain scores.
    A chain score is determined by the number and weight of the relations
    between chain members. Experimentally, we fixed the weight of reiteration
     = 10 synonymy = 10 antonymy = 7 hypernymy = 4 hyponymy = 4 related_with = 4 holo_part = 4 holo_member = 4 yan_kavram = 4
  */

  def calculateChainScores(chains: Seq[Chain]): Seq[Chain] = {
    info("Chain Scores Service calculate Chain Scores")
    var sumScore = 0
    val chainsWithScores = chains.map { chain =>
      val relations = chain.members.map(_._2)
      for(relation <- relations) {
        if (relation.equals("synonymy")) {
          sumScore += 10
        } else if (relation.equals("antonymy")) {
          sumScore += 7
        } else if (relation.equals("hypernymy") || relation.equals("hyponymy")
          || relation.equals("related_with") || relation.equals("holo_part")
          || relation.equals("holo_member") || relation.equals("yan_kavram")) {
          sumScore += 4
        }
      }
      chain.score = sumScore
      sumScore = 0
      chain
    }
    chainsWithScores
  }

  /*
   *
   * 1. Compute the aggregate score of each chain by summing the scores of
   * each individual element in the chain. 2. Pick up the chains whose score
   * is more than the mean of the scores for every chain computed in the
   * document. 3. For each of the strong chains, identify representative
   * words, whose contribution to the chain is maximum 4. Choose the sentence
   * that contains the first appearance of a representative chain member in
   * the text.
 */

  def calculateChainStrengths(chains: Seq[Chain]): Seq[Chain] = {
    info("Chain Scores Service calculate Chain Strengths")
    val chainsWithStrengths =  chains.map { chain =>
      val lexicals = chain.members.map(_._1)
      val uniqueLexicals = lexicals.map(_.getWord()).distinct
      val homogenity = 1.0 - (uniqueLexicals.size.toDouble / lexicals.size.toDouble)
      val strength = lexicals.size.toDouble * homogenity
      chain.strength = strength
      chain
    }
    chainsWithStrengths
  }

  /*
	 * ortalama puanın üstündekileri al ortalama strength üstündekileri al
	 * “Strength Criterion”: Score(Chain) > Average(Scores) + 2 ∗
	 * StandardDeviation(Scores)
	 */

  def getStrongChains(chains: Seq[Chain]): Seq[Chain] = {
    info("Chain Scores Service get Strong Chains")
    val sumScoreOfChain = chains.foldLeft(0.0)(_ + _.score)
    val sumStrengthOfChain = chains.foldLeft(0.0)(_ + _.strength)
    val averageScoreOfChain = sumScoreOfChain / chains.size.toDouble
    val averageStrengthOfChain = sumStrengthOfChain / chains.size.toDouble

    info(f"Kelime zinciri ortalama puan değeri = $averageScoreOfChain")
    info(f"Kelime zinciri ortalama güç değeri = $averageStrengthOfChain")
    var temp = 0.0
    chains.foreach { chain =>
      temp = temp + (averageStrengthOfChain - chain.strength) * (averageStrengthOfChain - chain.strength)
    }
    val variance = temp / chains.size.toDouble
    val stddev = Math.sqrt(variance)
    val criterion = averageStrengthOfChain + 2.0 * stddev
    info(f"Kelime zinciri kriter değeri = $criterion")

    val strongChains = chains.filter(chain => chain.strength >= criterion)
    if(strongChains.isEmpty) {
      info("Kriter degerinin ustunde zincir bulunamadi, en guclu zincir alincak!")
      Seq(chains.maxBy(chain => chain.strength))
    } else {
      strongChains
    }
  }
}

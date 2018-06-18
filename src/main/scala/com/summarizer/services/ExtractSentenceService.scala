package com.summarizer.services

import com.google.inject.{ImplementedBy, Singleton}
import com.summarizer.domain.Chain
import com.twitter.inject.Logging

import scala.collection.immutable.ListMap
import util.control.Breaks._

@ImplementedBy(classOf[DefaultExtractSentenceService])
trait ExtractSentenceService extends Logging {

  def heuristic1(chains: Seq[Chain], sentences: Seq[String]): Seq[String]

  def getFrequencyOfWords(words: Seq[String]): Map[String,Int]

  def heuristic2(chains: Seq[Chain], sentences: Seq[String]): Seq[String]

}

@Singleton
class DefaultExtractSentenceService extends ExtractSentenceService with Logging {
  /*
   *
   * We investigated three alternatives for this step: For each chain in the
   * summary representation choose the sentence that contains the first
   * appearance of a chain member in the text.
   *
   * her zincirdeki ilk kelimenin geçtiği cümleyi al
   */

  def heuristic1(chains: Seq[Chain], sentences: Seq[String]): Seq[String] = {
    info("Extract Sentence Service heuristic 1")
    var sentencesIndexNo = Seq.empty[Int]
    chains.foreach { chain =>
      val lexicals = chain.members.map(_._1)
      val sentenceNo = lexicals.head.getSentenceNo()
      if(!sentencesIndexNo.contains(sentenceNo)) {
        sentencesIndexNo = sentencesIndexNo :+ sentenceNo
      } else {
        lexicals.foreach { lexical =>
          val sentenceNo = lexical.getSentenceNo()
          if(!sentencesIndexNo.contains(sentenceNo)) {
            sentencesIndexNo = sentencesIndexNo :+ sentenceNo
            break
          }
        }
      }
    }
    sentencesIndexNo = sentencesIndexNo.sorted
    val extractedSentences = sentencesIndexNo.map(index => sentences(index))
    extractedSentences
  }

  /*
 * We therefore defined a criterion to evaluate the appropriateness of a
 * chain member to represent its chain based on its frequency of occurrence
 * in the chain. We found experimentally that such words, call them
 * representative words, have a frequency in the chain noless than the
 * average word frequency in the chain.
 *
 * her zincirde kelimelerin frekansını belirle ortalama üstündeki kelimeleri
 * al -> temsilci kelimeler bu kelimeleri içeren ilk cümleyi al
 *
 */

  def getFrequencyOfWords(words: Seq[String]): Map[String,Int] = {
    val frequency = words.groupBy(identity).mapValues(_.size)
    frequency
  }

  def heuristic2(chains: Seq[Chain], sentences: Seq[String]): Seq[String] = {
    info("Extract Sentence Service heuristic algorithm 2")
    var selectedSentences = Map.empty[Int,String]
    var representativeWords = Set.empty[String]
    for(chain <- chains) {
      val words = chain.getWordsOfChain
      val frequencyOfWords = getFrequencyOfWords(words)
      val uniqueWords = words.distinct
      val meanOfFrequency =  words.size.toDouble / uniqueWords.size.toDouble
      // all words are same
      if(uniqueWords.size == 1) {
        val lexicals = chain.members.map(_._1)
        val sentenceNo = lexicals.head.getSentenceNo()
        val sentence = sentences(sentenceNo)
        selectedSentences += (sentenceNo -> sentence)
        representativeWords +: lexicals.head.getWord()
      }
      else {
        val wordsAboveMean = frequencyOfWords.filter((word) => word._2.toDouble >= meanOfFrequency).keySet
        val lexicals = chain.members.map(_._1).filter(lexical => wordsAboveMean.contains(lexical.getWord()))
        lexicals.foreach { lexical =>
          if(!representativeWords.contains(lexical.getWord())) {
            val sentenceNo = lexical.getSentenceNo()
            val sentence = sentences(sentenceNo)
            selectedSentences += (sentenceNo -> sentence)
            representativeWords += lexical.getWord()
          }
        }

      }
    }
    val sortedSelectedSentences = ListMap(selectedSentences.toSeq.sortBy(_._1):_*)
    val result = sortedSelectedSentences.values.toSeq
    result
  }
}

package com.summarizer.services

import com.google.inject.{ImplementedBy, Singleton}
import com.summarizer.domain.Chain
import com.twitter.inject.Logging

import scala.collection.immutable.ListMap
import util.control.Breaks._

@ImplementedBy(classOf[DefaultExtractSentenceService])
trait ExtractSentenceService extends Logging {

  def heuristic1(chains: Seq[Chain], sentences: Seq[String]): Seq[String]

  def getFrequencyOfWords(words: Seq[String]): Map[String, Int]

  def heuristic2(chains: Seq[Chain],
                 paragraphsAndSentences: Map[Int, Seq[String]],
                 paragraphsAndSentencesWithoutHelperWords: Map[Int, Seq[String]]): Seq[String]

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
      if (!sentencesIndexNo.contains(sentenceNo)) {
        sentencesIndexNo = sentencesIndexNo :+ sentenceNo
      } else {
        lexicals.foreach { lexical =>
          val sentenceNo = lexical.getSentenceNo()
          if (!sentencesIndexNo.contains(sentenceNo)) {
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
 * first check the chains length ,
 * if we have only one chain then check how many unique words includes the chain, if all lexicals of chain are same,
 * then get first two lexicals sentence no and fetch them
 * else get the unique words and fetch sentences with their sentence no
 *
 * if we have more than one chain:
 * go to for loop
 * check if the chain has same lexicals only, then get one sentence from that chain, which is not selected yet
 * if chain has different lexicals then get the frequency of words, use lexicals above frequency for fetching sentences
 *
 */

  def getFrequencyOfWords(words: Seq[String]): Map[String, Int] = {
    val frequency = words.groupBy(identity).mapValues(_.size)
    frequency
  }

  def heuristic2(chains: Seq[Chain],
                 paragraphsAndSentences: Map[Int, Seq[String]],
                 paragraphsAndSentencesWithoutHelperWords: Map[Int, Seq[String]]): Seq[String] = {
    info("Extract Sentence Service heuristic algorithm 2")
    var selectedSentences = Map.empty[(Int, Int), String]
    var representativeWords = Set.empty[String]

    if (chains.length == 1) {
      val chain = chains.head
      val words = chain.getWordsOfChain
      val uniqueWords = words.distinct
      if (uniqueWords.size == 1) {
        val lexicals = chain.members.map(_._1)
        val values = lexicals.map { lexical =>
          (lexical.getParagraphNo(),lexical.getSentenceNo())
        }.distinct

        val (firstSelectedParagraphNo,firstSelectedSentenceNo) = values.head
        val sentences =  paragraphsAndSentences(firstSelectedParagraphNo)
        selectedSentences += ((firstSelectedParagraphNo,firstSelectedSentenceNo) -> sentences(firstSelectedSentenceNo))
        if (values.size > 1) {
          val (secondSelectedParagraphNo,secondSelectedSentenceNo) = values(1)
          val sentences =  paragraphsAndSentences(secondSelectedParagraphNo)
          selectedSentences += ((secondSelectedParagraphNo,secondSelectedSentenceNo) -> sentences(secondSelectedSentenceNo))
        }
      } else {
        uniqueWords.foreach { word =>
          val lexicals = chain.members.map(_._1).filter(_.getWord() == word)
          var sentenceNotAdded = true
          for (lexical <- lexicals; if sentenceNotAdded) {
            val (paragraphNo,sentenceNo) = (lexical.getParagraphNo(), lexical.getSentenceNo())
            representativeWords += lexical.getWord()
            if (!selectedSentences.contains((paragraphNo, sentenceNo))) {
              val sentences =  paragraphsAndSentences(paragraphNo)
              selectedSentences += ((paragraphNo,sentenceNo) -> sentences(sentenceNo))
              sentenceNotAdded = false
            }
          }
        }
      }

    } else {
      for (chain <- chains) {
        val words = chain.getWordsOfChain
        val uniqueWords = words.distinct
        if (uniqueWords.size == 1) {
          val lexical = chain.members.map(_._1).head
          val (paragraphNo, sentenceNo) = (lexical.getParagraphNo(), lexical.getSentenceNo())
          val sentences = paragraphsAndSentences(paragraphNo)
          if (!selectedSentences.contains((paragraphNo, sentenceNo))) {
            selectedSentences += ((paragraphNo, sentenceNo) -> sentences(sentenceNo))
          }
        } else {
          val frequencyOfWords = getFrequencyOfWords(words)
          val meanOfFrequency = words.size.toDouble / uniqueWords.size.toDouble
          val wordsAboveMean = frequencyOfWords.filter((word) => word._2.toDouble >= meanOfFrequency).keySet
          val lexicals = chain.members.map(_._1).filter(lexical => wordsAboveMean.contains(lexical.getWord()))
          var sentenceNotAdded = true
          for (lexical <- lexicals; if sentenceNotAdded) {
            val (paragraphNo, sentenceNo) = (lexical.getParagraphNo(), lexical.getSentenceNo())
            representativeWords += lexical.getWord()
            if (!selectedSentences.contains((paragraphNo, sentenceNo))) {
              val sentences = paragraphsAndSentences(paragraphNo)
              selectedSentences += ((paragraphNo, sentenceNo) -> sentences(sentenceNo))
              sentenceNotAdded = false
            }
          }
        }
      }
    }
    val sortedSelectedSentences = ListMap(selectedSentences.toSeq.sortBy(_._1): _*)
    val replaceSelectedSenteces = sortedSelectedSentences.map { info =>
      val sentencesWithoutHelperWords = paragraphsAndSentencesWithoutHelperWords(info._1._1)
      val sentence = sentencesWithoutHelperWords(info._1._2)
      if (sentence.matches(".*\\p{Punct}")) {
        sentence
      } else {
        sentence + "\n"
      }
    }.toSeq
    replaceSelectedSenteces
  }
}

package com.summarizer.services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import com.summarizer.domain.Lexical
import com.summarizer.modules.TurkishLanguageToolsModule
import com.twitter.inject.Logging

@ImplementedBy(classOf[DefaultPreProcessService])
trait PreProcessService {

  def cleanStopWords(text: String): String

  def getAllLexicals(paragraphsAndSentences: Map[Int, Seq[String]]): Seq[Lexical]

  def parseTextToSentencesAndParagraphs(text: String) : Map[Int,Seq[String]]

}

@Singleton
class DefaultPreProcessService @Inject()(nounService: NounService,
                                         sentenceService: SentenceService,
                                         paragraphService: ParagraphService) extends PreProcessService with Logging {

  def cleanStopWords(text: String): String = {
    val stopWords = TurkishLanguageToolsModule.getStopWordList
    stopWords.foldLeft(text)((a, b) => a.replaceAllLiterally(" " + b + " ", " ")).toString
  }

  def getAllLexicals(paragraphsAndSentences: Map[Int, Seq[String]]): Seq[Lexical] = {
    info("Pre Process Service get paragraphs and sentences")
    val lexicals = paragraphsAndSentences.flatMap { case (paragraphNo, sentences) =>
      sentences.zipWithIndex.flatMap { case (sentence, sentenceIndexNo) =>
        val words = nounService.getNounsForSummary(sentence)
        words.map(new Lexical(_, sentenceIndexNo, paragraphNo))
      }
    }
    lexicals.toSeq
  }

  def parseTextToSentencesAndParagraphs(text: String): Map[Int, Seq[String]] = {
    info("Pre Process Service parse text to paragraphs and sentences")
    var paragraphsAndSentences = Map.empty[Int, Seq[String]]
    val paragraphs = paragraphService.getParagraphs(text)
    paragraphs.zipWithIndex.foreach { case (paragraph, paragraphIndexNo) =>
      val sentences = sentenceService.getSentences(paragraph)
      paragraphsAndSentences += (paragraphIndexNo -> sentences)
    }
    paragraphsAndSentences
  }

}
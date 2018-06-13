package com.summarizer.services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import com.summarizer.domain.Lexical
import com.summarizer.modules.TurkishLanguageToolsModule
import com.twitter.inject.Logging

@ImplementedBy(classOf[DefaultPreProcessService])
trait PreProcessService {

  def cleanStopWords(text: String): String

  def getAllLexicals(text: String) : Seq[Lexical]

}

@Singleton
class DefaultPreProcessService @Inject()(nounService: NounService,
                                         sentenceService: SentenceService,
                                         paragraphService: ParagraphService) extends PreProcessService with Logging {

  def cleanStopWords(text: String): String = {
    val stopWords = TurkishLanguageToolsModule.getStopWordList
    stopWords.foldLeft(text)((a, b) => a.replaceAllLiterally(" " + b + " ", " ")).toString
  }

  def getAllLexicals(text: String): Seq[Lexical] = {
    info("Pre Process Service get all lexicals")
    val paragraphs = paragraphService.getParagraphs(text)
    val lexicals = paragraphs.zipWithIndex.flatMap { case (paragraph, paragraphIndexNo) =>
      val sentences = sentenceService.getSentences(paragraph)
      sentences.zipWithIndex.flatMap { case (sentence, sentenceIndexNo) =>
        val words = nounService.getNounsForSummary(sentence)
        words.map(new Lexical(_, sentenceIndexNo, paragraphIndexNo))
      }
    }

    lexicals
  }
}
package com.summarizer.services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import com.summarizer.modules.TurkishParserModule
import zemberek.tokenization.TurkishSentenceExtractor

import scala.collection.JavaConverters._

@ImplementedBy(classOf[DefaultSentenceService])
trait SentenceService {

  def getSentences(text: String): Seq[String]
  def getTitle(sentences: Seq[String]): String
}

@Singleton
class DefaultSentenceService extends SentenceService {

  val sentenceExtractor = TurkishParserModule.sentenceExtractor

  def getSentences(text: String): Seq[String] = {
    sentenceExtractor.fromParagraph(text).asScala.toList
  }

  //ilk cümleyi alır ve başlık sonunda bir noktalama işareti olmadığı için,
  // satırlara böler, ilk satırı başlık olarak geri dönderir
  def getTitle(sentences: Seq[String]): String = {
    sentences.head.split("\\r?\\n").head
  }
}
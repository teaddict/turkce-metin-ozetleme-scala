package com.summarizer.services

import com.google.inject.{ImplementedBy, Singleton}

@ImplementedBy(classOf[DefaultParagraphService])
trait ParagraphService {

  def getParagraphs(text: String): Seq[String]
}

@Singleton
class DefaultParagraphService extends ParagraphService {

  def getParagraphs(text: String): Seq[String] = {
    val delimiter = "\n\n"
    val paragraphs = text.split(delimiter)

    paragraphs.filter(paragraph => paragraph.length > 1)
  }

}
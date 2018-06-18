package com.summarizer.services

import com.google.inject.{ImplementedBy, Singleton}
import com.twitter.inject.Logging

@ImplementedBy(classOf[DefaultSemanticListParserService])
trait SemanticListParserService extends Logging {

  def createWordList(file: Seq[String]): Seq[String]

  def createRelationList(words: Seq[String], file: Seq[String]): Map[String,Seq[String]]

}

@Singleton
class DefaultSemanticListParserService extends SemanticListParserService {

  def createWordList(file: Seq[String]): Seq[String] = {
    val words = file.map(_.split(":").head).distinct
    words
  }

  def createRelationList(words: Seq[String], file: Seq[String]): Map[String,Seq[String]] = {
    var relations: Seq[String] = Seq.empty[String]
    var relationMap: Map[String,Seq[String]] = Map.empty[String,Seq[String]]

    var word = file.head.split(":")(0)
    relations = relations :+ file.head

    for( index <- 1 until file.size) {

      val previousWord = file(index-1).split(":")(0)
      word = file(index).split(":")(0)
      if(word == previousWord) {
        relations = relations :+ file(index)
      } else {
        relationMap += (previousWord -> relations)
        relations = Seq.empty[String]
        relations = relations :+ file(index)
      }
    }
    relationMap += (word -> relations)
    relationMap
  }

}

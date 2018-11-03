package com.summarizer.modules

import com.google.inject.{Provides, Singleton}
import com.summarizer.services.{DefaultFileService, DefaultSemanticListParserService}
import com.twitter.inject.TwitterModule

@Singleton
@Provides
object TurkishLanguageToolsModule extends TwitterModule {
  private val fileService = new DefaultFileService
  private val semanticListParserService = new DefaultSemanticListParserService

  private val semanticRelationWordList = {
    val resourcesPath = "lexical/all_relations.csv"
    val file = fileService.readFile(resourcesPath)
    val parsedFile = file.split("\n").toSeq
    val words = semanticListParserService.createWordList(parsedFile)
    semanticListParserService.createRelationList(words, parsedFile)
  }

  private val stopWordList = {
    val resourcesPath = "lexical/stopwords.csv"
    val file = fileService.readFile(resourcesPath)
    file.split("\n").toSeq
  }

  def getSemanticRelationWordList : Map[String, Seq[String]] = semanticRelationWordList

  def getStopWordList :  Seq[String] = stopWordList
}
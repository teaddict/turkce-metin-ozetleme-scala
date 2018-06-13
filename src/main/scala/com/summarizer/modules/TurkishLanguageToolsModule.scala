package com.summarizer.modules

import com.google.inject.{Provides, Singleton}
import com.summarizer.services.{DefaultFileService, DefaultSemanticListParserService}
import com.twitter.inject.TwitterModule

@Singleton
@Provides
object TurkishLanguageToolsModule extends TwitterModule {
  private val fileService = new DefaultFileService
  private val semanticListParserService = new DefaultSemanticListParserService

  def getSemanticRelationWordList = {
    val resourcesPath = "lexical/all_relations.csv"
    val file = fileService.readFile(resourcesPath)
    val parsedFile = file.split("\n").toSeq
    val words = semanticListParserService.createWordList(parsedFile)
    val semanticWordList = semanticListParserService.createRelationList(words, parsedFile)
    semanticWordList
  }

  def getStopWordList = {
    val resourcesPath = "lexical/stopwords.csv"
    val file = fileService.readFile(resourcesPath)
    val stopWords = file.split("\n").toSeq
    stopWords
  }
}
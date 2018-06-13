package com.summarizer.services

import com.google.inject.testing.fieldbinder.Bind
import com.summarizer.repositories.SummaryRepository
import org.scalatest.FunSpec
import com.twitter.inject.Mockito
import com.twitter.util.{Await, Future}
import org.scalatest.Matchers._

class SummaryServiceTest extends FunSpec with CommonServices with Mockito {

  describe("Get summary") {
    @Bind val summaryRepository = mock[SummaryRepository]
    summaryRepository.findByContextOfText(anyString) returns Future(None)

    val lexicalChainService = new DefaultLexicalChainService
    val chainScoresService = new DefaultChainScoresService
    val extractSentenceService = new DefaultExtractSentenceService
    val summaryService = new DefaultSummaryService(summaryRepository, preProcessService, nounService, lexicalChainService, chainScoresService, extractSentenceService, sentenceService)
    val fileService = new DefaultFileService
    it("should create summary") {
      val fileResourcesPath = "text/1.txt"
      val file = fileService.readFile(fileResourcesPath)
      val summaryResourcesPath = "text/summary1.txt"
      val expected = fileService.readFile(summaryResourcesPath)
      val summary = summaryService.create(file)
      val result = Await.result(summary).right.get.summaryOfText
      result shouldBe Some(expected)
    }
  }
}

package com.summarizer.services

import com.summarizer.domain.Summary
import com.summarizer.repositories.SummaryRepository
import com.google.inject.{ImplementedBy, Inject, Singleton}
import com.twitter.util.Future
import com.twitter.inject.Logging

@ImplementedBy(classOf[DefaultSummaryService])
trait SummaryService {
  def create(contextOfText: String): Future[Either[String, Summary]]
}

@Singleton
class DefaultSummaryService @Inject()(summaryRepository: SummaryRepository,
                                      preProcessService: PreProcessService,
                                      nounService: NounService,
                                      lexicalChainService: LexicalChainService,
                                      chainScoresService: ChainScoresService,
                                      extractSentenceService: ExtractSentenceService,
                                      sentenceService: SentenceService) extends SummaryService with Logging {

  override def create(contextOfText: String): Future[Either[String, Summary]] = {
    info("Summary service create")
    try {
      val paragraphsAndSentences = preProcessService.parseTextToSentencesAndParagraphs(contextOfText)
      val lexicals = preProcessService.getAllLexicals(paragraphsAndSentences)
      val chains = lexicalChainService.buildChains(lexicals)
      if (chains.isEmpty) {
        Future(Left("Summarizer can't create summary!"))
      } else {
        val uniqueChains = lexicalChainService.chainAnalyse(chains)
        val chainsWithScores = chainScoresService.calculateChainScores(uniqueChains)
        val chainsWithStrengths = chainScoresService.calculateChainStrengths(chainsWithScores)
        val strongChains = chainScoresService.getStrongChains(chainsWithStrengths)
        val extractedSentences = extractSentenceService.heuristic2(strongChains, paragraphsAndSentences)
        val summaryOfText = extractedSentences.mkString(" ")
        val summary = new Summary(contextOfText = contextOfText,
          summaryOfText = Some(summaryOfText),
          wordChain = Some(strongChains.flatMap(_.getChainInformation).mkString))
        info(s"Summary = $summaryOfText")
        Future(Right(summary))
      }
    }
    catch {
      case e: Throwable => Future(Left(e.getMessage))
    }

  }
}
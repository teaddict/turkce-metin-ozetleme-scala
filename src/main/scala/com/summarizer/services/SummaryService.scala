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
    summaryRepository.findByContextOfText(contextOfText).flatMap {
      case Some(result) => Future(Right(result))
      case None =>
        try {
          val sentences = sentenceService.getSentences(contextOfText)
          val lexicals = preProcessService.getAllLexicals(contextOfText)
          val chains = lexicalChainService.buildChains(lexicals)
          if(chains.isEmpty) {
            Future(Left("Summarizer can't create summary!"))
          } else {
            val uniqueChains = lexicalChainService.chainAnalyse(chains)
            val chainsWithScores = chainScoresService.calculateChainScores(uniqueChains)
            val chainsWithStrengths = chainScoresService.calculateChainStrengths(chainsWithScores)
            val strongChains = chainScoresService.getStrongChains(chainsWithStrengths)
            println(strongChains.size)
            strongChains.foreach(chain => println(chain.getChainInformation))
            val extractedSentences = extractSentenceService.heuristic2(strongChains, sentences)
            val summary = new Summary(contextOfText = contextOfText,
              summaryOfText = Some(extractedSentences.mkString),
              wordChain = Some(strongChains.flatMap(_.getChainInformation).mkString))
            summaryRepository.save(summary)
            Future(Right(summary))
          }
        }
        catch {
          case e: Throwable => Future(Left(e.getMessage))
        }
    }
  }
}
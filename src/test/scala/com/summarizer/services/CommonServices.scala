package com.summarizer.services

import zemberek.tokenization.TurkishTokenizer

trait CommonServices {
  val tokenizer = TurkishTokenizer.DEFAULT
  val sentenceService = new DefaultSentenceService
  val paragraphService = new DefaultParagraphService
  val nounService = new DefaultNounService(sentenceService, paragraphService)
  val verbService = new DefaultVerbService(sentenceService, paragraphService)
  val preProcessService = new DefaultPreProcessService(nounService,sentenceService,paragraphService)
}

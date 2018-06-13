package com.summarizer.modules

import com.summarizer.services._
import com.twitter.inject.TwitterModule

object SummaryModule extends TwitterModule{
  override def configure {
    bind[ChainScoresService].to[DefaultChainScoresService]
    bind[ExtractSentenceService].to[DefaultExtractSentenceService]
    bind[FileService].to[DefaultFileService]
    bind[LexicalChainService].to[DefaultLexicalChainService]
    bind[NounService].to[DefaultNounService]
    bind[ParagraphService].to[DefaultParagraphService]
    bind[PreProcessService].to[DefaultPreProcessService]
    bind[SemanticListParserService].to[DefaultSemanticListParserService]
    bind[SentenceService].to[DefaultSentenceService]
    bind[VerbService].to[DefaultVerbService]
  }
}

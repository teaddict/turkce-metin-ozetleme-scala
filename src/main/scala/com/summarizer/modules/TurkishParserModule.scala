package com.summarizer.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import zemberek.morphology.TurkishMorphology
import zemberek.tokenization.{TurkishSentenceExtractor, TurkishTokenizer}

//tek bir tane olusturmamiz gerek cunku cok zaman aliyor yaklasik 1,5 sn
//server icin iyi degil , bu yuzden bu sekilde hizlandirildi
@Singleton
@Provides
object TurkishParserModule extends TwitterModule {

  val getMorphology: TurkishMorphology = TurkishMorphology.createWithDefaults()

  val getTokenizer : TurkishTokenizer = TurkishTokenizer.DEFAULT

  val sentenceExtractor: TurkishSentenceExtractor = TurkishSentenceExtractor.DEFAULT

}
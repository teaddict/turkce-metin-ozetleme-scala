package com.summarizer.services

import com.google.inject.{Inject, Singleton}
import zemberek.morphology.TurkishMorphology
import zemberek.tokenization.TurkishTokenizer

//tek bir tane olusturmamiz gerek cunku cok zaman aliyor yaklasik 1,5 sn
//server icin iyi degil , bu yuzden bu sekilde hizlandirildi
@Singleton
class TurkishParser @Inject()(turkishMorphology: TurkishMorphology,
                              turkishTokenizer:  TurkishTokenizer)
{
    //parser ı burda oluşturmayıp fonksiyon içinde oluşturursak çok zaman alıyor!
    def getMorphology: TurkishMorphology = turkishMorphology
    def getTokenizer : TurkishTokenizer = turkishTokenizer
}
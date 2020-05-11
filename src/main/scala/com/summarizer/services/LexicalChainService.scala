package com.summarizer.services

import com.google.inject.{ImplementedBy, Singleton}
import com.summarizer.domain.{Chain, Lexical}
import com.twitter.inject.Logging
import com.summarizer.modules.TurkishLanguageToolsModule

import scala.collection.mutable.Buffer

@ImplementedBy(classOf[DefaultLexicalChainService])
trait LexicalChainService extends Logging {

  def chainAnalyse(chains: Seq[Chain]) : Seq[Chain]

  def buildChains(lexicals: Seq[Lexical]): Seq[Chain]
}

@Singleton
class DefaultLexicalChainService extends LexicalChainService with Logging {

  /*
   * burada chain analiz edilecek ve içindekilere bakılcak mesela aynı kelimeye
   * ait ve hep aynı kelimeden oluşan birden fazla zincir olmaması gerek çünkü
   * farklı synonymler olduğu için aynı kelimenin birden fazla zinciri
   * olabiliyor hepsinin de boyutu aynı oluyor
   */

  def chainAnalyse(chains: Seq[Chain]) : Seq[Chain] = {
    info("Lexical Chain Service analyze chains")
    val uniqueChains = collection.mutable.Map.empty[String, Chain]
    chains.foreach { chain =>
      uniqueChains +=  (chain.getWordsOfChain.toString() -> chain)
    }

    uniqueChains.values.toSeq
  }

  /* tum lexicaller birer birer isleme alinacak
   * zincirimiz bos ise -> kelimemizi semantik iliski listesinde sorgulayarak, tum iliskilerini aliyoruz
   * ve zincirimize ekliyoruz
   * zincirimiz bos degil ise -> her bir zinciri inceliyoruz
   * zincirde ayni kelime var mi sorguluyoruz -> varsa zincire ayni iliski tipi ve iliskilenen sözcukle ekliyoruz
   * eger yoksa -> kelimemizi semantik iliski listesinde sorgulayarak, tum iliskilerini aliyoruz
   * mevcut zincir + tum iliskiler icin bir zincir olusturuyoruz
   */
  def buildChains(lexicals: Seq[Lexical]): Seq[Chain] = {
    info("Lexical Chain Service build chains")
    val mapOfChains = scala.collection.mutable.Map[String, Chain]()
    val wordnet = TurkishLanguageToolsModule.getSemanticRelationWordList
    lexicals.foreach { lexical =>
        wordnet.get(lexical.getWord()) match {
          case Some(semanticRelations) =>
            for (semanticRelation <- semanticRelations) {
              val result = semanticRelation.split(":")
              val relationType = result(1)
              val relatedWord = result(2)
              mapOfChains.get(relatedWord) match {
                case Some(chain) => chain.addLexicalToChain(lexical, relationType, relatedWord)
                case None => mapOfChains(relatedWord) = Chain(None, 0, 0.0, List((lexical, relationType, relatedWord)))
              }
            }
          case None =>
        }
      }
    mapOfChains.values.toSeq
  }
}

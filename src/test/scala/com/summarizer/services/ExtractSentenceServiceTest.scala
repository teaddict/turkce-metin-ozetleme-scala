package com.summarizer.services

import com.summarizer.domain.{Chain, Lexical}
import org.scalatest.FunSpec

class ExtractSentenceServiceTest extends FunSpec {
  private val extractSentenceService = new DefaultExtractSentenceService

  describe("Heuristic Algorithm 1") {
    // her zincirdeki ilk kelimenin geçtiği cümleyi al
    it("should return correct sentences") {
      val lexical1 = new Lexical("araba",1,1)
      val lexical2 = new Lexical("otobus",2,1)
      val lexical3 = new Lexical("araba",3,1)
      val members = Seq((lexical1,"hypernymy","taşıt"),
                        (lexical2,"hypernymy","taşıt"),
                        (lexical3,"hypernymy","taşıt"))
      val chain = new Chain(members = members)

      val lexical4 = new Lexical("çay",4,2)
      val lexical5 = new Lexical("kahve",5,2)
      val lexical6 = new Lexical("su",6,2)
      val members2 = Seq((lexical4,"hypernymy","içecek"),
                        (lexical5,"hypernymy","içecek"),
                        (lexical6,"hypernymy","içecek"))
      val secondChain = new Chain(members = members2)

      val sentences = Seq("Bazı insanlar kendi arabalarıyla yolculuk etmeyi sever.",
                          "Bazı insanlarsa otobüsle yolculuktan hoşlanır.",
                          "Arabayla yolculuk etmek insanlar için daha konforlu olabilir.",
                          "Çay, bir çok kişinin en vazgeçilmezidir.",
                          "Kahve işe yeni yeni alışkanlık haline gelmektedir.",
                          "Cay ve kahve, su yerine gecmez.")

      val result = extractSentenceService.heuristic1(Seq(chain,secondChain),sentences)
      val expectedResult = Seq("Bazı insanlar kendi arabalarıyla yolculuk etmeyi sever.",
                               "Çay, bir çok kişinin en vazgeçilmezidir.")

      result === expectedResult
    }

  }

  describe("Heuristic Algorithm 2") {

  }

  describe("Heuristic Algorithm 3") {

  }

}

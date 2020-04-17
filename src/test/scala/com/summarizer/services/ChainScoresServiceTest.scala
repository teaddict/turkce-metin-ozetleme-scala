package com.summarizer.services

import com.summarizer.domain.{Chain, Lexical}
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class ChainScoresServiceTest extends FlatSpec {

  val lexical1 = new Lexical("araba",1,1)
  val lexical2 = new Lexical("otobüs",1,2)
  val lexical3 = new Lexical("uçak",1,3)
  val lexical4 = new Lexical("araba",1,4)
  val lexical5 = new Lexical("otobüs",1,5)
  val members = List((lexical1,"hypernymy","taşıt"),
                    (lexical2,"hypernymy","taşıt"),
                    (lexical3,"hypernymy","taşıt"),
                    (lexical4,"hypernymy","taşıt"),
                    (lexical5,"hypernymy","taşıt"))
  val chain = Chain(members = members)

  val chainScoresService = new DefaultChainScoresService

  /*
    hypernymy = 4 points
    3 words have hypernymy relation = 3 * 4 = 12
 */
  it should "calculate Chain Scores correctly" in {
    val result = chainScoresService.calculateChainScores(Seq(chain))
    val expectedResult = 20
    result.head.score shouldBe expectedResult
  }

  it should "calculate Chain Strength correctly" in {
    val result = chainScoresService.calculateChainStrengths(Seq(chain))
    val expectedResult = 2.0
    result.head.strength shouldBe expectedResult
  }


  it should "get strong chain correctly" in {
    val lexical4 = new Lexical("derviş",1,1)
    val lexical5 = new Lexical("denetçi",1,2)
    val lexical6 = new Lexical("insan",1,3)
    val members2 = List((lexical4,"hypernymy","kişi"),
                       (lexical5,"hypernymy","kişi"),
                       (lexical6,"synonymy","kişi"))

    val chain1 = Chain(score = 20, strength =2.0, members = members)
    val chain2 = Chain(score = 18, members = members2)

    val result = chainScoresService.getStrongChains(Seq(chain1,chain2))
    result.head shouldBe chain1
  }

}

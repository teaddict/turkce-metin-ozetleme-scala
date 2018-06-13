package com.summarizer.services

import com.summarizer.domain.{Chain, Lexical}
import org.scalatest.FlatSpec

class ChainScoresServiceTest extends FlatSpec {

  val lexical1 = new Lexical("araba",1,1)
  val lexical2 = new Lexical("otobus",1,2)
  val lexical3 = new Lexical("araba",1,3)
  val members = Seq((lexical1,"hypernymy","taşıt"),
                    (lexical2,"hypernymy","taşıt"),
                    (lexical3,"hypernymy","taşıt"))
  val chain = new Chain(members = members)

  val chainScoresService = new DefaultChainScoresService

  /*
    hypernymy = 4 points
    3 words have hypernymy relation = 3 * 4 = 12
 */
  it should "calculate Chain Scores correctly" in {
    val result = chainScoresService.calculateChainScores(Seq(chain))
    val expectedResult = 12
    result.head.score === expectedResult
  }

  it should "calculate Chain Strength correctly" in {
    val result = chainScoresService.calculateChainStrengths(Seq(chain))
    val expectedResult = 0.99999999999
    result.head.strength === expectedResult
  }


  it should "get strong chain correctly" in {
    val lexical4 = new Lexical("derviş",1,1)
    val lexical5 = new Lexical("denetçi",1,2)
    val lexical6 = new Lexical("insan",1,3)
    val members2 = Seq((lexical4,"hypernymy","kişi"),
                       (lexical5,"hypernymy","kişi"),
                       (lexical6,"synonymy","kişi"))

    val chain1 = new Chain(score = 12, strength =0.99999999999, members = members)
    val chain2 = new Chain(score = 18, members = members2)

    val result = chainScoresService.getStrongChains(Seq(chain1,chain2))
    val expectedResult = Seq(chain1)
    result.head === expectedResult
  }

}

package com.summarizer.services

import com.summarizer.domain.{Chain, Lexical}
import org.scalatest.FunSpec

class LexicalChainServiceTest extends FunSpec {

  val lexicalChainService = new DefaultLexicalChainService
  describe("build chains") {
    it("should build chains") {

      val lexical1 = new Lexical("araba", 1, 1)
      val lexical2 = new Lexical("otobüs", 1, 2)
      val lexical3 = new Lexical("moto", 1, 3)
      val lexical4 = new Lexical("bisiklet", 1, 5)

      val chains = lexicalChainService.buildChains(Seq(lexical1, lexical2, lexical3, lexical4))
      val chain1 = Chain(None, 0, 0.0, Seq((lexical1, "synonymy", "otomobil")))
      val chain2 = Chain(None, 0, 0.0, Seq((lexical1, "synonymy", "tekerlekli")))
      val chain3 = Chain(None, 0, 0.0, Seq((lexical2, "holo_member", "araç filosu")))
      val chain4 = Chain(None, 0, 0.0, Seq((lexical2, "holo_member", "toplu taşıma aracı")))
      val chain5 = Chain(None, 0, 0.0, Seq((lexical2, "synonymy", "motorlu")))
      chain5.addLexicalToChain(lexical3, "synonymy", "motorlu")
      val chain6 = Chain(None, 0, 0.0, Seq((lexical1, "hypernymy", "taşıt")))
      chain6.addLexicalToChain(lexical4, "hypernymy", "taşıt")
      val chain7 = Chain(None, 0, 0.0, Seq((lexical4, "synonymy", "çiftteker")))

      assert(chains.size === 7)
      assert(chains.contains(chain1) === true)
      assert(chains.contains(chain2) === true)
      assert(chains.contains(chain3) === true)
      assert(chains.contains(chain4) === true)
      assert(chains.contains(chain5) === true)
      assert(chains.contains(chain6) === true)
      assert(chains.contains(chain7) === true)
    }
  }
}

package com.summarizer.domain
import org.scalatest.FunSpec

class LexicalTest extends FunSpec {

  describe("Get and Set Lexical") {
    it("should crete lexical correctly") {
      val newLexical = new Lexical("test",1,2)
      assert(newLexical.getWord() === "test")
      assert(newLexical.getSentenceNo() === 1)
      assert(newLexical.getParagraphNo() === 2)
    }
  }
}

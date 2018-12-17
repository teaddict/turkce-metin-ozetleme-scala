package com.summarizer.services

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class PreProcessServiceTest extends FunSpec with CommonServices {
  describe("Pre process text") {

    val paragraph1 = "Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.\n\n"
    val paragraph2 = "Yıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği ve 18 yaşının altındaki voleybolcuların katılabildiği bir şampiyonadır. İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir."

    it("should remove stop words") {
      val cleanText = preProcessService.cleanStopWords(paragraph2)
      val expected = "Yıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği 18 yaşının altındaki voleybolcuların katılabildiği şampiyonadır. İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her yılda düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir."

      cleanText shouldBe expected
    }

    it("should generate all lexicals") {
      val sentences = Seq("Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu.",
                          "Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.")
      val paragraphsAndSentences = Map( 0 -> sentences)
      val allLexicals = preProcessService.getAllLexicals(paragraphsAndSentences)
      val expectedLexicals = Seq("dünya", "yıldız", "kız", "voleybol", "şampiyona", "yıldız", "takım", "final", "maç", "çin", "şampiyon", "türkiye", "voleybol", "tarih", "dünya", "şampiyon", "el", "şampiyona", "yıl", "brezilya", "curitiba", "kent", "yıl", "şampiyona", "kıta", "ele", "ülke", "takım")
      allLexicals.size shouldBe 17
      allLexicals.zipWithIndex.foreach { case (lexical, lexicalIndexNo) =>
        lexical.getWord() shouldBe expectedLexicals(lexicalIndexNo)
      }
    }

  }
}

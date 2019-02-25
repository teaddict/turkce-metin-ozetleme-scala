package com.summarizer.services

import org.scalatest.FunSpec

class NounServiceTest extends FunSpec with CommonServices {

  val paragraph1 = "Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.\n\n"
  val paragraph2 = "İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir."

  describe("Get nouns") {
    it("should return all nouns") {
      val text = paragraph1.concat(paragraph2)
      val nouns = nounService.getNouns(text)
      val result = Seq("dünya", "yıldız", "kız", "voleybol", "şampiyona", "yıldız", "takım", "final", "maç", "çin", "şampiyon", "türkiye", "voleybol", "tarih", "dünya", "şampiyon", "el", "şampiyona", "yıl", "brezilya", "curitiba", "kent", "yıl", "şampiyona", "kıta", "ele", "ülke", "takım")

      assert(nouns === result)
    }

  }

}
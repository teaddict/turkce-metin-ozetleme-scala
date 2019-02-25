package com.summarizer.services

import org.scalatest.FunSpec

class VerbServiceTest extends FunSpec with CommonServices {

  val paragraph1 = "Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.\n\n"
  val paragraph2 = "İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir."

  describe("Get verbs") {
    it("should return all verbs") {
      val text = paragraph1.concat(paragraph2)
      val verbs = verbService.getVerbs(text)
      val result = Seq("yenmek", "olmak", "etmek", "yapmak", "düzenlemek", "elemek", "katılmak")

      assert(verbs === result)
    }

  }

}
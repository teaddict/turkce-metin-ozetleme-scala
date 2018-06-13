package com.summarizer.services
import org.scalatest.FunSpec

class ParagraphServiceTest extends FunSpec {
  val paragraphService = new DefaultParagraphService
  val title = "Yıldız Kızlarımız Dünya Şampiyonu\n\n"
  val paragraph1 = "Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.\n\n"
  val paragraph2 = "Yıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği ve 18 yaşının altındaki voleybolcuların katılabildiği bir şampiyonadır. İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir."

  describe("Get paragraphs") {
    it("should get paragraphs correctly") {
      val text = title.concat(paragraph1).concat(paragraph2)
      assert(paragraphService.getParagraphs(text).length === 3)
    }

    it("should remove empty paragraphs") {
      val emptyLines = "\n\n"
      val text = title.concat(paragraph1).concat(paragraph2).concat(emptyLines)
      assert(paragraphService.getParagraphs(text).length === 3)
    }
  }

}

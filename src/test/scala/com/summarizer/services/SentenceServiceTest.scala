package com.summarizer.services

import zemberek.tokenization.TurkishSentenceExtractor
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class SentenceServiceTest extends FlatSpec {
  val sentenceService = new DefaultSentenceService

  val title = "Yıldız Kızlarımız Dünya Şampiyonu\n\n"
  val paragraph1 = "Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.\n\n"
  val paragraph2 = "Yıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği ve 18 yaşının altındaki voleybolcuların katılabildiği bir şampiyonadır. İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir."

    it should "get sentences from paragraph correctly" in {
      val sentence1 = "Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu."
      val sentence2 = "Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti."

      val result = sentenceService.getSentences(paragraph1)
      result shouldBe Seq(sentence1,sentence2)
    }

    it should "get title" in {
      val textWithTitle = title.concat(paragraph1)
      val expectedTitle = title.replaceAll("\n","")
      val result = sentenceService.getTitle(Seq(textWithTitle))
      result shouldBe expectedTitle
    }


}

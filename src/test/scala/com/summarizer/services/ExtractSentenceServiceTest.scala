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
      val chain = Chain(members = members)

      val lexical4 = new Lexical("çay",4,2)
      val lexical5 = new Lexical("kahve",5,2)
      val lexical6 = new Lexical("su",6,2)
      val members2 = Seq((lexical4,"hypernymy","içecek"),
                        (lexical5,"hypernymy","içecek"),
                        (lexical6,"hypernymy","içecek"))
      val secondChain = Chain(members = members2)

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
    it("should return correct sentences") {
      val lexical1 = new Lexical("metin", 0, 0)
      val lexical2 = new Lexical("metin", 3, 0)
      val lexical3 = new Lexical("metin", 0, 1)
      val lexical4 = new Lexical("metin", 0, 1)
      val lexical5 = new Lexical("metin", 0, 1)
      val lexical6 = new Lexical("metin", 2, 1)
      val members = Seq((lexical1, "holo_part", "okunacak şey"), (lexical2, "holo_part", "okunacak şey"), (lexical3, "holo_part", "okunacak şey"),
        (lexical4, "holo_part", "okunacak şey"), (lexical5, "holo_part", "okunacak şey"), (lexical6, "holo_part", "okunacak şey"))
      val chain = Chain(members = members)
      val sentencesFirstParagraph = Seq("Otomatik metin özetleme uygulamamız üniversite yıllarında geliştirilmeye başlamış ve TÜBİTAK tarafından ödüllendirilmiştir.",
        "Açık kaynak yazılım olarak hala geliştirilmeye devam edilmektedir.",
        "Bu yüzden yapacağınız her geri bildirim bizim için çok önem arz ediyor.",
        "Uygulamayı kullanmak için internet bağlantınızın olması gerekiyor çünkü metin özetleme işlemi sunucumuzda yapılıyor.",
        "Bunun dışında herhangi başka bir gereksinime ihtiyaç duymuyor.")
      val sentencesSecondParagraph = Seq("Bu uygulamayla tartışma metinlerinizi, makalelerinizi, bilimsel metinlerinizi, tarih metinlerinizi ve analiz çalışmalarınızı özetlemenize ve analiz etmenize yardımcı olmak istiyoruz.",
        "Belgelerinizin önemli fikirlerini tanımlayan ve özetleyen bir eğitim aracıdır.",
        "Tek Tıklamayla özetleyin, ana fikre gidin ya da basitleştirin, böylece metinlerinizi hızlı bir şekilde yorumlayabilirsiniz.")
      var paragraphsAndSentences = Map(0 -> sentencesFirstParagraph, 1 -> sentencesSecondParagraph)

      val result = extractSentenceService.heuristic2(Seq(chain), paragraphsAndSentences)
      val expectedResult = Seq("Otomatik metin özetleme uygulamamız üniversite yıllarında geliştirilmeye başlamış ve TÜBİTAK tarafından ödüllendirilmiştir.",
        "Uygulamayı kullanmak için internet bağlantınızın olması gerekiyor çünkü metin özetleme işlemi sunucumuzda yapılıyor.")

      result === expectedResult

    }

  }

  describe("Heuristic Algorithm 3") {

  }

}

package com.summarizer.services

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class SemanticListParserServiceTest extends FunSpec {
  describe("Parse Semantic List") {
    val semanticListParserService = new DefaultSemanticListParserService
    val relationList = Seq( "abacı:hypernymy:kişi",
                            "abacı:hypernymy:köy",
                            "abacı:synonymy:asalak",
                            "abacılık:hypernymy:iş",
                            "abacılık:synonymy:keçecilik",
                            "abadan:hypernymy:köy",
                            "abadan:hypernymy:şehir",
                            "abadan:synonymy:bağışlayıcı",
                            "abajur:hypernymy:kâğıt",
                            "abajur:synonymy:lamba",
                            "abak:hypernymy:köy",
                            "abak:synonymy:iffetli",
                            "abak:synonymy:temiz",
                            "abakan:synonymy:alicenap",
                            "abakan:synonymy:cömert",
                            "abaküs:related_with:matematik",
                            "abaküs:synonymy:çörkü",
                            "abaküs:synonymy:sayı boncuğu",
                            "abaküs:yan_kavram:hesap makinesi",
                            "abalı:synonymy:güçsüz",
                            "abalı:synonymy:kimsesiz")

    it("should create set of words") {
      val result = semanticListParserService.createWordList(relationList)
      val expectedResult = Seq( "abacı",
                                "abacılık",
                                "abadan",
                                "abajur",
                                "abak",
                                "abakan",
                                "abaküs",
                                "abalı")

      result shouldBe expectedResult
    }

    it("should create map of words with their relations") {
      val words = semanticListParserService.createWordList(relationList)
      val result = semanticListParserService.createRelationList(words, relationList)
      val expectedResult = Map( "abajur" -> Seq("abajur:hypernymy:kâğıt", "abajur:synonymy:lamba"),
                                "abacı" -> Seq("abacı:hypernymy:kişi", "abacı:hypernymy:köy", "abacı:synonymy:asalak"),
                                "abaküs" -> Seq("abaküs:related_with:matematik", "abaküs:synonymy:çörkü", "abaküs:synonymy:sayı boncuğu", "abaküs:yan_kavram:hesap makinesi"),
                                "abak" -> Seq("abak:hypernymy:köy", "abak:synonymy:iffetli", "abak:synonymy:temiz"),
                                "abakan" -> Seq("abakan:synonymy:alicenap", "abakan:synonymy:cömert"),
                                "abacılık" -> Seq("abacılık:hypernymy:iş", "abacılık:synonymy:keçecilik"),
                                "abadan" -> Seq("abadan:hypernymy:köy", "abadan:hypernymy:şehir", "abadan:synonymy:bağışlayıcı"),
                                "abalı" -> Seq("abalı:synonymy:güçsüz", "abalı:synonymy:kimsesiz")
                              )

      result shouldBe expectedResult
    }

  }
}

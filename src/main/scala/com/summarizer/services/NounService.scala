package com.summarizer.services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import zemberek.morphology.analysis.{SentenceAnalysis, SingleAnalysis}

import scala.collection.JavaConverters._
import com.summarizer.modules.TurkishParserModule
import com.twitter.inject.Logging

@ImplementedBy(classOf[DefaultNounService])
trait NounService {

  def getAnalyses(text: String): Seq[SentenceAnalysis]

  def handleAnalyses(analysis: SentenceAnalysis) : Seq[String]

  def getNouns(text: String): Seq[String]

  def getNounsForSummary(text: String): Seq[String]

}

@Singleton
class DefaultNounService @Inject() (sentenceService: SentenceService,
                                    paragraphService: ParagraphService) extends NounService with Logging {

  val turkishMorphology = TurkishParserModule.getMorphology

  override def getAnalyses(text: String): Seq[SentenceAnalysis] = {
    val paragraphs = paragraphService.getParagraphs(text)
    val sentences = paragraphs.flatMap(paragraph => sentenceService.getSentences(paragraph))
    val analyses : Seq[SentenceAnalysis] = sentences.map { sentence =>
      val analysis = turkishMorphology.analyzeSentence(sentence)
      turkishMorphology.disambiguate(sentence, analysis)
    }
    analyses
  }

  override def handleAnalyses(analysis: SentenceAnalysis): Seq[String] = {
    var nouns : Seq[String] = Seq.empty[String]
    for(word : SingleAnalysis <- analysis.bestAnalysis().asScala) {
      if(word.formatLong().contains("Noun")) {
        nouns = nouns :+ word.getStem()
      }
    }
    nouns
  }

  override def getNouns(text: String): Seq[String] = {
    info("Noun Service get nouns")
    val analyses = getAnalyses(text)
    val nouns = analyses.flatMap(analysis => handleAnalyses(analysis))
    nouns
  }

  override def getNounsForSummary(text: String): Seq[String] = {
    info("Noun Service get nouns for summary")
    val wordAnalysis = turkishMorphology.analyzeSentence(text)
    val analysis = turkishMorphology.disambiguate(text, wordAnalysis)
    val nouns = handleAnalyses(analysis)
    nouns
  }
}

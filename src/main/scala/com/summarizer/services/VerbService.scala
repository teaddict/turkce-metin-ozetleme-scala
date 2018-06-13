package com.summarizer.services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import zemberek.morphology.analysis.{SentenceAnalysis, SingleAnalysis}
import com.summarizer.modules.TurkishParserModule
import com.twitter.inject.Logging

import scala.collection.JavaConverters._

@ImplementedBy(classOf[DefaultVerbService])
trait VerbService {

  def getAnalyses(text: String): Seq[SentenceAnalysis]

  def handleAnalyses(analysis: SentenceAnalysis) : Seq[String]

  def getVerbs(text: String): Seq[String]

}

@Singleton
class DefaultVerbService @Inject() (sentenceService: SentenceService,
                                    paragraphService: ParagraphService) extends VerbService with Logging {

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
    var verbs : Seq[String] = Seq.empty[String]
    for(word : SingleAnalysis <- analysis.bestAnalysis().asScala) {
      if(word.formatLong().contains("Verb")) {
        verbs = verbs :+ word.getDictionaryItem().lemma
      }
    }
    verbs
  }

  override def getVerbs(text: String): Seq[String] = {
    info("Verb Service get verbs")
    val analyses = getAnalyses(text)
    val verbs = analyses.flatMap(analysis => handleAnalyses(analysis))
    verbs
  }
}

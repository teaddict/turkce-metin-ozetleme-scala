package com.summarizer.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.summarizer.domain.http.{SummaryPostRequest, SummaryPostResponse}
import com.summarizer.services.{NounService, SummaryService, VerbService}
import com.summarizer.swagger.SummarySwaggerDocument
import com.github.xiaodongw.swagger.finatra.SwaggerSupport
import com.google.inject.{Inject, Singleton}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging

@Singleton
class SummaryController @Inject()(summaryService: SummaryService,
                                  nounService: NounService,
                                  verbService: VerbService) extends Controller with SwaggerSupport with Logging {
  implicit protected val swagger = SummarySwaggerDocument

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  post("/ozetle/api/new", swagger {
    _.summary("Create new summary")
      .tag("Summary")
      .bodyParam[SummaryPostRequest]("Summary object")
      .responseWith[SummaryPostResponse](200, "summary created")
  }) { post: Request =>
    info("summary post")
    val summaryPostRequest = mapper.readValue(post.getContentString(), classOf[SummaryPostRequest])
    summaryService.create(summaryPostRequest.toDomain).map {
      case Right(summary) =>
        info("summary created")
        SummaryPostResponse(summary.summaryOfText)
      case Left(error) => response.ok.body("couldn't create summary: " + error)
    }
  }

  post("/ozetle/api/noun/new", swagger {
    _.summary("Get all nouns")
      .tag("Nouns")
      .bodyParam[SummaryPostRequest]("Summary object")
      .responseWith[SummaryPostResponse](200, "summary created")
  }) { post: Request =>
    info("get nouns")
    val summaryPostRequest = mapper.readValue(post.getContentString(), classOf[SummaryPostRequest])
    val nouns = nounService.getNouns(summaryPostRequest.toDomain).mkString(",")
    info("nouns are ready")
    SummaryPostResponse(Some(nouns))
  }

  post("/ozetle/api/verb/new", swagger {
    _.summary("Get all verbs")
      .tag("Verbs")
      .bodyParam[SummaryPostRequest]("Summary object")
      .responseWith[SummaryPostResponse](200, "summary created")
  }) { post: Request =>
    info("get verbs")
    val summaryPostRequest = mapper.readValue(post.getContentString(), classOf[SummaryPostRequest])
    val verbs = verbService.getVerbs(summaryPostRequest.toDomain).mkString(",")
    info("verbs are ready")
    SummaryPostResponse(Some(verbs))
  }

}

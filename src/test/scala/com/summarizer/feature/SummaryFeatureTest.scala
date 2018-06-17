package com.summarizer.feature

import com.summarizer.SummaryServer
import com.summarizer.domain.Summary
import com.summarizer.services.SummaryService
import com.google.inject.testing.fieldbinder.Bind
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.finagle.http.Status._
import com.twitter.inject.server.FeatureTest
import com.twitter.inject.Mockito
import com.twitter.util.Future

class SummaryFeatureTest extends FeatureTest with Mockito {

  override val server = new EmbeddedHttpServer(new SummaryServer)
  val summary =  Summary(_id = "5ab95038231118493361db1f", contextOfText = "Summary Test", summaryOfText = Some("Test is Successful"))
  @Bind val summaryService = mock[SummaryService]

  "post /ozetle/api/new" should {
    "return summary of the text" in {
      summaryService.create(anyString) returns Future.value(Right(summary))

      server.httpPost(
        path = "/ozetle/api/new",
        postBody =
          """
            {
              "contextOfText" : "Summary Test"
            }
          """,
        withJsonBody =
        s"""{
              "result":"Test is Successful"
            }""",
        andExpect = Ok
      )
    }
  }
}

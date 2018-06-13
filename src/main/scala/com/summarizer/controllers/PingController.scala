package com.summarizer.controllers

import com.summarizer.domain.http.PingResponse
import com.summarizer.swagger.SummarySwaggerDocument
import com.github.xiaodongw.swagger.finatra.SwaggerSupport
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class PingController extends Controller with SwaggerSupport {

  implicit protected val swagger = SummarySwaggerDocument

  get("/ping", swagger {
    _.summary("Get response for ping")
      .tag("Ping")
      .responseWith[PingResponse](200, "The pong message")
  }) { request: Request =>
  	info("ping")
    PingResponse(s"pong")
  }
}

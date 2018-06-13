package com.summarizer.controllers


import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller


class DefaultController extends Controller {
  get("/") { request: Request =>
    response.movedPermanently.location("/api-docs/ui")
  }
}

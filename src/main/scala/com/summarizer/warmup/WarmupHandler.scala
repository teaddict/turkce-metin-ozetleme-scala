package com.summarizer.warmup


import com.google.inject.{Inject, Singleton}
import com.twitter.finatra.http.routing.HttpWarmup
import com.twitter.finatra.httpclient.RequestBuilder._
import com.twitter.inject.utils.Handler

@Singleton
class WarmupHandler @Inject()(httpWarmup: HttpWarmup) extends Handler {
  override def handle(): Unit = {
    httpWarmup.send(get("/ping"),times = 5)
  }
}

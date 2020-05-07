package com.summarizer

import com.summarizer.controllers._
import com.summarizer.modules.{CustomJacksonModule, SummaryModule, TurkishParserModule}
import com.summarizer.swagger.SummarySwaggerDocument
import com.summarizer.warmup.WarmupHandler
import com.github.xiaodongw.swagger.finatra.SwaggerController
import com.summarizer.domain.CorsFilter
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.inject.requestscope.FinagleRequestScopeFilter
import io.swagger.models.Info

object SummaryServerMain extends SummaryServer

class SummaryServer extends HttpServer {

  SummarySwaggerDocument.info(new Info()
    .description("Summary application API")
    .version("0.0.1")
    .title("Summary")
  )

  override def jacksonModule = CustomJacksonModule

  override def modules = Seq(SummaryModule, TurkishParserModule)

  override def defaultFinatraHttpPort = ":9999"

  override val disableAdminHttpServer: Boolean = true

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[FinagleRequestScopeFilter[Request,Response]]
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add(new SwaggerController(swagger = SummarySwaggerDocument))
      .add[PingController]
      .add[CorsFilter, SummaryController]
      .add[DefaultController]
  }

  override def warmup(): Unit = {
    run[WarmupHandler]()
  }

}

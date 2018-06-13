package com.summarizer.repositories

import com.google.inject.{Singleton}
import com.summarizer.domain.Summary
import com.summarizer.modules.DatabaseModule._
import org.mongodb.scala._
import com.twitter.util.{Future => TwitterFuture}
import com.scalaza.raven.future.Conversions._
import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.model.Filters._

@Singleton
class SummaryRepository {
  private val summaryCollection = provideSummaryCollection()

  def findByContextOfText(contextOfText: String): TwitterFuture[Option[Summary]] =
    summaryCollection
      .find(equal("contextOfText", contextOfText))
      .toFuture()
      .map(_.headOption)
      .toTwitterFuture

  def save(summary: Summary): TwitterFuture[String] =
    summaryCollection
      .insertOne(summary)
      .toFuture()
      .map(_ => summary._id)
      .toTwitterFuture
}
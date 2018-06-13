package com.summarizer.modules

import com.summarizer.domain.Summary
import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import com.typesafe.config.ConfigFactory

@Singleton
@Provides
object DatabaseModule extends TwitterModule {
  val config = ConfigFactory.load()
  val MONGODB_URI = config.getString("mongo.uri")
  val MONGODB_DB_NAME = config.getString("mongo.database")
  val MONGODB_COLLECTION_SUMMARY = "summary"

  lazy val mongoClient: MongoClient = MongoClient(MONGODB_URI)
  lazy val codecRegistry = fromRegistries(fromProviders(classOf[Summary]), DEFAULT_CODEC_REGISTRY)
  lazy val database: MongoDatabase = mongoClient.getDatabase(MONGODB_DB_NAME).withCodecRegistry(codecRegistry)

  def provideSummaryCollection(): MongoCollection[Summary] = database.getCollection[Summary](MONGODB_COLLECTION_SUMMARY)
}
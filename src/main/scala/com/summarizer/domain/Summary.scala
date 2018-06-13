package com.summarizer.domain

import org.json4s.DefaultFormats
import org.mongodb.scala.bson.ObjectId

case class Summary(_id: String = new ObjectId().toHexString,
                   contextOfText: String,
                   summaryOfText: Option[String] = None,
                   wordChain: Option[String] = None,
                   filename: Option[String] = None)

object Summary {

  implicit val formats = new DefaultFormats {}

}

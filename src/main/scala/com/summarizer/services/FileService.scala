package com.summarizer.services
import com.twitter.inject.Logging

import scala.io.Source._
import java.io._

import com.google.inject.{ImplementedBy, Singleton}

import scala.io.{Codec, Source}

@ImplementedBy(classOf[DefaultFileService])
trait FileService extends Logging {

  def readFile(path: String): String

  def writeFile(path: String, file: String)

}

@Singleton
class DefaultFileService extends FileService {

  def readFile(path: String): String = {
    val source = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(path))(Codec.UTF8)
    val text = try source.mkString finally source.close()
    text
  }

  def writeFile(path: String, file: String) = {
    val writer = new PrintWriter(new File(path))
    writer.write(file)
    writer.close()
  }

}

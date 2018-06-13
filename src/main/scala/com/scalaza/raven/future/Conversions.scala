package com.scalaza.raven.future

import com.scalaza.raven.future.Converter._
import com.twitter.{util => twitter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

object Conversions {

  implicit class ScalaToTwitterFuture[T](f: Future[T]) {
    def toTwitterFuture: twitter.Future[T] = f
  }

  implicit class TwitterToScalaFuture[T](f: twitter.Future[T]) {
    def toScalaFuture: Future[T] = f
  }

  implicit class TwitterFutureFlatten[T](f: twitter.Future[twitter.Future[T]]) {
    def flatten(): twitter.Future[T] = f.flatMap(x => x)
  }

}
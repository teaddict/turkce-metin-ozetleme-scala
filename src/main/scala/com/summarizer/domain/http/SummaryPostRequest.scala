package com.summarizer.domain.http

import com.twitter.finatra.validation.NotEmpty

case class SummaryPostRequest(@NotEmpty contextOfText: String) {
  def toDomain = this.contextOfText
}

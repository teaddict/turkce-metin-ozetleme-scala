package com.summarizer.domain

class Lexical(word: String, sentenceNo: Int, paragraphNo: Int) {

  def getWord(): String = {
    word
  }

  def getSentenceNo(): Int = {
    sentenceNo
  }

  def getParagraphNo(): Int = {
    paragraphNo
  }

  def printLexical(): Unit = {
    println("Word: " + this.word)
    println("Sentence No: " + this.sentenceNo)
    println("Paragraph No: " + this.paragraphNo)
  }
}
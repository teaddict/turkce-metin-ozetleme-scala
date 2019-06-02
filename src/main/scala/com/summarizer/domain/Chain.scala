package com.summarizer.domain

case class Chain (var subjectOfChain : Option[String] = None,
             var score : Int = 0,
             var strength : Double = 0.0,
             var members : List[(Lexical, String, String)]) { //lexical, relationType, relatedWord

  def addLexicalToChain(lexical: Lexical, relationType: String, relatedWord: String) = {
    this.members = this.members :+ (lexical, relationType, relatedWord)
  }

  def getWordsOfChain: Seq[String] = {
    val words = this.members.map(_._1.getWord())
    words
  }

  def getRelationTypesOfChain: Seq[String] = {
    this.members.map(_._2)
  }

  def getRelatedWordsOfChain: Seq[String] = {
    this.members.map(_._3)
  }

  def getMember(lexical: Lexical): Option[(Lexical, String, String)] = {
    this.members.find(_._1.getWord() == lexical.getWord())
  }

  def getMembers: List[(Lexical, String, String)] = {
    this.members
  }

  def getParagraphsOfChain: Seq[Int] = {
    val paragraphs = this.members.map(_._1).map(_.getParagraphNo())
    paragraphs
  }

  def getSentencesOfChain(paragraphNo: Int): Seq[Int] = {
    val sentences = this.members.map(_._1).filter(_.getParagraphNo() == paragraphNo).map(_.getSentenceNo())
    sentences
  }

  def printChain() = {
    for((lexical, relationType, relatedWord) <- this.members) {
      println(lexical.getWord())
      println(relationType)
      println(relatedWord)
    }

    println("Lexical size: " + this.members.size)
    println("Score: " + this.score)
    println("Strength: " + this.strength)

  }

  def getChainInformation: String = {
    var chain = ""

    for((lexical, relationType, relatedWord) <- this.members) {
      chain += "(" + lexical.getWord() + " "
      chain += relationType + " "
      chain += relatedWord + ") "
      chain += "P" + lexical.getParagraphNo() + "-S" + lexical.getSentenceNo() + ","
    }

    chain += ":" + this.members.size
    chain += ":" + this.score
    chain += ":" + this.strength + "\n"
    chain
  }

}
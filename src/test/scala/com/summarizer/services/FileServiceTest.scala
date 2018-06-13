package com.summarizer.services

import org.scalatest.FunSpec
import org.scalatest.Matchers._

class FileServiceTest extends FunSpec {

  def randomString(length: Int) = {
    val r = new scala.util.Random
    val sb = new StringBuilder
    for (i <- 1 to length) {
      sb.append(r.nextPrintableChar)
    }
    sb.toString
  }

  describe("Read and write file") {
    val fileService = new DefaultFileService
    val file = randomString(15)

    it("should write file successfully") {
      val resourcesPath = getClass.getResource("/lexical/test.csv").getPath
      fileService.writeFile(resourcesPath,file)
    }

    it("should read file successfully") {
      val resourcesPath = "lexical/test.csv"
      val result = fileService.readFile(resourcesPath)
      val expectedResult = file
      result shouldBe expectedResult
    }

  }
}

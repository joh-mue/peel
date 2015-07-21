package eu.stratosphere.peel.core.results.etl.extractors

import java.io.File

class TextEventSourceReader private(override val file: File) extends EventSourceReader {

  override def handle(line: String): Msg = StringMsg(line)
}

object TextEventSourceReader {
  def apply(file: File) = {
    new TextEventSourceReader(file)
  }
}

package eu.stratosphere.peel.core.results.etl.extractors

import java.io.{BufferedReader, File}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.slf4j.LoggerFactory

/**
 * Created by alexander on 30.07.15.
 */
trait EventSourceReader extends Traversable[Msg] { //Iterator[A] {

  val file: File

  final val logger = LoggerFactory.getLogger(this.getClass)

  override def foreach[U](f: (Msg) => U): Unit = {
    var reader: BufferedReader = null
    try {
      reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath), StandardCharsets.UTF_8)
      while (reader.ready()) {
        f(handle(reader.readLine()))
      }
      f(EOF)

      reader.close()
      logger.debug(s"Reader ($file) done.")
    } catch {
      case e: Throwable => logger.error("Failed to read file $file", e)
    } finally {
      if (reader != null)
        reader.close()
    }
  }

  def handle(line: String): Msg
}

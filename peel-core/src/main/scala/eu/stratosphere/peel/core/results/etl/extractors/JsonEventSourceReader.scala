package eu.stratosphere.peel.core.results.etl.extractors

import java.io.File

import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s.native.JsonMethods._

class JsonEventSourceReader private(override val file: File) extends EventSourceReader {

  override def handle(line: String): Msg = {
    val json = parse(line)

    val builder = Map.newBuilder[String, String]
    var eventName = ""
    for (JObject(e) <- json; f <- e) f match {
      case JField("Event", JString(name)) => eventName = name
      case JField(key, value) => builder += key.toString -> value.toString
      case _ => new RuntimeException()
    }
    EventMsg(eventName, builder.result())
  }
}

object JsonEventSourceReader {
  def apply(file: File) = {
    new JsonEventSourceReader(file)
  }
}

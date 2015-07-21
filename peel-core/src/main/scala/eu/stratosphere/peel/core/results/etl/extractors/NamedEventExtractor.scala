package eu.stratosphere.peel.core.results.etl.extractors

import akka.actor.Props
import eu.stratosphere.peel.core.results.model.ExperimentEvent

import scala.util.matching.Regex

class NamedEventExtractor(val eventName: String) extends EventExtractor[EventMsg] {

  override val filePatterns: Seq[Regex] = NamedEventExtractor.patterns

  val buffer = Seq.newBuilder[Map[String, String]]

  override def extract(element: EventMsg): Unit = {
    if (eventName == element.event) buffer += element.value
  }

  override def collect(): Seq[ExperimentEvent] = {
    val events = buffer.result()
    log.info(s"Collected ${events.size} events of type '$eventName'.")
    val results = events.map(e => ExperimentEvent(
      runID,
      Symbol(eventName)
    ))
    results
  }
}

object NamedEventExtractor {
  def props(eventName: String): Props = Props(new NamedEventExtractor(eventName))

  val Events = Set(
    "SparkListenerApplicationStart",
    "SparkListenerJobStart",
    "SparkListenerTaskStart",
    "SparkListenerStageSubmitted",

    "SparkListenerApplicationEnd",
    "SparkListenerJobEnd",
    "SparkListenerTaskEnd",
    "SparkListenerStageCompleted"
  )

  val patterns = Seq("""app-\d+-\d+""".r)
}

//class AppStartExtractor extends NamedEventExtractor {
//  override val eventName: String = "SparkListenerApplicationStart"
//}
//
//class JobStartExtractor extends NamedEventExtractor {
//  override val eventName: String = "SparkListenerJobStart" // SparkListenerJobEnd
//}
//
//class TaskStartExtractor extends NamedEventExtractor {
//  override val eventName: String = "SparkListenerTaskStart" // SparkListenerTaskEnd
//}
//
//class StageStartExtractor extends NamedEventExtractor {
//  override val eventName: String = "SparkListenerStageSubmitted" // SparkListenerStageCompleted
//}

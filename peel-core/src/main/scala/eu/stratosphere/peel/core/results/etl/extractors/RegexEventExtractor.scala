package eu.stratosphere.peel.core.results.etl.extractors

import akka.actor.Props
import eu.stratosphere.peel.core.results.model.ExperimentEvent

import scala.util.matching.Regex

trait RegexEventExtractor[V] extends EventExtractor[StringMsg] {
  val buffer = Seq.newBuilder[V]
  val eventName: String

  override def collect(): Seq[ExperimentEvent] = {
    val events = buffer.result()
    log.info(s"Collected ${events.size} events of type '$eventName'.")
    val results = events.map(e => convert(e))
    results
  }

  def convert(events: V): ExperimentEvent
}

class SubTaskEvent extends RegexEventExtractor[(String, Int)] {
  // TODO: define valid files
  override val filePatterns: Seq[Regex] = Seq("TODO".r)
  override val eventName = "SubTaskEvent"
  val regex = "[0-9]+(?=/[0-9]+\\))".r

  override def extract(element: StringMsg): Unit = {
    element.str match {
      case regex(number) => buffer += ((eventName, number.asInstanceOf[Int]))
      case _ =>
    }
  }

  override def convert(events: (String, Int)): ExperimentEvent = ExperimentEvent(
    runID,
    Symbol(runName)
    // TODO insert values
  )
}

object SubTaskEvent {
  def props(): Props = Props(new SubTaskEvent)
}

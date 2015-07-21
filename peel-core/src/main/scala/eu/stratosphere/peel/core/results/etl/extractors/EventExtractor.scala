package eu.stratosphere.peel.core.results.etl.extractors

import akka.actor.{Actor, ActorLogging}
import eu.stratosphere.peel.core.results.model.ExperimentEvent

import scala.collection._
import scala.util.matching.Regex

/** Base trait for all event extractors. */
trait EventExtractor[A <: Msg] extends Actor with ActorLogging {

  /** Current run name */
  var runName: String = null

  /** Current run ID */
  var runID: Int = -1

  /** Message loop */
  final def receive: Receive = {
    case CurrentRun(name, id) =>
      runName = name
      runID = id

    case EOF =>
      val events = collect()
      log.debug(s"Received EOF msg, reporting ${events.size} collected events.")
      sender() ! ExtractedEvents(events)

    case msg: A => extract(msg)
  }

  /** A list of file patterns for in which the event extractor is interested */
  def filePatterns: Seq[Regex]

  /** Extracts events from an incoming message */
  def extract(msg: A): Unit

  /** Collect all extracted events */
  def collect(): Seq[ExperimentEvent]
}

//trait SparkEvents[A] {
//  ex: EventExtractor[A] =>
//
////  """app-\d+-\d+""".r +: patterns
//  override val patterns: Seq[Regex] = """app-\d+-\d+""".r +: ex.patterns
//}
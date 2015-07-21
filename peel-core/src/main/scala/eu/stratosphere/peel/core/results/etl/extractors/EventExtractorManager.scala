package eu.stratosphere.peel.core.results.etl.extractors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import eu.stratosphere.peel.core.results.model.ExperimentEvent
import eu.stratosphere.peel.core.util.Counter

import scala.collection.mutable

class EventExtractorManager extends Actor with ActorLogging {

  var caller: ActorRef = null

  val aggregatedResults = mutable.Buffer[ExperimentEvent]()

  var received = Counter(0)

  override def receive: Receive = {
    case Start(file, reader, router, runInfo) =>
      aggregatedResults.clear()
      // propagate run information
      router(runInfo, self)
      // read file and send to actors
      caller = sender()
      // TODO: introduce blocking or future + await
      for (f <- reader) yield router(f, self)

    case ExtractedEvents(res) =>
      aggregatedResults ++= res
      received.inc
//      if (received == EventExtractionSystem.handlers()) {
//        log.info("" + aggregatedResults.size)
//        log.debug("All executors completed.")
//        received.reset
//        caller ! End(aggregatedResults)
//      }
  }
}

object EventExtractorManager {
  def props() = {
    Props(new EventExtractorManager())
  }
}
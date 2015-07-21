package eu.stratosphere.peel.core.results.etl.extractors

import java.io.File

import akka.actor.ActorRef
import eu.stratosphere.peel.core.results.etl.RunState

class JsonEventExtractorFactory extends EventExtractorFactory {

  // register all event extractors to akka system
  for (event <- NamedEventExtractor.Events) {
     // EventExtractionSystem.register(event, NamedEventExtractor.props(event), NamedEventExtractor.patterns, add = true)
  }

  type A = Msg

  override def format(state: RunState, file: File): Option[EventSourceReader] = {
    val opt = JsonEventExtractorFactory.Patterns.find(p =>
      file.getName match {
        case p(_*) => true
        case _ => false
      }
    ) match {
      case Some(_) => Some(JsonEventSourceReader(file))
      case _ => None
    }
    opt
  }

  def router(state: RunState, file: File): Option[(Msg, ActorRef) => Unit] = {
//    println(EventExtractionSystem.actors.size)
//    val routees = (for (
//      actor <- EventExtractionSystem.actors;
//      f <- actor._1.find(p =>
//        file.getName match {
//          case p(_*) => true
//          case _ => false
//        }
//      )
//    ) yield {
//        ActorRefRoutee(actor._2)
//      }).toIndexedSeq
//    val retval =
//      if (routees.nonEmpty) {
//        val router = Router(BroadcastRoutingLogic(), routees = routees)
//        Some((msg: Msg, manager: ActorRef) => router.route(msg, manager))
//      } else {
//        None
//      }
//    retval
    None
  }
}

object JsonEventExtractorFactory {
  val Patterns = List(
    """(.*\.json)""".r,
    """app-\d+-\d+""".r
  )
}

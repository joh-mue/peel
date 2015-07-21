package eu.stratosphere.peel.core.results.etl

import java.io.File

import akka.actor.ActorRef
import eu.stratosphere.peel.core.results.model.ExperimentEvent

package object extractors {

  type Router = (Msg, ActorRef) => Unit

  // --------------------------------------------------------------------------
  // ETL message hierarchy for Akka
  // --------------------------------------------------------------------------

  sealed trait Msg

  case class StringMsg(
    str    : String
  ) extends Msg

  case class EventMsg(
    event  : String,
    value  : Map[String, String]
  ) extends Msg

  case object EOF extends Msg

  case class CurrentRun(
    name   : String,
    id     : Int
  ) extends Msg

  case class ExtractedEvents(
    result : Seq[ExperimentEvent]
  ) extends Msg

  case class Start(
    path   : File,
    reader : EventSourceReader,
    router : Router,
    runInfo: CurrentRun
  ) extends Msg

  case class End(
    events : Seq[ExperimentEvent]
  ) extends Msg

}

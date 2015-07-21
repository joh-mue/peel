package eu.stratosphere.peel.core.results.etl.extractors

import java.io.File

import akka.actor.ActorRef
import eu.stratosphere.peel.core.results.etl.RunState

/**
 * Created by alexander on 30.07.15.
 */
trait EventExtractorFactory {

  def format(state: RunState, file: File): Option[EventSourceReader] = ???

  def router(state: RunState, file: File): Option[(Msg, ActorRef) => Unit]
}

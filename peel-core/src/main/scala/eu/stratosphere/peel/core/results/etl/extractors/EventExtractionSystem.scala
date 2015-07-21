package eu.stratosphere.peel.core.results.etl.extractors

import akka.actor.{Props, ActorRef, ActorSystem}
import resource._

import scala.collection.mutable
import scala.reflect.runtime.universe._
import scala.util.matching.Regex

/** An actor ecosystem for ExperimentEvent extraction. */
object EventExtractionSystem extends {

  def apply() = {
    val system = ActorSystem("EventExtractionSystem")

    system.actorOf(Props[EventExtractorManager])

    sys addShutdownHook {
      system.shutdown()
    }

    system
  }

  val extractors = mutable.Buffer[(Seq[Regex], ActorRef)]()

  //  /** Registers an event extractor actor. */
  //  private def register[T <: EventExtractor[_] : Manifest](name: String, patterns: Seq[Regex], add: Boolean = true): ActorRef = {
  //    val ref = Sys.actorOf(Props[T], name = name)
  //    if (add) extractors += ((patterns, ref))
  //    ref
  //  }
  //
  //  /** Registers an actor. */
  //  private def register(name: String, props: Props, patterns: Seq[Regex], add: Boolean = true): ActorRef = {
  //    val ref = Sys.actorOf(props)
  //    if (add) extractors += ((patterns, ref))
  //    ref
  //  }


  def loadEventExtractors[A: TypeTag](): Seq[A] = {
    val fqn = typeOf[A] match /* get fully qualified name for type parameter 'A' */ {
      case TypeRef(_, sym, _) => sym.asClass.fullName
    }
    val res = Seq.newBuilder[A] // result accumulator
    for {
      inp <- managed(getClass.getResourceAsStream(s"/META-INF/services/$fqn")) // commands list
      fqn <- scala.io.Source.fromInputStream(inp).getLines() // fully-qualified class name
    } {
      res += Class.forName(fqn).newInstance().asInstanceOf[A]
    }
    res.result()
  }
}

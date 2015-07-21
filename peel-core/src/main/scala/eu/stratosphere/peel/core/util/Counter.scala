package eu.stratosphere.peel.core.util

class Counter private (private var count: Int) {

  private val init = count

  def inc : this.type = {
    count = count + 1
    this
  }

  def dec : this.type = {
    count = count - 1
    this
  }

  def reset = count = init

  override def equals(o: Any) = o match {
    case o: this.type => this.count == o.count
    case o: Int       => this.count == o
    case _            => false
  }
}

object Counter {
  def apply(count: Int = 0) = new Counter(count)
}

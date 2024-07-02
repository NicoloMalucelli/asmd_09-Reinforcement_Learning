package u09.model

object QMatrix:

  type Node = (Int, Int)

  enum Move:
    case LEFT, RIGHT, UP, DOWN
    override def toString = Map(LEFT -> "<", RIGHT -> ">", UP -> "^", DOWN -> "v")(this)

  import Move.*

  case class Facade(
                     width: Int,
                     height: Int,
                     initial: Node,
                     terminal: PartialFunction[Node, Boolean],
                     rewardActionState: PartialFunction[(Node, Move), Double] = { case (_,_) => 0;},
                     rewardState : PartialFunction[Node, Double] = { case _ => 0;},
                     jumps: PartialFunction[(Node, Move), Node] = Map.empty,
                     obstacles: Set[Node] = Set.empty,
                     gamma: Double,
                     alpha: Double,
                     epsilon: Double = 0.0,
                     v0: Double) extends QRLImpl:

    type State = Node
    type Action = Move

    def qEnvironment(): Environment = (s: Node, a: Move) =>
        // applies direction, without escaping borders
        val n2: Node = (s, a) match
          case ((n1, n2), UP) => (n1, (n2 - 1) max 0)
          case ((n1, n2), DOWN) => (n1, (n2 + 1) min (height - 1))
          case ((n1, n2), LEFT) => ((n1 - 1) max 0, n2)
          case ((n1, n2), RIGHT) => ((n1 + 1) min (width - 1), n2)
          case _ => ???
        // computes rewards, and possibly a jump
        val reward = rewardActionState.apply((s, a)) + rewardState.apply(s)
        val state = jumps.orElse[(Node, Move), Node](_ => n2)(s, a)

        if obstacles contains state
          then (0, s)
          else (reward, state)

    def qFunction = QFunction(Move.values.toSet, v0, terminal)
    def qSystem = QSystem(environment = qEnvironment(), initial, terminal)
    def makeLearningInstance() = QLearning(qSystem, gamma, alpha, epsilon, qFunction)

    def show[E](v: Node => E, formatString: String): String =
      (for
        row <- 0 until height
        col <- 0 until width
      yield formatString.format(v((col, row))) + (if (col == width - 1) "\n" else "\t"))
        .mkString("")


  case class CollectItemsFacade(
                     width: Int,
                     height: Int,
                     initial: (Node, Seq[(Node, Boolean)]),
                     terminal: PartialFunction[(Node, Seq[(Node, Boolean)]), Boolean] = {case _ => false},
                     items: Set[Node],
                     gamma: Double,
                     alpha: Double,
                     epsilon: Double = 0.0,
                     v0: Double) extends QRLImpl:

    type CollectableItem = (Node, Boolean)
    type State = (Node, Seq[CollectableItem])
    type Action = Move

    class CollectItemsEnv extends Environment:
      var collectedItems: Set[Node] = Set.empty
      override def reset(): Unit = collectedItems = Set.empty
      override def apply(s: State, a: Action): (Reward, State) =
        // applies direction, without escaping borders
        val n2: Node = (s._1, a) match
          case ((n1, n2), UP) => (n1, (n2 - 1) max 0)
          case ((n1, n2), DOWN) => (n1, (n2 + 1) min (height - 1))
          case ((n1, n2), LEFT) => ((n1 - 1) max 0, n2)
          case ((n1, n2), RIGHT) => ((n1 + 1) min (width - 1), n2)
          case _ => ???

        var reward = 0
        if (items contains n2) && !(collectedItems contains n2) then
          collectedItems = collectedItems + n2
          reward = 10

        (reward, (n2, items.map(pos => (pos, collectedItems contains pos)).toSeq))

    def qEnvironment(): Environment = CollectItemsEnv()

    def qFunction = QFunction(Move.values.toSet, v0, terminal)

    def qSystem = QSystem(environment = qEnvironment(), initial, terminal)

    def makeLearningInstance() = QLearning(qSystem, gamma, alpha, epsilon, qFunction)

    def show[E](v: State => E, formatString: String, items: Seq[CollectableItem]): String =
      (for
        row <- 0 until height
        col <- 0 until width
      yield formatString.format(v((col, row),items)) + (if (col == width - 1) "\n" else "\t"))
        .mkString("")
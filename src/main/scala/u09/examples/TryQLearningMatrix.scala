package u09.examples

import u09.model.QMatrix

object TryQMatrix extends App :

  import u09.model.QMatrix.Move.*
  import u09.model.QMatrix.*

  val rl: QMatrix.Facade = Facade(
    width = 5,
    height = 5,
    initial = (0,0),
    terminal = {case _=>false},
    rewardActionState = { case ((1,0),DOWN) => 10; case ((3,0),DOWN) => 5; case _ => 0},
    jumps = { case ((1,0),DOWN) => (1,4); case ((3,0),DOWN) => (3,2) },
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.3,
    v0 = 1
  )

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction,"%2.2f"))
  val q1 = rl.makeLearningInstance().learn(10000,100,q0)
  println(rl.show(q1.vFunction,"%2.2f"))
  println(rl.show(s => q1.bestPolicy(s).toString,"%7s"))


object ZigZag extends App :

  import u09.model.QMatrix.Move.*
  import u09.model.QMatrix.*

  val obstacles = Set((8,1), (6,0), (4,1), (3,1), (1,0))
  val rl: QMatrix.Facade = Facade(
    width = 10,
    height = 2,
    initial = (0,0),
    terminal = {case _ => false},
    rewardState = {case (9,1) => 10; case _ => 0},
    obstacles = obstacles,
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.3,
    v0 = 1
  )

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction,"%2.2f"))
  val q1 = rl.makeLearningInstance().learn(10000,100,q0)
  println(rl.show(q1.vFunction,"%2.2f"))
  println(rl.show(s => if obstacles contains s then "X" else q1.bestPolicy(s).toString,"%7s"))


object CollectItems extends App :

  import u09.model.QMatrix.Move.*
  import u09.model.QMatrix.*

  val items = Set((2,7), (6,9), (9,0))

  val rl: CollectItemsFacade = CollectItemsFacade(
    width = 10,
    height = 10,
    initial = ((0,0), items.map(pos => (pos, false)).toSeq),
    terminal = {case _ => false},
    items = items,
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.3,
    v0 = 1
  )

  var collectedItems =  Seq(((2,7), false), ((6,9), false), ((9,0),false))
  val q0 = rl.qFunction
  println(rl.show(q0.vFunction,"%2.2f", collectedItems))

  val q1 = rl.makeLearningInstance().learn(10000,100,q0)

  println(rl.show(q1.vFunction,"%2.2f", collectedItems))
  println(rl.show(s =>
    if collectedItems.filter((pos, collected) => !collected).map((pos, collected) => pos) contains s._1
      then "*"
      else q1.bestPolicy(s).toString,"%7s", collectedItems)
  )

  collectedItems =  Seq(((2,7), true), ((6,9), false), ((9,0),false))
  println(rl.show(s =>
    if collectedItems.filter((pos, collected) => !collected).map((pos, collected) => pos) contains s._1
    then "*"
    else q1.bestPolicy(s).toString, "%7s", collectedItems)
  )

  collectedItems = Seq(((2, 7), true), ((6, 9), true), ((9, 0), false))
  println(rl.show(s =>
    if collectedItems.filter((pos, collected) => !collected).map((pos, collected) => pos) contains s._1
    then "*"
    else q1.bestPolicy(s).toString, "%7s", collectedItems)
  )
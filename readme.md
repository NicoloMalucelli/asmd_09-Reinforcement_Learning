# Reinforcement Learning

## Task 1: BASIC-Q-LEARNING

Q-learning is a critic-based reinforcement learning technique, which means that the behaviour of the agent is
defined by a value function that describes how much a state is good in terms of expected discounted reward.

Changing the value of a hyperparameter affects the Q-table and may affect the agent behaviour.

- epsilon represents the exploration rate and can range from 0 to 1, determining the balance between exploration and
exploitation. When epsilon is 0, the agent has a fully deterministic behaviour since at each step it always takes the action 
having the highest expected return, therefore many states are never visited and the learning can't be optimal. 
When epsilon is 1, the agent always choose the 
action randomly from the set of possible actions, without consulting the Q-table. In a small environment such as the
one of the example, such behaviour can still lead to an optimal policy even though it can take much longer because the
number of possible states is quite low, anyway in a normal scenario, having epsilon equals to 1 is not suggested if
not at a very early stage of the training. Normally epsilon is not kept constant but is set higher at the beginning, to
encourage exploration of new states, and lowered while the agent learns. 
- gamma represents the discount rate and like epsilon can range between 0 and 1. When computing the discounted reward of
an action happened at a time t, at the immediate reward has to be summed the reward of the previous actions (t<sub>-1</sub>, t<sub>-2</sub>,...) that lead to that state multiplied
by a factor gamma<sup>(t - t<sub>-i</sub>)</sup>. Since gamma is raised by t, older actions in the trajectory are less and less considered
when summed to the expected reward. When gamma is equal to 1, all the actions in the trajectory have the same importance
but the training may never converge to an optimal behaviour. When gamma is equal to 0, only the immediate reward is considered
and the agent "can not see in the future": every choice is based just on the current state. 
Normally gamma ranges between 0.90 and 0.99.
- alpha is the learning rate, in other words determines how much the newly acquired information overrides the old one.
When alpha is close to one, the new information completely override the old one, while when alpha is zero, the new experiences
are not used at all to train the agent. A lower value of alpha allows the agent to slowly learn new information without forgetting
the old experiences but is slow to adapt to changes in the environment. In opposite, when alpha is high, the agent learns
quickly but there may be problem with the convergence of the algorithm to an optimal solution due to a high oscillation
in the Q-table values. Like epsilon, alpha is usually higher at the beginning of the training process and lowered while
training.
- the episode length influences directly the number of possible states the agent can visit. If the episode length is lower 
of the number of possible states or close to, the agent won't be able to visit all the states, leading to a suboptimal behaviour. 
A too big episode length has instead the only problem to slow down the training process since the number of state-action-reward tuples
to evaluate increases linearly. The episode length should be chosen depending on the difficulty of the task: in the example case an episode
length equals to 10.000 is completely useless since the algorithm converges really fast thanks to the small amount of states.
- By increasing the dimension of the grid, the algorithm takes longer to converge to an optimal solution since the number of states to
explore is higher. If the grid size is much bigger, it may be necessary to increase the episode length.


## Task 2: DESIGN-BY-Q-LEARNING

### obstacles

The goal of this task was to teach the agent how to reach a destination point while avoiding fix obstacles.

Obstacles are represented by a set of nodes; each time the agent performs an action moving in the same position as an obstacle, the agent is brang 
back to the cell it was coming from. No negative reward is assigned to the agent which learns anyway how to avoid the obstacles in order to maximize the
expected reward and reaching its destination in the lowest number of steps.

```
if obstacles contains state
  then (0, s)
  else (reward, state)
```

In the image shown below, it is depicted this exact behaviour. Each "X" represents an obstacle and we can notice how the agent is able to navigate from
the initial cell (0,0) to the destination (9,1) avoiding all the obstacles.

![image](https://github.com/NicoloMalucelli/asmd_09-Reinforcement_Learning/assets/73821474/871d6b47-cb79-48f9-93f4-cf1035a8056e)

### collect items

The goal of this second task was to teach the agent how to collect all the items in the lowest number of step. 

This modellation required more work than the previous one because the original environment model used in the other example didn't considered the scenarios in which
an environment can evolve during time. In this particular scenario, is in fact necessary to reset the state of each item once the episode is over. To do so, I defined
a `reset()` method in the `Environment` trait, overriding it in the Environment implementations that need it. This method is called before calling `runSingleEpisode` inside the
`learn` method of the `QLearning` class.

```
    final override def learn(episodes: Int, length: Int, qf: Q): Q =
      episodes match
        case 0 => qf
        case e => 
          system.environment.reset()
          learn(e - 1, length, runSingleEpisode((system.initial, qf), length)._2)
```

An other difference between this Environment and the former one is the state definition: while previously the state was simply identified by the agent position, in this second scenario,
the agent decision must be based also on the position of the items and whether they have already been collected or not:

```
type CollectableItem = (Node, Boolean)
type State = (Node, Seq[CollectableItem])
```

The images below show how the agent's behaviour evolves depending on which items are available at that time. As always, the agent's starting position is the node (0,0), while the items
are represented in the map by an asterisk (*).

In this first image is shown the initial state of the environment, when no item has been collected
![image](https://github.com/NicoloMalucelli/asmd_09-Reinforcement_Learning/assets/73821474/7465f4be-6031-4f7a-93e4-20f81e9dfaf6)

This second image shown the agent's policy when the first item is collected 
![image](https://github.com/NicoloMalucelli/asmd_09-Reinforcement_Learning/assets/73821474/354ef774-d0e4-4099-9fd7-a7a7cf248479)

This second image shown the agent's policy when only the last item is remaining. As we can see, the policies are not optimal! When only one item is left to collect, the agent is always in the bottom part of the environment, therefore
very unlikely the agent will visit the top-left part.
![image](https://github.com/NicoloMalucelli/asmd_09-Reinforcement_Learning/assets/73821474/ec312857-b9e1-4b1b-998a-cf7b729cdbde)




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

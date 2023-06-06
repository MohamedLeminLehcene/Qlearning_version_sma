package ma.enset.qlearning.sma.multiAgent.impl;// Agent1.java
import jade.core.Agent;

public class Agent1 extends Agent {
    protected void setup() {
        // Code spécifique à l'agent 1
        addBehaviour(new QLearningBehaviour());
        setupAgent("Agent1");
    }

    public void setupAgent(String agentName) {
        System.out.println(agentName + ": " + getLocalName() + " started.");
        // Additional setup code for Agent1
    }
}

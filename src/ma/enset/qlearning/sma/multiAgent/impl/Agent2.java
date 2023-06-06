package ma.enset.qlearning.sma.multiAgent.impl;// Agent2.java
import jade.core.Agent;

public class Agent2 extends Agent {
    protected void setup() {
        // Code spécifique à l'agent 2
        addBehaviour(new QLearningBehaviour());
        setupAgent("Agent2");
    }

    public void setupAgent(String agentName) {
        System.out.println(agentName + ": " + getLocalName() + " started.");
        // Additional setup code for Agent2
    }
}

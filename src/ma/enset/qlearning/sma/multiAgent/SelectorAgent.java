package ma.enset.qlearning.sma.multiAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.Map;

public class SelectorAgent extends Agent {
    private int numAgents;
    private int numReceivedResults;
    private Map<String, Integer> results;

    protected void setup() {
        numAgents = 4; // Mettez ici le nombre d'agents QLearningAgent
        numReceivedResults = 0;
        results = new HashMap<>();

        addBehaviour(new ResultReceiverBehaviour());
    }

    private class ResultReceiverBehaviour extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = receive(mt);

            if (msg != null) {
                String agentName = msg.getSender().getLocalName();
                int result = Integer.parseInt(msg.getContent());
                results.put(agentName, result);
                numReceivedResults++;

                if (numReceivedResults == numAgents) {
                    selectBestResult();
                    doDelete();
                }
            } else {
                block();
            }
        }
    }

    private void selectBestResult() {
        String bestAgent = null;
        int bestResult = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            String agentName = entry.getKey();
            int result = entry.getValue();
            if (result < bestResult) {
                bestResult = result;
                bestAgent = agentName;
            }
        }

        if (bestAgent != null) {
            System.out.println("Best result: Agent " + bestAgent + " - Goal state: " + bestResult);
        }
    }
}

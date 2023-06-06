package ma.enset.qlearning.sma.multiAgent.impl;
// MainAgent.java
import jade.core.Agent;
        import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;

public class MainAgentN  extends Agent {
    private int agent1ResultCount = 0;
    private int agent2ResultCount = 0;

    protected void setup() {
        addBehaviour(new StartAgentBehaviour());
        addBehaviour(new ReceiveResultBehaviour());
    }


    private class StartAgentBehaviour extends OneShotBehaviour {
        public void action() {
            try {
                // Créer et démarrer Agent1
                AgentController agent1Controller = getContainerController().createNewAgent("Agent1", "ma.enset.qlearning.sma.multiAgent.impl.Agent1", null);
                agent1Controller.start();

                // Créer et démarrer Agent2
                AgentController agent2Controller = getContainerController().createNewAgent("Agent2", "ma.enset.qlearning.sma.multiAgent.impl.Agent2", null);
                agent2Controller.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceiveResultBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage message = myAgent.receive();
            if (message != null) {
                String agentName = message.getSender().getLocalName();
                String result = message.getContent();
                if (agentName.equals("Agent1")) {
                    System.out.println("Agent1 Result:\n" + result);
                    agent1ResultCount++;
                } else if (agentName.equals("Agent2")) {
                    System.out.println("Agent2 Result:\n" + result);
                    agent2ResultCount++;
                }

                // Vérifier si tous les résultats ont été reçus
                if (agent1ResultCount == 1 && agent2ResultCount == 1) {
                    // Terminer l'agent principal
                    myAgent.doDelete();
                }
            } else {
                block();
            }
        }
    }


}


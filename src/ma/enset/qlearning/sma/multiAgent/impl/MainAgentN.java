package ma.enset.qlearning.sma.multiAgent.impl;
// MainAgent.java
import jade.core.Agent;
        import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;

public class MainAgentN  extends GuiAgent {
    private static int countActionsOfAgentOne;
    private static int countActionsOfAgentTwo;

    public static int getCountActionsOfAgentOne() {
        return countActionsOfAgentOne;
    }

    public static void setCountActionsOfAgentOne(int countActionsOfAgentOne) {
        MainAgentN.countActionsOfAgentOne = countActionsOfAgentOne;
    }

    public static int getCountActionsOfAgentTwo() {
        return countActionsOfAgentTwo;
    }

    public static void setCountActionsOfAgentTwo(int countActionsOfAgentTwo) {
        MainAgentN.countActionsOfAgentTwo = countActionsOfAgentTwo;
    }

    private Main main;
    private int agent1ResultCount = 0;
    private int agent2ResultCount = 0;

    protected void setup() {
        main = (Main)getArguments()[0];
        main.setMainAgentN(this);
        addBehaviour(new StartAgentBehaviour());
        addBehaviour(new ReceiveResultBehaviour());
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

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

    public static String extractValue(String input) {
        String marker = "=>";
        int index = input.indexOf(marker);
        if (index != -1) {
            String substring = input.substring(index + marker.length()).trim();
            if (substring.contains(" ")) {
                return substring.substring(0, substring.indexOf(" "));
            }
            return substring;
        }
        return "";
    }

    private class ReceiveResultBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage message = myAgent.receive();
            if (message != null) {
                String agentName = message.getSender().getLocalName();
                String result = message.getContent();
               // System.out.println(result);
                if (agentName.equals("Agent1")) {
                    String value = extractValue(result);
                    //System.out.println(value);
                    setCountActionsOfAgentOne(Integer.parseInt(value));

                   main.showResult(result);

                    //System.out.println("Agent1 Result:\n" + result);

                    agent1ResultCount++;
                } else if (agentName.equals("Agent2")) {

                //  System.out.println("Agent2 Result:\n" + result);
                    String value = extractValue(result);
                    //System.out.println(value);
                    setCountActionsOfAgentTwo(Integer.parseInt(value));
                    main.showResult(result);
                    agent2ResultCount++;
                }


                // Vérifier si tous les résultats ont été reçus
                if (agent1ResultCount == 1 && agent2ResultCount == 1) {

                    System.out.println("agent count 1 : "+getCountActionsOfAgentOne());
                    System.out.println("agent count 2: "+getCountActionsOfAgentTwo());

                    if(getCountActionsOfAgentOne()<getCountActionsOfAgentTwo()){
                        main.showResult("le meileur agent resut pour atteidre l'etat final(GOAL) est :  Agent1");
                    }
                    else if(getCountActionsOfAgentTwo()<getCountActionsOfAgentTwo())
                    {
                        main.showResult("le meileur agent resut pour atteidre l'etat final(GOAL) est :  Agent2");
                    }
                    else
                    {
                        main.showResult("\"Resultat egal\"");
                    }

                    // Terminer l'agent principal
                    myAgent.doDelete();

                    /*
                    if(getCountActionsOfAgentOne()>getCountActionsOfAgentTwo()){
                        System.out.println("le meileur agent resut pour atteidre l'etat final(GOAL) est :  Agent1");
                    }else if(getCountActionsOfAgentTwo()<getCountActionsOfAgentTwo())
                    {
                        System.out.println("le meileur agent resut pour atteidre l'etat final(GOAL) est :  Agent2");
                    }
                    else
                    {
                        System.out.println("\"Resultat egal\"");
                    }
                     */
                }
            } else {
                block();
            }
        }
    }

}


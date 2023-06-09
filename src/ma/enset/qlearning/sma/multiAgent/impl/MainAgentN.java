package ma.enset.qlearning.sma.multiAgent.impl;
// MainAgent.java
import jade.core.Agent;
        import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;

// La classe MainAgentN est un agent GUI (GuiAgent) qui coordonne l'exécution des agents Agent1 et Agent2.
public class MainAgentN  extends GuiAgent {
    private static int countActionsOfAgentOne;
    private static int countActionsOfAgentTwo;

    // Méthodes getter et setter pour le compteur d'actions de l'agent 1
    public static int getCountActionsOfAgentOne() {
        return countActionsOfAgentOne;
    }

    public static void setCountActionsOfAgentOne(int countActionsOfAgentOne) {
        MainAgentN.countActionsOfAgentOne = countActionsOfAgentOne;
    }
    // Méthodes getter et setter pour le compteur d'actions de l'agent 2
    public static int getCountActionsOfAgentTwo() {
        return countActionsOfAgentTwo;
    }

    public static void setCountActionsOfAgentTwo(int countActionsOfAgentTwo) {
        MainAgentN.countActionsOfAgentTwo = countActionsOfAgentTwo;
    }
    // Référence vers l'objet Main de l'interface utilisateur
    private Main main;
    // Compteur de résultats de l'agent 1
    private int agent1ResultCount = 0;
    // Compteur de résultats de l'agent 2
    private int agent2ResultCount = 0;

    protected void setup() {
        // Récupérer l'objet Main passé en argument
        main = (Main)getArguments()[0];
        // Définir cette instance de MainAgentN comme le MainAgentN de l'objet Main
        main.setMainAgentN(this);
        // Ajouter le comportement de démarrage des agents Agent1 et Agent2
        addBehaviour(new StartAgentBehaviour());
        // Ajouter le comportement de réception des résultats
        addBehaviour(new ReceiveResultBehaviour());
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

    }

    // Comportement OneShot pour démarrer les agents Agent1 et Agent2
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

    // Méthode utilitaire pour extraire la valeur d'un message
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

    // Comportement cyclique pour recevoir les résultats des agents Agent1 et Agent2
    private class ReceiveResultBehaviour extends CyclicBehaviour {
        public void action() {
            // Recevoir un message
            ACLMessage message = myAgent.receive();
            if (message != null) {
                // Nom de l'agent qui a envoyé le message
                String agentName = message.getSender().getLocalName();
                // Contenu du message
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
// Afficher les compteurs d'actions des agents 1 et 2
                    System.out.println("agent count 1 : "+getCountActionsOfAgentOne());
                    System.out.println("agent count 2: "+getCountActionsOfAgentTwo());
// Comparer les compteurs d'actions et afficher le meilleur agent
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


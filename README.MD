<h2>Lehcene Mohamed Lemine => Master SDIA ENSET MOHAMMEDIA</h2>
<h3>Architecture que j'ai abordé pour implementer cet Algorithme Q-Learning avec SMA.</h4>
<img src="photo/img05.png">
<h3>Voici mon systéme qui permet d'execute cet Algorithme Q-Learning est démarer</h3>
<img src="photo/img01.png">
<h3>Voici lorsque j'ai clique sur bouton qui permet de lancer d'execution cet algorithme il m'affiche ce resultat qui contient tableau d'apprentissage pour chacun des agnets , les états et les actions qui abordée pour atteindre état GOAL et les nombres des états qui executé et à la fin affiche un message qui contient quel l'agent est meilleur pour atteindre état GOAL par le chemin le plus court.</h3>
<img src="photo/img02.png">
<img src="photo/img03.png">

<h2>Implementation</h2>
<h4>Classe Main :</h4>

<pre>

package ma.enset.qlearning.sma.multiAgent.impl;// Main.java
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// La classe Main est la classe principale de l'application JavaFX qui lance l'interface utilisateur et gère la communication avec l'agent principal MainAgentN.
public class Main  extends Application {
    // Référence vers l'agent principal MainAgentN
    private MainAgentN mainAgentN;
    // Bouton pour exécuter l'algorithme QLearning
    private Button runButton;
    // Liste observable pour afficher les résultats
    private ObservableList<String> data = FXCollections.observableArrayList();
    // Méthode principale qui lance l'application JavaFX
    public static void main(String[] args){
        launch(args);
    }

    // Méthode start() qui crée l'interface utilisateur
    @Override
    public void start(Stage primaryStage) throws Exception {


        /*
        primaryStage.setTitle("Algorithme QLearning avec Systéme Multi Agents");

        runButton = new Button("Run QLearning");
        runButton.setOnAction((event -> {
            try {
                startContent();
            } catch (ControllerException e) {
                throw new RuntimeException(e);
            }
        }));

        VBox root = new VBox(runButton);
        Scene scene = new Scene(root, 200, 100);

        primaryStage.setScene(scene);
        primaryStage.show();
         */


        // Création des composants de l'interface utilisateur
        BorderPane root = new BorderPane();

        Button buttonEnvoie =  new Button("Execute Algorithme QLearning Avec SMA");
        buttonEnvoie.setPrefWidth(400);
        buttonEnvoie.setPrefHeight(30);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10));
        hBox.setBackground(new Background(new BackgroundFill(Color.ORANGE,null,null)));
        hBox.getChildren().add(buttonEnvoie);
        root.setBottom(hBox);


        ListView<String> listView = new ListView<>(data);
        VBox vBox= new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(listView);

        root.setCenter(vBox);

        Scene scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("QLearning Application");
        primaryStage.show();

// Lorsque le bouton "buttonEnvoie" est cliqué, la méthode startContent() est appelée
        buttonEnvoie.setOnAction((event -> {
            try {
                startContent();
            }catch (Exception e){
                e.printStackTrace();
            }
        }));

    }
    // Méthode qui exécute le contenu de l'application
    public void startContent() throws Exception{
        Runtime runtime = Runtime.instance();Profile mainAgentProfile = new ProfileImpl();
        AgentContainer mainAgentContainer = runtime.createMainContainer(mainAgentProfile);
        try {
            AgentController mainAgentController = mainAgentContainer.createNewAgent("MainAgentN",MainAgentN.class.getName(), new Object[]{this});
            mainAgentController.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
    public static void main(String[] args) {
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile mainAgentProfile = new ProfileImpl();
        AgentContainer mainAgentContainer = rt.createMainContainer(mainAgentProfile);
        try {
            AgentController mainAgentController = mainAgentContainer.createNewAgent("MainAgentN",MainAgentN.class.getName(), null);
            mainAgentController.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
     */
// Méthode pour afficher les résultats dans la liste observable "data"
    public void showResult(String result){
        Platform.runLater(()->{
            data.add(result);
        });
    }
    // Méthode pour définir la référence vers l'agent principal MainAgentN
    public void setMainAgentN(MainAgentN mainAgentN) {
        this.mainAgentN = mainAgentN;
    }
}


</pre>


<h4>Classe MainAgentN : </h4>
<pre>
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


</pre>

<h4>Classe sQLearningBehaviour : </h4>
<pre>package ma.enset.qlearning.sma.multiAgent.impl;// QLearningBehaviour.java
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearningBehaviour extends Behaviour {
    StringBuilder sb = new StringBuilder();
    private int agentActionsCount=0;
    // Ajouter une liste pour stocker les états et les actions
    private List<String> stateActionList = new ArrayList<>();
    private final int[][] grid = {
            {0, 0, 1},
            {0, -1, 0},
            {0, 0, 0}
    };
    private final double[][] qTable = new double[QLearningUtils.GRID_SIZE * QLearningUtils.GRID_SIZE][QLearningUtils.ACTION_SIZE];
    private final int[][] actions = {
            {0, -1},  // gauche
            {0, 1},   // droit
            {1, 0},   // bas
            {-1, 0}   // haut
    };

    private int state_x;
    private int state_y;

    // Réinitialise l'état de l'agent
    public void resetState() {
        state_x = 2;
        state_y = 0;
        agentActionsCount=0;
    }

    private void recordStateAction(int state, int action) {
        // Enregistre l'état et l'action dans une liste
        int state_x = state / QLearningUtils.GRID_SIZE;
        int state_y = state % QLearningUtils.GRID_SIZE;
        stateActionList.add("State: " + state + " action: " + action);
    }

    public void action() {
        // Logique du comportement de Q-learning
        int iter = 0;
        while (iter < QLearningUtils.MAX_EPOCH) {
            resetState();
            stateActionList.clear(); // Réinitialiser la liste des états et actions
            while (!finished()) {
                int currentState = state_x * QLearningUtils.GRID_SIZE + state_y;
                int act = chooseAction(0.4);
                recordStateAction(currentState, act); // Enregistrer l'état et l'action
                int nextState = executeAction(act);
                int act1 = chooseAction(0);

                qTable[currentState][act] = qTable[currentState][act] + QLearningUtils.ALPHA * (grid[state_x][state_y] + QLearningUtils.GAMMA * qTable[nextState][act1] - qTable[currentState][act]);
            }
            iter++;
        }


        // Afficher le nombre d'actions effectuées par l'agent
        sb.append("Agent : "+myAgent.getLocalName() +" Actions count => "+agentActionsCount+" \n");
        /*
        System.out.println("Bate");
        System.out.println("Agent " + myAgent.getLocalName() + " Actions Count: " + agentActionsCount);
         */

        // Envoyer les résultats au MainAgent
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(formatQTable(myAgent.getLocalName(), qTable, stateActionList));
        message.addReceiver(new AID("MainAgentN", AID.ISLOCALNAME));
        myAgent.send(message);
        QLearningUtils.FINISHED = true;
    }

    // Indique si le comportement est terminé
    public boolean done() {
        return QLearningUtils.FINISHED;
    }

    // Choisi une action selon la politique epsilon-greedy
    private int chooseAction(double eps) {
        Random rnd = new Random();
        double bestQ = 0;
        int act = 0;

        if (rnd.nextDouble() < eps) {
            act = rnd.nextInt(QLearningUtils.ACTION_SIZE);
        } else {
            int st = state_x * QLearningUtils.GRID_SIZE + state_y;
            for (int i = 0; i < QLearningUtils.ACTION_SIZE; i++) {
                if (qTable[st][i] > bestQ) {
                    bestQ = qTable[st][i];
                    act = i;
                }
            }
        }
        return act;
    }

    // Exécute l'action choisie et met à jour l'état de l'agent
    private int executeAction(int actIndex) {
        state_x = Math.max(0, Math.min(actions[actIndex][0] + state_x, QLearningUtils.GRID_SIZE - 1));
        state_y = Math.max(0, Math.min(actions[actIndex][1] + state_y, QLearningUtils.GRID_SIZE - 1));
        agentActionsCount++;
        return state_x * QLearningUtils.GRID_SIZE + state_y;
    }

    // Indique si l'agent a atteint un état final
    private boolean finished() {
        return grid[state_x][state_y] == 1;
    }

    // Formate la table Q et les états-actions dans une chaîne de caractères
    private String formatQTable(String agentName, double[][] qTable, List<String> stateActionList) {

        sb.append("******  q table ******\n");
        for (int i = 0; i < qTable.length; i++) {
            sb.append("[");
            for (int j = 0; j < qTable[i].length; j++) {
                sb.append(qTable[i][j]).append(" ");
            }
            sb.append("]\n");
        }

        sb.append("\n");
        sb.append("Agent: ").append(agentName).append("\n");
        sb.append("States and Actions:\n");
        for (String stateAction : stateActionList) {
            sb.append(stateAction).append("\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    // Récupère l'identifiant de l'agent principal (MainAgent)
    private AID getMainAgentAID() {
        return new AID("MainAgent", AID.ISLOCALNAME);
    }
}
</pre>

<h4>Classe QLearningUtils : </h4
<pre>
package ma.enset.qlearning.sma.multiAgent.impl;

import com.sun.org.apache.bcel.internal.generic.FALOAD;

public class QLearningUtils {
    public static boolean FINISHED=false;
    public static final  double ALPHA=0.1;
    public static final double GAMMA=0.9;
    public static final int MAX_EPOCH=20000;
    public static final int GRID_SIZE=3;
    public static final int ACTION_SIZE=4;

/*
    //private boolean finished = false;
    private final double ALPHA = 0.1;
    private final double GAMMA = 0.9;
    private final int MAX_EPOCH = 20000;
    private final int GRID_SIZE = 3;
    private final int ACTION_SIZE = 4;
 */

}


</pre>


<h4>Classe Agent 1 : </h4>
<pre>

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

</pre>

<h4>Classe Agent 2 : </h4>

<pre>
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

</pre>
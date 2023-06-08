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
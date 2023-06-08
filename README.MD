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
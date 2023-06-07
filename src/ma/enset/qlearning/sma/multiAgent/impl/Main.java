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

public class Main  extends Application {
    private MainAgentN mainAgentN;
    private Button runButton;
    private ObservableList<String> data = FXCollections.observableArrayList();

    public static void main(String[] args){
        launch(args);
    }

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


        //Interface JavaFX
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
        primaryStage.setTitle("Algorithme QLearning avec Systéme Multi Agents");
        primaryStage.show();


        buttonEnvoie.setOnAction((event -> {
            try {
                startContent();
            }catch (Exception e){
                e.printStackTrace();
            }
        }));

    }

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

    public void showResult(String result){
        Platform.runLater(()->{
            data.add(result);
        });
    }

    public void setMainAgentN(MainAgentN mainAgentN) {
        this.mainAgentN = mainAgentN;
    }
}


package ma.enset.qlearning.sma.multiAgent.impl;// Main.java
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;


public class Main {
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
}


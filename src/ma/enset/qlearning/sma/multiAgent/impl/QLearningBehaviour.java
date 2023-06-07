package ma.enset.qlearning.sma.multiAgent.impl;// QLearningBehaviour.java
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

    public void resetState() {
        state_x = 2;
        state_y = 0;
        agentActionsCount=0;
    }

    private void recordStateAction(int state, int action) {
        int state_x = state / QLearningUtils.GRID_SIZE;
        int state_y = state % QLearningUtils.GRID_SIZE;
        stateActionList.add("State: " + state + " action: " + action);
    }

    public void action() {
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
        sb.append("Agent : "+myAgent.getLocalName() +" Actions count : "+agentActionsCount+" \n");
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

    public boolean done() {
        return QLearningUtils.FINISHED;
    }

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

    private int executeAction(int actIndex) {
        state_x = Math.max(0, Math.min(actions[actIndex][0] + state_x, QLearningUtils.GRID_SIZE - 1));
        state_y = Math.max(0, Math.min(actions[actIndex][1] + state_y, QLearningUtils.GRID_SIZE - 1));
        agentActionsCount++;
        return state_x * QLearningUtils.GRID_SIZE + state_y;
    }

    private boolean finished() {
        return grid[state_x][state_y] == 1;
    }

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

    private AID getMainAgentAID() {
        return new AID("MainAgent", AID.ISLOCALNAME);
    }
}

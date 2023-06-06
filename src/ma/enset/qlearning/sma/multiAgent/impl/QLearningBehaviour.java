package ma.enset.qlearning.sma.multiAgent.impl;// QLearningBehaviour.java
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearningBehaviour extends Behaviour {
    private boolean finished = false;
    private final double ALPHA = 0.1;
    private final double GAMMA = 0.9;
    private final int MAX_EPOCH = 20000;
    private final int GRID_SIZE = 3;
    private final int ACTION_SIZE = 4;
    // Ajouter une liste pour stocker les états et les actions
    private List<String> stateActionList = new ArrayList<>();
    private final int[][] grid = {
            {0, 0, 1},
            {0, -1, 0},
            {0, 0, 0}
    };
    private final double[][] qTable = new double[GRID_SIZE * GRID_SIZE][ACTION_SIZE];
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
    }

    private void recordStateAction(int state, int action) {
        int state_x = state / GRID_SIZE;
        int state_y = state % GRID_SIZE;
        stateActionList.add("State: " + state + " action: " + action);
    }

    public void action() {
        int iter = 0;
        while (iter < MAX_EPOCH) {
            resetState();
            stateActionList.clear(); // Réinitialiser la liste des états et actions
            while (!finished()) {
                int currentState = state_x * GRID_SIZE + state_y;
                int act = chooseAction(0.4);
                recordStateAction(currentState, act); // Enregistrer l'état et l'action
                int nextState = executeAction(act);
                int act1 = chooseAction(0);

                qTable[currentState][act] = qTable[currentState][act] + ALPHA * (grid[state_x][state_y] + GAMMA * qTable[nextState][act1] - qTable[currentState][act]);
            }
            iter++;
        }

        // Envoyer les résultats au MainAgent
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(formatQTable(myAgent.getLocalName(), qTable, stateActionList));
        message.addReceiver(new AID("MainAgentN", AID.ISLOCALNAME));
        myAgent.send(message);
        finished = true;
    }

    public boolean done() {
        return finished;
    }

    private int chooseAction(double eps) {
        Random rnd = new Random();
        double bestQ = 0;
        int act = 0;

        if (rnd.nextDouble() < eps) {
            act = rnd.nextInt(ACTION_SIZE);
        } else {
            int st = state_x * GRID_SIZE + state_y;
            for (int i = 0; i < ACTION_SIZE; i++) {
                if (qTable[st][i] > bestQ) {
                    bestQ = qTable[st][i];
                    act = i;
                }
            }
        }
        return act;
    }

    private int executeAction(int actIndex) {
        state_x = Math.max(0, Math.min(actions[actIndex][0] + state_x, GRID_SIZE - 1));
        state_y = Math.max(0, Math.min(actions[actIndex][1] + state_y, GRID_SIZE - 1));
        return state_x * GRID_SIZE + state_y;
    }

    private boolean finished() {
        return grid[state_x][state_y] == 1;
    }

    private String formatQTable(String agentName, double[][] qTable, List<String> stateActionList) {
        StringBuilder sb = new StringBuilder();
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

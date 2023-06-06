package ma.enset.qlearning.sma.multiAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QLearningAgent extends Agent {
    private final double ALPHA = 0.1;
    private final double GAMMA = 0.9;
    private boolean isFinished = false;
    private final int MAX_EPOCH = 30000;
    private final int GRID_SIZE = 3;
    private final int ACTION_SIZE = 4;
    private int[][] grid;
    private double[][] qTable = new double[GRID_SIZE * GRID_SIZE][ACTION_SIZE];
    private int[][] actions;
    private int state_x;
    private int state_y;
    private int goalState;
    private int agentIndex;


    protected void setup() {
        actions = new int[][]{
                {0, -1},  // gauche
                {0, 1},   // droite
                {1, 0},   // bas
                {-1, 0}   // haut
        };

        grid = new int[][]{
                {0, 0, 1},
                {0, -1, 0},
                {0, 0, 0},
        };

        agentIndex = Integer.parseInt(getLocalName().substring(14));
        goalState = calculateGoalState(agentIndex);

        // Calcul des coordonnées initiales en fonction de l'indice de l'agent
        state_x = (agentIndex % GRID_SIZE) * 3;  // Utilisation de l'indice de l'agent pour définir l'état initial en x
        state_y = agentIndex / GRID_SIZE;  // Utilisation de l'indice de l'agent pour définir l'état initial en y

        addBehaviour(new QLearningBehaviour());
    }


    private class QLearningBehaviour extends CyclicBehaviour {
        public void action() {
            int currentState = state_x * GRID_SIZE + state_y;
            int action = chooseAction(0.4);
            int nextState = executeAction(action);
            int nextAction = chooseAction(0);

            qTable[currentState][action] = qTable[currentState][action] +
                    ALPHA * (grid[state_x][state_y] + GAMMA * qTable[nextState][nextAction] - qTable[currentState][action]);

            state_x = nextState / GRID_SIZE;
            state_y = nextState % GRID_SIZE;

            if (finished()) {
                showResult();
                sendResultToSelector();
                isFinished=true;
                doDelete();
            }
        }
    }

    private int calculateGoalState(int agentIndex) {
        // Extraction du dernier caractère du nom de l'agent et le parsing en tant qu'entier
        String agentName = getLocalName();
        int lastCharacter = Integer.parseInt(agentName.substring(agentName.length() - 1));

        // Calcul de l'état objectif pour chaque agent en fonction de son index
        // Cela peut être une logique spécifique à votre problème
        return (lastCharacter + 1) % (GRID_SIZE * GRID_SIZE);
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
        int new_x = Math.max(0, Math.min(actions[actIndex][0] + state_x, GRID_SIZE - 1));
        int new_y = Math.max(0, Math.min(actions[actIndex][1] + state_y, GRID_SIZE - 1));

        return new_x * GRID_SIZE + new_y;
    }

    private boolean finished() {
        return (state_x * GRID_SIZE + state_y) == goalState && !isFinished;
    }

    private void showResult() {
        System.out.println("Agent " + getLocalName() + " - Final state: " + (state_x * GRID_SIZE + state_y));
    }

    private void sendResultToSelector() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(String.valueOf(state_x * GRID_SIZE + state_y));

        List<AID> selectorAgents = getSelectorAgents();
        for (AID selectorAgent : selectorAgents) {
            message.addReceiver(selectorAgent);
        }

        send(message);
    }


    private List<AID> getSelectorAgents() {
        List<AID> selectors = new ArrayList<>();
        selectors.add(new AID("SelectorAgent", AID.ISLOCALNAME));
        return selectors;
    }

}

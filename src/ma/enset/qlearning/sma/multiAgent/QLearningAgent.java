package ma.enset.qlearning.sma.multiAgent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Random;

public class QLearningAgent extends Agent {
    private final double ALPHA = 0.1;
    private final double GAMMA = 0.9;
    private final int MAX_EPOCH = 10000;
    private final int GRID_SIZE = 3;
    private final int ACTION_SIZE = 4;
    private int[][] grid;
    private double[][] qTable = new double[GRID_SIZE * GRID_SIZE][ACTION_SIZE];
    private int[][] actions;
    private int state_x;
    private int state_y;

    @Override
    protected void setup() {
        actions = new int[][]{
                {0, -1},  // gauche
                {0, 1},   // droit
                {1, 0},   // down
                {-1, 0}   // up
        };
        grid = new int[][]{
                {0, 0, 1},
                {0, -1, 0},
                {0, 0, 0},
        };

        addBehaviour(new QLearningBehaviour());
    }

    private class QLearningBehaviour extends CyclicBehaviour {
        private int epoch = 0;
        private Random rnd = new Random();

        @Override
        public void action() {
            resetState();
            while (!finished()) {
                int currentState = state_x * GRID_SIZE + state_y;
                int action = chooseAction(0.4);
                int nextState = executeAction(action);
                int nextAction = chooseAction(0);
                qTable[currentState][action] = qTable[currentState][action]
                        + ALPHA * (grid[state_x][state_y] + GAMMA * qTable[nextState][nextAction] - qTable[currentState][action]);
            }

            epoch++;

            if (epoch >= MAX_EPOCH) {
                showResult();
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Finished");
                msg.addReceiver(getMainAgent());
                send(msg);
                block();
            }
        }

        private int chooseAction(double eps) {
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

        private void showResult() {
            System.out.println("****** Q-table ******");
            for (double[] line : qTable) {
                System.out.print("[");
                for (double qvalue : line) {
                    System.out.printf("%.2f ", qvalue);
                }
                System.out.println("]");
            }

            System.out.println();
            resetState();
            while (!finished()) {
                int action = chooseAction(0);
                System.out.println("State: " + (state_x * GRID_SIZE + state_y) + " Action: " + action);
                executeAction(action);
            }
            System.out.println("Final state: " + (state_x * GRID_SIZE + state_y));
        }

        private void resetState() {
            state_x = 2;
            state_y = 0;
        }
    }

    public static void main(String[] args) throws StaleProxyException {
        jade.core.Runtime rt = jade.core.Runtime.instance();
        rt.setCloseVM(true);

        Profile mainAgentProfile = new ProfileImpl();
        AgentContainer mainAgentContainer = rt.createMainContainer(mainAgentProfile);
        try {
            AgentController mainAgentController = mainAgentContainer.createNewAgent("MainAgent", MainAgent.class.getName(), null);
            mainAgentController.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }

        Profile agentLauncherProfile = new ProfileImpl();
        AgentContainer agentLauncherContainer = rt.createAgentContainer(agentLauncherProfile);
        AgentController agentLauncherController = agentLauncherContainer.createNewAgent("AgentLauncher", AgentLauncher.class.getName(), null);
        agentLauncherController.start();

        Profile qLearningAgentProfile = new ProfileImpl();
        AgentContainer qLearningAgentContainer = rt.createAgentContainer(qLearningAgentProfile);
        AgentController qLearningAgentController = qLearningAgentContainer.createNewAgent("QLearningAgent", ma.enset.qlearning.sma.multiAgent.QLearningAgent.class.getName(), null);
        qLearningAgentController.start();
    }

    public AID getMainAgent() {
        return new AID("MainAgent", AID.ISLOCALNAME);
    }
}

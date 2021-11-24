package Tanks.Game;



import Tanks.Ecxeptions.IncorrectSpawn;
import Tanks.Field.AdjMatrixGraph;
import Tanks.Field.Digraph;
import Tanks.Field.PartField;
import Tanks.Field.Graph;
import Tanks.Tanks.Enemy;
import Tanks.Tanks.Player;
import Tanks.Tanks.Tank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@JsonSerialize(as= GameLogic.class)
public class GameLogic {
    @JsonIgnore
    private static final char PLAYER = 'И';
    @JsonIgnore
    private static final char ENEMY = 'В';
    @JsonIgnore
    private static final char EMPTY = '_';
    @JsonIgnore
    private static final char WALL = '*';
    @JsonProperty("graph")
    private Graph graph;
    @JsonProperty("partFields")
    private ArrayList<PartField> partFields;
    @JsonProperty("enemyTanks")
    private ArrayList<Tank> enemyTanks;

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setPartFields(ArrayList<PartField> partFields) {
        this.partFields = partFields;
    }

    public void setEnemyTanks(ArrayList<Tank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }

    public Graph getGraph() {
        return graph;
    }

    public ArrayList<PartField> getPartFields() {
        return partFields;
    }

    public ArrayList<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    Player playerTank = new Player(null, 1, 1, 1);

    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    protected GameLogic(Graph graph, ArrayList<PartField> partFields, ArrayList<Tank> enemyTanks) {
        this.graph = graph;
        this.partFields = partFields;
        this.enemyTanks = enemyTanks;
    }

    protected GameLogic(int spawn, int bots) throws Exception {
        if (spawn > 35 || spawn < 0) { //проверка на правильность спавна
            throw new IncorrectSpawn();
        }
        ArrayList<Tank> enemyTanks = new ArrayList<>();
        ArrayList<PartField> partFields = new ArrayList<>();
        Graph battleField = new AdjMatrixGraph();

        int countVertex = 0;
        //создание связей игрового поля
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (j < 5) {
                    battleField.addAdge(countVertex, countVertex + 1);
                }
                if (i != 5) {
                    battleField.addAdge(countVertex, countVertex + 6);
                }
                countVertex++;

            }
        }

        for (int i = 0; i < 36; i++) {
            partFields.add(new PartField(i, EMPTY));
        }
        spawnPlayer(partFields, playerTank, spawn);
        for (int i = 0; i < bots; i++) {
            enemyTanks.add(new Enemy(null, 1, 1, 1));
            spawnEnemy(partFields, (Enemy) enemyTanks.get(i), spawn);
        }
        spawnWall(partFields);

        this.enemyTanks = enemyTanks;
        this.partFields = partFields;
        graph = battleField;
    }

    protected String showBattlefield() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        sb.append("_".repeat(13));
        sb.append("\n");
        int count = 0;
        for (int i = 0; i < 6; i++) {
            sb.append(" | ");
            for (int j = 0; j < 6; j++) {
                sb.append(partFields.get(count).getType()).append(" ");
                count++;
            }
            sb.append("|");
            sb.append("\n");
        }
        return sb.toString();
    }

    private static void spawnWall(ArrayList<PartField> partFields) {
        Random random = new Random();
        int numWalls = random.nextInt(9);
        for (int i = 0; i < numWalls; i++) {
            int index = random.nextInt(36);

            for (PartField p : partFields) {
                if (p.getIndex() == index && p.getType() == EMPTY) {
                    p.setType(WALL);
                }
            }
        }
    }

    private static void spawnEnemy(ArrayList<PartField> partFields, Enemy playerTank, int spawnPlayer) {
        Random random = new Random();
        int spawn = spawnPlayer;
        while (spawn == spawnPlayer) {
            spawn = random.nextInt(36);
        }
        for (PartField p : partFields) {
            if (p.getIndex() == spawn) {
                p.setType(ENEMY);
                playerTank.setPartField(p);
            }
        }

    }

    private static void spawnPlayer(ArrayList<PartField> partFields, Player playerTank, int spawn) {
        for (PartField p : partFields) {
            if (p.getIndex() == spawn) {
                p.setType(PLAYER);
                playerTank.setPartField(p);
            }
        }
    }


    public List<Integer> varMove() {
        List<Integer> variants = new ArrayList<>();
        int index = playerTank.getPartField().getIndex();
        // вниз
        if (index < 30 && graph.isAdj(index, partFields.get(index + 6).getIndex()) &&
                partFields.get(index + 6).getType() != ENEMY && partFields.get(index + 6).getType() != WALL) {
            variants.add(1);
        }
        // вверх
        if (index > 5 && graph.isAdj(index, partFields.get(index - 6).getIndex()) &&
                partFields.get(index - 6).getType() != ENEMY && partFields.get(index - 6).getType() != WALL) {
            variants.add(2);
        }
        // влево
        if (index > 0 && graph.isAdj(index, partFields.get(index - 1).getIndex()) &&
                partFields.get(index - 1).getType() != ENEMY && partFields.get(index - 1).getType() != WALL) {
            variants.add(3);
        }
        // вправо
        if (index < 35 && graph.isAdj(index, partFields.get(index + 1).getIndex()) &&
                partFields.get(index + 1).getType() != ENEMY && partFields.get(index + 1).getType() != WALL) {
            variants.add(4);
        }
        return variants;
    }

    public void PlayerMove(int nextInt) {
        //вниз
        if (nextInt == 1) {
            partFields.get(playerTank.getPartField().getIndex()).setType(EMPTY);
            partFields.get(playerTank.getPartField().getIndex() + 6).setType(PLAYER);
            playerTank.move(graph, partFields.get(playerTank.getPartField().getIndex()), partFields.get(playerTank.getPartField().getIndex() + 6));
            logger.info("Игрок сходил вниз");

        }
        //вверх
        if (nextInt == 2) {
            partFields.get(playerTank.getPartField().getIndex()).setType(EMPTY);
            partFields.get(playerTank.getPartField().getIndex() - 6).setType(PLAYER);
            playerTank.move(graph, partFields.get(playerTank.getPartField().getIndex()), partFields.get(playerTank.getPartField().getIndex() - 6));
            logger.info("Игрок сходил вверх");
        }
        //влево
        if (nextInt == 3) {
            partFields.get(playerTank.getPartField().getIndex()).setType(EMPTY);
            partFields.get(playerTank.getPartField().getIndex() - 1).setType(PLAYER);
            playerTank.move(graph, partFields.get(playerTank.getPartField().getIndex()), partFields.get(playerTank.getPartField().getIndex() - 1));
            logger.info("Игрок сходил влево");
        }
        //вправо
        if (nextInt == 4) {
            partFields.get(playerTank.getPartField().getIndex()).setType(EMPTY);
            partFields.get(playerTank.getPartField().getIndex() + 1).setType(PLAYER);
            playerTank.move(graph, partFields.get(playerTank.getPartField().getIndex()), partFields.get(playerTank.getPartField().getIndex() + 1));
            logger.info("Игрок сходил вправо");

        }
    }

    public boolean canShoot() {
        int player = playerTank.getPartField().getIndex();
        return player < 30 && partFields.get(player + 6).getType() == ENEMY ||
               player > 5 && partFields.get(player - 6).getType() == ENEMY ||
               player < 35 && partFields.get(player + 1).getType() == ENEMY ||
               player > 0 && partFields.get(player - 1).getType() == ENEMY;
    }

    public void makeShoot() {
        int count = 0;
        int playerInd = partFields.get(playerTank.getPartField().getIndex()).getIndex();
        while(!graph.isAdj(playerInd, partFields.get(enemyTanks.get(count).getPartField().getIndex()).getIndex())) {
            count++;
        }
        playerTank.shoot(graph, enemyTanks.get(count), playerTank.getDamage());
        if (!enemyTanks.get(count).isAlive()) {
            partFields.get(enemyTanks.get(count).getPartField().getIndex()).setType(EMPTY);
            enemyTanks.remove(count);
        }
        logger.info("игрок сделал залп");
    }

    public void BotRound() {
        Random random = new Random();
        int numberBot = random.nextInt(5);
        while (numberBot > enemyTanks.size() - 1) {
            numberBot = random.nextInt(5);
        }
        if(canShootBot(numberBot)) {
            makeShootBot(numberBot);
        } else {
            botMove(random.nextInt(5),numberBot);
        }
        logger.info("бот " + numberBot + " закончил ход");
    }
    private boolean canShootBot(int numberBot) {
        int enemy = enemyTanks.get(numberBot).getPartField().getIndex();
        return enemy < 30 && partFields.get(enemy + 6).getType() == PLAYER ||
                enemy > 5 && partFields.get(enemy - 6).getType() == PLAYER ||
                enemy < 35 && partFields.get(enemy + 1).getType() == PLAYER ||
                enemy > 0 && partFields.get(enemy - 1).getType() == PLAYER;
    }
    private void makeShootBot(int numberBot) {
        enemyTanks.get(numberBot).shoot(graph,playerTank,enemyTanks.get(numberBot).getDamage());
        logger.info("Бот сделал залп");
    }
    private void botMove(int nextInt, int bot) {
        Tank enemy = enemyTanks.get(bot);
        int index = enemy.getPartField().getIndex();
        //вниз
        if (index < 30 && nextInt == 1 && graph.isAdj(index, index + 6) && partFields.get(index + 6).getType() == EMPTY) {
            partFields.get(index).setType(EMPTY);
            partFields.get(index + 6).setType(ENEMY);
            enemy.move(graph, partFields.get(index), partFields.get(index + 6));
            logger.info("Бот сходил вниз");

        }
        //вверх
        if (index > 5 && nextInt == 2 && graph.isAdj(index, index - 6) && partFields.get(index - 6).getType() == EMPTY) {
            partFields.get(index).setType(EMPTY);
            partFields.get(index - 6).setType(ENEMY);
            enemy.move(graph, partFields.get(index), partFields.get(index - 6) );
            logger.info("Бот сходил вверх");
        }
        //влево
        if (index > 0 && nextInt == 3 && graph.isAdj(index, index - 1) && partFields.get(index - 1).getType() == EMPTY) {
            partFields.get(index).setType(EMPTY);
            partFields.get(index - 1).setType(ENEMY);
            enemy.move(graph, partFields.get(index), partFields.get(index - 1));
            logger.info("Бот сходил влево");
        }
        //вправо
        if (index < 35 && nextInt == 4 && graph.isAdj(index, index + 1) && partFields.get(index + 1).getType() == EMPTY ){
            partFields.get(index).setType(EMPTY);
            partFields.get(index + 1).setType(ENEMY);
            enemy.move(graph, partFields.get(index), partFields.get(index + 1));
            logger.info("Бот сходил вправо");
        }
    }

    public boolean gameOver() {
        if(!playerTank.isAlive()) {
            logger.info("Конец игры");
            return true;
        }
        if(enemyTanks.size() == 0) {
            logger.info("Конец игры");
            return true;
        }
        return false;
    }





}

package ru.vsu.cs.tanks.Game;

import ru.vsu.cs.tanks.Field.AdjListsGraph;
import ru.vsu.cs.tanks.Field.Graph;
import ru.vsu.cs.tanks.Field.PartField;
import ru.vsu.cs.tanks.Tanks.Enemy;
import ru.vsu.cs.tanks.Tanks.Player;
import ru.vsu.cs.tanks.Tanks.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private static final char PLAYER = 'И';
    private static final char ENEMY = 'В';
    private static final char EMPTY = '_';
    private static final char WALL = '*';

    private final Graph graph;
    private final ArrayList<PartField> partFields;
    private final ArrayList<Tank> enemyTanks;

    Player playerTank = new Player(null,2,1,1);
    
    protected GameLogic(int spawn, int bots) throws Exception {
        if(spawn > 35 || spawn < 0) {
            throw new Exception("IncorrectSpawn");
        }
        if(bots > 5 || bots < 1) {
            throw new Exception("IncorrectSpawn");
        }

        ArrayList<Tank> enemyTanks = new ArrayList<>();
        ArrayList<PartField> partFields = new ArrayList<>();
        Graph battleField = new AdjListsGraph();

        for(int i = 0; i < bots; i++) {
            enemyTanks.add(new Enemy(null,1,1,1));
        }
        for(int i = 0; i < 36; i++) {
            partFields.add(new PartField(i, EMPTY));
        }
        int countVertex = 0;
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 5; j++){
                battleField.addAdge(countVertex, countVertex + 1);
                if(i != 5) {
                    battleField.addAdge(countVertex, countVertex + 6);

                }
                countVertex++;
            }
            countVertex++;
        }
        spawnPlayer(partFields,playerTank,spawn);
        for(int i = 0; i < bots; i++) {
            spawnEnemy(partFields, (Enemy) enemyTanks.get(i),spawn);
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
        for(int i = 0; i < 6; i++){
            sb.append(" | ");
            for(int j = 0; j < 6; j++) {
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
        for(int i = 0; i < numWalls; i++) {
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
        for(PartField p : partFields) {
            if(p.getIndex() == spawn) {
                p.setType(ENEMY);
                playerTank.setPartField(p);
            }
        }

    }

    private static void spawnPlayer(ArrayList<PartField> partFields, Player playerTank, int spawn) {
        for(PartField p : partFields) {
            if(p.getIndex() == spawn) {
                p.setType(PLAYER);
                playerTank.setPartField(p);
            }
        }
    }



    public List<Integer> varMove() {
        List<Integer> variants = new ArrayList<>();
        int var = playerTank.getPartField().getIndex();
            // вниз
            if(var < 30 && graph.isAdj(playerTank.getPartField().getIndex(), partFields.get(var + 6).getIndex()) &&
                    partFields.get(var + 6).getType() != ENEMY && partFields.get(var + 6).getType() != WALL) {
                variants.add(1);
            }
            // вверх
            if(var > 5 && graph.isAdj(playerTank.getPartField().getIndex(), partFields.get(var - 6).getIndex()) &&
                    partFields.get(var - 6).getType() != ENEMY && partFields.get(var - 6).getType() != WALL) {
                variants.add(2);
            }
            // влево
            if(var > 0 &&graph.isAdj(playerTank.getPartField().getIndex(), partFields.get(var - 1).getIndex()) &&
                    partFields.get(var - 1).getType() != ENEMY && partFields.get(var - 1).getType() != WALL) {
                variants.add(3);
            }
            // вправо
            if (var < 35 && graph.isAdj(playerTank.getPartField().getIndex(), partFields.get(var + 1).getIndex()) &&
                    partFields.get(var + 1).getType() != ENEMY && partFields.get(var + 1).getType() != WALL) {
                variants.add(4);
            }
        return variants;
    }

    public void BotRound() {
    }

    public boolean GameOver() {
        return false;
    }
    public void PlayerMove(int nextInt) {
    }

    public boolean canShoot() {
        return true;
    }

    public void makeShoot() {
    }

    public boolean canRepair() {
        return true;
    }
    public void repair() {
    }
}

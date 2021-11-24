package Tanks.Game;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Game {
    public static final String RED = "\u001B[31m";


    //mvn exec:java -Dexec.mainClass=com.app.Tanks
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println(RED + "*****....................*ТАНЧИКИ*....................*****");
        System.out.println(RED + "*****............*Звуки военного оркестра*............*****");
        System.out.println("1 - Пролог\n2 - Загрузить игру\n3 - Начать новую игру");
        GameLogic battle = null;
        while (true) {
            switch (scanner.nextInt()) {
                case 1 -> {
                    GameLogic battleDemo = new GameLogic(15,10);
                    String battlefield = battleDemo.showBattlefield();
                    System.out.print(battlefield);
                    System.out.println("Наш союзник попал в засаду...");
                    System.out.println("Посмотрите что с ним творят мерзкие враги...");
                    while (!battleDemo.gameOver()) {
                        System.out.println("Наш товарищ пытается найти врага");
                        if(battleDemo.canShoot()) {
                            battleDemo.makeShoot();
                            System.out.println("Отличное попадание");
                            battlefield = battleDemo.showBattlefield();
                            System.out.print(battlefield);
                        } else {
                            System.out.println("Враги не подошли на растояние прямой наводки");
                        }
                        if(battleDemo.gameOver()){
                            System.out.println("Наш товарищ выстоял");
                            break;
                        }
                        battleDemo.BotRound();
                        System.out.println("Враги не дремют");
                        battlefield = battleDemo.showBattlefield();
                        System.out.print(battlefield);
                    }
                }
                case 2 -> {
                    ObjectMapper mapper = new ObjectMapper();
                    GameLogic inputBattle = mapper.readValue("Save.json", GameLogic.class);

                }
                case 3 -> {
                    int spawn = -1;
                    while (spawn < 0 || spawn > 35) {
                        System.out.println(RED + "выберите место спавна от 0 до 35...");
                        spawn = scanner.nextInt();
                    }
                    int bots = -1;
                    while (bots < 1 || bots > 5) {
                        System.out.println(RED + "выберите количество ботов от 1 до 5...");
                        bots = scanner.nextInt();
                    }
                    GameLogic battleNew = new GameLogic(spawn,bots);
                    battle(scanner, battleNew);
                }

            }
            break;
        }


    }
    public static void battle(Scanner scanner, GameLogic battleInput) throws Exception {

        System.out.println(RED + "*****................ДА НАЧНЕТСЯ БИТВА!...............*****");
        GameLogic battle = battleInput;
        String battlefield = battle.showBattlefield();
        System.out.print(battlefield);

        while (true) {
            System.out.println("-----------Ваш ход-----------");
            System.out.println("Выберите действие:");
            System.out.println("1 - двигаться.\n2 - стрелять.\n3 - выйти из игры.\n4 - Сохранить игру.");

            int action = scanner.nextInt();
            boolean stopGame = false;
            switch (action) {
                case 1 -> {
                    System.out.println("Выберите направление");
                    for(int i = 0; i < battle.varMove().size(); i++) {
                        if((battle.varMove().get(i)) == 1) {
                            System.out.println(battle.varMove().get(i) + " - вниз.");
                        }
                        if((battle.varMove().get(i)) == 2) {
                            System.out.println(battle.varMove().get(i) + " - вверх.");
                        }
                        if((battle.varMove().get(i)) == 3) {
                            System.out.println(battle.varMove().get(i) + " - влево.");
                        }
                        if((battle.varMove().get(i)) == 4) {
                            System.out.println(battle.varMove().get(i) + " - вправо.");
                        }
                    }
                    int choose = scanner.nextInt();
                    while (!battle.varMove().contains(choose)) {
                        System.out.println("Выберите направление");
                        choose = scanner.nextInt();
                    }
                    battle.PlayerMove(choose);
                }
                case 2 -> {
                    if (battle.canShoot()) {
                        battle.makeShoot();
                        System.out.println("Попадание!");
                    } else {
                        System.out.println("Нет противника в зоне видимости.");
                    }
                }
                case 3 -> {
                    System.out.println("Вы уверены, что хотите покинуть игру?\n1 - да.\n2 - нет.");
                    if(scanner.nextInt() == 1) {
                        stopGame = true;
                    }
                    else System.out.println("Добро пожаловать обратно!");

                }
                case 4 -> {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(Paths.get("battle.json").toFile(), battle);
                    continue;
                }
            }
            if(stopGame) {
                break;
            }
            if (battle.gameOver()) {
                System.out.println("Победа!");
                break;
            }
            System.out.println("-----------Ход противника-----------");
            battle.BotRound();
            System.out.println("Сложные вычисления искуственного интелекта...");
            if (battle.gameOver()) {
                System.out.println("Ваш танк уничтожен");
                break;
            }
            System.out.println(battle.showBattlefield());

        }
    }
}

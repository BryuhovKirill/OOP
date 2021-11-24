package ru.vsu.cs.tanks.Game;
import java.util.Scanner;

public class Game {
    public static final String RED = "\u001B[31m";


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println(RED + "*****....................*ТАНЧИКИ*....................*****");
        System.out.println(RED + "*****................ДА НАЧНЕТСЯ БИТВА!...............*****");
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
        GameLogic battle = new GameLogic(spawn, bots);
        String battlefield = battle.showBattlefield();
        System.out.print(battlefield);

        while (true) {
            System.out.println("-----------Ваш ход-----------");
            System.out.println("Выберите действие:");
            System.out.println("1 - двигаться.\n2 - стрелять.\n3 - выйти из игры.");

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
                    battle.PlayerMove(scanner.nextInt());
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
            }
            if(stopGame) {
                break;
            }
            if (battle.GameOver()) {
                System.out.println("Победа!");
                break;
            }
            System.out.println("-----------Ход противника-----------");
            battle.BotRound();
            System.out.println("Сложные вычисления искуственного интелекта...");
            if (battle.GameOver()) {
                System.out.println("Ваш танк уничтожен");
                break;
            }
            System.out.println(battle.showBattlefield());
        }
    }
}

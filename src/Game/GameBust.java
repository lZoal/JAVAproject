package Game;

import java.util.Scanner;

//잠수함의 객체 설정 및 게임 플레이 GameHelper와 SubM 클래스 이용
public class GameBust {
    Scanner sc=new Scanner(System.in);
    GameHelper game;
    SubM player1,player2;

    public void SetUpGame(){
        String name;
        int GameSize;
        System.out.printf("---------------------\n잠수함게임\n---------------------");
        System.out.println("\n게임판 사이즈 입력 10~26: ");
        GameSize=sc.nextInt();
        game= new GameHelper(GameSize);
        System.out.println("플레이어1 이름 입력: ");
        name=sc.next();
        player1 = new SubM(name);
        System.out.println("플레이어2 이름 입력: ");
        name=sc.next();
        player2 = new SubM(name);

        System.out.println("플레이어를 배치합니다.");
        game.PlaceSubMarine(player1);
        game.PlaceSubMarine(player2);

    }
    public void StartPlaying(){
        int choice;
        while(true) {
            game.printGrid(player1);
            System.out.printf("%s의차례 \n행동을 선택하세요.\n\n 1.이동\n\n 2.공격\n",player1.getName());

            choice=sc.nextInt();
            while(choice <=0 || choice >=3) {
                System.out.println("잘못 입력 하셨습니다 다시입력해주세요.");
                choice=sc.nextInt();
            }

            switch (choice) {
                case 1 -> {
                    game.printGrid(player1);
                    game.MoveSubMarine(player1);
                    game.printGrid(player1);
                    System.out.println();
                }
                case 2 -> {
                    game.printGrid(player1);
                    game.GridAttack(player1);
                    game.printGrid(player1);
                    System.out.println();
                }
            }
            if(game.IsEnd()) break;
            game.printGrid(player2);
            System.out.printf("%s의차례 \n행동을 선택하세요.\n\n 1.이동\n\n 2.공격",player2.getName());
            choice=sc.nextInt();
            while(choice <=0 || choice >=3) {
                System.out.println("잘못 입력 하셨습니다 다시입력해주세요.");
                choice=sc.nextInt();
            }
            switch (choice) {
                case 1 -> {
                    game.printGrid(player2);
                    game.MoveSubMarine(player2);
                    game.printGrid(player2);
                    System.out.println();
                }
                case 2 -> {
                    game.printGrid(player2);
                    game.GridAttack(player2);
                    game.printGrid(player2);
                    System.out.println();
                }
            }
            if(game.IsEnd()) break;

        }
        System.out.println("게임이 종료되었습니다.");
    }
}

import java.util.Scanner;

/**
 * Created by luke1998 on 10/11/16.
 * 斗兽棋
 * 该类为主类
 */
public class AnimalFight {
    static int[][] animalsMap = Origins.animalsMap;
    //从Origins里面调用animalsMap变量
    private static boolean countStep = true;
    //初始化countStep布尔变量，用来表示将要作出操作的玩家（true则玩家一操作，false则玩家二操作）
    private static int[][] player1animals = Origins.player1animals;
    private static int[][] player2animals = Origins.player2animals;
    //从Origins里面调用两个整形数组，初始化数组player1animals，player2animals，用来储存当前局的动物分布
    private static int[][] playerAnimals = new int[7][9];
    //用来操作当前动物的移动
    private static String playerInput;
    //用来存放用户（玩家）输入
    private static char playerMove;
    //将用户（玩家）输入分解为动物和方向，playerMove存放方向
    private static int playerAnimal;
    //将用户（玩家）输入分解为动物和方向，playerAnimal存放被移动的动物
    private static boolean judgeInputRange;
    //储存输入范围正确性的布尔值（为什么要单列一个变量来储存布尔值是为了防止 通过重复调用方法来获得布尔值）
    private static int destination;
    //用来储存目的地存在动物整形变量
    private static boolean judgeInputFormat;
    //储存输入合法性判断的布尔值（为什么要单列一个变量来储存布尔值是为了防止 通过重复调用方法来获得布尔值）
    private static int[] destinationCoordinate;
    //用来储存目的地的坐标数组
    private static int[] originCoordinate;
    //被移动动物的初始位置坐标数组
    private static int currentStep = 0;
    //currentStep表示当前步数
    private static int biggestStep = 0;
    //biggestStep表示最大存储的步数，即存盘（游戏记录）
    private static int[][][] historyPlayer1 = new int[500][7][9];
    private static int[][][] historyPlayer2 = new int[500][7][9];
    //两个数组，用来储存历史局的动物分布
    private static boolean toHelp;
    //储存【是否输入为help系列】的布尔值（为什么要单列一个变量来储存布尔值是为了防止 通过重复调用方法来获得布尔值）
    private static boolean notGoDie;
    //用来存储动物是否送死的布尔值
    private static boolean animal1InHole;
    private static boolean animal2InHole;
    //两个布尔值用来反映是否有动物进入对方穴，用来帮助判断输赢
    private static Scanner player_move = new Scanner(System.in);
    //初始化（只初始化一次）Scanner用来接收用户输入

    //主方法
    public static void main(String[] args) {
        //第一局需要的各种内容用以下方法得到
        Origins.readOriginal();
        //从Origins里面调用readOriginal方法，用来（分别）读取地形和动物数组
        Origins.playerHasAnimals();
        //从Origins里面调用playerHasAnimals方法，用来处理上面读取的信息，分别初始化玩家一二的动物分布，并储存
        System.out.println("请玩家1输入");
        //用户输入提示
        setHistory0();
        //用来保存currentStep = 0时的历史棋局
        eat();
        printHistory();
        //打印当前步数和最大步数
        Printer.printMapInGame();
        //从Printer里面调用printMapInGame方法，用来打印棋盘

        while (true) {
            player1animals = copyArray(historyPlayer1[currentStep - 1]);
            player2animals = copyArray(historyPlayer2[currentStep - 1]);
            //将上一步的棋局赋给这局的player1animals，player2animals，为以后的操作铺路
            moveAndJudge();
            //动物移动的方法
            ErrorInstruction();
            //判断输入格式是否正确，并给出提示
            if (judgeInputFormat && judgeInputRange) {
                //若输入格式和范围都为true，则执行下面代码块
                if (countStep) {
                    //若轮到玩家1移动，则执行以下代码块
                    playerAnimals = player1animals;
                    //playerAnimals赋值为player1animals
                    playButWhichKind();
                    //这个方法封装（我也不知道封装什么意思，感觉像是）了移动的所有方法
                    player1animals = copyArray(playerAnimals);
                    //将playerAnimals返回给player1animals
                } else {
                    //不解释了，类似上面注释
                    playerAnimals = player2animals;
                    playButWhichKind();
                    player2animals = copyArray(playerAnimals);
                }
                if (!toHelp) {
                    //如果输入 *** 不 *** 为help系列，则记录当前棋局
                    setHistory();
                }
            }
            eat();
            //吃子
            System.out.println("玩家输入为： \"" + playerInput + "\"");
            printHistory();
            //打印当前步数和最大步数
            Printer.printMapInGame();
            countStep = !countStep;
            //换一个玩家操作
            judgeWinNoAnimalLeft();
            judgeWinAnimalInOppositeHole();
            //判断输入
        }

    }

    private static void ErrorInstruction() {
        //判断输入格式是否正确，并给出提示
        if (judgeInputFormat) {
            judgeInputRange = (
                    (playerAnimal <= 8 && playerAnimal >= 0)
                            && (playerMove == 'w' || playerMove == 'a'
                            || playerMove == 'd' || playerMove == 's'));
            if (!toHelp) {
                if (judgeInputRange) {
                } else {
                    System.out.println("输入范围错误，重新输入");
                    countStep = !countStep;
                    instruction();
                }
            } else {
                judgeInputRange = false;
                countStep = !countStep;
                instruction();
                //System.out.println("现在是第" + ((currentStep + 1) / 2 + 1) + "回合");
            }
        } else {
            System.out.println("输入格式错误，重新输入");
            countStep = !countStep;
            instruction();
        }
    }

    private static void instruction() {
        //玩家输入提示
        if (countStep)
            System.out.println("请玩家2输入");
        else
            System.out.println("请玩家1输入");
    }

    private static void moveAndJudge() {
        //将输入转为指令
        playerInput = player_move.nextLine();
        toHelp();
        try {
            playerAnimal = Integer.parseInt(String.valueOf(playerInput.charAt(0)));
            playerMove = playerInput.charAt(1);
            judgeInputFormat = true;
        } catch (Exception e) {
            judgeInputFormat = toHelp;
        }
    }


    private static void riverMove() {
        //遇到小河相关的操作时运行以下代码块
        if (playerAnimal == 1) {
            //若被移动动物为鼠
            clearRecordHistory();
            //清空记录
            normalPlay();
            //普通移动（一次走一格）
        } else if (playerAnimal == 6 || playerAnimal == 7) {
            //狮虎跳河
            if (noRatInTheWay()) {
                //若移动方向上 河中没有对方鼠，则跳河
                clearRecordHistory();
                crossRiverPlay();
                //一次跳多格
            }
        } else {
            System.out.println("不能跳河或入河，重新输入");
            countStep = !countStep;
            currentStep--;
            //回到当前玩家重新输入
        }
    }

    private static void normalPlay() {
        //动物普通移动
        if (judgeElephantTryToEatRat()) {
            countStep = !countStep;
            currentStep--;
        } else {
            int i = originCoordinate[0];
            int j = originCoordinate[1];
            if (notGoIntoSelfHole()) {
                switch (playerMove) {
                    case 'w':
                        move(-1, 0, i > 0);
                        break;
                    case 'a':
                        move(0, -1, j > 0);
                        break;
                    case 'd':
                        move(0, 1, j < 8);
                        break;
                    case 's':
                        move(1, 0, i < 6);
                        break;
                }
            }
        }
    }

    private static void findAnimalToMove() {
        //获得被移动的动物坐标
        originCoordinate = new int[]{10, 10};
        //缺省值为【10，10】
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (playerAnimals[i][j] == playerAnimal) {
                    originCoordinate = new int[]{i, j};
                }
            }
        }
    }

    private static void playButWhichKind() {
        //将移动细分为多种操作，由条件选择性执行
        findAnimalToMove();
        findDestinationCoordinate();
        notGoDie();
        if (notGoDie) {
            if (judgeDestinationIsRiver())
                riverMove();
            else {
                if ((originCoordinate[0] != 10 && destinationCoordinate[0] != 10) && judgeRatEatElephant())
                    ratEatElephant();
                else if (originCoordinate[0] == 10 || destinationCoordinate[0] == 10) {
                    countStep = !countStep;
                } else {
                    clearRecordHistory();
                    normalPlay();
                }
            }
        }
        instruction();
    }

    private static void findDestinationCoordinate() {
        //获得终点位置坐标
        int i = originCoordinate[0];
        int j = originCoordinate[1];
        if (i == 10 && j == 10) {
            destinationCoordinate = new int[]{10, 10};
        } else {
            switch (playerMove) {
                case 'w':
                    destinationCoordinate = new int[]{i - 1, j};
                    break;

                case 'a':
                    destinationCoordinate = new int[]{i, j - 1};
                    break;

                case 'd':
                    destinationCoordinate = new int[]{i, j + 1};
                    break;

                case 's':
                    destinationCoordinate = new int[]{i + 1, j};
                    break;
            }
        }
    }

    private static boolean judgeDestinationIsRiver() {
        //判断移动前方是否为河，用来判断是否执行【河有关移动操作】
        int i = destinationCoordinate[0];
        int j = destinationCoordinate[1];
        return (j > 2 && j < 6) && (((i > 0 && i < 3) || (i > 3 && i < 6)));
    }

    private static void move(int moveLengthI, int moveLengthJ, boolean range) {
        //移动方法（这是真真真真真真真真移动动作了）
        int i = originCoordinate[0];
        int j = originCoordinate[1];
        int m = destinationCoordinate[0];
        int n = destinationCoordinate[1];
        boolean canEat = (countStep && player1animals[i][j] >= player2animals[m][n])
                || (!countStep && player1animals[m][n] <= player2animals[i][j]);
        if (range) {
            destination = playerAnimals[i + moveLengthI][j + moveLengthJ];
            if (destination == 0) {
                if (canEat) {
                    if ((countStep && player1animals[i][j] > player2animals[m][n])
                            || (!countStep && player1animals[m][n] < player2animals[i][j]))
                        playerAnimals[i + moveLengthI][j + moveLengthJ] = playerAnimals[i][j];
                    else if ((countStep && player1animals[i][j] == player2animals[m][n] && player1animals[i][j] != 1)
                            || (!countStep && player1animals[m][n] == player2animals[i][j]) && player1animals[m][n] != 1)
                        playerAnimals[i + moveLengthI][j + moveLengthJ] = 0;
                    else if ((countStep && player1animals[i][j] == player2animals[m][n] && player1animals[i][j] == 1)
                                    || (!countStep && player1animals[m][n] == player2animals[i][j]) && player1animals[m][n] == 1)
                    playerAnimals[i + moveLengthI][j + moveLengthJ] = playerAnimals[i][j];

                    playerAnimals[i][j] = 0;
                    if (countStep) player2animals[i + moveLengthI][j + moveLengthJ] = 0;
                    else player1animals[i + moveLengthI][j + moveLengthJ] = 0;
                } else {
                    System.out.println("不能跳河送死，重新输入");
                    countStep = !countStep;
                    currentStep--;
                }
            } else {
                haveSelfAnimal();
            }
        } else
            outOfBound();
    }

    private static void crossRiverPlay() {
        //跨河移动
        int i = originCoordinate[0];
        int j = originCoordinate[1];
        switch (playerMove) {
            case 'w':
                destinationCoordinate = new int[]{i - 3, j};
                break;

            case 'a':
                destinationCoordinate = new int[]{i, j - 4};
                break;

            case 'd':
                destinationCoordinate = new int[]{i, j + 4};
                break;

            case 's':
                destinationCoordinate = new int[]{i + 3, j};
                break;
        }
        if (notGoIntoSelfHole()) {
            switch (playerMove) {
                case 'w':
                    move(-3, 0, i > 0);
                    break;
                case 'a':
                    move(0, -4, j > 0);
                    break;
                case 'd':
                    move(0, +4, j < 8);
                    break;
                case 's':
                    move(3, 0, i < 6);
                    break;
            }
        }
    }

    private static void eat() {
        //吃子（好像有点功能重复，不过为了不出bug我还是没改）
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                player2animals[i][j] += 10;
                if (player1animals[i][j] > player2animals[i][j] - 10) {
                    player2animals[i][j] = 0;
                    animalsMap[i][j] = player1animals[i][j];
                } else if (player2animals[i][j] - 10 > player1animals[i][j]) {
                    player1animals[i][j] = 0;
                    animalsMap[i][j] = player2animals[i][j];
                } else {
                    player2animals[i][j] = 0;
                    player1animals[i][j] = 0;
                    animalsMap[i][j] = 0;
                }
            }
        }
    }

    private static boolean notGoIntoSelfHole() {
        //不能进自己方兽穴
        int i = destinationCoordinate[0];
        int j = destinationCoordinate[1];
        if (i == 3 && j == 0 && countStep || i == 3 && j == 8 && !countStep) {
            System.out.println("不能进入自己的兽穴，重新输入");
            countStep = !countStep;
            currentStep--;
            return false;
        } else return true;
    }

    private static void notGoDie() {
        //不能送死
        try {
            int i = destinationCoordinate[0];
            int j = destinationCoordinate[1];
            int m = originCoordinate[0];
            int n = originCoordinate[1];
            animal1InHole = player1animals[i][j] != 0 && ((j == 7 && i == 3) || (j == 8 && (i == 2 || i == 4)));
            animal2InHole = player2animals[i][j] != 0 && ((j == 1 && i == 3) || (j == 0 && (i == 2 || i == 4)));
            if (animal1InHole || animal2InHole) {
                anythingEatInHole();
                currentStep--;
                notGoDie = false;
            } else if (!(player1animals[m][n] == 1 && player2animals[i][j] == 8 && countStep) &&
                    !(player2animals[m][n] == 1 && player1animals[i][j] == 8 && !countStep)) {
                notGoDie = (player1animals[m][n] >= player2animals[i][j]
                        && countStep) ||
                        (player2animals[m][n] >= player1animals[i][j]
                                && !countStep);
                if (!notGoDie) {
                    System.out.println("不能送死，重新输入");
                    countStep = !countStep;
                    currentStep--;
                }
            } else notGoDie = true;

        } catch (Exception e) {
            animalDoNotExist();
            notGoDie = false;
            countStep = !countStep;
            currentStep--;
        }
    }

    private static void judgeWinNoAnimalLeft() {
        //判断动物一个都不剩，就是某方胜利
        int[][] player;
        int all = 0;
        if (currentStep > 10) {
            if (countStep)
                player = historyPlayer1[biggestStep - 1];
            else player = historyPlayer2[biggestStep - 1];
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (player[i][j] == 0)
                        all++;
                }
            }
            if (all == 63) {
                System.out.println("Game Is Over, NoAnimalLeft");
                System.exit(0);
            }
        }
    }

    private static void judgeWinAnimalInOppositeHole() {
        //己方动物进入对方兽穴，则己方胜利
        int[][] player;
        if (currentStep > 10) {
            if (!countStep) {
                player = historyPlayer1[biggestStep - 1];
                if (player[3][8] != 0) {
                    System.out.println("Game Is Over, AnimalInOppositeHole");
                    System.exit(0);
                }
            } else {
                player = historyPlayer2[biggestStep - 1];
                if (player[3][0] != 0) {
                    System.out.println("Game Is Over, AnimalInOppositeHole");
                    System.exit(0);
                }
            }
        }
    }

    private static void haveSelfAnimal() {
        //提示"此处是己方棋子"
        System.out.println("此处是己方棋子");
        countStep = !countStep;
    }

    private static boolean judgeRatEatElephant() {
        //判断鼠吃象
        try {
            int animal_to_eat = ((countStep) ?
                    player2animals[destinationCoordinate[0]][destinationCoordinate[1]]
                    : player1animals[destinationCoordinate[0]][destinationCoordinate[1]]);
            if (playerAnimal == 1 && destination == 0 && animal_to_eat == 8) {
                System.out.println("ratEatElephant");
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean judgeElephantTryToEatRat() {
        //判断象想吃鼠
        try {
            int animal_to_eat = ((countStep) ?
                    player2animals[destinationCoordinate[0]][destinationCoordinate[1]]
                    : player1animals[destinationCoordinate[0]][destinationCoordinate[1]]);
            if (playerAnimal == 8 && destination == 0 && animal_to_eat == 1) {
                System.out.println("ElephantCannotEatRat");
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static void ratEatElephant() {
        //鼠可以吃象
        int i = originCoordinate[0];
        int j = originCoordinate[1];
        //鼠在河里的判断，如果true就不能吃象
        if ((j > 2 && j < 6) && (((i > 0 && i < 3) || (i > 3 && i < 6)))) {
            System.out.println("不能从水里攻击陆地上的象");
            countStep = !countStep;
            currentStep--;
        } else {
            if (countStep) {
                player2animals[destinationCoordinate[0]][destinationCoordinate[1]] = 0;
                player1animals[originCoordinate[0]][originCoordinate[1]] = 0;
                player1animals[destinationCoordinate[0]][destinationCoordinate[1]] = 1;
            } else {
                player1animals[destinationCoordinate[0]][destinationCoordinate[1]] = 0;
                player2animals[originCoordinate[0]][originCoordinate[1]] = 0;
                player2animals[destinationCoordinate[0]][destinationCoordinate[1]] = 1;
            }
        }
    }

    private static void anythingEatInHole() {
        //穴中动物可以被对方任意动物吃掉
        if (countStep && animal2InHole) {
            player2animals[destinationCoordinate[0]][destinationCoordinate[1]] = 0;
            player1animals[originCoordinate[0]][originCoordinate[1]] = 0;
            player1animals[destinationCoordinate[0]][destinationCoordinate[1]] = playerAnimal;
            System.out.println("anythingEatInHole");
        } else if (!countStep && animal1InHole) {
            player1animals[destinationCoordinate[0]][destinationCoordinate[1]] = 0;
            player2animals[originCoordinate[0]][originCoordinate[1]] = 0;
            player2animals[destinationCoordinate[0]][destinationCoordinate[1]] = playerAnimal;
            System.out.println("anythingEatInHole");
        }
    }

    private static boolean judgeIfRatInTheWay(int[][] pa_int_2) {
        //判断对方资本主义老鼠是不是在河中，阻挡我方伟大的共产主义老虎或者狮子跳河
        int i = originCoordinate[0];
        int j = originCoordinate[1];
        switch (playerMove) {
            case 'w':
                int w1 = pa_int_2[i - 1][j];
                int w2 = pa_int_2[i - 2][j];
                return w1 == 0 && w2 == 0;
            case 'a':
                int a1 = pa_int_2[i][j - 1];
                int a2 = pa_int_2[i][j - 2];
                int a3 = pa_int_2[i][j - 3];
                return a1 == 0 && a2 == 0 && a3 == 0;
            case 'd':
                int d1 = pa_int_2[i][j + 1];
                int d2 = pa_int_2[i][j + 2];
                int d3 = pa_int_2[i][j + 3];
                return d1 == 0 && d2 == 0 && d3 == 0;
            case 's':
                int s1 = pa_int_2[i + 1][j];
                int s2 = pa_int_2[i + 2][j];
                return s1 == 0 && s2 == 0;
            default:
                return true;
        }
    }

    private static boolean noRatInTheWay() {
        //对方资本主义老鼠不在河中，我方狮子或者老虎举着党和人民的伟大旗帜，领导共产主义向河对岸发展（解放河对岸的动物）
        boolean noRatInTheWay1 = judgeIfRatInTheWay(player2animals);
        boolean noRatInTheWay2 = judgeIfRatInTheWay(player1animals);
        boolean noRatInTheWay = ((countStep) ? (noRatInTheWay1) : (noRatInTheWay2));
        if (noRatInTheWay) {
            System.out.println("noRatInTheWay");
            return true;
        } else {
            System.out.println("haveRatInTheWay,不能跳河，重新输入");
            countStep = !countStep;
            return false;
        }
    }

    private static void outOfBound() {
        //动物出界
        System.out.println("超出边界，重新输入");
        countStep = !countStep;
    }

    private static void animalDoNotExist() {
        System.out.println("动物不存在，重新输入");
    }

    private static boolean help1() {
        if (playerInput.equals("help1")) {
            System.out.println("helpInfo1:\n" +
                    "游戏规则：\n" +
                    "斗兽棋的棋盘 斗兽棋的棋盘横七列，纵九行，棋子放在格子中。\n" +
                    "双方底在线各有三个陷阱（作品字排）和一个兽穴(于品字中间)。 棋牌中部有两片水域，称之为小河。\n" +
                    "斗兽棋的棋子 斗兽棋棋子共十六个，分为左（玩家1）右（玩家2）双方，\n" +
                    "双方各有八只一样的棋子（下称为：兽 或 动物），按照战斗力强弱排列为：象>狮>虎>豹>狗>狼>猫>鼠。\n" +
                    "斗兽棋的走法 游戏开始时，左（玩家1）方先走，然后轮流走棋。每次可走动一只兽，每只兽每次走一方格，\n" +
                    "除己方兽穴和小河以外，前后左右均可。但是，狮、虎、鼠还有不同走法：\n" +
                    "狮虎跳河法：狮虎在小河边时，可以纵横对直跳过小河，且能把小河对岸的敌方较小的兽类吃掉，\n" +
                    "但是如果对方老鼠在河里，把跳的路线阻隔就不能跳，若对岸是对方比自己战斗力前的兽，也不可以跳过小河；\n" +
                    "鼠游过河法：鼠是唯一可以走入小河的兽，走法同陆地上一样，每次走一格，上下左右均可，\n" +
                    "而且，陆地上的其他兽不可以吃小河中的鼠，小河中的鼠也不能吃陆地上的象，鼠类互吃不受小河影响。\n" +
                    "斗兽棋的吃法 斗兽棋吃法分普通吃法和特殊此法，普通吃法是按照兽的战斗力强弱，强者可以吃弱者。 特殊吃法如下： \n" +
                    "1、鼠吃象法：八兽的吃法除按照战斗力强弱次序外，惟鼠能吃象，象不能吃鼠。 \n" +
                    "2、互吃法：凡同类相遇，可互相吃。 \n" +
                    "3、陷阱：棋盘设陷阱，专为限制敌兽的战斗力（自己的兽，不受限制），\n" +
                    "敌兽走入陷阱，即失去战斗力，本方的任意兽类都可以吃去陷阱里的兽类。\n" +
                    "综合普通吃法和特殊吃法，将斗兽棋此法总结如下：\n" +
                    "鼠可以吃象、鼠 猫可以吃猫、鼠； \n" +
                    "狼可以吃狼、猫、鼠； 狗可以吃狗、狼、猫、鼠； \n" +
                    "豹可以吃豹、狗、狼、猫、鼠； \n" +
                    "虎可以吃虎、豹、狗、狼、猫、鼠； \n" +
                    "狮可以吃狮、虎、豹、狗、狼、猫、鼠； \n" +
                    "象可以吃象、狮、虎、豹、狗、狼、猫；\n");
            return true;
        } else return false;
    }

    private static boolean help2() {
        if (playerInput.equals("help2")) {
            System.out.println("helpInfo2:\n" +
                    "字母\tw\ta\ts\td\n" +
                    "含义\t向上\t向左\t向下\t向右\n" +
                    "数字:\t1\t2\t3\t4\t5\t6\t7\t8\n" +
                    "含义:\t鼠\t猫\t狼\t狗\t豹\t虎\t狮\t象\n");
            return true;
        } else return false;
    }

    private static void toHelp() {
        toHelp = (help() || help1() || help2() ||
                restart() || undo() || redo() || exit());
    }

    private static boolean help() {
        if (playerInput.equals("help")) {
            System.out.println("游戏指示：\n" +
                    "输入restart重新开始游戏\n" +
                    "输入exit退出游戏\n" +
                    "输入help获得游戏帮助：\n" +
                    "输入help1获得游戏规则\n" +
                    "输入help2获得动物和移动指令对应的数字和字母\n" +
                    "输入undo悔棋\n" +
                    "输入redo重做（取消悔棋）\n");
            return true;
        } else return false;
    }

    private static boolean restart() {
        if (playerInput.equals("restart")) {
            currentStep = 1;
            clearRecordHistory();
            countStep = true;
            player1animals = copyArray(historyPlayer1[0]);
            player2animals = copyArray(historyPlayer2[0]);
            return true;
        } else return false;
    }

    private static boolean undo() {
        //悔棋
        if (playerInput.equals("undo")) {
            try {
                player1animals = copyArray(historyPlayer1[currentStep - 2]);
                player2animals = copyArray(historyPlayer2[currentStep - 2]);
                currentStep = currentStep - 1;
                countStep=!countStep;
            } catch (Exception e) {
                System.out.println("已经回到第1回合");
            }
            return true;
        } else return false;
    }

    private static int[][] copyArray(int[][] array) {
        //深度复制数组
        int[][] newArray = new int[7][9];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    private static void setHistory() {
        //保存历史棋局
        historyPlayer1[currentStep] = copyArray(player1animals);
        historyPlayer2[currentStep] = copyArray(player2animals);
        System.out.println("现在是第" + (currentStep / 2 + 1) + "回合");
        biggestStep = ++currentStep;
    }

    private static void setHistory0() {
        //保存0步时的棋局
        currentStep = 0;
        historyPlayer1[0] = copyArray(player1animals);
        historyPlayer2[0] = copyArray(player2animals);
        System.out.println("现在是第" + 1 + "回合");
        biggestStep = ++currentStep;
    }

    private static boolean redo() {
        //撤销悔棋
        if (playerInput.equals("redo")) {
            if (currentStep + 1 <= biggestStep) {
                player1animals = copyArray(historyPlayer1[currentStep]);
                player2animals = copyArray(historyPlayer2[currentStep]);
                currentStep = currentStep + 1;
                countStep=!countStep;
            } else {
                System.out.println("已经回到最新回合");
            }
            return true;
        } else return false;
    }

    private static boolean exit() {
        //退出游戏
        if (playerInput.equals("exit")) {
            System.out.println("玩家退出了游戏");
            System.exit(0);
            return true;
        } else return false;
    }

    private static void clearRecordHistory() {
        //清空某些步的历史棋局
        for (int k = 1 + currentStep; k < biggestStep; k++) {
            historyPlayer1[k] = copyArray(historyPlayer1[0]);
            historyPlayer2[k] = copyArray(historyPlayer2[0]);
        }
        biggestStep = currentStep;
    }

    private static void printHistory() {
        //打印当前步数和最大步数
        System.out.println("currentStep=" + (currentStep - 1));
        System.out.println("recordedStep=" + (biggestStep - 1));
    }
}

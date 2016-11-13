import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by luke1998 on 11/3/16.
 * 该类包含用来读入的方法
 */
class Origins {
    static int[][] animalsMap = new int[7][9];
    static int[][] landscapeInt = new int[7][9];
    static int[][] player1animals = new int[7][9];
    static int[][] player2animals = new int[7][9];

    static void readOriginal() {
        //读取读取文件操作
        Scanner scanner1 = null;
        Scanner scanner2 = null;
        try {
            scanner1 = new Scanner(new File("tile.txt"));
            scanner2 = new Scanner(new File("animal.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 7; i++) {
            assert scanner1 != null;
            String line_1 = scanner1.nextLine();//读出地图
            assert scanner2 != null;
            String line_2 = scanner2.nextLine();//读出地图
            char[] toChar_1 = new char[10];
            char[] toChar_2 = new char[10];
            for (int j = 0; j < 9; j++) {
                toChar_1[j] = line_1.charAt(j);
                toChar_2[j] = line_2.charAt(j);
                landscapeInt[i][j] = Integer.parseInt(String.valueOf(toChar_1[j]));
                animalsMap[i][j] = Integer.parseInt(String.valueOf(toChar_2[j]));
            }
        }
    }

    static void playerHasAnimals() {
        //将原始动物数组分为玩家1，2分别的数组
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                player1animals[i][j] = animalsMap[i][j];
                player2animals[i][j] = 0;
            }
            for (int j = 5; j < 9; j++) {
                player1animals[i][j] = 0;
                player2animals[i][j] = animalsMap[i][j];
            }
        }
    }
}

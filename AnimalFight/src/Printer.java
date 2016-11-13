/**
 * Created by luke1998 on 11/3/16.
 * 该类包含打印棋局的方法
 */
class Printer {
    private static int[][] animalsMap = AnimalFight.animalsMap;
    private static int[][] landscapeInt = Origins.landscapeInt;

    static void printMapInGame() {
        //打印棋局
        String[][] landscape = new String[7][9];
        String[][] animals = new String[7][9];
        //把int[][]变为animals[][][保存，输出为String[][]
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                landscape[i][j] = Integer.toString(landscapeInt[i][j]);
                String str[] = {" 　 ", " 河 ", "1陷 ", "1穴 ", " 陷2", " 穴2"};//key
                switch (landscape[i][j]) {
                    case "0":
                        landscape[i][j] = str[0];
                        break;
                    case "1":
                        landscape[i][j] = str[1];
                        break;
                    case "2":
                        landscape[i][j] = str[2];
                        break;
                    case "3":
                        landscape[i][j] = str[3];
                        break;
                    case "4":
                        landscape[i][j] = str[4];
                        break;
                    case "5":
                        landscape[i][j] = str[5];
                        break;
                }
                animals[i][j] = Integer.toString(animalsMap[i][j]);
                String str2[] = {" 　 ", "1鼠 ", "2猫 ", "3狼 ", "4狗 ", "5豹 ", "6虎 ", "7狮 ", "8象 "
                        , " 鼠1", " 猫2", " 狼3", " 狗4", " 豹5", " 虎6", " 狮7", " 象8"};//key
                switch (animals[i][j]) {
                    case "0":
                        animals[i][j] = str2[0];
                        break;
                    case "1":
                        animals[i][j] = str2[1];
                        break;
                    case "2":
                        animals[i][j] = str2[2];
                        break;
                    case "3":
                        animals[i][j] = str2[3];
                        break;
                    case "4":
                        animals[i][j] = str2[4];
                        break;
                    case "5":
                        animals[i][j] = str2[5];
                        break;
                    case "6":
                        animals[i][j] = str2[6];
                        break;
                    case "7":
                        animals[i][j] = str2[7];
                        break;
                    case "8":
                        animals[i][j] = str2[8];
                        break;
                    case "10":
                        animals[i][j] = str2[0];
                        break;
                    case "11":
                        animals[i][j] = str2[9];
                        break;
                    case "12":
                        animals[i][j] = str2[10];
                        break;
                    case "13":
                        animals[i][j] = str2[11];
                        break;
                    case "14":
                        animals[i][j] = str2[12];
                        break;
                    case "15":
                        animals[i][j] = str2[13];
                        break;
                    case "16":
                        animals[i][j] = str2[14];
                        break;
                    case "17":
                        animals[i][j] = str2[15];
                        break;
                    case "18":
                        animals[i][j] = str2[16];
                        break;
                }
            }
        }//至此地图已经转为文字格式
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (animalsMap[i][j] != 0) {
                    if (j < 8) {
                        System.out.print(animals[i][j]);
                    } else {
                        System.out.print(animals[i][j]);
                        System.out.println();
                    }
                } else {
                    if (j < 8) {
                        System.out.print(landscape[i][j]);
                    } else {
                        System.out.print(landscape[i][j]);
                        System.out.println();
                    }
                }
            }
        }
        //打印玩家操作提示
        System.out.println("———————————————————————————————————————————————————————————————\n"
                + "请玩家输入操作：\n"
                + "操作输入格式为：动物数字+移动方向（具体参照help2）\n"
                + "例如：1a，表示鼠向左走一步\n"
                + "输入help获取帮助\n");
    }

}

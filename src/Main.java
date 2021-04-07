import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static ArrayList<String> pathList = new ArrayList<>();
    private static ArrayList<String> banListA, banListB;
    private static int allLineCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean flag1 = true, flag2 = true;
        System.out.println("请输入要遍历的目录:");
        String filePath = scanner.nextLine();
        loop:
        while (true) {
            pathList = new ArrayList<>();
            if (flag1) {
                System.out.println("请输入要排除的目录名(多个数据用英文逗号隔开):");
                String[] banList = scanner.nextLine().split(",");
                banListA = new ArrayList<>(Arrays.asList(banList));
                System.out.println("排除目录为:" + banListA);
                flag1 = false;
            }
            if (flag2) {
                System.out.println("请输入要统计行数的文件后缀名(不需要带.)(多个数据用英文逗号隔开):");
                String[] banListTB = scanner.nextLine().split(",");
                banListB = new ArrayList<>(Arrays.asList(banListTB));
                System.out.println("统计文件后缀名为:" + banListB);
                flag2 = false;
            }
            pathR(filePath);
            System.out.println("\n文件列表:");
            for (String str : pathList) {
                System.out.println(str);
            }

            System.out.println("是否开始行数统计？输入Y继续，输入N1重新修改排除目录名，输入N2重新修改统计行数文件后缀名(Y/N)");
            String command = scanner.nextLine();
            switch (command.toUpperCase()) {
                case "Y":
                    countLine();
                    break loop;

                case "N1":
                    flag1 = true;
                    continue loop;

                case "N2":
                    flag2 = true;
                    continue loop;
                default:
                    System.out.println("输入命令不合规范，请重新输入");
            }
        }
    }

    private static void pathR(String path) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        if (fileList == null) {
            System.out.println("文件夹不存在或为空");
            return;
        }
        for (File fileT : fileList) {
            if (fileT.isDirectory()) {
                String pathA = fileT.getAbsolutePath();
                if (banListA.contains(pathA.substring(pathA.lastIndexOf('\\') + 1))) {
                    continue;
                }
                pathR(fileT.getAbsolutePath());
            } else {
                String pathB = fileT.getAbsolutePath();
                if (banListB.contains(pathB.substring(pathB.lastIndexOf('.') + 1))) {
                    pathList.add(fileT.getAbsolutePath());
                }
            }
        }
    }

    private static void countLine() {
        System.out.println("行数统计:");
        for (String path : pathList) {
            File file = new File(path);
            try {
                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
                lineNumberReader.setLineNumber(1);//Default line number is 0. then it will less than real line number if use default
                lineNumberReader.skip(file.length());
                int lineCount = lineNumberReader.getLineNumber();
                allLineCount += lineCount;
                System.out.println(lineCount + " 行" + "\t" + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("总计:" + allLineCount + " 行");
    }
}

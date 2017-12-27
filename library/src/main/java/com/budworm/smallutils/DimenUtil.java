package com.budworm.smallutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 根据values/dimens.xml, 自动计算比例并生成不同分辨率的dimens.xml
 * 注意用dp和sp，不要用dip，否则生成可能会出错；xml值不要有空格
 *
 *  ldpi  QVGA (240×320)
 *
 *  mdpi  HVGA (320×480)
 *
 *  hdpi  WVGA (480×800),FWVGA (480×854)
 *
 *  xhdpi  720P（1280*720）
 *
 *  xxhdpi 1080p（1920*1080 ）
 *
 *  xxxhdpi 4K（3840×2160）
 */
public class DimenUtil {

    private static void gen() {
        // 1920 * 1080 xxhdpi
        File file = new File("./app/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw600dp = new StringBuilder(); // 1.5

        try {
            System.out.println("生成不同分辨率");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    int num = Integer.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));
                    // 计算缩放值
                    sw600dp.append(start).append(Math.round(num * 1.333)).append(end).append("\n");
                } else {
                    sw600dp.append(tempString).append("\n");
                }
            }
            reader.close();

            String sw600dpFile = "./app/src/main/res/values-sw600dp/dimens.xml";
            writeFile(sw600dpFile, sw600dp.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void main(String[] args) {
        gen();
    }


}


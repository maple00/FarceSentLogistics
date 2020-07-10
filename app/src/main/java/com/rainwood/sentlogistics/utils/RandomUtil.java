package com.rainwood.sentlogistics.utils;

import java.util.Random;

/**
 * @Author: a797s
 * @Date: 2020/5/26 19:29
 * @Desc: 随机id
 */
public final class RandomUtil {

    /**
     * 创建字母+数字的组合id
     * 生成随机数当作getItemID
     * n ： 需要的长度
     */
    public static String getItemID(int n) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
            if ("char".equalsIgnoreCase(str)) { // 产生字母
                int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
                // System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
                val.append((char) (nextInt + random.nextInt(26)));
            } else { // 产生数字
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    /**
     * 创建纯数字的id
     */
    public static String getNumberId(int n) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            val.append(random.nextInt(10));
        }
        return val.toString();
    }
}

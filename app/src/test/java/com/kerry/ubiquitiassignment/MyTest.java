package com.kerry.ubiquitiassignment;

import android.util.Log;

import org.junit.Test;

public class MyTest {


    @Test
    public void test() {
        System.out.println("- - - - - - - - - - - - - - - - -");
//        String s = Integer.toBinaryString(1041);
//        String[] arr = s.split("");
//        for (String str: arr) {
//
//            System.out.println(str);
//        }
//        System.out.println(s);

        int answer = solution(32);
        System.out.println("- - - - - - - - - - - - - - - - -");
        System.out.println(answer);
        System.out.println("- - - - - - - - - - - - - - - - -");
    }

    public int solution(int N) {
        String binarytStr = Integer.toBinaryString(N);
        String[] arr = binarytStr.split("");

        int answer = 0;
        int count = 0;

        for (String str: arr) {
            System.out.println("str: " + str);
            if (str.equals("0")) {
                count += 1;
            } else {
                count = 0;
            }
            System.out.println("count: " +count);
            answer = Math.max(answer, count);
        }


        return answer;
    }

}

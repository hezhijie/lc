package com.m.cross;

/**
 * @author zhijie.he created on 2020/7/31
 * @version 1.0
 */
public class BigSum {

    public static void main(String args[]) {
        int a = Integer.MAX_VALUE;
        int b = Integer.MAX_VALUE/5;
        System.out.println(a);

        String s1 = String.valueOf(a);
        String s2 = String.valueOf(b);

        String ret = "";
        int anso = 0;
        int s1End = s1.length()-1;
        int s2End = s2.length()-1;
        int i = 0;
        for (; i <= s1End && i <= s2End; i++) {

            int c1 = Integer.parseInt(String.valueOf(s1.charAt(s1End-i)));
            int c2 = Integer.parseInt(String.valueOf(s2.charAt(s2End-i)));
            int mod = (c1+c2+anso)%10;
            anso = (c1+c1)/10;
            ret = mod + ret;
        }

        if (i<=s1End) {

        } else {

        }


        System.out.println(ret);
    }
}

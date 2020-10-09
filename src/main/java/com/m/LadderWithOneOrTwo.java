package com.m;

public class LadderWithOneOrTwo {

    static int count(int n) {
        int[] f = new int[n+1];
        f[0] = 1; f[1] = 1;
        for (int i = 2; i <= n; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        return f[n];
    }

    public static void main(String args[]) {
        System.out.println(count(3));
    }
}
/**
 * 1,1,1
 * 1,2
 * 2,1
 */

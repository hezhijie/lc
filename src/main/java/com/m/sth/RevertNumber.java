package com.m.sth;

public class RevertNumber {

    public static void revert(int num) {
        if (num > -10 && num < 10) {
            System.out.println(num);
            return;
        }
        int ret = 0;
        while (num != 0) {
            ret = ret*10+num%10;
            num = num/10;
        }
        System.out.println(ret);
    }

    public static void main(String[] args) {
        revert(-12);
    }

}

package com.m.array;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhijie.he created on 2020/7/27
 * @version 1.0 排列组合， abc 输出所有情况
 */
public class Comb {

    public void comb(char[] array, int start, Set<String> set) {
        if (start == array.length - 1) {
            set.add(Arrays.toString(array));
            return;
        }

        // a b c
        for (int i = start; i < array.length; i++) {
            swap(array,start,i);
            comb(array,start+1,set);
            swap(array,start,i);
        }

    }

    public void swap(char[] array, int i, int j) {
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String args[]) {
        Comb comb = new Comb();

        String a = "abc";
        Set<String> set = new HashSet<String>();
        comb.comb(a.toCharArray(),0,set);

        System.out.println(set);
    }
}

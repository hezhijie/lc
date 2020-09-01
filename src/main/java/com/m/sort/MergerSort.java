package com.m.sort;

/**
 * @author zhijie.he created on 2020/5/22
 * @version 1.0
 */
public class MergerSort {

    public void sort(int[] src, int low, int high, int[] temp) {
        if (low < high) {
            int mid = (high + low) / 2;
            sort(src, low, mid, temp);
            sort(src, mid + 1, high, temp);
            merge(src, low, mid, high, temp);
        }
    }

    public void merge(int[] src, int low, int mid, int high, int[] temp) {
        int i = low;
        int j = mid + 1;
        int t = 0;
        while (i <= mid && j <= high) {
            if (src[i] <= src[j]) {
                temp[t++] = src[i++];
            } else {
                temp[t++] = src[j++];
            }
        }

        while (i <= mid) {
            temp[t++] = src[i++];
        }
        while (j <= high) {
            temp[t++] = src[j++];
        }
        t = 0;
        while (low <= high) {
            src[low++] = temp[t++];
        }
    }

    public static void main(String args[]) {
        MergerSort mergerSort = new MergerSort();

        int src[] = { 3, 1, 2, 4 };
        int temp[] = new int[4];
        mergerSort.sort(src, 0, src.length - 1, temp);

        for (int i : src) {
            System.out.print(i);
        }
    }
}

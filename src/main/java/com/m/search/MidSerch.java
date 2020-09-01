package com.m.search;

/**
 * @author zhijie.he created on 2020/7/27
 * @version 1.0
 */
public class MidSerch {
    public static void main(String args[]) {
        int arr[] = {1,2,3,4,6};

        System.out.println(search(arr,0,arr.length-1,5));
    }
    // 二分查找
    public static int search(int[] array, int start, int end, int t) {
        if (array == null) {
            return -1;
        }
        //0,1,2,3,4,5
        while (start < end) {
            int mid = (end - start) / 2 + start;
            if (array[mid] == t) {
                return mid;
            }
            if (array[mid]>t) {
                end = mid;
            } else {
                start = mid+1;
            }
        }
        return -1;

    }
}

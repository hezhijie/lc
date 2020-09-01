package com.m.sort;

/**
 * @author zhijie.he created on 2020/5/22
 * @version 1.0
 */
public class FastSort {


    public void swap(int arr[],int i,int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public void sort(int[] arr, int left, int right) {

        int i = left;
        int j = right;
        if (left >= right) {
            return;
        }
        int target = arr[i];
        // 交换法
        while(i<j) {
            while(arr[j]>target && i<j) {
                j--;
            }
            while(arr[i]<=target && i<j) {
                i++;
            }
            if(i<j) {
                swap(arr,i,j); // 后面小于目标的值 和前面大于目标的值交换
            }
        }
        System.out.println("===相遇"+arr[i]);
        swap(arr,left,i); // 两个指针相遇, 因为是j先走的， 所以相遇的地方一定小于等于目标值

        sort(arr, left, i - 1);
        sort(arr, i + 1, right);




        // 填坑法
//        while (i < j) {
//            while (i < j && arr[j] > target) {
//                j--;
//            }
//            if (i < j) {
//                arr[i] = arr[j];
//                i++;
//            }
//            while (i < j && arr[i] <= target) {
//                i++;
//            }
//            if (i < j) {
//                arr[j] = arr[i];
//                j--;
//            }
//        }
//        arr[i] = target;
//        sort(arr, left, i - 1);
//        sort(arr, i + 1, right);

    }

    public static void main(String args[]) {
        int[] arr = { 2,7,1,6,8,4,3,5 };
        FastSort fastSort = new FastSort();
        fastSort.sort(arr, 0, arr.length - 1);

        for (int a : arr) {
            System.out.print(a);
        }

    }
}

package com.hk.sort;

import java.util.Arrays;

public class HeapSort {
    public static void main(String[] args) {
        int[] arr = new int[]{20, 50, 21, 40, 70, 10, 80, 30, 60};

        //最后一位下标
        int lastIndex = arr.length - 1;

        //arr.length / 2 - 1;堆特性只有这个数量的节点有子节点
        for (int i = lastIndex / 2 - 1; i >= 0; i--) {
            constructHeap(arr, i, lastIndex);
        }
        System.out.println(Arrays.toString(arr));
        //[80, 70, 21, 60, 50, 10, 20, 30, 40]

        while (lastIndex > 0) {
            //把堆顶的元素换到末尾
            swap(arr, 0, lastIndex);
            //末尾不参与置换
            lastIndex--;
            //对堆结构进行调整，最后一位不参与调整
            constructHeap(arr, 0, lastIndex);
        }

        System.out.println(Arrays.toString(arr));
        //[10, 20, 21, 30, 40, 50, 60, 70, 80]
    }

    /**
     * 对数组的某个父节点进行操作，比较其与子节点的大小，将最大的置换到父节点位置
     *
     * @param arr       给定数组
     * @param i         处理哪一个父节点
     * @param lastIndex 参与交换的数组尾标。可以指定小于数组长度，代表后面的几个数不参与交换
     */
    private static void constructHeap(int[] arr, int i, int lastIndex) {
        if (lastIndex <= 0) {
            return;
        }
        //堆特性，左节点右节点index
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (right > lastIndex) {
            //只比较left和父节点哪个大放到父节点
            if (arr[left] > arr[i]) {
                swap(arr, left, i);
                //与父节点交换的节点是否有子节点，如果有的话需要递归
                if (left <= lastIndex / 2 - 1) {
                    constructHeap(arr, left, lastIndex);
                }
            }
        } else {
            //比较三个最大的和父节点交换
            int sonMaxIndex = arr[left] >= arr[right] ? left : right;
            if (arr[sonMaxIndex] > arr[i]) {
                swap(arr, sonMaxIndex, i);
                //与父节点交换的节点是否有子节点，如果有的话需要递归
                if (sonMaxIndex <= lastIndex / 2 - 1) {
                    constructHeap(arr, sonMaxIndex, lastIndex);
                }
            }
        }
    }

    private static void swap(int[] arr, int son, int father) {
        int temp = arr[father];
        arr[father] = arr[son];
        arr[son] = temp;
    }
}

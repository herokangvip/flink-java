package com.hk.sort;

import com.hk.topn.Order;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * 堆排算法
 * 实现订单排序或topN(topN性能比Collections.sort方法性能快一个数量级)
 * 一百万数据在80ms左右
 */
public class OrderHeapSortUtils {

    public static Order[] topN(Order[] arr, int topSize, Comparator<Order> comparator) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        if (topSize >= arr.length) {
            Arrays.sort(arr, comparator);
            return arr;
        }
        int lastIndex = arr.length - 1;
        for (int i = lastIndex / 2 - 1; i >= 0; i--) {
            constructHeap(arr, i, lastIndex, comparator);
        }
        //依次把堆顶元素换到末尾
        while (lastIndex >= 1) {
            swap(arr, lastIndex, 0);
            lastIndex--;
            constructHeap(arr, 0, lastIndex, comparator);
            if (lastIndex == arr.length - topSize - 1) {
                break;
            }
        }
        //return arr;
        Order[] res = new Order[topSize];
        //System.arraycopy(arr, 1, res, 0, topSize);
        int idx = arr.length - 1;
        for (int i = 0; i < topSize; i++) {
            res[i] = arr[idx - i];
        }
        return res;
    }

    public static Order[] sort(Order[] arr, Comparator<Order> comparator) {
        if (arr == null || arr.length == 0) {
            return null;
        }

        int lastIndex = arr.length - 1;
        for (int i = lastIndex / 2 - 1; i >= 0; i--) {
            constructHeap(arr, i, lastIndex, comparator);
        }

        //依次把堆顶元素换到末尾
        while (lastIndex >= 1) {
            swap(arr, lastIndex, 0);
            lastIndex--;
            constructHeap(arr, 0, lastIndex, comparator);
        }
        return arr;
    }

    private static void constructHeap(Order[] arr, int i, int lastIndex, Comparator<Order> comparator) {
        if (lastIndex <= 0) {
            return;
        }
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (right > lastIndex) {
            //说明只有左子节点
            //比较左子节点和父节点的大小
            if (comparator.compare(arr[left], arr[i]) > 0) {
                swap(arr, left, i);
                //和top节点交换的子节点如果有子节点，递归
                if (left <= lastIndex / 2 - 1) {
                    constructHeap(arr, left, lastIndex, comparator);
                }
            }
        } else {
            //左右子节点都有
            int sonMax = comparator.compare(arr[left], arr[right]) > 0 ? left : right;
            if (comparator.compare(arr[sonMax], arr[i]) > 0) {
                swap(arr, sonMax, i);
                //和top节点交换的子节点如果有子节点，递归
                if (sonMax <= lastIndex / 2 - 1) {
                    constructHeap(arr, sonMax, lastIndex, comparator);
                }
            }
        }
    }

    private static void swap(Order[] arr, int left, int top) {
        Order temp = arr[top];
        arr[top] = arr[left];
        arr[left] = temp;
    }
}

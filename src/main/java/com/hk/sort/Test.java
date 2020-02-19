package com.hk.sort;

import com.hk.topn.Order;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        test1();
        //test2();
    }

    private static void test1() {
        //20, 50, 21, 40, 70, 10, 80, 30, 60
        ArrayList<Order> list = new ArrayList<>();
        list.add(new Order(1L, 1000L, 20L));
        list.add(new Order(2L, 1000L, 50L));
        list.add(new Order(3L, 1000L, 21L));
        list.add(new Order(4L, 1000L, 40L));
        list.add(new Order(5L, 1000L, 70L));
        list.add(new Order(6L, 1000L, 10L));
        list.add(new Order(7L, 1000L, 80L));
        list.add(new Order(8L, 1000L, 30L));
        list.add(new Order(9L, 1000L, 60L));

        Order[] arr = new Order[list.size()];
        list.toArray(arr);
        Order[] res = OrderHeapSortUtils.topN(arr, 300, Comparator.comparing(Order::getNum));
        System.out.println(Arrays.toString(res));
    }

    private static void test2() {
        //20, 50, 21, 40, 70, 10, 80, 30, 60
        ArrayList<Order> list = new ArrayList<>();
        ArrayList<Order> list2 = new ArrayList<>();

        list.add(new Order(1L, 1L, 999999L));
        list.add(new Order(1L, 1L, 888888L));
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            Order order = new Order(1L, 1L, random.nextInt(1000) + 1L);
            list.add(order);
            list2.add(order);
        }

        long start1 = System.currentTimeMillis();
        Collections.sort(list, Comparator.comparing(Order::getNum));
        long end1 = System.currentTimeMillis();
        System.out.println("====java自带的sort  耗时：" + (end1 - start1) + "ms");


        Order[] arr = new Order[list2.size()];
        list2.toArray(arr);
        long start2 = System.currentTimeMillis();

        Order[] res = OrderHeapSortUtils.topN(arr, 50, Comparator.comparing(Order::getNum));
        long end2 = System.currentTimeMillis();
        System.out.println("====堆排序topN算法  耗时：" + (end2 - start2) + "ms");
    }
}

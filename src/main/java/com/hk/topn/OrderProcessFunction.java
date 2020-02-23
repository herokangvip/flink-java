package com.hk.topn;

import com.hk.sort.OrderHeapSortUtils;
import org.apache.flink.api.common.state.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.*;

public class OrderProcessFunction extends ProcessAllWindowFunction<Order, List<Order>, TimeWindow> {
    MapStateDescriptor<Long, Order> descriptorOfAllMap = new MapStateDescriptor<Long, Order>("sku_order_all_map", Long.class, Order.class);
    MapState<Long, Order> allMap = null;
    private int topSize;

    public OrderProcessFunction(int topSize) {
        super();
        this.topSize = topSize;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        allMap = getRuntimeContext().getMapState(descriptorOfAllMap);
    }

    @Override
    public void process(Context context, Iterable<Order> iterable, Collector<List<Order>> collector) throws Exception {
        Iterator<Order> it = iterable.iterator();
        int aa = 0;
        while (it.hasNext()) {
            aa++;
            Order order = it.next();
            Long skuId = order.getSkuId();
            if (allMap.contains(skuId)) {
                Order allMapOrder = allMap.get(skuId);
                allMapOrder.setNum(allMapOrder.getNum() + order.getNum());
                allMap.put(skuId, allMapOrder);
            } else {
                allMap.put(skuId, order);
            }
        }

        System.out.println("==每次处理:"+aa);
        ArrayList<Order> list = new ArrayList<>();
        Iterator<Order> iterator = allMap.values().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Order[] arr = new Order[list.size()];
        list.toArray(arr);
        Order[] orders = OrderHeapSortUtils.topN(arr, topSize, Comparator.comparing(Order::getNum).reversed());
        List<Order> res = new ArrayList<>();
        //保证排序顺序
        for (int i = 0; i < orders.length; i++) {
            res.add(orders[i]);
        }
        collector.collect(res);
    }

    @Override
    public void clear(Context context) throws Exception {
        allMap.clear();
    }


}

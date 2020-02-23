package com.hk.topn;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyOrderSourceFunction implements SourceFunction<Order> {
    private boolean running = true;

    private Long time = System.currentTimeMillis();
    private Long timeEnd = System.currentTimeMillis() + 1000 * 60 * 60 * 25;
    private int size = 0;

    @Override
    public void run(SourceContext<Order> sourceContext) throws Exception {
        ArrayList<Long> skuIdList = new ArrayList<>();
        //skuIdList.add(3200L);
        skuIdList.add(3201L);
        skuIdList.add(3202L);
        skuIdList.add(3203L);
        //skuIdList.add(3204L);
        /*skuIdList.add(3205L);
        skuIdList.add(3206L);
        skuIdList.add(3207L);
        skuIdList.add(3208L);
        skuIdList.add(3209L);*/
        Random random = new Random();
        while (running && time < timeEnd && size < 999999999) {
            int i = random.nextInt(skuIdList.size());
            Long timestamp = time + 1000 * 60 * 10;//10min
            Order order = new Order(skuIdList.get(i), timestamp, 1L);
            sourceContext.collect(order);
            TimeUnit.MILLISECONDS.sleep(200);
            time = timestamp;
            size++;
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}

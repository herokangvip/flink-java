package com.hk.topn;

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class OrderAssinerWaterMarks extends BoundedOutOfOrdernessTimestampExtractor<Order> {
    public OrderAssinerWaterMarks(Time maxOutOfOrderness) {
        super(maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(Order order) {
        return order.getTimestamp();
    }
}

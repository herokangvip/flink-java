package com.hk.topn;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;

/**
 * 每天topN实时统计案例，qps高的话可以修改trigger改为n条记录触发一次窗口操作
 * 排序算法为堆排序优化后的，性能比Collections.sort方法性能快一个数量级
 *   一百万数据在80ms左右
 * clean package -Pbuild-jar -Dmaven.test.skip=true
 */
public class DaysTopn {

    private static int topSize = 3;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        initProperties(env);
        DataStreamSource<Order> source = env.addSource(new MyOrderSourceFunction());
        SingleOutputStreamOperator<List<Order>> dataStream = source
                .assignTimestampsAndWatermarks(new OrderAssinerWaterMarks(Time.seconds(5)))
                .windowAll(TumblingEventTimeWindows.of(Time.days(1), Time.hours(16)))
                .allowedLateness(Time.minutes(1))
                .trigger(new OrderTrigger(30))
                .process(new OrderProcessFunction(topSize));

        dataStream.print();
        // TODO: 2020/2/18 sink
        env.execute("实时订单topN");
    }




    private static void initProperties(StreamExecutionEnvironment env) {
        //全局并行度1，测试用
        env.setParallelism(1);
        //一般生产上stateBackend在集群端统一配置new FsStateBackend("hdfs://home/flink/checkpoints")
        //env.setStateBackend(new MemoryStateBackend());
        //checkpoint间隔不要设置太短,状态后端压力太大，可能导致任务重启等问题。推荐5分钟左右，
        env.enableCheckpointing(1000 * 60 * 10);
        //默认就是exactlyOnce
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //设置时间语义
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //失败后重启三次每次间隔20s
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, org.apache.flink.api.common.time.Time.seconds(20)));
        //设置checkPoint最大并行度
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //即使手动取消任务也不要删除保存点数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

    }
}

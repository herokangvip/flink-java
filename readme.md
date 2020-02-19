
 * 每天topN实时统计案例，qps高的话可以修改trigger改为n条记录触发一次窗口操作
 * 排序算法为堆排序优化后的，性能比Collections.sort方法性能快一个数量级
 *   本地测试一百万数据耗时在80ms左右，结合trigger和sort优化可以处理日亿级订单


备忘录：

* 为每一步设置uid，用于savePoint恢复等

* Source：Source的并发数不能大于Source的Shard数。
       Source数量和上游kafka的Partition数量相关。
       例如，Partition个数是8，Source的并发数可以配置为8、4、2，最好不要超过8
* 中间算子：根据预QPS计算
       QPS低的，中间处理节点数和Source并行度一样。
       QPS高的，中间处理节点数配置为比Source并行度大，例如，32、64、128
* Sink：
       并发度和下游的Partition数相关，一般是下游Partition个数的2~3倍。
       如果配置过大会导致输入超时或失败。例如，下游Sink节点个数是4，建议Sink的并发度最大配置为12
       
source.shuffle();
source.rebalance();
    shuffle随机，rebalance轮训，对于数据倾斜的情况很适合
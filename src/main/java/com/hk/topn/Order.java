package com.hk.topn;

import java.io.Serializable;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long skuId;
    private Long timestamp;
    private Long num;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Order() {
    }

    public Order(Long skuId, Long timestamp, Long num) {
        this.skuId = skuId;
        this.timestamp = timestamp;
        this.num = num;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Order{" +
                "skuId=" + skuId +
//                ", timestamp=" + timestamp +
                ", num=" + num +
                '}';
    }
}

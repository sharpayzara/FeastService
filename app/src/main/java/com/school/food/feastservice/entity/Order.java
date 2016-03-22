package com.school.food.feastservice.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Order extends BmobObject {
    private static final long serialVersionUID = 1L;
    private String businessName;
    private String orderNum;
    private String orderData;
    private String totalMoney;
    private String factTotalMoney;
    private String objectId;
    private Long discountData;
    private String phoneNum;

    public Order(String objectId, String businessName, String orderNum, String totalMoney, String orderData, String factTotalMoney,Long discountData,String phoneNum) {
        super();
        this.orderData = orderData;
        this.businessName = businessName;
        this.orderNum = orderNum;
        this.objectId = objectId;
        this.totalMoney = totalMoney;
        this.factTotalMoney = factTotalMoney;
        this.discountData = discountData;
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getFactTotalMoney() {
        return factTotalMoney;
    }

    public void setFactTotalMoney(String factTotalMoney) {
        this.factTotalMoney = factTotalMoney;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getDiscountData() {
        return discountData;
    }

    public void setDiscountData(Long discountData) {
        this.discountData = discountData;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
}

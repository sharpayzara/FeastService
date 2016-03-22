package com.school.food.feastservice.entity;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/16.
 */
public class UserOrder extends BmobObject implements Serializable{
    private Double totalMoney;
    private String businessName;
    private String phoneNum;
    private Boolean isUnReg;
    private List<PreOrder> preOrders;
    private String orderId;
    private String factTotalMoney;
    private Boolean isUse;
    private Long  discountData;
    public String getOrderId() {
        return orderId;
    }

    public Long getDiscountData() {
        return discountData;
    }

    public void setDiscountData(Long discountData) {
        this.discountData = discountData;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean getUse() {
        return isUse;
    }

    public Boolean getUnReg() {
        return isUnReg;
    }

    public void setUnReg(Boolean unReg) {
        isUnReg = unReg;
    }

    public void setUse(Boolean use) {
        isUse = use;
    }

    public String getFactTotalMoney() {
        return factTotalMoney;
    }

    public void setFactTotalMoney(String factTotalMoney) {
        this.factTotalMoney = factTotalMoney;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<PreOrder> getPreOrders() {
        return preOrders;
    }

    public void setPreOrders(List<PreOrder> preOrders) {
        this.preOrders = preOrders;
    }
}

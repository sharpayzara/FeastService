package com.school.food.feastservice.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.school.food.feastservice.R;
import com.school.food.feastservice.entity.Lottery;
import com.school.food.feastservice.entity.Order;
import com.school.food.feastservice.entity.User;
import com.school.food.feastservice.entity.UserOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.UpdateListener;


public class OrderQueryListAdapter extends RecyclerView.Adapter<OrderQueryListAdapter.OrderHolder>{
    Context mContext;
    List<Order> list;
    private boolean isUsed = true;
    ReflushListener listener;
    boolean isCanTD = true;
    public static Long sysCurrentTime;
    public static String userPhone;

    public OrderQueryListAdapter(Context context, List<Order> list, boolean isUsed, ReflushListener listener,Long sysCurrentTime) {
        mContext = context;
        this.list = list;
        this.isUsed = isUsed;
        this.listener = listener;
        this.sysCurrentTime = sysCurrentTime;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderHolder holder = new OrderHolder(LayoutInflater.from(mContext).inflate(R.layout.order_query_item, parent,
                false));
        return holder;
    }

    private void checkIsValid(final int position){
        BmobQuery<UserOrder> b1 = new BmobQuery<UserOrder>();
        b1.addWhereEqualTo("objectId",list.get(position).getObjectId());
        BmobQuery<UserOrder> b2 = new BmobQuery<UserOrder>();
        b2.addWhereEqualTo("isUnReg", false);
        BmobQuery<UserOrder> b3 = new BmobQuery<UserOrder>();
        b3.addWhereEqualTo("isUse", false);
        List<BmobQuery<UserOrder>> queries = new ArrayList<BmobQuery<UserOrder>>();
        queries.add(b1);
        queries.add(b2);
        queries.add(b3);
        BmobQuery<UserOrder> query = new BmobQuery<UserOrder>();
        query.and(queries);
        query.findObjects(mContext, new FindListener<UserOrder>() {
            @Override
            public void onSuccess(List<UserOrder> lists) {
                if(lists.size() == 0){
                    Toast.makeText(mContext,"订单异常，请稍后再试",Toast.LENGTH_SHORT).show();
                    isCanTD = true;
                    return ;
                }
                UserOrder userOrder = new UserOrder();
                userOrder.setUse(true);
                userOrder.update(mContext, lists.get(0).getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        checkLotteryNum();
                        Toast.makeText(mContext, "操作成功，请出票...", Toast.LENGTH_SHORT).show();
                        listener.reflush();
                        isCanTD = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        isCanTD = true;
                        Toast.makeText(mContext, "操作失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mContext,"订单异常，请稍后再试",Toast.LENGTH_SHORT).show();
                isCanTD = true;
                return ;
            }
        });

    }

    @Override
    public void onBindViewHolder(final OrderHolder holder, final int position) {
        holder.totalMoney.setText(list.get(position).getTotalMoney()+"元");
        holder.factTotalMoney.setText(list.get(position).getFactTotalMoney()+"元");
        holder.orderNum.setText(list.get(position).getOrderNum());
        holder.orderTime.setText(list.get(position).getOrderData());
        holder.businessName.setText(list.get(position).getBusinessName());
        holder.unreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("确认使用吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("使用", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(list.get(position).getDiscountData() != null && list.get(position).getDiscountData() > sysCurrentTime && list.get(position).getDiscountData() != 0){
                                    Toast.makeText(mContext,"订单未到期,暂不能使用",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(!isCanTD){
                                    return;
                                }
                                isCanTD = false;
                                userPhone = list.get(position).getPhoneNum();
                                checkIsValid(position);
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        TextView totalMoney,factTotalMoney;
        TextView orderNum;
        TextView businessName;
        TextView orderTime;
        Button unreg;
        public OrderHolder(View view){
            super(view);
            totalMoney = (TextView) view.findViewById(R.id.total_money);
            factTotalMoney = (TextView) view.findViewById(R.id.fact_total_money);
            orderNum = (TextView) view.findViewById(R.id.order_num);
            businessName = (TextView) view.findViewById(R.id.business_name);
            orderTime = (TextView) view.findViewById(R.id.order_time);
            unreg = (Button) view.findViewById(R.id.unreg);
            if(isUsed){
                unreg.setVisibility(View.GONE);
            }
        }
    }

    private void checkLotteryNum(){
        BmobQuery<Lottery> b1 = new BmobQuery<Lottery>();
        b1.addWhereEqualTo("phoneNum",userPhone);
        b1.findObjects(mContext, new FindListener<Lottery>() {
            @Override
            public void onSuccess(List<Lottery> list) {
                if(list.size() > 0){
                    if(list.get(0).getLotteryNum() == null){
                        updateLotteryNum(0,list.get(0).getObjectId());
                    }else{
                        updateLotteryNum(list.get(0).getLotteryNum(),list.get(0).getObjectId());
                    }
                }else{
                    saveLotteryNum();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void saveLotteryNum(){
        Lottery lottery = new Lottery();
        lottery.setLotteryNum(1);
        lottery.setPhoneNum(userPhone);
        lottery.save(mContext);
    }

    private void updateLotteryNum(Integer i,String objId){
        Lottery lott = new Lottery();
        lott.setLotteryNum(i + 1);
        lott.update(mContext,objId, new UpdateListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }


}
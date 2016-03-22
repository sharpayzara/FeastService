package com.school.food.feastservice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.school.food.feastservice.adapter.OrderQueryListAdapter;
import com.school.food.feastservice.adapter.ReflushListener;
import com.school.food.feastservice.entity.Order;
import com.school.food.feastservice.entity.UserOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;

public class OrderListActivity extends Activity implements View.OnClickListener,ReflushListener {
    private Button searchBtn;
    private EditText searchTxt;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout materialRefreshLayout;
    private List<Order> orderList = new ArrayList<Order>();
    private OrderQueryListAdapter mAdapter;
    private Long sysCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mContext = this;
        initUI();

        initData();
    }

    private void initData() {
        loadData();
    }

    private void loadData(){
        getServerTime();
    }

    private void initUI() {
        sysCurrentTime = new Date().getTime();
        searchTxt = (EditText) findViewById(R.id.searchTxt);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.materialRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                orderList.clear();
                Toast.makeText(mContext,"正在加载，请稍后..",Toast.LENGTH_SHORT).show();
                loadData();
                materialRefreshLayout.finishRefresh();
            }
        });
        mAdapter =new OrderQueryListAdapter(mContext,orderList,false,this,sysCurrentTime);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.searchBtn){
            loadDataByCondition(searchTxt.getText().toString());
        }
    }

    @Override
    public void reflush() {
        Toast.makeText(mContext,"提交成功，请出票...",Toast.LENGTH_SHORT).show();
        orderList.clear();
        loadData();
    }

    @Override
    public void doFinish() {
        this.finish();
    }


    private void loadDataByCondition(String str){
        BmobQuery<UserOrder> eq1 = new BmobQuery<UserOrder>();
        eq1.addWhereEqualTo("isUnReg",false);
        BmobQuery<UserOrder> eq2 = new BmobQuery<UserOrder>();
        eq2.addWhereEqualTo("isUse",false);
        BmobQuery<UserOrder> eq3 = new BmobQuery<UserOrder>();
        eq3.addWhereContains("orderId",str);
        List<BmobQuery<UserOrder>> queries = new ArrayList<BmobQuery<UserOrder>>();
        queries.add(eq1);
        queries.add(eq2);
        queries.add(eq3);
        BmobQuery<UserOrder> bmobQuery = new BmobQuery<UserOrder>();
        bmobQuery.and(queries);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(mContext, new FindListener<UserOrder>() {
            @Override
            public void onSuccess(List<UserOrder> list) {
                orderList.clear();
                if(list.size() > 0){
                    for(UserOrder order : list){
                        orderList.add(new Order(order.getObjectId(),order.getBusinessName(),order.getOrderId().toString(),order.getTotalMoney().toString(),order.getCreatedAt()
                                ,order.getFactTotalMoney(),order.getDiscountData(),order.getPhoneNum()));
                    }
                }
                else{
                    Toast.makeText(mContext,"数据为空",Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mContext, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getServerTime() {
        Bmob.getServerTime(mContext, new GetServerTimeListener() {
            @Override
            public void onSuccess(long time) {
                sysCurrentTime = time * 1000;
                OrderQueryListAdapter.sysCurrentTime = sysCurrentTime;
                BmobQuery<UserOrder> eq1 = new BmobQuery<UserOrder>();
                eq1.addWhereEqualTo("isUnReg",false);
                BmobQuery<UserOrder> eq2 = new BmobQuery<UserOrder>();
                eq2.addWhereEqualTo("isUse",false);
                List<BmobQuery<UserOrder>> queries = new ArrayList<BmobQuery<UserOrder>>();
                queries.add(eq1);
                queries.add(eq2);
                BmobQuery<UserOrder> bmobQuery = new BmobQuery<UserOrder>();
                bmobQuery.and(queries);
                bmobQuery.order("-createdAt");
                bmobQuery.findObjects(mContext, new FindListener<UserOrder>() {
                    @Override
                    public void onSuccess(List<UserOrder> list) {
                        if(list.size() > 0){
                            orderList.clear();
                            for(UserOrder order : list){
                                orderList.add(new Order(order.getObjectId(),order.getBusinessName(),order.getOrderId().toString(),order.getTotalMoney().toString(),order.getCreatedAt()
                                        ,order.getFactTotalMoney(),order.getDiscountData(),order.getPhoneNum()));
                            }
                        } else{
                            Toast.makeText(mContext,"数据为空",Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mContext, "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(mContext,"获取系统时间失败",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

}
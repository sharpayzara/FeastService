package com.school.food.feastservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button orderButton, prizeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bmob.initialize(this,"7ff3b5ddaf309007e16c13b8fafc1fd1");
        Bmob.initialize(this,"0acf0e0a6af176a3d7febac237423ada");
        BmobInstallation.getCurrentInstallation(this).save();
        initUI();
    }

    private void initUI() {
        orderButton = (Button) findViewById(R.id.order_buton);
        prizeButton = (Button) findViewById(R.id.prizer_buton);
        orderButton.setOnClickListener(this);
        prizeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.order_buton) {
            Intent intent = new Intent(this,OrderListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.prizer_buton) {
          /*  Intent intent = new Intent(this,PrizeListActivity.class);
            startActivity(intent);*/
            Toast.makeText(this,"二期项目敬请期待",Toast.LENGTH_SHORT).show();

        }
    }
}
package com.example.asus.aweme_xp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    public boolean code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void setCode(boolean code) {
        Switch Login = (Switch) findViewById(R.id.start);//通过findViewById找到按钮控件对应的id并给它起一个名字
        if (Login.isChecked()){
            code = true;
        }
        code =false;
    }

}
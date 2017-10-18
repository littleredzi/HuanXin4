package com.example.administrator.huanxin4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.huanxin4.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editadd;
    private Button btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        editadd = (EditText) findViewById(R.id.editadd);
        btnadd = (Button) findViewById(R.id.btnadd);

        btnadd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnadd:
                String trim = editadd.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)){
                    //参数为要添加的好友的username和添加理由
                    try {
                        EMClient.getInstance().contactManager().addContact(trim, "约么");
                        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        Log.e("error","添加失败");
                    }
                }
                break;
        }
    }


}

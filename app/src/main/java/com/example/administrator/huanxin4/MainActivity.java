package com.example.administrator.huanxin4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name;
    private EditText password;
    private TextView textView3;
    private Button login;
    private Button zhuce;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (EMClient.getInstance().isLoggedInBefore()){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        initView();
    }

    private void initView() {
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        textView3 = (TextView) findViewById(R.id.textView3);
        login = (Button) findViewById(R.id.login);
        zhuce = (Button) findViewById(R.id.zhuce);
         progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在登录...");
        login.setOnClickListener(this);
        zhuce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:

                progressDialog.show();
                String nameString = name.getText().toString().trim();
                String passwordString = password.getText().toString().trim();
                if (!TextUtils.isEmpty(nameString)&&!TextUtils.isEmpty(passwordString)){
                    EMClient.getInstance().login(nameString, passwordString, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Message message = new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.e("error",s);
                            Message message = new Message();
                            message.what=2;
                            handler.sendMessage(message);

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
                break;
            case R.id.zhuce:

                break;
        }
    }


}

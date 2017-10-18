package com.example.administrator.huanxin4;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.huanxin4.fragment.FriendsFragment;
import com.example.administrator.huanxin4.fragment.MessageFragment;
import com.example.administrator.huanxin4.fragment.MyFragment;
import com.example.administrator.huanxin4.service.MyService;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.EMACallback;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private FrameLayout frame;
    private RadioButton message;
    private RadioButton friends;
    private RadioButton my;
    private RadioGroup radio;
    private FriendsFragment frameFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;
    private TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.myContext=this;
        setContentView(R.layout.activity_home);
        initView();

        //消息监听
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                final int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
                Log.e("收到消息","未读消息"+unreadMsgsCount);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(HomeActivity.this, "收到消息", Toast.LENGTH_SHORT).show();
                        number.setVisibility(View.VISIBLE);
                          number.setText(""+unreadMsgsCount);

                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        if (notification == null) return;
                        Ringtone r = RingtoneManager.getRingtone(HomeActivity.this, notification);
                        r.play();
                    }
                });



            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });

        setListener();
    }

    private void initView() {
        frame = (FrameLayout) findViewById(R.id.frame);
        message = (RadioButton) findViewById(R.id.message);
        friends = (RadioButton) findViewById(R.id.friends);
        my = (RadioButton) findViewById(R.id.my);
        radio = (RadioGroup) findViewById(R.id.radio);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        frameFragment = new FriendsFragment();
        transaction.add(R.id.frame, frameFragment);
        transaction.commit();
        number = (TextView) findViewById(R.id.number);



    }

    private void setListener() {
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                setDelete(supportFragmentManager);
                switch (i) {
                    case R.id.message:
                        if (messageFragment == null) {
                            messageFragment = new MessageFragment();
                            fragmentTransaction.add(R.id.frame, messageFragment).commit();
                        } else {
                            fragmentTransaction.show(messageFragment).commit();
                        }
                        break;
                    case R.id.friends:
                        if (frameFragment == null) {
                            frameFragment = new FriendsFragment();
                            fragmentTransaction.add(R.id.frame, frameFragment).commit();
                        } else {
                            fragmentTransaction.show(frameFragment).commit();
                        }
                        break;
                    case R.id.my:
                        if (myFragment == null) {
                            myFragment = new MyFragment();
                            fragmentTransaction.add(R.id.frame, myFragment).commit();
                        } else {
                            fragmentTransaction.show(myFragment).commit();
                        }

                        break;

                }
            }
        });
    }

    private void setDelete(FragmentManager supportFragmentManager) {
        if (messageFragment != null) {
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.hide(messageFragment).commit();
        }
        if (frameFragment != null) {
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.hide(frameFragment).commit();
        }
        if (myFragment != null) {
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.hide(myFragment).commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadMsgsCount>0){
            number.setText(""+unreadMsgsCount);
            number.setVisibility(View.VISIBLE);
        }else{
            number.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(HomeActivity.this, MyService.class);
        //开启服务
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Activity","销毁了");
    }
}

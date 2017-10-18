package com.example.administrator.huanxin4.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.huanxin4.App;
import com.example.administrator.huanxin4.HomeActivity;
import com.example.administrator.huanxin4.R;
import com.example.administrator.huanxin4.activity.ChatActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import static com.example.administrator.huanxin4.R.id.number;
import static com.xiaomi.push.service.y.m;
import static com.xiaomi.push.service.y.s;

public class MyService extends Service {
    boolean boo=true;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate","....");
        final StringBuffer sb=new StringBuffer();
        //消息监听
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {

                for (int i = 0; i <list.size() ; i++) {
                    EMMessage message = list.get(i);
                    String from = message.getFrom();
                    sb.append(from+"!");
                }
                String s = sb.toString();
                String[] split = s.split("!");
                for (int i = 0; i <split.length ; i++) {
                    if (split[0].equals(split[i])){
                        boo=true;
                    }else{
                        boo=false;
                    }
                }
                if (!boo){
                    Intent intent = new Intent(App.myContext, HomeActivity.class);
                    PendingIntent activity = PendingIntent.getActivity(App.myContext, 0, intent, 0);
                    NotificationManager systemService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification builder = new NotificationCompat.Builder(App.myContext)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round))
                            .setContentTitle("收多个好友的消息")
                            .setContentText("点击跳转")
                            .setContentIntent(activity)
                            .setAutoCancel(true)
                            .build();
                    systemService.notify(1,builder);
                }else if (boo){
                    Intent intent = new Intent(App.myContext, ChatActivity.class);
                    intent.putExtra("username",split[0]);
                    PendingIntent activity = PendingIntent.getActivity(App.myContext, 0, intent, 0);
                    NotificationManager systemService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification builder = new NotificationCompat.Builder(App.myContext)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round))
                            .setContentTitle("收一个好友的消息")
                            .setContentText("点击跳转")
                            .setContentIntent(activity)
                            .setAutoCancel(true)
                            .build();
                    systemService.notify(1,builder);
                }
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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand","....");
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service被杀了","....");

        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("MyFragment", "退出成功");
            }

            @Override
            public void onError(int i, String s) {
                Log.e("MyFragment", "退出失败" + i + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}

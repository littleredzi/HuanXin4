package com.example.administrator.huanxin4;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class App extends Application {

    public static Context myContext;
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        EMOptions options = new EMOptions();
        options.setAutoLogin(false);
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("TAg", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().init(this, options);



        //添加好友的监听
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                Toast.makeText(getApplicationContext(), "对方同意", Toast.LENGTH_SHORT).show();
                Log.e("dzh", "对方同意");
            }

            @Override
            public void onContactDeleted(String s) {

            }

            //收到好友请求
            @Override
            public void onContactInvited(String s, String s1) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(s);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("error","添加失败");
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                Toast.makeText(getApplicationContext(), "有新的好友", Toast.LENGTH_SHORT).show();
                Log.e("dzh","有新的好友");
            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });
    }
}

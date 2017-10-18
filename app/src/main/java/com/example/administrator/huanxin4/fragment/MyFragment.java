package com.example.administrator.huanxin4.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.huanxin4.MainActivity;
import com.example.administrator.huanxin4.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import static android.R.attr.id;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity activity;
    private Button my_btn_exit;
    private Button creat_chatroom;


    String subject ="hello";
    String description="world";
    String welcomeMessage="come";
    int members=1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_my, container, false);
        activity = getActivity();
        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        my_btn_exit = (Button) inflate.findViewById(R.id.my_btn_exit);

        my_btn_exit.setOnClickListener(this);
        creat_chatroom = (Button) inflate.findViewById(R.id.creat_chatroom);
        creat_chatroom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_btn_exit:
                //退出当前账号
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("MyFragment", "退出成功");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("MyFragment", "退出失败" + i + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
            case R.id.creat_chatroom:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().chatroomManager().createChatRoom(subject, description, welcomeMessage, members, null);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "创建成功,聊天室id为"+id, Toast.LENGTH_SHORT).show();

                                }
                            });
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "创建失败"+e.getErrorCode()+","+e.getDescription(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                }).start();

                break;
        }
    }
}

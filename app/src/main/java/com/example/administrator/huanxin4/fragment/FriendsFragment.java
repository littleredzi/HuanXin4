package com.example.administrator.huanxin4.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.huanxin4.R;
import com.example.administrator.huanxin4.activity.AddActivity;
import com.example.administrator.huanxin4.activity.ChatActivity;
import com.example.administrator.huanxin4.view.ContactItemView;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private EaseTitleBar title_bar;
    private EaseContactList friends_listview;
    private ListView listView;
    List<EaseUser> listdata=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    friends_listview.refresh();
                    break;
            }
        }
    };
    private FragmentActivity activity;
    private ContactItemView applicationItem;
    private View inflate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         inflate = inflater.inflate(R.layout.fragment_friends, container, false);
        ImageView add = inflate.findViewById(R.id.add);
        //添加好友
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
              startActivityForResult(intent,100);
            }
        });
        activity = getActivity();








        return inflate;
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, ChatActivity.class);
                EaseUser easeUser = listdata.get(i);
                intent.putExtra("username",easeUser.getUsername());
                startActivity(intent);
            }
        });
    }

    private void initView(View inflate) {
        title_bar = (EaseTitleBar) inflate.findViewById(R.id.title_bar);
        title_bar.setTitle("联系人");
        friends_listview = (EaseContactList) inflate.findViewById(R.id.friends_listview);
        friends_listview.init(listdata);
        listView = friends_listview.getListView();
    }

    //获取好友
    public void getFriends() {
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                strings.size();
                listdata.clear();
                for (String name:strings){
                    listdata.add(new EaseUser(name));
                }
                Message message=new Message();
                message.what=100;
                handler.sendMessage(message);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume","显示了");
        initView(inflate);
        getFriends();
        setListener();

    }
}

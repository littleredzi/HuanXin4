package com.example.administrator.huanxin4.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.administrator.huanxin4.R;
import com.example.administrator.huanxin4.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private FrameLayout message_frame;
    private EaseConversationListFragment conversationListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_message, container, false);


        initView(inflate);

        conversationListFragment = new EaseConversationListFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.message_frame, conversationListFragment).commit();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("username",conversation.conversationId());
                startActivity(intent);
            }
        });


        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                conversationListFragment.refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                conversationListFragment.refresh();
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                conversationListFragment.refresh();
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                conversationListFragment.refresh();
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                conversationListFragment.refresh();
            }
        });


        return inflate;
    }

    private void initView(View inflate) {
        message_frame = (FrameLayout) inflate.findViewById(R.id.message_frame);
    }

}

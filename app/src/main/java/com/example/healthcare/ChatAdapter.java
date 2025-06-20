package com.example.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = getItem(position);

        if (convertView == null || convertView.getTag() == null || (boolean) convertView.getTag() != message.isUser()) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    message.isUser() ? R.layout.item_user_message : R.layout.item_bot_message,
                    parent, false
            );
            convertView.setTag(message.isUser()); // Store type in tag to avoid reusing wrong layout
        }

        TextView msgText = convertView.findViewById(R.id.messageText);
        msgText.setText(message.getMessage());

        return convertView;
    }
}

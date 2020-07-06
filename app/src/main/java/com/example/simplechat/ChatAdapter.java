package com.example.simplechat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);
        ViewHolder view = new ViewHolder(contactView);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        if(isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageMe.setVisibility(View.GONE);
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        final ImageView profileView;
        if (isMe) {
            profileView = holder.imageMe;
        } else {
            profileView = holder.imageOther;
        }
        Glide.with(mContext).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());
    }

    private static String getProfileUrl(String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInteger = new BigInteger(hash);
            hex = bigInteger.abs().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = itemView.findViewById(R.id.ivProfileOther);
            imageMe = itemView.findViewById(R.id.ivProfileMe);
            body = itemView.findViewById(R.id.tvBody);
        }
    }
}

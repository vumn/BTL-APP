package com.example.btl_app.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.btl_app.Model.User;
import com.example.btl_app.R;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    static class ViewHolderUser {
        TextView lvUserId;
        TextView lvUserName;
        TextView lvRole;
    }
    private List<User> userList;
    private Context context;


    public UserAdapter(Context context, List<User> userList)
    {
        this.context = context;
        this.userList = userList;
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderUser holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_user, viewGroup, false);

            holder = new ViewHolderUser();
            holder.lvUserId = view.findViewById(R.id.lvUserId);
            holder.lvUserName = view.findViewById(R.id.lvUsername);
            holder.lvRole = view.findViewById(R.id.lvRole);

            view.setTag(holder);
        } else {
            holder = (ViewHolderUser) view.getTag();
        }

        User user = userList.get(i);

        holder.lvUserId.setText(user.getUserId());
        holder.lvUserName.setText(user.getUserName());
        holder.lvRole.setText(user.getRole());

        return view;
    }
}

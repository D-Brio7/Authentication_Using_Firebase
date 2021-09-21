package com.example.admin.brios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 17/10/2019.
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList<User> users;
    private Context context;

    public CustomAdapter(ArrayList<User> list, Context cont){
        this.users = list;
        this.context = cont;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public Object getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout, null);
            holder = new ViewHolder();
            holder.id = (TextView)convertView.findViewById(R.id.id);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.email = (TextView)convertView.findViewById(R.id.email);
            holder.contact = (TextView)convertView.findViewById(R.id.contact);
            holder.address = (TextView)convertView.findViewById(R.id.address);
            holder.password = (TextView)convertView.findViewById(R.id.password);
            convertView.setTag(holder);
        }
        else
            {
                holder = (ViewHolder)convertView.getTag();
            }

        User user = users.get(position);
        holder.id.setText(Integer.toString(user.getId()));
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.contact.setText(user.getContact());
        holder.address.setText(user.getAddress());
        holder.password.setText(user.getPassword());
        return convertView;
    }

    private static class ViewHolder
    {
        public TextView id;
        public TextView name;
        public TextView email;
        public TextView contact;
        public TextView address;
        public TextView password;
    }
}

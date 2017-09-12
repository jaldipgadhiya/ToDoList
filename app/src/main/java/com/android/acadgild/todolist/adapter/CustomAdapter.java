package com.android.acadgild.todolist.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.acadgild.todolist.R;
import com.android.acadgild.todolist.model.ToDoData;

import java.util.ArrayList;

/**
 * Created by Jal on 20-07-2017.
 * Adapter class for displaying Expense Income Information List
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<ToDoData> data;
    LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<ToDoData> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            //Inflate View
            convertView = inflater.inflate(R.layout.custom_row, parent, false);
            // create ViewHolder
            holder = new ViewHolder();
            holder.titleDueDate = (TextView) convertView.findViewById(R.id.tvTitleDueDate);
            holder.title = (TextView) convertView.findViewById(R.id.toDoTitle);
            holder.desc = (TextView) convertView.findViewById(R.id.toDoDesc);
            holder.dueDate = (TextView) convertView.findViewById(R.id.toDoDueDate);
            holder.image = (ImageView) convertView.findViewById(R.id.imgTaskDone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        holder.titleDueDate.setText(data.get(position).getKeyDate());
        holder.title.setText(data.get(position).getKeyTitle());
        holder.desc.setText(data.get(position).getKeyDescription());
        holder.dueDate.setText(data.get(position).getKeyDate());
        if(data.get(position).getKeyPhotoImage()!=null){
        byte[] bytes = data.get(position).getKeyPhotoImage();
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.image.setImageBitmap(bitmap);}


        return convertView;
    }


    public class ViewHolder {
        //TextView view;
        TextView titleDueDate, title, desc, dueDate;
        ImageView image;
    }


}

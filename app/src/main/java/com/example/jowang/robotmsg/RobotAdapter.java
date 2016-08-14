package com.example.jowang.robotmsg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jowang on 16/8/12.
 */
public class RobotAdapter extends BaseAdapter {
    private ArrayList<RobotMessage> list;
    Context context;

    public RobotAdapter(Context ctx, ArrayList<RobotMessage> list) {
        this.list = list;
        this.context=ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private final class myHolder
    {
        TextView mDate;
        TextView mMsg;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        RobotMessage robotMessage =list.get(position);
        myHolder holder=null;
        if (getItemViewType(position)==0){
            view=LayoutInflater.from(context).inflate(R.layout.list_cell_output,viewGroup,false);
            holder=new myHolder();
            holder.mDate=(TextView)view.findViewById(R.id.output_date);
            holder.mMsg=(TextView)view.findViewById(R.id.output_msg);
            view.setTag(holder);
        }else if (getItemViewType(position)==1){
            view= LayoutInflater.from(context).inflate(R.layout.list_cell_input,viewGroup,false);
            holder=new myHolder();
            holder.mDate=(TextView)view.findViewById(R.id.input_date);
            holder.mMsg=(TextView)view.findViewById(R.id.input_msg);
            view.setTag(holder);
        }
        holder=(myHolder)view.getTag();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.mDate.setText(df.format(robotMessage.getDate()));
        holder.mMsg.setText(robotMessage.getMsg());
        return view;
    }
    @Override
    public int getItemViewType(int position) {
        RobotMessage robotMessage =list.get(position);
        if (robotMessage.getType()== RobotMessage.Type.OUTPUT){
            return 0;
        }else if (robotMessage.getType()== RobotMessage.Type.INPUT){

            return 1;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }
}

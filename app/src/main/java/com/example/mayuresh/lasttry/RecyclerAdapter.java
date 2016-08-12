package com.example.mayuresh.lasttry;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * Created by rajat on 2/8/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Notices> dataSource;
    public RecyclerAdapter(List<Notices> dataArgs){
        dataSource = dataArgs;
 
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single, parent, false);
 
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
 
 
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notices n=dataSource.get(position);
        holder.textView.setText(n.getTitle());
        holder.imageView.setBackgroundResource(R.drawable.ic_sms_black_24dp);
        holder.imageView.setVisibility(View.VISIBLE);
        holder.timestamp.setText(n.getTime());
        holder.dis.setText(n.getMsg());
    }
 
    @Override
    public int getItemCount() {
        return dataSource.size();
    }
 
    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;
        protected TextView dis;
        protected TextView timestamp;
        protected ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView =  (TextView) itemView.findViewById(R.id.txt);
            imageView= (ImageView) itemView.findViewById(R.id.img);
            timestamp= (TextView) itemView.findViewById(R.id.time);
            dis= (TextView) itemView.findViewById(R.id.dics);
        }
 
 
    }
}
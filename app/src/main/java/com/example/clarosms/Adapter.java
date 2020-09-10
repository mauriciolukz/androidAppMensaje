package com.example.clarosms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<String> titles,descriptions,images,sms;
    private LayoutInflater inflater;

    Adapter(Context context, List<String> titles,List<String> descriptions,List<String> sms,List<String> images){
        Log.d("data", "titles -> "+titles);
        this.titles = titles;
        this.descriptions = descriptions;
        this.images = images;
        this.sms = sms;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = titles.get(position);
        String desc = descriptions.get(position);
        String sss = sms.get(position);
        holder.title.setText(title);
        holder.desc.setText(desc);
        holder.sss.setText(sss);

    }

    @Override
    public int getItemCount() {
        //return 10;
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc,sss;
        ImageView thumbnail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            thumbnail = itemView.findViewById(R.id.cardImg);
            sss = itemView.findViewById(R.id.sms);
        }
    }
}

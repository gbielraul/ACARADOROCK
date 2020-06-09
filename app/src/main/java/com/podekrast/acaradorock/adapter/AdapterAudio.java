package com.podekrast.acaradorock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.podekrast.acaradorock.R;

import java.util.List;

public class AdapterAudio extends RecyclerView.Adapter<AdapterAudio.MyViewHolder> {

    private List<String> titles;
    private Context context;

    public AdapterAudio(List<String> titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_audio, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = titles.get(position);
        holder.txtTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title_player_audio);
        }
    }
}

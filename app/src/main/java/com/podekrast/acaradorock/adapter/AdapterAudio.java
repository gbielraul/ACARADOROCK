package com.podekrast.acaradorock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.podekrast.acaradorock.R;
import com.podekrast.acaradorock.model.Audio;

import java.util.List;

public class AdapterAudio extends RecyclerView.Adapter<AdapterAudio.AudioViewHolder> {

    private List<Audio> audios;

    public AdapterAudio(List<Audio> audios) {
        this.audios = audios;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recupera o layout e retorna para o ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        //Recupera o titulo do programa e chama o método que adiciona no TextView
        Audio audio = audios.get(position);
        holder.bind(audio);
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitle, mTxtDate;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.txt_title_audio);
            mTxtDate = itemView.findViewById(R.id.txt_date_audio);
        }

        void bind(Audio audio) {
            mTxtTitle.setText(audio.getProgramTitle());
            mTxtDate.setText(audio.getProgramDate());
        }
    }
}

package com.podekrast.acaradorock.audios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.podekrast.acaradorock.R;
import com.podekrast.acaradorock.helper.ConfigFirebase;

import java.util.ArrayList;
import java.util.List;

import com.podekrast.acaradorock.adapter.AdapterAudio;
import com.podekrast.acaradorock.helper.RecyclerItemClickListener;
import com.podekrast.acaradorock.model.Audio;

public class BlackSabadleAudioActivity extends AppCompatActivity {

    private String programName = "BlackSabadle";
    private RecyclerView mRecyclerView;
    private DatabaseReference reference;
    private AdapterAudio adapter;
    private Audio audio;
    private List<Audio> audios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_audio);

        //Recupera as Views do XML
        ImageView mBtnReturn = findViewById(R.id.btn_return_audio);
        TextView mTxtTitleProgram = findViewById(R.id.txt_title_program);
        mRecyclerView = findViewById(R.id.recycler_view_audio);

        //Muda o texto da ActionBar para o nome do programa
        mTxtTitleProgram.setText(programName);

        //Adiciona o evento de clique para o botão que retorna para tela anterior
        mBtnReturn.setOnClickListener(blackSabadleReturn);

        //Recupera a instância do model Audio
        audio = new Audio();
        //Recupera a referência do programa no FirebaseDatabase
        reference = ConfigFirebase.getDatabase().child("audios").child(programName);
        //Cria uma lista de audios
        audios = new ArrayList<>();

        //Chama o método que recupera os dados do programa
        getProgramData();

        //Chama o método que configura a RecyclerView
        configureRecyclerView();

        //Chama o método que adiciona o evento de clique na RecyclerView
        setRecyclerClickListener();
    }

    //Metodo que configura a RecyclerView
    private void configureRecyclerView() {
        //Adiciona um LayoutManager para a RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        //Adiciona um adapter para a RecyclerView
        adapter = new AdapterAudio(audios);
        mRecyclerView.setAdapter(adapter);
    }

    //Método que adiciona o evento de clique na RecyclerView
    private void setRecyclerClickListener() {
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Recupera os dados do programa no model Audio
                audio.setProgramName(programName);
                audio.setProgramId(audios.get(position).getProgramId());
                audio.setProgramTitle(audios.get(position).getProgramTitle());
                audio.setProgramDate(audios.get(position).getProgramDate());
                audio.setProgramUrl(audios.get(position).getProgramUrl());

                //Inicia a activity do player
                Intent intent = new Intent(BlackSabadleAudioActivity.this, PlayerAudioActivity.class);
                //Passando o título e o do áudio pela Intent para fazer referência
                intent.putExtra("selectedAudio", audio);
                //Inicia a Activity
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    //Método que recupera os dados do programa
    private void getProgramData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Para cada *data* nos filhos de *dataSnapshot*
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //Recupera os dados do programa
                    Audio audio = new Audio();
                    audio.setProgramId(data.getKey());
                    audio.setProgramTitle(data.child("name").getValue(String.class));
                    audio.setProgramDate(data.child("date").getValue(String.class));
                    audio.setProgramUrl(data.child("url").getValue(String.class));
                    //Adiciona na lista de audios
                    audios.add(audio);
                }
                //Atualiza a RecyclerView toda vez que adiciona um item novo na lista
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método que retorna para tela anterior
    private View.OnClickListener blackSabadleReturn = v -> finish();
}

package com.podekrast.acaradorock.audios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.podekrast.acaradorock.R;
import com.podekrast.acaradorock.adapter.AdapterAudio;
import com.podekrast.acaradorock.helper.ConfigFirebase;
import com.podekrast.acaradorock.helper.RecyclerItemClickListener;
import com.podekrast.acaradorock.model.Audio;

import java.util.ArrayList;

public class BackToStageAudioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdapterAudio mAdapter;
    private ArrayList<String> titlesList;
    private ArrayList<String> idList;
    private ArrayList<String> urlList;
    private DatabaseReference reference;
    private Audio mAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_back_to_stage_audio);

        //Recupera a RecyclerView
        recyclerView = findViewById(R.id.rv_back_to_stage_audio);
        //Recupera a referência do programa no FirebaseDatabase
        reference = ConfigFirebase.getDatabase().child("audios").child("BackToStage");
        //Cria uma lista de títulos
        titlesList = new ArrayList<>();
        //Cria uma lista de Id's
        idList = new ArrayList<>();
        //Cria uma lista de url's
        urlList = new ArrayList<>();

        //Recupera a instância do model Audio
        mAudio = new Audio();

        //Adiciona o LayoutManager para a RecyclerView
        mLayoutManager = new LinearLayoutManager(BackToStageAudioActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Adiciona o Adapter para a RecyclerView
        mAdapter = new AdapterAudio(titlesList, BackToStageAudioActivity.this);
        recyclerView.setAdapter(mAdapter);

        //Chama o método que recupera os títulos, os id's e as url's
        titles();

        //Adiciona evento de clique na RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(BackToStageAudioActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Recupera o título do item clicado
                String selectedName = titlesList.get(position);
                //Recupera o id do item clicado
                String selectedId = idList.get(position);
                //Recupera a url do item clicado
                String selectedUrl = urlList.get(position);

                //Adiciona os detalhes do audio clicado para o model Audio
                mAudio.addAudio(
                        selectedId,
                        "BackToStage",
                        selectedName,
                        selectedUrl
                );

                //Toast.makeText(RockRollaAudioActivity.this, selectedAudio, Toast.LENGTH_SHORT).show();

                //Inicia a activity do player
                Intent intent = new Intent(BackToStageAudioActivity.this, PlayerAudioActivity.class);
                //Passando o título e o do áudio pela Intent para fazer referência
                intent.putExtra("selectedAudio", mAudio);

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

    //Método que recupera os títulos, os id's e as url's
    private void titles() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Para cada *data* nos filhos de *dataSnapshot*
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //Recupera os títulos
                    String title = data.child("name").getValue(String.class);
                    titlesList.add(title);
                    //Recupera os id's
                    String id = data.getKey();
                    idList.add(id);
                    //Recupera as url's
                    String url = data.child("url").getValue(String.class);
                    urlList.add(url);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void backToStageAudioReturn(View view) {
        finish();
    }
}

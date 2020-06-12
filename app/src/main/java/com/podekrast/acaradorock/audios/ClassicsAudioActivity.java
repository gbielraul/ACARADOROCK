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
import com.podekrast.acaradorock.helper.ConfigFirebase;

import java.util.ArrayList;

import com.podekrast.acaradorock.adapter.AdapterAudio;
import com.podekrast.acaradorock.helper.RecyclerItemClickListener;
import com.podekrast.acaradorock.model.Audio;

public class ClassicsAudioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> titlesList;
    private ArrayList<String> idList;
    private ArrayList<String> urlList;
    private DatabaseReference reference;
    private AdapterAudio adapter;
    private Audio audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classics_audio);

        //Recupera a instância do model Audio
        audio = new Audio();
        //Recupera a RecyclerView
        recyclerView = findViewById(R.id.rv_classics_audio);
        //Recupera a referência do programa no FirebaseDatabase
        reference = ConfigFirebase.getDatabase().child("audios").child("Classics");
        //Cria uma lista de títulos
        titlesList = new ArrayList<>();
        //Cria uma lista de Id's
        idList = new ArrayList<>();
        //Cria uma lista de url's
        urlList = new ArrayList<>();

        //Adiciona um LayoutManager para a RecyclerView
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(ClassicsAudioActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Adiciona um adapter para a RecyclerView
        adapter = new AdapterAudio(titlesList, ClassicsAudioActivity.this);
        recyclerView.setAdapter(adapter);

        //Chama o método que recupera os títulos, os id's e as url's
        titles();

        //Adiciona evento de clique na RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ClassicsAudioActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Recupera o título do item clicado
                String selectedName = titlesList.get(position);
                //Recupera o id do item clicado
                String selectedId = idList.get(position);
                //Recupera a url do item clicado
                String selectedUrl = urlList.get(position);

                //Adiciona os detalhes do audio clicado para o model Audio
                audio.addAudio(
                        selectedId,
                        "Classics",
                        selectedName,
                        selectedUrl
                );

                //Toast.makeText(RockRollaAudioActivity.this, selectedAudio, Toast.LENGTH_SHORT).show();

                //Inicia a activity do player
                Intent intent = new Intent(ClassicsAudioActivity.this, PlayerAudioActivity.class);
                //Passando o título e o do áudio pela Intent para fazer referência
                intent.putExtra("selectedAudio", audio);

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

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void classicsAudioReturn(View view) {
        finish();
    }
}

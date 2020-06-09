package com.podekrast.acaradorock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.podekrast.acaradorock.helper.ConfigFirebase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private double mCurrentVersion;
    private RelativeLayout mUpdateScreen;
    private ScrollView mScrollViewHome;
    private LinearLayout mBtnLogout;
    private String downloadUrl;
    private ProgressBar mProgressBarHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);

        //Indica a versão atual do app
        mCurrentVersion = 1;

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getAuth();
        //Recupera a referência do Firebase Database
        mReference = ConfigFirebase.getDatabase();

        //Recupera a tela de atualização
        mUpdateScreen = findViewById(R.id.update_screen);
        //Recupera o ScrollView
        mScrollViewHome = findViewById(R.id.scrollView_home);
        //Recupera o botão de Logout
        mBtnLogout = findViewById(R.id.btn_logout);

        //Recupera a ProgressBar
        mProgressBarHome = findViewById(R.id.progress_bar_home);

        //Chama o método que verifica a versão do app
        verifyVersion();
    }

    //Método que verifica a versão do app
    private void verifyVersion() {
        //Recupera a referência para recuperar a versão atualizada
        DatabaseReference ref = mReference.child("config");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Recupera a versão atualizada
                double updatedVersion = dataSnapshot.child("version").getValue(double.class);
                //Recupera a url do download
                downloadUrl = dataSnapshot.child("downloadUrl").getValue(String.class);
                //Se a versão atual for menor que a versão atualizada
                if (mCurrentVersion < updatedVersion) {
                    //Torna invisivel o ScrollView
                    mScrollViewHome.setVisibility(View.INVISIBLE);
                    //Torna invisivel o botão de Logout
                    mBtnLogout.setVisibility(View.INVISIBLE);
                    //Torna visivel a tela de atualização
                    mUpdateScreen.setVisibility(View.VISIBLE);
                    //Torna a ProgressBar invisivel
                    mProgressBarHome.setVisibility(View.INVISIBLE);
                } else {
                    //Torna visivel o ScrollView
                    mScrollViewHome.setVisibility(View.VISIBLE);
                    //Torna visivel o botão de Logout
                    mBtnLogout.setVisibility(View.VISIBLE);
                    //Torna invisivel a tela de atualização
                    mUpdateScreen.setVisibility(View.GONE);
                    //Torna a ProgressBar invisivel
                    mProgressBarHome.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Método que atualiza o app
    public void updateApp(View view) {
        //Vai para o link de download
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
        startActivity(intent);
    }

    //Método que realiza o LogOut
    public void logOut(View view) {

        //Realiza o LogOut
        mAuth.signOut();

        //Volta para a tela de LogIn
        Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    //Método que chama a Activity de áudios
    public void audios(View view) {

        //Chama a Activity de áudios
        Intent intent = new Intent(HomeActivity.this, AudiosActivity.class);
        startActivity(intent);
    }

    //Método que vai para o app da rádio na PlayStore
    public void openRadioApp(View view) {

        //Vai para o app Rádio Joiville.net na PlayStore
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.webradiocast.ovjhvkynchtq"));
        startActivity(intent);
    }

    //Método que abre a página da TV Sobrinho MS
    public void openTVSobrinhoPage(View view) {

        //Abre a página da TV Sobrinho MS
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tvsobrinhoms/"));
        startActivity(intent);
    }

    //Método que abre a página da A Cara do Rock
    public void openACDRPage(View view) {

        //Abre a página da A Cara do Rock
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/acaradorock/"));
        startActivity(intent);
    }

    //Método que abre o Instagram da A Cara do Rock
    public void openPodekreInstagram(View view) {

        //Abre o Instagram da A Cara do Rock
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/podekre/"));
        startActivity(intent);
    }

    //Método que abre o Youtube da A Cara do Rock
    public void openPodekreChannel(View view) {

        //Abre o Youtube da A Cara do Rock
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCC0F9GYaa6OZk_FTwZgz09A"));
        startActivity(intent);
    }
}

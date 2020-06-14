package com.podekrast.acaradorock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private ScrollView mScrollView;
    private LinearLayout mBtnSignOut;
    private String downloadUrl;
    private ProgressBar mProgressBarHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Indica a versão atual do app
        mCurrentVersion = 1;

        //Recupera a instância do Firebase Auth
        mAuth = ConfigFirebase.getAuth();
        //Recupera a referência do Firebase Database
        mReference = ConfigFirebase.getDatabase();

        //Recupera as Views do XML
        mUpdateScreen = findViewById(R.id.update_screen);
        mScrollView = findViewById(R.id.scrollView_home);
        mBtnSignOut = findViewById(R.id.btn_sign_out);
        ImageView mBtnAudios = findViewById(R.id.btn_audios);
        ImageView mBtnRadioApp = findViewById(R.id.btn_radio_app);
        ImageView mBtnTvsPage = findViewById(R.id.btn_tvs);
        ImageView mBtnAcdrPage = findViewById(R.id.btn_acdr_page);
        ImageView mBtnPdkInsta = findViewById(R.id.btn_podekre_instagram);
        ImageView mBtnPdkChannel = findViewById(R.id.btn_podekre_channel);
        mProgressBarHome = findViewById(R.id.progress_bar_home);

        //Chama o método que verifica a versão do app
        verifyVersion();

        //Adiciona o evento de clique que sai da conta do usuário
        mBtnSignOut.setOnClickListener(signOut);
        //Adiciona o evento de clique que chama AudiosActivity
        mBtnAudios.setOnClickListener(openAudiosActivity);
        //Adiciona o evento de clique que vai para o aplicativo da radio
        mBtnRadioApp.setOnClickListener(openRadioApp);
        //Adiciona o evento de clique que vai para a pagina da TV Sobrinho
        mBtnTvsPage.setOnClickListener(openTVSobrinhoPage);
        //Adiciona o evento de clique que vai para a pagina da A Cara do Rock
        mBtnAcdrPage.setOnClickListener(openACDRPage);
        //Adiciona o evento de clique que vai para o instagram do Podekre
        mBtnPdkInsta.setOnClickListener(openPodekreInstagram);
        //Adiciona o evento de clique que vai para o canal no Youtube do Podekre
        mBtnPdkChannel.setOnClickListener(openPodekreChannel);
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
                    mScrollView.setVisibility(View.INVISIBLE);
                    //Torna invisivel o botão de Logout
                    mBtnSignOut.setVisibility(View.INVISIBLE);
                    //Torna visivel a tela de atualização
                    mUpdateScreen.setVisibility(View.VISIBLE);
                    //Torna a ProgressBar invisivel
                    mProgressBarHome.setVisibility(View.INVISIBLE);
                } else {
                    //Torna visivel o ScrollView
                    mScrollView.setVisibility(View.VISIBLE);
                    //Torna visivel o botão de Logout
                    mBtnSignOut.setVisibility(View.VISIBLE);
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
    private View.OnClickListener signOut = v -> {
        //Instancia um AlertDialog perguntando se realmente deseja sair da conta
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle(R.string.logout_dialog_title)
                .setMessage(R.string.logout_dialog_message)
                .setPositiveButton(R.string.yes, (DialogInterface dialog, int which) -> {

                    //Realiza o LogOut
                    mAuth.signOut();
                    //Volta para a tela de LogIn
                    Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .create()
                .show();

    };

    //Método que chama a Activity de áudios
    private View.OnClickListener openAudiosActivity = v -> {
        //Chama a Activity de áudios
        startActivity(new Intent(HomeActivity.this, AudiosActivity.class));
    };

    //Método que vai para o app da rádio na PlayStore
    private View.OnClickListener openRadioApp = v -> {
        //Vai para o app Rádio Joiville.net na PlayStore
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.webradiocast.ovjhvkynchtq")));
    };

    //Método que abre a página da TV Sobrinho MS
    private View.OnClickListener openTVSobrinhoPage = v -> {

        //Abre a página da TV Sobrinho MS
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tvsobrinhoms/"));
        startActivity(intent);
    };

    //Método que abre a página da A Cara do Rock
    private View.OnClickListener openACDRPage = v -> {
        //Abre a página da A Cara do Rock
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/acaradorock/"));
        startActivity(intent);
    };

    //Método que abre o Instagram da A Cara do Rock
    private View.OnClickListener openPodekreInstagram = v -> {
        //Abre o Instagram da A Cara do Rock
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/podekre/"));
        startActivity(intent);
    };

    //Método que abre o Youtube da A Cara do Rock
    private View.OnClickListener openPodekreChannel = v -> {
        //Abre o Youtube da A Cara do Rock
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCC0F9GYaa6OZk_FTwZgz09A"));
        startActivity(intent);
    };
}
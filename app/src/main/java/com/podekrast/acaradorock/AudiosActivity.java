package com.podekrast.acaradorock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.podekrast.acaradorock.audios.BackToStageAudioActivity;
import com.podekrast.acaradorock.audios.ClassicsAudioActivity;
import com.podekrast.acaradorock.audios.GameSoundTrackAudioActivity;
import com.podekrast.acaradorock.audios.MadeInBrazilAudioActivity;
import com.podekrast.acaradorock.audios.RockRollaAudioActivity;
import com.podekrast.acaradorock.audios.TributeAudioActivity;
import com.podekrast.acaradorock.audios.UnderCoverAudioActivity;

public class AudiosActivity extends AppCompatActivity {

    private ImageView mBtnBlackSabadle, mBtnMadeInBrazil, mBtnFeitoNoBrasil, mBtnEspecial, mBtnVoltaAoMundo, mBtnAHistoria, mBtnTemporada1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_audios);

        mBtnBlackSabadle = findViewById(R.id.btn_black_sabadle);
        mBtnMadeInBrazil = findViewById(R.id.btn_made_in_brazil);
        mBtnFeitoNoBrasil = findViewById(R.id.btn_feito_no_brasil);
        mBtnEspecial = findViewById(R.id.btn_especial);
        mBtnVoltaAoMundo = findViewById(R.id.btn_volta_ao_mundo);
        mBtnAHistoria = findViewById(R.id.btn_a_historia);
        mBtnTemporada1 = findViewById(R.id.btn_temporada_1);

        setBackground();
    }

    //Método que muda o fundo dos botões
    public void setBackground() {

        //Se a versão da api do android do usuário for maior ou igual que a api 21 muda o fundo dos botões
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBtnBlackSabadle.setBackgroundResource(R.drawable.ripple_bg_btn_black_sabadle);
            mBtnMadeInBrazil.setBackgroundResource(R.drawable.ripple_bg_btn_made_in_brazil);
            mBtnFeitoNoBrasil.setBackgroundResource(R.drawable.ripple_bg_btn_feito_no_brasil);
            mBtnEspecial.setBackgroundResource(R.drawable.ripple_bg_btn_especial);
            mBtnVoltaAoMundo.setBackgroundResource(R.drawable.ripple_bg_btn_volta_ao_mundo);
            mBtnAHistoria.setBackgroundResource(R.drawable.ripple_bg_btn_a_historia);
            mBtnTemporada1.setBackgroundResource(R.drawable.ripple_bg_btn_temporada_1);
        }
    }

    //Método que retorna para tela principal
    public void audiosReturn(View view) {
        finish();
    }

    /* Métodos que chamam a Activity selecionada */
    public void openRockRollaAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, RockRollaAudioActivity.class);
        startActivity(intent);
    }

    public void openBackToStageAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, BackToStageAudioActivity.class);
        startActivity(intent);
    }

    public void openClassicsAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, ClassicsAudioActivity.class);
        startActivity(intent);
    }

    public void openUnderCoverAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, UnderCoverAudioActivity.class);
        startActivity(intent);
    }

    public void openMadeInBrazilAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, MadeInBrazilAudioActivity.class);
        startActivity(intent);
    }

    public void openGameSoundTrackAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, GameSoundTrackAudioActivity.class);
        startActivity(intent);
    }

    public void openTributeAudio(View view) {
        Intent intent = new Intent(AudiosActivity.this, TributeAudioActivity.class);
        startActivity(intent);
    }
}

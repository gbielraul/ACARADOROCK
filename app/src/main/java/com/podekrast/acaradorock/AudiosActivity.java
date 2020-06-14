package com.podekrast.acaradorock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.podekrast.acaradorock.audios.FeitoNoBrasilAudioActivity;
import com.podekrast.acaradorock.audios.EspecialAudioActivity;
import com.podekrast.acaradorock.audios.AHistoriaAudioActivity;
import com.podekrast.acaradorock.audios.MadeInBrazilAudioActivity;
import com.podekrast.acaradorock.audios.BlackSabadleAudioActivity;
import com.podekrast.acaradorock.audios.Temporada1AudioActivity;
import com.podekrast.acaradorock.audios.VoltaAoMundoAudioActivity;

public class AudiosActivity extends AppCompatActivity {

    private Button mBtnBlackSabadle, mBtnMadeInBrazil, mBtnFeitoNoBrasil, mBtnEspecial, mBtnVoltaAoMundo, mBtnAHistoria, mBtnTemporada1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);

        //Recupera as Views do XML
        ImageView mBtnReturn = findViewById(R.id.btn_return_audios);
        mBtnBlackSabadle = findViewById(R.id.btn_black_sabadle);
        mBtnMadeInBrazil = findViewById(R.id.btn_made_in_brazil);
        mBtnFeitoNoBrasil = findViewById(R.id.btn_feito_no_brasil);
        mBtnEspecial = findViewById(R.id.btn_especial);
        mBtnVoltaAoMundo = findViewById(R.id.btn_volta_ao_mundo);
        mBtnAHistoria = findViewById(R.id.btn_a_historia);
        mBtnTemporada1 = findViewById(R.id.btn_temporada_1);

        //Chama o método que muda o fundo dos botões
        setBackground();
        //Chama o método que adiciona os listeners nos botões
        setButtonListener();

        //Adiciona o evento de clique que retorna para tela anterior
        mBtnReturn.setOnClickListener(audiosReturn);
    }

    //Método que muda o fundo dos botões
    private void setBackground() {
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

    //Método que adiciona os listeners nos botões
    private void setButtonListener() {
        mBtnBlackSabadle.setOnClickListener(openBlackSabadle);
        mBtnMadeInBrazil.setOnClickListener(openMadeInBrazil);
        mBtnFeitoNoBrasil.setOnClickListener(openFeitoNoBrasil);
        mBtnEspecial.setOnClickListener(openEspecial);
        mBtnVoltaAoMundo.setOnClickListener(openVoltaAoMundo);
        mBtnAHistoria.setOnClickListener(openAHistoria);
        mBtnTemporada1.setOnClickListener(openTemporada1);
    }

    //Método que retorna para tela principal
    private View.OnClickListener audiosReturn = v -> finish();

    //Métodos que chamam a Activity selecionada
    private View.OnClickListener openBlackSabadle = v ->
            startActivity(new Intent(AudiosActivity.this, BlackSabadleAudioActivity.class));

    private View.OnClickListener openMadeInBrazil = v ->
            startActivity(new Intent(AudiosActivity.this, MadeInBrazilAudioActivity.class));

    private View.OnClickListener openFeitoNoBrasil = v ->
            startActivity(new Intent(AudiosActivity.this, FeitoNoBrasilAudioActivity.class));

    private View.OnClickListener openEspecial = v ->
            startActivity(new Intent(AudiosActivity.this, EspecialAudioActivity.class));

    private View.OnClickListener openVoltaAoMundo = v ->
            startActivity(new Intent(AudiosActivity.this, VoltaAoMundoAudioActivity.class));

    private View.OnClickListener openAHistoria = v ->
            startActivity(new Intent(AudiosActivity.this, AHistoriaAudioActivity.class));

    private View.OnClickListener openTemporada1 = v ->
            startActivity(new Intent(AudiosActivity.this, Temporada1AudioActivity.class));
}

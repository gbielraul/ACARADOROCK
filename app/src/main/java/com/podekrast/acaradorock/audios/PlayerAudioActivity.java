package com.podekrast.acaradorock.audios;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.podekrast.acaradorock.R;
import com.podekrast.acaradorock.model.Audio;

import java.io.IOException;

public class PlayerAudioActivity extends AppCompatActivity {

    private TextView mTxtCurrentTime;
    private TextView mTxtDuration;
    private MediaPlayer mMediaPlayer;
    private Button mBtnPlay;
    private RelativeLayout mProgressBar;
    private String mAudioUrl;
    private SeekBar mSeekBar;
    private Handler mHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_audio);

        //Recupera as Views do XML
        ImageView mBtnReturn = findViewById(R.id.btn_return_player_audio);
        TextView mTxtTitle = findViewById(R.id.txt_title_player_audio);
        mTxtCurrentTime = findViewById(R.id.txt_current_time);
        mTxtDuration = findViewById(R.id.txt_duration);
        mBtnPlay = findViewById(R.id.btn_play_audio);
        mSeekBar = findViewById(R.id.seek_bar_player_audio);
        mProgressBar = findViewById(R.id.progress_bar_player_audio);

        //Adiciona o evento de clique no botão que retorna para tela anterior
        mBtnReturn.setOnClickListener(playerAudioReturn);

        //Configura a SeekBar
        mSeekBar.setMax(100);

        //Recupera a instância o Handler
        mHandler = new Handler();

        //Recupera a instância do MediaPlayer
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //Recupera os dados do áudio
        Intent intent = getIntent();
        Audio audio = (Audio) intent.getSerializableExtra("selectedAudio");

        //Recupera o título do áudio
        String mAudioTitle = audio.getProgramTitle();
        //Recupera a url do áudio
        mAudioUrl = audio.getProgramUrl();

        //Muda o texto para o título do áudio
        mTxtTitle.setText(mAudioTitle);

        //Adiciona o evento de clique na SeekBar
        mSeekBar.setOnTouchListener((v, event) -> {
            SeekBar seekBar = (SeekBar) v;
            int playPosition = (mMediaPlayer.getDuration() / 100) * seekBar.getProgress();
            mMediaPlayer.seekTo(playPosition);
            mTxtCurrentTime.setText(convertMillisecond(mMediaPlayer.getCurrentPosition()));
            return false;
        });

        //Adiciona o evento de clique no botão
        mBtnPlay.setOnClickListener(playMusic);

        configureSeekBar();
    }

    private void configureSeekBar() {
        try {
            mMediaPlayer.setDataSource(mAudioUrl);
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(mp -> {
                //Ativa o botão
                mBtnPlay.setEnabled(true);
                //Torna a ProgressBar invisível
                mProgressBar.setVisibility(View.GONE);
                //Recupera a duração do áudio e coloca no texto
                mTxtDuration.setText(convertMillisecond(mMediaPlayer.getDuration()));
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PlayerAudioActivity.this, "Erro", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            //Chama o método que atualiza a SeekBar
            updateSeekBar();
            //Recupera o tempo atual e coloca no texto
            long currentTime = mMediaPlayer.getCurrentPosition();
            mTxtCurrentTime.setText(convertMillisecond(currentTime));
        }
    };

    private View.OnClickListener playMusic = v -> {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            mBtnPlay.setBackgroundResource(R.drawable.btn_pause);
            updateSeekBar();
        } else {
            mHandler.removeCallbacks(updater);
            mMediaPlayer.pause();
            mBtnPlay.setBackgroundResource(R.drawable.btn_play);
        }
    };

    //Método que atualiza a SeekBar
    private void updateSeekBar() {

        //Se o áudio estiver tocando atualiza a SeekBar
        if (mMediaPlayer.isPlaying()) {
            if (mTxtCurrentTime == mTxtDuration) {
                mMediaPlayer.stop();
            }
            mSeekBar.setProgress((int) (((float) mMediaPlayer.getCurrentPosition() / mMediaPlayer.getDuration()) * 100));
            mHandler.postDelayed(updater, 1000);
        }
    }

    //Método que configura a duração do áudio
    private String convertMillisecond(long milliseconds) {
        //A string da duração adaptada
        String timerString = "";
        String minutesString;
        String secondsString;

        //Configura o tempo
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        //Se a hora for maior que zero, adiciona na duração
        if (hours > 0) {
            timerString = hours + ":";
        }
        //Se os minutos for menos que dez, adiciona um zero atrás e adiciona na duração
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            //Senão, simplesmente adiciona na duração
            minutesString = "" + seconds;
        }
        //Se os segundos for menos que dez, adiciona um zero atrás e adiciona na duração
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            //Senão, simplesmente adiciona na duração
            secondsString = "" + seconds;
        }

        //Configura a duração
        timerString = timerString + minutesString + ":" + secondsString;

        //Retorna a duração
        return timerString;
    }

    //Método que retorna para a tela anterior
    private View.OnClickListener playerAudioReturn = v -> finish();

    @Override
    protected void onRestart() {
        super.onRestart();

        //Atualiza a seekBar
        updateSeekBar();
        //Verifica se está tocando
        if (mMediaPlayer.isPlaying()) {
            mBtnPlay.setBackgroundResource(R.drawable.btn_pause);
        } else {
            mBtnPlay.setBackgroundResource(R.drawable.btn_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Desativa o MediaPlayer
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}

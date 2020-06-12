package com.podekrast.acaradorock.audios;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.podekrast.acaradorock.R;
import com.podekrast.acaradorock.helper.ConfigFirebase;
import com.podekrast.acaradorock.model.Audio;

import java.io.IOException;

public class PlayerAudioActivity extends AppCompatActivity {

    private TextView mTxtTitle, mTxtCurrentTime, mTxtDuration;
    private FirebaseStorage mStorage;
    private DatabaseReference mReference;
    private String mAudioUrl, mAudioTitle;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private Button mBtnPlay;
    private RelativeLayout mProgressBar;
    private SeekBar mSeekBar;
    private Handler mHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_audio);

        //Recupera a instância do FirebaseStorage
        mStorage = FirebaseStorage.getInstance();

        //Recupera o texto que vai aparecer o título da música
        mTxtTitle = findViewById(R.id.txt_title_player_audio);
        //Recupera o texto que vai aparecer a tempo atual do áudio
        mTxtCurrentTime = findViewById(R.id.txt_current_time);
        //Recupera o texto que vai aparecer a duração total do áudio
        mTxtDuration = findViewById(R.id.txt_duration);
        //Recupera o botão de que vai executar o áudio
        mBtnPlay = findViewById(R.id.btn_play_audio);
        //Recupera a referência do FirebaseDatabase
        mReference = ConfigFirebase.getDatabase();

        //Recupera a SeekBar
        mSeekBar = findViewById(R.id.seek_bar_player_audio);

        //Configura a SeekBar
        mSeekBar.setMax(100);

        //Recupera a instância o Handler
        mHandler = new Handler();

        //Recupera a instância do MediaPlayer
        mMediaPlayer = new MediaPlayer();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //Recupera a ProgressBar
        mProgressBar = findViewById(R.id.progress_bar_player_audio);

        //Recupera os dados do áudio
        Intent intent = getIntent();
        Audio audio = (Audio) intent.getSerializableExtra("selectedAudio");

        //Recupera o título do áudio
        mAudioTitle = audio.getName();
        //Recupera a url do áudio
        mAudioUrl = audio.getUrl();
        //Recupera a duração do áudio

        //Muda o texto para o título do áudio
        mTxtTitle.setText(mAudioTitle);

        //Adiciona o evento de clique na SeekBar
        mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mMediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mMediaPlayer.seekTo(playPosition);
                mTxtCurrentTime.setText(convertMillisecond(mMediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        //Adiciona o evento de clique no botão
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    mBtnPlay.setBackgroundResource(R.drawable.btn_pause);
                    updateSeekBar();
                } else {
                    mHandler.removeCallbacks(updater);
                    mMediaPlayer.pause();
                    mBtnPlay.setBackgroundResource(R.drawable.btn_play);
                }
            }
        });

        try {
            mMediaPlayer.setDataSource(mAudioUrl);
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //Ativa o botão
                    mBtnPlay.setEnabled(true);
                    //Torna a ProgressBar invisível
                    mProgressBar.setVisibility(View.GONE);
                    //Recupera a duração do áudio e coloca no texto
                    mTxtDuration.setText(convertMillisecond(mMediaPlayer.getDuration()));
                }
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
    public void playerAudioReturn(View view) {
        finish();
    }

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

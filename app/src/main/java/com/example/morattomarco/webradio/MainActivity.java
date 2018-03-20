package com.example.morattomarco.webradio;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    ImageButton b_play,brussia;
    SeekBar volumeSeekbar=null;
    AudioManager audioManager=null;
    TextView buffer;
    String message;
    MediaPlayer mediaPlayer;
    Spinner spinner;
    ImageView log;
    boolean prepared=false,started=false,part=false;
    ArrayList<Radio> streaming=new ArrayList<Radio>();
    //String stream="http://stream.radiobase.net/stream/stream.m3u";
    //String stream="http://stream.radiobase.net:8000/live01.mp3";
    //String stream="http://stream.radioreklama.bg:80/radio1rock128";


    //String streeam=res.getString(R.string.select);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Resources res = getResources();
        String streeam=res.getString(R.string.select);

        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_MESSAGE);
        brussia=(ImageButton) findViewById(R.id.brussia);

        if (message.equals("Stalin") || message.equals("stalin")) {
            setContentView(R.layout.russiap);

            mediaPlayer= MediaPlayer.create(this, R.raw.urrs);

        } else {

            setContentView(R.layout.activity_main);
            Log.i("INFO", "CREA ATTIVITà");
            streaming.add(new Radio("http://stream.radiobase.net:8000/live01.mp3", "Radio Base"));
            streaming.add(new Radio("http://stream.radioreklama.bg:80/radio1rock128", "Radio Reklama"));
            streaming.add(new Radio("http://icecast.unitedradio.it/Virgin.mp3", "Virgin Radio"));
            streaming.add(new Radio("http://shoutcast.rtl.it:3010/", "Radio RTL"));
            streaming.add(new Radio("http://radiodeejay-lh.akamaihd.net/i/RadioDeejay_Live_1@189857/master.m3u8", "Radio Deejay"));
            streaming.add(new Radio("http://radiocapital-lh.akamaihd.net/i/RadioCapital_Live_1@196312/master.m3u8", "Radio Capital"));


            Log.i("INFO", "AGGIUNGE STREAMING ALL'ARRAY");


            buffer = (TextView) findViewById(R.id.Edescription);
            Log.i("INFO", "RICEVE INTENT E MOSTRA MESSAGGIO");


            spinner = (Spinner) findViewById(R.id.S_stream);


            b_play = (ImageButton) findViewById(R.id.Bplay);

            buffer.setEnabled(true);

            b_play.setEnabled(false);
            b_play.setImageResource(R.drawable.ic_loading);


            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.stream_list, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


            if (started) {
                b_play.setImageResource(R.drawable.ic_pause);
            } else {

                b_play.setImageResource(R.drawable.ic_play);
            }



            if (!prepared) {
                b_play.setEnabled(false);
            } else {
                b_play.setEnabled(true);
            }


            spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                    Log.i("INFO", "ENTRA ONITEMSELECTED");

                    String text = "";
                    try {
                        text = adapterView.getSelectedItem().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                /*
                0 - radiobase
                1 - radioreklama
                2 - virginradio
                3 - rtl
                4 - deejay
                5 - capital
                 */
                    Resources res = getResources();
                    String streeam=res.getString(R.string.select);
                    log = (ImageView) findViewById(R.id.imageView3);
                    String radio = null;
                    for (int s = 0; s <= streaming.size() - 1; s++) {
                        if (text.equals(streaming.get(s).getNome())) {
                            streeam = streaming.get(s).getStream();
                            radio = streaming.get(s).getNome();
                            switch (s) {
                                case 0: {
                                    log.setImageResource(R.mipmap.lo_radiobase_round);
                                }
                                break;
                                case 1: {
                                    log.setImageResource(R.mipmap.lo_radioreklama_round);
                                }
                                break;
                                case 2: {
                                    log.setImageResource(R.mipmap.lo_virginradio_round);
                                }
                                break;
                                case 3: {
                                    log.setImageResource(R.mipmap.lo_rtl_round);
                                }
                                break;
                                case 4: {
                                    log.setImageResource(R.mipmap.lo_deejay_round);
                                }
                                break;
                                case 5: {
                                    log.setImageResource(R.mipmap.lo_capital_round);
                                }
                                break;
                            }

                        }
                    }

                    buffer.setText(streeam);


                    if (streeam.equals(res.getString(R.string.select))) {
                        buffer.setText(streeam);
                    } else {
                        b_play.setImageResource(R.drawable.ic_loading);
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        if (started) {
                            mediaPlayer.pause();
                            mediaPlayer.stop();
                            if (mediaPlayer.isPlaying()) {

                                mediaPlayer.reset();
                                mediaPlayer.release();
                            }
                        } else {
                        }
                        new PlayerTask().execute(streeam);
                        Log.i("INFO", "PARTE LO STREAM");
                        started = false;

                        String be1 = res.getString(R.string.b1);
                        String be2 = res.getString(R.string.b2);
                        buffer.setText(be1 + " " + message + " " + be2 + " " + radio);


                        //started=false;

                    /*mediaPlayer.pause();
                    b_play.setImageResource(R.drawable.ic_play);*/
                    }
                }

                @Override
                public void onNothingSelected(AdapterView adapterView) {

                }
            });


            b_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (started) {
                        started = false;
                        mediaPlayer.pause();
                        buffer.setEnabled(false);
                        b_play.setImageResource(R.drawable.ic_play);
                    } else {
                        started = true;


                        mediaPlayer.start();
                        buffer.setEnabled(false);
                        b_play.setImageResource(R.drawable.ic_pause);
                    }

                }
            });
            initControls();
        }

    }

    private void initControls()
    {
        try
        {
            volumeSeekbar = (SeekBar)findViewById(R.id.seekBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared=true;
            } catch (IOException e) {
                e.printStackTrace();
                prepared=false;
                Toast.makeText(MainActivity.this,"Please turn on WiFi and try again", Toast.LENGTH_LONG).show();
            }
            return prepared;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mediaPlayer.start();
            b_play.setEnabled(true);
            buffer.setEnabled(false);
            b_play.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
        }
    }


    public void control(View view) {
        if (started) {
            started = false;
            mediaPlayer.pause();
        } else {
            started = true;
            mediaPlayer.start();
        }
    }
}


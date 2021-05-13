package com.example.music.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music.R;
import com.example.music.adapter.ViewPagerPlayAdapter;
import com.example.music.fragment.FragmentDiaNhac;
import com.example.music.fragment.FragmentPlayDanhSachFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textViewTimeSong, textViewTotalTime;
    SeekBar seekBar;
    ImageButton imageButtonPlay, imageButtonRepeat, imageButtonNext, imageButtonPre, imageButtonRandom, imageButtonRepeatBg, imageButtonRandomBg;
    ViewPager viewPager;
    public static ArrayList<String>  arrayList = new ArrayList<>();
    public static ArrayList<String> arrayLink = new ArrayList<>();
    public static ViewPagerPlayAdapter adapter;
    FragmentDiaNhac fragmentDiaNhac;
    FragmentPlayDanhSachFile fragmentPlayDanhSachFile;
    MediaPlayer mediaPlayer;

    int position = 0;
    boolean repeat = false;
    boolean checkradom = false;
    boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //tránh tình trạng phát sinh khi sử dụng mạng
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dataIntent();
        initview();
        initEvent();
    }

    private void initEvent() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter.getItem(1) != null) {
                    if (arrayList.size() > 0) {
                        fragmentDiaNhac.play();
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 300);
                    }
                }
            }
        }, 500);

        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imageButtonPlay.setImageResource(R.drawable.ic_play);
                    fragmentDiaNhac.objectAnimatorPause();
                } else {
                    mediaPlayer.start();
                    imageButtonPlay.setImageResource(R.drawable.ic_signs);
                    fragmentDiaNhac.objectAnimatorStart();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                arrayLink.clear();
                arrayList.clear();
            }
        });

        imageButtonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false) {
                    if (checkradom == true) {
                        checkradom = false;
                        imageButtonRandomBg.setImageResource(android.R.color.transparent);
                    }
                    imageButtonRepeatBg.setImageResource(R.drawable.ic_rec);
                    repeat = true;
                } else {
                    imageButtonRepeatBg.setImageResource(android.R.color.transparent);
                    repeat = false;
                }
            }
        });

        imageButtonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkradom == false) {
                    if (repeat == true) {
                        repeat = false;
                        imageButtonRepeatBg.setImageResource(android.R.color.transparent);
                    }
                    imageButtonRandomBg.setImageResource(R.drawable.ic_rec);
                    checkradom = true;
                } else {
                    imageButtonRandomBg.setImageResource(android.R.color.transparent);
                    checkradom = false;
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    if (position < arrayList.size()) {
                        imageButtonPlay.setImageResource(R.drawable.ic_signs);
                        position++;
                        if (repeat == true) {
                            if (position == 0) {
                                position = arrayList.size();
                            }
                            position -= 1;
                        }
                        if (checkradom == true) {
                            Random random = new Random();
                            int index = random.nextInt(arrayList.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (arrayList.size() - 1)) {
                            position = 0;
                        }
                        new Play().execute(arrayLink.get(position));
                        fragmentDiaNhac.play();
                        getSupportActionBar().setTitle(arrayList.get(position));

                    }
                }
                imageButtonNext.setClickable(false);
                imageButtonPre.setClickable(false);
                Handler handler1 = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageButtonNext.setClickable(true);
                        imageButtonPre.setClickable(true);
                    }
                }, 5000);
            }
        });

        imageButtonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    if (position < arrayList.size()) {
                        imageButtonPlay.setImageResource(R.drawable.ic_signs);
                        position--;
                        if (position < 0) {
                            position = arrayList.size() - 1;
                        }
                        if (repeat == true) {
                            position += 1;
                        }
                        if (checkradom == true) {
                            Random random = new Random();
                            int index = random.nextInt(arrayList.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        new Play().execute(arrayLink.get(position));
                        fragmentDiaNhac.play();
                        getSupportActionBar().setTitle(arrayList.get(position));

                    }
                }
                imageButtonNext.setClickable(false);
                imageButtonPre.setClickable(false);
                Handler handler1 = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageButtonNext.setClickable(true);
                        imageButtonPre.setClickable(true);
                    }
                }, 5000);
            }
        });
    }

    private void initview() {
        toolbar = findViewById(R.id.tb_play);
        textViewTimeSong = findViewById(R.id.tv_time_song);
        textViewTotalTime = findViewById(R.id.tv_total_time_song);
        seekBar = findViewById(R.id.sb_play);
        imageButtonPlay = findViewById(R.id.ib_play);
        imageButtonRepeat = findViewById(R.id.ib_repeat);
        imageButtonRepeatBg = findViewById(R.id.ib_repeatbg);
        imageButtonNext = findViewById(R.id.ib_next);
        imageButtonPre = findViewById(R.id.ib_review);
        imageButtonRandom = findViewById(R.id.ib_suffle);
        imageButtonRandomBg = findViewById(R.id.ib_sufflebg);
        viewPager = findViewById(R.id.vp_play);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                arrayList.clear();
                arrayLink.clear();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);

        fragmentDiaNhac = new FragmentDiaNhac();
        fragmentPlayDanhSachFile = new FragmentPlayDanhSachFile(this);
        adapter = new ViewPagerPlayAdapter(getSupportFragmentManager());
        adapter.addFragment(fragmentPlayDanhSachFile);
        adapter.addFragment(fragmentDiaNhac);
        viewPager.setAdapter(adapter);

        fragmentDiaNhac = (FragmentDiaNhac) adapter.getItem(1);
        if (arrayList.size() > 0) {
            getSupportActionBar().setTitle(arrayList.get(position));
            new Play().execute(arrayLink.get(position));
            imageButtonPlay.setImageResource(R.drawable.ic_signs);
        }
    }

    private void dataIntent() {
        arrayList = new ArrayList<>();
        arrayLink = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver.query(songuri,null,null,null,null);
        if (songcursor!=null && songcursor.moveToFirst()){
            int songtitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songdata = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String title = songcursor.getString(songtitle);
                String data = songcursor.getString(songdata);
                arrayLink.add(data);
                arrayList.add(title);
            } while (songcursor.moveToNext());
        }
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("position")) {
                position = intent.getIntExtra("position", 0);
            }
        }
    }

    class Play extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.setDataSource(s);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            timeSong();
        }
    }

    private void timeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        textViewTotalTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        updateTime();
    }

    private void updateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    textViewTimeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    //nghe hết bài hát
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 300);

        final Handler handlerTransferSong = new Handler();
        handlerTransferSong.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true) {
                    if (position < arrayList.size()) {
                        imageButtonPlay.setImageResource(R.drawable.ic_signs);
                        position++;
                        if (repeat == true) {
                            if (position == 0) {
                                position = arrayList.size();
                            }
                            position -= 1;
                        }
                        if (checkradom == true) {
                            Random random = new Random();
                            int index = random.nextInt(arrayList.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (arrayList.size() - 1)) {
                            position = 0;
                        }
                        new Play().execute(arrayLink.get(position));
                        fragmentDiaNhac.play();
                        getSupportActionBar().setTitle(arrayList.get(position));

                    }
                    imageButtonNext.setClickable(false);
                    imageButtonPre.setClickable(false);
                    Handler handler1 = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageButtonNext.setClickable(true);
                            imageButtonPre.setClickable(true);
                        }
                    }, 5000);

                    next = false;
                    handlerTransferSong.removeCallbacks(this);
                } else {
                    handlerTransferSong.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
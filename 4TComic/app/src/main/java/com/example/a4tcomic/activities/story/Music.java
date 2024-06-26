package com.example.a4tcomic.activities.story;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.a4tcomic.R;

public class Music extends Service {
    // Khai báo đối tượng mà service quản lý
    MediaPlayer mymusic;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    // hàm dùng khởi tạo đối tượng mà service quản lý
    @Override
    public void onCreate() {
        super.onCreate();
        mymusic = MediaPlayer.create(Music.this, R.raw.nhac);
        // setting lặp đi lại lại
        mymusic.setLooping(true);
    }
    // Hàm dùng để khởi động Service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mymusic == null) {
            mymusic = MediaPlayer.create(Music.this, R.raw.nhac);
            mymusic.setLooping(true);
        }

        if (mymusic.isPlaying()) {
            mymusic.pause();
        } else {
            mymusic.start();
        }
        return START_STICKY;
    }
    // Hàm để dừng đối tượng mà Service quản lý

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mymusic != null) {
            mymusic.stop();
            mymusic.release();
            mymusic = null;
        }
    }
}

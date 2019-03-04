package com.example.mymusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<com.example.mymusic.songs> songs = new ArrayList<>();
    RecyclerView rv;
    SeekBar sb;
    SongAdapter songadapter;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.recycle);
        sb = (SeekBar) findViewById(R.id.seekbar);

      //  songs s = new songs("sadda haq", "R.k", "https://www.youtube.com/watch?v=p9DQINKZxWE");
      //  songs.add(s);
        songadapter = new SongAdapter(MainActivity.this, songs);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        DividerItemDecoration dm = new DividerItemDecoration(rv.getContext(), lm.getOrientation());
        rv.setLayoutManager(lm);
        rv.addItemDecoration(dm);
        rv.setAdapter(songadapter);
        songadapter.setOnitemclicklistner(new SongAdapter.onItemClickListner() {
            @Override
            public void onItemClick(final Button b, View v, com.example.mymusic.songs obj, int position) {
                mp = new MediaPlayer();
                try {
                    if (b.getText().toString().equals("stop")) {
                        b.setText("play");
                        mp.stop();
                        mp.reset();
                        mp.release();
                        mp = null;
                    } else {
                        mp.setDataSource(obj.getSongurl());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                                b.setText("stop");
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkpermission();
    }

    public void checkpermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x7b);
                return;
            }

        } else {
            loadsongs();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadsongs();
                } else {
                    Toast.makeText(this, "error loading", Toast.LENGTH_SHORT).show();
                    checkpermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void loadsongs(){
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection= MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor!= null){
            if (cursor.moveToFirst()) {
                do {
                String name= (String) cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String artist= (String) cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String url= (String) cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    com.example.mymusic.songs s=new songs(name,artist,url);
                    songs.add(s);
            }while (cursor.moveToNext());
            } cursor.close();
            songadapter=new SongAdapter(this,songs);
        }
    }
}

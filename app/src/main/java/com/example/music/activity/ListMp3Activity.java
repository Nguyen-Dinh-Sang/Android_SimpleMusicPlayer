package com.example.music.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.music.R;

import java.util.ArrayList;

public class ListMp3Activity extends AppCompatActivity {
    private final int MY=1;
    ArrayList<String> arrayList,arraylink;
    ListView listView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mp3);
        if (ContextCompat.checkSelfPermission(ListMp3Activity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ListMp3Activity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(ListMp3Activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY);
            }
            else {
                ActivityCompat.requestPermissions(ListMp3Activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY);
            }
        }
        else {
            doStuff();
        }
    }
    private void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver.query(songuri,null,null,null,null);
        if (songcursor!=null && songcursor.moveToFirst()){
            int songtitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songdata = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songArtist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                String title = songcursor.getString(songtitle);
                String data = songcursor.getString(songdata);
                arraylink.add(data);
                arrayList.add(title);
            } while (songcursor.moveToNext());
        }
    }
    private void doStuff() {
        listView = findViewById(R.id.listmusic);
        arrayList = new ArrayList<>();
        arraylink = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ListMp3Activity.this, Processing.class);
//                intent.putExtra("ten",arrayList.get(position));
//                intent.putExtra("link",arraylink.get(position));
//                setResult(200,intent);
//                finish();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY:{
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(ListMp3Activity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ListMp3Activity.this,"ERMISSION_GRANTED",Toast.LENGTH_LONG).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(ListMp3Activity.this,"NO ERMISSION_GRANTED",Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }
}
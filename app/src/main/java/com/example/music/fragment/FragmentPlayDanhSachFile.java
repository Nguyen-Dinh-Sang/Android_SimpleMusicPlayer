package com.example.music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.R;
import com.example.music.activity.ListMp3Activity;
import com.example.music.activity.PlayActivity;

import java.util.ArrayList;

public class FragmentPlayDanhSachFile extends Fragment {
    View view;
    ListView listView;
    ArrayAdapter<String> adapter;
    Context context;

    public FragmentPlayDanhSachFile( Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danhsach_file, container, false);
        listView = view.findViewById(R.id.listmusic2);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, PlayActivity.arrayList);
        listView.setAdapter(adapter);

        return view;
    }
}
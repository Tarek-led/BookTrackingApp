package com.example.bookstoreapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.provider.BookItem;
import com.example.bookstoreapp.provider.BookViewModel;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//    MyRecyclerViewAdapter adapter;
//
//    ArrayList<BookItem> data;

    private BookViewModel mBookViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        recyclerView = findViewById(R.id.recyclerView2);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter = new MyRecyclerViewAdapter();
//        recyclerView.setAdapter(adapter);
//
//        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
//        mBookViewModel.getAllItems().observe(this, newData -> {
//            adapter.setData((ArrayList<BookItem>) newData);
//            adapter.notifyDataSetChanged();
//        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frame2,new RecyclerViewFragment()).commit();
    }
}

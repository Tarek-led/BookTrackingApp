package com.example.bookstoreapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.provider.BookItem;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

    List<BookItem> data = new ArrayList<>();

    public MyRecyclerViewAdapter(){
    }


    public void setData(ArrayList<BookItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bookID.setText("Book ID: " + data.get(position).getBookID());
        holder.bookTitle.setText("Title: " + data.get(position).getBookTitle());
        holder.bookISBN.setText("ISBN: " +data.get(position).getBookISBN());
        holder.bookAuthor.setText("Author: " +data.get(position).getBookAuthor());
        holder.bookDesc.setText("Description: " +data.get(position).getBookDesc());
        holder.bookPrice.setText("Price: " +data.get(position).getBookPrice());
        holder.bookItemId.setText("Item ID:  " + (position+1));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bookID,bookTitle,bookISBN,bookAuthor,bookDesc,bookPrice, bookItemId;

        public ViewHolder(View itemView){
            super(itemView);
            bookID = itemView.findViewById(R.id.cardBookID);
            bookTitle = itemView.findViewById(R.id.cardBookTitle);
            bookISBN = itemView.findViewById(R.id.cardBookISBN);
            bookAuthor = itemView.findViewById(R.id.cardBookAuthor);
            bookDesc = itemView.findViewById(R.id.cardBookDesc);
            bookPrice = itemView.findViewById(R.id.cardBookPrice);
            bookItemId = itemView.findViewById(R.id.bookItemId);
        }
    }
}
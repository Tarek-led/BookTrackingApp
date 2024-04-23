package com.example.bookstoreapp.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class BookItem {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int bookItemID;

    @ColumnInfo(name="bookID")
    private String bookID;

    @ColumnInfo(name = "title")
    private String bookTitle;

    @ColumnInfo(name = "isbn")
    private String bookISBN;

    @ColumnInfo(name = "author")
    private String bookAuthor;

    @ColumnInfo(name = "description")
    private String bookDesc;

    @ColumnInfo(name = "price")
    private double bookPrice;

    public BookItem(String bookID,String bookTitle, String bookISBN, String bookAuthor, String bookDesc, double bookPrice) {
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.bookISBN = bookISBN;
        this.bookAuthor = bookAuthor;
        this.bookDesc = bookDesc;
        this.bookPrice = bookPrice;
    }

    public int getBookItemID() {
        return bookItemID;
    }

    public void setBookItemID(int bookItemID) {
        this.bookItemID = bookItemID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }
}

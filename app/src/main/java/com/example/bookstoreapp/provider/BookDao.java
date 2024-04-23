package com.example.bookstoreapp.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM books")
    LiveData<List<BookItem>> getAllBooks();

    @Query("select * from books where bookID=:bookTitle")
    List<BookItem> getBook(String bookTitle);

    @Insert
    void addBook(BookItem item);

    @Query("delete from books where bookID= :bookTitle")
    void deleteBook(String bookTitle);

    @Query("delete FROM books")
    void deleteAllBooks();

    @Query("SELECT COUNT(*) FROM books")
    LiveData<Integer> getBookCount();

    @Query("SELECT COUNT(*) FROM books WHERE bookItemID = :bookItemID")
    int countBookID(int bookItemID);

    @Query("delete from books where bookItemID = (SELECT MAX(bookItemID) FROM books)")
    void deleteLastBookAdded();

    @Query("delete from books where price >50")
    void deleteIfPrice();
}
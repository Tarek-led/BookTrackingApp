package com.example.bookstoreapp.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao mItemDao;
    private LiveData<List<BookItem>> mAllItems;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mItemDao = db.bookDao();
        mAllItems = mItemDao.getAllBooks();
    }

    LiveData<List<BookItem>> getAllItems() {
        return mAllItems;
    }

    void insert(BookItem item) {
        BookDatabase.databaseWriteExecutor.execute(() -> mItemDao.addBook(item));
    }

    void deleteAll() {
        BookDatabase.databaseWriteExecutor.execute(() -> {
            mItemDao.deleteAllBooks();
        });
    }

    void deleteLastBookAdded(){
        BookDatabase.databaseWriteExecutor.execute(() -> mItemDao.deleteLastBookAdded());
    }

    public LiveData<Integer> getBookCount() {
        return mItemDao.getBookCount();
    }

    void deleteIfPrice(){
        BookDatabase.databaseWriteExecutor.execute(() -> mItemDao.deleteIfPrice());
    }
}

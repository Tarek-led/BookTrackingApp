package com.example.bookstoreapp.provider;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepository mRepository;
    private LiveData<List<BookItem>> mAllItems;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllItems = mRepository.getAllItems();
    }

    public LiveData<List<BookItem>> getAllItems() {
        return mAllItems;
    }

    public void insert(BookItem item) {
        mRepository.insert(item);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void deleteLast(){
        mRepository.deleteLastBookAdded();
    }

    public LiveData<Integer> totalCount(){
        return mRepository.getBookCount();
    }

    public void deletePrice(){
        mRepository.deleteIfPrice();
    }
}

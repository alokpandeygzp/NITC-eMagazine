package com.example.nitcemag.ui.myArticles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyArticlesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MyArticlesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyArticles fragment..");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
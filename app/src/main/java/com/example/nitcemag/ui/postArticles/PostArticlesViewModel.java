package com.example.nitcemag.ui.postArticles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PostArticlesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PostArticlesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Post Articles fragment..");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
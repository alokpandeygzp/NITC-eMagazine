package com.example.nitcemag.ui.reviewedArticles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReviewedArticlesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ReviewedArticlesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Reviewed Articles fragment..");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
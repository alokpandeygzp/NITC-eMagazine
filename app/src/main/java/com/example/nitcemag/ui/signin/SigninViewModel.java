package com.example.nitcemag.ui.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SigninViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mText;

    public SigninViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Signin fragment..");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
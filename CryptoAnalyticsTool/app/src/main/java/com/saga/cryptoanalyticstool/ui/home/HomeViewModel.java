package com.saga.cryptoanalyticstool.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class HomeViewModel extends ViewModel {

    private FirebaseAuth mAuth;

    private final MutableLiveData<String> username;
    private final MutableLiveData<String> email;

    public HomeViewModel() {
        mAuth = FirebaseAuth.getInstance();
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        username.setValue(mAuth.getCurrentUser().getEmail().substring(0, mAuth.getCurrentUser().getEmail().indexOf('@')));
        email.setValue(mAuth.getCurrentUser().getEmail());
    }

    public LiveData<String> getUsername() {
        return username;
    }
    public LiveData<String> getEmail() { return email; }
}
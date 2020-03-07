package com.example.bookapp.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookapp.models.User;

public class ViewModelUser extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}

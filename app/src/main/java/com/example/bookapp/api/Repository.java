package com.example.bookapp.api;

import com.android.volley.RequestQueue;

class Repository {
    RequestQueue requestQueue;
    String currentUserID;

    Repository(RequestQueue requestQueue, String currentUserID) {
        this.requestQueue = requestQueue;
        this.currentUserID = currentUserID;
    }
}

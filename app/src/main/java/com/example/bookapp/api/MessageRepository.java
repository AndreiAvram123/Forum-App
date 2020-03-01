package com.example.bookapp.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bookapp.models.ApiConstants;
import com.example.bookapp.models.Message;
import com.example.bookapp.utilities.MessageDataConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.bookapp.models.ApiConstants.URL_SEND_MESSAGE;
import static com.example.bookapp.models.ApiConstants.URL_UPLOAD_COMMENT;

public class MessageRepository {

    private static MessageRepository instance;
    private RequestQueue requestQueue;
    private MessageRepositoryCallback callback;

    public static MessageRepository getInstance(RequestQueue requestQueue) {
        if (instance == null) {
            instance = new MessageRepository(requestQueue);
        }
        return instance;
    }

    private MessageRepository(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void setCallback(MessageRepositoryCallback messageRepositoryCallback) {
        this.callback = messageRepositoryCallback;
    }

    public void pushRequestFetchMessagesWithUser(String currentUserID, String user2ID, int offset) {
        String formattedURLSavedPosts = String.format(ApiConstants.URL_LAST_MESSAGES, currentUserID, user2ID, offset);
        //push request
        StringRequest request = new StringRequest(Request.Method.GET, formattedURLSavedPosts, (response) ->
        {
            ArrayList<Message> lastMessages = MessageDataConverter.convertJsonArrayToMessages(response);
            if (callback != null) {
                callback.onLastMessagesReady(lastMessages);
            }
        },
                Throwable::printStackTrace);
        requestQueue.add(request);

    }

    public void sendMessage(Message message,String user2ID) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("messageContent", message.getMessageContent());
            postBody.put("currentUserId", message.getSenderID());
            postBody.put("receiverId",user2ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Debug",postBody.toString());


        JsonObjectRequest sendMessageRequest = new JsonObjectRequest(Request.Method.POST, URL_SEND_MESSAGE, postBody, response -> Log.d("Debug", response.toString()), error -> {

        });

        requestQueue.add(sendMessageRequest);
    }

    public interface MessageRepositoryCallback {
        void onLastMessagesReady(@NonNull ArrayList<Message> lastMessages);
    }
}

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

public class MessageRepository {

    private static MessageRepository instance;
    private RequestQueue requestQueue;
    private MessageRepositoryCallback callback;
    private int messageOffset = 0;
    private String lastMessageID;
    private static final String TAG = Message.class.getSimpleName();

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
        String formattedURLSavedPosts = String.format(ApiConstants.URL_LAST_MESSAGES, currentUserID, user2ID, this.messageOffset);
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

    public void sendMessage(String messageContent, String user2ID, String currentUserID) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("messageContent", messageContent);
            postBody.put("currentUserId", currentUserID);
            postBody.put("receiverId",user2ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Pushing send message request with" + postBody.toString());

        JsonObjectRequest sendMessageRequest = new JsonObjectRequest(Request.Method.POST, URL_SEND_MESSAGE, postBody,
                response -> {
                    //callback when the message has reached the server
                    try {
                        if (response.getInt("responseCode") == ApiConstants.REQUEST_COMPLETED_NO_ERROR) {
                            Message.Builder builder = new Message.Builder();
                            builder.setMessageContent(postBody.getString("messageContent"));
                            builder.setMessageID(response.getString("lastMessageID"));
                            builder.setSenderID(currentUserID);
                            builder.setMessageDate(response.getLong("lastMessageDate"));
                            callback.onNewMessageReady(builder.createMessage());
                            Log.d("Debug", "Message reached the server " + builder.createMessage());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {

                });

        requestQueue.add(sendMessageRequest);
    }

    public interface MessageRepositoryCallback {
        void onLastMessagesReady(@NonNull ArrayList<Message> lastMessages);

        void onNewMessagesReady(@NonNull ArrayList<Message> messages);

        void onNewMessageReady(@NonNull Message message);
    }
}

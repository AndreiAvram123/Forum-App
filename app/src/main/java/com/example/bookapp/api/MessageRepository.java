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
import java.util.Timer;
import java.util.TimerTask;

import static com.example.bookapp.models.ApiConstants.URL_SEND_MESSAGE;

public class MessageRepository extends Repository {

    private static MessageRepository instance;

    private MessageRepositoryCallback callback;
    private String lastMessageID;
    private static final String TAG = Message.class.getSimpleName();
    private Timer timer;
    private TimerTask pushRequestNewMessages;
    private boolean shouldOtherFunctionsWait = false;
    private boolean lastMessagesFetched = false;

    public static MessageRepository getInstance(RequestQueue requestQueue, String currentUserID) {
        if (instance == null) {
            instance = new MessageRepository(requestQueue, currentUserID);
        }
        return instance;
    }

    private MessageRepository(RequestQueue requestQueue, String currentUserID) {
        super(requestQueue, currentUserID);
    }


    public void setCallback(MessageRepositoryCallback messageRepositoryCallback) {
        this.callback = messageRepositoryCallback;
    }

    private void initializeAsyncFetchFunctions(String user2ID) {
        timer = new Timer();
        pushRequestNewMessages = new TimerTask() {
            @Override
            public void run() {
                if (!shouldOtherFunctionsWait) {
                    pushRequestFetchNewMessage(currentUserID, user2ID);
                }
            }
        };

        int newMessageCheckInterval = 1500;
        timer.scheduleAtFixedRate(pushRequestNewMessages, 0, newMessageCheckInterval);
    }

    /**********************************************Push request functions ********************************/

    public void pushRequestFetchOldMessages(String user2ID, int offset) {
        String formattedURlOldMessages = String.format(ApiConstants.URL_OLD_MESSAGES, this.currentUserID, user2ID, offset);
        //push request
        StringRequest request = new StringRequest(Request.Method.GET, formattedURlOldMessages, (response) ->
        {
            ArrayList<Message> lastMessages = MessageDataConverter.convertJsonArrayToMessages(response);
            if (!lastMessages.isEmpty()) {
                this.lastMessageID = lastMessages.get(lastMessages.size() - 1).getMessageID();
                Log.d("Debug", lastMessageID);
                if (timer == null) {
                    initializeAsyncFetchFunctions(user2ID);
                }
                if (callback != null) {
                    callback.onLastMessagesFetched(lastMessages);
                }
            }
        },
                Throwable::printStackTrace);
        requestQueue.add(request);

    }

    private void pushRequestFetchNewMessage(String currentUserID, String user2ID) {
        String formattedURLNewMessages = String.format(ApiConstants.URL_FETCH_NEW_MESSAGE, currentUserID, user2ID, lastMessageID);
        //push request
        StringRequest request = new StringRequest(Request.Method.GET, formattedURLNewMessages, (response) ->
        {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("responseCode") == ApiConstants.REQUEST_COMPLETED_NO_ERROR) {
                    ArrayList<Message> newMessages = MessageDataConverter.convertJsonArrayToMessages(jsonObject.getJSONArray("results"));
                    lastMessageID = newMessages.get(newMessages.size() - 1).getMessageID();
                    if (!newMessages.isEmpty() && callback != null) {
                        callback.onNewMessagesReady(newMessages);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                Throwable::printStackTrace);
        requestQueue.add(request);
    }

    public void sendMessage(String messageContent, String user2ID, String currentUserID) {
        shouldOtherFunctionsWait = true;
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("messageContent", messageContent);
            postBody.put("currentUserId", currentUserID);
            postBody.put("receiverId",user2ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest sendMessageRequest = new JsonObjectRequest(Request.Method.POST, URL_SEND_MESSAGE, postBody,
                response -> {
                    shouldOtherFunctionsWait = false;
                    //callback when the message has reached the server
                    try {
                        if (response.getInt("responseCode") == ApiConstants.REQUEST_COMPLETED_NO_ERROR) {
                            Message.Builder builder = new Message.Builder();
                            builder.setMessageContent(postBody.getString("messageContent"));
                            builder.setMessageID(response.getString("lastMessageID"));
                            builder.setSenderID(currentUserID);
                            builder.setMessageDate(response.getLong("lastMessageDate"));
                            Message message = builder.createMessage();
                            callback.onSendMessageReady(message);
                            lastMessageID = message.getMessageID();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    shouldOtherFunctionsWait = false;
                });

        requestQueue.add(sendMessageRequest);
    }

    public void shutdownAsyncTasks() {
        pushRequestNewMessages.cancel();
    }

    public interface MessageRepositoryCallback {
        void onLastMessagesFetched(@NonNull ArrayList<Message> oldMessages);


        void onNewMessagesReady(@NonNull ArrayList<Message> messages);

        void onSendMessageReady(@NonNull Message message);
    }
}

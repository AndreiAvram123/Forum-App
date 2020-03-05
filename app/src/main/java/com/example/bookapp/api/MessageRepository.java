package com.example.bookapp.api;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.bookapp.models.ApiConstants;
import com.example.bookapp.models.Message;
import com.example.bookapp.utilities.MessageDataConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.bookapp.models.ApiConstants.URL_SEND_MESSAGE;

public class MessageRepository extends Repository {

    private static MessageRepository instance;

    private MessageRepositoryCallback callback;
    private int currentMessageOffset;
    private static final String TAG = Message.class.getSimpleName();
    private Timer timer;
    private TimerTask pushRequestNewMessages;
    private boolean shouldOtherFunctionsWait = false;
    private boolean lastMessagesFetched = false;
    private String lastMessageID;

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
        shouldOtherFunctionsWait = true;
        String formattedURlOldMessages = String.format(ApiConstants.URL_OLD_MESSAGES, this.currentUserID, user2ID, offset);
        //push request
        StringRequest request = new StringRequest(Request.Method.GET, formattedURlOldMessages, (response) ->
        {
            ArrayList<Message> oldMessages = MessageDataConverter.convertJsonArrayToMessages(response);
            if (!oldMessages.isEmpty()) {
                shouldOtherFunctionsWait = false;
                this.currentMessageOffset += oldMessages.size();

                if (!lastMessagesFetched) {
                    lastMessagesFetched = true;
                    this.lastMessageID = oldMessages.get(0).getMessageID();
                }
                if (callback != null) {
                    callback.onOldMessagesFetched(oldMessages);
                }
                if (timer == null) {
                    initializeAsyncFetchFunctions(user2ID);
                }
            } else {
                //todo
                //does it work?
                this.lastMessageID = "0";
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
                JSONArray jsonArray = new JSONArray(response);
                ArrayList<Message> newMessages = MessageDataConverter.convertJsonArrayToMessages(jsonArray);
                    if (!newMessages.isEmpty() && callback != null) {
                        this.lastMessageID = newMessages.get(newMessages.size() - 1).getMessageID();
                        callback.onNewMessagesReady(newMessages);
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

        StringRequest sendMessageRequest = new StringRequest(Request.Method.POST, URL_SEND_MESSAGE,
                response -> {
                    shouldOtherFunctionsWait = false;
                    //callback when the message has reached the server
                    try {
                        JSONObject jsonObject = new JSONObject();
                            Message.Builder builder = new Message.Builder();
                        builder.setMessageContent(messageContent);
                        builder.setMessageID(jsonObject.getString("lastMessageID"));
                            builder.setSenderID(currentUserID);
                        builder.setMessageDate(jsonObject.getLong("lastMessageDate"));
                            Message message = builder.createMessage();
                            callback.onSendMessageReady(message);
                        this.lastMessageID = message.getMessageID();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    shouldOtherFunctionsWait = false;
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("messageContent", messageContent);
                params.put("currentUserId", currentUserID);
                params.put("receiverId", user2ID);
                return params;
            }
        };

        requestQueue.add(sendMessageRequest);
    }

    public void shutdownAsyncTasks() {
        pushRequestNewMessages.cancel();
    }

    public interface MessageRepositoryCallback {

        void onOldMessagesFetched(@NonNull ArrayList<Message> oldMessages);

        void onNewMessagesReady(@NonNull ArrayList<Message> messages);

        void onSendMessageReady(@NonNull Message message);

    }
}

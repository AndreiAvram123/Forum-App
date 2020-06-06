package com.example.dataLayer.repositories;

import androidx.annotation.NonNull;

import com.example.bookapp.models.MessageDTO;

import java.util.ArrayList;

public class MessageRepository {

//    private static MessageRepository instance;
//    private MessageRepositoryCallback callback;
//    private int currentMessageOffset;
//    private static final String TAG = Message.class.getSimpleName();
//    private Timer timer;
//    private TimerTask pushRequestNewMessages;
//    private boolean shouldOtherFunctionsWait = false;
//    private boolean lastMessagesFetched = false;
//    private String lastMessageID;
//
//
//
//    public void setCallback(MessageRepositoryCallback messageRepositoryCallback) {
//        this.callback = messageRepositoryCallback;
//    }
//
//    private void initializeAsyncFetchFunctions(String user2ID) {
//        pushRequestNewMessages = new TimerTask() {
//            @Override
//            public void run() {
//                if (!shouldOtherFunctionsWait) {
//                    pushRequestFetchNewMessage(currentUserID, user2ID);
//                }
//            }
//        };
//    }
//
//    private void startAsyncFunctions() {
//        int newMessageCheckInterval = 1500;
//        timer.scheduleAtFixedRate(pushRequestNewMessages, 0, newMessageCheckInterval);
//    }
//
//    /**********************************************Push request functions ********************************/
//
//
//    public void pushRequestFetchOldMessages(String user2ID, int offset) {
//        shouldOtherFunctionsWait = true;
//        String formattedURlOldMessages = String.format(ApiConstants.URL_OLD_MESSAGES, this.currentUserID, user2ID, offset);
//        //push request
//        StringRequest request = new StringRequest(Request.Method.GET, formattedURlOldMessages, (response) ->
//        {
//            ArrayList<Message> oldMessages = MessageDataConverter.convertJsonArrayToMessages(response);
//            if (!oldMessages.isEmpty()) {
//                shouldOtherFunctionsWait = false;
//                this.currentMessageOffset += oldMessages.size();
//
//                if (!lastMessagesFetched) {
//                    lastMessagesFetched = true;
//                    this.lastMessageID = oldMessages.get(0).getMessageID();
//                }
//                if (callback != null) {
//                    callback.onOldMessagesFetched(oldMessages);
//                }
//                if (timer == null) {
//                    initializeAsyncFetchFunctions(user2ID);
//                    startAsyncFunctions();
//                }
//            } else {
//                //todo
//                //does it work?
//                this.lastMessageID = "0";
//            }
//        },
//                Throwable::printStackTrace);
//        requestQueue.add(request);
//
//    }
//
//    private void pushRequestFetchNewMessage(String currentUserID, String user2ID) {
//        String formattedURLNewMessages = String.format(ApiConstants.URL_FETCH_NEW_MESSAGE, currentUserID, user2ID, lastMessageID);
//        //push request
//        StringRequest request = new StringRequest(Request.Method.GET, formattedURLNewMessages, (response) ->
//        {
//            try {
//                JSONArray jsonArray = new JSONArray(response);
//                ArrayList<Message> newMessages = MessageDataConverter.convertJsonArrayToMessages(jsonArray);
//                    if (!newMessages.isEmpty() && callback != null) {
//                        this.lastMessageID = newMessages.get(newMessages.size() - 1).getMessageID();
//                        callback.onNewMessagesReady(newMessages);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        },
//                Throwable::printStackTrace);
//        requestQueue.add(request);
//    }
//
//    public void sendMessage(String messageContent, String user2ID, String currentUserID) {
//        shouldOtherFunctionsWait = true;
//
//        StringRequest sendMessageRequest = new StringRequest(Request.Method.POST, URL_SEND_MESSAGE,
//                response -> {
//                    shouldOtherFunctionsWait = false;
//                    //callback when the message has reached the server
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                            Message.Builder builder = new Message.Builder();
//                        builder.setMessageContent(messageContent);
//                        builder.setMessageID(jsonObject.getString("lastMessageID"));
//                            builder.setSenderID(currentUserID);
//                        builder.setMessageDate(jsonObject.getLong("lastMessageDate"));
//                            Message message = builder.createMessage();
//                            callback.onSendMessageReady(message);
//                        this.lastMessageID = message.getMessageID();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                },
//                error -> {
//                    shouldOtherFunctionsWait = false;
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("messageContent", messageContent);
//                params.put("currentUserId", currentUserID);
//                params.put("receiverId", user2ID);
//                return params;
//            }
//        };
//
//        requestQueue.add(sendMessageRequest);
//    }
//
//    public void shutdownAsyncTasks() {
//        pushRequestNewMessages.cancel();
//    }
//
    public interface MessageRepositoryCallback {

    void onOldMessagesFetched(@NonNull ArrayList<MessageDTO> oldMessageDTOS);

    void onNewMessagesReady(@NonNull ArrayList<MessageDTO> messageDTOS);

    void onSendMessageReady(@NonNull MessageDTO messageDTO);

    }
}

package com.example.bookapp.utilities;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageDataConverter {

    @NonNull
    public static ArrayList<Message> convertJsonArrayToMessages(String response) {
        ArrayList<Message> dataToReturn = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject messageJson = jsonArray.getJSONObject(i);
                Message message = new Message(messageJson.getString("messageContent"),
                        messageJson.getLong("messageDate"), messageJson.getString("senderId"));
                dataToReturn.add(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataToReturn;
    }
}

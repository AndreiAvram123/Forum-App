package com.example.bookapp.utilities;

import androidx.annotation.NonNull;

import com.example.bookapp.models.MessageDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageDataConverter {

    @NonNull
    public static ArrayList<MessageDTO> convertJsonArrayToMessages(String response) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            return new ArrayList<>();
        } else {
            return convertJsonArrayToMessages(jsonArray);
        }
    }

    @NonNull
    public static ArrayList<MessageDTO> convertJsonArrayToMessages(@NonNull JSONArray jsonArray) {
        ArrayList<MessageDTO> dataToReturn = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject messageJson = jsonArray.getJSONObject(i);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToReturn;
    }

}

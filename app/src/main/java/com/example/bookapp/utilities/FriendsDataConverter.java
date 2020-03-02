package com.example.bookapp.utilities;

import androidx.annotation.NonNull;

import com.example.bookapp.models.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsDataConverter {
    @NonNull
    public static ArrayList<Friend> convertJsonArrayToFriendsObjects(@NonNull String json) {
        ArrayList<Friend> dataToReturn = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject friendJson = jsonArray.getJSONObject(i);
                Friend.Builder builder = new Friend.Builder();
                builder.setUserID(friendJson.getString("userID"))
                        .setUsername(friendJson.getString("username"))
                        .setImageURL(friendJson.getString("profilePicture"));
                if (!friendJson.isNull("lastMessage")) {
                    JSONObject lastMessageJson = friendJson.getJSONObject("lastMessage");
                    builder.setLastMessage(MessageDataConverter.convertJsonObjectToMessage(lastMessageJson));
                }
                dataToReturn.add(builder.create());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataToReturn;
    }

}

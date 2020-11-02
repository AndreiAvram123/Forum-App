package com.andrei.kit.interfaces;

import androidx.annotation.NonNull;

public interface MessageInterface {
    void sendMessage(@NonNull String messageContent, @NonNull String user2ID, @NonNull String currentUserID);

    void fetchMoreMessages(@NonNull String user2ID, int offset);
}

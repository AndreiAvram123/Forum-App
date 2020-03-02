package com.example.bookapp.models;

public class ApiConstants {
    public static final String URL_LATEST_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?recentPosts";
    public static final String URL_POST_AUTOCOMPLETE = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?suggestionQuery=%s";
    public static final String URL_POST_COMMENTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s&comments";
    public static final String URL_POST_DETAILS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s";
    public static final String URL_UPLOAD_COMMENT = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?uploadComment";
    public static final String UPLOAD_IMAGE_URL = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?uploadPost";
    public static final String URL_SAVED_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?savedPosts&userID=%s";
    public static final String URL_ADD_POST_TO_FAVORITES = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?addPostToFavorite&postID=%s&userID=%s";
    public static final String URL_AUTHENTICATE_ACCOUNT_ID = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?authenticateThirdPartyAccount&email=%s";
    public static final String URL_CREATE_THIRD_PARTY_ACCOUNT = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?createThirdPartyAccount";
    public static final String URL_MY_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?myPosts&userID=%s";
    public static final String URL_MY_FRIENDS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?friends&lastMessage&userID=%s";
    public static final String URL_OLD_MESSAGES = "http://sgb967.poseidon.salford.ac.uk/cms/ChatController.php?requestName=fetchOldMessages&currentUserId=%s&receiverId=%s&offset=%s";
    public static final String URL_SEND_MESSAGE = "http://sgb967.poseidon.salford.ac.uk/cms/ChatController.php?requestName=sendMessage";
    public static final String URL_FETCH_NEW_MESSAGE = "http://sgb967.poseidon.salford.ac.uk/cms/ChatController.php?requestName=fetchNewMessages&currentUserId=%s&receiverId=%s&lastMessageId=%s";


    public static final int RESPONSE_CODE_ACCOUNT_EXISTS = 1;
    public static final int RESPONSE_CODE_ACCOUNT_UNEXISTENT = 0;
    public static final int RESPONSE_CODE_ACCOUNT_PARAMETERS_NOT_PROVIDED = -1;
    public static final int RESPONSE_CODE_ACCOUNT_CREATED = 2;
    public static final int REQUEST_COMPLETED_NO_ERROR = 200;
}

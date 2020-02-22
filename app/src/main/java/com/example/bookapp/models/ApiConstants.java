package com.example.bookapp.models;

public class ApiConstants {
    public static final String URL_LATEST_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?recentPosts";
    public static final String URL_POST_AUTOCOMPLETE = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?suggestionQuery=%s";
    public static final String URL_POST_COMMENTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s&comments";
    public static final String URL_POST_DETAILS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?postID=%s";
    public static final String URL_UPLOAD_COMMENT = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?uploadComment";
    public static final String UPLOAD_IMAGE_URL = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?uploadPost";
    public static final String URL_SAVED_POSTS = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?savedPosts&userID=%s";
    public static final String URL_AUTHENTICATE_ACCOUNT_ID = "http://sgb967.poseidon.salford.ac.uk/cms/RestfulRequestHandler.php?authenticateThirdPartyAccount&userID=%s";
    public static final int RESPONSE_CODE_ACCOUNT_EXISTS = 1;
    public static final int RESPONSE_CODE_ACCOUNT_UNEXISTENT = 0;
    public static final int RESPONSE_CODE_ACCOUNT_ID_NOT_PROVIDED = -1;

}

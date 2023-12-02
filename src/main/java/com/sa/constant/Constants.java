package com.sa.constant;

import java.net.HttpURLConnection;

public interface Constants {

    interface StatusCode {
        String OK_200_STR = HttpURLConnection.HTTP_OK + "";

        String CREATED_201_STR = HttpURLConnection.HTTP_CREATED + "";
        String BAD_REQUEST_400_STR = HttpURLConnection.HTTP_BAD_REQUEST + "";
    }
}

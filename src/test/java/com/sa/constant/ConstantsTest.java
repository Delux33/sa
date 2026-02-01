package com.sa.constant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Constants Tests")
class ConstantsTest {

    @Test
    @DisplayName("Should have correct OK_200_STR value")
    void shouldHaveCorrectOk200StrValue() {
        assertEquals("200", Constants.StatusCode.OK_200_STR);
        assertEquals(String.valueOf(HttpURLConnection.HTTP_OK), Constants.StatusCode.OK_200_STR);
    }

    @Test
    @DisplayName("Should have correct CREATED_201_STR value")
    void shouldHaveCorrectCreated201StrValue() {
        assertEquals("201", Constants.StatusCode.CREATED_201_STR);
        assertEquals(String.valueOf(HttpURLConnection.HTTP_CREATED), Constants.StatusCode.CREATED_201_STR);
    }

    @Test
    @DisplayName("Should have correct BAD_REQUEST_400_STR value")
    void shouldHaveCorrectBadRequest400StrValue() {
        assertEquals("400", Constants.StatusCode.BAD_REQUEST_400_STR);
        assertEquals(String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST), Constants.StatusCode.BAD_REQUEST_400_STR);
    }
}

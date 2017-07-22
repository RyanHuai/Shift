package com.mainframevampire.shift.data.remote;

public class ApiUtils {

    private ApiUtils() {};

    public static final String BASE_URL = "https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}

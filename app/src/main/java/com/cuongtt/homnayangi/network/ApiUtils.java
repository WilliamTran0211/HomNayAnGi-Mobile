package com.cuongtt.homnayangi.network;

public class ApiUtils {
    public static final String BASE_URL = "http://10.0.2.2:8000/";
    private ApiUtils() {
    }

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}

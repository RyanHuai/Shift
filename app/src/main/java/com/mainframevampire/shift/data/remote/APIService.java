package com.mainframevampire.shift.data.remote;


import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.InputShift;
import com.mainframevampire.shift.data.model.Shift;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers("Authorization: Deputy RyanHuai")
    @GET("dmc/business")
    Call<Business>  saveBusiness();

    @Headers("Authorization: Deputy RyanHuai")
    @POST("dmc/shift/start")
    Call<String> saveStartShift(@Body InputShift inputShift);

    @Headers("Authorization: Deputy RyanHuai")
    @POST("dmc/shift/end")
    Call<String> saveEndShift(@Body InputShift inputShift);

    @Headers("Authorization: Deputy RyanHuai")
    @GET("dmc/shifts")
    Call<ArrayList<Shift>>  saveShifts();



}

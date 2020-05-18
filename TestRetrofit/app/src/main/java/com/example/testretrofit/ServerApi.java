package com.example.testretrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerApi {
    @GET("breeds")
    Call<List<Poroda>> getBreeds();
    @GET("images/search")
    //https://api.thecatapi.com/v1/images/search?breed_id=aege&limit=100
    Call<List<UrlImg>> getImgUrl(@Query("breed_id") String id, @Query("limit") int limit);
    @POST("votes")
    Call<Golos> setGolos(@Body Golos g);

}

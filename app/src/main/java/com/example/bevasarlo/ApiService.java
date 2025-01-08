package com.example.bevasarlo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("products")
    Call<Void> createProduct(@Body Product product);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @GET("products")
    Call<List<Product>> getProducts();

    @PUT("products/{id}")
    Call<Void> updateProduct(@Path("id") int id, @Body Product product);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}


package com.ateam.funshoppers.Rest;


import com.ateam.funshoppers.model.Business;
import com.ateam.funshoppers.model.BusinessAddress;
import com.ateam.funshoppers.model.BusinessPictures;
import com.ateam.funshoppers.model.BusinessProducts;
import com.ateam.funshoppers.model.BusinessReviews;
import com.ateam.funshoppers.model.Businesses;
import com.ateam.funshoppers.model.ContactDetails;
import com.ateam.funshoppers.model.ProductFavourites;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by Home on 11/30/2016.
 */

public interface ApiInterface {

    @GET("api/business/search")
    Call<List<Businesses>> getBusinesses();
    @GET("api/business/{id}")
    Call<Business> getBusiness(@Path("id") String id);

    @GET("api/business/{id}/products")
    Call<List<BusinessProducts>> getBusinessProducts(@Path("id") String id);

    @GET("api/business/{id}/pictures")
    Call<List<BusinessPictures>> getBusinessPictures(@Path("id") String id);


    @GET("api/business/{id}/reviews")
    Call<List<BusinessReviews>> getBusinessReviews(@Path("id") String id);

    @GET("api/business/{id}/favourites")
    Call<List<ProductFavourites>> getBusinessFavourites(@Path("id") String id);

    @GET("api/business/{id}/contacts")
    Call<List<ContactDetails>> getContactDetails(@Path("id") String id);

    @GET("api/business/{id}/addresses")
    Call<List<BusinessAddress>> getBusinessAddress(@Path("id") String id);

    @GET("api/product/{id}")
    Call<BusinessProducts> getProductDetails(@Path("id") String id);

    @GET("api/product/{id}/pictures")
    Call<List<BusinessPictures>> getProductPictures(@Path("id") String id);

    @GET("api/product/{id}/reviews")
    Call<List<BusinessReviews>> getProductReviews(@Path("id") String id);

    @GET("api/product/{id}/favourites")
    Call<ProductFavourites> getProductFavourites(@Path("id") int id);

    @GET("api/review/{id}")
    Call<BusinessReviews> getReviewDetails(@Path("id") int id);

    @GET("api/review/{id}/comments")
    Call<BusinessReviews> getReviewComments(@Path("id") int id);

    @GET("api/review/{id}/likes")
    Call<ProductFavourites> getReviewLikes(@Path("id") int id);


}
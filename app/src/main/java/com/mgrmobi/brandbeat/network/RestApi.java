package com.mgrmobi.brandbeat.network;

import com.mgrmobi.brandbeat.network.request.RequestBrand;
import com.mgrmobi.brandbeat.network.request.RequestIdsCategory;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestLogin;
import com.mgrmobi.brandbeat.network.request.RequestPassword;
import com.mgrmobi.brandbeat.network.request.RequestRegistration;
import com.mgrmobi.brandbeat.network.request.RequestReview;
import com.mgrmobi.brandbeat.network.request.RequestSocial;
import com.mgrmobi.brandbeat.network.request.RequestUpdateProfile;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseIncomeRange;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;
import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseRegistration;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;
import com.mgrmobi.brandbeat.network.responce.ResponseSubCategotry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit.http.Field;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.PUT;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface RestApi {

    @POST("/auth/internal/")
    Call<BaseResponse<ResponseLogin>> login(@Body RequestLogin request);

    @POST("/registration/internal/")
    Call<ResponseRegistration> registration(@Body RequestRegistration request);

    @GET("/user/{id}/info/")
    Call<BaseResponse<ResponseProfile>> getUserProfile(@Header("X-ACCESS-TOKEN") String token, @Path("id") String id);

    @GET("/categories/")
    Call<BaseResponse<ArrayList<ResponseCategories>>> getCategories(@Header("X-ACCESS-TOKEN") String token);

    @GET("/categories/{category_id}/brands/")
    Call<BaseResponse<ArrayList<ResponseCategories>>> getSubCategories(@Header("X-ACCESS-TOKEN") String token, @Path("category_id") String catId, @Query("page") String page, @Query("pageSize") String pageSize);

    @PUT("interests/subcategories")
    Call<BaseResponse> setSubCategories(@Header("X-ACCESS-TOKEN") String token, @Body Map<String, List<String>> strings);

    @POST("/reset-password-request/")
    Call<BaseResponse> resetPassword(@Body Map<String, String> stringStringMap);

    @POST("/auth/social/")
    Call<BaseResponse<ResponseLogin>> loginSocial(@Body RequestSocial social);

    @POST("/logout")
    Call<BaseResponse> logout(@Header("X-ACCESS-TOKEN") String token);

    @GET("/feeds/follower")
    Call<BaseResponse<List<ResponseFeed>>> getMyFeed(@Header("X-ACCESS-TOKEN") String token, @Query("page") String page, @Query("pageSize") String pageSize, @Query("withText") String withText);

    @GET("/feeds/local")
    Call<BaseResponse<List<ResponseLocalFeed>>> getLocalFeed(@Header("X-ACCESS-TOKEN") String token, @Query("lat") String lat, @Query("lng") String lng,
                                                             @Query("page") String page, @Query("pageSize") String pageSize, @Query("withText") String withText);

    @GET("/brands/{brand_id}/reviews")
    Call<BaseResponse<List<ResponseReview>>> getReviews(@Header("X-ACCESS-TOKEN") String token, @Path("brand_id") String id, @Query("page") String page, @Query("pageSize") String pageSize, @Query("withText") String withText);

    @GET("/brands/{brand_id}/")
    Call<BaseResponse<ResponseBrand>> getBrand(@Header("X-ACCESS-TOKEN") String token, @Path("brand_id") String id);

    @PUT("/interests/brands/")
    Call<BaseResponse> followBrand(@Header("X-ACCESS-TOKEN") String token, @Body Map<String, ArrayList<String>> id);

    @POST("interests/brands/deletes")
    Call<BaseResponse> unFollowBrand(@Header("X-ACCESS-TOKEN") String token, @Body Map<String, ArrayList<String>> id);

    @POST("brands/{brand_id}/reviews")
    Call<BaseResponse> sendReview(@Header("X-ACCESS-TOKEN") String token, @Path("brand_id") String id, @Body RequestReview social);

    @POST("feeds/{review_id}/")
    Call<BaseResponse> updateReview(@Header("X-ACCESS-TOKEN") String token, @Path("review_id") String id, @Body RequestReview review);

    @GET("/feeds/{id}/")
    Call<BaseResponse<ResponseReview>> getReview(@Header("X-ACCESS-TOKEN") String token, @Path("id") String id);

    @DELETE("feeds/{review_id}/")
    Call<BaseResponse> deleteReview(@Header("X-ACCESS-TOKEN") String token, @Path("review_id") String id);

    @GET("feeds/{review_id}/comments")
    Call<BaseResponse<ArrayList<ResponseComment>>> getReviewComments(@Header("X-ACCESS-TOKEN") String token, @Path("review_id") String id);

    @GET("/user/{user_id}/reviews")
    Call<BaseResponse<ArrayList<ResponseReview>>> getUserReview(@Header("X-ACCESS-TOKEN") String token, @Path("user_id") String id, @Query("page") String page, @Query("pageSize") String pageSize);

    @Multipart
    @POST("/storage/users/upload/")
    Call<BaseResponse<ResponsePhotoUrl>> updatePhoto(@Header("X-ACCESS-TOKEN") String token, @Part("file\"; filename=\"pp.jpeg\"") RequestBody photo);

    @Multipart
    @POST("/storage/brands/upload/")
    Call<BaseResponse<ResponsePhotoUrl>> updateBrandPhoto(@Header("X-ACCESS-TOKEN") String token, @Part("file\"; filename=\"pp.jpeg\"") RequestBody photo);

    @PUT("/user/edit/")
    Call<BaseResponse> updateUser(@Header("X-ACCESS-TOKEN") String token, @Header("X-DEVICE-ID") String deviceId, @Body RequestUpdateProfile photo);

    @POST("/change-password")
    Call<BaseResponse> updatePassword(@Header("X-ACCESS-TOKEN") String token, @Body RequestPassword requestPassword);

    @POST("/brands/")
    Call<BaseResponse> createBrand(@Header("X-ACCESS-TOKEN") String token, @Body RequestBrand brand);

    @PUT("/interests/categories")
    Call<BaseResponse> setCategories(@Header("X-ACCESS-TOKEN") String token, @Body RequestIdsCategory categoriesBody);

    @POST("/interests/categories/deletes")
    Call<BaseResponse> deleteCategories(@Header("X-ACCESS-TOKEN") String token, @Body Map<String, List<String>> strings);

    @POST("/interests/subcategories/deletes")
    Call<BaseResponse> deleteSubCategories(@Header("X-ACCESS-TOKEN") String token, @Body Map<String, List<String>> strings);

    @GET("/last-viewed/brands")
    Call<BaseResponse<ArrayList<ResponseBrand>>> getRecentBrands(@Header("X-ACCESS-TOKEN") String token);

    @POST("/feeds/{review_id}/comments")
    Call<BaseResponse> addComment(@Header("X-ACCESS-TOKEN") String token, @Path("review_id") String reviewId, @Body Map<String, String> map);

    @POST("/comments/{comment_id}")
    Call<BaseResponse> editComment(@Header("X-ACCESS-TOKEN") String token, @Path("comment_id") String reviewId, @Body Map<String, String> map);

    @GET("/brands")
    Call<BaseResponse<List<ResponseBrand>>> getSubCategoriesBrand(@Header("X-ACCESS-TOKEN") String token, @Query("filter") String subCategoryString, @Query("pageSize") String pageSize, @Query("page") String page);

    @GET("/feeds")
    Call<BaseResponse<ArrayList<ResponseReview>>> getReviewByHashTag(@Header("X-ACCESS-TOKEN") String token, @Query("filter") String filter);

    @GET("/brands")
    Call<BaseResponse<List<ResponseBrand>>> getTrandingBrands(@Header("X-ACCESS-TOKEN") String token, @Query("filter") String filter);//, @Query("pageSize") String pageSize);

    @DELETE("/comments/{comment_id}")
    Call<BaseResponse> deleteComment(@Header("X-ACCESS-TOKEN") String token, @Path("comment_id") String id);

    @POST("/user/{user_id}/subscribe")
    Call<BaseResponse> followUser(@Header("X-ACCESS-TOKEN") String token, @Path("user_id") String id);

    @POST("/user/{user_id}/unsubscribe")
    Call<BaseResponse> unFollowUser(@Header("X-ACCESS-TOKEN") String token, @Path("user_id") String id);

    @GET("/brands")
    Call<BaseResponse<ArrayList<ResponseBrand>>> getSearchBrand(@Header("X-ACCESS-TOKEN") String token, @Query("query") String searchString);

    @GET("/brands")
    Call<BaseResponse<ArrayList<ResponseBrand>>> getSearchBrand(@Header("X-ACCESS-TOKEN") String token, @Query("query") String searchString, @Query("filter") String subcategory);

    @GET("/brands")
    Call<BaseResponse<ArrayList<ResponseBrand>>> getSearchBrandFilter(@Header("X-ACCESS-TOKEN") String token, @Query("filter") String subcategory);

    @POST("/feeds/{review_id}/like")
    Call<BaseResponse> addLikeReview(@Header("X-ACCESS-TOKEN") String token, @Path("review_id") String id);

    @DELETE("/feeds/{review_id}/like")
    Call<BaseResponse> deleteLikeReview(@Header("X-ACCESS-TOKEN") String token, @Path("review_id") String id);

    @GET("/notifications")
    Call<BaseResponse<List<ResponseNotification>>> getNotifications(@Header("X-ACCESS-TOKEN") String token, @Query("page") String page, @Query("pageSize") String pageSize);

    @GET("/notifications-manage")
    Call<BaseResponse<ResponseNotificationSettings>> getNotificationsSettings(@Header("X-ACCESS-TOKEN") String token);

    @POST("/notifications-manage")
    Call<BaseResponse> setNotificationSettings(@Header("X-ACCESS-TOKEN") String token, @Body ResponseNotificationSettings responseNotificationSettings);

    @GET("/brands")
    Call<BaseResponse<List<ResponseBrand>>> getFeatureBrand(@Header("X-ACCESS-TOKEN") String token, @Query("filter") String filterValue);

    @DELETE("/storage/users/{file_path}")
    Call<BaseResponse> deleteUserFile(@Header("X-ACCESS-TOKEN") String token, @Path("file_path") String file_path, @Query("fromField") String s);

    @PUT("/auth/token/")
    Call<BaseResponse<ResponseLogin>> updateToken(@Body Map<String, String> refreshToken);

    @Multipart
    @POST("/storage/reviews/upload/")
    Call<BaseResponse<ResponsePhotoUrl>> uploadReviewPhoto(@Header("X-ACCESS-TOKEN") String token, @PartMap Map<String, RequestBody> stringRequestBodyMap);

    @GET("/income-ranges")
    Call<BaseResponse<List<ResponseIncomeRange>>> getIncomeRange(@Header("X-ACCESS-TOKEN") String token);

    @GET("/income-ranges")
    Call<BaseResponse<List<ResponseIncomeRange>>> getIncomeRange(@Header("X-ACCESS-TOKEN") String token, @Query("filter") String country);

    @GET("/user/{user_id}/achievements")
    Call<BaseResponse<List<ResponseAchievement>>> getAchivmients(@Header("X-ACCESS-TOKEN") String token, @Path("user_id") String userId);

    @GET("/achievements")
    Call<BaseResponse<List<ResponseAchievement>>> getAchivmients(@Header("X-ACCESS-TOKEN") String token);

    @POST("/user/update/location")
    Call<BaseResponse> updateLocation(@Header("X-ACCESS-TOKEN") String token, @Body RequestLocation location);

    @GET("/brands/{brand_id}/reviews")
    Call<BaseResponse<List<ResponseReview>>> getReviewsWithParams(@Header("X-ACCESS-TOKEN") String token,
                                                                  @Path("brand_id") String brandId,
                                                                  @QueryMap Map<String, String> map);

    @GET("brands/{brand_id}/reviews/stats")
    Call<BaseResponse<ResponseStatistics>> getBrandReviewStatistics(@Header("X-ACCESS-TOKEN") String token, @Path("brand_id") String brandId);

    @GET("subcategories/{brand_id}")
    Call<BaseResponse<ResponseSubCategotry>> getSubcategoryBrands(@Header("X-ACCESS-TOKEN") String token, @Path("brand_id") String brandId);
}


package com.mgrmobi.brandbeat.network;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.network.request.RequestBrand;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestLogin;
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

import rx.Observable;


/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */

public interface RestApiBean {
    Observable<BaseResponse<ResponseLogin>> login(@NonNull RequestLogin requestLogin);

    Observable<BaseResponse<ResponseLogin>> loginSocial(@NonNull RequestSocial social);

    Observable<ResponseRegistration> register(@NonNull RequestRegistration requestRegistration);

    Observable<BaseResponse<ResponseProfile>> getProfile(@NonNull RequestProfile requestProfile);

    Observable<BaseResponse<ArrayList<ResponseCategories>>> getCategories();

    Observable<BaseResponse> deleteCategories(List<String> id);

    Observable<BaseResponse> deleteComment(String id);

    Observable<BaseResponse<ArrayList<ResponseCategories>>> getSubCategories(String id);

    Observable<BaseResponse> restorePassword(String email);

    Observable<BaseResponse> logOut();

    Observable<BaseResponse<List<ResponseFeed>>> getMyFeed(int page);

    Observable<BaseResponse<List<ResponseLocalFeed>>> getLocalFeed(String lat, String lng, String page);

    Observable<BaseResponse<List<ResponseReview>>> getReviews(String brandId, String page, String pageCount);

    Observable<BaseResponse<ResponseBrand>> getBrand(String brandId);

    Observable<BaseResponse> followBrand(String brandId);

    Observable<BaseResponse> unFollowBrand(String brtandId);

    Observable<BaseResponse> sendReview(RequestReview review);

    Observable<BaseResponse> updateReview(RequestReview review);

    Observable<BaseResponse<ResponseReview>> getReview(String idReview, String idBrand);

    Observable<BaseResponse> deleteReview(String idReview);

    Observable<BaseResponse<ArrayList<ResponseComment>>> getComments(String reviewId);

    Observable<BaseResponse<ArrayList<ResponseReview>>> getUserReviews(String userId, String page);

    Observable<BaseResponse<ResponsePhotoUrl>> updatePhoto(Bitmap bitmap);

    Observable<BaseResponse<ResponsePhotoUrl>> updateBrandPhoto(Bitmap bitmap);

    Observable<BaseResponse> updateUser(RequestUpdateProfile profile);

    Observable<BaseResponse> updatePassword(String oldPassword, String newPassword);

    Observable<BaseResponse> createBrand(RequestBrand requestBrand);

    Observable<BaseResponse> updateCategory(ArrayList<String> ids);

    Observable<BaseResponse> setSubCategories(ArrayList<String> ids);

    Observable<BaseResponse> deleteSubCategories(ArrayList<String> ids);

    Observable<BaseResponse<ArrayList<ResponseBrand>>> getRecentBrand();

    Observable<BaseResponse> addComment(String idReview, String text);

    Observable<BaseResponse> editComment(String idComment, String text);

    Observable<BaseResponse<List<ResponseBrand>>> getSubCategoryBrands(String subCategoryId, String page);

    Observable<BaseResponse<ArrayList<ResponseReview>>> getReviewsByHashTag(String hashTag);

    Observable<BaseResponse<List<ResponseBrand>>> getTrandinBrands();

    Observable<BaseResponse> followUser(String id);

    Observable<BaseResponse> unFollowUser(String id);

    Observable<BaseResponse<ArrayList<ResponseBrand>>> getSearchBrands(String searchString, String categoryId);

    Observable<BaseResponse<ArrayList<ResponseBrand>>> getSuggestedBrand();

    Observable<BaseResponse> addLikeReview(String idReview);

    Observable<BaseResponse> deleteLike(String idReview);

    Observable<BaseResponse<List<ResponseNotification>>> getNotifications(String page);

    Observable<BaseResponse<ResponseNotificationSettings>> getNotificationsSettings();

    Observable<BaseResponse> setNotificationsSettings(ResponseNotificationSettings responseNotificationSettings);

    Observable<BaseResponse<List<ResponseBrand>>> getFeatureBrand();

    Observable<BaseResponse> deleteUserFile(String path);

    Observable<BaseResponse<ResponseLogin>> updateToken();

    Observable<BaseResponse<ResponsePhotoUrl>> updateReviewsImage(ArrayList<Bitmap> bitmaps);

    Observable<BaseResponse<List<ResponseIncomeRange>>> getIncomeRange();

    Observable<BaseResponse<List<ResponseIncomeRange>>> getIncomeRange(String country);

    Observable<BaseResponse<List<ResponseAchievement>>> getAchivmients();

    Observable<BaseResponse<List<ResponseAchievement>>> getAllAchivmients();

    Observable<BaseResponse> updateLocation(RequestLocation location);

    Observable<BaseResponse<List<ResponseReview>>> getReviewsFromParamentrs(String brandId, String rate, String page,
                                                                            boolean withText, boolean withComments);

    Observable<BaseResponse<ResponseStatistics>> getBrandReviewStatistics(String brandId);

    Observable<BaseResponse<ResponseSubCategotry>> getSubactegories(String subacategoryId);
}

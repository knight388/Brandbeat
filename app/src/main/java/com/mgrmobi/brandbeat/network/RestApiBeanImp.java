package com.mgrmobi.brandbeat.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.BuildConfig;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.errors.APIError;
import com.mgrmobi.brandbeat.network.errors.ErrorUtils;
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
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.CompositeException;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@Singleton
public class RestApiBeanImp extends AbstractRestApi implements RestApiBean {

    private Context appContext;
    private RestApi restApi;
    public Retrofit.Builder builder;
    private Retrofit retrofit;
    private final String PAGE_SIZE = "20";
    private OkHttpClient client;

    @Inject
    public RestApiBeanImp(Context appContext) {
        super(appContext);

        this.appContext = appContext;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).addInterceptor(logging).build();

        builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);
        retrofit = builder.build();
        restApi = retrofit.create(RestApi.class);
    }

    @Override
    public Observable<BaseResponse<ResponseLogin>> login(final RequestLogin requestLogin) {
        return Observable.<BaseResponse<ResponseLogin>>create(subscriber -> {
            Call<BaseResponse<ResponseLogin>> call = restApi.login(requestLogin);
            try {
                Response<BaseResponse<ResponseLogin>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseLogin>> loginSocial(@NonNull final RequestSocial social) {
        return Observable.<BaseResponse<ResponseLogin>>create(subscriber -> {
            Call<BaseResponse<ResponseLogin>> call = restApi.loginSocial(social);
            try {
                Response<BaseResponse<ResponseLogin>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }


    @Override
    public Observable<ResponseRegistration> register(@NonNull final RequestRegistration requestRegistration) {
        return Observable.<ResponseRegistration>create(subscriber -> {
            Call<ResponseRegistration> call = restApi.registration(requestRegistration);
            try {
                Response<ResponseRegistration> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseProfile>> getProfile(@NonNull final RequestProfile requestProfile) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse<ResponseProfile>>create(subscriber -> {
            Call<BaseResponse<ResponseProfile>> call = restApi.getUserProfile(token, requestProfile.getId());
            try {
                Response<BaseResponse<ResponseProfile>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseCategories>>> getCategories() {

        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse<ArrayList<ResponseCategories>>>create(subscriber -> {
            Call<BaseResponse<ArrayList<ResponseCategories>>> call = restApi.getCategories(token);
            try {
                Response<BaseResponse<ArrayList<ResponseCategories>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> deleteCategories(final List<String> id) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        Map<String, List<String>> stringArrayListMap = new HashMap<>();
        stringArrayListMap.put("categories", id);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse>create(subscriber -> {
            Call<BaseResponse> call = restApi.deleteCategories(token, stringArrayListMap);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    subscriber.onError(null);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> deleteComment(final String id) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        List<String> categoriesId = new ArrayList<>();
        categoriesId.add(id);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse>create(subscriber -> {
            Call<BaseResponse> call = restApi.deleteComment(token, id);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    subscriber.onError(null);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseCategories>>> getSubCategories(final String id) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse<ArrayList<ResponseCategories>>>create(subscriber -> {
            Call<BaseResponse<ArrayList<ResponseCategories>>> call = restApi.getSubCategories(token, id, 1 +"",100+"");
            try {
                Response<BaseResponse<ArrayList<ResponseCategories>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> restorePassword(String email) {
        return Observable.<BaseResponse>create(subscriber -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("email", email);
            Call<BaseResponse> call = restApi.resetPassword(map);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> logOut() {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.logout(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN));
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseFeed>>> getMyFeed(int page) {
        return Observable.<BaseResponse<List<ResponseFeed>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseFeed>>> call = restApi.getMyFeed(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), page + "", Utils.PAGE_SIZE + "", "true");
            call(call, subscriber);
        });
    }

    public void call(Call call, Subscriber subscriber) {
        try {
            Response<BaseResponse<List<ResponseFeed>>> response = call.execute();
            if (response.isSuccess()) {
                subscriber.onNext(response.body());
                subscriber.onCompleted();
            }
            else {

                parseError(response, subscriber);
            }
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                subscriber.onError(new NetworkErrorException());
            }
            else {
                subscriber.onError(new Throwable(e.getMessage()));
            }
        }
    }

    @Override
    public Observable<BaseResponse<List<ResponseLocalFeed>>> getLocalFeed(String lat, String lng, String page) {
        return Observable.<BaseResponse<List<ResponseLocalFeed>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseLocalFeed>>> call = restApi.getLocalFeed(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), lat, lng, page, PAGE_SIZE, "true");
            try {
                Response<BaseResponse<List<ResponseLocalFeed>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseReview>>> getReviews(String brandId, String page, String countPage) {
        return Observable.<BaseResponse<List<ResponseReview>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseReview>>> call = restApi.getReviews(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), brandId, page, countPage, "true");
            try {
                Response<BaseResponse<List<ResponseReview>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseBrand>> getBrand(final String brandId) {
        return Observable.<BaseResponse<ResponseBrand>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<ResponseBrand>> call = restApi.getBrand(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), brandId);
            try {
                Response<BaseResponse<ResponseBrand>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> followBrand(final String brandId) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ArrayList<String> strings = new ArrayList<String>();
            Map<String, ArrayList<String>> stringArrayListMap = new HashMap<String, ArrayList<String>>();
            stringArrayListMap.put("brands", strings);
            strings.add(brandId);
            Call<BaseResponse> call = restApi.followBrand(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), stringArrayListMap);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> unFollowBrand(final String brtandId) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ArrayList<String> strings = new ArrayList<String>();
            Map<String, ArrayList<String>> stringArrayListMap = new HashMap<String, ArrayList<String>>();
            stringArrayListMap.put("brands", strings);
            strings.add(brtandId);
            Call<BaseResponse> call = restApi.unFollowBrand(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), stringArrayListMap);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> sendReview(final RequestReview review) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ArrayList<String> strings = new ArrayList<String>();

            Call<BaseResponse> call = restApi.sendReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), review.getBrandId(), review);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateReview(final RequestReview review) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ArrayList<String> strings = new ArrayList<String>();

            Call<BaseResponse> call = restApi.updateReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), review.getReviewId(), review);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }


    @Override
    public Observable<BaseResponse<ResponseReview>> getReview(final String idReview, String brandId) {
        return Observable.<BaseResponse<ResponseReview>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);

            Call<BaseResponse<ResponseReview>> call = restApi.getReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idReview);
            try {
                Response<BaseResponse<ResponseReview>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> deleteReview(final String idReview) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);

            Call<BaseResponse> call = restApi.deleteReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idReview);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseComment>>> getComments(final String idReview) {
        return Observable.<BaseResponse<ArrayList<ResponseComment>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);

            Call<BaseResponse<ArrayList<ResponseComment>>> call = restApi.getReviewComments(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idReview);
            try {
                Response<BaseResponse<ArrayList<ResponseComment>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseReview>>> getUserReviews(final String userId, String page) {
        return Observable.<BaseResponse<ArrayList<ResponseReview>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);

            Call<BaseResponse<ArrayList<ResponseReview>>> call = restApi.getUserReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), userId, page, PAGE_SIZE);
            try {
                Response<BaseResponse<ArrayList<ResponseReview>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponsePhotoUrl>> updatePhoto(final Bitmap bitmap) {
        return Observable.<BaseResponse<ResponsePhotoUrl>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Call<BaseResponse<ResponsePhotoUrl>> call = restApi.updatePhoto(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN),
                    RequestBody.create(MediaType.parse("image/jpeg"), byteArray));
            try {
                Response<BaseResponse<ResponsePhotoUrl>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponsePhotoUrl>> updateBrandPhoto(final Bitmap bitmap) {
        return Observable.<BaseResponse<ResponsePhotoUrl>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Call<BaseResponse<ResponsePhotoUrl>> call = restApi.updateBrandPhoto(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN),
                    RequestBody.create(MediaType.parse("image/jpeg"), byteArray));
            try {
                Response<BaseResponse<ResponsePhotoUrl>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateUser(final RequestUpdateProfile profile) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.updateUser(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), Utils.getHardwareId(), profile);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> updatePassword(final String oldPassword, final String newPassword) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            RequestPassword requestPassword = new RequestPassword();
            requestPassword.newPassword = newPassword;
            requestPassword.oldPassword = oldPassword;
            Call<BaseResponse> call = restApi.updatePassword(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), requestPassword);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> createBrand(final RequestBrand requestBrand) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.createBrand(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), requestBrand);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateCategory(final ArrayList<String> ids) {
        return Observable.<BaseResponse>create(subscriber -> {
            RequestIdsCategory requestBrand = new RequestIdsCategory();
            requestBrand.setStrings(ids);

            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.setCategories(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN),
                    requestBrand);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> setSubCategories(final ArrayList<String> ids) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        Map<String, List<String>> stringArrayListMap = new HashMap<>();
        stringArrayListMap.put("subcategories", ids);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse>create(subscriber -> {
            Call<BaseResponse> call = restApi.setSubCategories(token, stringArrayListMap);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    subscriber.onError(null);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> deleteSubCategories(final ArrayList<String> ids) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        Map<String, List<String>> stringArrayListMap = new HashMap<>();
        stringArrayListMap.put("subcategories", ids);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse>create(subscriber -> {
            Call<BaseResponse> call = restApi.deleteSubCategories(token, stringArrayListMap);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    subscriber.onError(null);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    try {
                        if (!subscriber.isUnsubscribed())
                            subscriber.onError(new Throwable(e.getMessage()));
                    } catch (CompositeException th) {

                    }
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseBrand>>> getRecentBrand() {
        return Observable.<BaseResponse<ArrayList<ResponseBrand>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);

            Call<BaseResponse<ArrayList<ResponseBrand>>> call = restApi.getRecentBrands(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN));
            try {
                Response<BaseResponse<ArrayList<ResponseBrand>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> addComment(String idReview, String text) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", text);
            Call<BaseResponse> call = restApi.addComment(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idReview, map);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> editComment(final String idComment, final String text) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Map<String, String> map = new HashMap<String, String>();
            map.put("text", text);
            Call<BaseResponse> call = restApi.editComment(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idComment, map);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseBrand>>> getSubCategoryBrands(final String subCategoryId, final String page) {
        return Observable.<BaseResponse<List<ResponseBrand>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            String subCategory = "'subcategoryId eq " + subCategoryId + "'";
            Call<BaseResponse<List<ResponseBrand>>> call = restApi.getSubCategoriesBrand(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), subCategory, PAGE_SIZE, page);
            try {
                Response<BaseResponse<List<ResponseBrand>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseReview>>> getReviewsByHashTag(final String hashTag) {
        return Observable.<BaseResponse<ArrayList<ResponseReview>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            String filter_string = "'hashtags eq " + hashTag + "'";
            Call<BaseResponse<ArrayList<ResponseReview>>> call = restApi.getReviewByHashTag(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), filter_string);
            try {
                Response<BaseResponse<ArrayList<ResponseReview>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseBrand>>> getTrandinBrands() {
        return Observable.<BaseResponse<List<ResponseBrand>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            String filter_string = "trendingRateeqtrue";
            Call<BaseResponse<List<ResponseBrand>>> call = restApi.getTrandingBrands(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), filter_string);
            try {
                Response<BaseResponse<List<ResponseBrand>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> followUser(final String id) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ArrayList<String> strings = new ArrayList<String>();
            Call<BaseResponse> call = restApi.followUser(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), id);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> unFollowUser(final String id) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            ArrayList<String> strings = new ArrayList<String>();
            Call<BaseResponse> call = restApi.unFollowUser(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), id);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {

                    parseError(response, subscriber);
                }
                else {
                    subscriber.onError(new Throwable());
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseBrand>>> getSearchBrands(String searchString, String categoryId) {
        return Observable.<BaseResponse<ArrayList<ResponseBrand>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<ArrayList<ResponseBrand>>> call;
            if (categoryId == null) {
                call = restApi.getSearchBrand(
                        userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), searchString);
            }
            else {
                String filter = "subcategoryId eq " + categoryId;
                call = restApi.getSearchBrand(
                        userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), searchString, filter);
            }
            try {
                Response<BaseResponse<ArrayList<ResponseBrand>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ArrayList<ResponseBrand>>> getSuggestedBrand() {
        return Observable.<BaseResponse<ArrayList<ResponseBrand>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<ArrayList<ResponseBrand>>> call;
            String filter = "suggested eq true";
            call = restApi.getSearchBrandFilter(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), filter);
            try {
                Response<BaseResponse<ArrayList<ResponseBrand>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> addLikeReview(final String idReview) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.addLikeReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idReview);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> deleteLike(final String idReview) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.deleteLikeReview(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), idReview);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseNotification>>> getNotifications(String page) {
        return Observable.<BaseResponse<List<ResponseNotification>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseNotification>>> call = restApi.getNotifications(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), page, PAGE_SIZE);
            try {
                Response<BaseResponse<List<ResponseNotification>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseNotificationSettings>> getNotificationsSettings() {
        return Observable.<BaseResponse<ResponseNotificationSettings>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<ResponseNotificationSettings>> call = restApi.getNotificationsSettings(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN));
            try {
                Response<BaseResponse<ResponseNotificationSettings>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse> setNotificationsSettings(final ResponseNotificationSettings responseNotificationSettings) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.setNotificationSettings(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), responseNotificationSettings);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseBrand>>> getFeatureBrand() {
        return Observable.<BaseResponse<List<ResponseBrand>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseBrand>>> call;
            String filter = "'trendOne eq true'";
            call = restApi.getFeatureBrand(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), filter);
            try {
                Response<BaseResponse<List<ResponseBrand>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });

    }

    @Override
    public Observable<BaseResponse> deleteUserFile(final String path) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            int lastSlash = path.lastIndexOf("/") + 1;
            String resultPath = path.substring(lastSlash, path.length());
            Call<BaseResponse> call = restApi.deleteUserFile(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), resultPath, "photo");
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseLogin>> updateToken() {
        UserDataUtils requestLogin = new UserDataUtils(appContext);
        String accessToken = requestLogin.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        String updateToken = requestLogin.getUserData(UserDataUtils.KEY_USER_PROFILE_RERFRESH_TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("refreshToken", updateToken);
        return Observable.<BaseResponse<ResponseLogin>>create(subscriber -> {
            Call<BaseResponse<ResponseLogin>> call = restApi.updateToken(map);
            try {
                Response<BaseResponse<ResponseLogin>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }


    @Override
    public Observable<BaseResponse<ResponsePhotoUrl>> updateReviewsImage(ArrayList<Bitmap> bitmaps) {
        return Observable.<BaseResponse<ResponsePhotoUrl>>create(subscriber -> {
            Map<String, RequestBody> stringRequestBodyMap = new HashMap<String, RequestBody>();
            for(int i = 0; i < bitmaps.size(); i++) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                stringRequestBodyMap.put("file\"; filename=\"" + i + "pp.jpeg\"", RequestBody.create(MediaType.parse("image/jpeg"), byteArray));
            }
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<ResponsePhotoUrl>> call = restApi.uploadReviewPhoto(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN),
                    stringRequestBodyMap);
            try {
                Response<BaseResponse<ResponsePhotoUrl>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseIncomeRange>>> getIncomeRange() {
        return Observable.<BaseResponse<List<ResponseIncomeRange>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseIncomeRange>>> call = restApi.getIncomeRange(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN));
            try {
                Response<BaseResponse<List<ResponseIncomeRange>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseIncomeRange>>> getIncomeRange(String country) {
        return Observable.<BaseResponse<List<ResponseIncomeRange>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            String result = "country eq " + country;
            Call<BaseResponse<List<ResponseIncomeRange>>> call = restApi.getIncomeRange(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), result);
            try {
                Response<BaseResponse<List<ResponseIncomeRange>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseAchievement>>> getAchivmients() {
        return Observable.<BaseResponse<List<ResponseAchievement>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseAchievement>>> call = restApi.getAchivmients(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN),
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_ID));
            try {
                Response<BaseResponse<List<ResponseAchievement>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseAchievement>>> getAllAchivmients() {
        return Observable.<BaseResponse<List<ResponseAchievement>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse<List<ResponseAchievement>>> call = restApi.getAchivmients(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN));
            try {
                Response<BaseResponse<List<ResponseAchievement>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }


    @Override
    public Observable<BaseResponse> updateLocation(RequestLocation location) {
        return Observable.<BaseResponse>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Call<BaseResponse> call = restApi.updateLocation(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN),
                    location);
            try {
                Response<BaseResponse> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<List<ResponseReview>>>
                    getReviewsFromParamentrs(final String brandId, final String rate,
                                             final String page, final boolean withImage,
                                             final boolean withComments) {
        return Observable.<BaseResponse<List<ResponseReview>>>create(subscriber -> {
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            Map<String, String> stringStringMap = new HashMap<>();
            stringStringMap.put("page", page);
            stringStringMap.put("pageSize", PAGE_SIZE);
            if(rate != null)
                stringStringMap.put("filter","'rate eq " + rate + "'");
            if(withComments)
                stringStringMap.put("withComments", String.valueOf(withComments));
            if (withImage)
                stringStringMap.put("withImages", String.valueOf(withImage));
            Call<BaseResponse<List<ResponseReview>>> call = restApi.getReviewsWithParams(
                    userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN), brandId, stringStringMap);
            try {
                Response<BaseResponse<List<ResponseReview>>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {

                    parseError(response, subscriber);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseStatistics>> getBrandReviewStatistics(final String brandId) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse<ResponseStatistics>>create(subscriber -> {
            Call<BaseResponse<ResponseStatistics>> call = restApi.getBrandReviewStatistics(token, brandId);
            try {
                Response<BaseResponse<ResponseStatistics>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    subscriber.onError(null);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    @Override
    public Observable<BaseResponse<ResponseSubCategotry>> getSubactegories(final String subacategoryId) {
        UserDataUtils userDataUtils = new UserDataUtils(appContext);
        String token = userDataUtils.getUserData(UserDataUtils.KEY_USER_PROFILE_TOKEN);
        return Observable.<BaseResponse<ResponseSubCategotry>>create(subscriber -> {
            Call<BaseResponse<ResponseSubCategotry>> call = restApi.getSubcategoryBrands(token, subacategoryId);
            try {
                Response<BaseResponse<ResponseSubCategotry>> response = call.execute();
                if (response.isSuccess()) {
                    subscriber.onNext(response.body());
                    subscriber.onCompleted();
                }
                else {
                    subscriber.onError(null);
                }
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    subscriber.onError(new NetworkErrorException());
                }
                else {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            }
        });
    }

    private void parseError(Response response, Subscriber subscriber) {
        APIError error = ErrorUtils.parseError(response, retrofit);
        if (error.getMessage() == null) {
            subscriber.onError(new NetworkErrorException());
            return;
        }
        if (error.getCode().equals("001-004")) {
            subscriber.onError(new Throwable(error.getCode()));
            UserDataUtils userDataUtils = new UserDataUtils(appContext);
            userDataUtils.clear();
        }
        else
        if (error.getErrorsUser() != null && error.getErrorsUser().size() != 0 &&
                error.getErrorsUser().get(0) != null && error.getErrorsUser().get(0).getMessage() != null)
            subscriber.onError(new Throwable(error.getMessage() + " " + error.getErrorsUser().get(0).getMessage()));
        else
            subscriber.onError(new NetworkErrorException());
    }
}

package com.seyipaye.gadsapp.ui;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seyipaye.gadsapp.ui.main.AppApi;
import com.seyipaye.gadsapp.ui.main.MainRVItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class AppRepository {

    private static AppRepository INSTANCE;

    public static AppRepository getRepository () {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository();
        }
        return INSTANCE;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://gadsapi.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    AppApi appApi = retrofit.create(AppApi.class);

    public void fetchLearningLeaders (Callbacks.Leaders listener) {
        listener.onFetchStarted();

        appApi.fetchLearningLeaders().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Timber.i(response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        Type myType = new TypeToken<List<MainRVItem>>(){}.getType();
                        List<MainRVItem> mainRVItemList = gson.fromJson(response.body().string(), myType);

                        listener.onFetchSuccess(mainRVItemList);

                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onFetchFailure("Oops, An internal error occurred, try again later");
                    }
                } else {
                    listener.onFetchFailure(getError( "fetchLearningLeaders", response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFetchFailure(t.getLocalizedMessage());
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public void fetchSkillIqLeaders (Callbacks.Leaders listener) {
        listener.onFetchStarted();

        appApi.fetchSkillIqLeaders().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        Type myType = new TypeToken<List<MainRVItem>>(){}.getType();
                        List<MainRVItem> mainRVItemList = gson.fromJson(response.body().string(), myType);

                        listener.onFetchSuccess(mainRVItemList);

                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onFetchFailure("Oops, An internal error occurred, try again later");
                    }
                } else {
                    listener.onFetchFailure(getError( "fetchLearningLeaders", response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFetchFailure(t.getLocalizedMessage());
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public void sendForm(String fName, String lName, String email, String githubLink, Callbacks.Form listener) {
        //listener.onPingStarted();
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(" https://docs.google.com/forms/d/e/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AppApi appApi2 = retrofit2.create(AppApi.class);

        appApi2.sendForm(fName, lName, email, githubLink).enqueue(new Callback<Void>() {            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess("Submission Successful");
                } else {
                    listener.onFailure("Submission not Successful");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailure(t.getLocalizedMessage());
            }
        });
    }

   /* 225.971ms
    I/ContentValues: onFetchStarted:
    I/ContentValues: onFetchStarted:
    D/EGL_emulation: eglMakeCurrent: 0xdfdff280: ver 3 0 (tinfo 0xe603abf0)
    I/ContentValues: onResponse: Response{protocol=h2, code=200, message=, url=https://docs.google.com/forms/d/e/1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse}
        onResponse: E znull

        09/11 20:13:47: Applying changes to 'app' on 9.0 Correct Pixel 3a.*/


        public String getError(String methodName, ResponseBody errorBody) {
        JSONObject errorJson = null;
        try {
            String errorString = errorBody.string();
            Log.e(TAG, String.format("%s: %s", methodName, errorString));
            errorJson = new JSONObject(errorString);
            JSONObject error = errorJson.getJSONObject("error");
            return error.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Oops, An internal error occurred, try again later";
        } catch (JSONException e) {
            if (errorJson != null) {
                try {
                    return errorJson.getString("error");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    return "Oops, An internal error occurred, try again later";
                }
            } else {
                e.printStackTrace();
                return "Oops, An internal error occurred, try again later";
            }
        }
    }
}

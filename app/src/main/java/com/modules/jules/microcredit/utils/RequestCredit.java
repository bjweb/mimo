package com.modules.jules.microcredit.utils;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public final class RequestCredit {

    public interface OnParsingState{
        public void onErrorParse(String cause);
        public void onError(String e);
    }

    public interface OnResquestJsonObjectListener extends OnParsingState{
        public void onResult(boolean bool, String message);
    }


    private static RequestCredit instance = new RequestCredit();


    private final String ETAT   ="state";
    private final String MESSAGE="msg";

    public RequestCredit(){}

    public static RequestCredit getInstance(){
        return instance;
    }

    public void requestJsonObjectWithParams(final Context context, String url, final Map<String,String> params,
                                            final OnResquestJsonObjectListener onResquestJsonObjectListener){

        if(params==null){
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,
                url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    onResquestJsonObjectListener.onResult(object.getBoolean(ETAT),object.getString(MESSAGE));

                } catch (JSONException e) {
                    onResquestJsonObjectListener.onError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResquestJsonObjectListener.onError(error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(request);

    }

    public void requestJsonObjectWithParams(final Activity activity, String url, final Map<String,String> params,
                                            final OnResquestJsonObjectListener onResquestJsonObjectListener){

        if(params==null){
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST,
                url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    onResquestJsonObjectListener.onResult(object.getBoolean(ETAT),object.getString(MESSAGE));

                } catch (JSONException e) {
                    onResquestJsonObjectListener.onError(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResquestJsonObjectListener.onError(error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(request);

    }

    public void requestJsonObject(final Activity activity, String url,
                                  final OnResquestJsonObjectListener onResquestJsonObjectListener){

        StringRequest request = new StringRequest(Request.Method.POST,
                url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    onResquestJsonObjectListener.onResult(object.getBoolean(ETAT),object.getString(MESSAGE));

                } catch (JSONException e) {
                    onResquestJsonObjectListener.onErrorParse(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResquestJsonObjectListener.onError(error.getMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        queue.add(request);

    }

}
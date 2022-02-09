package com.example.admd_hw2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class serverService extends Service implements Serializable {


    public serverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
    return myBinder;
    }
    String strReturn="";



    serverInterface.Stub myBinder=new serverInterface.Stub() {

        @Override
        public String getMovies() throws RemoteException {


            RequestQueue requestQueue;
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);

            requestQueue.start();

            String url ="http://10.0.2.2:8080";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            strReturn=response;

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            strReturn=error.toString();
                        }
                    });

          requestQueue.add(stringRequest);

            return strReturn;
        }//end getMovies

        @Override
        public void postRating(String title,String rating) throws RemoteException{
            String url="http://10.0.2.2:8080/";
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {}
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String,String> getParams()throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("star",rating);
                    params.put("title",title);
                    return params;
                }
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError{
                    Map<String,String> params=new HashMap<>();
                    params.put("Content-type","application/x-www-form-urlencoded");
                    return params;
                }
            };

            RequestQueue requestQueue;

            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
            requestQueue.add(stringRequest);
        }//end postRating


    };
}
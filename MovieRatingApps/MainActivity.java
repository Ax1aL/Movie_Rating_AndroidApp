package com.example.admd_hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    serverInterface myInterface;
    TextView textView;
    MovieListView adapter;
    ListView listView;
    ArrayList<String> Titles=new ArrayList<String>();
    ArrayList<String> Description=new ArrayList<String>();
    ArrayList<Drawable> Images=new ArrayList<Drawable>();
    ArrayList<String> MovieDetails=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent svrService=new Intent(MainActivity.this,serverService.class);

        bindService(svrService,myServiceConnection, Context.BIND_AUTO_CREATE);



        textView=(TextView)findViewById(R.id.text);
        Button btn=(Button)findViewById(R.id.button);
        listView=(ListView)findViewById(R.id.movieList);

        //tempImg.add(getDrawable(R.drawable.ic_launcher_background));
        adapter=new MovieListView(this,Titles,Description,Images);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    proccessJson(myInterface.getMovies());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
          Intent intent=new Intent(MainActivity.this, MovieDetails.class).putExtra("Movie",MovieDetails.get(position));
          startActivity(intent);

        });


    }



    ServiceConnection myServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myInterface=serverInterface.Stub.asInterface(service);


            try {
                textView.setText(myInterface.getMovies());
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }//end

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    void proccessJson(String str){

        try {
            JSONArray json=new JSONArray(str);
            Titles.clear();
            Description.clear();
            Images.clear();
            MovieDetails.clear();
            for(int i=0;i<json.length();++i){
                JSONObject jsonObj=new JSONObject(json.get(i).toString());
                MovieDetails.add(json.get(i).toString());
                Titles.add(jsonObj.get("title").toString());
                Description.add(jsonObj.get("description").toString());
                Images.add(getDrawable(R.drawable.ic_launcher_background));
            }


        } catch (JSONException e) {

        }

    }

}
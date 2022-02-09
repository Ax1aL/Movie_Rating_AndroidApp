package com.example.admd_hw2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class MovieDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    serverInterface myInterface;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    int rating=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_movie);



        Intent svrService=new Intent(MovieDetails.this,serverService.class);

        bindService(svrService,myServiceConnection, Context.BIND_AUTO_CREATE);

        String starString="";
        float starMedian=0.0f;

        Spinner spinner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.STARS, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        TextView txtTitle=(TextView)findViewById(R.id.Title);
        TextView txtDescription=(TextView)findViewById(R.id.Description);
        TextView txtDetails=(TextView)findViewById(R.id.Details);
        TextView txtStars=(TextView)findViewById(R.id.stars);
        ImageView img=(ImageView) findViewById(R.id.imageView);
        Button btn=(Button)findViewById(R.id.RATE);


        String jsonDetails="";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            jsonDetails = extras.getString("Movie");




        try {
            JSONObject temp=new JSONObject(jsonDetails.toString());
            txtTitle.setText(temp.get("title").toString());
            txtDescription.setText("Description:\n"+temp.get("description").toString());
            txtDetails.setText("Director(s):"+temp.get("director").toString()+"\nActors:"+temp.get("actors").toString());
            starString=temp.get("stars").toString();
            ((TextView)findViewById(R.id.length)).setText("Year:"+temp.get("year").toString()+"--Length:"+temp.get("length").toString());

            img.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<starString.length();++i){
            starMedian+=starString.charAt(i)-48;
        }
        starMedian=starMedian/starString.length();
        txtStars.setText(String.valueOf(df.format(starMedian)));



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MovieDetails.this, "Thanks for rating!", Toast.LENGTH_LONG).show();
                try {
                    myInterface.postRating(txtTitle.getText().toString(),String.valueOf(rating));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


    }
    ServiceConnection myServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myInterface=serverInterface.Stub.asInterface(service);

        }//end

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rating=position+1;
        Toast.makeText(MovieDetails.this, String.valueOf(rating), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

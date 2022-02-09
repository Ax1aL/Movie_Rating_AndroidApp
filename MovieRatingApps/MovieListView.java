package com.example.admd_hw2;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieListView extends ArrayAdapter<String> {

    private final Activity context;
    private final  ArrayList<String> maintitle;
    private final  ArrayList<String> description;
    private final  ArrayList<Drawable> imgid;

    public MovieListView(Activity context, ArrayList<String> maintitle,ArrayList<String> description, ArrayList<Drawable> imgid) {
        super(context, R.layout.movie_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.imgid=imgid;
        this.description=description;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.movie_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView descText = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        titleText.setText(maintitle.get(position));
        descText.setText(description.get(position));
        imageView.setImageDrawable(imgid.get(position));

        return rowView;

    };
}

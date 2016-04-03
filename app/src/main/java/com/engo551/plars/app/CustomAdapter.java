package com.engo551.plars.app;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Xueyang Zou on 2016-03-17.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    public CustomAdapter(Context context, String [] resNames) {
        super(context, R.layout.custom_row, resNames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*getView is: for the strings that we pass to it, this is how
        * we would like it to layout*/

        LayoutInflater ListviewInflator = LayoutInflater.from(getContext());
        View customView = ListviewInflator.inflate(R.layout.custom_row, parent, false);

        /*get a reference to the string of 'res' defined in ListActivity.java*/
        String singleResItem = getItem(position);
        TextView ResText = (TextView) customView.findViewById(R.id.CustomText);
        ImageView ResImage = (ImageView) customView.findViewById(R.id.imageView);

        /*set the content of CustomText and imageView*/
        ResText.setText(singleResItem);
        ResImage.setImageResource(R.drawable.images);
        return customView;


    }
}

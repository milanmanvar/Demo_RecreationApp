package com.milan.recreationapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.milan.recreationapp.R;
import com.milan.recreationapp.model.ClubModel_New;
import com.milan.recreationapp.view.ClubInfoActivity;
import com.milan.recreationapp.view.OurClubActivity;

import java.util.ArrayList;

/**
 * Created by utsav.k on 28-03-2016.
 */
public class ClubLocationListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ClubModel_New> listItem;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private int tempPos = -1;

    public ClubLocationListAdapter(Context context,
                                   ArrayList<ClubModel_New> items) {
        this.context = context;
        this.listItem = items;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public ClubModel_New getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_club_location_list, null);
            holder.txtName = (TextView) convertView
                    .findViewById(R.id.row_club_location_txtName);
            holder.imgInfo = (ImageView) convertView
                    .findViewById(R.id.row_club_location_imgInfo);
            holder.imgHour = (ImageView) convertView
                    .findViewById(R.id.row_club_location_imgHour);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(position == listItem.size()-1){
            holder.imgInfo.setVisibility(View.INVISIBLE);
            holder.imgHour.setVisibility(View.INVISIBLE);
        }else{
            holder.imgInfo.setVisibility(View.VISIBLE);
            holder.imgHour.setVisibility(View.VISIBLE);
        }

        holder.txtName.setText(listItem.get(position).getName());
        if (listItem.get(position).is24Hour())
            holder.imgHour.setVisibility(View.VISIBLE);
        else
            holder.imgHour.setVisibility(View.INVISIBLE);




        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == listItem.size()-1) {

                    ((OurClubActivity) context).mapZoomOut();
                }else{
                    ((OurClubActivity) context).changeLocation(listItem.get(position).getLat(), listItem.get(position).getLng(), listItem.get(position).getName());
                }


            }
        });
        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDetail = new Intent(context, ClubInfoActivity.class);
                iDetail.putExtra("clubdata", listItem.get(position));
                context.startActivity(iDetail);
            }
        });
        return convertView;
    }


    public class ViewHolder {
        private TextView txtName;
        private ImageView imgInfo, imgHour;
    }

}

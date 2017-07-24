package com.mainframevampire.shift;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Shift> mShifts;
    private Context mContext;

    public Adapter(Context context, List<Shift> shifts) {
        mContext = context;
        mShifts = shifts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shift_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new BindViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BindViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mShifts.size();
    }

    public class BindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        private TextView mStartDateAndTime;
        private TextView mStartLocation;

        private TextView mEndDateAndTime;
        private TextView mEndLocation;
        private Shift mShift;

        private LinearLayout mDetailShiftLayout;

        
        public BindViewHolder(View itemView){
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.imageView);

            mStartDateAndTime = (TextView) itemView.findViewById(R.id.startDateAndTime);
            mStartLocation = (TextView) itemView.findViewById(R.id.startLocation);

            mEndDateAndTime = (TextView) itemView.findViewById(R.id.endDateAndTime);
            mEndLocation = (TextView) itemView.findViewById(R.id.endLocation);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position) {
            mShift = mShifts.get(position);
            Picasso.with(mContext).load(mShifts.get(position).getImage()).into(mImageView);
            String startDate = getDate(mShifts.get(position).getStart());
            String startTime = getTime(mShifts.get(position).getStart());
            mStartDateAndTime.setText(startDate + " " + startTime);

            Double dbStartLatitude = Double.parseDouble(mShifts.get(position).getStartLatitude());
            Double dbStartLongitude = Double.parseDouble(mShifts.get(position).getStartLongitude());
            if (isValidLatLng(dbStartLatitude, dbStartLongitude)) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(mContext, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(dbStartLatitude,dbStartLongitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    mStartLocation.setText(address + ","+ city + " "+ state + "," + postalCode + " "+ country);
                } catch (IOException e) {
                    e.printStackTrace();
                    mStartLocation.setText("Location not available");
                }
            } else {
                mStartLocation.setText("Location not available");
            }

            String endDate = getDate(mShifts.get(position).getEnd());
            String endTime = getTime(mShifts.get(position).getEnd());
            mEndDateAndTime.setText(endDate + " " + endTime );

            Double dbEndLatitude = Double.parseDouble(mShifts.get(position).getEndLatitude());
            Double dbEndLongitude = Double.parseDouble(mShifts.get(position).getEndLongitude());
            if (isValidLatLng(dbEndLatitude, dbEndLongitude)) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(mContext, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(dbEndLatitude,dbEndLongitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    mEndLocation.setText(address + ","+ city + " "+ state + "," + postalCode + " "+ country);
                } catch (IOException e) {
                    e.printStackTrace();
                    mEndLocation.setText("Location not available");
                }
            } else {
                mEndLocation.setText("Location not available");
            }
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DetailActivity.SHIFT_DETAIL, mShift);
            mContext.startActivity(intent);
        }
    }

    private String getDate(String dateAndTime) {
        return dateAndTime.substring(0, 10);
    }

    private String getTime(String dateAndTime) {
        return dateAndTime.substring(11, 19);
    }

    public boolean isValidLatLng(double lat, double lng){
        if(lat < -90 || lat > 90)
        {
            return false;
        }
        else if(lng < -180 || lng > 180)
        {
            return false;
        }
        return true;
    }


}

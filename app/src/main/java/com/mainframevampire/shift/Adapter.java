package com.mainframevampire.shift;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
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
import com.mainframevampire.shift.data.model.ShiftDetail;
import com.mainframevampire.shift.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnLoadListener {
        void onLoadData();
    }
    private ArrayList<ShiftDetail> mShiftDetails;
    private Context mContext;

    private OnLoadListener mOnloadListener;

    public Adapter(Context context, ArrayList<ShiftDetail>  shiftdetails, RecyclerView recyclerView) {
        mContext = context;
        mShiftDetails = shiftdetails;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean isUserScrolling = false;
                private boolean isListGoingUp = true;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //check if the top most is visible and user is scrolling
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        isUserScrolling = true;
                        if (isListGoingUp) {
                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isListGoingUp) {
                                            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                                if (mOnloadListener != null) {
                                                    mOnloadListener.onLoadData();
                                                }
                                            }
                                        }
                                    }
                                },50);
                            }
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isUserScrolling) {
                        if (dy > 0) {
                            isListGoingUp = false;
                        }
                        else {
                            isListGoingUp = true;
                        }
                    }
                }
            });
        }
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
        return mShiftDetails.size();
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
            mShift = new Shift(
                    mShiftDetails.get(position).getId(),
                    mShiftDetails.get(position).getStart(),
                    mShiftDetails.get(position).getEnd(),
                    mShiftDetails.get(position).getStartLatitude(),
                    mShiftDetails.get(position).getStartLongitude(),
                    mShiftDetails.get(position).getEndLatitude(),
                    mShiftDetails.get(position).getEndLongitude(),
                    mShiftDetails.get(position).getImage());

            Picasso.with(mContext).load(mShiftDetails.get(position).getImage()).into(mImageView);
            String startDate = getDate(mShiftDetails.get(position).getStart());
            String startTime = getTime(mShiftDetails.get(position).getStart());
            mStartDateAndTime.setText(startDate + " " + startTime);

            String endDate = getDate(mShiftDetails.get(position).getEnd());
            String endTime = getTime(mShiftDetails.get(position).getEnd());
            mEndDateAndTime.setText(endDate + " " + endTime );

            switch (mShiftDetails.get(position).getStartLocationStatus()) {
                case "0":
                    String address = mShiftDetails.get(position).getStartAddress();
                    String city = mShiftDetails.get(position).getStartCity();
                    String state = mShiftDetails.get(position).getStartState();
                    String country = mShiftDetails.get(position).getStartCountry();
                    String postalCode = mShiftDetails.get(position).getStartPostcode();
                    mStartLocation.setText(address + "," + city + " " + state + "," + postalCode + " " + country);
                    break;
                case "1":
                    mStartLocation.setText("Location not available");
                    break;
                case "2":
                    mStartLocation.setText("cound't find location without network");
                    break;
                case "3":
                    mStartLocation.setText("downloading location");
            }

            switch (mShiftDetails.get(position).getEndLocationStatus()) {
                case "0":
                    String address = mShiftDetails.get(position).getEndAddress();
                    String city = mShiftDetails.get(position).getEndCity();
                    String state = mShiftDetails.get(position).getEndState();
                    String country = mShiftDetails.get(position).getEndCountry();
                    String postalCode = mShiftDetails.get(position).getEndPostcode();
                    mEndLocation.setText(address + "," + city + " " + state + "," + postalCode + " " + country);
                    break;
                case "1":
                    mEndLocation.setText("Location not available");
                    break;
                case "2":
                    mEndLocation.setText("cound't find location without network");
                    break;
                case "3":
                    mEndLocation.setText("downloading location");
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

    public void updateData(ArrayList<ShiftDetail> shiftDetails) {
        mShiftDetails = shiftDetails;
    }

    public void setOnLoadListener(OnLoadListener onloadListener) {
        mOnloadListener = onloadListener;
    }

}

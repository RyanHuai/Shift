package com.mainframevampire.shift;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        private TextView mStartStreet;
        private TextView mStartState;
        private TextView mStartCountry;

        private TextView mEndDateAndTime;
        private TextView mEndStreet;
        private TextView mEndState;
        private TextView mEndCountry;

        
        public BindViewHolder(View itemView){
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.imageView);

            mStartDateAndTime = (TextView) itemView.findViewById(R.id.startDateAndTime);
            mStartStreet = (TextView) itemView.findViewById(R.id.startStreet);
            mStartState = (TextView) itemView.findViewById(R.id.startState);
            mStartCountry = (TextView) itemView.findViewById(R.id.startCountry);

            mEndDateAndTime = (TextView) itemView.findViewById(R.id.endDateAndTime);
            mEndStreet = (TextView) itemView.findViewById(R.id.endStreet);
            mEndState = (TextView) itemView.findViewById(R.id.endState);
            mEndCountry = (TextView) itemView.findViewById(R.id.endCountry);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position) {

            Picasso.with(mContext).load(mShifts.get(position).getImage()).into(mImageView);
            String startDate = getDate(mShifts.get(position).getStart());
            String startTime = getTime(mShifts.get(position).getStart());
            mStartDateAndTime.setText(startDate + " " + startTime);
            //mStartStreet
            //mStartState
            //mStartCountry
            String endDate = getDate(mShifts.get(position).getEnd());
            String endTime = getTime(mShifts.get(position).getEnd());
            mEndDateAndTime.setText(endDate + " " + endTime );
        }


        @Override
        public void onClick(View v) {

        }
    }

    private String getDate(String dateAndTime) {
        return dateAndTime.substring(0, 10);
    }

    private String getTime(String dateAndTime) {
        return dateAndTime.substring(11, 19);
    }


}

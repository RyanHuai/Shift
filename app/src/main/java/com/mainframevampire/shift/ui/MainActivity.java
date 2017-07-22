package com.mainframevampire.shift.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mainframevampire.shift.Adapter;
import com.mainframevampire.shift.R;
import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.InputShift;
import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.data.remote.APIService;
import com.mainframevampire.shift.data.remote.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String dateString = "";
    private APIService mAPIService;

    //view variable
    private TextView mOfflineModeLabel;
    private ImageView mBusinessImageView;
    private TextView mBusinessText;

    private LinearLayout mCurrentShiftLayout;
    private ImageView mCurrentImageView;
    private TextView mCurrentDateAndTime;
    private TextView mCurrentLocation;
    private Button mEndButton;

    private Button mStartButton;

    private RelativeLayout mStartEndLayout;
    private TextView mDate;
    private Button mDateButton;
    private TextView mTime;
    private Button mTimeButton;
    private EditText mRoad;
    private EditText mSuburb;
    private EditText mCity;
    private EditText mPostcode;
    private EditText mState;
    private EditText mCountry;
    private Button mCancelButton;
    private Button mConfirmButton;

    private boolean mShowStartLayout = true;

    private RecyclerView mRecyclerView;
    private List<Shift> mShifts;
    private Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOfflineModeLabel = (TextView) findViewById(R.id.offlineModeLabel);
        mBusinessImageView = (ImageView) findViewById(R.id.businessImage);
        mBusinessText = (TextView) findViewById(R.id.businessText);



        mCurrentShiftLayout = (LinearLayout) findViewById(R.id.currentShiftLayout);
        mCurrentImageView = (ImageView) findViewById(R.id.currentImageView);
        mCurrentDateAndTime = (TextView) findViewById(R.id.currentDateAndTime);
        mCurrentLocation = (TextView) findViewById(R.id.currentLocation);
        mEndButton = (Button) findViewById(R.id.endButton);

        mStartButton = (Button) findViewById(R.id.startButton);

        mStartEndLayout = (RelativeLayout) findViewById(R.id.startEndLayout);
        mDate = (TextView) findViewById(R.id.DateText);
        mDateButton = (Button) findViewById(R.id.dateButton);
        mTime = (TextView) findViewById(R.id.TimeText);
        mTimeButton = (Button) findViewById(R.id.timeButton);
        mRoad = (EditText) findViewById(R.id.editRoadText);
        mSuburb = (EditText) findViewById(R.id.editSuburbText);
        mCity = (EditText) findViewById(R.id.editCityText);
        mPostcode = (EditText) findViewById(R.id.editPostcodeText);
        mState = (EditText) findViewById(R.id.editStateText);
        mCountry = (EditText) findViewById(R.id.editCountryText);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mConfirmButton = (Button) findViewById(R.id.confirmButton);
        mStartEndLayout.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        if (!isNetworkAvailable()) {
            mOfflineModeLabel.setVisibility(View.VISIBLE);
            //TODO Load from local database
        } else {
            mOfflineModeLabel.setVisibility(View.GONE);
            mAPIService = ApiUtils.getAPIService();
            //load from Deputy web
            loadBusinessFromWeb();
            loadShiftsFromWeb();
        }

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartEndEditLayout();
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartEndEditLayout();
            }
        });

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });


        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecyclerViewLayout();
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isAllFieldsInput = checkInput();
                if(isAllFieldsInput) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("information not completed")
                            .setMessage("Please input all the fields");
                    builder.create().show();
                }

            }
        });

        //mAPIService = ApiUtils.getAPIService();

//        loadBusinessFromWeb();
//        loadShiftsFromWeb();
//        InputShift inputShift = new InputShift(
//                "2017-01-17T06:35:57+00:00",
//                "-23.832270",
//                "5555.084356"
//        );
//        startShift(inputShift);
//
//        InputShift inputEndShift = new InputShift(
//                "2017-01-24T06:35:57+00:00",
//                "-23.832270",
//                "5555.084356"
//        );
//        endShift(inputEndShift);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
            mOfflineModeLabel.setVisibility(View.VISIBLE);
        } else {
            mOfflineModeLabel.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void loadBusinessFromWeb() {
        mAPIService.saveBusiness().enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                if(response.isSuccessful()) {
                    Business business = response.body();
                    mBusinessText.setText(business.getName());
                    Picasso.with(MainActivity.this).load(business.getLogo()).into(mBusinessImageView);
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveBusiness to API.");
            }
        });
    }

    private void loadShiftsFromWeb() {
        mAPIService.saveShifts().enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(Call<List<Shift>> call, Response<List<Shift>> response) {
                if(response.isSuccessful()) {
                    if (mShifts != null) {
                        mShifts = response.body();
                        Collections.sort(mShifts);
                        if (mShifts.get(0).getEnd().equals("")) {
                            //shift in progress
                            mShifts.remove(0);
                            mShowStartLayout = false;
                            showCurrentShiftLayout();
                            Picasso.with(MainActivity.this).load(mShifts.get(0).getImage()).into(mCurrentImageView);
                            String startDate = mShifts.get(0).getStart().substring(0, 10);
                            String startTime = mShifts.get(0).getStart().substring(11, 19);
                            mCurrentDateAndTime.setText(startDate + " " + startTime);
                        } else {
                            mShowStartLayout = true;
                            showStartButton();
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mAdapter = new Adapter(MainActivity.this, mShifts);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mShowStartLayout = true;
                        showStartButton();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Shift>> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveShifts to API.");
            }
        });
    }

    private void startShift(InputShift inputShift) {
        mAPIService.saveStartShift(inputShift).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "saveStartShift:" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveStartShift to API.");
            }
        });
    }

    private void endShift(InputShift inputEndShift) {
        mAPIService.saveEndShift(inputEndShift).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "saveStartShift:" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveStartShift to API.");
            }
        });
    }

    private void showStartButton() {
        mCurrentShiftLayout.setVisibility(View.GONE);
        mStartButton.setVisibility(View.VISIBLE);
    }

    private void showCurrentShiftLayout() {
        mCurrentShiftLayout.setVisibility(View.VISIBLE);
        mStartButton.setVisibility(View.GONE);
    }

    private void showStartEndEditLayout() {
        mStartEndLayout.setVisibility(View.VISIBLE);
        mStartButton.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showRecyclerViewLayout() {
        mRecyclerView.setVisibility(View.VISIBLE);
        if (mShowStartLayout) {
            mStartButton.setVisibility(View.VISIBLE);
            mCurrentShiftLayout.setVisibility(View.GONE);
            mStartEndLayout.setVisibility(View.GONE);
        } else {
            mStartButton.setVisibility(View.GONE);
            mCurrentShiftLayout.setVisibility(View.VISIBLE);
            mStartEndLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String yearString = String.valueOf(year);
        String monthString = String.valueOf(month+1);
        String formattedMonth = ("00" + monthString).substring(monthString.length());
        String dayString = String.valueOf(dayOfMonth);
        String formattedday = ("00" + dayString).substring(dayString.length());

        mDate.setText(yearString + "-" + formattedMonth + "-" + formattedday);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourString = String.valueOf(hourOfDay);
        String minuteString = String.valueOf(minute);
        String formattedHour = ("00" + hourString).substring(hourString.length());
        String formattedMinute = ("00" + minuteString).substring(minuteString.length());
        mTime.setText(formattedHour + ":" + formattedMinute);
    }

    private Boolean checkInput() {
        Boolean isAllFieldsInput = false;
        if (mDate.getText().toString().trim().length() != 0 &&
                mTime.getText().toString().trim().length() != 0 &&
                mRoad.getText().toString().trim().length() != 0 &&
                mSuburb.getText().toString().trim().length() != 0 &&
                mCity.getText().toString().trim().length() != 0 &&
                mPostcode.getText().toString().trim().length() != 0 &&
                mState.getText().toString().trim().length() != 0 &&
                mCountry.getText().toString().trim().length() != 0 ) {
            isAllFieldsInput = true;
        }
        return isAllFieldsInput;
    }
}

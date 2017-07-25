package com.mainframevampire.shift.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mainframevampire.shift.Adapter;
import com.mainframevampire.shift.R;
import com.mainframevampire.shift.data.model.Business;
import com.mainframevampire.shift.data.model.InputShift;
import com.mainframevampire.shift.data.model.Shift;
import com.mainframevampire.shift.data.model.ShiftDetail;
import com.mainframevampire.shift.data.remote.APIService;
import com.mainframevampire.shift.data.remote.ApiUtils;
import com.mainframevampire.shift.database.ShiftsDataSource;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SHIFTS = "SHIFTS";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String BUSINESS_NAME = "BUSINESS_NAME";
    public static final String BUSINESS_LOGO = "BUSINESS_LOGO";
    public static final String SHIFT_DETAIL = "SHIFT_DETAIL";
    public static final String BROADCAST_ACTION = "com.mainframevampire.shift.BROADCAST";
    public static final String KEY_MESSAGE = "com.mainframevampire.shift.MESSAGE";
    public static final String MESSAGE_SOURCE = "MESSAGE_SOURCE";
    private APIService mAPIService;

    //view variable
    private TextView mOfflineModeLabel;
    private TextView mRemindingLabel;

    private LinearLayout mCurrentShiftLayout;
    private ImageView mCurrentImageView;
    private TextView mCurrentDateAndTime;
    private TextView mCurrentLocation;
    private Button mEndButton;

    private Button mStartButton;

    private ScrollView mStartEndLayout;
    private TextView mLayoutLabel;
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

    private ProgressBar mLoadingProgressBar;

    private boolean mShowStartLayout = true;
    private boolean mIsStart = true;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ArrayList<ShiftDetail> mShiftDetails;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location currentBestLocation = null;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(BROADCAST_ACTION));

        //todo: use butterknife
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();

        mOfflineModeLabel = (TextView) findViewById(R.id.offlineModeLabel);
        mRemindingLabel = (TextView) findViewById(R.id.remindingLabel);

        mCurrentShiftLayout = (LinearLayout) findViewById(R.id.currentShiftLayout);
        mCurrentImageView = (ImageView) findViewById(R.id.currentImageView);
        mCurrentDateAndTime = (TextView) findViewById(R.id.currentDateAndTime);
        mCurrentLocation = (TextView) findViewById(R.id.currentLocation);
        mEndButton = (Button) findViewById(R.id.endButton);

        mStartButton = (Button) findViewById(R.id.startButton);

        mStartEndLayout = (ScrollView) findViewById(R.id.startEndLayout);
        mLayoutLabel = (TextView) findViewById(R.id.layoutLabel);
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

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mShiftDetails = new ArrayList<>();

        showFirstTimeView();
        if (!isNetworkAvailable()) {
            mOfflineModeLabel.setVisibility(View.VISIBLE);
            ShiftsDataSource dataSource = new ShiftsDataSource(this);
            int businessCount = dataSource.GetBusinessTableCount();
            int shiftDetailsCount = dataSource.GetShiftsTableCount();
            if (businessCount == 0 && shiftDetailsCount == 0) {
                showNetworkNotAvailableDialog(
                        "Network not available",
                        "Please turn on your network");
                mRemindingLabel.setVisibility(View.VISIBLE);
            } else {
                mRemindingLabel.setVisibility(View.GONE);
                loadBusinessFromLocalDatabase();
                loadShiftDetailFromLocalDatabase();
            }
        } else {
            mOfflineModeLabel.setVisibility(View.GONE);
            mAPIService = ApiUtils.getAPIService();
            //load business info
            ShiftsDataSource dataSource = new ShiftsDataSource(this);
            int count = dataSource.GetBusinessTableCount();
            if (count == 0) {
                loadBusinessFromWeb();
            } else {
                loadBusinessFromLocalDatabase();
            }
            //load shifts
            loadShiftsFromWeb();
        }

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    mIsStart = true;
                    showStartEndEditLayout(mIsStart);
                    setCurrentTimeAndLocation();
                } else {
                    showNetworkNotAvailableDialog(
                            "Network not available",
                            "Please turn on your network");
                }
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    mEndButton.setEnabled(false);
                    mIsStart = false;
                    showStartEndEditLayout(mIsStart);
                    setCurrentTimeAndLocation();
                } else {
                    showNetworkNotAvailableDialog(
                            "Network not available",
                            "Please turn on your network");
                }
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
                mEndButton.setEnabled(true);
                showRecyclerViewLayout();
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isAllFieldsInput = checkInput();
                if (isAllFieldsInput) {
                    new ProcessShiftInBackgound().execute();
                } else {
                    //todo: show which field is not valid
                    showNetworkNotAvailableDialog(
                            "information not completed",
                            "Please input all the fields");
                }

            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ShiftDetail shiftDetail = intent.getParcelableExtra(KEY_MESSAGE);
            String messageSource = intent.getStringExtra(MESSAGE_SOURCE);
            Log.d("receiver id ", shiftDetail.getId() + "");
            Log.d("receiver message ", messageSource);
            if (messageSource.equals("LIST")) {
                for (int i = 0; i < mShiftDetails.size(); i++) {
                    if (mShiftDetails.get(i).getId() == shiftDetail.getId()) {
                        mShiftDetails.get(i).setStartAddress(shiftDetail.getStartAddress());
                        mShiftDetails.get(i).setStartCity(shiftDetail.getStartCity());
                        mShiftDetails.get(i).setStartState(shiftDetail.getStartState());
                        mShiftDetails.get(i).setStartCountry(shiftDetail.getStartCountry());
                        mShiftDetails.get(i).setStartPostcode(shiftDetail.getStartPostcode());
                        mShiftDetails.get(i).setStartLocationStatus(shiftDetail.getStartLocationStatus());
                        mShiftDetails.get(i).setEndAddress(shiftDetail.getEndAddress());
                        mShiftDetails.get(i).setEndCity(shiftDetail.getEndCity());
                        mShiftDetails.get(i).setEndState(shiftDetail.getEndState());
                        mShiftDetails.get(i).setEndCountry(shiftDetail.getEndCountry());
                        mShiftDetails.get(i).setEndPostcode(shiftDetail.getEndPostcode());
                        mShiftDetails.get(i).setEndLocationStatus(shiftDetail.getEndLocationStatus());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } else if (messageSource.equals("CURRENT")) {
                switch (shiftDetail.getStartLocationStatus()) {
                    case "0":
                        mCurrentLocation.setText(
                                shiftDetail.getStartAddress() + "," +
                                        shiftDetail.getStartCity() + " " +
                                        shiftDetail.getStartState() + "," +
                                        shiftDetail.getStartPostcode() + " " +
                                        shiftDetail.getStartCountry());
                        break;
                    case "1":
                        mCurrentLocation.setText("Location not available");
                        break;
                    case "2":
                        mCurrentLocation.setText("cound't find location without network");
                        break;
                    case "3":
                        mCurrentLocation.setText("downloading location");
                }
            }

            //start LoadTableService to load data in table
            Intent loadIntent = new Intent(MainActivity.this, LoadTableService.class);
            loadIntent.putExtra(TABLE_NAME, "SHIFTS");
            loadIntent.putExtra(SHIFT_DETAIL, shiftDetail);
            startService(loadIntent);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNetworkAvailable()) {
            mOfflineModeLabel.setVisibility(View.VISIBLE);
        } else {
            mOfflineModeLabel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private class ProcessShiftInBackgound extends AsyncTask<Void, Integer, Void> {
        InputShift inputShift;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Products'information needs to be downloaded for app's first run");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            inputShift = validateAndFormatInput();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (inputShift != null) {
                if (isNetworkAvailable()) {
                    if (mIsStart) {
                        startShift(inputShift);
                    } else {
                        endShift(inputShift);
                    }
                } else {
                    showNetworkNotAvailableDialog(
                            "Network not available",
                            "Please turn on your network");
                }
            } else {
                //todo: show which field is not valid
                showNetworkNotAvailableDialog(
                        "Address fields not valid",
                        "Please make sure all address fields are valid");
            }
            progressDialog.dismiss();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void loadBusinessFromWeb() {
        mAPIService.saveBusiness().enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                if (response.isSuccessful()) {
                    Business business = response.body();
                    //start LoadTableService to load data in table
                    Intent intent = new Intent(MainActivity.this, LoadTableService.class);
                    intent.putExtra(TABLE_NAME, "BUSINESS");
                    intent.putExtra(BUSINESS_NAME, business.getName());
                    intent.putExtra(BUSINESS_LOGO, business.getLogo());
                    startService(intent);
                    //set action bar
                    SetBarIconInBackGround task = new SetBarIconInBackGround();
                    task.execute(business.getLogo());
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveBusiness to API.");
            }
        });
    }

    private void loadBusinessFromLocalDatabase() {
        ShiftsDataSource dataSource = new ShiftsDataSource(this);
        int count = dataSource.GetBusinessTableCount();
        //set action bar
        if (count != 0) {
            Business business = dataSource.readBusinessTable();
            if (isNetworkAvailable()) {
                SetBarIconInBackGround task = new SetBarIconInBackGround();
                task.execute(business.getLogo());
            } else {
                setTitle(business.getName());
            }
        } else {
            showNetworkNotAvailableDialog(
                    "business info not downloaded",
                    "Please open your network to download business info");
        }

    }

    private class SetBarIconInBackGround extends AsyncTask<String, Void, Void> {
        Bitmap bitmap;

        @Override
        protected Void doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setLogo(drawable);

        }
    }

    private void loadShiftsFromWeb() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mAPIService.saveShifts().enqueue(new Callback<ArrayList<Shift>>() {
            @Override
            public void onResponse(Call<ArrayList<Shift>> call, Response<ArrayList<Shift>> response) {
                mLoadingProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ArrayList<Shift> shifts = response.body();
                    if (shifts != null) {
                        shifts = response.body();
                        //sort shifts by ID DESC
                        Collections.sort(shifts);
                        Log.d(TAG, mShiftDetails + "");
                        if (mShiftDetails.size() == 0) {
                            //convert Shift to ShiftDetail
                            for (Shift shift : shifts) {
                                ShiftDetail shiftDetail = new ShiftDetail(
                                        shift.getId(),
                                        shift.getStart(),
                                        shift.getEnd(),
                                        shift.getStartLatitude(),
                                        shift.getStartLongitude(),
                                        "3",
                                        shift.getEndLatitude(),
                                        shift.getEndLongitude(),
                                        "3",
                                        shift.getImage());
                                mShiftDetails.add(shiftDetail);
                            }


                            if (mShiftDetails.get(0).getEnd().equals("")) {
                                //shift in progress
                                mShowStartLayout = false;
                                showCurrentShiftLayout();
                                updateCurrentShiftLayout(mShiftDetails.get(0));

                                Picasso.with(MainActivity.this).load(mShiftDetails.get(0).getImage()).into(mCurrentImageView);
                                String startDate = mShiftDetails.get(0).getStart().substring(0, 10);
                                String startTime = mShiftDetails.get(0).getStart().substring(11, 19);
                                mCurrentDateAndTime.setText(startDate + " " + startTime);
                                mShiftDetails.remove(0);
                            } else {
                                mShowStartLayout = true;
                                showStartButton();
                            }


                            Log.d("mShiftDetails b:", mShiftDetails.size() + "");
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mAdapter = new Adapter(MainActivity.this, mShiftDetails, mRecyclerView);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnLoadListener(new Adapter.OnLoadListener() {
                                @Override
                                public void onLoadData() {
                                    loadShiftsFromWeb();
                                }
                            });

                            //start LoadAddressService to get the address detail
                            for (ShiftDetail shiftdetail : mShiftDetails) {
                                Intent addressIntent = new Intent(MainActivity.this, LoadAddressService.class);
                                addressIntent.putExtra(MESSAGE_SOURCE, "LIST");
                                addressIntent.putExtra(SHIFT_DETAIL, shiftdetail);
                                startService(addressIntent);
                            }
                        } else {
                            if (mShiftDetails.get(0).getId() == shifts.get(0).getId()) {
                                mShowStartLayout = true;
                                if (!shifts.get(0).getEnd().equals("") && mShiftDetails.get(0).equals("")) {
                                    mShiftDetails.get(0).setEnd(shifts.get(0).getEnd());
                                    mAdapter.notifyDataSetChanged();
                                    mShiftDetails.get(0).setEndLatitude(shifts.get(0).getEndLatitude());
                                    mShiftDetails.get(0).setEndLongitude(shifts.get(0).getEndLongitude());
                                    Intent addressIntent = new Intent(MainActivity.this, LoadAddressService.class);
                                    addressIntent.putExtra(MESSAGE_SOURCE, "LIST");
                                    addressIntent.putExtra(SHIFT_DETAIL, mShiftDetails.get(0));
                                    startService(addressIntent);
                                }
                            } else {
                                if (shifts.get(0).getEnd().equals("")) {
                                    mShowStartLayout = false;
                                    showCurrentShiftLayout();
                                    ShiftDetail shiftDetail = new ShiftDetail(
                                            shifts.get(0).getId(),
                                            shifts.get(0).getStart(),
                                            shifts.get(0).getEnd(),
                                            shifts.get(0).getStartLatitude(),
                                            shifts.get(0).getStartLongitude(),
                                            "3",
                                            shifts.get(0).getEndLatitude(),
                                            shifts.get(0).getEndLongitude(),
                                            "3",
                                            shifts.get(0).getImage());
                                    updateCurrentShiftLayout(shiftDetail);

                                    Picasso.with(MainActivity.this).load(shifts.get(0).getImage()).into(mCurrentImageView);
                                    String startDate = shifts.get(0).getStart().substring(0, 10);
                                    String startTime = shifts.get(0).getStart().substring(11, 19);
                                    mCurrentDateAndTime.setText(startDate + " " + startTime);
                                    showRecyclerViewLayout();
                                } else {
                                    mShowStartLayout = true;
                                    showRecyclerViewLayout();
                                    //load again
                                    ArrayList<ShiftDetail> shiftDetails = new ArrayList<>();
                                    for (Shift shift : shifts) {
                                        ShiftDetail shiftDetail = new ShiftDetail(
                                                shift.getId(),
                                                shift.getStart(),
                                                shift.getEnd(),
                                                shift.getStartLatitude(),
                                                shift.getStartLongitude(),
                                                "3",
                                                shift.getEndLatitude(),
                                                shift.getEndLongitude(),
                                                "3",
                                                shift.getImage());
                                        shiftDetails.add(shiftDetail);
                                    }
                                    mAdapter.updateData(shiftDetails);
                                    mShiftDetails = shiftDetails;
                                    mAdapter.notifyDataSetChanged();

                                    //start LoadAddressService to get the address detail
                                    for (ShiftDetail shiftdetail : mShiftDetails) {
                                        Intent addressIntent = new Intent(MainActivity.this, LoadAddressService.class);
                                        addressIntent.putExtra(MESSAGE_SOURCE, "LIST");
                                        addressIntent.putExtra(SHIFT_DETAIL, shiftdetail);
                                        startService(addressIntent);
                                    }
                                }
                            }
                        }
                    } else {
                        mIsStart = true;
                        mShowStartLayout = true;
                        showStartButton();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Shift>> call, Throwable t) {
                mLoadingProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "Unable to submit saveShifts to API.");
            }
        });
    }


    private void loadShiftDetailFromLocalDatabase() {
        ShiftsDataSource dataSource = new ShiftsDataSource(this);
        int count = dataSource.GetShiftsTableCount();
        if (count != 0) {
            mShiftDetails = dataSource.readAllShiftsTable();
            if (mShiftDetails.get(0).getEnd().equals("")) {
                //shift in progress
                mShowStartLayout = false;
                showCurrentShiftLayout();
                updateCurrentShiftLayout(mShiftDetails.get(0));
                Picasso.with(MainActivity.this).load(mShiftDetails.get(0).getImage()).into(mCurrentImageView);
                String startDate = mShiftDetails.get(0).getStart().substring(0, 10);
                String startTime = mShiftDetails.get(0).getStart().substring(11, 19);
                mCurrentDateAndTime.setText(startDate + " " + startTime);
                mShiftDetails.remove(0);
            } else {
                mShowStartLayout = true;
                showStartButton();
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mAdapter = new Adapter(MainActivity.this, mShiftDetails, mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            showNetworkNotAvailableDialog(
                    "shifts info not downloaded",
                    "Please open your network to download shifts info");
            mIsStart = true;
            mShowStartLayout = true;
            showStartButton();
        }
    }


    private void startShift(InputShift inputShift) {
        mAPIService.saveStartShift(inputShift).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String returnString = response.body().replaceAll("\"", "");
                    if (returnString.equals("Start shift - All good")) {
                        loadShiftsFromWeb();
                        showRecyclerViewLayout();
                        Log.d(TAG, "returnString:" + response.body());
                    }
                    if (returnString.substring(0, 4).equals("Nope")) {
                        Log.d(TAG, "returnString.substring(0,3):" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Unable to submit saveStartShift to API.");
            }
        });
    }

    private void endShift(InputShift inputEndShift) {
        mAPIService.saveEndShift(inputEndShift).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String returnString = response.body().replaceAll("\"", "");
                    if (returnString.equals("End shift - All good")) {
                        loadShiftsFromWeb();
                        showRecyclerViewLayout();
                    }
                    if (returnString.substring(0, 4).equals("Nope")) {
                        Log.d(TAG, "returnString.substring(0,3):" + response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Unable to submit saveStartShift to API.");
            }
        });
    }

    private void showFirstTimeView() {
        mOfflineModeLabel.setVisibility(View.GONE);
        mRemindingLabel.setVisibility(View.GONE);
        mCurrentShiftLayout.setVisibility(View.GONE);
        mStartButton.setVisibility(View.GONE);
        mStartEndLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showStartButton() {
        mCurrentShiftLayout.setVisibility(View.GONE);
        mStartButton.setVisibility(View.VISIBLE);
    }

    private void showCurrentShiftLayout() {
        mCurrentShiftLayout.setVisibility(View.VISIBLE);
        mStartButton.setVisibility(View.GONE);
        mEndButton.setEnabled(true);
    }

    private void showStartEndEditLayout(Boolean isStart) {
        if (isStart) {
            mLayoutLabel.setText("Start a shift");
        } else {
            mLayoutLabel.setText("End a shift");
        }
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

    private void updateCurrentShiftLayout(ShiftDetail shiftDetail) {
        Picasso.with(MainActivity.this).load(shiftDetail.getImage()).into(mCurrentImageView);
        String startDate = shiftDetail.getStart().substring(0, 10);
        String startTime = shiftDetail.getStart().substring(11, 19);
        mCurrentDateAndTime.setText(startDate + " " + startTime);

        //start LoadAddressService to get the address detail
        Intent Intent = new Intent(MainActivity.this, LoadAddressService.class);
        Intent.putExtra(MESSAGE_SOURCE, "CURRENT");
        Intent.putExtra(SHIFT_DETAIL, shiftDetail);
        startService(Intent);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String yearString = String.valueOf(year);
        String monthString = String.valueOf(month + 1);
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

    private void setCurrentTimeAndLocation() {
        //set current date and time
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", java.util.Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        mDate.setText(currentDate.substring(0, 10));
        mTime.setText(currentDate.substring(11, 19));

        //set current location
        if (isLocationEnabled(this)) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
            }
        } else {
            //todo: show a dialog to turn on the location directly
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Location not enabled")
                    .setMessage("Please turn on your location to get the current location");
            builder.create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted, request location update again
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
                    Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location location = getLastBestLocation(locationGPS, locationNet);
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            mRoad.setText(addresses.get(0).getAddressLine(0));
                            //mSuburb.setText(addresses.get(0).getFeatureName());
                            mCity.setText(addresses.get(0).getLocality());
                            mPostcode.setText(addresses.get(0).getPostalCode());
                            mState.setText(addresses.get(0).getAdminArea());
                            mCountry.setText(addresses.get(0).getCountryName());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (SecurityException e) {
                    Toast.makeText(MainActivity.this, "SecurityException: \n" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();
            Log.d(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.d(TAG, latitude);

            makeUseOfNewLocation(loc);

            if (currentBestLocation == null) {
                currentBestLocation = loc;
            }

        /*------- To get city name from coordinates -------- */
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    mRoad.setText(addresses.get(0).getAddressLine(0));
                    //mSuburb.setText(addresses.get(0).getFeatureName());
                    mCity.setText(addresses.get(0).getLocality());
                    mPostcode.setText(addresses.get(0).getPostalCode());
                    mState.setText(addresses.get(0).getAdminArea());
                    mCountry.setText(addresses.get(0).getCountryName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private Location getLastBestLocation(Location locationGPS, Location locationNet) {
        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    /**
     * This method modify the last know good location according to the arguments.
     *
     * @param location The possible new location.
     */
    void makeUseOfNewLocation(Location location) {
        if (isBetterLocation(location, currentBestLocation)) {
            currentBestLocation = location;
        }
    }

    /**
     * Determines whether one location reading is better than the current location fix
     *
     * @param location            The new location that you want to evaluate
     * @param currentBestLocation The current location fix, to which you want to compare the new one.
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location,
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse.
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /*----Method to Check GPS is enable or disable ----- */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


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
                mCountry.getText().toString().trim().length() != 0) {
            isAllFieldsInput = true;
        }
        return isAllFieldsInput;
    }

    private InputShift validateAndFormatInput() {
        String formattedDate = " ";
        String formattedLatitude = " ";
        String formattedLongitude = " ";
        //validate and initialise date and time
        String dateString = mDate.getText().toString().trim();
        String time = mTime.getText().toString().trim() + ":" + "00";

        //TODO: check date and time whether are bigger than current date and time

        TimeZone tz = TimeZone.getDefault();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
        dateFormat.setTimeZone(tz);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", java.util.Locale.getDefault());
        try {
            Date inputDate = dateFormat.parse(dateString + "T" + time);
            Log.d(TAG, "formattedDate:" + inputDate.toString());
            formattedDate = sdf.format(inputDate);
            Log.d(TAG, "formattedDateString:" + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //validate and initialise address
        String road = mRoad.getText().toString().trim();
        String suburb = mSuburb.getText().toString().trim();
        String city = mCity.getText().toString().trim();
        String postcode = mPostcode.getText().toString().trim();
        String state = mState.getText().toString().trim();
        String country = mCountry.getText().toString().trim();

        Geocoder geoCoder = new Geocoder(this);
        List<Address> addresses;

        try {
            Log.d(TAG, road);
            addresses = geoCoder.getFromLocationName(
                    road + "," + city + "," + state + "," + country, 3);
            Log.d(TAG, addresses.size() + "");
            if (addresses.size() > 0) {
                Address location = addresses.get(0);
                formattedLatitude = String.valueOf(location.getLatitude());
                formattedLongitude = String.valueOf(location.getLongitude());
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new InputShift(formattedDate, formattedLatitude, formattedLongitude);
    }

    private void showNetworkNotAvailableDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setMessage(message);
        builder.create().show();

    }

}

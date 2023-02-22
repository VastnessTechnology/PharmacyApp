package com.vgroyalchemist.views;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TrackOrderVO;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.TrackOrderService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


public class FragmentTrackOrder extends NonCartFragment implements OnMapReadyCallback {


    //Variable Declaration
    public static final String FRAGMENT_ID = "67";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    String deliveryBoyID = "";
    String delvType = "";

    TextView mTextViewDate;
    TextView mTextViewOrderNo;
    TextView mTextViewStatus;

    LinearLayout mLinearLayoutDate;
    LinearLayout mLinearLayoutstatus;
    ProgressBar mVerticalSeekBar;

    ArrayList<TrackOrderVO> mArrayListItemList;
    ArrayList mArrayListOrderStatus;

    private GoogleMap map;


    int mintTotal = 0;
    float mfloat = 0;

    Bundle mBundle;
    String mStringOrderNo;
    String mStringOrderDate;
    String Orderstatus;

    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVO;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;

    String mStringUserId;
    String OrderId;
    SupportMapFragment mapFragment;

    Handler handler;
    Runnable runnable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mArrayListItemList = new ArrayList<TrackOrderVO>();
        mPostParseGet = new PostParseGet(mainActivity);
        mArrayListOrderStatus = new ArrayList();
        mBundle = getArguments();
        mArrayListOrderStatus.add("Pending");
        mArrayListOrderStatus.add("Confirm");
        mArrayListOrderStatus.add("Dispatch");
        mArrayListOrderStatus.add("Out for Delivery");
        mArrayListOrderStatus.add("Delivered");

        if (mBundle != null) {
            mStringOrderNo = mBundle.getString("OrderNo");
            mStringOrderDate = mBundle.getString("orderdate");
            Orderstatus = mBundle.getString("Orderstatus");
            OrderId = mBundle.getString("OrderId");
            delvType = mBundle.getString("DelvType");
        }

    }

    private void fetchDeliveryBoyLocation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://vgroyaldeliveryboy-default-rtdb.firebaseio.com/");
        //    database.setPersistenceEnabled(true);
        database.getReference("location").child(deliveryBoyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Double> values = (HashMap<String, Double>) dataSnapshot.getValue();
                LatLng latLng = new LatLng(values.get("latitude"), values.get("longitude"));
                displayLocationOnMap(latLng);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void displayLocationOnMap(LatLng latLng) {

        map.addMarker(new MarkerOptions().position(latLng).title("Delivery Person"));
        //call moveCamera() on mMap to update the camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.getUiSettings().setZoomControlsEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        handler.removeCallbacks(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_trackorder, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Track Order");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);


        mTextViewDate = fragmentView.findViewById(R.id.fragment_track_order_textview_date);
        mTextViewOrderNo = fragmentView.findViewById(R.id.fragment_track_order_textview_order_number);
        mTextViewStatus = fragmentView.findViewById(R.id.fragment_track_order_textview_status);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        String mDate = mStringOrderDate;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, MMM d, yyyy");
        try {
            Date date = fmt.parse(mDate);
            String mString = fmt2.format(date);
            mTextViewDate.setText(mString);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        mTextViewOrderNo.setText(mStringOrderNo);

        mLinearLayoutDate = fragmentView.findViewById(R.id.fragment_track_order_linear_date);
        mLinearLayoutstatus = fragmentView.findViewById(R.id.fragment_track_order_linear_main);
        mVerticalSeekBar = fragmentView.findViewById(R.id.fragment_track_order_progressbar);

        TrackOrderData();


        if (delvType.equalsIgnoreCase("Delivery")) {
            if (Orderstatus.equalsIgnoreCase("Dispatch") || Orderstatus.equalsIgnoreCase("Out for Delivery")) {
                fragmentView.findViewById(R.id.linearMap).setVisibility(View.VISIBLE);
            } else {
                fragmentView.findViewById(R.id.linearMap).setVisibility(View.GONE);
            }
        } else {
            fragmentView.findViewById(R.id.linearMap).setVisibility(View.GONE);
        }

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });


//        for (int i = 0; i < mArrayListMyOrders.size(); i++) {
//            if (mArrayListMyOrders.get(i).getData().getDetails().get(0).getItem_list() != null &&
//                    mArrayListMyOrders.get(i).getData().getDetails().get(0).getItem_list().size() > 0) {
//                mArrayListItemList.addAll(mArrayListMyOrders.get(i).getData().getDetails().get(0).getTrack_order());

//                createDate(mArrayListItemList);
//            }
//        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_tracking);
        mapFragment.getMapAsync(FragmentTrackOrder.this);
        handler = new Handler();


        return fragmentView;
    }


    public void createTextView(ArrayList mArrayList) {

        for (int i = 0; i < mArrayList.size(); i++) {

            final TextView[] tv = new TextView[mArrayList.size()];

            tv[i] = new TextView(mainActivity);

            tv[i].setText(mArrayList.get(i).toString());


            if (mArrayList.get(i).toString().equalsIgnoreCase("true")) {
                tv[i].setTextColor(getResources().getColor(R.color.parrot_green));
            } else {
                tv[i].setTextColor(Color.BLACK);
            }

            tv[i].setBackgroundColor(Color.WHITE);
            tv[i].setTextSize(14);
            tv[i].setPadding(2, 2, 2, 2);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            params.setMargins(15, 0, 15, 0);
            params.weight = 1.2f;
            tv[i].setLayoutParams(params);
            mLinearLayoutstatus.addView(tv[i]);
        }
    }

    public void createDate(ArrayList mArrayList) {

        for (int i = 0; i < mArrayList.size(); i++) {

            final TextView[] mTextView = new TextView[mArrayList.size()];

            mTextView[i] = new TextView(mainActivity);

            mTextView[i].setBackgroundColor(Color.WHITE);
            mTextView[i].setTextColor(Color.BLACK);
            mTextView[i].setTextSize(13);
            mTextView[i].setPadding(2, 2, 2, 2);


//            if (mArrayListItemList.get(i).toString().equalsIgnoreCase("pending") || mArrayListItemList.get(i).toString().equalsIgnoreCase("Processed") ||
//                    mArrayListItemList.get(i).toString().equalsIgnoreCase("shipped"))  {
//                if (!mArrayList.get(i).toString().equalsIgnoreCase("")) {
////                    mTextView[i].setText(mArrayList.get(i).getDate());
//                    mTextView[i].setText("21-04-2021");
//                } else {
//                    mTextView[i].setText("-");
//                }
//            } else {
//                mTextView[i].setText("-");
//            }

            mLinearLayoutDate.setWeightSum(mArrayList.size());

            LinearLayout.LayoutParams paramsDate = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            paramsDate.setMargins(15, 0, 15, 0);
            paramsDate.weight = 1.2f;
            mTextView[i].setLayoutParams(paramsDate);
            mLinearLayoutDate.addView(mTextView[i]);
        }
    }


    public void onResumeData() {

    }


    @Override
    public void doWork() {
        onResumeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        onResumeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mCommonHelper.hideKeyboard(mainActivity);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void TrackOrderData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                TrackOrderList mGetAllOrderList = new TrackOrderList(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, mGetAllOrderList);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (!mCommonHelper.check_Internet(mainActivity)) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                        return;
                    }
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {

                        try {
                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {
//                            Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);

                                if (mServerResponseVO.getData() != null) {

                                    mArrayListItemList = (ArrayList<TrackOrderVO>) mServerResponseVO.getData();

                                    if (mArrayListItemList != null && mArrayListItemList.size() > 0) {
                                          //
                                        deliveryBoyID = mArrayListItemList.get(0).getDeliveryboyid();
                                        if (deliveryBoyID!=null) {
                                            Log.e("id",deliveryBoyID);
                                            runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (!deliveryBoyID.equalsIgnoreCase(""))
                                                        fetchDeliveryBoyLocation();
                                                }
                                            };
                                            handler.postDelayed(runnable, 500);
                                        }

                                        for (int i = 0; i < mArrayListItemList.size(); i++) {
                                            final TextView[] tv = new TextView[mArrayListItemList.size()];
                                            tv[i] = new TextView(mainActivity);
                                            tv[i].setText(mArrayListItemList.get(i).getOrderStatusName());
                                            if (mArrayListItemList.get(i).getOrderStatusValue().equalsIgnoreCase("true")) {
                                                tv[i].setTextColor(getResources().getColor(R.color.parrot_green));
                                            } else {
                                                tv[i].setTextColor(Color.BLACK);
                                            }
                                            tv[i].setBackgroundColor(Color.WHITE);
                                            tv[i].setTextSize(14);
                                            tv[i].setPadding(2, 2, 2, 2);
                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams
                                                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            params.setMargins(15, 0, 15, 0);
                                            params.weight = 1.2f;
                                            tv[i].setLayoutParams(params);
                                            mLinearLayoutstatus.addView(tv[i]);
                                        }
                                        mLinearLayoutstatus.setWeightSum(mArrayListItemList.size());
                                        for (int j = 0; j < mArrayListItemList.size(); j++) {
                                            if (mArrayListItemList.get(j).getOrderStatusValue().equalsIgnoreCase("true")) {
                                                mintTotal++;
                                            }
                                        }
                                        mfloat = (mintTotal * 100) / (mArrayListItemList.size());
                                        int total = (int) mfloat;

                                        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mVerticalSeekBar, "progress", total);
                                        progressAnimator.setDuration(1000);
                                        progressAnimator.setInterpolator(new LinearInterpolator());
                                        progressAnimator.start();

                                    }
                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

    }

    @SuppressLint("RestrictedApi")
    public class TrackOrderList extends TaskExecutor {
        protected TrackOrderList(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            TrackOrderService trackOrderService = new TrackOrderService();
            mServerResponseVO = trackOrderService.TrackOrder(mainActivity, Tags.TrackingStatus, mStringUserId, OrderId);

            return null;
        }
    }

}
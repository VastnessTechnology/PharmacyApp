package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SaveMedicineReminderService;
import com.vgroyalchemist.webservice.SaveVitalService;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// developed by krishna 13-03-2021
// Add Vital data

public class FragmentAddVital extends NonCartFragment {

    //Variable Declaration
    public static final String FRAGMENT_ID = "45";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;


    TextInputLayout mTextInputLayoutUnit;
    TextInputLayout mTextInputLayoutUnitValue;

    EditText mEditTextDate;
    EditText mEditTextTime;
    EditText mEditTextUnit;
    EditText mEditTextUnitValue;
    EditText mEditTextRemarks;

    TextView mTextViewSave;

    String mStringDate;
    String mStringTime;
    String mStringUnitValue;
    String mStringUnit;
    String mStringRemakrs;


    Calendar mCalendar;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Format mFormatTime;

    ServerResponseVO mServerResponseVOSave;
    PostParseGet mPostParseGet;

    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String GetUserId;

    Bundle mBundle;
    String mStringVitalId;
    String mStringVitalUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mPostParseGet = new PostParseGet(mainActivity);

        mBundle = getArguments();

        if(mBundle != null)
        {
            mStringVitalId = mBundle.getString("VitalID");
            mStringVitalUnit = mBundle.getString("VitalUnit");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_add_vital, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Add Vital");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mTextInputLayoutUnit = fragmentView.findViewById(R.id.fragment_add_vital_textinput_edittext_vitalUnit);
        mTextInputLayoutUnitValue = fragmentView.findViewById(R.id.fragment_add_vital_textinput_unitValue);

        mEditTextDate= fragmentView.findViewById(R.id.fragment_add_vital_textview_startdate);
        mEditTextTime =fragmentView.findViewById(R.id.fragment_add_vital_textview_time);
        mEditTextUnit =fragmentView.findViewById(R.id.fragment_add_vital_edittext_vitalUnit);
        mEditTextUnitValue =fragmentView.findViewById(R.id.fragment_add_vital_textview_edttext_unitValue);
        mEditTextRemarks =fragmentView.findViewById(R.id.fragment_add_vital_textview_edttext_Remarks);

        mTextViewSave =fragmentView.findViewById(R.id.fragment_Add_vital_textview_submit);

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        GetUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

            mCalendar = Calendar.getInstance();
            dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
            mFormatTime = new SimpleDateFormat("hh:mm a");

        if(mStringVitalUnit != null)
        {
            mEditTextUnit.setText(mStringVitalUnit);
        }
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        String currentDate = dateFormatter.format(mCalendar.getTime());
        mEditTextDate.setText(currentDate);
        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });

        String currentTime = mFormatTime.format(mCalendar.getTime());
        mEditTextTime.setText(currentTime);
        mEditTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetTime();
            }
        });

        mTextViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCommonHelper.hideKeyboard(mainActivity);
                if (!mCommonHelper.check_Internet(mainActivity)) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    return;
                }

                mStringDate = mEditTextDate.getText().toString().trim();
//                mStringTime = mEditTextTime.getText().toString().trim();
                mStringUnit = mEditTextUnit.getText().toString().trim();
                mStringUnitValue = mEditTextUnitValue.getText().toString().trim();
                mStringRemakrs = mEditTextRemarks.getText().toString().trim();

                String mDate = mEditTextTime.getText().toString().trim();
                SimpleDateFormat fmt = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat fmt2 = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date date = fmt.parse(mDate);
                    String mString = fmt2.format(date);
                    mStringTime = mString;
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                if(!validateUnit())
                {
                    return;
                }
                if(!validateUnitValue())
                {
                    return;
                }

                AddVital();
            }
        });

        return  fragmentView;

    }


    //     Select Start Date Function
    private void setStartDate() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(mainActivity,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEditTextDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        fromDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        fromDatePickerDialog.show();
    }

    //    set time AM & PM Function
    private void SetTime() {
        Calendar newCalendar = Calendar.getInstance();

        TimePickerDialog Tp = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String timeSet = "";
                if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    timeSet = "PM";
                } else if (hourOfDay == 0) {
                    hourOfDay += 12;
                    timeSet = "AM";
                } else if (hourOfDay == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
                }

                mEditTextTime.setText(hourOfDay + ":" + minute + " " + timeSet);

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);

        Tp.show();
    }

    private boolean validateUnit() {
        String email = mEditTextUnit.getText().toString().trim();

        if (email.isEmpty()) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.select_unit)).show(mainActivity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        }

        return true;
    }

    private boolean validateUnitValue() {
        String email = mEditTextUnitValue.getText().toString().trim();

        if (email.isEmpty()) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.unit_value)).show(mainActivity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        }

        return true;
    }


    //    Save Vital
    public void AddVital() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                AddVitalExe addVitalExe = new AddVitalExe(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, addVitalExe);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {
                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        try {

                            if (mServerResponseVOSave.getStatus().equalsIgnoreCase("true")) {
                                Snackbar.with(mainActivity).text(mServerResponseVOSave.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                                Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.what == Tags.WHAT) {
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mainActivity.onBackPressed();
                                                }
                                            }, 800);
                                        }
                                    }
                                };
                                handler.sendEmptyMessage(Tags.WHAT);

                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVOSave.getMsg()).show(mainActivity);
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

    @SuppressLint("RestrictedApi")
    public class AddVitalExe extends TaskExecutor {
        protected AddVitalExe(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            SaveVitalService fetchGetProfilenfoService = new SaveVitalService();

            mServerResponseVOSave = fetchGetProfilenfoService.AddVital(mainActivity, Tags.AddVital, GetUserId, mStringDate, mStringTime, mStringUnit,
                   mStringUnitValue,mStringRemakrs, mStringVitalId);

            return null;
        }
    }

    public void onResumeData() {
        mainActivity.toolbar.setVisibility(View.GONE);

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
}
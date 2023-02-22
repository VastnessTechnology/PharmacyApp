package com.vgroyalchemist.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.MedicineNameVo;
import com.vgroyalchemist.vos.MultiMedicineReminderItem;
import com.vgroyalchemist.vos.ScheduleActiveVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.FetchMedicineNameService;
import com.vgroyalchemist.webservice.PostParseGet;
import com.vgroyalchemist.webservice.SaveMedicineReminderService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;


// developed By Krishna  6-3-2021
//   Add Medicine Class

public class FragmentAddMedicine extends NonCartFragment implements TextWatcher {

    //Variable Declaration
    public static final String FRAGMENT_ID = "25";

    public MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;

    EditText mEditTextMedicineName;
    TextInputLayout mTextInputLayoutMedicineName;
    EditText mEditTextRemark;
    TextInputLayout mTextInputLayoutRemark;
    TextView mTextViewStartDate;
    TextView mTextViewEndDate;
    TextView mTextViewScheduleType;
    TextView mTextViewSchedulePattern;
    EditText mTextViewInterval;
    ImageView mImageViewplus;
    ImageView mImageViewMinus;
    TextView mTextViewReminderTime;
    EditText mTextViewdosage;
    CheckBox mCheckBoxAlarm;
    LinearLayout mLinearLayoutSchedulePattern;
    LinearLayout mLinearLayoutInterval;
    RelativeLayout mRelativeLayoutAddReminder;

    TextView mTextViewSave;
    LinearLayout mLinearLayoutaddlayout;
    ViewGroup newLayout;

    LinearLayout mLinearLayoutWeekname;
    TextView mTextViewWeekName;

    ScrollView scrollView;


    //    Sechule pattern
    Dialog mDialogSchedulePattern;
    ListView mListViewSchedulePattern;
    ArrayList<String> mArrayListSchedulePattern;

    //    Day of Week
    Dialog mDialogWeek;
    ListView mListViewWeek;
    ArrayList<String> mArrayListWeek;

    ArrayList<String> removeItem = new ArrayList<String>();

    private SimpleDateFormat dateFormatter;
    private Format mFormatTime;
    Calendar mCalendar;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    ArrayList mArrayListADDReminder;
    ArrayList<MedicineNameVo> mArrayListMedicineName;
    ArrayList<MultiMedicineReminderItem> multiprescriptionItems = new ArrayList<MultiMedicineReminderItem>();
    JSONArray jsonArray;
    Object romoveItem_teg = null;
//    AddReminderAdpter addReminderAdpter;

    EditText mTextViewdosageRow;
    int itemCount = 0;
    Dialog popupView;
    ListView mListView;
    TextView mTextViewListTitle;
    EditText mAutoCompleteTextView;
    MedicineNameAdapter medicineNameAdapter;

    ServerResponseVO mServerResponseVOMedicinename;
    ServerResponseVO mServerResponseVOSave;
    PostParseGet mPostParseGet;
    String mStringMedicineId = "";
    String mStringMedicineName;
    String mCheckboxAlaramValue;


    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String GetUserId;


    RecyclerView mListViewReminder;
    Adpter_Reminder mAdpter_reminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();


        mArrayListSchedulePattern = new ArrayList<String>();
        mArrayListSchedulePattern.add("Daily");
        mArrayListSchedulePattern.add("Months");
        mArrayListSchedulePattern.add("By Day of Week");

        mArrayListWeek = new ArrayList<String>();
        mArrayListWeek.add("Sunday");
        mArrayListWeek.add("Monday");
        mArrayListWeek.add("Tuesday");
        mArrayListWeek.add("Wednesday");
        mArrayListWeek.add("Thursday");
        mArrayListWeek.add("Friday");
        mArrayListWeek.add("Saturday");

        mArrayListADDReminder = new ArrayList();
        mArrayListMedicineName = new ArrayList<MedicineNameVo>();

        mPostParseGet = new PostParseGet(mainActivity);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_add_medicine, container, false);


        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Add Medicine");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);

        mEditTextMedicineName = fragmentView.findViewById(R.id.fragment_add_medicine_edit_medicine_name);
        mTextInputLayoutMedicineName = fragmentView.findViewById(R.id.input_layout_medicin_name);
        mEditTextRemark = fragmentView.findViewById(R.id.fragment_add_medicine_edit_remarks);
        mTextInputLayoutRemark = fragmentView.findViewById(R.id.input_layout_medicin_remarks);

        mTextViewStartDate = fragmentView.findViewById(R.id.fragment_add_medicine_textview_start_date_value);
        mTextViewEndDate = fragmentView.findViewById(R.id.fragment_add_medicine_textview_end_date_value);
        mTextViewScheduleType = fragmentView.findViewById(R.id.fragment_add_medicine_textview_schedul_type_value);
        mTextViewSchedulePattern = fragmentView.findViewById(R.id.fragment_add_medicine_textview_schedul_pattern_value);
        mTextViewInterval = fragmentView.findViewById(R.id.fragment_add_medicine_textview_interval_value);
        mImageViewplus = fragmentView.findViewById(R.id.fragment_add_medicine_imageview_add_more_reminder);
        mLinearLayoutWeekname = fragmentView.findViewById(R.id.fragment_add_medicine_linear_weekname);
        mTextViewWeekName = fragmentView.findViewById(R.id.fragment_add_medicine_weekname);

        mLinearLayoutSchedulePattern = fragmentView.findViewById(R.id.fragment_add_medicine_linear_schedul_pattern);
        mLinearLayoutInterval = fragmentView.findViewById(R.id.fragment_add_medicine_linear_interval);
        mRelativeLayoutAddReminder = fragmentView.findViewById(R.id.fragment_add_medicine_add_reminder_time);

        mTextViewSave = fragmentView.findViewById(R.id.fragment_Add_medicin_textview_submit);
//        mLinearLayoutaddlayout = fragmentView.findViewById(R.id.fragment_add_medicine_Linaer);

//        scrollView = fragmentView.findViewById(R.id.fragment_add_medicine_scroll);
        mListViewReminder = fragmentView.findViewById(R.id.fragment_add_medicine_Listview);
        mCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
        mFormatTime = new SimpleDateFormat("hh:mm a");

        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        GetUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();

        mTextViewReminderTime = fragmentView.findViewById(R.id.row_reminder_time_textview_reminder_time_value);
        mTextViewdosageRow = fragmentView.findViewById(R.id.row_reminder_textview_dosage_value);
        mCheckBoxAlarm = fragmentView.findViewById(R.id.row_reminder_checkbox_alarm);

        mCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
        mFormatTime = new SimpleDateFormat("hh:mm a");

        String currentTime = mFormatTime.format(mCalendar.getTime());
        mTextViewReminderTime.setText(currentTime);
        mTextViewReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetTime();
            }
        });

        mCheckBoxAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckboxAlaramValue = "true";
                } else {
                    mCheckboxAlaramValue = "false";
                }
            }
        });

//        newLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.row_add_reminder, null, false);
//        mTextViewReminderTime = newLayout.findViewById(R.id.row_reminder_time_textview_reminder_time_value);
//        mTextViewdosageRow = newLayout.findViewById(R.id.row_reminder_textview_dosage_value);
//        mCheckBoxAlarm = newLayout.findViewById(R.id.row_reminder_checkbox_alarm);
//        mImageViewMinus = newLayout.findViewById(R.id.row_reminder_time_imageview_minus);
//        mTextViewReminderTime.setTag(itemCount);
//        newLayout.setTag(itemCount);
//        mImageViewMinus.setTag(itemCount);
//        mLinearLayoutaddlayout.addView(newLayout);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                mainActivity.onBackPressed();
            }
        });

        String currentDate = dateFormatter.format(mCalendar.getTime());
        mTextViewStartDate.setText(currentDate);

//        setRowData();


        mTextViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });

        mTextViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndDate();
            }
        });

        mTextViewSchedulePattern.setText(mArrayListSchedulePattern.get(0));
        mTextViewSchedulePattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DilogSchedulePattern();
            }
        });

        mTextViewWeekName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DilogWeek();
            }
        });

        if (mTextViewSchedulePattern.getText().toString().equalsIgnoreCase("By Day of Week")) {
            mLinearLayoutInterval.setVisibility(View.GONE);
            mLinearLayoutWeekname.setVisibility(View.VISIBLE);
            mTextViewWeekName.setText(mArrayListWeek.get(0));

        } else {
            mLinearLayoutInterval.setVisibility(View.VISIBLE);
            mLinearLayoutWeekname.setVisibility(View.GONE);
            mTextViewWeekName.setText(" ");
        }

        mEditTextMedicineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                DialogMedicineName();
            }
        });

        mTextViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
            }
        });


        mImageViewplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mTextViewReminderTime.getText().toString().equalsIgnoreCase("") || mTextViewdosageRow.getText().toString().equalsIgnoreCase("") ||
                        mCheckBoxAlarm.getText().toString().equalsIgnoreCase("")) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.pls_enter_all_filed)).show(mainActivity);

                    return;
                }

                MultiMedicineReminderItem multiMedicineReminderItem = new MultiMedicineReminderItem();
                multiMedicineReminderItem.setAlarm(mCheckboxAlaramValue);
                multiMedicineReminderItem.setDosage(mTextViewdosageRow.getText().toString().trim());
                multiMedicineReminderItem.setReminderTime(mTextViewReminderTime.getText().toString().trim());
                multiprescriptionItems.add(multiMedicineReminderItem);

                mAdpter_reminder = new Adpter_Reminder(mainActivity, multiprescriptionItems);
                mListViewReminder.setHasFixedSize(true);
                mListViewReminder.setLayoutManager(new LinearLayoutManager(mainActivity));
                mListViewReminder.setAdapter(mAdpter_reminder);

                mCheckBoxAlarm.setChecked(false);
                mTextViewdosageRow.setText("");


//                arrayjson();

//                if (romoveItem_teg != null) {
//                    itemCount++;
//                    addMultipalItem();
//                    setRowData();
//                }
//                 if (mTextViewReminderTime.getText().toString().equalsIgnoreCase("") || mTextViewdosageRow.getText().toString().equalsIgnoreCase("") ||
//                        mCheckBoxAlarm.getText().toString().equalsIgnoreCase("")) {
//                    Snackbar.with(mainActivity).text(getResources().getString(R.string.pls_ente r_all_filed)).show(mainActivity);
//                }
//                else {
//
//
//                    mTextViewReminderTime.setEnabled(false);
//                    mTextViewdosageRow.setEnabled(false);
//                    mCheckBoxAlarm.setEnabled(false);
//                    itemCount++;
//                    addMultipalItem();
//                    setRowData();


//                }
            }
        });

        new GetMedicineListProcess().execute();
        return fragmentView;
    }

//    public void addMultipalItem() {
//        newLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.row_add_reminder, null, false);
//
//        mTextViewReminderTime = newLayout.findViewById(R.id.row_reminder_time_textview_reminder_time_value);
//        mTextViewdosageRow = newLayout.findViewById(R.id.row_reminder_textview_dosage_value);
//        mCheckBoxAlarm = newLayout.findViewById(R.id.row_reminder_checkbox_alarm);
//        mImageViewMinus = newLayout.findViewById(R.id.row_reminder_time_imageview_minus);
//        mTextViewReminderTime.setTag(itemCount);
//        newLayout.setTag(itemCount);
//        mImageViewMinus.setTag(itemCount);
//        mImageViewMinus.setVisibility(View.VISIBLE);
//
//        mLinearLayoutaddlayout.addView(newLayout);
////        scrollView.post(new Runnable() {
////            public void run() {
////                scrollView.fullScroll(50000);
////            }
////        });
//    }

//    public void setRowData() {
//
//        mImageViewMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mLinearLayoutaddlayout.getChildCount() > 1) {
//                    Object tag = v.getTag();
//                    romoveItem_teg = v.getTag();
//                    View findViewWithTag = ((ViewGroup) mLinearLayoutaddlayout.getParent()).findViewWithTag(tag);
//                    ((ViewGroup) findViewWithTag.getParent()).removeView(findViewWithTag);
//                    removeItem.add(String.valueOf(((Integer) tag).intValue()));
//                    multiprescriptionItems.remove(String.valueOf(((Integer) tag).intValue()));
//                }
//            }
//        });
//        mCalendar = Calendar.getInstance();
//        dateFormatter = new SimpleDateFormat("E, MMM dd, yyyy", Locale.US);
//        mFormatTime = new SimpleDateFormat("hh:mm a");
//
//        String currentTime = mFormatTime.format(mCalendar.getTime());
//        mTextViewReminderTime.setText(currentTime);
//        mTextViewReminderTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SetTime();
//            }
//        });
//
//        mCheckBoxAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mCheckboxAlaramValue = "true";
//                } else {
//                    mCheckboxAlaramValue = "false";
//                }
//            }
//        });
//    }


    //    Past Schedule adpter class
    public class Adpter_Reminder extends RecyclerView.Adapter<Adpter_Reminder.MyViewHolderPastSchedule> {
        Context context;
        ArrayList<MultiMedicineReminderItem> reminderItems;

        public Adpter_Reminder(Context context, ArrayList<MultiMedicineReminderItem> arrayList_InvoiceItem_copies) {

            this.context = context;
            this.reminderItems = arrayList_InvoiceItem_copies;
        }

        @NonNull
        @Override
        public Adpter_Reminder.MyViewHolderPastSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_reminder, parent, false);
            Adpter_Reminder.MyViewHolderPastSchedule myViewHolderPastSchedule = new Adpter_Reminder.MyViewHolderPastSchedule(v);
            return myViewHolderPastSchedule;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull Adpter_Reminder.MyViewHolderPastSchedule holder, @SuppressLint("RecyclerView") final int position) {

            holder.ReminderTime.setText(reminderItems.get(position).getReminderTime());
            holder.dosageRow.setText(reminderItems.get(position).getDosage());
            if (reminderItems.get(position).getAlarm().equalsIgnoreCase("true")) {
                holder.Alarm.setChecked(true);
            } else {
                holder.Alarm.setChecked(false);
            }

            holder.Minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reminderItems.remove(position);

                    notifyDataSetChanged();
                }
            });
            holder.dosageRow.setEnabled(false);
            holder.ReminderTime.setEnabled(false);
            holder.Alarm.setEnabled(false);

//            holder.ReminderTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Calendar newCalendar = Calendar.getInstance();
//                    TimePickerDialog Tp = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                            String timeSet = "";
//                            if (hourOfDay > 12) {
//                                hourOfDay -= 12;
//                                timeSet = "PM";
//                            } else if (hourOfDay == 0) {
//                                hourOfDay += 12;
//                                timeSet = "AM";
//                            } else if (hourOfDay == 12) {
//                                timeSet = "PM";
//                            } else {
//                                timeSet = "AM";
//                            }
//
//                            holder.ReminderTime.setText(hourOfDay + ":" + minute + " " + timeSet);
//
//                        }
//                    }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);
//
//                    Tp.show();
//                }
//            });


        }
        @Override
        public int getItemCount() {
            return reminderItems.size();
        }

        public class MyViewHolderPastSchedule extends RecyclerView.ViewHolder {

            TextView ReminderTime;
            TextView dosageRow;
            ImageView Minus;
            CheckBox Alarm;

            public MyViewHolderPastSchedule(View itemView) {
                super(itemView);

                ReminderTime = itemView.findViewById(R.id.row_reminder_time_textview_reminder_time_value);
                dosageRow = itemView.findViewById(R.id.row_reminder_textview_dosage_value);
                Alarm = itemView.findViewById(R.id.row_reminder_checkbox_alarm);
                Minus = itemView.findViewById(R.id.row_reminder_time_imageview_minus);
                Minus.setVisibility(View.VISIBLE);
            }
        }
    }

    //     Select Start Date Function
    private void setStartDate() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mTextViewStartDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
        fromDatePickerDialog.show();
    }

    // Select End Date Function
    private void setEndDate() {
        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mTextViewEndDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());

        toDatePickerDialog.show();
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

                mTextViewReminderTime.setText(hourOfDay + ":" + minute + " " + timeSet);

            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);


        Tp.show();
    }

    //  show Dialog Week
    public void DilogWeek() {
        mDialogWeek = new Dialog(mainActivity);
        mDialogWeek.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogWeek.setContentView(R.layout.dialog_list_item);
        mDialogWeek.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        mListViewWeek = mDialogWeek.findViewById(R.id.dialog_listview);

        MyBaseAdapterWeek myBaseAdapterWeek = new MyBaseAdapterWeek(mainActivity, R.layout.row_title, mArrayListWeek);
        mListViewWeek.setAdapter(myBaseAdapterWeek);
        mDialogWeek.show();
    }

    //    Apdter Week List
    public class MyBaseAdapterWeek extends ArrayAdapter<String> {

        ArrayList<String> mArrayListWeek;

        public MyBaseAdapterWeek(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            mArrayListWeek = objects;
        }

        @Override
        public int getCount() {
            return mArrayListWeek.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderWeek holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderWeek();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderWeek) convertView.getTag();
            }

            holder.mTextViewTitle.setText(mArrayListWeek.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mDialogWeek.dismiss();
                    mTextViewWeekName.setText(mArrayListWeek.get(position));

                }
            });

            return convertView;
        }
    }

    static class ViewHolderWeek {
        TextView mTextViewTitle;
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (medicineNameAdapter != null)
            medicineNameAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    // show  Dialog SchedulePattern

    public void DilogSchedulePattern() {
        mDialogSchedulePattern = new Dialog(mainActivity);
        mDialogSchedulePattern.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogSchedulePattern.setContentView(R.layout.dialog_list_item);
        mDialogSchedulePattern.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mListViewSchedulePattern = mDialogSchedulePattern.findViewById(R.id.dialog_listview);
        MyBaseAdapterSchedulePattrn adapterSchedulePattrn = new MyBaseAdapterSchedulePattrn(mainActivity, R.layout.row_title, mArrayListSchedulePattern);
        mListViewSchedulePattern.setAdapter(adapterSchedulePattrn);
        mDialogSchedulePattern.show();
    }


    //   Adapter Schedule pattern
    public class MyBaseAdapterSchedulePattrn extends ArrayAdapter<String> {

        ArrayList<String> mArrayListpattern;

        public MyBaseAdapterSchedulePattrn(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            mArrayListpattern = objects;
        }

        @Override
        public int getCount() {
            return mArrayListpattern.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderSchedulePattern holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderSchedulePattern();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolderSchedulePattern) convertView.getTag();
            }

            holder.mTextViewTitle.setText(mArrayListpattern.get(position));


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mDialogSchedulePattern.dismiss();

                    mTextViewSchedulePattern.setText(mArrayListpattern.get(position));
                    if (mTextViewSchedulePattern.getText().toString().equalsIgnoreCase("By Day of Week")) {
                        mLinearLayoutInterval.setVisibility(View.GONE);
                        mLinearLayoutWeekname.setVisibility(View.VISIBLE);
                        mTextViewWeekName.setText(mArrayListWeek.get(0));
                        mTextViewInterval.setText("");

                    } else {
                        mLinearLayoutWeekname.setVisibility(View.GONE);
                        mLinearLayoutInterval.setVisibility(View.VISIBLE);
                        mTextViewWeekName.setText(" ");
                    }
                }
            });

            return convertView;

        }
    }

    static class ViewHolderSchedulePattern {
        TextView mTextViewTitle;

    }


//    medicine name dialog box code

    public void DialogMedicineName() {

        popupView = new Dialog(mainActivity);
        popupView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupView.setContentView(R.layout.dialog_country);
        popupView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        mAutoCompleteTextView = popupView.findViewById(R.id.dialog_country_autocomplettext);
        mAutoCompleteTextView.addTextChangedListener(this);

        mListView = popupView.findViewById(R.id.dialog_country_listview);
        mTextViewListTitle = popupView.findViewById(R.id.dialog_country_title);
        mTextViewListTitle.setText("Select Medicine Name");

        medicineNameAdapter = new MedicineNameAdapter(mainActivity, R.layout.row_title, mArrayListMedicineName);
        mListView.setAdapter(medicineNameAdapter);
        popupView.show();
    }


    public class MedicineNameAdapter extends ArrayAdapter<MedicineNameVo> {

        ArrayList<MedicineNameVo> mArrayList;
        ArrayList<MedicineNameVo> searchList;
        private Filter filter;

        public MedicineNameAdapter(Context context, int resource, ArrayList<MedicineNameVo> objects) {
            super(context, resource, objects);
            mArrayList = objects;
            searchList = objects;
        }

        public ArrayList<MedicineNameVo> getOriginalDataSet() {
            return mArrayList;
        }

        public ArrayList<MedicineNameVo> getSearchDataSet() {
            return searchList;
        }

        public void setSearchDataSet(ArrayList<MedicineNameVo> searchDataSet) {
            searchList = new ArrayList<MedicineNameVo>(searchDataSet);
            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            if (filter == null)
                filter = new CustomFilterState(this);
            return filter;
        }

        @Override
        public int getCount() {

            return searchList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolderMedicineName holder;

            if (convertView == null) {

                convertView = mainActivity.getLayoutInflater().inflate(R.layout.row_title, parent, false);
                holder = new ViewHolderMedicineName();

                holder.mTextViewTitle = convertView.findViewById(R.id.row_name_textview_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolderMedicineName) convertView.getTag();
            }


            holder.mTextViewTitle.setText(searchList.get(position).getMedicineName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mStringMedicineId = searchList.get(position).getMedicineId();
                    mStringMedicineName = searchList.get(position).getMedicineName();

                    mEditTextMedicineName.setText(mStringMedicineName);
                    popupView.dismiss();
//                    mCommonHelper.hideKeyboard(mainActivity);
//                    new FragmentAddAddress.GetCityDataExecute().execute();
                }
            });
            return convertView;
        }
    }

    static class ViewHolderMedicineName {
        TextView mTextViewTitle;
    }

    private class CustomFilterState extends Filter {

        MedicineNameAdapter adapter;

        public CustomFilterState(MedicineNameAdapter adapter) {
            this.adapter = adapter;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Utility.debugger(" filter called ");
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase(Locale.getDefault()).trim();
            if (prefix == null || prefix.length() == 0) {
                ArrayList<MedicineNameVo> list = new ArrayList<MedicineNameVo>(adapter.getOriginalDataSet());
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<MedicineNameVo> list = new ArrayList<MedicineNameVo>(adapter.getOriginalDataSet());
                final ArrayList<MedicineNameVo> nlist = new ArrayList<MedicineNameVo>();
                int count = list.size();
                for (int i = 0; i < count; i++) {
                    final MedicineNameVo menuItemVO = list.get(i);
                    final String value = menuItemVO.getMedicineName().toLowerCase(Locale.getDefault());

                    Utility.debugger("value: " + value + " prefix: " + prefix + " compare: " + value.contains(prefix));
                    if (value.contains(prefix)) {
                        nlist.add(menuItemVO);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.setSearchDataSet((ArrayList<MedicineNameVo>) filterResults.values);
        }
    }

    // get all medicine name list
    public class GetMedicineListProcess extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FetchMedicineNameService fetchMyOrderService = new FetchMedicineNameService();
            mServerResponseVOMedicinename = fetchMyOrderService.fetchMedicineData(mainActivity, Tags.GetAllMedicineName);
            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Void result) {

            if (isAdded()) {

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
                        if (mServerResponseVOMedicinename.getStatus().equalsIgnoreCase("true")) {

                            if (mServerResponseVOMedicinename.getData() != null) {
                                mArrayListMedicineName = (ArrayList<MedicineNameVo>) mServerResponseVOMedicinename.getData();

                                if (mArrayListMedicineName != null) {

                                    for (int i = 0; i < mArrayListMedicineName.size(); i++) {
//
//                                        mStringMedicineId = mArrayListMedicineName.get(i).getMedicineId();
                                        mStringMedicineName = mArrayListMedicineName.get(i).getMedicineName();
                                    }
                                }
                            }

                        }
                    } catch (Exception e) {

                    }
                }

            }
            super.onPostExecute(result);
        }

    }

    //    Save Medicine Reminder
    public void SaveMedicineReminder() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                SaveMedicineReminder saveMedicineReminder = new SaveMedicineReminder(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, saveMedicineReminder);
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
                                @SuppressLint("HandlerLeak") Handler handler = new Handler() {
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
                            e.printStackTrace();
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
    public class SaveMedicineReminder extends TaskExecutor {
        protected SaveMedicineReminder(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            SaveMedicineReminderService fetchGetProfilenfoService = new SaveMedicineReminderService();

            mServerResponseVOSave = fetchGetProfilenfoService.AddMedicine(mainActivity, Tags.AddSchedule, GetUserId, mEditTextRemark.getText().toString().trim(), mStringMedicineId, mEditTextMedicineName.getText().toString().trim(),
                    mTextViewStartDate.getText().toString().trim(), mTextViewEndDate.getText().toString().trim(), mTextViewSchedulePattern.getText().toString().trim(), mTextViewInterval.getText().toString().trim(),
                    mTextViewWeekName.getText().toString().trim(), jsonArray);

            return null;
        }
    }

    //    Reminder Save Method
    public void saveData() {
        mCommonHelper.hideKeyboard(mainActivity);
        if (!mCommonHelper.check_Internet(mainActivity)) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
            return;
        }

//        if(mStringMedicineId.equalsIgnoreCase(""))
//        {
//            mStringMEdicineIdSelecte ="";
//        }
//        else {
//            mStringMEdicineIdSelecte =mStringMedicineId;
//        }

        if (!validateMedicinName()) {
            return;
        }
        if (!validateStarDate()) {
            return;
        }

        if (!validateEndDate()) {
            return;
        }
//        if ((Date.parse(mTextViewStartDate.getText().toString().trim()) > Date.parse(mTextViewEndDate.getText().toString().trim()))) {
//            Snackbar.with(mainActivity).text("End Date should not be less than Start Date").show(mainActivity);
//            return;
//        }

//        if (!validateDosage()) {
//            return;
//        }

        if (mTextViewReminderTime.getText().toString().equalsIgnoreCase("") || mTextViewdosageRow.getText().toString().equalsIgnoreCase("") ||
                mCheckBoxAlarm.getText().toString().equalsIgnoreCase("")) {

            if (multiprescriptionItems.size() == 0) {
                Snackbar.with(mainActivity).text(getResources().getString(R.string.pls_enter_all_filed)).show(mainActivity);

            } else {
                arrayjson();
            }


        } else {

            MultiMedicineReminderItem multiMedicineReminderItem = new MultiMedicineReminderItem();
            multiMedicineReminderItem.setAlarm(mCheckboxAlaramValue);
            multiMedicineReminderItem.setDosage(mTextViewdosageRow.getText().toString().trim());
            multiMedicineReminderItem.setReminderTime(mTextViewReminderTime.getText().toString().trim());
            multiprescriptionItems.add(multiMedicineReminderItem);

//            mAdpter_reminder = new Adpter_Reminder(mainActivity, multiprescriptionItems);
//            mListViewReminder.setHasFixedSize(true);
//            mListViewReminder.setLayoutManager(new LinearLayoutManager(mainActivity));
//            mListViewReminder.setAdapter(mAdpter_reminder);

            arrayjson();
        }
        SaveMedicineReminder();

    }

    // creare jasson array
    public void arrayjson() {
        try {

            jsonArray = new JSONArray();

            for (int j = 0; j < multiprescriptionItems.size(); j++) {
                if (!removeItem.contains(String.valueOf(j))) {
                    JSONObject jSONObject = new JSONObject();
                    JSONObject jSONObject2 = new JSONObject();

                    jSONObject.put("ReminderTime", multiprescriptionItems.get(j).getReminderTime());
                    jSONObject.put("dosage", multiprescriptionItems.get(j).getDosage());
                    jSONObject.put("alarm", multiprescriptionItems.get(j).getAlarm());


                    jsonArray.put(jSONObject);
                    PrintStream printStream = System.out;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean validateMedicinName() {
        String email = mEditTextMedicineName.getText().toString().trim();

        if (email.isEmpty()) {
            mTextInputLayoutMedicineName.setError(getResources().getString(R.string.validmedicinename));
            requestFocus(mEditTextMedicineName);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        } else {
            mTextInputLayoutMedicineName.setError(null);
            mTextInputLayoutMedicineName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateStarDate() {
        String mStringstartdate = mTextViewStartDate.getText().toString().trim();
        if (mStringstartdate.equalsIgnoreCase("")) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.start_date)).show(mainActivity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        }
        return true;
    }

    private boolean validateEndDate() {
        String mStringenddate = mTextViewEndDate.getText().toString().trim();
        if (mStringenddate.equalsIgnoreCase("")) {
            Snackbar.with(mainActivity).text(getResources().getString(R.string.enddate)).show(mainActivity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        }
        return true;
    }

    private boolean validateDosage() {
        String mStringstartdate = mTextViewdosageRow.getText().toString().trim();
        if (mStringstartdate.equalsIgnoreCase("")) {
            Snackbar.with(mainActivity).text("Enter Dosage value").show(mainActivity);
            mCommonHelper.hideKeyboard(mainActivity);
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
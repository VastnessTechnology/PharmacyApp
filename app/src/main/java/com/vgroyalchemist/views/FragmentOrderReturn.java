package com.vgroyalchemist.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vgroyalchemist.R;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.loaders.LoaderTask;
import com.vgroyalchemist.loaders.TaskExecutor;
import com.vgroyalchemist.requestobjects.ReturnReasonVO;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.SweetAlertDialogSingleButton;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.webservice.AddOrderReturnService;
import com.vgroyalchemist.webservice.GetReturnresultService;
import com.vgroyalchemist.webservice.PostParseGet;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/*  developed by krishna 24-03-2021
* order return class */
public class FragmentOrderReturn extends NonCartFragment {


    ///Variable Declaration
    public static final String FRAGMENT_ID = "19";

    public static MainActivity mainActivity;
    public View fragmentView = null;
    CommonHelper mCommonHelper;

    ImageView mImageViewBack;
    TextView mTextViewTitle;
    ImageView mImageViewSearch;
    ImageView mImageViewCart;


    ImageView mImageViewMoreUpload;
    ImageView mImageViewUploadResons;
    RecyclerView mRecyclerView;
    TextView mTextViewOrderId;
    EditText mEditTextAdditonalResons;
    TextView mTextViewSubmit;
    LinearLayout mLinearLayoutcheckbox;
    RecyclerView mRecyclerViewCheckbox;

    ArrayList mArrayListcheckbox;
    ArrayList<ReturnReasonVO> mStringResones;
    static Bitmap mBitmap;
    ArrayList<Uri> mArrayUri;
    List<String> imagesEncodedList;
    Uri mImageUri;
    String imageEncoded;
    static ArrayList<Bitmap> mArrayListImage;
    PostParseGet mPostParseGet;
    ServerResponseVO mServerResponseVOGetReson;
    ServerResponseVO mServerResponseVO;
    String mStringGetResonid;
    static String mStringImageBase64;
    String mStringAdditiontext;
    CheckBox checkBox;
    LinearLayout.LayoutParams checkParams;
    // userdata
    public SharedPreferences mSharedPreferencesUserId;
    SharedPreferences.Editor mEditor;
    String mStringUserId;
    String OrderId;

    ArrayList mImageVOArrayList;
    Bundle mBundle;
    String img_count;


    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int SELECT_PICTURE = 732;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mArrayListImage = new ArrayList<Bitmap>();
        mArrayUri = new ArrayList<Uri>();
        mPostParseGet = new PostParseGet(mainActivity);
        mStringResones = new ArrayList<ReturnReasonVO>();
        mArrayListcheckbox = new ArrayList();
        mImageVOArrayList = new ArrayList();
        mBundle = getArguments();
        if (mBundle != null) {
            OrderId = mBundle.getString("OrderID");
        }


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_orderreturn, container, false);

        mImageViewBack = fragmentView.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmentView.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Return order");
        mImageViewSearch = fragmentView.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmentView.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);


        mImageViewMoreUpload = fragmentView.findViewById(R.id.fragment_order_return_imageview_upload_More_image);
        mImageViewUploadResons = fragmentView.findViewById(R.id.fragment_order_return_imageview_upload);
        mRecyclerView = fragmentView.findViewById(R.id.fragment_order_return_recycleview);
        mEditTextAdditonalResons = fragmentView.findViewById(R.id.fragment_order_return_edit_additonal_details);
        mTextViewSubmit = fragmentView.findViewById(R.id.fragment_order_return_textview_submit);
        mLinearLayoutcheckbox = fragmentView.findViewById(R.id.fragment_order_return_Linear_checkbox);
        mRecyclerViewCheckbox = fragmentView.findViewById(R.id.fragment_order_return_recycleview_issue);


        mSharedPreferencesUserId = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_USER_ID, 0);
        mStringUserId = mSharedPreferencesUserId.getString("UserId", "");
        mEditor = mSharedPreferencesUserId.edit();


        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {

                ReturnOrderExecutor returnOrderExecutor = new ReturnOrderExecutor(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, returnOrderExecutor);
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

                        if (mServerResponseVOGetReson != null && mServerResponseVOGetReson.getStatus() != null) {

                            if (mServerResponseVOGetReson.getStatus().equalsIgnoreCase("true")) {
//                                Snackbar.with(mainActivity).text(mServerResponseVOGetReson.getMsg()).show(mainActivity);

                                mStringResones = (ArrayList<ReturnReasonVO>) mServerResponseVOGetReson.getData();

                                if (mStringResones != null && mStringResones.size() > 0) {


                                    ReturnResonAdapter returnResonAdapter = new ReturnResonAdapter(mainActivity, mStringResones);
                                    mRecyclerViewCheckbox.setHasFixedSize(true);
                                    mRecyclerViewCheckbox.setLayoutManager(new LinearLayoutManager(mainActivity));
                                    mRecyclerViewCheckbox.setAdapter(returnResonAdapter);
                                }
                            }
                        } else {
                            Snackbar.with(mainActivity).text(mServerResponseVOGetReson.getMsg()).show(mainActivity);
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {

            }
        });


        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment currentFrag =mainActivity.getSupportFragmentManager().findFragmentById(R.id.activity_main_container);
//                mCurrentFragment = currentFrag.getClass().getName();
//                mainActivity. removeFragment(currentFrag, currentFrag.getClass().getName());
                mainActivity.onBackPressed();
            }
        });

        mImageViewUploadResons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    verifyStoragePermissions(mainActivity);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.enable_permission)).show(mainActivity);
                    return;
                }

                selectImage();
            }
        });

        mImageViewMoreUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    verifyStoragePermissions(mainActivity);
                    return;
                }

                if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.enable_permission)).show(mainActivity);
                    return;
                }
                selectImage();
            }
        });

        mTextViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStringAdditiontext = mEditTextAdditonalResons.getText().toString().trim();
                if (mArrayListcheckbox.size() == 0) {
                    Snackbar.with(mainActivity).text(getResources().getString(R.string.return_reason_validate)).show(mainActivity);
                    return;
                }
                if (mArrayListcheckbox != null) {
                    mStringGetResonid = mArrayListcheckbox.toString().replace("[", "").replace("]", "");
                    Utility.debugger("VT check box id...." + mStringGetResonid);
                }

                addOrderReturn();
            }
        });
        return fragmentView;
    }

    public static JSONObject createCustomOptionJsonSingleNew() {

        JSONObject optionJSON = new JSONObject();

        try {
            try {
                int i = 0;
                while (i < mArrayListImage.size()) {
                    mBitmap = mArrayListImage.get(i);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bao);
                    byte[] ba = bao.toByteArray();
                    mStringImageBase64 = Base64.encodeToString(ba, Base64.DEFAULT);
                    i++;

                    optionJSON.put("VastnessTechnology" + i, mStringImageBase64);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionJSON;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                onCaptureImageResult(data);
            } else {
                onSelectFromGalleryResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {

        mBitmap = (Bitmap) data.getExtras().get("data");
        mArrayListImage.add(mBitmap);
        Uri imageUri = getImageUri(getApplicationContext(), mBitmap);
        File file = new File(getRealPathFromURI(imageUri));
        PrintStream printStream = System.out;
        printStream.println("Vt URI ----->" + imageUri);
        PrintStream printStream2 = System.out;
        printStream2.println("Vt file ----->" + file);
        mArrayUri.add(imageUri);

        if (mArrayUri != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mImageViewUploadResons.setVisibility(View.GONE);
            mImageViewMoreUpload.setVisibility(View.VISIBLE);
            callRecyclview();

        } else {
            mRecyclerView.setVisibility(View.GONE);
            mImageViewUploadResons.setVisibility(View.VISIBLE);
            mImageViewMoreUpload.setVisibility(View.GONE);
        }


    }


    private void onSelectFromGalleryResult(Intent data) {
        try {
            String[] strArr = {"_data"};
            imagesEncodedList = new ArrayList();
            if (data.getData() != null) {
                mImageUri = data.getData();
                Cursor query = getActivity().getContentResolver().query(mImageUri, strArr, null, null, null);
                query.moveToFirst();
                imageEncoded = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
//                if(mImageUri.getPath().endsWith(".gif"))
//                {
//                    SweetAlertDialogSingleButton mSweetAlertDialog = new SweetAlertDialogSingleButton(mainActivity)
//                            .setContentText(getResources().getString(R.string.gifalert))
//                            .setConfirmText(getResources().getString(R.string.dialog_ok))
//                            .setConfirmClickListener(new SweetAlertDialogSingleButton.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialogSingleButton sDialog) {
//                                    sDialog.dismissWithAnimation();
//
//                                }
//                            });
//                    mSweetAlertDialog.setCancelable(false);
//                    mSweetAlertDialog.show();
//                    return;
//                }
//                else {
                    mArrayUri.add(mImageUri);
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                    mArrayListImage.add(mBitmap);
                    callRecyclview();
//                }
            } else if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                    Uri uri = clipData.getItemAt(i3).getUri();
                    mArrayUri.add(uri);
                    Cursor query2 = getActivity().getContentResolver().query(uri, strArr, null, null, null);
                    query2.moveToFirst();
                    imageEncoded = query2.getString(query2.getColumnIndex(strArr[0]));
                    imagesEncodedList.add(imageEncoded);
                    query2.close();
//                    if(uri.getPath().endsWith(".gif"))
//                    {
//                        SweetAlertDialogSingleButton mSweetAlertDialog = new SweetAlertDialogSingleButton(mainActivity)
//                                .setContentText(getResources().getString(R.string.gifalert))
//                                .setConfirmText(getResources().getString(R.string.dialog_ok))
//                                .setConfirmClickListener(new SweetAlertDialogSingleButton.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialogSingleButton sDialog) {
//                                        sDialog.dismissWithAnimation();
//
//                                    }
//                                });
//                        mSweetAlertDialog.setCancelable(false);
//                        mSweetAlertDialog.show();
//                        return;
//                    }
//                    else {
                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        mArrayListImage.add(mBitmap);
                        callRecyclview();
//                    }
                }
                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
            }

            if (mArrayUri != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mImageViewUploadResons.setVisibility(View.GONE);
                mImageViewMoreUpload.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mImageViewUploadResons.setVisibility(View.VISIBLE);
                mImageViewMoreUpload.setVisibility(View.GONE);
            }
        } catch (Exception unused) {
            Toast.makeText(mainActivity, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public Uri getImageUri(Context context, Bitmap bitmap2) {
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap2, "Title", (String) null));
    }


    private void callRecyclview() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        ReturnOrderAdapter customAdapter = new ReturnOrderAdapter(mainActivity, mArrayUri);
        mRecyclerView.setAdapter(customAdapter);
    }

    public class ReturnOrderAdapter extends RecyclerView.Adapter<ReturnOrderAdapter.MyViewHolderCancleOrder> {

        Context mContext;
        ArrayList<Uri> mArrayList;

        public ReturnOrderAdapter(MainActivity mainActivity, ArrayList<Uri> mArrayUri) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayUri;
        }

        @NonNull
        @Override
        public MyViewHolderCancleOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_prescription, parent, false);
            MyViewHolderCancleOrder vh = new MyViewHolderCancleOrder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderCancleOrder holder, int position) {

            holder.mImageViewSelectIamge.setImageURI(mArrayList.get(position));
            holder.mImageViewDelete.setTag(position);
            holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mArrayList.remove(position);
                    mArrayListImage.remove(position);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
                    mRecyclerView.setLayoutManager(gridLayoutManager);
                    ReturnOrderAdapter customAdapter = new ReturnOrderAdapter(mainActivity, mArrayList);
                    mRecyclerView.setAdapter(customAdapter);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolderCancleOrder extends RecyclerView.ViewHolder {
            ImageView mImageViewSelectIamge;
            ImageView mImageViewDelete;

            public MyViewHolderCancleOrder(View itemView) {
                super(itemView);
                mImageViewSelectIamge = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview);
                mImageViewDelete = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview_delete);
            }
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


    //    Order Return reson List
    public class ReturnOrderExecutor extends TaskExecutor {

        protected ReturnOrderExecutor(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {
            try {
                GetReturnresultService getReturnresultService = new GetReturnresultService();
                mServerResponseVOGetReson = getReturnresultService.getresonreturn(mainActivity, Tags.GETRETURNRESON);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                    intent.setAction("android.intent.action.GET_CONTENT");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


//    add order return process

    public void addOrderReturn() {

        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<TaskExecutor>() {
            @Override
            public Loader<TaskExecutor> onCreateLoader(int arg0, Bundle arg1) {
                AddOrderReturn addOrderReturn = new AddOrderReturn(mainActivity, null);
                return new LoaderTask<TaskExecutor>(mainActivity, addOrderReturn);
            }

            @Override
            public void onLoadFinished(Loader<TaskExecutor> arg0, TaskExecutor arg1) {

                if (isAdded()) {

                    getLoaderManager().destroyLoader(0);
                    if (mPostParseGet.isNetError) {
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.interneterror)).show(mainActivity);
                    } else if (mPostParseGet.isOtherError)
                        Snackbar.with(mainActivity).text(getResources().getString(R.string.notabletocommunicatewithserver)).show(mainActivity);
                    else {
                        if (mServerResponseVO != null && mServerResponseVO.getStatus() != null) {

                            if (mServerResponseVO.getStatus().equalsIgnoreCase("true")) {

                                if (mServerResponseVO.getData() != null) {
                                    Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);

                                    Handler handler = new Handler() {
                                        @SuppressLint("HandlerLeak")
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.what == Tags.WHAT) {

                                                mainActivity.removeAllBackFragments();
                                                FragmentMyOrderDetails fragmentMyOrderDetails = new FragmentMyOrderDetails();
                                                Bundle mBundle = new Bundle();
                                                mBundle.putString("OrderId",OrderId);
                                                fragmentMyOrderDetails.setArguments(mBundle);
                                                mainActivity.addFragment(fragmentMyOrderDetails, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMyOrderDetails.getClass().getName());

                                            }
                                        }
                                    };
                                    handler.sendEmptyMessage(Tags.WHAT);

                                }
                            } else {
                                Snackbar.with(mainActivity).text(mServerResponseVO.getMsg()).show(mainActivity);
                                mCommonHelper.hideKeyboard(mainActivity);
                            }
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<TaskExecutor> arg0) {
            }
        });
    }


    public class AddOrderReturn extends TaskExecutor {

        protected AddOrderReturn(Context context, Bundle bundle) {
            super(context, bundle);
        }

        @Override
        protected Object executeTask() {

            AddOrderReturnService mGetAllCartService = new AddOrderReturnService();

            try {
                mServerResponseVO = mGetAllCartService.addorderreturn(mainActivity, Tags.ADDORDERRETURN, OrderId, mStringUserId, mStringGetResonid, mStringAdditiontext, mImageVOArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    // Return reson Adapter
    public class ReturnResonAdapter extends RecyclerView.Adapter<ReturnResonAdapter.MyViewHolderReson> {


        Context mContext;
        ArrayList<ReturnReasonVO> mArrayListitem;


        public ReturnResonAdapter(MainActivity mainActivity, ArrayList<ReturnReasonVO> mArrayListOrderItem) {
            this.mContext = mainActivity;
            this.mArrayListitem = mArrayListOrderItem;
        }

        @NonNull
        @Override
        public MyViewHolderReson onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_checkbox, parent, false);
            MyViewHolderReson viewHolderOrderItem = new MyViewHolderReson(v);
            return viewHolderOrderItem;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderReson holder, int position) {

            if (!mArrayListitem.get(position).getReason().equalsIgnoreCase("null") && mArrayListitem.get(position).getReason() != null) {
                holder.mTextViewResonText.setText(mArrayListitem.get(position).getReason());
            }
            holder.mImageView.setTag(mArrayListitem.get(position));


            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.mImageViewSelected.setVisibility(View.VISIBLE);
                    holder.mImageView.setVisibility(View.GONE);
                    mArrayListcheckbox.add(mArrayListitem.get(position).getReasonId());

                }
            });

            holder.mImageViewSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mImageViewSelected.setVisibility(View.GONE);
                    holder.mImageView.setVisibility(View.VISIBLE);
                    mArrayListcheckbox.remove(mArrayListitem.get(position).getReasonId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayListitem.size();
        }

        public class MyViewHolderReson extends RecyclerView.ViewHolder {

            TextView mTextViewResonText;
            ImageView mImageView;
            ImageView mImageViewSelected;


            public MyViewHolderReson(View itemView) {
                super(itemView);

                mTextViewResonText = itemView.findViewById(R.id.row_checkbox_textview_reason_text);
                mImageView = itemView.findViewById(R.id.row_checkbox_imageview);
                mImageViewSelected = itemView.findViewById(R.id.row_checkbox_imageview_selected);
            }
        }
    }

//      this funtion use ask permission
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
//        int callPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
//
//        if (callPermission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
        int callPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
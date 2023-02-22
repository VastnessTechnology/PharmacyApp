package com.vgroyalchemist.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vgroyalchemist.CommonClass.CommonClass;
import com.vgroyalchemist.R;

import com.vgroyalchemist.controller.ActivitySplashScreen;
import com.vgroyalchemist.controller.MainActivity;
import com.vgroyalchemist.utils.CommonHelper;
import com.vgroyalchemist.utils.Snackbar;
import com.vgroyalchemist.utils.SweetAlertDialogSingleButton;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.MYPrescriptionVO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParserFactory;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.vgroyalchemist.views.FragmentOrderReturn.verifyStoragePermissions;

/*  developed by krishna 24-03-2021
 *  select image  camera gallery and uploaded prescription */
public class FragmentUploadPrescription extends NonCartFragment {


    public static final String FRAGMENT_ID = "23";

    public static MainActivity mainActivity;
    CommonHelper mCommonHelper;
    View fragmnetview = null;
    ImageView mImageViewBack;
    TextView mTextViewTitle;
    TextView mTextViewContinue;
    LinearLayout mLinearLayoutCamera;
    LinearLayout mLinearLayoutGallery;
    LinearLayout mLinearLayoutPresciption;
    public RecyclerView mRecyclerView;

    ArrayList<Bitmap> mArrayListImage;

    CommonClass mCommonClass;
    String IMAGE_DIRECTORY_NAME = "/ImageDirectory";
    private Uri fileUri1;

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int SELECT_PICTURE = 732;

    ImageView mImageViewSearch;
    ImageView mImageViewCart;
    Bitmap mBitmap;
    ArrayList<Uri> mArrayUri;

    List<String> imagesEncodedList;
    Uri mImageUri;
    String imageEncoded;
    Bundle mBundle;

    File file;
    URL myFileUrl;

    //    address sharepreferance
    public SharedPreferences mSharedPreferencesAddress;
    SharedPreferences.Editor mEditorAddress;
    boolean AddAddress;

    RelativeLayout mRelativeLayoutGuide;
    RelativeLayout mRelativeLayoutWhy;

    //login
    public SharedPreferences mSharedPreferencesLoginFirst;
    SharedPreferences.Editor mEditorLogin;
    String GuestUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mCommonHelper = new CommonHelper();
        mArrayListImage = new ArrayList<Bitmap>();
        mCommonClass = new CommonClass();
        mArrayUri = new ArrayList<Uri>();
        mBundle = getArguments();

        if (mBundle != null) {
            mArrayUri = (ArrayList<Uri>) mBundle.getSerializable("SelectImage");
            new SaveTask().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmnetview = inflater.inflate(R.layout.fragment_upload_prescription, container, false);

        mImageViewBack = fragmnetview.findViewById(R.id.fragment_imageview_back);
        mTextViewTitle = fragmnetview.findViewById(R.id.fragment_title_textview);
        mTextViewTitle.setText("Upload Prescription");
        mImageViewSearch = fragmnetview.findViewById(R.id.fragment_Serach);
        mImageViewSearch.setVisibility(View.GONE);
        mImageViewCart = fragmnetview.findViewById(R.id.fragment_cart);
        mImageViewCart.setVisibility(View.GONE);
        mTextViewContinue = fragmnetview.findViewById(R.id.fragment_upload_prescription_textview_submit);
        mLinearLayoutCamera = fragmnetview.findViewById(R.id.fragment_upload_prescription_linear_camera);
        mLinearLayoutGallery = fragmnetview.findViewById(R.id.fragment_upload_prescription_linear_gallery);
        mLinearLayoutPresciption = fragmnetview.findViewById(R.id.fragment_upload_prescription_linear_myprescription);
        mRecyclerView = fragmnetview.findViewById(R.id.fragment_upload_prescription_recyclerview);

        mRelativeLayoutGuide = fragmnetview.findViewById(R.id.fragment_Menu_linaer_Guide);
        mRelativeLayoutWhy = fragmnetview.findViewById(R.id.fragment_Menu_Liner_WhyPrec);

        mSharedPreferencesLoginFirst = mainActivity.getSharedPreferences(Tags.PREFS_NAME, 0);
        mEditorLogin = mSharedPreferencesLoginFirst.edit();
        GuestUser = mSharedPreferencesLoginFirst.getString("GuestUser", "0");

        mSharedPreferencesAddress = mainActivity.getSharedPreferences(Tags.TAG_SHARED_PREFRENCE_Address, 0);
        mEditorAddress = mSharedPreferencesAddress.edit();
        AddAddress = mSharedPreferencesAddress.getBoolean("AddAddress", false);

        mRelativeLayoutGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentGuidePrescription fragmentGuidePrescription = new FragmentGuidePrescription();
                mainActivity.addFragment(fragmentGuidePrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentGuidePrescription.getClass().getName());

            }
        });

        mRelativeLayoutWhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentWhyPrescription fragmentWhyPrescription = new FragmentWhyPrescription();
                mainActivity.addFragment(fragmentWhyPrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentWhyPrescription.getClass().getName());
            }
        });

        if (mArrayUri != null && mArrayUri.size() > 0) {
            callRecyclview();

        }
        mLinearLayoutPresciption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GuestUser.equalsIgnoreCase("1")) {
                    FragmentLoginScreen fragmentLoginScreen = new FragmentLoginScreen();
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean(Tags.IS_FROM_LOGIN, true);
                    fragmentLoginScreen.setArguments(mBundle);
                    mainActivity.addFragment(fragmentLoginScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginScreen.getClass().getName());
                } else {
                    FragmentMyPrescription fragmentMyPrescription = new FragmentMyPrescription();
                    mainActivity.addFragment(fragmentMyPrescription, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentMyPrescription.getClass().getName());
                }

            }
        });
        mLinearLayoutCamera.setOnClickListener(new View.OnClickListener() {
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

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
                startActivityForResult(intent, 1);
            }
        });

        mLinearLayoutGallery.setOnClickListener(new View.OnClickListener() {
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

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                intent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        mTextViewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GuestUser.equalsIgnoreCase("1")) {

                    FragmentLoginScreen fragmentLoginScreen = new FragmentLoginScreen();
                    Bundle mBundle = new Bundle();
                    mBundle.putBoolean(Tags.IS_FROM_LOGIN, true);
                    fragmentLoginScreen.setArguments(mBundle);
                    mainActivity.addFragment(fragmentLoginScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, fragmentLoginScreen.getClass().getName());
                } else {


                    if (mArrayListImage.size() == 0) {
                        errorpopup();
                    } else {
                        FragmentOrderSummary mFragmentOrderSummary = new FragmentOrderSummary();
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("imagearray", mArrayListImage);
                        mFragmentOrderSummary.setArguments(mBundle);
                        mainActivity.addFragment(mFragmentOrderSummary, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, mFragmentOrderSummary.getClass().getName());


                    }
                }
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        return fragmnetview;
    }


    public class SaveTask extends AsyncTask<String, String, String> {

        URL myFileUrl;

        SaveTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                for (int i = 0; i < mArrayUri.size(); i++) {
                    myFileUrl = new URL(mArrayUri.get(i).toString());

                    HttpURLConnection connection = (HttpURLConnection) myFileUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    mBitmap = BitmapFactory.decodeStream(input);
                    mArrayListImage.add(mBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String args) {

        }
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
        callRecyclview();
    }

    @SuppressLint("Range")
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

                PrintStream printStream = System.out;
                printStream.println("Vt URI ----->" + mImageUri);

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
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    mArrayUri.add(uri);
                    Cursor query2 = getActivity().getContentResolver().query(uri, strArr, null, null, null);
                    query2.moveToFirst();
                    imageEncoded = query2.getString(query2.getColumnIndex(strArr[0]));
                    imagesEncodedList.add(imageEncoded);
                    query2.close();

                    PrintStream printStream = System.out;
                    printStream.println("Vt URI ----->" + uri);

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
        } catch (Exception unused) {
            Toast.makeText(mainActivity, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void errorpopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(getResources().getString(R.string.prescriptionerrormsg));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return;
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
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap2, "ThisIsImageTitleString", null));
    }


    public void callRecyclview() {
        mRecyclerView.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        PrescriptionImageAdapter customAdapter = new PrescriptionImageAdapter(mainActivity, mArrayUri);
        mRecyclerView.setAdapter(customAdapter);
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

    public class PrescriptionImageAdapter extends RecyclerView.Adapter<PrescriptionImageAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<Uri> mArrayList;

        public PrescriptionImageAdapter(MainActivity mainActivity, ArrayList<Uri> mArrayUri) {
            this.mContext = mainActivity;
            this.mArrayList = mArrayUri;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_prescription, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            if (mArrayList.get(position) != null) {
                Glide.with(mContext)
                        .load(mArrayList.get(position))
                        .placeholder(R.drawable.logo)
                        .centerCrop()
                        .into(holder.mImageViewSelectIamge);
            }
//            holder.mImageViewSelectIamge.setImageURI(mArrayList.get(position));
            Utility.debugger("VT selected item --->" + mArrayList.get(position));
            holder.mImageViewDelete.setTag(position);
            holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mArrayList.remove(position);
                    mArrayListImage.remove(position);
                    callRecyclview();
                }
            });


        }


        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageViewSelectIamge;
            ImageView mImageViewDelete;

            public MyViewHolder(View itemView) {
                super(itemView);
                mImageViewSelectIamge = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview);
                mImageViewDelete = (ImageView) itemView.findViewById(R.id.row_upload_prescription_imageview_delete);
            }
        }
    }

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
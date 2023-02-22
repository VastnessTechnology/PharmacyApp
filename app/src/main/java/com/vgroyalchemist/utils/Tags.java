package com.vgroyalchemist.utils;
public class Tags {


    public static final int WHAT = 1;
    public static String PACKAGENAME = "com.vgroyalchemist";
    public static String FOLDERPATH = "/Android/data/" + Tags.PACKAGENAME + "/";

    public static final String TAG_FRAGMENT_HOME_NEW_THEME = PACKAGENAME + ".views.FragmentHomeNewTheme";
    public static final String TAG_FRAGMENT_LOGIN = PACKAGENAME + ".views.FragmentLoginScreen";
    public static final String TAG_FRAGMENT_UPLOAD_PRESCRIPTION = PACKAGENAME + ".views.FragmentUploadPrescription";
    public static final String TAG_FRAGMENT_ORDER_PRESCRIPTION = PACKAGENAME + ".views.FragmentOrderWithPresciption";
    public static final String TAG_FRAGMENT_MEDICINES = PACKAGENAME + ".views.FragmentMedicines";
    public static final String TAG_FRAGEMNT_ARTICLES = PACKAGENAME + ".views.FragmentArticles";
    public static final String TAG_FRAGEMENT_CART = PACKAGENAME + ".views.FragmentCart";
    public static final String TAG_FRAGEMENT_PROFILE = PACKAGENAME + ".views.FragmentProfile";
    public static final String TAG_FRAGEMENT_SERCH_SUSITITUE = PACKAGENAME + ".views.FragmentSearchSubstitutes";
    public static final String TAG_FRAGEMENT_ADD_ADDRESS = PACKAGENAME + ".views.FragmentAddAddress";
    public static final String TAG_FRAGEMENT_SELECT_ADDRESS = PACKAGENAME + ".views.FragmentSelectAddress";
    public static final String TAG_FRAGEMENT_ARTICLES = PACKAGENAME + ".views.FragmentArticles";
    public static final String TAG_FRAGEMENT_SCHEDULE = PACKAGENAME + ".views.FragmentSchedule";
    public static final String TAG_FRAGMENT_CONFIRMORDER = PACKAGENAME + ".views.FragmentConfirmOrder";
    public static final String TAG_FRAGMENT_ABOUTUS = PACKAGENAME + ".views.FragmentAboutUs";
    public static final String TAG_FRAGMENT_PREFERENCE = PACKAGENAME + ".views.FragmentPreference";
    public static final String TAG_FRAGMENT_NOTIFICATION = PACKAGENAME + ".views.FragmentNotification";
    public static final String TAG_FRAGMENT_SEARCH_ORDER_SUMMARY = PACKAGENAME + ".views.FragmentSearchOrderSummary";
    public static final String TAG_FRAGMENT_VITAL = PACKAGENAME + ".views.FragmentVital";
    public static final String TAG_FRAGMENT_LEGAL = PACKAGENAME + ".views.FragmentLegal";
    public static final String TAG_FRAGMENT_COMPLAINLIST = PACKAGENAME + ".views.FragmentComplainList";
    public static final String TAG_FRAGMENT_REFER_EARN = PACKAGENAME + ".views.FragmentReferEarn";
    public static final String TAG_FRAGMENT_MENU = PACKAGENAME + ".views.FragmentMenu";
    public static final String TAG_FRAGMENT_TAB = PACKAGENAME + ".views.FragmentTabActivity";


    public static String mStringDeviceToken = "12345";
    public static String REG_ID = "";
    public static String IS_FROM_LOGIN = "IS_FROM_LOGIN";
    public static String isFromEdit = "isFromEdit";
    public static String KEY_ADDRESS_DATA = "KEY_ADDRESS_DATA";
    public static String COUNT = "COUNT";
    public static String NOTICOUNT = "NotiCOUNT";
    public static final String DB_NAME = "VGpharma.sqlite";

    public static String User_Id = "user_id";
    public static String Email_Id = "email";

    public static final String PARAM_STATUS = "status";
    public static final String PARAM_MSG = "msg";
    public static final String PARAM_DETAILS = "details";
    public static String SOCIAL_FACEBOOK = "Facebook";
    public static String SOCIAL_GOOGLE = "Google";


    public static final String DECIMAL_FORMAT = "##,###,##0.##";
    public static String LABEL_CURRENCY_SIGN = "";

    public static int CounterHome;
    public static boolean bln_HomePage;
    public static boolean bln_Page_Not_Leftmenu;

//    public static String BASEURL = "http://vgpharma.vastnesstechnology.co.in/";

   //public static String BASEURL = "http://122.170.0.183:9090/";
   public static String BASEURL = "http://vgapi.vgrc.in/";
    //public static String BASEURL = "http://192.168.1.22:9090/";


    public static String Signup = BASEURL + "PatUsers/" + "SignUp";
//    public static String Signup = BASEURL + "user/" + "SignUp";
    public static String Login = BASEURL + "user/" + "Login";
    public static String OTPVerify = BASEURL + "PatUsers/" + "OtpVerification";

    public static String CheckEmail = BASEURL + "PatUsers/" + "UpdateEmailID";
    public static String ForgotPasswordSendEmail = BASEURL + "user/" + "FPSentEmailId";
    public static String verifyOtp = BASEURL + "user/" + "FPCheckOtp";
    public static String ResendOtp = BASEURL + "user/" + "ReSentOtp";
    public static String SetNewPassword = BASEURL + "user/" + "FPNewPwd";
    public static String Social_signup = BASEURL + "user/" + "SocialSignUp";

    public static String GetProfile = BASEURL + "Account/" + "GetProfileData";
    public static String UpdateProfile = BASEURL + "Account/" + "UpdateProfile";
    public static String AddAddress = BASEURL + "Account/" + "AddUserAddress";
    public static String ChnagePassword = BASEURL + "Account/" + "ChangePassword";
    public static String UpdateAddress = BASEURL + "Account/" + "UpdateUserAddress";
    public static String DeleteAddress = BASEURL + "Account/" + "DeleteUserAddress";
    public static String GETDEFUALTADDRESS = BASEURL + "Account/" + "GetDefaultAddress";
    public static String GetAllUserAddress = BASEURL + "Account/" + "GetAllAddressData";
    public static String GetStatesWebservice = BASEURL + "Account/" + "GetState";
    public static String getCityData = BASEURL + "Account/" + "GetCity";
    public static String GetAddressData = BASEURL + "Account/" + "GetAddressData";
    public static String CheckProfileMobileNo = BASEURL + "Account/" + "CheckProfileMobileNo";
    public static String MobileOtpVerification = BASEURL + "Account/" + "MobileOtpVerification";
    public static String MobileReSentOtp = BASEURL + "Account/" + "MobileReSentOtp";

    public static String SearchMedicine = BASEURL + "Medicine/" + "SearchMedicine";
    public static String MedicineList = BASEURL + "Medicine/" + "ListMedicineCategory";
    public static String MedicineCategoryList = BASEURL + "Medicine/" + "ListMedicineFromCategory";
    public static String ListAllAtricles = BASEURL + "Medicine/" + "ListAllArticles";
    public static String GetBookmarkDetail = BASEURL + "Medicine/" + "GetBookmarkDetails";
    public static String AddBookMark = BASEURL + "Medicine/" + "AddBookmark";
    public static String ProductDeatils = BASEURL + "Medicine/" + "SearchMedicineDetails";
    public static String REMOVEBOOKMARK = BASEURL + "Medicine/" + "RemoveBookmark";
    public static String FINDALMEDICINE = BASEURL + "Medicine/" + "FindAltMedicine";
    public static String GetAllMedicineName = BASEURL + "Medicine/" + "GetAllMedicineName";

    public static String GetBannerMenu = BASEURL + "order/" + "GetAllBannerMenu";
    public static String GetAllBanner = BASEURL + "order/" + "GetAllBanner";

    public static String AddCard = BASEURL + "Order/" + "AddtoCard";
    public static String GETALLCARTDATA = BASEURL + "Order/" + "GetAllCartData";
    public static String GETCOUNT = BASEURL + "Order/" + "GetCount";
    public static String DELETECART = BASEURL + "Order/" + "DeleteFromCart";
    public static String UPDATECARTDATA = BASEURL + "Order/" + "UpdateCartData";
    public static String PLACEORDER = BASEURL + "Order/" + "PlaceOrder";
    public static String GETALLORDER = BASEURL + "Order/" + "GetAllOrders";
    public static String REORDER = BASEURL + "Order/" + "ReOrder";
    public static String GETALLREVIEW = BASEURL + "Order/" + "GetAllReview";
    public static String GETCOUPEN = BASEURL + "Order/" + "GetAllCoupen";
    public static String CANCLEORDER = BASEURL + "Order/" + "CancelOrder";
    public static String ADDREVIEW = BASEURL + "Order/" + "AddtoReview";
    public static String SENDMESSAGE = BASEURL + "Order/" + "SaveDeliveryMessage";
    public static String GETORDERDetails = BASEURL + "Order/" + "GetOrderDetails";
    public static String GETRETURNRESON = BASEURL + "Order/" + "GetAllReturnReason";
    public static String ADDORDERRETURN = BASEURL + "Order/" + "ReturnOrder";
    public static String DeLIVERYFEEDBACK = BASEURL + "Order/" + "AddDeliveryReview";
    public static String ORDERWITHPRIESCIRPTION = BASEURL + "Order/" + "PrescriptionUpload";
    public static String GetAllUploadedPrecription = BASEURL + "Order/" + "GetAllUploadedPrecription";
    public static String GetOrderFilterMenu = BASEURL + "Order/" + "GetOrderFilterMenu";
    public static String GetOrderFilterList = BASEURL + "Order/" + "GetOrderFilterList";
    public static String CheckPrescReqInOrder = BASEURL + "Order/" + "CheckPrescReqInOrder";
    public static String CheckUserAddress = BASEURL + "Order/" + "CheckUserAddress";
    public static String ChangeOrderStatus = BASEURL + "Order/" + "ChangeOrderStatus";
    public static String GetAllOrderPrecription = BASEURL + "Order/" + "GetAllOrderPrecription";
    public static String GetAllOrderPrecriptionImg = BASEURL + "Order/" + "GetAllOrderPrecriptionImg";

    public static String TrackingStatus  = BASEURL + "Order/" + "TrackingStatus";
    public static String GetCategoryList  = BASEURL + "Dashboard/" + "GetCategoryList";


    public static String AddSchedule = BASEURL + "Schedule/" + "AddSchedule";
    public static String GetActiveSchedule = BASEURL + "Schedule/" + "GetActiveSchedule";
    public static String GetPastSchedule = BASEURL + "Schedule/" + "GetPastSchedule";
    public static String UpdateScheduleReminder = BASEURL + "Schedule/" + "UpdateScheduleReminder";
    public static String GetScheduleDetails = BASEURL + "Schedule/" + "GetScheduleDetails";
    public static String GetPrivacyDetails = BASEURL + "Schedule/" + "GetPrivacyDetails";

    public static String GetAllVitals = BASEURL + "Vital/" + "GetAllVitals";
    public static String GetUserVitals = BASEURL + "Vital/" + "GetUserVitals";
    public static String GetUserVitalsDateWise = BASEURL + "Vital/" + "GetUserVitalsDateWise";
    public static String AddVital = BASEURL + "Vital/" + "AddVital";
    public static String GetVitalGraph = BASEURL + "Vital/" + "GetVitalGraph";
    public static String DeleteVital = BASEURL + "Vital/" + "DeleteVital";

    public static String GetAllNotification = BASEURL + "Notification/" + "GetAllNotification";
    public static String DeleteNotification = BASEURL + "Notification/" + "DeleteNotification";
    public static String ReadNotification = BASEURL + "Notification/" + "ReadNotification";

    public static String AddComplain = BASEURL + "Complain/" + "AddComplain";
    public static String GetAllComplainMedicine = BASEURL + "Complain/" + "GetAllComplainMedicine";
    public static String GetAllComplain = BASEURL + "Complain/" + "GetAllComplain";

    public static String CheckPatient = BASEURL + "PatUsers/" + "CheckPatient";



    public static String TAG_SHARED_PREFERENCE_MYID = "SHARED_PREFERENCE_USERDATA";
    public static String INTENT_TAG_MYID_OBJECT = "INTENT_TAG_USERDATA_OBJECT";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static String PUSH_NOTIFICATION_KEY = "push_notification_key";
    public static final String NOTIFICATION_KEY = "notificationkey";
    public static String IMAGES_INTENT = "IMAGES_INTENT";
    public static String IMAGES_POSITION = "IMAGES_POSITION";

    // App Device Token Feature Shared Prefrences
    public static String TAG_SHARED_PREFERENCE_APP_DEVICETOKEN = "TAG_SHARED_PREFERENCE_APP_DEVICETOKEN";
    public static String TAG_SHARED_PREFERENCE_REFERAL_CODE = "TAG_SHARED_PREFERENCE_REFERAL_CODE";
    public static String TAG_SHARED_PREFERENCE_USER_ID = "TAG_SHARED_PREFERENCE_USER_ID";
    public static String TAG_SHARED_PREFERENCE_Cart_Count = "TAG_SHARED_PREFERENCE_Cart_Count";
    public static String TAG_SHARED_PREFERENCE_SELECTED_ADDRESS = "SELECTED_ADDRESS";
    public static String USER_FIRST_NAME = "USER_FIRST_NAME";
    public static String TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION = "SHARED_PREFERENCE_PUSH_NOTIFICATION";
    public static String TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA = "TAG_SHARED_PREFERENCE_PUSH_NOTIFICATION_DATA";
    public static String TAG_SHARED_PREFRENCE_Address = "TAG_SHARED_PREFRENCE_Address";

    // Insert Country In Shared Prefrences
    public static String TAG_SHARED_PREFRENCES_STATE = "TAG_SHARED_PREFRENCES_STATE_DATA";
}

package com.aleksandr.aleksandrov.clinic;

import java.util.Properties;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public abstract class Resources {
    public static final String IP = "192.168.1.12";
    //    public static String IP = "192.168.1.230";//12
    public static String GET_ALL_SPECIALIZATIONS = "http://www.clinic-motor.zp.ua/desk";
    public static String SEND_APPEAL = "//www.clinic-motor.zp.ua/desk/create?";

    public static final String URL = "jdbc:mysql://localhost:3306/beclinic";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    public static Properties PROPERTIES = new Properties();

    public static final String PHONE_1 = "+380617205707", PHONE_2 = "+380617205003";
    public static final String CLINIC_LATITUDE = "47.8266837", CLINIC_LONGITUDE = "35.2022885";



    public static final int ID_APPRECEIVER = 16;
    public static final String TAG_SUCCESS = "success";

    //Appeal table.
    public static final String _ID = "_ID";
    public static final String TAG_ID_SPEC = "appspecialization_id";
    public static final String TAG_APP_DATE = "date";
    public static final String TAG_APP_TIME = "time";
    public static final String TAG_APP_ONAME= "oname";
    public static final String TAG_APP_OEMAIL = "oemail";
    public static final String TAG_APP_OPHONE = "ophone";
    public static final String TAG_APP_DESCRIPTION = "odescription";
    public static final String TAG_ID_APPRECEIVER = "personnel_id";

    public static final int milliseconds = 1000;
    public static final int seconds = 60;
    public static final int minutes = 60;
    public static final int hours = 24;

    // milliseconds * seconds * minutes * hours - 24 часа
    public static final long TIME = milliseconds * seconds * minutes * hours;//4320000

    // Check data base for specializations updates (in days).
    public static final int CHECK_FOR_SPECIALIZATIONS_UPDATES = 7;

    public static final String TAG_TIME_STAMP = "time_stamp";

    //Appspecialization table.
    public static final String TAG_APPSPECIALIZATION_NAME = "name";
    public static final String TAG_APPSPECIALIZATION_ID = "id";

    //    public static final String url_all_appspecialization = "http://" + IP + "/~aleksandr/Clinic/get_all_appspecialization.php";
//    public static final String url_all_appspecialization = "http://"+IP+"/Clinic/get_all_appspecialization.php";//gas
    public static final String url_all_appspecialization = "http://"+ IP +"/gas";//gas
    public static final String TAG_APPSPECIALIZATIONS = "appspecialization";

}

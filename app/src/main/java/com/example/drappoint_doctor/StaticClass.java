package com.example.drappoint_doctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticClass {

    public static int PICK_SINGLE_IMAGE = 1;
    public static String SHARED_PREFERENCES = "shared_preferences";
    public static String NAME = "username";
    public static String SPECIALTY = "specialty";
    public static String EMAIL = "email";
    public static String PHONE = "phone";
    public static String ADDRESS = "address";
    public static String CITY = "city";
    public static String MAX = "max";
    public static String SUNDAY = "sunday";
    public static String MONDAY = "monday";
    public static String TUESDAY = "tuesday";
    public static String WEDNESDAY = "wednesday";
    public static String THURSDAY = "thursday";
    public static String FRIDAY = "friday";
    public static String SATURDAY = "saturday";
    public static String PHOTO = "photo";
    public static String DOCTOR_ID = "doctor_id";
    public static String mySimpleDateFormat = "dd-MM-yyyy";


    public static boolean isValidEmail(String email) {
        if(email.length()>4){
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }else{
            return false;
        }

    }
    public static boolean containsDigit(String s) {
        if(s.length()>2){
            boolean containsDigit = false;
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
            return containsDigit;
        }else{
            return false;
        }
    }
    public static String getCurrentTime(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
    }
}

package com.example.drappoint_doctor.activities.core.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.drappoint_doctor.R;
import com.example.drappoint_doctor.StaticClass;
import com.example.drappoint_doctor.adapters.SetTime;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private View fragmentView;
    private LinearLayout showScheduleLL, editScheduleLL;
    private TextView emailTV, nameTV, specialtyTV, phoneTV, addressCityTV, maxTV, errorTV,
                     sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    private EditText nameET, specialtyET, phoneET, addressET, cityET, maxET,
             sundayStart, mondayStart, tuesdayStart, wednesdayStart,
             thursdayStart, fridayStart, saturdayStart,
             sundayEnd, mondayEnd, tuesdayEnd, wednesdayEnd,
             thursdayEnd, fridayEnd, saturdayEnd;
    private ImageView editNameIV, editSpecialtyIV, editPhoneIV, editAddressCityIV,
            editMaxIV, editScheduleIV;
    private CheckBox sundayCheck, mondayCheck, tuesdayCheck, wednesdayCheck,
             thursdayCheck, fridayCheck, saturdayCheck;
    private String[] sundayArr =new String[2], mondayArr =new String[2], tuesdayArr =new String[2], wednesdayArr =new String[2],
            thursdayArr =new String[2], fridayArr =new String[2], saturdayArr =new String[2];
    private String sun, mon, tues, wednes, thurs, fri, satur;
    private boolean editName, editSpecialty, editPhone, editAddressCity,
            editMax, editSchedule;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DocumentReference userReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = fragmentView.getContext().getSharedPreferences(StaticClass.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        userReference = database.collection("doctors").document(sharedPreferences.getString(StaticClass.EMAIL, ""));
        findViewsByIds();
        setData();
        setEdit();
        setScheduleClockOnClick();
        return fragmentView;
    }

    private void findViewsByIds(){
        emailTV = fragmentView.findViewById(R.id.emailTV);
        nameTV = fragmentView.findViewById(R.id.nameTV);
        specialtyTV = fragmentView.findViewById(R.id.specialtyTV);
        phoneTV = fragmentView.findViewById(R.id.phoneTV);
        addressCityTV = fragmentView.findViewById(R.id.addressCityTV);
        maxTV = fragmentView.findViewById(R.id.maxTV);
        nameET = fragmentView.findViewById(R.id.nameET);
        specialtyET = fragmentView.findViewById(R.id.specialtyET);
        phoneET = fragmentView.findViewById(R.id.phoneET);
        addressET = fragmentView.findViewById(R.id.addressET);
        cityET = fragmentView.findViewById(R.id.cityET);
        errorTV = fragmentView.findViewById(R.id.errorTV);
        maxET = fragmentView.findViewById(R.id.maxET);
        sunday = fragmentView.findViewById(R.id.sunday);
        monday = fragmentView.findViewById(R.id.monday);
        tuesday = fragmentView.findViewById(R.id.tuesday);
        wednesday = fragmentView.findViewById(R.id.wednesday);
        thursday = fragmentView.findViewById(R.id.thursday);
        friday = fragmentView.findViewById(R.id.friday);
        saturday = fragmentView.findViewById(R.id.saturday);
        sundayStart = fragmentView.findViewById(R.id.sundayStart);
        mondayStart = fragmentView.findViewById(R.id.mondayStart);
        tuesdayStart = fragmentView.findViewById(R.id.tuesdayStart);
        wednesdayStart = fragmentView.findViewById(R.id.wednesdayStart);
        thursdayStart = fragmentView.findViewById(R.id.thursdayStart);
        fridayStart = fragmentView.findViewById(R.id.fridayStart);
        saturdayStart = fragmentView.findViewById(R.id.saturdayStart);
        sundayEnd = fragmentView.findViewById(R.id.sundayEnd);
        mondayEnd = fragmentView.findViewById(R.id.mondayEnd);
        tuesdayEnd = fragmentView.findViewById(R.id.tuesdayEnd);
        wednesdayEnd = fragmentView.findViewById(R.id.wednesdayEnd);
        thursdayEnd = fragmentView.findViewById(R.id.thursdayEnd);
        fridayEnd = fragmentView.findViewById(R.id.fridayEnd);
        saturdayEnd = fragmentView.findViewById(R.id.saturdayEnd);
        sundayCheck = fragmentView.findViewById(R.id.sundayCheck);
        mondayCheck = fragmentView.findViewById(R.id.mondayCheck);
        tuesdayCheck = fragmentView.findViewById(R.id.tuesdayCheck);
        wednesdayCheck = fragmentView.findViewById(R.id.wednesdayCheck);
        thursdayCheck = fragmentView.findViewById(R.id.thursdayCheck);
        fridayCheck = fragmentView.findViewById(R.id.fridayCheck);
        saturdayCheck = fragmentView.findViewById(R.id.saturdayCheck);
        editNameIV = fragmentView.findViewById(R.id.editNameIV);
        editSpecialtyIV = fragmentView.findViewById(R.id.editSpecialtyIV);
        editPhoneIV = fragmentView.findViewById(R.id.editPhoneIV);
        editAddressCityIV = fragmentView.findViewById(R.id.editAddressCityIV);
        editMaxIV = fragmentView.findViewById(R.id.editMaxIV);
        editScheduleIV = fragmentView.findViewById(R.id.editScheduleIV);
        editScheduleLL = fragmentView.findViewById(R.id.editScheduleLL);
        showScheduleLL = fragmentView.findViewById(R.id.showsScheduleLL);
    }
    @SuppressLint("SetTextI18n")
    private void setData(){
        emailTV.setText(sharedPreferences.getString(StaticClass.EMAIL, "no email"));
        nameTV.setText(sharedPreferences.getString(StaticClass.NAME, "no name"));
        nameET.setText(sharedPreferences.getString(StaticClass.NAME, "no name"));
        specialtyTV.setText(sharedPreferences.getString(StaticClass.SPECIALTY, "no specialty"));
        specialtyET.setText(sharedPreferences.getString(StaticClass.SPECIALTY, "no specialty"));
        phoneTV.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone"));
        phoneET.setText(sharedPreferences.getString(StaticClass.PHONE, "no phone"));
        addressCityTV.setText(
                sharedPreferences.getString(StaticClass.ADDRESS, "no address")+", "+
                sharedPreferences.getString(StaticClass.CITY, "no city"));
        addressET.setText(sharedPreferences.getString(StaticClass.ADDRESS, "no address"));
        cityET.setText(sharedPreferences.getString(StaticClass.CITY, "no city"));
        maxTV.setText(String.valueOf(sharedPreferences.getLong(StaticClass.MAX, 0)));
        maxET.setText(String.valueOf(sharedPreferences.getLong(StaticClass.MAX, 0)));
        getSchedule();
        setShowSchedule();
    }
    private void setShowSchedule(){
        sunday.setText(sharedPreferences.getString(StaticClass.SUNDAY, ""));
        monday.setText(sharedPreferences.getString(StaticClass.MONDAY, ""));
        tuesday.setText(sharedPreferences.getString(StaticClass.TUESDAY, ""));
        wednesday.setText(sharedPreferences.getString(StaticClass.WEDNESDAY, ""));
        thursday.setText(sharedPreferences.getString(StaticClass.THURSDAY, ""));
        friday.setText(sharedPreferences.getString(StaticClass.FRIDAY, ""));
        saturday.setText(sharedPreferences.getString(StaticClass.SATURDAY, ""));
    }
    private void getSchedule(){
        String temp;
        temp = sharedPreferences.getString(StaticClass.SUNDAY, "");
        if(temp.equals("closed")){
            sundayCheck.setChecked(true);
            sundayArr[0] = "00:00";
            sundayArr[1] = "00:00";
        }else {
            sundayArr = temp.split("-");
        }
        sundayStart.setText(sundayArr[0]);
        sundayEnd.setText(sundayArr[1]);
        temp = sharedPreferences.getString(StaticClass.MONDAY, "");
        if(temp.equals("closed")){
            mondayCheck.setChecked(true);
            mondayArr[0] = "00:00";
            mondayArr[1] = "00:00";
        }else {
            mondayArr = temp.split("-");
        }
        mondayStart.setText(mondayArr[0]);
        mondayEnd.setText(mondayArr[1]);
        temp = sharedPreferences.getString(StaticClass.TUESDAY, "");
        if(temp.equals("closed")){
            tuesdayCheck.setChecked(true);
            tuesdayArr[0] = "00:00";
            tuesdayArr[1] = "00:00";
        }else {
            tuesdayArr = temp.split("-");
        }
        tuesdayStart.setText(tuesdayArr[0]);
        tuesdayEnd.setText(tuesdayArr[1]);
        temp = sharedPreferences.getString(StaticClass.WEDNESDAY, "");
        if(temp.equals("closed")){
            wednesdayCheck.setChecked(true);
            wednesdayArr[0] = "00:00";
            wednesdayArr[1] = "00:00";
        }else {
            wednesdayArr = temp.split("-");
        }
        wednesdayStart.setText(wednesdayArr[0]);
        wednesdayEnd.setText(wednesdayArr[1]);
        temp = sharedPreferences.getString(StaticClass.THURSDAY, "");
        if(temp.equals("closed")){
            thursdayCheck.setChecked(true);
            thursdayArr[0] = "00:00";
            thursdayArr[1] = "00:00";
        }else {
            thursdayArr = temp.split("-");
        }
        thursdayStart.setText(thursdayArr[0]);
        thursdayEnd.setText(thursdayArr[1]);
        temp = sharedPreferences.getString(StaticClass.FRIDAY, "");
        if(temp.equals("closed")){
            fridayCheck.setChecked(true);
            fridayArr[0] = "00:00";
            fridayArr[1] = "00:00";
        }else {
            fridayArr = temp.split("-");
        }
        fridayStart.setText(fridayArr[0]);
        fridayEnd.setText(fridayArr[1]);
        temp = sharedPreferences.getString(StaticClass.SATURDAY, "");
        if(temp.equals("closed")){
            saturdayCheck.setChecked(true);
            saturdayArr[0] = "00:00";
            saturdayArr[1] = "00:00";
        }else {
            saturdayArr = temp.split("-");
        }
        saturdayStart.setText(saturdayArr[0]);
        saturdayEnd.setText(saturdayArr[1]);
    }
    private void setEdit(){
        editNameIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTV.setVisibility(editName ? View.VISIBLE : View.GONE);
                nameET.setVisibility(!editName ? View.VISIBLE : View.GONE);
                editNameIV.setImageResource(editName ? R.drawable.ic_edit : R.drawable.ic_check);
                if(editName) updateName();
                editName = !editName;
            }
        });
        editSpecialtyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialtyTV.setVisibility(editSpecialty ? View.VISIBLE : View.GONE);
                specialtyET.setVisibility(!editSpecialty ? View.VISIBLE : View.GONE);
                editSpecialtyIV.setImageResource(editSpecialty ? R.drawable.ic_edit : R.drawable.ic_check);
                if(editSpecialty) updateSpecialty();
                editSpecialty = !editSpecialty;
            }
        });
        editPhoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneTV.setVisibility(editPhone ? View.VISIBLE : View.GONE);
                phoneET.setVisibility(!editPhone ? View.VISIBLE : View.GONE);
                editPhoneIV.setImageResource(editPhone ? R.drawable.ic_edit : R.drawable.ic_check);
                if(editPhone) updatePhone();
                editPhone = !editPhone;
            }
        });
        editAddressCityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressCityTV.setVisibility(editAddressCity ? View.VISIBLE : View.GONE);
                addressET.setVisibility(!editAddressCity ? View.VISIBLE : View.GONE);
                cityET.setVisibility(!editAddressCity ? View.VISIBLE : View.GONE);
                editAddressCityIV.setImageResource(editAddressCity ? R.drawable.ic_edit : R.drawable.ic_check);
                if(editAddressCity) updateAddressCity();
                editAddressCity = !editAddressCity;
            }
        });
        editMaxIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxTV.setVisibility(editMax ? View.VISIBLE : View.GONE);
                maxET.setVisibility(!editMax ? View.VISIBLE : View.GONE);
                editMaxIV.setImageResource(editMax ? R.drawable.ic_edit : R.drawable.ic_check);
                if(editMax) updateMax();
                editMax = !editMax;
            }
        });
        editScheduleIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editScheduleLL.setVisibility(editSchedule ? View.GONE : View.VISIBLE);
                showScheduleLL.setVisibility(!editSchedule ? View.GONE : View.VISIBLE);
                editScheduleIV.setImageResource(editSchedule ? R.drawable.ic_edit : R.drawable.ic_check);
                if(editSchedule) updateSchedule();
                editSchedule = !editSchedule;
            }
        });
    }
    private void setScheduleClockOnClick(){
        new SetTime(sundayStart, fragmentView.getContext());
        new SetTime(sundayEnd, fragmentView.getContext());
        new SetTime(mondayStart, fragmentView.getContext());
        new SetTime(mondayEnd, fragmentView.getContext());
        new SetTime(tuesdayStart, fragmentView.getContext());
        new SetTime(tuesdayEnd, fragmentView.getContext());
        new SetTime(wednesdayStart, fragmentView.getContext());
        new SetTime(thursdayStart, fragmentView.getContext());
        new SetTime(thursdayEnd, fragmentView.getContext());
        new SetTime(fridayStart, fragmentView.getContext());
        new SetTime(fridayEnd, fragmentView.getContext());
        new SetTime(saturdayStart, fragmentView.getContext());
        new SetTime(saturdayEnd, fragmentView.getContext());
    }
    private void updateName(){
        String name = nameET.getText().toString();
        if(!name.isEmpty() && !StaticClass.containsDigit(name)){
            editor.putString(StaticClass.NAME, name);
            editor.apply();
            userReference.update("name", name);
            setData();
        }else{
            displayErrorTV();
        }
    }
    private void updateSpecialty(){
        String specialty = specialtyET.getText().toString();
        if(!specialty.isEmpty()){
            editor.putString(StaticClass.SPECIALTY, specialty);
            editor.apply();
            userReference.update("specialty", specialty);
            setData();
        }else{
            displayErrorTV();
        }
    }
    private void updatePhone(){
        if(phoneET.getText().toString().length()>9){
            String phone = phoneET.getText().toString();
            editor.putString(StaticClass.PHONE, phone);
            editor.apply();
            userReference.update("phone", phone);
            setData();
        }else{
            displayErrorTV();
        }
    }
    private void updateMax(){
        if(!maxET.getText().toString().isEmpty()){
            long max = Long.parseLong(maxET.getText().toString());
            editor.putLong(StaticClass.MAX, max);
            editor.apply();
            userReference.update("max", max);
            setData();
        }else{
            displayErrorTV();
        }
    }
    private void updateAddressCity(){
        if(!addressET.getText().toString().isEmpty()
        && !cityET.getText().toString().isEmpty()){
            editor.putString(StaticClass.ADDRESS, addressET.getText().toString());
            editor.putString(StaticClass.CITY, cityET.getText().toString());
            editor.apply();
            userReference.update("address", addressET.getText().toString());
            userReference.update("city", cityET.getText().toString());
            setData();
        }else{
            displayErrorTV();
        }
    }
    private void setCheckedToClosed(){
        if(sundayCheck.isChecked()) sun="closed";
        else sun = sundayStart.getText().toString() +"-"+ sundayEnd.getText().toString();
        if(mondayCheck.isChecked()) mon="closed";
        else mon = mondayStart.getText().toString() +"-"+ mondayEnd.getText().toString();
        if(tuesdayCheck.isChecked()) tues="closed";
        else tues = tuesdayStart.getText().toString() +"-"+ tuesdayEnd.getText().toString();
        if(wednesdayCheck.isChecked()) wednes="closed";
        else wednes = wednesdayStart.getText().toString() +"-"+ wednesdayEnd.getText().toString();
        if(thursdayCheck.isChecked()) thurs="closed";
        else thurs = thursdayStart.getText().toString() +"-"+ thursdayEnd.getText().toString();
        if(fridayCheck.isChecked()) fri="closed";
        else fri = fridayStart.getText().toString() +"-"+ fridayEnd.getText().toString();
        if(saturdayCheck.isChecked()) satur="closed";
        else satur = saturdayStart.getText().toString() +"-"+ saturdayEnd.getText().toString();

    }
    private HashMap<String, String> getScheduleMap(){
        HashMap<String, String> schedule = new HashMap<>();
        schedule.put("sundayArr", sun);
        schedule.put("mondayArr", mon);
        schedule.put("tuesdayArr", tues);
        schedule.put("wednesdayArr", wednes);
        schedule.put("thursdayArr", thurs);
        schedule.put("fridayArr", fri);
        schedule.put("saturdayArr", satur);
        return schedule;
    }
    private void updateSchedule(){
        setCheckedToClosed();
        editor.putString(StaticClass.SUNDAY, sun);
        editor.putString(StaticClass.MONDAY, mon);
        editor.putString(StaticClass.TUESDAY, tues);
        editor.putString(StaticClass.WEDNESDAY, wednes);
        editor.putString(StaticClass.THURSDAY, thurs);
        editor.putString(StaticClass.FRIDAY, fri);
        editor.putString(StaticClass.SATURDAY, satur);
        editor.apply();
        userReference.update("schedule", getScheduleMap());
        setData();
    }
    private void displayErrorTV() {
        errorTV.setText("Invalid input");
        errorTV.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorTV.setVisibility(View.GONE);
            }
        }, 1500);
    }








}










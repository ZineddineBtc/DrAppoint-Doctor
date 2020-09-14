package com.example.drappoint_doctor.activities.entry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drappoint_doctor.R;
import com.example.drappoint_doctor.StaticClass;
import com.example.drappoint_doctor.activities.TermsActivity;
import com.example.drappoint_doctor.activities.core.CoreActivity;
import com.example.drappoint_doctor.adapters.SetTime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetProfileActivity extends AppCompatActivity {

    EditText nameET, phoneET, specialtyET, addressET, cityET, maxET,
             sundayStart, mondayStart, tuesdayStart, wednesdayStart, thursdayStart, fridayStart, saturdayStart,
             sundayEnd, mondayEnd, tuesdayEnd, wednesdayEnd, thursdayEnd, fridayEnd, saturdayEnd;
    TextView errorTV, scheduleTV;
    Button finishButton, saveScheduleButton;
    LinearLayout shadeLL, scheduleLL;
    SharedPreferences sharedPreferences;
    String name, phone, address, city, email, specialty,
        sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    Long max;
    FirebaseFirestore database;
    ProgressDialog progressDialog;
    boolean canFinish, canSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        findViewsByIds();
        checkBuildVersion();
    }
    public void checkBuildVersion(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
    }
    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // not granted
                moveTaskToBack(true);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void findViewsByIds(){
        nameET = findViewById(R.id.nameET);
        nameET.requestFocus();
        specialtyET = findViewById(R.id.specialtyET);
        phoneET = findViewById(R.id.phoneET);
        addressET = findViewById(R.id.addressET);
        cityET = findViewById(R.id.cityET);
        errorTV = findViewById(R.id.errorTV);
        maxET = findViewById(R.id.maxET);
        sundayStart = findViewById(R.id.sundayStart);
        mondayStart = findViewById(R.id.mondayStart);
        tuesdayStart = findViewById(R.id.tuesdayStart);
        wednesdayStart = findViewById(R.id.wednesdayStart);
        thursdayStart = findViewById(R.id.thursdayStart);
        fridayStart = findViewById(R.id.fridayStart);
        saturdayStart = findViewById(R.id.saturdayStart);
        sundayEnd = findViewById(R.id.sundayEnd);
        mondayEnd = findViewById(R.id.mondayEnd);
        tuesdayEnd = findViewById(R.id.tuesdayEnd);
        wednesdayEnd = findViewById(R.id.wednesdayEnd);
        thursdayEnd = findViewById(R.id.thursdayEnd);
        fridayEnd = findViewById(R.id.fridayEnd);
        saturdayEnd = findViewById(R.id.saturdayEnd);
        setDaysClickable();
        finishButton = findViewById(R.id.finishButton);
        saveScheduleButton = findViewById(R.id.saveScheduleButton);
        shadeLL = findViewById(R.id.shadeLL);
        scheduleLL = findViewById(R.id.scheduleLL);
        scheduleTV = findViewById(R.id.scheduleTV);
    }
    public void setDaysClickable(){
        new SetTime(sundayStart, this);
        new SetTime(mondayStart, this);
        new SetTime(tuesdayStart, this);
        new SetTime(wednesdayStart, this);
        new SetTime(thursdayStart, this);
        new SetTime(fridayStart, this);
        new SetTime(saturdayStart, this);
        new SetTime(sundayEnd, this);
        new SetTime(mondayEnd, this);
        new SetTime(tuesdayEnd, this);
        new SetTime(wednesdayEnd, this);
        new SetTime(thursdayEnd, this);
        new SetTime(fridayEnd, this);
        new SetTime(saturdayEnd, this);
    }
    public void showSchedule(View view){
        shadeLL.setVisibility(View.VISIBLE);
        scheduleLL.setVisibility(View.VISIBLE);
        maxET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(String.valueOf(s).isEmpty()){
                    saveScheduleButton.setBackground(getDrawable(R.drawable.grey_background_rounded_border));
                    canSave = false;
                }else{
                    canSave = true;
                    saveScheduleButton.setBackground(getDrawable(R.drawable.special_background_rounded_border));
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }@Override public void afterTextChanged(Editable s) {

            }
        });
    }
    public void saveSchedule(View view){
        if(canSave){
            max = Long.parseLong(maxET.getText().toString());
            setDays();
            shadeLL.setVisibility(View.GONE);
            scheduleLL.setVisibility(View.GONE);
            scheduleTV.setBackgroundColor(getColor(R.color.green));
        }
    }
    public void setDays(){
        if(sundayStart.getText().toString().isEmpty() && sundayEnd.getText().toString().isEmpty()){
            sunday = "closed";
        }else{
            sunday = sundayStart.getText().toString()+"-"+sundayEnd.getText().toString();
        }
        if(mondayStart.getText().toString().isEmpty() && mondayEnd.getText().toString().isEmpty()){
            monday = "closed";
        }else{
            monday = mondayStart.getText().toString()+"-"+mondayEnd.getText().toString();
        }
        if(tuesdayStart.getText().toString().isEmpty() && tuesdayEnd.getText().toString().isEmpty()){
            tuesday = "closed";
        }else{
            tuesday = tuesdayStart.getText().toString()+"-"+tuesdayEnd.getText().toString();
        }
        if(wednesdayStart.getText().toString().isEmpty() && wednesdayEnd.getText().toString().isEmpty()){
            wednesday = "closed";
        }else{
            wednesday = wednesdayStart.getText().toString()+"-"+wednesdayEnd.getText().toString();
        }
        if(thursdayStart.getText().toString().isEmpty() && thursdayEnd.getText().toString().isEmpty()){
            thursday = "closed";
        }else{
            thursday = thursdayStart.getText().toString()+"-"+thursdayEnd.getText().toString();
        }
        if(fridayStart.getText().toString().isEmpty() && fridayEnd.getText().toString().isEmpty()){
            friday = "closed";
        }else{
            friday = fridayStart.getText().toString()+"-"+fridayEnd.getText().toString();
        }
        if(saturdayStart.getText().toString().isEmpty() && saturdayEnd.getText().toString().isEmpty()){
            saturday = "closed";
        }else{
            saturday = saturdayStart.getText().toString()+"-"+saturdayEnd.getText().toString();
        }
    }
    public HashMap<String, String> getSchedule(){
        HashMap<String, String> schedule = new HashMap<>();
        schedule.put("sunday", sunday);
        schedule.put("monday", monday);
        schedule.put("tuesday", tuesday);
        schedule.put("wednesday", wednesday);
        schedule.put("thursday", thursday);
        schedule.put("friday", friday);
        schedule.put("saturday", saturday);
        return schedule;
    }
    public void writeSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticClass.NAME, name);
        editor.putString(StaticClass.SPECIALTY, specialty);
        editor.putString(StaticClass.PHONE, phone);
        editor.putString(StaticClass.ADDRESS, address);
        editor.putString(StaticClass.CITY, city);
        editor.putString(StaticClass.EMAIL, email);
        editor.putLong(StaticClass.MAX, max);
        editor.putBoolean(StaticClass.VACATION, false);
        editor.putString(StaticClass.SUNDAY, sunday);
        editor.putString(StaticClass.MONDAY, monday);
        editor.putString(StaticClass.TUESDAY, tuesday);
        editor.putString(StaticClass.WEDNESDAY, wednesday);
        editor.putString(StaticClass.THURSDAY, thursday);
        editor.putString(StaticClass.FRIDAY, friday);
        editor.putString(StaticClass.SATURDAY, saturday);
        editor.apply();
    }
    public void writeOnlineDatabase(){
        Map<String, Object> userReference = new HashMap<>();
        userReference.put("name", name);
        userReference.put("phone", phone);
        userReference.put("address", address);
        userReference.put("city", city);
        userReference.put("specialty", specialty);
        userReference.put("max", max);
        userReference.put("vacation", false);
        userReference.put("schedule", getSchedule());

        database.collection("doctors")
                .document(email)
                .set(userReference)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String, Object> dateReference = new HashMap<>();
                        dateReference.put(StaticClass.getCurrentTime(), new ArrayList<String>());
                        database.collection("doctors-date")
                                .document(email)
                                .set(dateReference)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(getApplicationContext(), CoreActivity.class));
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error writing user",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
    public void finishRegister(View view) {
        name = nameET.getText().toString().trim();
        specialty = specialtyET.getText().toString().trim();
        phone = phoneET.getText().toString().trim();
        address = addressET.getText().toString().trim();
        city = cityET.getText().toString().trim();
        email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
                .getEmail();
        if(name.length()<2){
            displayErrorTV(R.string.name_unspecified);
            return;
        }
        if(StaticClass.containsDigit(name)) {
            displayErrorTV(R.string.name_not_number);
            return;
        }
        if(specialty.isEmpty()){
            displayErrorTV(R.string.specialty_unspecified);
            return;
        }
        if(phone.length() < 10) {
            displayErrorTV(R.string.insufficient_phone_number);
            return;
        }
        if(address.isEmpty()) {
            displayErrorTV(R.string.address_unspecified);
            return;
        }
        if(city.isEmpty()) {
            displayErrorTV(R.string.city_unspecified);
            return;
        }
        if(!canSave){
            displayErrorTV(R.string.schedule_unspecified);
            return;
        }
        setDays();
        progressDialog.setMessage("Setting up profile...");
        progressDialog.show();
        writeSharedPreferences();
        writeOnlineDatabase();
    }
    public void toTermsAndConditions(View view) {
        startActivity(new Intent(getApplicationContext(), TermsActivity.class));
    }
    public void displayErrorTV(int resourceID) {
        errorTV.setText(resourceID);
        errorTV.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                errorTV.setVisibility(View.GONE);
            }
        }, 1500);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

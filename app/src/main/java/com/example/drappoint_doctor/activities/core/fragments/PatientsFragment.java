package com.example.drappoint_doctor.activities.core.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.drappoint_doctor.R;
import com.example.drappoint_doctor.StaticClass;
import com.example.drappoint_doctor.adapters.SetDate;
import com.example.drappoint_doctor.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientsFragment extends Fragment {

    private View fragmentView;
    private ListView patientsLV;
    private ArrayAdapter adapter;
    private ArrayList<String> patientNameList = new ArrayList<>();
    private ArrayList<DocumentReference> patientReferenceList;
    private ArrayList<Patient> patients = new ArrayList<>();
    private ProgressDialog progressDialog;
    private FirebaseFirestore database;
    private TextView dateTV, nameTV, emailTV, phoneTV, addressTV, emptyListTV;
    private LinearLayout shadeLL, patientLL;
    private String doctorId;
    private DocumentReference patientReference;
    private int p;
    private HashMap<String, Object> wholeDocument;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_patients, container, false);
        doctorId = fragmentView.getContext().getSharedPreferences(StaticClass.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(StaticClass.EMAIL, " ");
        database = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(fragmentView.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
        getWholeDocument();
        findViewsByIds();
        return fragmentView;
    }
    private void getWholeDocument(){
        database.collection("doctors-date").document(doctorId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        wholeDocument = (HashMap<String, Object>) document.getData();
                        getPatientsList();
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }
    private void findViewsByIds(){
        dateTV = fragmentView.findViewById(R.id.dateTV);
        dateTV.setText(StaticClass.getCurrentTime());
        new SetDate(dateTV, fragmentView.getContext());
        dateTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getPatientsList();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}@Override public void afterTextChanged(Editable s) {}
        });
        patientsLV = fragmentView.findViewById(R.id.patientsLV);
        nameTV = fragmentView.findViewById(R.id.nameTV);
        emailTV = fragmentView.findViewById(R.id.emailTV);
        phoneTV = fragmentView.findViewById(R.id.phoneTV);
        phoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        });
        addressTV = fragmentView.findViewById(R.id.addressTV);
        addressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
        ImageView closeIV = fragmentView.findViewById(R.id.closeIV);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shadeLL.setVisibility(View.GONE);patientLL.setVisibility(View.GONE);
            }
        });
        shadeLL = fragmentView.findViewById(R.id.shadeLL);
        patientLL = fragmentView.findViewById(R.id.patientLL);
    }
    private void getPatientsList(){
        patientReferenceList = new ArrayList<>();
        patientReferenceList = (ArrayList<DocumentReference>)
                wholeDocument.get(dateTV.getText().toString());
        if(patientReferenceList != null){
            if(!patientReferenceList.isEmpty()){
                getPatientsData();
            }
        }else{
            emptyListTV.setVisibility(View.VISIBLE);
        }
    }
    private void getPatientsData(){
        patients.clear();
        patientNameList.clear();
        for(DocumentReference documentReference: patientReferenceList) {
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()) {
                                    Patient patient = new Patient();
                                    patient.setId(document.getId());
                                    String name = document.get("name").toString();
                                    patient.setName(name);
                                    patient.setPhone(document.get("phone").toString());
                                    patient.setAddress(document.get("address").toString());
                                    patients.add(patient);
                                    patientNameList.add(name);
                                    setAdapter();
                                }
                            }
                        }
                    });
        }
    }
    private void setAdapter(){
        adapter = new ArrayAdapter(fragmentView.getContext(), android.R.layout.simple_list_item_1, patientNameList);
        progressDialog.dismiss();
        patientsLV.setAdapter(adapter);
        patientsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPatient(position);
                patientReference = patientReferenceList.get(position);
                p = position;
            }
        });
    }
    private void showPatient(int position){
        nameTV.setText(patients.get(position).getName());
        emailTV.setText(patients.get(position).getId());
        phoneTV.setText(patients.get(position).getPhone());
        addressTV.setText(patients.get(position).getAddress());
        shadeLL.setVisibility(View.VISIBLE);
        patientLL.setVisibility(View.VISIBLE);
    }
    private void deletePatientFromList(){
        database.collection("doctors-date").document(doctorId)
                .update(dateTV.getText().toString(), FieldValue.arrayRemove(patientReference));
        patientReferenceList.remove(p);
        patientNameList.remove(p);
        adapter.notifyDataSetChanged();
        shadeLL.setVisibility(View.GONE);
        patientLL.setVisibility(View.GONE);

    }
    private void dialPhone(){
        String phoneNumber = phoneTV.getText().toString()
                .replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }
    private void openMap(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(
                Uri.parse("https://www.google.com/maps/search/" +
                        addressTV.getText().toString()));
        startActivity(browserIntent);
    }

}
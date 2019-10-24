package com.example.charity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DonateFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private String[] items = {"Select a Category to Donate", "Books", "Toys", "Clothes", "Others"};
    private static final int REQUEST_IMAGE_CAPTURE = 107;
    private Button captureImageButton, pickupButton;
    private EditText title;
    private TextView tv_loc;
    private ImageView imageView;
    private DatabaseHelper db;
    private static final String PREFS_NAME = "PrefsFile";

    private String currentPhotoPath;
    private String imageFileName;
    Uri photoURI;
    Bitmap bitmap;

    LocationCallback callback;
    FusedLocationProviderClient client;
    Location location;
    double lat, longi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getActivity());
        final Spinner spinner = view.findViewById(R.id.spinner1);
        tv_loc = view.findViewById((R.id.tv_location));
        captureImageButton = view.findViewById(R.id.CaptureImageButton);
        pickupButton = view.findViewById(R.id.PickupButton);
        title = view.findViewById(R.id.edittexttitle);
        client = LocationServices.getFusedLocationProviderClient(getContext());
        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        imageView = view.findViewById(R.id.imageCapture);
        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String email = sp.getString("pref_mail", "");
                String category = spinner.getSelectedItem().toString().trim();
                String thingname = title.getText().toString().trim();
                if (sp.getString("pref", "").equals("true")) {
                    long val = db.addUserInfo(email, bitmap, category, thingname);
                    if (val > 0) {
                        callback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult){
                                location = locationResult.getLocations().get(0);
                                lat = location.getLatitude();
                                longi = location.getLongitude();
                                super.onLocationResult(locationResult);
                                tv_loc.setText("Latitude = " + lat + " Longitude = " + longi);
                            }
                        };
                        LocationRequest request = new LocationRequest();
                        request.setFastestInterval(3000);
                        request.setInterval(5000);
                        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        client.requestLocationUpdates(request, callback, Looper.myLooper());
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Sign In first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(), "com.example.android.file_provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Bundle extras = data.getExtras();
        //Bitmap imageBitmap = (Bitmap) extras.get("data");
        imageView.setImageURI(photoURI);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getContext(), items[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

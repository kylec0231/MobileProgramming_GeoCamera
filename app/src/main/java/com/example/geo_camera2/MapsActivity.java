package com.example.geo_camera2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
         OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    Button btn_take, btn_list, btn_load;
    TextView tv_message;
    ImageView iv_photo;
    String pathToFile;
    GPSTracker gps;

    static final String imageDir = "/storage/emulated/0/Android/data/com.example.geo_camera2/files/Pictures/";

    // name of the file that is saved by the camera
    String currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_PHOTO = 3;

    private static final LatLng MELBOURNE = new LatLng(36.0822, -94.1719);
    private Marker melbourne = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btn_take = findViewById(R.id.btnCameraOn);
        btn_load = findViewById(R.id.btn_loadImage);
        iv_photo = findViewById(R.id.iv_photo);
        tv_message = findViewById(R.id.tv_message);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }


        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakerAction();
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                }


        });
    }

    public void theFunction() {
//            mMap.clear();
//        File imgFile = new File("/storage/emulated/0/Pictures");
//        //File imgFile = new File("/storage/emulated/0/DCIM/Camera/IMG_20191027_025320.jpg");
//        if (imgFile.exists()) {
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(myBitmap);

//
//                    ImageView myImage = (ImageView) findViewById(R.id.iv_photo);
//
//                    myImage.setImageBitmap(myBitmap);

            String path = "/storage/emulated/0/Pictures";
            String fileList = null;

            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
                fileList += "\n" + files[i].getName();
                if(files[i].getName().contains("AND"))
                {
                    String latStr = files[i].getName().substring(files[i].getName().indexOf("Test") + 4, files[i].getName().indexOf("AND"));
                    double latDouble = Double.parseDouble(latStr);

                    String longStr = files[i].getName().substring(files[i].getName().indexOf("AND") + 3, files[i].getName().indexOf("END"));
                    double longDouble = Double.parseDouble(longStr);
                    Log.d("Kyle", latStr + " " + longStr);



//                            Drawable drawable = Drawable.createFromPath(files[i].toString());
//
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(latDouble, longDouble))
//                                    .title(files[i].getName())
//                                    .icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable)drawable).getBitmap())));

                    Bitmap eachBitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.iv_photo);

                    myImage.setImageBitmap(eachBitmap);

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latDouble, longDouble))
                            .title(files[i].getName()));


                }
            }
//            TextView tv_message;
//            tv_message = findViewById(R.id.tv_message);
//            tv_message.setText(fileList);


//                    melbourne = mMap.addMarker(new MarkerOptions()
//                            .position(MELBOURNE)
//                            .title("Melbourne")
//                            .snippet("Population: 4,137,400")
//                            .icon(BitmapDescriptorFactory.fromBitmap(myBitmap)));

//                    try {
//                        ExifInterface exifInterface = new ExifInterface(imgFile.toString());
//                        TextView tv_message;
//                        tv_message = findViewById(R.id.tv_message);
//                        String exif="Exif: " + imgFile.toString();
//
//                        exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
//                        exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
//                        exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//                        exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
//                        exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
//                        exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//                        exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
//                        exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
//                        exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
//                        exif += "\nGPS related:";
//                        exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
//                        exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
//                        exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//                        exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//                        exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                        exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//                        exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
//                        exif += "\n LATITUDE_NORTH: " + exifInterface.getAttribute(ExifInterface.LATITUDE_NORTH);
//                        exif += "\n LATITUDE_SOUTH: " + exifInterface.getAttribute(ExifInterface.LATITUDE_SOUTH);
//                        exif += "\n TAG_GPS_DEST_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DEST_LATITUDE);
//                        exif += "\n TAG_GPS_DEST_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DEST_LATITUDE_REF);
//                        exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//                        exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//                        exif += "\n TAG_ISO_SPEED_LATITUDE_YYY: " + exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_LATITUDE_YYY);
//                        exif += "\n TAG_ISO_SPEED_LATITUDE_ZZZ: " + exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_LATITUDE_ZZZ);
//
//
//                        tv_message.setText(exif);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//


      //  }

//                String imagePathString = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/Pictures";
//                Uri imagePath = Uri.parse(imagePathString);
//
//                // create an intent to select a photo from the gallery
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
////                Uri imagePath = Uri.parse("Pictures/");
////                Intent i = new Intent(Intent.ACTION_PICK, imagePath);
//                // start the intent with a request code
//                startActivityForResult(i, SELECT_PHOTO);
//
//                Log.d("mylog", MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//                Log.d("mylog", imagePathString);

//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] gratResults){

        Log.d("Kyle", "here");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        gps = new GPSTracker(MapsActivity.this);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                iv_photo.setImageBitmap(bitmap);

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.

                }
                theFunction();

            } else if (requestCode == SELECT_PHOTO) {
                Uri selectedPhoto = data.getData();
                ImageView iv_photo;
                iv_photo = findViewById(R.id.iv_photo);

                Glide.with(this).load(selectedPhoto).into(iv_photo);

                // show the file name in tv_message
                TextView tv_message;
                tv_message = findViewById(R.id.tv_message);
                tv_message.setText(selectedPhoto.toString());
            }
        }
    }

    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePic.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(MapsActivity.this, "com.example.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, REQUEST_TAKE_PHOTO);
            }

        }
    }

    private File createPhotoFile() {
        gps = new GPSTracker(MapsActivity.this);

        File image = null;
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            //        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String name = "Test" + latitude + "AND" + longitude + "END";
            File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            Log.d("mylog", storageDir.toString());

            try {
                image = File.createTempFile(name, ".jpeg", storageDir);

            } catch (IOException e) {
                Log.d("myLog", "Except : " + e.toString());
            }

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            Log.d("Kyle__", image.getAbsolutePath());
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(image.getAbsolutePath());
                exif.setLatLong(37, -12);
                exif.saveAttributes();

                Log.d("Kyle", String.valueOf(exif.getLatLong()[0]));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Kyle", "Uh oh");
            }

        } else {

            //        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String name = "Test1";
            File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            Log.d("mylog", storageDir.toString());

            try {
                image = File.createTempFile(name, ".jpg", storageDir);

            } catch (IOException e) {
                Log.d("myLog", "Except : " + e.toString());
            }

        }



        return image;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(36.0822, -94.1719);
        float zoom = 14.0f;
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in fayetteville"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoom));
        theFunction();


    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
}
package com.example.servicemotor;

import static com.example.servicemotor.DBmain.TABLENAME;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.servicemotor.databinding.ActivityMain2Binding;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
public class MainActivity2 extends AppCompatActivity {
    private ActivityMain2Binding binding;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abut;
    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    int id = 0;
    public static final int MY_CAMERA_REQUEST_CODE = 100;
    public static final int MY_STORAGE_REQUEST_CODE = 101;
    String cameraPermission[];
    String storagePermission[];
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dBmain = new DBmain(this);
        preferences = getSharedPreferences("AndroidHiveLogin", 0);
        editor = preferences.edit();
        //findid();
        insertData();
        editData();
        binding.edtimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int avatar = 0;
                if (avatar == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (avatar == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        dl = (DrawerLayout) findViewById(R.id.dl);
        abut = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abut.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abut);
        abut.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent a = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_message) {
                    Intent a = new Intent(MainActivity2.this, MainActivity2.class);
                    startActivity(a);
                } else if (id == R.id.nav_profile) {
                    Intent a = new Intent(MainActivity2.this, MainActivity3.class);
                    startActivity(a);
                } else if (id == R.id.nav_api) {
                    Intent a = new Intent(MainActivity2.this, MainActivity4.class);
                    startActivity(a);
                } else if (id == R.id.nav_logout) {
                    editor.clear();
                    editor.commit();
                    Intent a = new Intent(MainActivity2.this, LoginActivity.class);
                    startActivity(a);
                }
                return true;
            }
        });

    }
    private void editData() {
        if (getIntent().getBundleExtra("userdata") != null) {
            Bundle bundle = getIntent().getBundleExtra("userdata");
            id = bundle.getInt("id");
            //for set name
            binding.edtname.setText(bundle.getString("name"));
            binding.edtstar.setText(bundle.getString("star"));
            binding.edtprice.setText(bundle.getString("price"));
            //for image
            byte[] bytes = bundle.getByteArray("avatar");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.edtimage.setImageBitmap(bitmap);
            //visible edit button and hide submit button
            binding.btnSubmit.setVisibility(View.GONE);
            binding.btnEdit.setVisibility(View.VISIBLE);
        }
    }
    private void requestStoragePermission() {
        requestPermissions(storagePermission, MY_STORAGE_REQUEST_CODE);
    }
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void pickFromGallery() {
        CropImage.activity().start(this);
    }
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, MY_CAMERA_REQUEST_CODE);
    }
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void insertData() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("name", binding.edtname.getText().toString());
                cv.put("star", binding.edtstar.getText().toString());
                cv.put("avatar", imageViewToBy(binding.edtimage));
                cv.put("price", binding.edtprice.getText().toString());
                sqLiteDatabase = dBmain.getWritableDatabase();
                Long rec = sqLiteDatabase.insert("servis", null, cv);
                if (rec != null) {
                    Toast.makeText(MainActivity2.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    binding.edtname.setText("");
                    binding.edtimage.setImageResource(R.mipmap.ic_launcher);
                    binding.edtstar.setText("");
                    binding.edtprice.setText("");
                } else {
                    Toast.makeText(MainActivity2.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //for view display
        binding.btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, DisplayData.class));
            }
        });
        //for storing new data or update data
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("name", binding.edtname.getText().toString());
                cv.put("star", binding.edtstar.getText().toString());
                cv.put("price", binding.edtprice.getText().toString());
                cv.put("avatar", imageViewToBy(binding.edtimage));
                sqLiteDatabase = dBmain.getWritableDatabase();
                long recedit = sqLiteDatabase.update(TABLENAME, cv, "id=" + id, null);
                if (recedit != -1) {
                    Toast.makeText(MainActivity2.this, "Update Succesfully", Toast.LENGTH_SHORT).show();
//clear data adfte submit
                    binding.edtname.setText("");
                    binding.edtstar.setText("");
                    binding.edtprice.setText("");
                    binding.edtimage.setImageResource(R.mipmap.ic_launcher);
//edit hide and submit visible
                    binding.btnEdit.setVisibility(View.GONE);
                    binding.btnSubmit.setVisibility(View.VISIBLE);
                    Intent a = new Intent(MainActivity2.this, DisplayData.class);
                    startActivity(a);
                }
            }
        });
    }
    public static byte[] imageViewToBy(ImageView avatar) {
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && storage_accepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            } break;
            case MY_STORAGE_REQUEST_CODE: {
                boolean storage_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storage_accepted) {
                    pickFromGallery();
                } else {
                    Toast.makeText(this, "please enable storage permission", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(binding.edtimage);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abut.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
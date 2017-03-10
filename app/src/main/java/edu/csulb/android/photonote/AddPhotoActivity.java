package edu.csulb.android.photonote;

import android.Manifest;
import android.app.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private EditText photoName;
    private Button btnTakePhoto, btnSavePhoto;
    private String mfilePath;
    Uri file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        photoName = (EditText) findViewById(R.id.editCaption);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnSavePhoto = (Button) findViewById(R.id.btnSave);
        imageView = (ImageView) findViewById(R.id.imageView);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) ||
                ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                )) {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
        }
    }

    public void onClickTakePhoto(View view) throws IOException {
        photoName = (EditText) findViewById(R.id.editCaption);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = createImageFile();
            if(imageFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        imageFile);
                mfilePath = imageFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        String filename = imageFileName+".png";
        File sd = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dest = new File(sd, filename);
        return dest;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = (ImageView) findViewById(R.id.imageView);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mfilePath, bmOptions);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = 1;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mfilePath, bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void onClickSavePhoto(View view){
        String caption = photoName.getText().toString();
        if(mfilePath != null) {
            if (caption.trim().length() > 0) {
                // database handler
                NotesDatabaseHelper db = new NotesDatabaseHelper(getApplicationContext());
                // inserting new note into database
                db.insertImage(caption, mfilePath);
                // making input filed text to blank
                photoName.setText("");
                // Hiding the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(photoName.getWindowToken(), 0);
                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                finish();


            } else {
                Toast.makeText(getApplicationContext(), "Please enter caption", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please take a photo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mfilePath != null){
            File file = new File(mfilePath);
            file.delete();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}

package edu.csulb.android.photonote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPhotoActivity extends AppCompatActivity {
    private TextView caption, createdAt;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        caption = (TextView)findViewById(R.id.tvCaption);
        createdAt = (TextView)findViewById(R.id.tvCreatedAt);
        imageView = (ImageView)findViewById(R.id.imageView);
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            viewPhoto(bundle);
        }
    }

    public void viewPhoto(Bundle bundle){
        String strCaption = bundle.get("caption").toString();
        caption.setText(strCaption);
        String strCreatedAt = "Created At: "+bundle.get("date").toString();
        createdAt.setText(strCreatedAt);

        String filePath = bundle.get("filePath").toString();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}

package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a2017_09_13.sapi_news_2017.image.GlideApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * This class serves the purpose of creating an event.
 */
public class Add_EventActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etitle,edescription;
    Button bchoose_photo,bselect_location,bsave_event;
    ImageView ivselect;
    Double mlatitude , mlongitude;
    private FirebaseAuth mAuth;

    // Create a storage reference from our app
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final int SELECTED_PICTURE = 1;
    private static final int RC_MAPS_COORDINATE_ADD = 123;

    /**
     *
     * @param savedInstanceState which is a Bundle object containing the activity's previously saved state.
     *                           If theactivity has never existed before, the valu of the Bundle object is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event);


        etitle = (EditText)findViewById(R.id.etaetitle);
        edescription =(EditText)findViewById(R.id.etaedescription);
        bchoose_photo = (Button)findViewById(R.id.btaechoosephoto);
        bselect_location = (Button)findViewById(R.id.btaeselectlocation);
        bsave_event = (Button)findViewById(R.id.btaesave_event);
        ivselect = (ImageView)findViewById(R.id.ivaeselect);

        bselect_location.setOnClickListener(this);
        bsave_event.setOnClickListener(this);
        bchoose_photo.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }


    /**
     * When the user is done with the subsequent activity and returns,the system calls your activity sonActivityResult() method.
     * This method includes three arguments : The request code you passed to startActivityForResult();
     * A result code specified by the second activity. This is either RESULT_OK if the operation was successful or  RESULT_CANCELED
     * if the user backed out or the operation failed for some reason.
     * @param requestCode what we give it will return to this.
     * @param resultCode specify that it was successful or failed.
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            return;
        }
        switch (requestCode){
            case SELECTED_PICTURE:
                Uri uri = data.getData();
                String[]projection= {MediaStore.Images.Media.DATA};

                Cursor cursor =getContentResolver().query(uri,projection,null,null,null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                GlideApp.with(getApplicationContext()).load(filePath).into(ivselect);


                StorageReference filepath = storageReference.child("Photos").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Add_EventActivity.this,"Upload Done.",Toast.LENGTH_LONG).show();
                    }
                });

            case RC_MAPS_COORDINATE_ADD:
                mlatitude=data.getDoubleExtra("latitude", 0);
                mlongitude=data.getDoubleExtra("longitude", 0);

            default:
                break;
        }
    }

    /**
     * there it is the onClick event choice of options.
     * @param v this decide when to call.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btaechoosephoto:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECTED_PICTURE);
                break;

            case R.id.btaeselectlocation:
                Intent i2 = new Intent(Add_EventActivity.this, MapsActivity.class);
                startActivityForResult(i2,RC_MAPS_COORDINATE_ADD);
                break;

            case R.id.btaesave_event:

                String cID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                Long tsLong = System.currentTimeMillis()/1000;

                DatabaseReference addEventsDatabaseReference = FirebaseDatabase.getInstance()
                        .getReference("events").child(cID);
                addEventsDatabaseReference.child("coordinate").child("latitude").setValue(mlatitude);
                addEventsDatabaseReference.child("coordinate").child("longitude").setValue(mlongitude);
                addEventsDatabaseReference.child("title").setValue(etitle.getText().toString());
                addEventsDatabaseReference.child("description").setValue(edescription.getText().toString());
                //Todo : user ideje kellene ide plusz az image et kelelne meg betenni
                addEventsDatabaseReference.child("pictures").child("1").child("pictureId").setValue("1");
                addEventsDatabaseReference.child("uId").setValue(mAuth.getCurrentUser().getUid());

                Toast.makeText(Add_EventActivity.this, "New event created",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;

            default:
                break;
        }

    }
}

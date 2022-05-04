package com.example.ptunga;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Register extends AppCompatActivity {
    ImageButton btnClose;
    ImageView usrImage;
    Uri imageUri;
    Coms coms;
    FirebaseAuth auth;
    StorageReference stRef;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;
    EditText usrFirstName,usrLastName,usrPhone,usrCountry,usrDistrict,usrFullAddress,usrEmail,usrPassword,usrConfirmPW,usrProvince;
    Button btnRegister;
    private final static int GALLERY_REQUEST_CODE=100;
    public static final int CAMERA_REQUEST_CODE=100;
    public static final int STORAGE_REQUEST_CODE=200;
    public static final int IMAGE_GALLERY_REQUEST_CODE=300;
    public static final int IMAGE_CAMERA_REQUEST_CODE=400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coms=new Coms(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);


        setContentView(R.layout.activity_register);
        usrFirstName=findViewById(R.id.usrFirstName);
        usrLastName=findViewById(R.id.usrLastName);
        usrPhone=findViewById(R.id.usrPhone);
        usrEmail=findViewById(R.id.usrEmail);
        usrCountry=findViewById(R.id.usrCountry);
        usrProvince=findViewById(R.id.usrProvince);
        usrDistrict=findViewById(R.id.usrDistrict);
        usrFullAddress=findViewById(R.id.usrFullAddress);
        usrPassword=findViewById(R.id.usrPassword);
        usrConfirmPW=findViewById(R.id.usrConfirmPW);
        btnRegister=findViewById(R.id.btnRegister);
        btnClose=findViewById(R.id.btnClose);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              validateData();
            }

        });
        usrImage=findViewById(R.id.usrImage);
        usrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent,GALLERY_REQUEST_CODE);
                showChooser();

            }
        });
    }

    private void showChooser() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        String [] options={"From Camera","From Gallery"};
        builder.setTitle("Pick Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    //
                    if (checkCameraPermission()){
                        pickFromCamera();

                    }
                    else {
                        requestCameraPermission();
                    }
                }
                else{
                    if (checkGalleryPermission()){
                        pickFromGallery();

                    }
                    else {
                        requestCameraPermission();
                    }

                }
            }
        });
        builder.show();
    }

    private void validateData() {
        if(imageUri==null){
            coms.message("Image Error","Kindly select an image");
            return;
            //send message to user

        }
        if(TextUtils.isEmpty(usrFirstName.getText().toString())){
            usrFirstName.setError("FirstName required");
            usrFirstName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(usrLastName.getText().toString())){
            usrFirstName.setError("LastName required");
            usrLastName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(usrPhone.getText().toString())||usrPhone.getText().toString().length()<12){
            usrFirstName.setError("Invalid phone");
            usrPhone.requestFocus();
            return;
    }
        //Validate remaining fields.
        if(!Patterns.EMAIL_ADDRESS.matcher(usrEmail.getText().toString()).matches()){
          usrEmail.setError("Invalid Email");
          usrEmail.requestFocus();
          return;
        }
        if(!usrPassword.getText().toString().equals(usrConfirmPW.getText().toString().toString())){
           usrConfirmPW.setError("Password mismatch");
           usrConfirmPW.requestFocus();
           return;
        }
        createUserAccount();
    }

    private void createUserAccount() {
        auth=FirebaseAuth.getInstance();
        progressDialog.setTitle("Creating User Account");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(usrEmail.getText().toString(),usrPassword.getText().toString()).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //save to firebase
                        saveToDatabase();

                    }

                    private void saveToDatabase() {
                        progressDialog.setTitle("Save user Infos");
                        progressDialog.show();
                        String filename="ProfileImages/"+auth.getUid();
                        stRef= FirebaseStorage.getInstance().getReference(filename);
                        stRef.putFile(imageUri).
                        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        saveAll(uri);
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                coms.message("Image Error",e.getMessage());
                            }
                        })
                        ;
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
                        progressDialog.dismiss();
                        coms.message("Error Creating Account",e.getMessage());
                    }
                });

    }

    private void saveAll(Uri uri) {
        progressDialog.setTitle("Saving Data");
        progressDialog.show();
        dbRef= FirebaseDatabase.getInstance().getReference("InfoUsers");
        User user=new User(uri.toString(),usrFirstName.getText().toString(),
                usrLastName.getText().toString(),
                usrPhone.getText().toString(),
                usrCountry.getText().toString(),
                usrProvince.getText().toString(),
                usrDistrict.getText().toString(),
                usrFullAddress.getText().toString(),
                usrEmail.getText().toString());
        dbRef.child(auth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                //startActivity(new Intent(Register.this,Login.class));
                startActivity(new Intent(Register.this,Login.class));

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                coms.message("Data Error",e.getMessage());
            }
        })

        ;
    }

    private void requestCameraPermission() {
        String [] cameraPersmissions={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this,cameraPersmissions,CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_image title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_image descriptoion");

        imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,IMAGE_CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        String cameraPermission=Manifest.permission.CAMERA;
        String storagePermission=Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return ContextCompat.checkSelfPermission(this,cameraPermission)== PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,storagePermission)==PackageManager.PERMISSION_GRANTED;
    }

    private void requestGalleryPermission() {
        String[] galleryPermissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this,galleryPermissions,STORAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_GALLERY_REQUEST_CODE);

    }

    private boolean checkGalleryPermission() {
        String gallerryPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return ContextCompat.checkSelfPermission(this,
                gallerryPermission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            usrImage.setImageURI(imageUri);
        }
    }
}
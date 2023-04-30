package com.shekhar.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    private EditText Name;
    Button submit;
    Bitmap bitmap;
    Uri imageUri;

    TextInputEditText textInputEditText;

    ImageView imageView;
    Button button;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    int REQUEST_CODE = 12345;
    boolean isPermissionGranted = false;

    String[] item = {"L12","L13","L14"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapteritems;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FirebaseFirestore database;

    private StorageReference storageRef;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        submit = findViewById(R.id.submit);
        Name = findViewById(R.id.full_name_text_field);
        textInputEditText = findViewById(R.id.description_box);

        database = FirebaseFirestore.getInstance();

        // Create a Cloud Storage reference from the app
          storageRef = FirebaseStorage.getInstance().getReference();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = Name.getText().toString();
                String s = autoCompleteTextView.getText().toString();
                String d = textInputEditText.getText().toString();

                if(n.trim().isEmpty()){
                    Toast.makeText(Home.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(s.trim().isEmpty()){
                    Toast.makeText(Home.this, "Please select your section", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(d.trim().isEmpty()){
                    Toast.makeText(Home.this, "Please write some description", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(bitmap==null){
                    Toast.makeText(Home.this, "Please add image", Toast.LENGTH_SHORT).show();
                    return;
                }




                //Upload image to firebase
                final ProgressDialog progressDialog = new ProgressDialog(Home.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();




                // Create a reference
                StorageReference imageRef = storageRef.child("images/"+Timestamp.now()+".jpg");

                UploadTask uploadTask = imageRef.putFile(imageUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismiss();
                        Toast.makeText(Home.this, "Unable to upload image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        progressDialog.dismiss();


                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {



                                //Store data in Fire store : database
                                Map<String, Object> complaintMap = new HashMap<>();
                                complaintMap.put("name", n);
                                complaintMap.put("section", s);
                                complaintMap.put("description", d);
                                complaintMap.put("image", uri.toString());
                                complaintMap.put("timestamp", Timestamp.now());


                                // Add a new document with a generated ID
                                database.collection("complaints")
                                        .add(complaintMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());

                                                //Image : Pass image uri
                                                Intent intent = new Intent(Home.this,Complaint_status.class);

                                                intent.putExtra("Name",n);
                                                intent.putExtra("Section",s);
                                                intent.putExtra("description",d);
                                                intent.putExtra("image", imageUri.toString());


                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Firestore", "Error adding document", e);
                                            }
                                        });




                            }
                        });

                    }
                }) .addOnProgressListener((OnProgressListener<? super UploadTask.TaskSnapshot>) taskSnapshot -> {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                });







            }
        });
        imageView = findViewById(R.id.image);
        button = findViewById(R.id.Pick);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();

            }
        });


        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapteritems = new ArrayAdapter<String>(this,R.layout.list_item,item);

        autoCompleteTextView.setAdapter(adapteritems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Home.this,"Item:" +item,Toast.LENGTH_SHORT).show();
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationview);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }


        }




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_TAG","Home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(Home.this,Homepage.class);
                        startActivity(intent);

                    case R.id.nav_settings:
                        Log.i("MENU_DRAWER_TAG","Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Home.this,LoginPage.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();




                }
                return true;
            }
        });
    }
    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            isPermissionGranted = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Complete Action Using"),REQUEST_CODE);

        }
        else {
            ActivityCompat.requestPermissions(Home.this, permissions, REQUEST_CODE);
        }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true;
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data !=null)
        {
            Uri filePath = data.getData();
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Nothing selected",Toast.LENGTH_LONG).show();
        }
    }
}
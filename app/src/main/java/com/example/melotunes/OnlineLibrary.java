package com.example.melotunes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class OnlineLibrary extends AppCompatActivity {
    private boolean checkPermission=false;
    Uri uri;
    String songName,SongUrl;
    ListView listView;
    ArrayList<String> arrayListSongName=new ArrayList<>();
    ArrayList<String> arrayListSongUrl=new ArrayList<>();
    ArrayAdapter<String>arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_library);
        listView=findViewById(R.id.listview);
        retrieveSongs();
    }

    private void retrieveSongs() {
        DatabaseReference dbReference=FirebaseDatabase.getInstance().getReference("Songs");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds:snapshot.getChildren()){
                    UploadSong song=ds.getValue(UploadSong.class);
                    arrayListSongName.add(song.getSongName());
                    arrayListSongUrl.add(song.getSongUrl());

               }
               arrayAdapter=new ArrayAdapter<String>(OnlineLibrary.this, android.R.layout.simple_list_item_1,arrayListSongName);
               listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
      getMenuInflater().inflate(R.menu.menu_res,menu);
     return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem)
    {
        if(menuItem.getItemId()==R.id.upload){
          if(validatePermission()){
              pickSong();
          }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void pickSong() {
        Intent intent_upload=new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode ==RESULT_OK){
               uri=data.getData();
               Cursor cursor=getApplicationContext().getContentResolver()
                       .query(uri,null,null,null,null);
               int indexedname=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
               cursor.moveToFirst();
               songName=cursor.getString(indexedname);
               cursor.close();
                uploadSongToFirebase();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadSongToFirebase() {
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Songs")
                .child(uri.getLastPathSegment());
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                    Uri songUrl=uriTask.getResult();
                    SongUrl=songUrl.toString();
                    uploadDetailsToDatabase();
                 progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OnlineLibrary.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                int currentProgress = (int)progress;
                progressDialog.setMessage("Uploaded: "+currentProgress+"%");

            }
        });
    }

    private void uploadDetailsToDatabase() {
       UploadSong Song= new UploadSong(songName,SongUrl);
        FirebaseDatabase.getInstance().getReference("Songs").push()
                .setValue(Song).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                {
                    Toast.makeText(OnlineLibrary.this,"Song Uploaded",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OnlineLibrary.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePermission()
    {
        Dexter.withActivity(OnlineLibrary.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        checkPermission=true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                         checkPermission=false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
        return checkPermission;
    }
}

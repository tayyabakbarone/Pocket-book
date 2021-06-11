package com.example.application.pocketbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ReceiptActivity extends AppCompatActivity {
    ImageView backBtn;
    Button uploadBtn;
    Button FillR;
    Button retrievePdfFile;
    EditText selectFromStorage;
    ImageView attachmentBtn;

    StorageReference storageReference;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        FillR = findViewById(R.id.FillR);
        backBtn = findViewById(R.id.backBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        retrievePdfFile = findViewById(R.id.retrievePdfFile);
        selectFromStorage =  findViewById(R.id.selectFromStorage);
        attachmentBtn = findViewById(R.id.attachmentBtn);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");
        
        uploadBtn.setEnabled(false);

        attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

        /*
        selectFromStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


*/
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiptActivity.this,MenuActivity.class));
            }
        });
        FillR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ReceiptActivity.this, FillInReceipt.class));
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"),12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uploadBtn.setEnabled(true);
            selectFromStorage.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPDFFileFirebase(data.getData());
                }
            });
        }
    }

    private void uploadPDFFileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is Loading");
        progressDialog.show();
        StorageReference reference = storageReference.child("upload"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                    Uri uri = uriTask.getResult();
                    putPDF putPDF = new putPDF(selectFromStorage.getText().toString(),uri.toString());
                    databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                    Toast.makeText(ReceiptActivity.this,"File Upload",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress =(100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded...."+(int)progress+"%");

            }
        });

    }

    public void retrievePDF(View view) {
        startActivity(new Intent(getApplicationContext(),RetrievePDFActivity.class));
    }

}

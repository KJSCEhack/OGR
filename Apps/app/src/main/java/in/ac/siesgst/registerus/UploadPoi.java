package in.ac.siesgst.registerus;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class UploadPoi extends AppCompatActivity {

    ImageView docImage;
    TextView heading;
    Button uploadImage,processImage;
    Uri resultUri;

    String name,email;

    private static Uri imageUri = null;
    private Bitmap bmp;

    int flagZero = 0;
    int flagOne = 0;

    String poiValue;
    String poaValue;

    String poiUri;
    String poaUri;

    private final int select_photo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_poi);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        docImage = findViewById(R.id.share_imageview);
        heading = findViewById(R.id.heading);
        uploadImage = findViewById(R.id.select_image);
        processImage = findViewById(R.id.process);

        if(flagZero == 0 && flagOne == 0) {
            heading.setText("Scan the required part of your POA: ");
        }
        else if(flagZero == 0 && flagOne ==1) {
            heading.setText("Scan the full POA: ");
        }
        else if(flagZero == 1 && flagOne ==0) {
            heading.setText("Scan the required part of your POI: ");
        }
        else if(flagZero == 1 && flagOne ==1) {
            heading.setText("Scan the full POI: ");
        }


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"SELECT IMAGE TO CONTINUE"), select_photo);
            }
        });

        processImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagZero == 0 &&flagOne == 0) {
                    if (bmp == null) {
                        Toast.makeText(UploadPoi.this, "First upload image", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bmp);
                        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                        textRecognizer.processImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText result) {
                                        String str = result.getText();
                                        Log.d("CompleteTag",str);
                                        String abc[] = str.split("\n");
                                        String pqr = "";
                                        for(int m = 0 ; m < abc.length ; m++) {
                                            abc[m] = abc[m].replace("\n","");
                                            abc[m] = abc[m].replace(" ","");
                                            if(abc[m].length() == 12) {
                                                pqr = abc[m];
                                            }
                                        }

                                        poaValue = pqr;
                                        heading.setText("Scan the full POA: ");
                                        processImage.setText("Next");
                                        docImage.setImageResource(R.drawable.ic_launcher_background);
                                        flagZero = 0;
                                        flagOne = 1;
                                        Log.d("NEW DATA", poaValue);
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                    }
                }
                else if(flagZero == 0 && flagOne == 1) {
                    if (bmp == null) {
                        Toast.makeText(UploadPoi.this, "First upload image", Toast.LENGTH_SHORT).show();
                    } else {
                        poaUri = resultUri.toString();
                        docImage.setImageResource(R.drawable.ic_launcher_background);
                        heading.setText("Scan the required part of POI: ");
                        processImage.setText("Process");
                        flagZero = 1;
                        flagOne = 0;
                    }

                }
                else if(flagZero == 1 && flagOne == 0) {
                    if (bmp == null) {
                        Toast.makeText(UploadPoi.this, "First upload image", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bmp);
                        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                        textRecognizer.processImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText result) {
                                        String str = result.getText();
                                        String abc = str.substring(str.lastIndexOf("\n"));
                                        abc = abc.replace("\n","");
                                        abc = abc.replace(" ","");
                                        poiValue = abc;
                                        heading.setText("Scan the full POI: ");
                                        processImage.setText("Finish");
                                        docImage.setImageResource(R.drawable.ic_launcher_background);
                                        Log.d("NEW DATA", poaValue);
                                        flagZero = 1;
                                        flagOne = 1;
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                    }
                }
                else if(flagZero == 1 && flagOne == 1) {
                    if (bmp == null) {
                        Toast.makeText(UploadPoi.this, "First upload image", Toast.LENGTH_SHORT).show();
                    } else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(UploadPoi.this);
                        dialog.setTitle("Registration");
                        dialog.setMessage("Please wait while we register the user...");
                        final Dialog d = dialog.create();
                        d.setCanceledOnTouchOutside(true);
                        d.show();


                        poiUri = resultUri.toString();
                        processImage.setText("Finish");


                        String nums = "1234567890";
                        String alphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                        String ssn = "";
                        String pwd = "";

                        for(int i = 0 ; i < 5 ; i++) {
                            Random r = new Random();
                            ssn = ssn + String.valueOf(alphas.charAt(r.nextInt(25)));
                        }
                        for(int i = 5 ; i < 8 ; i++) {
                            Random r = new Random();
                            ssn = ssn + String.valueOf(nums.charAt(r.nextInt(10)));
                        }
                        for(int i = 0 ; i < 3 ; i++) {
                            Random r = new Random();
                            pwd = pwd + String.valueOf(nums.charAt(r.nextInt(10)));
                        }
                        for(int i = 3 ; i < 8 ; i++) {
                            Random r = new Random();
                            pwd = pwd + String.valueOf(alphas.charAt(r.nextInt(25)));
                        }

                        final String tempSsn = ssn;
                        final String tempPwd = pwd;

                        final StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                            .child("poa")
                            .child(tempSsn+".jpg");
                        storageRef.putFile(Uri.parse(poaUri))
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final String poaDownloadUrl = uri.toString();
                                                    final StorageReference newRef = FirebaseStorage.getInstance()
                                                            .getReference()
                                                            .child("poi")
                                                            .child(tempSsn+".jpg");
                                                    newRef.putFile(Uri.parse(poiUri)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    String poiDownloadUrl = uri.toString();

                                                                    DatabaseReference mReference = FirebaseDatabase.getInstance()
                                                                            .getReference()
                                                                            .child("Users")
                                                                            .child(tempSsn);

                                                                    HashMap<String,Object> map = new HashMap<>();
                                                                    map.put("poaNum",poaValue);
                                                                    map.put("poiNum",poiValue);
                                                                    map.put("poaUri",poaDownloadUrl);
                                                                    map.put("poiUri",poiDownloadUrl);
                                                                    map.put("name",name);
                                                                    map.put("email",email);
                                                                    map.put("password",tempPwd);

                                                                    mReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()) {
                                                                                d.hide();
                                                                                Intent ii = new Intent(UploadPoi.this,FinalActivity.class);
                                                                                ii.putExtra("ssn",tempSsn);
                                                                                ii.putExtra("pwd",tempPwd);
                                                                                startActivity(ii);
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                        Log.d("TAG","\nPoa:"+poaValue+"\nPoa Uri: "+poaUri+"\nPoi: "+poiValue+"\nPoi Uri"+poiUri);
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case select_photo:
                if (resultcode == RESULT_OK) {
                    Uri imageUri = imagereturnintent.getData();


                    //String path = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), bitmap, "Title", null);
                    //imageUri = Uri.parse(path);

                    CropImage
                            .activity(imageUri)
                            .setOutputCompressQuality(100)
                            .start(this);

                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE :
                CropImage.ActivityResult res = CropImage.getActivityResult(imagereturnintent);
                resultUri = res.getUri();
                Bitmap bb = BitmapFactory.decodeFile(resultUri.getPath());
                bmp = bb;
                docImage.setImageBitmap(bb);
                break;
        }
    }
}

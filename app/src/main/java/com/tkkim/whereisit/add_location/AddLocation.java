package com.tkkim.whereisit.add_location;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.detail_location.DetailLocation;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.io.File;
import java.io.IOException;

public class AddLocation extends AppCompatActivity {

    private DBHelper dbHelper;

    private ImageView ivLocImg;
    private EditText etLocName;
    private EditText etLocComment;
    private Button btnInsert;
    private Button btnImg;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_IMAGE_CROP = 3;

    private boolean update = false;
    private String locNo = "";
    private String locName = "";
    private String locComment = "";
    private String locImgpath = "";

    private Uri photoURI, albumURI = null;
    private Boolean album = false;
    private String imgFilePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_add_location);


        setContentView(R.layout.activity_add_location);

        ivLocImg = (ImageView) findViewById(R.id.ivLocImg);
        btnImg = (Button) findViewById(R.id.btnImg);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] ListString = {getString(R.string.d_open_gallery), getString(R.string.d_open_camera)};
                AlertDialog.Builder ab = new AlertDialog.Builder(AddLocation.this);
                ab.setTitle(getString(R.string.d_title_photo));
                ab.setIcon(R.drawable.ic_android_white_24dp);
                ab.setItems(ListString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                openGallery();
                                return;
                            case 1:
                                Toast.makeText(getApplicationContext(), getString(R.string.d_coming_soon) + ListString[item], Toast.LENGTH_SHORT).show();
                                return;
                        }
                    }
                });
                AlertDialog dialog = ab.create();
                dialog.show();

            }
        });
        etLocName = (EditText) findViewById(R.id.etLocName);
        etLocName.setText(locName);
        etLocComment = (EditText) findViewById(R.id.etLocComment);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locName = etLocName.getText().toString();
                locComment = etLocComment.getText().toString();


                if (locName.length() == 0 || locName == " " || locName == null) {
                    Toast.makeText(getApplicationContext(), "위치명을 적어주세요.", Toast.LENGTH_SHORT).show();
                } else if (imgFilePath.length() == 0 || imgFilePath == "" || imgFilePath == null) {
                    Toast.makeText(getApplicationContext(), "사진을 올려주세요.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder ab = new AlertDialog.Builder(AddLocation.this);
                    ab.setTitle("사진 올리기");
                    ab.setMessage("사진을 아직 안 올리셨네요.\n그대로 진행할까요?");
                    ab.setIcon(getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                    ab.setCancelable(false);
                    ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            imgFilePath = null;
                            addLoc();
                            infoSnackbar();
                        }
                    });

                    ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = ab.create();
                    dialog.show();
                } else {
                    addLoc();
                    infoSnackbar();
                }

            }
        });

        if (getIntent().getStringExtra(DetailLocation.PUT_LOC_NO) != null) {
            update = true;
            updateSetting();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addLoc() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(getApplicationContext(), dbHelper.DB_NAME, null, 1);
        }

        MyLocation data = new MyLocation();

        if (update) {
            data.setLoc_no(Integer.parseInt(locNo));
            data.setLoc_name(locName);
            data.setLoc_comment(locComment);
            data.setLoc_imgpath(imgFilePath);
            dbHelper.updateLoc(data);
            finish();
        } else {
            data.setLoc_name(locName);
            data.setLoc_comment(locComment);
            data.setLoc_imgpath(imgFilePath);
            dbHelper.addLoc(data);

            etLocName.setText("");
            etLocComment.setText("");
            imgFilePath = "";
            ivLocImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_black_24dp));
        }
    }

    private void updateSetting() {
        locNo = getIntent().getStringExtra(DetailLocation.PUT_LOC_NO);
        locName = getIntent().getStringExtra(DetailLocation.PUT_LOC_NAME);
        locComment = getIntent().getStringExtra(DetailLocation.PUT_LOC_COMMENT);
        locImgpath = getIntent().getStringExtra(DetailLocation.PUT_LOC_IMGPATH);

        if (locImgpath == null || locImgpath == "noPhoto") {

        } else {
            ivLocImg.setImageURI(Uri.fromFile(new File(locImgpath)));
        }

        imgFilePath = locImgpath;
        etLocName.setText(locName);
        etLocComment.setText(locComment);
    }

    private void infoSnackbar() {
        View wView = getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(wView, "insert OK~! 추가완료", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.BLUE)
                .setAction("GOOD", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.colorFab));
        snackbar.show();
    }

    // 사진찍기
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File file = null;
            try {
                file = createImageFile(); // 사진찍은 후 저장할 임시 파일
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "createImageFile Failed", Toast.LENGTH_LONG).show();
            }

            if (file != null) {
                photoURI = Uri.fromFile(file); // 임시 파일의 위치,경로 가져옴
                albumURI = Uri.fromFile(file); // 임시 파일의 위치,경로 가져옴
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // 임시 파일 위치에 저장
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 저장할 폴더 생성
    private File createImageFile() throws IOException {
//         Create an image file name, 폴더명 지정 방법 (문제 : DIRECTORY_DCIM , DIRECTORY_PICTURE 경로가 없는 폰 존재)
//        String imageFileName = "tmp_" + String.valueOf(System.currentTimeMillis());
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/APPmyTEST");
//        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
//        mCurrentPhotoPath = file.getAbsolutePath();
//        return file;


        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaPhoto");
        if (!fileDir.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            fileDir.mkdir();


        imgFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaPhoto/" + System.currentTimeMillis() + ".jpg";
        File file = new File(imgFilePath);


//         특정 경로와 폴더를 지정하지 않고, 메모리 최상 위치에 저장 방법
//        String imgName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        File file = new File(Environment.getExternalStorageDirectory(), imgName);
//        mCurrentPhotoPath = storageDir.getAbsolutePath();
        return file;
    }

    private void openGallery() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void cropImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(photoURI, "image/*");
        intent.putExtra("outputX", 1000); // crop한 이미지의 x축 크기
        intent.putExtra("outputY", 1000); // crop한 이미지의 y축 크기
        intent.putExtra("aspectX", 1); // crop 박스의 x축 비율
        intent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        intent.putExtra("scale", true);

        if (album == false) {
            intent.putExtra("output", photoURI); // 크랍된 이미지를 해당 경로에 저장
        } else if (album == true) {
            intent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        }


        startActivityForResult(intent, REQUEST_IMAGE_CROP);
    }

    // ActivityResult = 가져온 사진 뿌리기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));

        if (resultCode != RESULT_OK) {
            Log.d("===Intent===", "onActivityResult : RESULT_NOT_OK");
        } else {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: // 앨범 이미지 가져오기
                    album = true;
                    File file = null;
                    try {
                        file = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file != null) {
                        albumURI = Uri.fromFile(file); // 앨범 이미지 Crop한 결과는 새로운 위치 저장
                    }

                    photoURI = data.getData(); // 앨범 이미지의 경로
//                    photoURI = albumURI; // 앨범 이미지의 경로

                    // break; REQUEST_IMAGE_CAPTURE로 전달하여 Crop
                case REQUEST_IMAGE_CAPTURE:

                    cropImage();

                    break;
                case REQUEST_IMAGE_CROP:


//                    Bitmap bitmap = BitmapFactory.decodeFile(photoURI.getPath());
//                    iv_capture.setImageBitmap(bitmap);

                    uploadImage();

//                    Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE ); // 동기화
//                    if(album == false) {
//                        mediaScanIntent.setData(photoURI); // 동기화
//                    } else if(album == true){
//                        album = false;
//                        mediaScanIntent.setData(albumURI); // 동기화
//                    }
//                    this.sendBroadcast(mediaScanIntent); // 동기화

                    break;
            }
        }
    }

    private void uploadImage() {

//                     iv_capture 에 띄우기
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivLocImg.setImageBitmap(bitmap);
    }

}

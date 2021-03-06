package com.tkkim.whereisit.add_stuff;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.add_stuff.data.Stuff;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class AddStuff extends AppCompatActivity {

    private DBHelper dbHelper;

    private ImageView ivStuImg;
    private Button btnImg;
    private TextView tvSelectedLoc;
    private Button btnChoice;
    private EditText etStuName;
    private EditText etStuComment;
    private Button btnInsert;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_IMAGE_CROP = 3;
    public static final int REQUEST_ACT = 4;

    private Uri photoURI, albumURI = null;
    private Boolean album = false;
    private String imgFilePath = "";

    private ArrayList<MyLocation> items;
    private ArrayList<MyLocation> arrLocNo;
    public Boolean boolChoiced = false;
    public static final String PUT_ITEM = "locItem";
    public static final String PUT_FROM_Detail_Loc = "detailLoc";
    public static final String PUT_ARR_LOC_NO = "locNo";
    public static final String PUT_CHOICED = "choiced";
    public static final String PUT_LOC_NAME = "locName";
    private String locNo = "";
    private String locName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stuff);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_add_stuff);


        ivStuImg = (ImageView) findViewById(R.id.ivStuImg);

        btnImg = (Button) findViewById(R.id.btnImg);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] ListString = {getString(R.string.d_open_gallery), getString(R.string.d_open_camera)};
                AlertDialog.Builder ab = new AlertDialog.Builder(AddStuff.this);
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

        tvSelectedLoc = (TextView) findViewById(R.id.tvSelectedLoc);

        btnChoice = (Button) findViewById(R.id.btnChoice);
        btnChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChoiceLocation.class);
                if (items != null) {
                    intent.putExtra(PUT_ITEM, items);
                    intent.putExtra(PUT_ARR_LOC_NO, arrLocNo);
                    intent.putExtra(PUT_CHOICED, boolChoiced);
                }
                startActivityForResult(intent, REQUEST_ACT);
            }
        });
        etStuName = (EditText) findViewById(R.id.etStuName);
        etStuComment = (EditText) findViewById(R.id.etStuComment);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String locName = tvSelectedLoc.getText().toString();
                String stuName = etStuName.getText().toString();
                String stuComment = etStuComment.getText().toString();

                if (locName.length() == 0 || locName == " ") {
                    Toast.makeText(getApplicationContext(), "위치를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (stuName.length() == 0 || stuName == " ") {
                    Toast.makeText(getApplicationContext(), "물건명을 적어주세요.", Toast.LENGTH_SHORT).show();
                } else if (stuComment.length() == 0 || stuComment == " ") {
                    Toast.makeText(getApplicationContext(), "물건설명을 적어주세요.", Toast.LENGTH_SHORT).show();
                } else if (imgFilePath.length() == 0 || imgFilePath == "" || imgFilePath == null) {
                    Toast.makeText(getApplicationContext(), "사진을 올려주세요.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder ab = new AlertDialog.Builder(AddStuff.this);
                    ab.setTitle("사진 올리기");
                    ab.setMessage("사진을 아직 안 올리셨네요.\n그대로 진행할까요?");
                    ab.setIcon(getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                    ab.setCancelable(false);
                    ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            imgFilePath = null;
                            addStuff();
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
                    addStuff();
                }

            }
        });

        getLocAll();

        if (getIntent().getBooleanExtra(PUT_FROM_Detail_Loc, false)) {
            arrLocNo = (ArrayList<MyLocation>) getIntent().getSerializableExtra(PUT_ARR_LOC_NO);
            signLocName(arrLocNo);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

                case REQUEST_IMAGE_CAPTURE:
                    cropImage();
                    break;

                case REQUEST_IMAGE_CROP:
                    uploadImage();
                    break;

                case REQUEST_ACT:
                    arrLocNo = (ArrayList<MyLocation>) data.getSerializableExtra(PUT_ARR_LOC_NO);
                    signLocName(arrLocNo);
                    break;
            }
        }
    }

    private void signLocName(ArrayList<MyLocation> arrLocNo) {
        if (arrLocNo != null) {
            boolChoiced = true;

            tvSelectedLoc.setText(arrLocNo.get(0).getLoc_name());
            if (arrLocNo.size() > 1) {
                tvSelectedLoc.setText(arrLocNo.get(0).getLoc_name() + " + " + (arrLocNo.size() - 1));
            }
        }
    }

    private void getLocAll() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(this, DBHelper.DB_NAME, null, 1);
        }
        items = dbHelper.getLocAll();
    }

    private void addStuff() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(this, dbHelper.DB_NAME, null, 1);
        }
        Stuff data = new Stuff();
        data.setStu_name(etStuName.getText().toString());
        data.setStu_comment(etStuComment.getText().toString());
        data.setStu_imgpath(imgFilePath);
        dbHelper.addStuff(data, arrLocNo);

        tvSelectedLoc.setText("");
        etStuName.setText("");
        etStuComment.setText("");
//        ivStuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_black_24dp));
//        ivStuImg.setVisibility(View.VISIBLE);
        imgFilePath = "";
        ivStuImg.setVisibility(View.GONE);
    }


    // 저장할 폴더 생성
    private File createImageFile() throws IOException {
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaPhoto");
        if (!fileDir.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            fileDir.mkdir();

        imgFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaPhoto/" + System.currentTimeMillis() + ".jpg";
        File file = new File(imgFilePath);
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

    private void uploadImage() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivStuImg.setImageBitmap(bitmap);
        ivStuImg.setVisibility(View.VISIBLE);
    }

}

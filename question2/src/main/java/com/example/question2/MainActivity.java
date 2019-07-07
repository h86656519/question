package com.example.question2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private Button camera_btn, merge_btn;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    private static final int CAMERA_REQUEST_CODE = 0x001;
    private Uri imageUri, mCutUri;
    private File path;
    private static final int REQUSETCODE = 100;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private String folderName = "question2";  //資料夾名稱
    private Uri cropImageUri;
    private Bitmap Outerframe;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folderName + "/" + "photo.jpg"); //拍照的原圖
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folderName + "/" + "crop_photo.jpg"); //相機裁修過後的圖片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions(); //先確認權限
        initPath();  //建立要儲存的資料夾
        initView();
        listener();
    }

    public void listener() {
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = Uri.fromFile(fileUri); //原圖
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //通过FileProvider创建一个content类型的Uri
                    imageUri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getPackageName() + ".fileprovider", fileUri);
                    takePicture(imageUri, CAMERA_REQUEST_CODE);  //啟動相機
                }
            }
        });

        merge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fis = null;
                if (Outerframe != null) {
                    try {
                        fis = new FileInputStream(fileCropUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap backBitmap = BitmapFactory.decodeStream(fis);
                    Bitmap finshBitmap = combineBitmap(backBitmap, Outerframe);
                    generateCompositePhoto(finshBitmap);
                } else {
                    Toast.makeText(MainActivity.this, "請先選擇要合成的外框", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outerframe = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.frame);
                Toast.makeText(MainActivity.this, "選擇 : 點外框", Toast.LENGTH_SHORT).show();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outerframe = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.frame_film);
                Toast.makeText(MainActivity.this, "選擇 : 底片外框", Toast.LENGTH_SHORT).show();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outerframe = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.frame_lace);
                Toast.makeText(MainActivity.this, "選擇 : 雷絲外框", Toast.LENGTH_SHORT).show();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outerframe = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.frame_flower);
                Toast.makeText(MainActivity.this, "選擇 : 花外框", Toast.LENGTH_SHORT).show();
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outerframe = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.frame_wood);
                Toast.makeText(MainActivity.this, "選擇 : 木外框", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initView() {
        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);
        imageView5 = findViewById(R.id.image5);
        camera_btn = findViewById(R.id.camera);
        merge_btn = findViewById(R.id.mergecamera);
    }

    public void takePicture(Uri imageUri, int requestCode) {
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, requestCode);
    }

    //建存放的資料夾
    private void initPath() {
        path = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folderName + "/");
        //  Environment.getExternalStorageDirectory() 抓的是 SD卡的位置
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    cropImageUri(imageUri, cropImageUri, 1, 1, 480, 480, CODE_RESULT_REQUEST);
                    break;

                case CODE_RESULT_REQUEST:
                    if (data != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(MainActivity.this.getContentResolver().openInputStream(mCutUri));//產生Bitmap
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fileUri.delete();
                    }
                    break;
            }
        }
    }

    public void cropImageUri(Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        mCutUri = desUri;

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUSETCODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(MainActivity.this, "權限已取得", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    //判斷 "不在詢問"
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.CAMERA)) {
                        //沒按下前
                        Toast.makeText(MainActivity.this, "請到設定開啟權限", Toast.LENGTH_SHORT).show();
                    } else {
                        //有按下後
                        Toast.makeText(MainActivity.this, "若要使用次功能，請到設定開啟權限2", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    public void checkPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUSETCODE);
    }


    public Bitmap combineBitmap(Bitmap backBitmap, Bitmap frontBitmap) {
        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true); //Bitmap.Config.ARGB_8888 圖片質量高
        Canvas canvas = new Canvas(bitmap);
        Rect baseRect = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
        Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, frontRect, baseRect, null);
        return bitmap;
    }

    public void generateCompositePhoto(Bitmap finshBitmap) {
        try {
            File file = new File(path, "Image.png");   // 開啟檔案
            FileOutputStream out = new FileOutputStream(file); // 開啟檔案串流
            finshBitmap.compress(Bitmap.CompressFormat.PNG, 90, out); // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
            out.flush(); // 刷新並
            out.close(); // 關閉檔案串流
            Toast.makeText(MainActivity.this, "合成成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "合成失敗", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "合成失敗", Toast.LENGTH_SHORT).show();
        }
    }
}
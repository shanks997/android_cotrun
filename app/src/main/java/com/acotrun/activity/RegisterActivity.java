package com.acotrun.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acotrun.R;
import com.acotrun.utility.BitmapUtils;
import com.acotrun.utility.Constant;
import com.acotrun.utility.NetInfoUtil;
import com.acotrun.utility.ResponseUtil;
import com.acotrun.utility.UploadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private ImageView iv_avatar;
    private RelativeLayout rl_head;
    private LinearLayout ll_sex;
    private PopupWindow pop;
    private InputMethodManager imm;
    private AlertDialog.Builder builder;
    private Button btn_ok;
    private EditText edt_name;
    private EditText edt_pwd;
    private TextView tv_sex;

    private String name;
    private String pwd;
    private String sex;
    private Handler handler;
    private static boolean flag;
    private static final String TAG = "RegisterActivity";

    // 请求识别码
    private static final int REQUEST_IMAGE_GET = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 2;

    private final int PERMISSION_READ_AND_CAMERA = 0;// 读和相机权限
    private final int PERMISSION_READ = 1;// 读取权限
    private static final String IMAGE_FILE_NAME = "avatar.jpg";
//    private static final String CROP_IMAGE_FILE_NAME = "avatar_crop.jpg";
    private static final String path = Environment.getExternalStorageDirectory() + File.separator + "acotrun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        iv_avatar = findViewById(R.id.reg_iv_avatar);
        rl_head = findViewById(R.id.rl_head);
        rl_head.setOnClickListener(this);
        ll_sex = findViewById(R.id.ll_sex);
        ll_sex.setOnClickListener(this);
        btn_ok = findViewById(R.id.reg_btn_ok);
        btn_ok.setOnClickListener(this);
        edt_name = findViewById(R.id.reg_edt_name);
        edt_pwd = findViewById(R.id.reg_edt_pwd);
        tv_sex = findViewById(R.id.reg_tv_sex);

        initView();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:					// 注册成功
                        if (flag) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            RegisterActivity.this.finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
    }

    private void initView() {
        pop = new PopupWindow();
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0xa0000000);
        pop.setBackgroundDrawable(dw);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        View view = getLayoutInflater().inflate(R.layout.head_portrait,
                null);
        pop.setContentView(view);
        View parent = view.findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        TextView camera = view.findViewById(R.id.hp_camera);
        TextView lib = view.findViewById(R.id.hp_lib);
        TextView cancel = view.findViewById(R.id.hp_cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拍照及文件权限申请
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 权限还没有授予，进行申请
                    // 申请的 requestCode 为 300
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_READ_AND_CAMERA);
                } else {
                    // 权限已经申请，直接拍照
                    pop.dismiss();
                    Uri pictureUri;
                    Intent it_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File pictureFile = new File(path, IMAGE_FILE_NAME);
                    // 判断当前系统
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pictureUri = FileProvider.getUriForFile(RegisterActivity.this,
                                getPackageName()+".fileProvider", pictureFile);
                    } else {
                        it_camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        pictureUri = Uri.fromFile(pictureFile);
                    }
                    // 去拍照,拍照的结果存到 pictureUri 对应的路径中
                    it_camera.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                    startActivityForResult(it_camera, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 图库权限申请
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // 权限还没有授予，进行申请
                    // 申请的 requestCode 为 200
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_READ);
                } else {
                    // 如果权限已经申请过，直接进行图片选择
                    pop.dismiss();
                    Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                    intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    // 判断系统中是否有处理该 Intent 的 Activity
                    if (intentFromGallery.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intentFromGallery, REQUEST_IMAGE_GET);
                    } else {
                        Toast.makeText(RegisterActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ:
            case PERMISSION_READ_AND_CAMERA:
        }
    }

    private void startPhotoZoom(Uri uri) {
        //保存裁剪后的图片
        File cropFile = new File(path, IMAGE_FILE_NAME);
        try {
            if(cropFile.exists()) {
                cropFile.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Uri cropUri;
        cropUri = Uri.fromFile(cropFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        String mLinshi = System.currentTimeMillis() + IMAGE_FILE_NAME;
        File mFile = new File(path, mLinshi);
        try {
            if(mFile.exists()) {
                mFile.delete();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        Uri mUriPath = Uri.parse("file://" + mFile.getAbsolutePath());
        //将裁剪好的图输出到所建文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_SMALL_IMAGE_CUTTING);
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 切割
                case REQUEST_SMALL_IMAGE_CUTTING:
                    showHeadImage();
                    break;

                // 相册选取
                case REQUEST_IMAGE_GET:
                    Uri uri = getImageUri(this, data);
                    startPhotoZoom(uri);
                    break;

                // 拍照
                case REQUEST_IMAGE_CAPTURE:

                    File tempFile = new File(
                            path,
                            IMAGE_FILE_NAME);
                    cropRawPhoto(getImageContentUri(tempFile));
                    break;
            }
        }
    }

    private void showHeadImage() {
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
        if (isSdCardExist) {
            File file = new File(path, IMAGE_FILE_NAME);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(path + File.separator + IMAGE_FILE_NAME);
                // 将图片显示到ImageView中
                iv_avatar.setImageBitmap(bm);
            }
        }
    }

    public Uri getImageUri(Context context, Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(Build.VERSION.SDK_INT >= 19){
            if(DocumentsContract.isDocumentUri(context,uri)){
                String docId = DocumentsContract.getDocumentId(uri);
                if("com.android.providers.media.documents".equals(uri.getAuthority())){
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID+"="+id;
                    imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
                }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                    imagePath = getImagePath(context,contentUri,null);
                }
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(context,uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
            }
        }else{
            uri= data.getData();
            imagePath = getImagePath(context,uri,null);
        }
        File file = new File(imagePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context,
                    getPackageName()+".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private String getImagePath(Context context,Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onClick(View view) {
        if (view == rl_head) {
            boolean isSdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
            if (isSdCardExist) {
                File pictureFile = new File(path);
//                Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                if (!pictureFile.exists()) {
                    try {
                        pictureFile.mkdir();
                        Toast.makeText(this, "mkdir success", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "exception->" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "sd card not exists", Toast.LENGTH_LONG).show();
            }
            pop.showAtLocation(findViewById(R.id.parent), Gravity.BOTTOM
                    | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
            imm.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (view == ll_sex) {
            builder = new AlertDialog.Builder(this);
            builder.setItems(Constant.GENDER_ITEMS, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    dialog.dismiss();
                    tv_sex.setText(Constant.GENDER_ITEMS[index]);
                }
            });
            builder.show();
        }
        if (view == btn_ok) {
            name = edt_name.getText().toString().trim();
            pwd = edt_pwd.getText().toString().trim();
            sex = tv_sex.getText().toString().trim();

            if(name.equals("")) {
                Toast.makeText(this, "注册名不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (pwd.equals("")) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (sex.equals("")) {
                Toast.makeText(this, "性别不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread() {
                @Override
                public void run() {
                    String picName;
                    try {
                        picName = UploadUtil.uploadPic(path + File.separator + IMAGE_FILE_NAME);
                        if (picName == null) {
                            flag = false;
                            return;
                        }
                        flag = NetInfoUtil.register(name, pwd, picName, sex);
                        if (flag) {
                            List<String> list = NetInfoUtil.getUser(name);
                            ResponseUtil.writeUserInfo(RegisterActivity.this, list);
                            String name = list.get(2);
                            BitmapUtils.savePic(name);
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

}
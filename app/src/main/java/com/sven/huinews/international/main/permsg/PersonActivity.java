package com.sven.huinews.international.main.permsg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sven.huinews.international.AppConfig;
import com.sven.huinews.international.R;
import com.sven.huinews.international.base.BaseActivity;
import com.sven.huinews.international.config.Constant;
import com.sven.huinews.international.entity.requst.UserMsgRequest;
import com.sven.huinews.international.entity.response.UserDatasResponse;
import com.sven.huinews.international.main.follow.activity.FollwVideoPlayActivity;
import com.sven.huinews.international.main.permsg.contact.PersonMsgContract;
import com.sven.huinews.international.main.permsg.model.PersonMsgModel;
import com.sven.huinews.international.main.permsg.persenter.PersonMsgPresenter;
import com.sven.huinews.international.utils.Common;
import com.sven.huinews.international.utils.ImageUtils;
import com.sven.huinews.international.utils.StatusBarUtils;
import com.sven.huinews.international.utils.ToastUtils;
import com.sven.huinews.international.utils.cache.UserSpCache;
import com.sven.huinews.international.view.ChooseHeadDIalog;
import com.sven.huinews.international.view.ChooseSexDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * 修改用户信息
 */

public class PersonActivity extends BaseActivity<PersonMsgPresenter, PersonMsgModel> implements PersonMsgContract.View {
    //
    private String etName, mFilePath, mMarkId = "";
    private EditText perNickname, signature;
    private TextView nickNameLenth, genderTv, perAge, btnSend, textView;
    private ImageView img_output;
    private SimpleDraweeView headImage;
    private ChooseHeadDIalog mChooseHeadDIalog;
    private ChooseSexDialog mChooseSexDialog;
    private String userHeadPath;
    private int sexType;
    final int DATE_DIALOG = 1;
    int mYear, mMonth, mDay;
    private final static int TAKE_PHOTO_REQUEST = 1;
    private final static int REQUEST_CODE_CHOOSE_IMAGE = 2;
    public static final int REQUEST_CODE_CROP_IMAGE = 3;
    private UserSpCache mUserSpCache = UserSpCache.getInstance(AppConfig.getAppContext());
    private ProgressDialog progressDialog;

    private Uri mUriSave;//裁剪后图片保存路径
    private Uri iconUri;//选择或拍照后的路径
    private File file;

    public static void toThis(Context mContext, String personid) {
        Intent intent = new Intent(mContext, FollwVideoPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("personid", personid);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        Bundle bundle = getIntent().getExtras();
        etName = bundle.getString("name");
        return R.layout.activity_person;
    }

    @Override
    public void initView() {
        StatusBarUtils.setColor(this, Color.WHITE);

        perNickname = findViewById(R.id.perNickname);
        nickNameLenth = findViewById(R.id.nickNameLenth);
        img_output = findViewById(R.id.img_output);
        headImage = findViewById(R.id.iv_me_head);
        genderTv = findViewById(R.id.genderTv);
        perAge = findViewById(R.id.perAge);
        signature = findViewById(R.id.signature);
        btnSend = findViewById(R.id.btnSend);
        textView = findViewById(R.id.textView);
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + "/" + "temp.png";// 指定路径
        perNickname.setText(etName);
        nickNameLenth.setText(etName.length() + "/12");
        mUriSave = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));

        setSaveStatus(0);

        //日期
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        //获取用户信息
        mChooseHeadDIalog = new ChooseHeadDIalog(this);
        mChooseHeadDIalog.setOnCheckInLisenter(new ChooseHeadDIalog.OnCheckInLisenter() {
            @Override
            public void toPhoto(int type) {
                if (type == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
                    mChooseHeadDIalog.dismiss();
                } else if (type == 2) {
                    //调用系统拍照In
                    Intent photoIn = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photoIn, TAKE_PHOTO_REQUEST);

                    mChooseHeadDIalog.dismiss();
                }
            }
        });

        mChooseSexDialog = new ChooseSexDialog(this);
        mChooseSexDialog.setOnCheckInLisenter(new ChooseSexDialog.OnCheckInLisenter() {
            @Override
            public void toChooseSex(int type) {
                setSaveStatus(1);
                if (type == 1) {  //女
                    sexType = type;
                    genderTv.setText(getString(R.string.female));
                    mChooseSexDialog.dismiss();
                } else if (type == 2) { //男
                    sexType = type;
                    genderTv.setText(getString(R.string.male));
                    mChooseSexDialog.dismiss();
                }
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.updateApk));
        progressDialog.setCancelable(false);
    }

    /**
     * @param type 0.不可点击  1.可点击
     */
    private void setSaveStatus(int type) {
        switch (type) {
            case 0:
                btnSend.setOnClickListener(null);
                btnSend.setClickable(false);
                btnSend.setBackgroundColor(mContext.getResources().getColor(R.color.c999999));
                break;
            case 1:
                btnSend.setOnClickListener(this);
                btnSend.setClickable(true);
                btnSend.setBackgroundColor(mContext.getResources().getColor(R.color.personSave));
                break;
        }
    }

    @Override
    public void initEvents() {
        perNickname.addTextChangedListener(textWatcher);
        signature.addTextChangedListener(signatureWatch);
        img_output.setOnClickListener(this);
        headImage.setOnClickListener(this);
        genderTv.setOnClickListener(this);

        perAge.setOnClickListener(this);
    }

    TextWatcher signatureWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (textView.getText().length() != editable.length()) {
                setSaveStatus(1);
            }
            if (editable.length() < 41) {
                textView.setText(editable.length() + "/80");
            } else {
                ToastUtils.showShort(mContext, getResources().getString(R.string.overlong));
            }
        }
    };
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() != etName.length()) {
                setSaveStatus(1);
            }
            nickNameLenth.setText(editable.length() + "/12");
            etName = editable.toString();

        }
    };

    @Override
    public void onClickEvent(View v) {
        if (v == img_output) {
            PersonActivity.this.finish();
        } else if (v == headImage) {
            mChooseHeadDIalog.show();
        } else if (v == btnSend) {
            checkPersonMsg();
        } else if (v == genderTv) {
            mChooseSexDialog.show();
        } else if (v == perAge) {
            showDialog(DATE_DIALOG);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, R.style.MyDatePickerDialogTheme, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            setSaveStatus(1);

            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            perAge.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
        }
    };


    private void checkPersonMsg() {
        if (TextUtils.isEmpty(signature.getText().toString().trim())) {
            ToastUtils.showLong(mContext, getString(R.string.please_say_something));
        } else if (!btnSend.isClickable()) {
            ToastUtils.showLong(mContext, getString(R.string.updateApk));
        } else {
            mPresenter.upLoadPerMsg();
        }
    }


    //拍照或选择相册后，数据在这里处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iconUri = null;
        if (null == data) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_IMAGE://将图片进行裁剪
                if (data.getData() != null) {
                    iconUri = data.getData();
                    startCropImage(iconUri);
                }
                break;
            case REQUEST_CODE_CROP_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUriSave));
//                        headImage.setImageBitmap(bitmap1);
                        if (bitmap1 != null) {
                            file = ImageUtils.saveBitmapToSdCard(bitmap1);
                        }
                        if (file != null) {
                            userHeadPath = file.getName();
                            uploadFile(file);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case TAKE_PHOTO_REQUEST:
                Bundle bundle = data.getExtras();//获取到图片数据
                if (null != bundle) {
                    Bitmap bm = bundle.getParcelable("data");
//                    iconUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, null, null));
                    file = ImageUtils.saveBitmapToSdCard(bm);
                    if (file != null) {
                        userHeadPath = file.getName();
                    }
                    uploadFile(file);
//                    startCropImage(iconUri);
                }
                break;


        }
    }

    private void setHead(Uri uri) {
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(getResources()).setRoundingParams(RoundingParams.asCircle()).build();
        headImage.setHierarchy(hierarchy);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri).build();
        headImage.setController(controller);
        mChooseHeadDIalog.dismiss();
    }

    private void uploadFile(File file) {
        progressDialog.show();
        mPresenter.uploadeHeadImage(file);
    }

    @Override
    public void initObject() {
        setMVP();
        mPresenter.getUserDetailsDataInfo(false);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorTip(int code, String msg) {
        ToastUtils.showLong(this, msg);
        progressDialog.cancel();
    }

    @Override
    public UserMsgRequest getUserMsgRequest() {
        UserMsgRequest request = new UserMsgRequest();
        request.setHeadImg(userHeadPath);
        request.setMarkId(TextUtils.isEmpty(userHeadPath) ? "" : mMarkId);
        request.setNickname(etName);
        request.setSex(sexType + "");
        request.setSignature(signature.getText().toString());
        request.setBirthday(perAge.getText().toString());
        return request;
    }

    //设置用户信息
    @Override
    public void setUserInfo(UserDatasResponse userInfo) {

        headImage.setImageURI(userInfo.getData().getUserAvatar());
        perNickname.setText(userInfo.getData().getNickname());
        perAge.setText(userInfo.getData().getBirthday());
        genderTv.setText(userInfo.getData().getSex() == 1 ? getString(R.string.female) : getString(R.string.male));
        signature.setText(userInfo.getData().getSignature().equals("") ? getString(R.string.example) : userInfo.getData().getSignature());


    }

    @Override
    public void uploadImageSuccess(String json) {
        progressDialog.cancel();
        setHead(Uri.fromFile(file));
        try {
            JSONObject jsonObject = new JSONObject(json);
            mMarkId = jsonObject.getJSONObject("data").getString("markId");
            userHeadPath = jsonObject.getJSONObject("data").getString("fileName");

            setSaveStatus(1);

            ToastUtils.showShort(mContext, getString(R.string.success));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void uploadPerMsgSuccess() {
        ToastUtils.showShort(mContext, getString(R.string.success));
        mUserSpCache.putString(Constant.NIKE_NAME, etName);

        PersonActivity.this.finish();
    }

    private void startCropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        if (Build.MODEL.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9999);
            intent.putExtra("aspectY", 9998);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        File cropFile = new File(Environment.getExternalStorageDirectory() + "/crop_image.jpg");
        try {
            if (cropFile.exists()) {
                cropFile.delete();
            }
            cropFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mUriSave = Uri.fromFile(cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriSave);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

}
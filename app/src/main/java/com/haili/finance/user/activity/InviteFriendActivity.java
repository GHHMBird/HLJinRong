package com.haili.finance.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.ShareUtils;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.GetInviteCodeRequest;
import com.haili.finance.business.user.GetInviteCodeResponse;
import com.haili.finance.business.user.InviteInfoModel;
import com.haili.finance.business.user.ShareInfoRequest;
import com.haili.finance.business.user.ShareInfoResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.BitmapHelper;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.StringUtils;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by hhm on 2017/2/27.
 */

public class InviteFriendActivity extends BaseActivity implements View.OnClickListener {

    private TextView allPeople;
    private TextView moneyView;
    private TextView truePeople;
    private TextView inviteCode;
    private ImageView ivEr;
    private Subscription subscrip,subscrip2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invert_friend);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("邀请好友");
        initView();
        init();
    }

    private void init() {//网络请求初始化一下就好了
        addLoadingFragment(R.id.invite_friend_fl, "InviteFriendActivity");
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("InviteFriendActivity")) {
            getData();
        }
    }

    private void getData() {
        subscrip=  UserBusinessHelper.getInviteInfo(new GetInviteCodeRequest())
                .subscribe(new Action1<GetInviteCodeResponse>() {
                    @Override
                    public void call(GetInviteCodeResponse getInviteCodeResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (getInviteCodeResponse.dataModel != null) {
                            DataCache.instance.saveCacheData("haili", "GetInviteCodeResponse", getInviteCodeResponse);
                            analyseData(getInviteCodeResponse.dataModel);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (DataCache.instance.getCacheData("haili", "GetInviteCodeResponse") != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }

                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(InviteFriendActivity.this, requestErrorThrowable.getMessage());
                            }
                        } else {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                loadingFragment.showLoadingFailView();
                            }
                        }

                    }
                });
    }

    private void analyseData(InviteInfoModel dataModel) {
        moneyView.setText(dataModel.bonusAmount);
        allPeople.setText(dataModel.alreadyInvitedNum);
        truePeople.setText(dataModel.effectiveNum);
        inviteCode.setText(dataModel.myInvitationCode);
        String imageCode = dataModel.imgUrl;
        if (StringUtils.isEmpty(dataModel.imgUrl)){
            ivEr.setImageDrawable(getResources().getDrawable(R.mipmap.loading_fail));
        }else {
            Bitmap bitmap = BitmapHelper.getImageFromStr(imageCode.substring(imageCode.indexOf(","), imageCode.length()));
            Matrix matrix = new Matrix();
            matrix.postScale(2.5f, 5f); //长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ivEr.setBackground(new BitmapDrawable(resizeBmp));
        }
    }

//    public Bitmap stringtoBitmap(String string){
//        //将字符串转换成Bitmap类型
//        Bitmap bitmap=null;
//        try {
//            byte[]bitmapArray;
//            bitmapArray= Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAGQAAABkAQAAAABYmaj5AAAA7UlEQVR42tXUMQ6DMAwFUKMM2doLRMo1unElcgEgF2iulC3XqJQL4I0hqutUSLCAM7YRA28wcfItgA5rgR/WAjAlChE6SUhl7CkkfpEUjVtLl8A1aOwttQruLaIyRdPtnZ2Kz+eScftpT8UL1xz2GzzVorlCzbpMkmjNz4fCrZcrcR03MkDpJGGyM2/CoUlaoNxj9vxIosgjwPtklITECeT3Y+vlQu8+4/q66RwkYTKg1QyKJH1zsJ4OiZ2Isx3A+mS9JJ4XV8dKyYqGQ6ilDRprR2Zq0FAvQ6Eo4s/TrFWQVOearI/kJf3LH+yoD1mgwGoLIJRrAAAAAElFTkSuQmCC", Base64.DEFAULT);
//            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }

    private void initView() {
        findViewById(R.id.invite_friend_my_money).setOnClickListener(this);
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menu_layout);
        menuLayout.setOnClickListener(this);
        ivEr = (ImageView) findViewById(R.id.invite_friend_picture);
        Button inviteNow = (Button) findViewById(R.id.invite_friend_btn);
        inviteNow.setOnClickListener(this);
        moneyView = (TextView) findViewById(R.id.invite_friend_money);
        allPeople = (TextView) findViewById(R.id.invite_friend_all_num);
        truePeople = (TextView) findViewById(R.id.invite_friend_true_num);
        inviteCode = (TextView) findViewById(R.id.invite_friend_code);
        initCacheData();
    }

    private void initCacheData() {
        GetInviteCodeResponse cacheData = DataCache.instance.getCacheData("haili", "GetInviteCodeResponse");
        if (cacheData == null) {
            return;
        }
        analyseData(cacheData.dataModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_layout://我的邀请
                Intent intent = new Intent(this, MyInviteActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.invite_friend_btn://立即邀请(开始分享)
                goShare();
                break;
            case R.id.invite_friend_my_money://我的奖励金额明细
                Intent intent2 = new Intent(this, MyInviteActivity.class);
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;
        }
    }

    private void goShare() {//分享界面
        ShareInfoRequest request = new ShareInfoRequest();
        request.type = 1;
        subscrip2=  UserBusinessHelper.shareInfo(request)
                .subscribe(new Action1<ShareInfoResponse>() {
                    @Override
                    public void call(ShareInfoResponse shareInfoResponse) {
                        if (shareInfoResponse.dataModel != null) {
                            String imgUrl = shareInfoResponse.dataModel.imgUrl;
                            String title = shareInfoResponse.dataModel.title;
                            String url = shareInfoResponse.dataModel.url;
                            String body = shareInfoResponse.dataModel.describe;
                            UMImage umImage = new UMImage(InviteFriendActivity.this, imgUrl);
                            umImage.setThumb(umImage);
                            ShareUtils.share(InviteFriendActivity.this, title, umImage, url, body);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            ViewHelper.showToast(InviteFriendActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }   if (subscrip2!=null) {
            subscrip2.unsubscribe();
        }
    }
}

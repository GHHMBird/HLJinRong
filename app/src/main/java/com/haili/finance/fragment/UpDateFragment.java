package com.haili.finance.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.mcxiaoke.bus.Bus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/*
 * Created by Monkey on 2017/3/16.
 */

public class UpDateFragment extends Fragment implements View.OnClickListener {

    private NotificationManager notifyManager;
    private Button btn;
    private int fileLength;
    private RelativeLayout rl_update_out, rl_update_in;
    private ProgressBar pbar;
    private static final String KEY_EVENT = "KEY";
    private TextView textRate;
    private static String string, urlStr;
    public static final String TAG = "UpDateFragment";
    private boolean isShow;
    private static int type;
    private boolean isStartDownLoad = false;
    /*
     * 新建一个UpDateFragment对象
     *
     * @param event RxBus广播的事件，字符串类型，不能为空，保证唯一。若为NULL或“”则不会发送事件广播
     * @return UpDateFragment 对象
     */
    public static UpDateFragment newInstance(@NonNull String event, String text, String url, int num) {
        string = text;
        urlStr = url;
        type = num;
        Bundle args = new Bundle();
        args.putString(KEY_EVENT, event);
        UpDateFragment fragment = new UpDateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textRate.setText(msg.what + "%");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        btn = (Button) view.findViewById(R.id.update_btn);
        ImageView closeImage = (ImageView) view.findViewById(R.id.update_close);
        rl_update_out = (RelativeLayout) view.findViewById(R.id.rl_update_out);
        rl_update_in = (RelativeLayout) view.findViewById(R.id.rl_update_in);
        TextView text = (TextView) view.findViewById(R.id.update_text);
        textRate = (TextView) view.findViewById(R.id.update_rate);
        text.setText(string);
        pbar = (ProgressBar) view.findViewById(R.id.update_pbar);
        closeImage.setOnClickListener(this);
        btn.setOnClickListener(this);
        rl_update_out.setOnClickListener(this);
        rl_update_in.setOnClickListener(this);
        return view;
    }

    private void showNotification() {
        notifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(100, 100, true)//todo true则不显示具体下载进度，false则显示当前进度
                .setAutoCancel(false)
                .setTicker("海狸金融开始下载")
                .setContentTitle("海狸金融")
                .setContentText("正在下载新版本");
        Notification build = builder.build();
        build.defaults |= Notification.DEFAULT_VIBRATE;
        build.flags = Notification.FLAG_NO_CLEAR;
        notifyManager.notify(12315, build);
        isShow = true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String event = getArguments().getString(KEY_EVENT, "");
        if (!TextUtils.isEmpty(event)) {
            Bus.getDefault().post(event);
        }
    }

    public void removeSelf(final FragmentManager fragmentManager) {
        if (isShow) {
            notifyManager.cancel(12315);
        }
        if (fragmentManager == null) {
            return;
        }
        getFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0)
                .remove(UpDateFragment.this).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn:
                btn.setVisibility(View.GONE);
                pbar.setVisibility(View.VISIBLE);
                textRate.setVisibility(View.VISIBLE);
                isStartDownLoad = true;
                downLoad();
                break;
            case R.id.rl_update_in:

                break;
            case R.id.update_close:
            case R.id.rl_update_out:
                if (type == 0) {
                    if (!isStartDownLoad){
                        removeSelf(getFragmentManager());
                    }
                }
                break;
        }
    }

    private void downLoad() {
        showNotification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "file";
                String fileName = "haili.apk";
                OutputStream output = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    fileLength = conn.getContentLength();
                    if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                        trustAllHosts();
                        HttpsURLConnection https = (HttpsURLConnection) url
                                .openConnection();
                        https.setHostnameVerifier(DO_NOT_VERIFY);
                        conn = https;
                    } else {
                        conn = (HttpURLConnection) url.openConnection();
                    }

                    //开始写入
                    String SDCard = Environment.getExternalStorageDirectory() + "";
                    String pathName = SDCard + "/" + path + "/" + fileName;//文件存储路径

                    File file = new File(pathName);
                    InputStream input = conn.getInputStream();
                    if (file.exists()) {
                        file.delete();
                    }
                    String dir = SDCard + "/" + path;
                    new File(dir).mkdir();//新建文件夹
                    file.createNewFile();//新建文件
                    output = new FileOutputStream(file);
                    int i;
                    int nowSize = 0;
                    //读取大文件
                    byte[] buffer = new byte[10 * 1024];
                    pbar.setMax(fileLength);
                    while ((i = input.read(buffer)) != -1) {
                        nowSize += i;
                        pbar.setProgress(nowSize);
                        int a = (nowSize*100 / fileLength);
                        handler.sendEmptyMessage(a);
                        output.write(buffer, 0, i);
                    }
                    output.flush();
                    install(file);
                    isStartDownLoad = false;
                    removeSelf(getFragmentManager());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                        }

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void install(File file) {

        //调用系统安装程序
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public static void trustAllHosts() {//屏蔽https证书验证，防止ssl报错
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}

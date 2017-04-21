package com.haili.finance.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.haili.finance.R;

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

public class DownloadService extends Service {

    String urlStr;
    private boolean isStartDown = false;
    private NotificationManager notifyManager;
    private long fileLength;

    public DownloadService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        urlStr = intent.getStringExtra("url");
        if (!isStartDown) {
            isStartDown = true;
            startDown();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDown() {

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

                    if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                        trustAllHosts();
                        HttpsURLConnection https = (HttpsURLConnection) url
                                .openConnection();
                        https.setHostnameVerifier(DO_NOT_VERIFY);
                        conn = https;
                    } else {
                        conn = (HttpURLConnection) url.openConnection();
                    }

                    fileLength = getFileLength(urlStr);//获取文件总大小

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
                    //读取大文件
                    byte[] buffer = new byte[10 * 1024];
                    while ((i = input.read(buffer)) != -1) {
                        output.write(buffer, 0, i);
                    }
                    output.flush();
                    notifyManager.cancel(1);

                    install(file);

                    stopSelf();

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

//    private void setProgress(long l) {
//        builder.setProgress(100, (int) l, false);
//        notifyManager.notify(1, builder.build());
//    }

    private int getFileLength(String url1) throws IOException {
        int length = 0;
        URL url;
        try {
            url = new URL(url1);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            //根据响应获取文件大小
            length = urlcon.getContentLength();
            urlcon.disconnect();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return length;
    }

    private void showNotification() {

        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(100, 100, true)//todo true则不显示具体下载进度，false则显示当前进度
                .setAutoCancel(false)
                .setTicker("海狸金融开始下载")
                .setContentTitle("海狸金融")
                .setContentText("正在下载新版本");
        Notification build = builder.build();
        build.defaults |= Notification.DEFAULT_VIBRATE;
        build.flags = Notification.FLAG_NO_CLEAR;
        notifyManager.notify(1, build);
    }

    private void install(File file) {
        notifyManager.cancel(1);
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
package com.haili.finance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.helper.URLHelper;
import com.mcxiaoke.bus.Bus;

/**
 * Created by lfu on 2017/3/28.
 */

public class UrlSettingActivity extends BaseActivity implements View.OnClickListener {

    TextView present_url,test_url_1,test_url_2,test_url_3,test_url_4,test_url_5;
    Button btn_edit;
    EditText edit_url;
    RelativeLayout ok;
    String URL = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_setting);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("网络环境配置");
        ok = (RelativeLayout)findViewById(R.id.ok);
        edit_url = (EditText)findViewById(R.id.edit_url);
        present_url = (TextView)findViewById(R.id.present_url);
        test_url_1 = (TextView)findViewById(R.id.test_url_1);
        test_url_2 = (TextView)findViewById(R.id.test_url_2);
        test_url_3 = (TextView)findViewById(R.id.test_url_3);
        test_url_4 = (TextView)findViewById(R.id.test_url_4);
        test_url_5 = (TextView)findViewById(R.id.test_url_5);
        btn_edit = (Button)findViewById(R.id.btn_edit);
        ok.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        test_url_1.setOnClickListener(this);
        test_url_2.setOnClickListener(this);
        test_url_3.setOnClickListener(this);
        test_url_4.setOnClickListener(this);
        String yourString = URLHelper.getInstance().URL;
        String Seperator="/";
        String[] Resources=yourString.split(Seperator);
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<Resources.length;i++){
            if(i<=2){
                stringBuffer.append(Resources[i]);
            }
        }
        present_url.setText("当前：" + stringBuffer);
        test_url_1.setText("振杰测试：http://192.168.4.199:8080");
        test_url_2.setText("金城测试：http://192.168.4.210:8080");
        test_url_3.setText("测试环境：http://192.168.80.50:8080");
        test_url_4.setText("小柯测试：http://192.168.4.212:8080");
        test_url_5.setText("暂无配置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_url_1:
                URL = "http://192.168.4.199:8080/hl-004-gateway-web/app/";
                present_url.setText("当前：http://192.168.4.199:8080");
                upData(0);
                break;
            case R.id.test_url_2:
                URL = "http://192.168.4.210:8080/hl-004-gateway-web/app/";
                present_url.setText("当前：http://192.168.4.210:8080");
                upData(1);
                break;
            case R.id.test_url_3:
                URL = "http://192.168.80.50:8080/gateway-web/app/";
                present_url.setText("当前：http://192.168.80.50:8080");
                upData(2);
                break;
            case R.id.test_url_4:
                URL = "http://192.168.100.212:8002/gateway-web/app/";
                present_url.setText("当前：http://192.168.100.212:8002");
                upData(3);
                break;
//            case R.id.test_url_5:
//                URLHelper.getInstance().URL = "http://192.168.100.204:8002/gateway-web/app/";
//                break;
            case R.id.btn_edit:
                URL = edit_url.getText().toString();
                present_url.setText(URL);
                upData(-1);
                break;
            case R.id.ok:
                URLHelper.getInstance().URL = URL;
                Bus.getDefault().post("reload_some_interface");
                finish();
                break;
        }
    }

    private void upData(int i){
        if (i == -1){
            test_url_1.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_2.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_3.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_4.setBackgroundColor(getResources().getColor(R.color.white));
            return;
        }
        if(i == 0){
            test_url_1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            test_url_2.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_3.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_4.setBackgroundColor(getResources().getColor(R.color.white));
            return;
        }
        if (i == 1){
            test_url_2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            test_url_1.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_3.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_4.setBackgroundColor(getResources().getColor(R.color.white));
            return;
        }
        if (i == 2){
            test_url_3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            test_url_1.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_2.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_4.setBackgroundColor(getResources().getColor(R.color.white));
            return;
        }
        if (i == 3){
            test_url_4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            test_url_3.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_1.setBackgroundColor(getResources().getColor(R.color.white));
            test_url_2.setBackgroundColor(getResources().getColor(R.color.white));
            return;
        }
    }
}

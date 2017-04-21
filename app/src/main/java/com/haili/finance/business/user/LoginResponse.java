package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/6.
 */

public class LoginResponse extends ResponseData implements Serializable{

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("data")
    public  LoginModel loginModel;

}

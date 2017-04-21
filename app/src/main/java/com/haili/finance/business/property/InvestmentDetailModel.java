package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 *
 * Created by Monkey on 2017/3/18.
 */

public class InvestmentDetailModel implements Serializable {
    @Expose
    @SerializedName("productId")
    public String productId;//产品ID
    @Expose
    @SerializedName("productName")
    public String productName;//产品名称
    @Expose
    @SerializedName("manageMoneyId")
    public String manageMoneyId;//理财ID
    @Expose
    @SerializedName("investDeadline")
    public String investDeadline;//项目期限
    @Expose
    @SerializedName("interestDate")
    public String interestDate;//起息日期
    @Expose
    @SerializedName("investBalance")
    public String investBanalce;//投资金额
    @Expose
    @SerializedName("yieldRateBalance")
    public String yieldRateBanalce;//预期收益
    @Expose
    @SerializedName("repaymentTime")
    public String repaymentTime;//预计还款日期
    @Expose
    @SerializedName("productState")
    public int productState;//还款状态
    @Expose
    @SerializedName("yieldRate")
    public double yieldRate;//年化收益率
    @Expose
    @SerializedName("repaymentStyle")
    public String repaymentStyle;//还款方式
    @Expose
    @SerializedName("expiryDate")
    public String expiryDate;//到期日期
    @Expose
    @SerializedName("productPact")
    public String productPact;//项目合同
}

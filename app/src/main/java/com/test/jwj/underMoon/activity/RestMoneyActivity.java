package com.test.jwj.underMoon.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.test.jwj.underMoon.CustomView.ItemLayout;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.alipay.AuthResult;
import com.test.jwj.underMoon.alipay.PayResult;
import com.test.jwj.underMoon.global.UserAction;

import java.util.Map;

/**
 * Created by jiangweijin on 2018/8/16.
 */

public class RestMoneyActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ItemLayout.LayoutClickListener {
    private RadioGroup RG_first;
    private RadioGroup RG_second;
    private TextView mRest_pay;
    private EditText mEt_input;
    private ItemLayout alipay;
    private ItemLayout wechat;
    private int type;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private static final int INFO = 3;
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018081361016269";
    /* 商户私钥，pkcs8格式 */
    /* 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /* 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /* RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /* 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCWEtrIbT9Xy6LORoFKWkyhH/JBGavRPwf+SDlOHImAHybgWg1rBgoXmZufLvFlW7DqPGwxacxEpd7hOZhShYRahVhZRhgjsbvIROV5iuh42S2/WmFZ5YDN3niKhEttyxwoWYhgVI6Oh1kqGgtotgAVeqteqPPBCb5jCzhlVoq5TOS6KEqA4mnSh8i9nDdOOgnOmXJnT7ZoK76n4P74ADvStXohqKABq9trqdvrBWb78x3/jB6N/m3JnsfyPYydBnVKYXWK0oGBNVErdJUp8JhkvQj6H9f8KgkN8XKH9UMVzcN9/3qj7sSvoVxScS1UJGztYgoWRo7afglZYE/OAYFnAgMBAAECggEAJuKJx8bu07vS2mnQVEijdFhHt/CD4XrYgl2KY5/nTMhFlXof4ew1rznA5fUO3Tlt1LOFQSRtphfocSkwO6mWyrGkW7VaydzikBix8NU6OR3kyaEMenOJ9U3Ao/t1Y+RtzlKskHE4YtvVEaCf9ii6StZ3EtbqGcmBiD4/BZrv5OUNfQd5EitRjbK1r41vK/65lY1v0ZyDeStu5rRSQN5Qn9zKKIjjaPXviGP1Yo2pCgAyFUIa/eNoaw7wqomp2Up1e6AtmvWgH/t1DnqatfyLyLGjNaRML8oPhMEugSjmIC/chw92/pWY6TAV2Vfr8iy0R6RE59ivOuGhi0kHRK4kUQKBgQDpQ9ImgU4M1Vvw8SDcRuGk4KeHte35V9RbmIiyUF5mHXPf5tG7tGkW65sWn6hquKMHcYVAUFUBwIpsWWstHfRz9JZGxDz68+1ijnybCjXGXoJBVOfT1e8zky7/nl1UGYMrWkvYr3P0nNjDqEX7hDoUh3bRlz7jXvtys2Nis6PlXwKBgQCks1VPWKfj8jUQQNGq/6iDQe5mBrhcsJjf5kaCwi/a86IWIcSTYLu+DtjZL8JM0T8sBAVyfsCmJSj8SAa5mrboBZTFaOZ+6hfXEOAjuVEc/fY3l9bLuEOeTiuVLKEBOczh76Fj5x0gBd0g/NEohiulcdXdg0W8ORa7DrHqlvmY+QKBgQCNCk7krUZOCCuhUYq25bzFfniNW/lZzDtAbsgoWOPbBm/rr5qczgbErwyE72BbtuwMMh2Jt4jOmGaaAK8HBpeqDPdYLotYiWi9ML4y2EePe9FyQy4xLaeGHbZLJKv1j7951Q0LJXsNKlD+bJ5z541eoFG9hJ+nxuRug/zRzyCILQKBgQCHSOciLeh6TFFZ8GRI2YdJibaRB6QYPtbT0wrIDUnRx520IDif9i1AiGGGxLwM7TO+q+7thUApOQzZbTBY9MSZATyaivgJ969tcOcrcOU3s0Ozln1RCSJBvmP+PJJjt16bl4Ix1X0O+MISfpgveUYQt9i8A0Acw6fwLrnlv+11wQKBgQCRj261ACARPL0TOBZJxjR+iFXEDsoZLUhiwNnK3Fr/EaCDKEaN92F4NbTXYY/Xf/vD5v4+Jyupe8EL4Ka2Y3fksAWkk++YR9ouQxniTzclhM5Yf5DYe8Nuy5M1RqxDmdecGYru+/nX6HhhKdtimuBoI+Hjjq2vEt/kUHLJLyNtFw==";
    public static final String RSA_PRIVATE = "";
    private String orderInfo;
    private String amount;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /*
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RestMoneyActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RestMoneyActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(RestMoneyActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(RestMoneyActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case INFO:
                    orderInfo = (String) msg.obj;
                    EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        setContentView(R.layout.activity_rest_money);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.header_title)).setText("账户余额");
        findViewById(R.id.header_option).setVisibility(View.GONE);
        RG_first = ((RadioGroup) findViewById(R.id.rg_first));
        RG_second = ((RadioGroup) findViewById(R.id.rg_second));
        mRest_pay = (TextView) findViewById(R.id.rest_pay);
        mEt_input = (EditText) findViewById(R.id.et_input_money);
        alipay = ((ItemLayout) findViewById(R.id.rest_alipay));
        wechat = ((ItemLayout) findViewById(R.id.rest_wechat));
        mEt_input.clearFocus();
    }

    @Override
    protected void initEvents() {
        findViewById(R.id.header_back).setOnClickListener(this);
        findViewById(R.id.rest_pay).setOnClickListener(this);
        RG_first.setOnCheckedChangeListener(this);
        RG_second.setOnCheckedChangeListener(this);
        alipay.setLayoutClickListener(this);
        wechat.setLayoutClickListener(this);
        mEt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                amount = mEt_input.getText().toString().trim();
                mRest_pay.setText(String.format("%s 元",amount));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.rest_pay:
                switch (type){
                    case 1:
                        alipay();
                        break;
                    case 2:
                        //TODO wechat

                        break;
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup != null && i>0){
            if(radioGroup == RG_first){
                RG_second.clearCheck();
            }else if(radioGroup == RG_second){
                RG_first.clearCheck();
            }
            radioGroup.check(i);
            for (int index = 0;index < radioGroup.getChildCount();index++){
                if (((RadioButton)radioGroup.getChildAt(index)).isChecked()) {
                    mEt_input.setText("");
                    mEt_input.clearFocus();
                    amount = ((RadioButton) radioGroup.getChildAt(index)).getText().toString().split("元")[0];
                    mRest_pay.setText(String.format("支付 %s 元",amount));
                }
            }
        }
    }

    @Override
    public void onLayoutClick(View view) {
        switch (view.getId()){
            case R.id.rest_alipay:
                type = 1;
                alipay.getBackImg().setVisibility(View.VISIBLE);
                alipay.setBackImg(R.mipmap.selected_style);
                wechat.getBackImg().setVisibility(View.GONE);
                break;
            case R.id.rest_wechat:
                type = 2;
                wechat.getBackImg().setVisibility(View.VISIBLE);
                wechat.setBackImg(R.mipmap.selected_style);
                alipay.getBackImg().setVisibility(View.GONE);
                break;
        }
    }

    public void alipay() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;

        new orderStringThread().start();

//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
    }

    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {
            PayTask alipay = new PayTask(RestMoneyActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.e("tag", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };

    class orderStringThread extends Thread{
        @Override
        public void run() {
            Message msg1=mHandler.obtainMessage();
            msg1.what=INFO;
            msg1.obj= UserAction.getOrderString(amount);
            mHandler.sendMessage(msg1);
        }
    }
}

package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maisi.video.obj.video.UserInfoEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class BindZhiFuActivity extends BaseActivity {


    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_bind)
    ImageView ivBind;
    @BindView(R.id.iv_change)
    ImageView ivChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_bind_zhifu);
        ButterKnife.bind(this);
        if(VipHelperUtils.getInstance().isWechatLogin()) {
            etAccount.setText(VipHelperUtils.getInstance().getVipUserInfo().getZfbno());
            etName.setText(VipHelperUtils.getInstance().getVipUserInfo().getZfbname());
        }

/*        Service.getComnonService().getWechatTips()
                .compose(BoyiRxUtils.<WechatTipsEntity>applySchedulers())
                .subscribe(new Subscriber<WechatTipsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final WechatTipsEntity result) {

                        mTvWechatTips.setText(result.getRemarkValue());
                    }
                });*/

/*        Service.getComnonService().getMaiSiBiTips()
                .compose(BoyiRxUtils.<WechatTipsEntity>applySchedulers())
                .subscribe(new Subscriber<WechatTipsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final WechatTipsEntity result) {

                        mTvMaisibiTips.setText(result.getRemarkValue());
                    }
                });*/

/*        Service.getComnonService().getMaisibiRate()
                .compose(BoyiRxUtils.<MsbRateEntity>applySchedulers())
                .subscribe(new Subscriber<MsbRateEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final MsbRateEntity result) {

                        mTvMsbRate.setText(result.getValue2());
                    }
                });*/
/*
        if (!VipHelperUtils.getInstance().isWechatLogin())
            return;*/





    }

    @OnClick({R.id.iv_bind, R.id.iv_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bind:
                go2Bind(false);
                break;
            case R.id.iv_change:
                go2Bind(true);
                break;
        }
    }

    /*private void go2Transfer() {
        if (!VipHelperUtils.getInstance().isWechatLogin()) {
            Toast.makeText(
                    APPAplication.instance,
                    "请先登录~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (BindZhiFuActivity.this.result == null) {
            Toast.makeText(
                    APPAplication.instance,
                    "请稍候再试~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog dialog =
                new MaterialDialog.Builder(this)
                        .title("输入对方推荐码和转账数量")
                        .customView(R.layout.dialog_input_transfer, true)
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String amount = amountInput.getText().toString();
                                String account = transferAccount.getText().toString();
                                if (amount.length() == 0 || account.length() == 0) {
                                    Toast.makeText(
                                            APPAplication.instance,
                                            "请确定推荐码和数量填写完整~~",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Service.getComnonServiceForString().giveMSB2Other(VipHelperUtils.getInstance().getVipUserInfo().getUid(), Double.parseDouble(amount), account)
                                        .compose(BoyiRxUtils.<String>applySchedulers())
                                        .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                            @Override
                                            public void onNext(String result) {
                                                if (result.equals("success")) {

                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "转账成功！",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "转账失败！",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                                UserModel.getInstance().refreshUserInfo();
                                            }
                                        });

                            }
                        })
                        .build();
        dialog.show();
        amountInput = dialog.getCustomView().findViewById(R.id.et_amount);
        transferAccount = dialog.getCustomView().findViewById(R.id.et_recommend_num);


    }*/

    private void go2Bind(final boolean isChange) {
        if (!VipHelperUtils.getInstance().isWechatLogin()) {
            Toast.makeText(
                    APPAplication.instance,
                    "请先登录~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String payAccountS = etAccount.getText().toString();
        String payNameS = etName.getText().toString();
        if (payAccountS.length() == 0 || payNameS.length() == 0) {
            Toast.makeText(
                    APPAplication.instance,
                    "请确定账号和实名填写完整~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Service.getComnonService().bindZhiFu(VipHelperUtils.getInstance().getVipUserInfo().getUid(),payAccountS, payNameS)
                .compose(BoyiRxUtils.<UserInfoEntity>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<UserInfoEntity>() {

                    @Override
                    public void onNext(UserInfoEntity result) {
                        if( result != null) {

                            UserModel.getInstance().refreshUserInfo(result);
                            if(isChange) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        "修改成功",
                                        Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(
                                        APPAplication.instance,
                                        "绑定成功",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
/*                        if (result.equals("success")) {

                            Toast.makeText(
                                    APPAplication.instance,
                                    "提现成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "提现失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                        UserModel.getInstance().refreshUserInfo();*/
                    }
                });



       /* CashRequestEntity entity = new CashRequestEntity();
        entity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());
//        entity.setAmount(BindZhiFuActivity.this.result.getCommend2cash());
        entity.setMaisibi(VipHelperUtils.getInstance().getVipUserInfo().getCommendLeft());
        entity.setPayeeAccount(payAccountS);
        entity.setPyeeRealName(payNameS);
        Gson gson = new Gson();
        String toJson = gson.toJson(entity);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
        Service.getComnonServiceForString().alitransfer(body)
                .compose(BoyiRxUtils.<String>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                    @Override
                    public void onNext(String result) {
                        if (result.equals("success")) {

                            Toast.makeText(
                                    APPAplication.instance,
                                    "提现成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "提现失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                        UserModel.getInstance().refreshUserInfo();
                    }
                });

        MaterialDialog dialog =
                new MaterialDialog.Builder(this)
                        .title("输入支付宝账号")
                        .customView(R.layout.dialog_input_transfer2, true)
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String payAccountS = payAccount.getText().toString();
                                String payNameS = payName.getText().toString();
                                if (payAccountS.length() == 0 || payNameS.length() == 0) {
                                    Toast.makeText(
                                            APPAplication.instance,
                                            "请确定账号和实名填写完整~~",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                CashRequestEntity entity = new CashRequestEntity();
                                entity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());
                                entity.setAmount(BindZhiFuActivity.this.result.getCommend2cash());
                                entity.setMaisibi(VipHelperUtils.getInstance().getVipUserInfo().getCommendLeft());
                                entity.setPayeeAccount(payAccountS);
                                entity.setPyeeRealName(payNameS);
                                Gson gson = new Gson();
                                String toJson = gson.toJson(entity);
                                RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
                                Service.getComnonServiceForString().alitransfer(body)
                                        .compose(BoyiRxUtils.<String>applySchedulers())
                                        .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                            @Override
                                            public void onNext(String result) {
                                                if (result.equals("success")) {

                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "提现成功",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "提现失败",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                                UserModel.getInstance().refreshUserInfo();
                                            }
                                        });
                            }
                        })
                        .build();
        dialog.show();
        payAccount = dialog.getCustomView().findViewById(R.id.et_pay_account);
        payName = dialog.getCustomView().findViewById(R.id.et_pay_name);*/
    }


}

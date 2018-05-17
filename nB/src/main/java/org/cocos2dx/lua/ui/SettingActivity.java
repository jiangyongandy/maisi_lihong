package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.maisi.video.obj.video.UpdateEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.DownLoadUtil;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;
import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class SettingActivity extends BaseActivity {


    @BindView(R.id.rl_clean)
    RelativeLayout rlClean;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.rl_exit)
    RelativeLayout rlExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.rl_clean, R.id.rl_update, R.id.rl_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_clean:
                clearCache();
                break;
            case R.id.rl_update:
                reqUpdateVersion();
                break;
            case R.id.rl_exit:
                exitLogin();
                break;
        }
    }

    private void exitLogin() {
        MaterialDialog dialog = new MaterialDialog.Builder(SettingActivity.this)
                .title("退出登录？")
                .content("确认退出登录吗？")
                .positiveText("确定")
                .negativeText("")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        UserModel.getInstance().exitLogin();
                        EventBus.getDefault().post(new Object(), EventBusTag.TAG_LOGIN_EXIT);
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void clearCache() {
        MaterialDialog dialog = new MaterialDialog.Builder(SettingActivity.this)
                .title("清理缓存")
                .content("缓存已经清理干净！")
                .positiveText("确定")
                .negativeText("")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void reqUpdateVersion() {
        //版本更新
        Service.getComnonService().versionUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<UpdateEntity>() {
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
                    public void onNext(final UpdateEntity result) {
                        if (Integer.parseInt(result.getValue1()) > AppUtils.getAppVersionCode()) {

                            MaterialDialog dialog = new MaterialDialog.Builder(SettingActivity.this)
                                    .title("有新版本")
                                    .content("有最新版本是否立即下载？（强烈推荐下载获取更稳定体验！！）")
                                    .positiveText("马上下载")
                                    .negativeText("取消")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            if (result.getValue2() != null) {
                                                DownLoadUtil downLoadUtil = new DownLoadUtil(SettingActivity.this);
                                                downLoadUtil.downloadAPK(result.getValue2(), "迈思最新版");
                                            }
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                        }else {
                            ToastUtil.show(APPAplication.instance,
                                    "已是最新版本了哦~~",
                                    Toast.LENGTH_SHORT);
                        }

                    }
                });
    }
}

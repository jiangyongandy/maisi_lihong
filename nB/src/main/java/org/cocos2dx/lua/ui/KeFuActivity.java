package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zuiai.nn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class KeFuActivity extends BaseActivity {


    @BindView(R.id.rl_message_system)
    RelativeLayout rlMessageSystem;
    @BindView(R.id.rl_message_kefu)
    RelativeLayout rlMessageKefu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_kefu);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.rl_message_system, R.id.rl_message_kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_message_system:
                Intent intent1 = new Intent(this, SystemMessageActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_message_kefu:
                Intent intent = new Intent(this, BrowserActivity.class);
                Uri content_url = Uri.parse("http://im.molinsoft.com/im.htm?pid=402840886318c26a016318c400ec0002");
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
    }
}

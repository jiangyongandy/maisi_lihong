package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zuiai.nn.R;

import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class RewardListActivity extends BaseActivity {


    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_reward_list);
        ButterKnife.bind(this);

        // Create an instance of SectionedRecyclerViewAdapter
        final SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

// Set up your RecyclerView with the SectionedRecyclerViewAdapter
        RecyclerView recyclerView = rvList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sectionAdapter);

        Service.getComnonService().queryIncomeDetail(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .compose(BoyiRxUtils.<ArrayList<String>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<String>>() {

                    @Override
                    public void onNext(ArrayList<String> result) {
                        if(result == null || result.size() == 0) {
                            ToastUtil.show(RewardListActivity.this, "暂无收益数据哦~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        for (String s : result) {
                            // Add your Sections
                            sectionAdapter.addSection(new MySection(s));
                        }
                        sectionAdapter.notifyDataSetChanged();
                    }
                });
    }


    class MySection extends StatelessSection {
        List<String> itemList = new ArrayList<>();
        String month = "";

        public MySection(String data) {
            // call constructor with layout resources for this Section header and items
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.list_section_item_reward)
                    .headerResourceId(R.layout.list_section_head_reward)
                    .footerResourceId(R.layout.list_section_footer_reward)
                    .build());
            itemList.clear();
            String[] strings = data.split("\\|");
            month = strings[0];
            itemList.addAll(Arrays.asList(strings[1].split(",")));
        }

        @Override
        public int getContentItemsTotal() {
            return itemList.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new MyHeadViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom instance of ViewHolder for the items of this section
            return new MyItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

            // bind your view here
            switch (position) {
                case 0:
                    itemHolder.tv_type.setText("直推奖金");
                    break;
                case 1:
                    itemHolder.tv_type.setText("分销奖金");
                    break;
                case 2:
                    itemHolder.tv_type.setText("月度分红");
                    break;
            }
            itemHolder.tv_num.setText("¥" + itemList.get(position));
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            super.onBindHeaderViewHolder(holder);
            MyHeadViewHolder itemHolder = (MyHeadViewHolder) holder;
            itemHolder.tv_month.setText(month);
        }
    }

    class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_type;
        private final TextView tv_num;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }

    class MyHeadViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_month;

        public MyHeadViewHolder(View itemView) {
            super(itemView);

            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
        }
    }


}

package org.cocos2dx.lua.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maisi.video.obj.video.PersonEntity;
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
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class MyTeamActivity extends BaseActivity {


    @BindView(R.id.et_team_name)
    EditText etTeamName;
    @BindView(R.id.iv_btn_search)
    ImageView ivBtnSearch;
    @BindView(R.id.lv_team_list)
    ListView lvTeamList;
    @BindView(R.id.lv_member_list)
    RecyclerView lvMemberList;
    @BindView(R.id.tv_my_team)
    TextView tvMyTeam;
    private int currentIndex;
    private int selIndex;
    private ArrayList<PersonEntity> teamList = new ArrayList<>();
    private ArrayList<String> memberList = new ArrayList<>();
    private ListTeamAdapter listTeamAdapter;
    private SectionedRecyclerViewAdapter sectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_my_team);
        ButterKnife.bind(this);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        RecyclerView recyclerView = lvMemberList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sectionAdapter);

        //test
/*        for (int i = 0; i < 10; i++) {
            ChargeInfoEntity entity = new ChargeInfoEntity();
            teamList.add(entity);
            if(i == 0) {
                entity.setSel(true);
            }
        }*/
        refreshTeamMemberData();


    }

    private void refreshTeamMemberData() {
        Service.getComnonService().queryTeamWithUid(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .compose(BoyiRxUtils.<ArrayList<String[]>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<String[]>>() {

                    @Override
                    public void onNext(ArrayList<String[]> result) {
                        if (result == null || result.size() == 0) {
                            ToastUtil.show(MyTeamActivity.this, "暂无团队成员哦~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        teamList.clear();
                        int i = 0;
                        for (String[] s : result) {
                            PersonEntity entity = new PersonEntity();
                            entity.setUid(s[0]);
                            entity.setName(s[1]);
                            if (i == 0) {
                                entity.setSel(true);
                                selIndex = i;
                            }
                            teamList.add(entity);
                            i++;
                        }
                        listTeamAdapter = new ListTeamAdapter(MyTeamActivity.this, teamList);
                        lvTeamList.setAdapter(listTeamAdapter);
                    }
                });
    }

    private void refreshFieldCommendData(String uid) {
        //test
/*            memberList.clear();
            for (int i = 0; i < 10; i++) {
                ChargeInfoEntity entity = new ChargeInfoEntity();
                memberList.add(entity);
            }*/
        Service.getComnonService().queryCommenNumWithUid(uid)
                .compose(BoyiRxUtils.<ArrayList<String>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<String>>() {

                    @Override
                    public void onNext(ArrayList<String> result) {
                        sectionAdapter.removeAllSections();
                        if (result == null || result.size() == 0) {
                            ToastUtil.show(MyTeamActivity.this, "该成员暂无推广数据哦~~", Toast.LENGTH_SHORT);
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

    private void searchOneFieldCommendData(String name) {
        Service.getComnonService().queryWithName(name, VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .compose(BoyiRxUtils.<ArrayList<String[]>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<String[]>>() {

                    @Override
                    public void onNext(ArrayList<String[]> result) {
                        if (result == null || result.size() == 0) {
                            ToastUtil.show(MyTeamActivity.this, "没有搜索到该团队成员哦~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        teamList.clear();
                        int i = 0;
                        for (String[] s : result) {
                            PersonEntity entity = new PersonEntity();
                            entity.setUid(s[0]);
                            entity.setName(s[1]);
                            if (i == 0) {
                                entity.setSel(true);
                                selIndex = i;
                            }
                            teamList.add(entity);
                            i++;
                        }
                        listTeamAdapter = new ListTeamAdapter(MyTeamActivity.this, teamList);
                        lvTeamList.setAdapter(listTeamAdapter);
                    }
                });
    }

    private void changeListSelState(int position) {
        if (teamList.get(selIndex) != null) {
            teamList.get(selIndex).setSel(false);
            teamList.get(position).setSel(true);
            selIndex = position;
            listTeamAdapter.notifyDataSetChanged();

        }

    }

    @OnClick({R.id.iv_btn_search, R.id.tv_my_team})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_btn_search:
                String string = etTeamName.getText().toString();
                if(string.length() <= 0) {
                    ToastUtil.show(this, "要查找的名字要输入完整哦", Toast.LENGTH_SHORT);
                    return;
                }
                searchOneFieldCommendData(string);
                break;
            case R.id.tv_my_team:
                refreshTeamMemberData();
                break;
        }
    }


    public class ListTeamAdapter extends BaseAdapter {

        private final ArrayList<PersonEntity> prices;
        private final LayoutInflater inflater;
        private final Context context;
        private int currentIndex;

        public ListTeamAdapter(Context context, ArrayList<PersonEntity> prices) {
            this.context = context;
            this.prices = prices;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return prices.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ViewHolder holder = null;
            PersonEntity chargeInfoEntity = prices.get(position);
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_item_team, parent, false);
                holder = new ViewHolder();
                holder.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
                holder.view_sel_status = rootView.findViewById(R.id.view_sel_status);
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeListSelState(position);
                    }
                });
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            setData(holder, chargeInfoEntity);
            return rootView;
        }

        public void setData(ViewHolder holder, PersonEntity entity) {
            if (entity.isSel()) {
                holder.view_sel_status.setVisibility(View.VISIBLE);
                holder.tv_name.setTextColor(getResources().getColor(R.color.item_sel_txt_team));
                refreshFieldCommendData(entity.getUid());
            } else {
                holder.tv_name.setTextColor(getResources().getColor(R.color.item_not_sel_txt_team));
                holder.view_sel_status.setVisibility(View.INVISIBLE);
            }
            if(entity.getName() != null && entity.getName().length() > 0) {

                holder.tv_name.setText(entity.getName());
            }else {

                holder.tv_name.setText(entity.getUid());
            }
        }


        class ViewHolder {
            TextView tv_name;
            View view_sel_status;
        }

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
            switch (position) {
                case 0:
                    itemHolder.tv_type.setText("新增会员");
                    break;
                case 1:
                    itemHolder.tv_type.setText("新增大使");
                    break;
            }
            itemHolder.tv_num.setText(itemList.get(position) + "人");
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

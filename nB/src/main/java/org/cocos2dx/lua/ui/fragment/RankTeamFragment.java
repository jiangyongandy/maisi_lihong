package org.cocos2dx.lua.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maisi.video.obj.video.ChargeInfoEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RankTeamFragment extends LazyFragment {

    @BindView(R.id.lv_list)
    ListView lvList;
    private boolean isFirstLazyLoad;
    private ArrayList<ChargeInfoEntity> list = new ArrayList<>();

    private ListAdapter listAdapter;


    @Override
    protected void lazyLoad() {
        if (!CommonConstant.isRelease) {
            return;
        }
        if (VipHelperUtils.getInstance().isWechatLogin() && !isFirstLazyLoad) {
            isFirstLazyLoad = true;

        }
    }

    @Override
    protected void initData() {
        Service.getComnonService().queryTeamList()
                .compose(BoyiRxUtils.<ArrayList<String[]>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<String[]>>() {

                    @Override
                    public void onNext(ArrayList<String[]> result) {
                        if(result == null || result.size() == 0) {
                            ToastUtil.show(getActivity(), "暂无排行榜数据哦~~", Toast.LENGTH_SHORT);
                            return;
                        }
                        listAdapter = new ListAdapter(getActivity(), result);
                        lvList.setAdapter(listAdapter);
                        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        });
                    }
                });
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_rank_team_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void jumpActivity(Class _activity) {
        Intent intent = new Intent(getContext(), _activity);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public class ListAdapter extends BaseAdapter {

        private final ArrayList<String[]> prices;
        private final LayoutInflater inflater;
        private final Context context;

        public ListAdapter(Context context, ArrayList<String[]> prices) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ViewHolder holder = null;
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_rank_item, parent, false);
                holder = new ViewHolder(rootView);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            setData(holder, position);
            return rootView;
        }

        public void setData(ListAdapter.ViewHolder holder, int position) {
            String[] entity = prices.get(position);
            int rankNum = position + 1;
            holder.tvNo.setText("" + rankNum);
            holder.tvUid.setText(entity[0]);
            holder.tvName.setText(entity[1]);
//            holder.tvName.setText(entity[1]);
            holder.tvNum.setText(entity[2]);
        }

        class ViewHolder {
            @BindView(R.id.tv_no)
            TextView tvNo;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_uid)
            TextView tvUid;
            @BindView(R.id.tv_num)
            TextView tvNum;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }
}

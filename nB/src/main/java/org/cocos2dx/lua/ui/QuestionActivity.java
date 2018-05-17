package org.cocos2dx.lua.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.maisi.video.obj.video.QuestionEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.service.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class QuestionActivity extends BaseActivity {


    @BindView(R.id.lv_list)
    ListView lvList;
    private int currentIndex;
    private double currentPay;
    private LinearLayout[] linearLayouts;
    private ArrayList<QuestionEntity > list = new ArrayList<>();
    private ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_question_list);
        ButterKnife.bind(this);

        listAdapter = new ListAdapter(this, list);
        lvList.setAdapter(listAdapter);

        //获取充值金额
        Service.getComnonService().getQuestion()
                .compose(BoyiRxUtils.<ArrayList<QuestionEntity>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<QuestionEntity>>() {

                    @Override
                    public void onNext(ArrayList<QuestionEntity> result) {
                        list.clear();
                        list.addAll(result);
                        listAdapter.notifyDataSetChanged();
                    }
                });
    }


    public class ListAdapter extends BaseAdapter {

        private final ArrayList<QuestionEntity > prices;
        private final LayoutInflater inflater;
        private final Context context;
        private int currentIndex;

        public ListAdapter(Context context, ArrayList<QuestionEntity > prices) {
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
            final QuestionEntity entity = this.prices.get(position);
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_item_question, parent, false);
                holder = new ViewHolder();
                holder.tv_question = (TextView) rootView.findViewById(R.id.tv_question);
                holder.tv_answer = (TextView) rootView.findViewById(R.id.tv_answer);
                final ViewHolder finalHolder = holder;
                holder.tv_question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!entity.isOpen()) {
                            entity.setOpen(true);
                            finalHolder.tv_answer.setVisibility(View.VISIBLE);
                        }else {
                            finalHolder.tv_answer.setVisibility(View.GONE);
                            entity.setOpen(false);
                        }

                    }
                });
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            setData(holder, entity);
            return rootView;
        }

        public void setData(ViewHolder holder,QuestionEntity entity) {
            holder.tv_question.setText("Q:"+entity.getValue1());
            holder.tv_answer.setText("A:"+entity.getValue2());
            if(!entity.isOpen()) {
                holder.tv_answer.setVisibility(View.GONE);
            }else {
                holder.tv_answer.setVisibility(View.VISIBLE);
            }
        }


        class ViewHolder {
            TextView tv_question;
            TextView tv_answer;
            boolean isopen;
        }

    }
}

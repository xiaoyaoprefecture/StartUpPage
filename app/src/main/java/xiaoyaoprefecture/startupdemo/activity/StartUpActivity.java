package xiaoyaoprefecture.startupdemo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import xiaoyaoprefecture.startupdemo.R;
import xiaoyaoprefecture.startupdemo.constant.constant;
import xiaoyaoprefecture.startupdemo.util.PreferenceUtil;

/**
 * Created by Administrator on 2017/9/4.
 */

public class StartUpActivity extends AppCompatActivity {
    /**
     * 是否显示启动页
     */
    boolean isShow=false;
    private ViewPager mViewPager;
    /**
     * 装载小圆圈的LinearLayout
     */
    private LinearLayout indicatorLayout;
    /**
     * ViewPager的每个页面集合
     */
    private List<View>views;
    /**
     * ViewPager下面的小圆圈
     */
    private ImageView[]mImageViews;
    /**
     * PageAdapter对象
     */
    private MyPageAdapter myPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startuppage);
        //获取存入的是否显示启动页的值
        isShow= PreferenceUtil.getBoolean(StartUpActivity.this, constant.SHOW_GUIDE);
        if (isShow){
            //直接进入主页面
            enterMainActivity();
        }else {
            //进入启动页
            initView();
        }

    }

    /**
     * 初始化启动页
     */
    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.StartUpPage);
        indicatorLayout= (LinearLayout) findViewById(R.id.linearlayout);
        LayoutInflater inflater= LayoutInflater.from(StartUpActivity.this);
        views=new ArrayList<>();
        //添加资源
        views.add(inflater.inflate(R.layout.pager1,null));
        views.add(inflater.inflate(R.layout.pager2,null));
        views.add(inflater.inflate(R.layout.pager3,null));
        views.add(inflater.inflate(R.layout.pager4,null));
        myPageAdapter=new MyPageAdapter(StartUpActivity.this,views);
        mImageViews=new ImageView[views.size()];
        drawCircl();
        mViewPager.setAdapter(myPageAdapter);
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    /**
     * 页面改变的监听事件
     */
    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 页面改变后，如果是当前页面，将小圆圈改为选中，其它页面则改为未选中
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            for (int i=0;i<mImageViews.length;i++){
                if (position==i){//选中
                    mImageViews[i].setImageResource(R.drawable.selected);
                }else {//未选中
                    mImageViews[i].setImageResource(R.drawable.unselected);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    /**
     * 画圆
     */
    private void drawCircl() {
        int num=views.size();
        for (int i=0;i<num;i++){
            //实例化每一个圆圈
            mImageViews[i]=new ImageView(StartUpActivity.this);
            if (i==0){
                //选中
                mImageViews[i].setImageResource(R.drawable.selected);
            }else {
                mImageViews[i].setImageResource(R.drawable.unselected);
            }
            //给每个圆圈设置间隔
            mImageViews[i].setPadding(8,8,8,8);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity= Gravity.CENTER_VERTICAL;
            indicatorLayout.addView(mImageViews[i],params );

        }
    }

    /**
     * 进入主页面
     */
    private void enterMainActivity() {
        startActivity(new Intent(StartUpActivity.this,MainActivity.class));
        finish();
    }
    private class MyPageAdapter extends PagerAdapter {
        private List<View>Views;
        private Activity context;
        public MyPageAdapter(Activity context,List<View>Views){
            this.Views=Views;
            this.context=context;
        }
        @Override
        public int getCount() {
            return Views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(Views.get(position));
        }

        /**
         * 实例化页卡，如果是最后一页，则获取它的button并且添加点击事件
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(Views.get(position),0);
            if (position==Views.size()-1){//最后一页
                Button button=container.findViewById(R.id.enterMain);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       PreferenceUtil.setBoolean(context,constant.SHOW_GUIDE,true);
                        enterMainActivity();
                    }
                });
            }
            return Views.get(position);
        }
    }

}

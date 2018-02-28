package com.hcll.fishshrimpcrab.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.Record.fragment.MainRecordFragment;
import com.hcll.fishshrimpcrab.base.BaseAtivity;
import com.hcll.fishshrimpcrab.club.fragment.MainClubFragment;
import com.hcll.fishshrimpcrab.common.widget.NoScrollViewPager;
import com.hcll.fishshrimpcrab.game.fragment.MainGameFragment;
import com.hcll.fishshrimpcrab.me.fragment.MainMeFragment;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.animate.AnimationType;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAtivity implements OnTabSelectListener {

    public final static String BROADCAST_ACTION_RESREQ_RESULT = "wu2dou.braodcast.action.resreq.result";

    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_GENDER = "gender";
    public static final String EXTRA_NICK = "nick";
    public static final String EXTRA_PORTRAIT = "portrait";
    public static final String EXTRA_DIAMONDS = "diamonds";

    @Titles
    private static final int[] mTitles = {R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4};

    @SeleIcons
    private static final int[] mSeleIcons = {R.drawable.main_tab_game_select, R.drawable.main_tab_club_select,
            R.drawable.main_tab_record_select, R.drawable.main_tab_me_select};

    @NorIcons
    private static final int[] mNormalIcons = {R.drawable.main_tab_game, R.drawable.main_tab_club,
            R.drawable.main_tab_record, R.drawable.main_tab_me};

    @BindView(R.id.main_view_pager)
    NoScrollViewPager mainViewPager;
    @BindView(R.id.main_tabbar)
    JPTabBar mainTabbar;

    private List<Fragment> list = new ArrayList<>();
    private int userId;

    /**
     * 本地广播管理
     */
    private LocalBroadcastManager mLocalBroadcastManager;
    private ResReqBroadcastReceiver broadcastReceiverRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initParam();
        initView();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        broadcastReceiverRes = new ResReqBroadcastReceiver();
        mLocalBroadcastManager.registerReceiver(broadcastReceiverRes, new IntentFilter(BROADCAST_ACTION_RESREQ_RESULT));
    }


    private void initParam() {
        userId = getIntent().getIntExtra(EXTRA_USER_ID, 0);
    }

    private void initView() {
//        showTopBar();
        MainGameFragment gameFragment = new MainGameFragment();
        MainClubFragment clubFragment = new MainClubFragment();
        MainRecordFragment recordFragment = new MainRecordFragment();
        MainMeFragment meFragment = new MainMeFragment();
        MainFragment mainFragment = new MainFragment();

        list.add(gameFragment);
        list.add(clubFragment);
        list.add(recordFragment);
        list.add(meFragment);
        list.add(mainFragment);

        mainTabbar.setTabListener(this);
        mainViewPager.setOffscreenPageLimit(4);
        mainViewPager.setAdapter(new MainFragAdapter(getSupportFragmentManager(), list));

        mainTabbar.setContainer(mainViewPager);
        mainTabbar.setTabListener(this);
        mainTabbar.setUseScrollAnimate(true);
        mainTabbar.setAnimation(AnimationType.SCALE);
        mainTabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainTabbar.setAllNotSelect();
                mainViewPager.setCurrentItem(mainViewPager.getAdapter().getCount() - 1, false);
            }
        });
        mainViewPager.setCurrentItem(mainViewPager.getAdapter().getCount() - 1, false);

    }

    public static Intent createActivit(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    public static Intent createLoginResReceiver(int gender, String nick, String portrait, int diamonds) {
        Intent intent = new Intent(BROADCAST_ACTION_RESREQ_RESULT);
        intent.putExtra(EXTRA_GENDER, gender);
        intent.putExtra(EXTRA_NICK, nick);
        intent.putExtra(EXTRA_PORTRAIT, portrait);
        intent.putExtra(EXTRA_DIAMONDS, diamonds);
        return intent;
    }

    @Override
    public void onTabSelect(int index) {

    }

    public JPTabBar getTabbar() {
        return mainTabbar;
    }


    @Override
    protected void onDestroy() {
        mLocalBroadcastManager.unregisterReceiver(broadcastReceiverRes);
        super.onDestroy();
    }

    /**
     * 发送消息的广播接收器
     */
    private class ResReqBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}

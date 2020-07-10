package com.rainwood.sentlogistics.ui.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rainwood.sentlogistics.ui.fragment.BulletinFragment;
import com.rainwood.sentlogistics.utils.ListUtil;
import com.rainwood.sentlogistics.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/7 13:26
 * @Desc: 消息给公告adapter
 */
public final class BulletinTypeAdapter extends FragmentPagerAdapter {

    List<String> mBulletinList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public BulletinTypeAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void setBulletinList(List<String> bulletinList) {
        mBulletinList.clear();
        mBulletinList.addAll(bulletinList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        LogUtil.d(this, "getItem - > " + position);
        return BulletinFragment.newInstance(mBulletinList.get(position));
    }

    @Override
    public int getCount() {
        return ListUtil.getSize(mBulletinList);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mBulletinList.get(position);
    }
}

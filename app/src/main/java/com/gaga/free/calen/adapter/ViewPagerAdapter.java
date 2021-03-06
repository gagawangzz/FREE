package com.gaga.free.calen.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gaga.free.calen.CustomCalendar;
import com.gaga.free.calen.fragment.CalendarFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private int mCount;
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();
    private CustomCalendar mCalendar;


    public ViewPagerAdapter(FragmentManager fm, int count, CustomCalendar calendar) {
        super(fm);
        this.mCount = count;
        this.mCalendar = calendar;
    }

    @Override
    public Fragment getItem(int position) {
        return CalendarFragment.newInstance(mCalendar);
    }

    @Override
    public int getCount() {
        return this.mCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}

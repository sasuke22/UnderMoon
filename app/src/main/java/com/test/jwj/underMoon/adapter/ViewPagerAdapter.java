package com.test.jwj.underMoon.adapter;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.test.jwj.underMoon.fragments.BaseFragment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final static String FRAGMENT_ARGUMENTS = "fragment_arguments";
    private ArrayList<Class<? extends BaseFragment>> fragmentClassList;
    private Activity act;

    public ViewPagerAdapter(Activity act, FragmentManager fm, ArrayList<Class<? extends BaseFragment>> fragments){
        super(fm);
        this.act = act;
        this.fragmentClassList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentClassList.size();
    }

    @Override
    public Fragment getItem(int position) {
        final Class[] classes = {};
        BaseFragment fragment = null;
        Bundle bundle;

        bundle = new Bundle();
        bundle.putInt(FRAGMENT_ARGUMENTS, position);//如果fragment需要参数的话可以从这里传进去
        try {
            fragment = fragmentClassList.get(position).getConstructor(classes).newInstance();
            fragment.setArguments(bundle);
            fragment.act = act;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return fragment;
    }

    //这个类干嘛用的？
    public int getItemPosition(Class<? extends BaseFragment> item) {
        return fragmentClassList.indexOf(item);
    }
}

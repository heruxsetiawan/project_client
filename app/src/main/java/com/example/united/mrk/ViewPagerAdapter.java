package com.example.united.mrk;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by DAT on 8/16/2015.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public  ArrayList<Fragment> mFragmentList = new ArrayList<>();
    public  ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private long baseId = 0;
    Context context;
    ViewPager viewPager;
    TabLayout tabLayout;
    private FragmentTransaction mCurTransaction = null;
    private ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();


    public ViewPagerAdapter(FragmentManager manager, Context context, ViewPager viewPager,
                            TabLayout tabLayout ) {
        super(manager);
        this.context = context;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;



    }



    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    public View getTabView(final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null);
        TextView tabItemName = (TextView) view.findViewById(R.id.textViewTabItemName);
        ImageView tabItemAvatar = (ImageView) view.findViewById(R.id.imageViewTabItemAvatar);
        tabItemName.setText(mFragmentTitleList.get(position));
       // tabItemName.setTextColor(R.drawable.tab_text);
       // tabItemName.setTextColor(context.getResources().getColor(android.R.color.background_light));

/*
        switch (mFragmentTitleList.get(position)) {
            case "Beer":
                tabItemAvatar.setImageResource(R.drawable.tab_beer_selector);
                break;

            case "Rokok":
                tabItemAvatar.setImageResource(R.drawable.tab_smoke_selector);
                break;

            case "Snack":
                tabItemAvatar.setImageResource(R.drawable.tab_snack_selector);
                break;

            case "Beverages":
                tabItemAvatar.setImageResource(R.drawable.tab_beverages_selector);
                break;

            case "Beverages TTH":
                tabItemAvatar.setImageResource(R.drawable.tab_bevaragestth_selector);
                break;

            case "Food":
                tabItemAvatar.setImageResource(R.drawable.tab_food_selector);
                break;

            case "Merchandise":
                tabItemAvatar.setImageResource(R.drawable.tab_merch_selector);
                break;

            case "Paket":
                tabItemAvatar.setImageResource(R.drawable.tab_paket);
                break;

        }
*/
        return view;
    }


    public void destroyFragmentView(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
    }
    @Override
    public int getItemPosition(Object object) {
          return POSITION_NONE;
        //return PagerAdapter.POSITION_UNCHANGED;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }



    @Override
    public Fragment getItem(int position) {
        return  mFragmentList.get(position);
    }


}



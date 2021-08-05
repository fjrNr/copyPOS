package com.example.copypos.adapter;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.copypos.R;
import com.example.copypos.navigationDrawer.home.SellPhotocopyFragment;
import com.example.copypos.navigationDrawer.home.SellPrintFragment;
import com.example.copypos.navigationDrawer.home.SellProductFragment;
import com.example.copypos.navigationDrawer.home.SellServiceFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_product, R.string.tab_print, R.string.tab_photocopy, R.string.tab_service};
    private final Context mContext;

    public HomePagerAdapter(Context context, FragmentManager childFragmentManager) {
        super(childFragmentManager);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int pos) {
        switch (pos){
            case 0: return SellProductFragment.newInstance();
            case 1: return SellPrintFragment.newInstance();
            case 2: return SellPhotocopyFragment.newInstance();
            case 3: return SellServiceFragment.newInstance();
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 4;
    }
}

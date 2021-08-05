package com.example.copypos.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.copypos.R;
import com.example.copypos.navigationDrawer.transactions.PurchaseListFragment;
import com.example.copypos.navigationDrawer.transactions.SaleListFragment;

public class TransactionsPagerAdapter extends FragmentPagerAdapter {
    private static final int[] TAB_TITLES = {R.string.tab_sale, R.string.tab_purchase};
    private final Context mContext;

    public TransactionsPagerAdapter(Context context, FragmentManager childFragmentManager) {
        super(childFragmentManager);
        this.mContext = context;
    }

    @NonNull
    public Fragment getItem(int pos) {
        if (pos == 0) {
            return SaleListFragment.newInstance();
        }else if (pos == 1) {
            return PurchaseListFragment.newInstance();
        }else{
            return null;
        }
    }

    public CharSequence getPageTitle(int position) {
        return this.mContext.getResources().getString(TAB_TITLES[position]);
    }

    public int getCount() {
        return 2;
    }
}

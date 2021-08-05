package com.example.copypos.navigationDrawer.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.copypos.R;
import com.example.copypos.adapter.TransactionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TransactionsFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transactions, container, false);
        TransactionsPagerAdapter pagerAdapter = new TransactionsPagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        ((TabLayout) root.findViewById(R.id.tabs)).setupWithViewPager(viewPager);
        return root;
    }
}

package com.example.copypos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.copypos.R;
import com.example.copypos.api.ApiClient;
import com.example.copypos.contractView.FragmentCommunicator;
import com.example.copypos.controller.SharedPrefController;
import com.example.copypos.navigationDrawer.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, FragmentCommunicator {
    private static final int INTENT_SWITCH = 100;
    TextView branchName;
    DrawerLayout drawer;
    View headerView;
    ImageView imgSwitch;
    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    NavigationView navigationView;
    ImageView profileImage;
    TextView profileName;
    Toolbar toolbar;

    SharedPrefController spc;

    /* access modifiers changed from: protected */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        profileImage = headerView.findViewById(R.id.imageView);
        profileName = headerView.findViewById(R.id.txt_name);
        branchName = headerView.findViewById(R.id.txt_branch);
        imgSwitch = headerView.findViewById(R.id.img_test);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_purchase, R.id.nav_transactions, R.id.nav_employees, R.id.nav_inventory, R.id.nav_expenses, R.id.nav_services, R.id.nav_report, R.id.nav_empty).setDrawerLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
        imgSwitch.setOnClickListener(this);
        profileImage.setOnClickListener(this);

        spc = new SharedPrefController(this);

        if (!spc.getSPOwnerMode()) {
            imgSwitch.setBackground(getDrawable(R.drawable.ic_action_bar_logout));
        }
        hideMenuItem();
        loadUserProfile();
    }

    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        navController.navigate(menuItem.getItemId());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void hideMenuItem(){
        Menu nav_menu = navigationView.getMenu();
        if(!spc.getSPOwnerMode()) {
            if(!spc.getSPAllSell()){
                nav_menu.findItem(R.id.nav_home).setVisible(false);
            }

            if(!spc.getSPAllPur()){
                nav_menu.findItem(R.id.nav_purchase).setVisible(false);
            }

            if(!spc.getSPAllInventory()){
                nav_menu.findItem(R.id.nav_inventory).setVisible(false);
                nav_menu.findItem(R.id.nav_transactions).setVisible(false);
            }

            if(!spc.getSPAllExpense()){
                nav_menu.findItem(R.id.nav_expenses).setVisible(false);
                nav_menu.findItem(R.id.nav_transactions).setVisible(false);
            }

            nav_menu.findItem(R.id.nav_employees).setVisible(false);
            nav_menu.findItem(R.id.nav_services).setVisible(false);
            nav_menu.findItem(R.id.nav_report).setVisible(false);
        }
    }

    private void loadUserProfile() {
        profileName.setText(spc.getSpFullUserName());
        branchName.setText(spc.getSPBranchName());
        if (spc.getSpBranchImage().equals("")) {
            profileImage.setImageDrawable(null);
            return;
        }
        Picasso picasso = Picasso.get();
        picasso.load(ApiClient.BASE_URL_BRANCH_IMAGE + spc.getSpBranchImage()).fit().into(profileImage);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_SWITCH && resultCode == -1) {
            loadUserProfile();
        }
    }

    public void onClick(View v) {
        if (v == profileImage) {
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "Profile image", Toast.LENGTH_SHORT).show();
        } else if (v == imgSwitch) {
            if(spc.getSPOwnerMode()){
                drawer.closeDrawer(GravityCompat.START);
                startActivityForResult(new Intent(this, BranchListActivity.class), INTENT_SWITCH);
            }else{
                spc.saveSPString(SharedPrefController.SP_FULLUSERNAME, "");
                spc.saveSPString(SharedPrefController.SP_USERIMAGE, "");
                spc.saveSPBoolean(SharedPrefController.SP_OWNERMODE, false);
                spc.saveSPBoolean(SharedPrefController.SP_SUDAH_LOGIN, false);

                spc.saveSPInt(SharedPrefController.SP_BRANCHID, 0);
                spc.saveSPInt(SharedPrefController.SP_EMPLOYEEID, 0);
                spc.saveSPBoolean(SharedPrefController.SP_ALLSELL, false);
                spc.saveSPBoolean(SharedPrefController.SP_ALLPUR, false);
                spc.saveSPBoolean(SharedPrefController.SP_ALLEXPENSE, false);
                spc.saveSPBoolean(SharedPrefController.SP_ALLINVENTORY, false);
                Intent i = new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }

    public void onSetData(HashMap<String, String> item) {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment fragment = Objects.requireNonNull(navHostFragment).getChildFragmentManager().getFragments().get(0);
//        ((HomeFragment) fragment).updateCart(item);
        ((HomeFragment) fragment).id = Integer.parseInt(Objects.requireNonNull(item.get("id")));
        ((HomeFragment) fragment).name = item.get("name");
        ((HomeFragment) fragment).price = Integer.parseInt(Objects.requireNonNull(item.get("price")));
        ((HomeFragment) fragment).type = item.get("type");
        ((HomeFragment) fragment).inputDialog();
    }
}

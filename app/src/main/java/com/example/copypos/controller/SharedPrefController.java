package com.example.copypos.controller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class SharedPrefController {
    public static final String SP_APP = "spApp";
    public static final String SP_ALLEXPENSE = "spAllExpense";
    public static final String SP_ALLINVENTORY = "spAllInventory";
    public static final String SP_ALLPUR = "spAllPur";
    public static final String SP_ALLSELL = "spAllSell";
    public static final String SP_BRANCHID = "spBranchId";
    public static final String SP_BRANCHIMAGE = "spBranchIamge";
    public static final String SP_BRANCHNAME = "spBranchName";
    public static final String SP_BUYPRODUCTID = "spProductId";
    public static final String SP_BUYPRODUCTNAME = "spProductName";
    public static final String SP_BUYPRODUCTPRICE = "spProductPrice";
    public static final String SP_CUSTOMERID = "spCustomerId";
    public static final String SP_CUSTOMERNAME = "spCustomerName";
    public static final String SP_EMPLOYEEID = "spEmployeeId";
    public static final String SP_FULLUSERNAME = "spFullUserName";
    public static final String SP_OWNERID = "spOwnerId";
    public static final String SP_OWNERMODE = "spOwnerMode";
    public static final String SP_SUDAH_LOGIN = "spHasLogged";
    public static final String SP_SUPPLIERID = "spSupplierId";
    public static final String SP_SUPPLIERNAME = "spSupplierName";
    public static final String SP_USERIMAGE = "spUserImage";
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefController(Context context) {
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value) {
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public Boolean getSPOwnerMode() {
        return sp.getBoolean(SP_OWNERMODE, false);
    }

    public String getSpFullUserName() {
        return sp.getString(SP_FULLUSERNAME, "");
    }

    public String getSpUserImage() {
        return sp.getString(SP_USERIMAGE, "");
    }

    public String getSpBranchImage() {
        return sp.getString(SP_BRANCHIMAGE, "");
    }

    public String getSPBranchName() {
        return sp.getString(SP_BRANCHNAME, "");
    }

    public String getSPProductName() {
        return sp.getString(SP_BUYPRODUCTNAME, "");
    }

    public int getSPOwnerId() {
        return sp.getInt(SP_OWNERID, 0);
    }

    public int getSPBranchId() {
        return sp.getInt(SP_BRANCHID, 0);
    }

    public int getSPProductId() {
        return sp.getInt(SP_BUYPRODUCTID, 0);
    }

    public int getSPProductPrice() {
        return sp.getInt(SP_BUYPRODUCTPRICE, 0);
    }

    public Boolean getSPAllSell() {
        return sp.getBoolean(SP_ALLSELL, false);
    }

    public Boolean getSPAllPur() {
        return sp.getBoolean(SP_ALLPUR, false);
    }

    public Boolean getSPAllExpense() {
        return sp.getBoolean(SP_ALLEXPENSE, false);
    }

    public Boolean getSPAllInventory() {
        return sp.getBoolean(SP_ALLINVENTORY, false);
    }

    public Boolean getSPSudahLogin() {
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}

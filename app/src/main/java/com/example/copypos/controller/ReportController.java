package com.example.copypos.controller;

import android.os.Environment;
import android.util.Log;

import com.example.copypos.api.ApiClient;
import com.example.copypos.api.ApiInterface;
import com.example.copypos.contractView.ReportView;
import com.example.copypos.model.Report;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportController {
    private ReportView view;

    public ReportController(ReportView view) {
        this.view = view;
    }

    public void getBestSellerList(final int branchId, final int periodeTypeId, final String periode, final int itemType){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Report> call = apiInterface.getBestSellerItem(branchId, periodeTypeId, periode, itemType);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    view.onGetSummaryResult(response.body());
//                    view.onGetSummaryResult();
                }
            }
            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void getSummary(final int branchId, final int periodeTypeId, final String periode){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Report> call = apiInterface.getReportSummary(branchId, periodeTypeId, periode);
        call.enqueue(new Callback<Report>() {

            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    view.onGetSummaryResult(response.body());
                }
            }
            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    public void printReport(final int branchId, final int periodeTypeId, final String periode, final String reportType){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.printReport(branchId, periodeTypeId, periode, reportType);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // The most important part is in below code
                    String fileName;
                    Date date = new Date();
                    SimpleDateFormat formatter; new SimpleDateFormat("yyyyMMdd");
                    String strPeriode;

                    if(periodeTypeId == 1){
                        formatter = new SimpleDateFormat("yyyyMMdd");
                    }else{
                        formatter = new SimpleDateFormat("yyyyMM");
                    }

                    strPeriode = formatter.format(date);
                    fileName = "laporan_" + periode + "_" + strPeriode + ".pdf";

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileName);
                    if(writtenToDisk){
                        view.hideProgress();
                        view.onSuccess("Ekspor data berhasil, file "+ fileName + "telah tersimpan");
                    }else{
                        view.hideProgress();
                        view.onError("Gagal ekspor data ke dalam format laporan PDF");
                    }
                } else {
                    view.hideProgress();
                    view.onError("Gagal ambil data");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.hideProgress();
                view.onError(t.getLocalizedMessage());
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                     fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}

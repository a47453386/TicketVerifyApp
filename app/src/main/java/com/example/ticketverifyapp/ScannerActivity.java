package com.example.ticketverifyapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ticketverifyapp.Model.VerifyRequest;
import com.example.ticketverifyapp.Model.VerifyResponse;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity {
    private PreviewView previewView;
    private boolean isDetected = false;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        previewView = findViewById(R.id.previewView);
        startTime = System.currentTimeMillis();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            Toast.makeText(this, "需要相機權限才能掃描", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
                Log.e("Scanner", "相機初始化失敗", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
            // 緩衝 1 秒避免開啟即閃退
            if (System.currentTimeMillis() - startTime < 1000) {
                image.close();
                return;
            }
            @SuppressWarnings("UnsafeOptInUsageError")
            InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());

            scanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        if (barcodes.size() > 0 && !isDetected) {
                            isDetected = true;
                            String rawValue = barcodes.get(0).getRawValue();

                            // 🚩 改為執行核銷 API
                            runOnUiThread(() -> performVerify(rawValue));
                        }
                    })
                    .addOnCompleteListener(task -> image.close());
        });

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    private void performVerify(String code) {
        RetrofitClient.getApiService().verifyTicket(new VerifyRequest(code)).enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VerifyResponse result = response.body();
                    // 🚩 顯示自定義彈窗
                    showPixelDialog(result.isSuccess(), result.getMessage(),
                            result.getData() != null ? result.getData().getScannedTime() : null, code);
                } else {
                    // 情況 B：邏輯錯誤 (400 Bad Request，例如日期不對、已核銷)
                    try {
                        // 1. 取得原始的 JSON 錯誤字串
                        String errorJson = response.errorBody().string();

                        // 2. 使用 Gson 手動解析這個字串成 VerifyResponse 物件
                        VerifyResponse errorResult = new Gson().fromJson(errorJson, VerifyResponse.class);

                        // 3. 只顯示裡面的 message
                        showPixelDialog(false, errorResult.getMessage(), null, code);

                    } catch (Exception e) {
                        // 如果解析失敗（例如伺服器噴了非 JSON 的錯誤）
                        showPixelDialog(false, "系統錯誤: " + response.code(), null, code);
                    }
                }   
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {
                showPixelDialog(false, "連線伺服器失敗", null, code);
            }
        });
    }

    private void showPixelDialog(boolean isSuccess, String message, String time, String code) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_pixel_result);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setCancelable(false);

        TextView tvTitle = dialog.findViewById(R.id.tvDialogTitle);
        TextView tvContent = dialog.findViewById(R.id.tvDialogContent);
        Button btnConfirm = dialog.findViewById(R.id.btnDialogConfirm);

        // 成功用藍色，失敗用粉紅色
        if (isSuccess) {
            tvTitle.setText("[ 驗證通過 ]");
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.pixel_blue));
            btnConfirm.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pixel_blue)));
        } else {
            tvTitle.setText("[ 驗證失敗 ]");
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.pixel_pink));
            btnConfirm.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pixel_pink)));
        }

        String displayInfo = ">> 序號: " + code + "\n>> 訊息: " + message;
        if (time != null) displayInfo += "\n>> 時間: " + time;
        tvContent.setText(displayInfo);

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            // 回傳結果給 MainActivity 更新終端機
            Intent resultIntent = new Intent();
            resultIntent.putExtra("scanned_code", code);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        dialog.show();
    }
}
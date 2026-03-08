package com.example.ticketverifyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketverifyapp.APIService.APIService;
import com.example.ticketverifyapp.Model.VerifyRequest;
import com.example.ticketverifyapp.Model.VerifyResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etCheckInCode;
    private Button btnVerify, btnOpenScanner;
    private TextView tvResult;

    // 🚩 註冊掃描結果接收器
    private final ActivityResultLauncher<Intent> scannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String scannedCode = result.getData().getStringExtra("scanned_code");
                    // 🚩 修改：使用中文像素風格提示
                    tvResult.setText(">> 偵測到代碼: " + scannedCode + "\n>> 執行驗證程序...");
                    performVerify(scannedCode);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCheckInCode = findViewById(R.id.etCheckInCode);
        btnVerify = findViewById(R.id.btnVerify);
        tvResult = findViewById(R.id.tvResult);
        btnOpenScanner = findViewById(R.id.btnOpenScanner);

        // 🚩 初始狀態修改
        tvResult.setText(">> 終端機：已就緒...");

        btnVerify.setOnClickListener(v -> {
            String code = etCheckInCode.getText().toString().trim();
            if (code.isEmpty()) {
                tvResult.setText(">> 錯誤：識別碼不可為空");
                return;
            }
            performVerify(code);
        });

        btnOpenScanner.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
            scannerLauncher.launch(intent);
        });
    }

    private void performVerify(String code) {
        VerifyRequest request = new VerifyRequest(code);

        RetrofitClient.getApiService().verifyTicket(request).enqueue(new Callback<VerifyResponse>() {
            @Override
            public void onResponse(Call<VerifyResponse> call, Response<VerifyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VerifyResponse result = response.body();

                    // 🚩 修改：套用賽博駭客風中文回傳
                    String terminalMsg = ">> 狀態：" + (result.isSuccess() ? "驗證成功" : "驗證失敗") +
                            "\n>> 訊息：" + result.getMessage();

                    if (result.getData() != null) {
                        terminalMsg += "\n>> 時間：" + result.getData().getScannedTime();
                    }
                    tvResult.setText(terminalMsg);
                } else {
                    tvResult.setText(">> 系統錯誤：代碼 " + response.code());
                }
            }

            @Override
            public void onFailure(Call<VerifyResponse> call, Throwable t) {
                tvResult.setText(">> 連線失敗：伺服器無回應");
            }
        });
    }
}
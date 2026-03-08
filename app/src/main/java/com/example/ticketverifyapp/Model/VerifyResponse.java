package com.example.ticketverifyapp.Model;

import com.google.gson.annotations.SerializedName;

public class VerifyResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private TicketData data;

    // Getter 方法：讓 Activity 可以讀取資料
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public TicketData getData() { return data; }
}
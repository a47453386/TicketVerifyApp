package com.example.ticketverifyapp.Model;

import com.google.gson.annotations.SerializedName;

public class VerifyRequest {
    @SerializedName("checkInCode")
    private String checkInCode;

    public VerifyRequest(String code) {
        this.checkInCode = code;
    }

    public String getCheckInCode() { return checkInCode; }
    public void setCheckInCode(String checkInCode) { this.checkInCode = checkInCode; }
}
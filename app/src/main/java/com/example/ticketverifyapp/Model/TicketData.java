package com.example.ticketverifyapp.Model;

import com.google.gson.annotations.SerializedName;

public class TicketData {
    @SerializedName("ticketID")
    private String ticketID;

    @SerializedName("scannedTime")
    private String scannedTime;

    // Getter 方法
    public String getTicketID() { return ticketID; }
    public String getScannedTime() { return scannedTime; }
}
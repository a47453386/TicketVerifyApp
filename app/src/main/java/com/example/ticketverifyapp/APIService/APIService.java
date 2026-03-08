package com.example.ticketverifyapp.APIService;

import retrofit2.Call;

import com.example.ticketverifyapp.Model.VerifyRequest;
import com.example.ticketverifyapp.Model.VerifyResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("api/TicketApi/Verify")
    Call<VerifyResponse> verifyTicket(@Body VerifyRequest request);
}

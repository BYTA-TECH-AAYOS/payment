package com.bytatech.ayoos.payment.client.paypal.api;



import org.springframework.cloud.openfeign.FeignClient;

import com.bytatech.ayoos.payment.client.paypal.ClientConfiguration;


@FeignClient(name = "paypal", url = "${paypal.url:https://api.sandbox.paypal.com/v1}", configuration = ClientConfiguration.class)
public interface PaymentGatewayApiClient extends PaymentGatewayApi {

	
}
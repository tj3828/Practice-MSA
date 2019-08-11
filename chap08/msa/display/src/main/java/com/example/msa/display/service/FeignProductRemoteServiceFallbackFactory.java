package com.example.msa.display.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignProductRemoteServiceFallbackFactory implements FallbackFactory<FeignProductRemoteService> {
    @Override
    public FeignProductRemoteService create(Throwable throwable) {
        System.out.println("t = " + throwable);
        return productId -> "[Feign Fallback : this product is sold out ]";
    }
}

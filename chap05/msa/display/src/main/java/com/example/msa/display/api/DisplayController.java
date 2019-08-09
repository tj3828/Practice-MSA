package com.example.msa.display.api;

import com.example.msa.display.service.ProductRemoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/displays")
public class DisplayController {

    private final ProductRemoteService productRemoteService;

    public DisplayController(ProductRemoteService productRemoteService) {
        this.productRemoteService = productRemoteService;
    }

    @GetMapping("/{displayId}")
    public String getDisplayDetail(@PathVariable String displayId) {
        String productInfo = productRemoteService.getProductInfo("12345");
        return String.format("[display id = %s at %s %s", displayId, System.currentTimeMillis(), productInfo);
    }

}



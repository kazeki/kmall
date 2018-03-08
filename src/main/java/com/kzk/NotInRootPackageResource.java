package com.kzk;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class NotInRootPackageResource {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}

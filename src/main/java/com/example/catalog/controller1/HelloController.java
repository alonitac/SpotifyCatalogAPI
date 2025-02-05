
package com.example.catalog.controller1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello from API! There is no UI here...";
    }
    @GetMapping("/internal")
    public  String internal(){
        return "Internal Service";
    }

}

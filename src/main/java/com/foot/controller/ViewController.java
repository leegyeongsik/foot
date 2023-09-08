package com.foot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/bp/resist")
    public String getBpResister() {
        return "bpResist";
    }

    @GetMapping("/view/bpSave")
    public String bpSavePage() {
        return "createBidProduct";
    }


}
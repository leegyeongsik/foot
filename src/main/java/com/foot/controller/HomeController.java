package com.foot.controller;

import com.foot.entity.UserRoleEnum;
import com.foot.security.UserDetailsImpl;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class HomeController {
    @GetMapping("")
    public String HomePage() {
        return "index";
    }

    @GetMapping("/Product/{productId}")
    public String ProductPage(@PathVariable Long productId , Model model){
        model.addAttribute("productId" ,productId);
        return "innerProduct";
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/Product/create")
    public String CreateProductPage(){
        return "createProduct";
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/Product/update/{productId}")
    public String UpdateProductPage(@PathVariable Long productId , Model model){
        model.addAttribute("productId" ,productId);
        return "updateProduct";
    }

    @GetMapping("/Chats")
    public String chatList(){
        return "chatlist";
    }
    @GetMapping("/Chats/{ChannelId}")
    public String chat(@PathVariable Long ChannelId , Model model , @AuthenticationPrincipal UserDetailsImpl userDetails){
        model.addAttribute("ChannelId" ,ChannelId);
        model.addAttribute("username" ,userDetails.getUser().getName());
        return "chat";
    }
}

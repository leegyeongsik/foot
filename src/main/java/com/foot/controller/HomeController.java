package com.foot.controller;

import com.foot.entity.UserRoleEnum;
import org.springframework.security.access.annotation.Secured;
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
}

package com.foot.controller;

import com.foot.entity.User;
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


    // 로그인 시 일반유저는 홈으로, 관리자는 관리자 페이지로 리다이렉트
    @GetMapping("/view/home")
    public String LoginHome(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        if(user.getRole() == UserRoleEnum.ADMIN) {
            return "redirect:/view/admin";
        } else {
            return "index";
        }
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

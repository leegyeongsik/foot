package com.foot.controller;

import com.foot.dto.ProfileResponseDto;
import com.foot.entity.User;
import com.foot.entity.UserRoleEnum;
import com.foot.repository.UserRepository;
import com.foot.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/view/admin")
@RequiredArgsConstructor
public class AdminViewController {
    private final UserRepository userRepository;
    private final AdminService adminService;

    // 전체 회원 목록 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/user-list")
    public String getUserList(Model model,
                              @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
                              Pageable pageable,
                              String searchKeyword) {

        Page<User> userList = null;

        if(searchKeyword == null) {
            userList = adminService.getUserList(pageable);

        } else {
            userList = adminService.userSearchList(searchKeyword, pageable);
        }

        int nowPage = userList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 4, userList.getTotalPages());

        model.addAttribute("list", userList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "userlist";
    }

    // 회원 상세 조회
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = adminService.getUser(id);
        model.addAttribute("user", user);
        return "adminUser";
    }



}

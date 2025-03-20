package com.videoanalysis.controller;

import com.videoanalysis.model.User;
import com.videoanalysis.service.UserService;
import com.videoanalysis.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final VideoService videoService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            userService.handleDailyLogin(user);
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/login";
    }

    @PostMapping("/parse")
    @ResponseBody
    public String parseVideo(@RequestParam String url, @AuthenticationPrincipal User user) {
        return videoService.parseVideo(url, user);
    }
} 
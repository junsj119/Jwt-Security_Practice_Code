package com.sparta.springweb.controller;

import com.sparta.springweb.dto.LoginRequestDto;
import com.sparta.springweb.dto.SignupRequestDto;
import com.sparta.springweb.jwt.JwtTokenProvider;
import com.sparta.springweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 로그인
    @PostMapping("/user/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!username.equals("anonymousUser")){
            return "이미 로그인이 되어있습니다.";
        }
        try{
            userService.login(loginRequestDto);
            String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());
            System.out.println(token);
            return token;
        }catch(Exception e){
            return e.getMessage();
        }
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(@Valid @RequestBody SignupRequestDto requestDto) {
        String res = userService.registerUser(requestDto);
        if (res.equals("")) {
            return "회원가입 성공";
        } else {
//            model.addAttribute("errortext", userService.registerUser(requestDto));
            return res;
        }
    }
}
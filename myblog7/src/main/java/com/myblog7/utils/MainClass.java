package com.myblog7.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MainClass {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("test"));
    }
}
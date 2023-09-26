package com.myblog7.controller;

import com.myblog7.entity.Role;
import com.myblog7.entity.User;
import com.myblog7.payload.JWTAuthResponse;
import com.myblog7.payload.LoginDto;
import com.myblog7.payload.SignUpDto;
import com.myblog7.repository.RoleRepository;
import com.myblog7.repository.UserRepository;
import com.myblog7.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider tokenProvider;
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        if (userRepo.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email Already Exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepo.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username Already Exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        // this will assign role to register individual
//        Role roles = roleRepo.findByName("ROLE_ADMIN").orElseThrow(() -> new NoSuchElementException("Role 'ROLE_ADMIN' not found"));
//        user.setRoles(Collections.singleton(roles));
        userRepo.save(user);

        return new ResponseEntity<>("User is Registered Successfully", HttpStatus.OK);
    }


}

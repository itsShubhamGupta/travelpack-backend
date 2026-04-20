package com.travelpack.service;

import com.travelpack.dao.RoleRepository;
import com.travelpack.dao.UserRepository;
import com.travelpack.dto.JwtResponse;
import com.travelpack.dto.RegisterRequest;
import com.travelpack.entity.Role;
import com.travelpack.entity.User;
import com.travelpack.exception.ResourceAlreadyExistsException;
import com.travelpack.exception.ResourceNotFoundException;
import com.travelpack.jwt.JwtAuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private  UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationHelper jwtHelper;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    public AuthService(UserRepository userRepository,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository=roleRepository;
    }

    public void signup(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("username is already");
        }
        Role role = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(registerRequest.getPassword());

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setName(registerRequest.getName());
        user.setPhone(registerRequest.getPhone());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
       userRepository.save(user);
    }

    public JwtResponse login(RegisterRequest userDto) {

        //authenticate with Authentication manager
        this.doAuthenticate(userDto.getEmail(),userDto.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getEmail());
        String token = jwtHelper.generateToken(userDetails);
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("NOT FOUND"));
        RegisterRequest userDto1=new RegisterRequest();
        userDto1.setName(user.getName());
        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getRoleName()) // ROLE_ADMIN, ROLE_USER
                .collect(Collectors.toList());
        userDto1.setRoles(roles);
        JwtResponse response = JwtResponse.builder().user(userDto1).token(token).build();

        return response;
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authenticationToken);

        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}

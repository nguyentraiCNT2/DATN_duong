package com.nckhntu.doantonghiep.IMPL;

import com.nckhntu.doantonghiep.DTO.UserDTO;
import com.nckhntu.doantonghiep.Entity.LogEntity;
import com.nckhntu.doantonghiep.Entity.UserEntity;
import com.nckhntu.doantonghiep.Repository.LogRepository;
import com.nckhntu.doantonghiep.Repository.UserRepository;
import com.nckhntu.doantonghiep.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserIMPL implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;
    private final LogRepository logRepository;

    public UserIMPL(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, HttpSession httpSession, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
        this.logRepository = logRepository;
    }

    @Override
    public boolean register(UserDTO user) {
        try {
            boolean isMatchEmail = userRepository.existsByEmail(user.getEmail());
            if (isMatchEmail) {
                throw new RuntimeException("Tài Khoản đã tồn tại");
            }
            if (user.getPassword().length() < 8){
                throw new RuntimeException("Mật khẩu cần tối thiểu 8 ký tự");
            }
            if (user.getPassword().contains(" ")){
                throw new RuntimeException("Mật khẩu không thể chứa khoảng trắng");
            }
            UserEntity userEntity = modelMapper.map(user, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setRole("USER");
            userEntity.setActive(true);
            userEntity.setCreatedAt(Timestamp.from(Instant.now()));
          UserEntity saveUser =   userRepository.save(userEntity);
//            LogEntity log = new LogEntity();
//            log.setUserId(saveUser);
//            log.setAction("Người dùng "+user.getFullName()+" với email là "+user.getEmail()+" vừa đăng ký thành công!");
//            log.setCreatedAt(Timestamp.from(Instant.now()));
//            logRepository.save(log);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserDTO login(String email, String password) {
        try {
            UserEntity userOpt = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
            boolean isMatchPassword = passwordEncoder.matches(password, userOpt.getPassword());
            if (!isMatchPassword) throw new RuntimeException("Mật khẩu không chính xác");
            if (!userOpt.getActive()) throw new RuntimeException("Tài khoản của bạn đã bị khóa");
            httpSession.setAttribute("userRole", userOpt.getRole()); // Lưu quyền vào session
            httpSession.setAttribute("userEmail", userOpt.getEmail()); // Lưu email vào session
//            // Cấu hình xác thực cho SecurityContextHolder
//            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userOpt.getRole()));
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(userOpt.getEmail(), null, authorities);
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            LogEntity log = new LogEntity();
//            log.setUserId(userOpt);
//            log.setAction("Người dùng "+userOpt.getFullName()+" với email là "+userOpt.getEmail()+" Đăng nhập vào hệ thống!");
//            log.setCreatedAt(Timestamp.from(Instant.now()));
//            logRepository.save(log);
            return modelMapper.map(userOpt, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

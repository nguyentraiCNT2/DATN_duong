package com.nckhntu.doantonghiep.Service;

import com.nckhntu.doantonghiep.DTO.UserDTO;

public interface UserService {
    boolean register(UserDTO user);
    UserDTO login(String email, String password);
}

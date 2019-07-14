package com.apps.photoapp.api.users.service;

import com.apps.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(final UserDto userDetails);
    UserDto getUserDetailsByEmail(final String email);
    UserDto getUserByUserId(String userId);
}

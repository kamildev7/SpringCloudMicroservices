package com.apps.photoapp.api.users.service;

import com.apps.photoapp.api.users.shared.UserDto;

public interface UsersService {
    UserDto createUser(final UserDto userDetails);
}

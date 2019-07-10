package com.apps.photoapp.api.users.service;

import com.apps.photoapp.api.users.data.User;
import com.apps.photoapp.api.users.data.UserRepository;
import com.apps.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImplementation implements UsersService {

    private UserRepository userRepository;

    public UsersServiceImplementation(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(final UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDetails, User.class);
        user.setEncryptedPassword("test");

        final User save = userRepository.save(user);
        return modelMapper.map(save, UserDto.class);
    }
}

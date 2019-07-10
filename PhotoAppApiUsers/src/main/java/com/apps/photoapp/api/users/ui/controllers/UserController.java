package com.apps.photoapp.api.users.ui.controllers;

import com.apps.photoapp.api.users.service.UsersService;
import com.apps.photoapp.api.users.shared.UserDto;
import com.apps.photoapp.api.users.ui.model.CreateUserRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment env;

    private UsersService usersService;

    public UserController(final UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + env.getProperty("local.server.port");
    }

    @PostMapping
    public String createUser(@Valid @RequestBody final CreateUserRequest userDetails) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        usersService.createUser(userDto);

        return "Create user method is called";
    }
}

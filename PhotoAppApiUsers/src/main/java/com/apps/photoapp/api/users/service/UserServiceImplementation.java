package com.apps.photoapp.api.users.service;

import com.apps.photoapp.api.users.data.AlbumsServiceClient;
import com.apps.photoapp.api.users.data.UserEntity;
import com.apps.photoapp.api.users.data.UserRepository;
import com.apps.photoapp.api.users.shared.UserDto;
import com.apps.photoapp.api.users.ui.model.AlbumResponse;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {

    private static final String ALBUMS_URL = "albums.url";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //    private final RestTemplate restTemplate;
    private final Environment environment;
    private final AlbumsServiceClient albumsServiceClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserServiceImplementation(final UserRepository userRepository,
                                     final BCryptPasswordEncoder bCryptPasswordEncoder, final Environment environment,
                                     final AlbumsServiceClient albumsServiceClient) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        //        this.restTemplate = restTemplate;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
    }

    @Override
    public UserDto createUser(final UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity user = modelMapper.map(userDetails, UserEntity.class);

        final UserEntity save = userRepository.save(user);
        return modelMapper.map(save, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        //we use email as a username in this project
        final UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, Collections.emptyList());
    }

    @Override
    public UserDto getUserDetailsByEmail(final String email) {
        final UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(final String userId) {
        final UserEntity user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new UsernameNotFoundException(userId);
        }

        String albumsUrl = String.format(environment.getProperty(ALBUMS_URL), userId);

//        ResponseEntity<List<AlbumResponse>> albumsListResponse = restTemplate
//                .exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponse>>() {
//                });
//        List<AlbumResponse> albumsList = albumsListResponse.getBody();

        List<AlbumResponse> albums = albumsServiceClient.getAlbums(userId);

        final UserDto userDto = new ModelMapper().map(user, UserDto.class);
        userDto.setAlbums(albums);

        return userDto;
    }


}

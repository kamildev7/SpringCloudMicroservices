package com.apps.photoapp.api.users.data;

import com.apps.photoapp.api.users.ui.model.AlbumResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallback = AlbumsFallback.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    List<AlbumResponse> getAlbums(@PathVariable final String id);
}


@Component
class AlbumsFallback implements AlbumsServiceClient {

    @Override
    public List<AlbumResponse> getAlbums(final String id) {
        return new ArrayList<>();
    }
}
package com.apps.photoapp.api.users.data;

import com.apps.photoapp.api.users.ui.model.AlbumResponse;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    List<AlbumResponse> getAlbums(@PathVariable final String id);
}


@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

    @Override
    public AlbumsServiceClient create(final Throwable throwable) {
        return new AlbumsServiceClientFallback(throwable);
    }
}

class AlbumsServiceClientFallback implements AlbumsServiceClient {

    private final Logger logger = LoggerFactory.getLogger(AlbumsServiceClientFallback.class);

    private final Throwable throwable;

    public AlbumsServiceClientFallback(final Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public List<AlbumResponse> getAlbums(final String id) {
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
            logger.error("404 error took place when getAlbums was called with userId: " + id + ". Error message: " + throwable
                    .getLocalizedMessage());
        } else {
            logger.error("Other error took place: " + throwable.getLocalizedMessage());
        }

        return new ArrayList<>();
    }
}
package org.slicer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;


@RestController
public class SlicerController {

    @Autowired
    SlicerService slicerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/slice", produces = "application/zip")
    public Mono<Void> createNewSlice(@RequestBody Slice slice, ServerHttpResponse response) throws IOException{
        ZeroCopyHttpOutputMessage zeroCopyHttpOutputMessage = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
        slicerService.serveSlice(slice);
        Resource resource = new ClassPathResource(SlicerUtils.ZIP_LOCATION.toString());
        File file = resource.getFile();
        return zeroCopyHttpOutputMessage.writeWith(file, 0, file.length());
    }
}

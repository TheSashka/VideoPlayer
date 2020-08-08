package com.wotn.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VideoController {

    @Value("${video.location}")
    private String videoLocation;

    @GetMapping("videos/{name}")
    public ResponseEntity<ResourceRegion> getFullVideo(@PathVariable String name, @RequestHeader HttpHeaders headers) throws IOException {
        UrlResource resource = new UrlResource("file:" + videoLocation + "/" + name);
        ResourceRegion region = resourceRegion(resource, headers);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
    }

    private ResourceRegion resourceRegion(UrlResource resource, HttpHeaders headers) throws IOException {
        HttpRange range = headers.getRange().stream().findFirst().orElse(null);
        long contentLength = resource.contentLength();
        final long chunkSize = 1000000L;
        if (range != null) {
            long rangeStart = range.getRangeStart(contentLength);
            long rangeEnd = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(chunkSize, rangeEnd - rangeStart + 1);
            return new ResourceRegion(resource, rangeStart, rangeLength);
        }
        else {
            long rangeLength = Math.min(chunkSize, contentLength);
            return new ResourceRegion(resource, 0, rangeLength);
        }
    }
}

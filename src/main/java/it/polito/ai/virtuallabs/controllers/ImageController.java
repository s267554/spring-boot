package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.entities.Image;
import it.polito.ai.virtuallabs.repositories.ImageDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    ImageDbRepository imageDbRepository;

    @PostMapping
    URI uploadImage(@RequestParam("file") MultipartFile multipartImage) throws Exception {
        Image dbImage = new Image();
        dbImage.setContent(multipartImage.getBytes());
        dbImage.setId(UUID.randomUUID().toString());

        String id = imageDbRepository.save(dbImage).getId();

        return WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder.methodOn(ImageController.class)
                                .downloadImage(id)
                ).toUri();
    }

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    ByteArrayResource downloadImage(@PathVariable String imageId) {
        byte[] image = imageDbRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();

        return new ByteArrayResource(image);
    }
}

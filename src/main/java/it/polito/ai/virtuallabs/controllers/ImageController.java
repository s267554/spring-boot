package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.entities.Image;
import it.polito.ai.virtuallabs.repositories.ImageDbRepository;
import it.polito.ai.virtuallabs.services.ImageService;
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

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public String uploadImage(@RequestParam("file") MultipartFile multipartImage) throws Exception {
        return imageService.uploadImage(multipartImage);
    }

    @GetMapping(value = "/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ByteArrayResource downloadImage(@PathVariable String imageId) {
        byte[] image = imageDbRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();

        return new ByteArrayResource(image);
    }
}

package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.controllers.ImageController;
import it.polito.ai.virtuallabs.entities.Image;
import it.polito.ai.virtuallabs.repositories.ImageDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class ImageServiceImpl implements ImageService{

    @Autowired
    ImageDbRepository imageDbRepository;

    @Override
    public String uploadImage(MultipartFile multipartImage) throws IOException {
        Image dbImage = new Image();
        dbImage.setContent(multipartImage.getBytes());
        dbImage.setId(UUID.randomUUID().toString());

        String id = imageDbRepository.save(dbImage).getId();

        return String.valueOf(WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder.methodOn(ImageController.class)
                                .downloadImage(id)
                ).toUri());
    }
}

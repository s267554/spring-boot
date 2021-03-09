package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDbRepository extends JpaRepository<Image, String> {}

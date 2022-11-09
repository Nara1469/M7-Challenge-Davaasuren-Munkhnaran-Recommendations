package com.company.musicstorerecommendations.repository;

import com.company.musicstorerecommendations.model.AlbumRecommendation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlbumRecommendationRepositoryTest {
    @Autowired
    AlbumRecommendationRepository albumRecommendationRepository;

    @Before
    public void setUp() throws Exception {
        albumRecommendationRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteAlbumRecommendation() {
        //Arrange
        AlbumRecommendation newAlbumRecommendation = new AlbumRecommendation();
        newAlbumRecommendation.setAlbumId(10);
        newAlbumRecommendation.setUserId(20);
        newAlbumRecommendation.setLiked(true);

        //Act
        newAlbumRecommendation = albumRecommendationRepository.save(newAlbumRecommendation);

        Optional<AlbumRecommendation> foundAlbumRecommendation = albumRecommendationRepository.findById(newAlbumRecommendation.getAlbumRecommendationId());

        //Assert
        assertTrue(foundAlbumRecommendation.isPresent());

        //Arrange
        newAlbumRecommendation.setAlbumId(30);
        newAlbumRecommendation.setUserId(40);
        newAlbumRecommendation.setLiked(false);

        //Act
        albumRecommendationRepository.save(newAlbumRecommendation);
        foundAlbumRecommendation = albumRecommendationRepository.findById(newAlbumRecommendation.getAlbumRecommendationId());

        //Assert
        assertTrue(foundAlbumRecommendation.isPresent());

        //Act
        albumRecommendationRepository.deleteById(newAlbumRecommendation.getAlbumRecommendationId());
        foundAlbumRecommendation = albumRecommendationRepository.findById(newAlbumRecommendation.getAlbumRecommendationId());

        //Assert
        assertFalse(foundAlbumRecommendation.isPresent());
    }

    @Test
    public void shouldFindAllAlbumRecommendation() {
        //Arrange
        AlbumRecommendation albumRecommendation1 = new AlbumRecommendation();
        albumRecommendation1.setAlbumId(1);
        albumRecommendation1.setUserId(2);
        albumRecommendation1.setLiked(true);


        AlbumRecommendation albumRecommendation2 = new AlbumRecommendation();
        albumRecommendation2.setAlbumId(1);
        albumRecommendation2.setUserId(3);
        albumRecommendation2.setLiked(true);

        //Act
        albumRecommendation1 = albumRecommendationRepository.save(albumRecommendation1);
        albumRecommendation2 = albumRecommendationRepository.save(albumRecommendation2);
        List<AlbumRecommendation> allAlbumRecommendation = new ArrayList();
        allAlbumRecommendation.add(albumRecommendation1);
        allAlbumRecommendation.add(albumRecommendation2);

        //Act
        List<AlbumRecommendation> foundAllAlbumRecommendation = albumRecommendationRepository.findAll();

        //Assert
        assertEquals(allAlbumRecommendation.size(), foundAllAlbumRecommendation.size());
    }
}
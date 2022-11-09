package com.company.musicstorerecommendations.repository;

import com.company.musicstorerecommendations.model.ArtistRecommendation;
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
public class ArtistRecommendationRepositoryTest {
    @Autowired
    ArtistRecommendationRepository artistRecommendationRepository;

    @Before
    public void setUp() throws Exception {
        artistRecommendationRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteArtistRecommendation() {
        //Arrange
        ArtistRecommendation newArtistRecommendation = new ArtistRecommendation();
        newArtistRecommendation.setArtistId(7);
        newArtistRecommendation.setUserId(6);
        newArtistRecommendation.setLiked(true);

        //Act
        newArtistRecommendation = artistRecommendationRepository.save(newArtistRecommendation);

        Optional<ArtistRecommendation> foundArtistRecommendation = artistRecommendationRepository.findById(newArtistRecommendation.getArtistRecommendationId());

        //Assert
        assertTrue(foundArtistRecommendation.isPresent());

        //Arrange
        newArtistRecommendation.setArtistId(8);
        newArtistRecommendation.setUserId(4);
        newArtistRecommendation.setLiked(false);

        //Act
        artistRecommendationRepository.save(newArtistRecommendation);
        foundArtistRecommendation = artistRecommendationRepository.findById(newArtistRecommendation.getArtistRecommendationId());

        //Assert
        assertTrue(foundArtistRecommendation.isPresent());

        //Act
        artistRecommendationRepository.deleteById(newArtistRecommendation.getArtistRecommendationId());
        foundArtistRecommendation = artistRecommendationRepository.findById(newArtistRecommendation.getArtistRecommendationId());

        //Assert
        assertFalse(foundArtistRecommendation.isPresent());
    }

    @Test
    public void shouldFindAllArtistRecommendation() {
        //Arrange
        ArtistRecommendation artistRecommendation1 = new ArtistRecommendation();
        artistRecommendation1.setArtistId(9);
        artistRecommendation1.setUserId(8);
        artistRecommendation1.setLiked(true);


        ArtistRecommendation artistRecommendation2 = new ArtistRecommendation();
        artistRecommendation2.setArtistId(9);
        artistRecommendation2.setUserId(9);
        artistRecommendation2.setLiked(true);

        //Act
        artistRecommendation1 = artistRecommendationRepository.save(artistRecommendation1);
        artistRecommendation2 = artistRecommendationRepository.save(artistRecommendation2);
        List<ArtistRecommendation> allArtistRecommendation = new ArrayList();
        allArtistRecommendation.add(artistRecommendation1);
        allArtistRecommendation.add(artistRecommendation2);

        //Act
        List<ArtistRecommendation> foundAllArtistRecommendation = artistRecommendationRepository.findAll();

        //Assert
        assertEquals(allArtistRecommendation.size(), foundAllArtistRecommendation.size());
    }
}
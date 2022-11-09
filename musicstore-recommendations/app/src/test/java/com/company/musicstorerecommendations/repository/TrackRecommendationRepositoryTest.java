package com.company.musicstorerecommendations.repository;

import com.company.musicstorerecommendations.model.TrackRecommendation;
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
public class TrackRecommendationRepositoryTest {
    @Autowired
    TrackRecommendationRepository trackRecommendationRepository;

    @Before
    public void setUp() throws Exception {
        trackRecommendationRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteTrackRecommendation() {
        //Arrange
        TrackRecommendation newTrackRecommendation = new TrackRecommendation();
        newTrackRecommendation.setTrackId(1);
        newTrackRecommendation.setUserId(2);
        newTrackRecommendation.setLiked(true);

        //Act
        newTrackRecommendation = trackRecommendationRepository.save(newTrackRecommendation);

        Optional<TrackRecommendation> foundTrackRecommendation = trackRecommendationRepository.findById(newTrackRecommendation.getTrackRecommendationId());

        //Assert
        assertTrue(foundTrackRecommendation.isPresent());

        //Arrange
        newTrackRecommendation.setTrackId(3);
        newTrackRecommendation.setUserId(4);
        newTrackRecommendation.setLiked(false);

        //Act
        trackRecommendationRepository.save(newTrackRecommendation);
        foundTrackRecommendation = trackRecommendationRepository.findById(newTrackRecommendation.getTrackRecommendationId());

        //Assert
        assertTrue(foundTrackRecommendation.isPresent());

        //Act
        trackRecommendationRepository.deleteById(newTrackRecommendation.getTrackRecommendationId());
        foundTrackRecommendation = trackRecommendationRepository.findById(newTrackRecommendation.getTrackRecommendationId());

        //Assert
        assertFalse(foundTrackRecommendation.isPresent());
    }

    @Test
    public void shouldFindAllTrackRecommendation() {
        //Arrange
        TrackRecommendation trackRecommendation1 = new TrackRecommendation();
        trackRecommendation1.setTrackId(10);
        trackRecommendation1.setUserId(20);
        trackRecommendation1.setLiked(true);


        TrackRecommendation trackRecommendation2 = new TrackRecommendation();
        trackRecommendation2.setTrackId(10);
        trackRecommendation2.setUserId(21);
        trackRecommendation2.setLiked(true);

        //Act
        trackRecommendation1 = trackRecommendationRepository.save(trackRecommendation1);
        trackRecommendation2 = trackRecommendationRepository.save(trackRecommendation2);
        List<TrackRecommendation> allTrackRecommendation = new ArrayList();
        allTrackRecommendation.add(trackRecommendation1);
        allTrackRecommendation.add(trackRecommendation2);

        //Act
        List<TrackRecommendation> foundAllTrackRecommendation = trackRecommendationRepository.findAll();

        //Assert
        assertEquals(allTrackRecommendation.size(), foundAllTrackRecommendation.size());
    }
}
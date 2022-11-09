package com.company.musicstorerecommendations.repository;

import com.company.musicstorerecommendations.model.LabelRecommendation;
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
public class LabelRecommendationRepositoryTest {
    @Autowired
    LabelRecommendationRepository labelRecommendationRepository;

    @Before
    public void setUp() throws Exception {
        labelRecommendationRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteLabelRecommendation() {
        //Arrange
        LabelRecommendation newLabelRecommendation = new LabelRecommendation();
        newLabelRecommendation.setLabelId(5);
        newLabelRecommendation.setUserId(10);
        newLabelRecommendation.setLiked(true);

        //Act
        newLabelRecommendation = labelRecommendationRepository.save(newLabelRecommendation);

        Optional<LabelRecommendation> foundLabelRecommendation = labelRecommendationRepository.findById(newLabelRecommendation.getLabelRecommendationId());

        //Assert
        assertTrue(foundLabelRecommendation.isPresent());

        //Arrange
        newLabelRecommendation.setLabelId(6);
        newLabelRecommendation.setUserId(11);
        newLabelRecommendation.setLiked(false);

        //Act
        labelRecommendationRepository.save(newLabelRecommendation);
        foundLabelRecommendation = labelRecommendationRepository.findById(newLabelRecommendation.getLabelRecommendationId());

        //Assert
        assertTrue(foundLabelRecommendation.isPresent());

        //Act
        labelRecommendationRepository.deleteById(newLabelRecommendation.getLabelRecommendationId());
        foundLabelRecommendation = labelRecommendationRepository.findById(newLabelRecommendation.getLabelRecommendationId());

        //Assert
        assertFalse(foundLabelRecommendation.isPresent());
    }

    @Test
    public void shouldFindAllLabelRecommendation() {
        //Arrange
        LabelRecommendation labelRecommendation1 = new LabelRecommendation();
        labelRecommendation1.setLabelId(2);
        labelRecommendation1.setUserId(4);
        labelRecommendation1.setLiked(true);


        LabelRecommendation labelRecommendation2 = new LabelRecommendation();
        labelRecommendation2.setLabelId(4);
        labelRecommendation2.setUserId(4);
        labelRecommendation2.setLiked(true);

        //Act
        labelRecommendation1 = labelRecommendationRepository.save(labelRecommendation1);
        labelRecommendation2 = labelRecommendationRepository.save(labelRecommendation2);
        List<LabelRecommendation> allLabelRecommendation = new ArrayList();
        allLabelRecommendation.add(labelRecommendation1);
        allLabelRecommendation.add(labelRecommendation2);

        //Act
        List<LabelRecommendation> foundAllLabelRecommendation = labelRecommendationRepository.findAll();

        //Assert
        assertEquals(allLabelRecommendation.size(), foundAllLabelRecommendation.size());
    }
}
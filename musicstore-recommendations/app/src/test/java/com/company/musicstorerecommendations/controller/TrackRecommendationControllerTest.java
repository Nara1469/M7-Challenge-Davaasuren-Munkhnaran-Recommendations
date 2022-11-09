package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.TrackRecommendation;
import com.company.musicstorerecommendations.repository.TrackRecommendationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackRecommendationController.class)
public class TrackRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackRecommendationRepository trackRecommendationRepository;

    @Autowired
    private ObjectMapper mapper;

    TrackRecommendation inputTrackRecommendation = new TrackRecommendation();
    TrackRecommendation outputTrackRecommendation = new TrackRecommendation();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputTrackRecommendation.setTrackId(10);
        inputTrackRecommendation.setUserId(20);
        inputTrackRecommendation.setLiked(true);

        inputJson = mapper.writeValueAsString(inputTrackRecommendation);

        outputTrackRecommendation.setTrackId(10);
        outputTrackRecommendation.setUserId(20);
        outputTrackRecommendation.setLiked(true);
        outputTrackRecommendation.setTrackRecommendationId(4);

        outputJson = mapper.writeValueAsString(outputTrackRecommendation);
    }

    @Test
    public void shouldAddTrackRecommendation() throws  Exception{
        doReturn(outputTrackRecommendation).when(trackRecommendationRepository).save(inputTrackRecommendation);

        //Act & Assert
        this.mockMvc.perform(post("/track-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllTrackRecommendations() throws Exception {
        List<TrackRecommendation> trackRecommendationList = new ArrayList<>();
        trackRecommendationList.add(outputTrackRecommendation);

        outputJson = mapper.writeValueAsString(trackRecommendationList);

        doReturn(trackRecommendationList).when(trackRecommendationRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/track-recommendation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getTrackRecommendationById() throws Exception {
        doReturn(Optional.of(outputTrackRecommendation)).when(trackRecommendationRepository).findById(4);

        //Act & Assert
        this.mockMvc.perform(get("/track-recommendation/4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateTrackRecommendation() throws Exception {
        doReturn(null).when(trackRecommendationRepository).save(outputTrackRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/track-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteTrackRecommendation() throws Exception {
        doReturn(Optional.ofNullable(outputTrackRecommendation)).when(trackRecommendationRepository).findById(4);

        //Act & Assert
        this.mockMvc.perform(delete("/track-recommendation/4"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentTrackRecommendationId() throws Exception{
        doReturn(Optional.empty()).when(trackRecommendationRepository).findById(404);

        mockMvc.perform(
                        get("/track-recommendation/404")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyTrackRecommendationTable() throws Exception{
        List<TrackRecommendation> emptyList = new ArrayList();

        doReturn(emptyList).when(trackRecommendationRepository).findAll();

        mockMvc.perform(
                        get("/track-recommendation")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateTrackRecommendationIdNotFound() throws Exception {
        TrackRecommendation anyTrackRecommendation = new TrackRecommendation();
        anyTrackRecommendation.setTrackId(5);
        anyTrackRecommendation.setUserId(20);
        anyTrackRecommendation.setLiked(true);
        anyTrackRecommendation.setTrackRecommendationId(404);

        doReturn(null).when(trackRecommendationRepository).save(anyTrackRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/track-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteTrackRecommendationIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(trackRecommendationRepository).findById(404);
        mockMvc.perform(
                        get("/track-recommendation/404")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddTrackRecommendationDataNotValid() throws Exception{
        TrackRecommendation newTrackRecommendation = new TrackRecommendation();
        newTrackRecommendation.setTrackId(null); // Track ID is required
        newTrackRecommendation.setUserId(null); // User ID is required
        newTrackRecommendation.setLiked(true);

        outputJson = mapper.writeValueAsString(newTrackRecommendation);

        doReturn(null).when(trackRecommendationRepository).save(newTrackRecommendation);

        mockMvc.perform(post("/track-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateTrackRecommendationDataNotValid() throws Exception{
        TrackRecommendation updatingTrackRecommendation = new TrackRecommendation();
        updatingTrackRecommendation.setTrackId(5);
        updatingTrackRecommendation.setUserId(20);
        updatingTrackRecommendation.setLiked(true);
        updatingTrackRecommendation.setTrackRecommendationId(2);

        doReturn(updatingTrackRecommendation).when(trackRecommendationRepository).save(updatingTrackRecommendation);

        updatingTrackRecommendation.setTrackId(null); // Track ID is required
        updatingTrackRecommendation.setUserId(null); // User ID is required

        outputJson = mapper.writeValueAsString(updatingTrackRecommendation);

        doReturn(null).when(trackRecommendationRepository).save(updatingTrackRecommendation);

        mockMvc.perform(put("/track-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
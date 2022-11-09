package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.ArtistRecommendation;
import com.company.musicstorerecommendations.repository.ArtistRecommendationRepository;
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
@WebMvcTest(ArtistRecommendationController.class)
public class ArtistRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistRecommendationRepository artistRecommendationRepository;

    @Autowired
    private ObjectMapper mapper;

    ArtistRecommendation inputArtistRecommendation = new ArtistRecommendation();
    ArtistRecommendation outputArtistRecommendation = new ArtistRecommendation();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputArtistRecommendation.setArtistId(4);
        inputArtistRecommendation.setUserId(6);
        inputArtistRecommendation.setLiked(true);

        inputJson = mapper.writeValueAsString(inputArtistRecommendation);

        outputArtistRecommendation.setArtistId(4);
        outputArtistRecommendation.setUserId(6);
        outputArtistRecommendation.setLiked(true);
        outputArtistRecommendation.setArtistRecommendationId(2);

        outputJson = mapper.writeValueAsString(outputArtistRecommendation);
    }

    @Test
    public void shouldAddArtistRecommendation() throws  Exception{
        doReturn(outputArtistRecommendation).when(artistRecommendationRepository).save(inputArtistRecommendation);

        //Act & Assert
        this.mockMvc.perform(post("/artist-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllArtistRecommendations() throws Exception {
        List<ArtistRecommendation> artistRecommendationList = new ArrayList<>();
        artistRecommendationList.add(outputArtistRecommendation);

        outputJson = mapper.writeValueAsString(artistRecommendationList);

        doReturn(artistRecommendationList).when(artistRecommendationRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/artist-recommendation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getArtistRecommendationById() throws Exception {
        doReturn(Optional.of(outputArtistRecommendation)).when(artistRecommendationRepository).findById(2);

        //Act & Assert
        this.mockMvc.perform(get("/artist-recommendation/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateArtistRecommendation() throws Exception {
        doReturn(null).when(artistRecommendationRepository).save(outputArtistRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/artist-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteArtistRecommendation() throws Exception {
        doReturn(Optional.ofNullable(outputArtistRecommendation)).when(artistRecommendationRepository).findById(2);

        //Act & Assert
        this.mockMvc.perform(delete("/artist-recommendation/2"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentArtistRecommendationId() throws Exception{
        doReturn(Optional.empty()).when(artistRecommendationRepository).findById(202);

        mockMvc.perform(
                        get("/artist-recommendation/202")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyArtistRecommendationTable() throws Exception{
        List<ArtistRecommendation> emptyList = new ArrayList();

        doReturn(emptyList).when(artistRecommendationRepository).findAll();

        mockMvc.perform(
                        get("/artist-recommendation")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateArtistRecommendationIdNotFound() throws Exception {
        ArtistRecommendation anyArtistRecommendation = new ArtistRecommendation();
        anyArtistRecommendation.setArtistId(5);
        anyArtistRecommendation.setUserId(20);
        anyArtistRecommendation.setLiked(true);
        anyArtistRecommendation.setArtistRecommendationId(202);

        doReturn(null).when(artistRecommendationRepository).save(anyArtistRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/artist-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteArtistRecommendationIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(artistRecommendationRepository).findById(202);
        mockMvc.perform(
                        get("/artist-recommendation/202")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddArtistRecommendationDataNotValid() throws Exception{
        ArtistRecommendation newArtistRecommendation = new ArtistRecommendation();
        newArtistRecommendation.setArtistId(null); // Artist ID is required
        newArtistRecommendation.setUserId(null); // User ID is required
        newArtistRecommendation.setLiked(true);

        outputJson = mapper.writeValueAsString(newArtistRecommendation);

        doReturn(null).when(artistRecommendationRepository).save(newArtistRecommendation);

        mockMvc.perform(post("/artist-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateArtistRecommendationDataNotValid() throws Exception{
        ArtistRecommendation updatingArtistRecommendation = new ArtistRecommendation();
        updatingArtistRecommendation.setArtistId(5);
        updatingArtistRecommendation.setUserId(20);
        updatingArtistRecommendation.setLiked(true);
        updatingArtistRecommendation.setArtistRecommendationId(2);

        doReturn(updatingArtistRecommendation).when(artistRecommendationRepository).save(updatingArtistRecommendation);

        updatingArtistRecommendation.setArtistId(null); // Artist ID is required
        updatingArtistRecommendation.setUserId(null); // User ID is required

        outputJson = mapper.writeValueAsString(updatingArtistRecommendation);

        doReturn(null).when(artistRecommendationRepository).save(updatingArtistRecommendation);

        mockMvc.perform(put("/artist-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
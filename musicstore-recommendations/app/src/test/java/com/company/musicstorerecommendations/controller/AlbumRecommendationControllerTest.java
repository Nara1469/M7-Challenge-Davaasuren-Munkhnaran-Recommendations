package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.AlbumRecommendation;
import com.company.musicstorerecommendations.repository.AlbumRecommendationRepository;
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
@WebMvcTest(AlbumRecommendationController.class)
public class AlbumRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumRecommendationRepository albumRecommendationRepository;

    @Autowired
    private ObjectMapper mapper;

    AlbumRecommendation inputAlbumRecommendation = new AlbumRecommendation();
    AlbumRecommendation outputAlbumRecommendation = new AlbumRecommendation();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputAlbumRecommendation.setAlbumId(3);
        inputAlbumRecommendation.setUserId(5);
        inputAlbumRecommendation.setLiked(true);

        inputJson = mapper.writeValueAsString(inputAlbumRecommendation);

        outputAlbumRecommendation.setAlbumId(3);
        outputAlbumRecommendation.setUserId(5);
        outputAlbumRecommendation.setLiked(true);
        outputAlbumRecommendation.setAlbumRecommendationId(1);

        outputJson = mapper.writeValueAsString(outputAlbumRecommendation);
    }

    @Test
    public void shouldAddAlbumRecommendation() throws  Exception{
        doReturn(outputAlbumRecommendation).when(albumRecommendationRepository).save(inputAlbumRecommendation);

        //Act & Assert
        this.mockMvc.perform(post("/album-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllAlbumRecommendations() throws Exception {
        List<AlbumRecommendation> albumRecommendationList = new ArrayList<>();
        albumRecommendationList.add(outputAlbumRecommendation);

        outputJson = mapper.writeValueAsString(albumRecommendationList);

        doReturn(albumRecommendationList).when(albumRecommendationRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/album-recommendation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getAlbumRecommendationById() throws Exception {
        doReturn(Optional.of(outputAlbumRecommendation)).when(albumRecommendationRepository).findById(1);

        //Act & Assert
        this.mockMvc.perform(get("/album-recommendation/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateAlbumRecommendation() throws Exception {
        doReturn(null).when(albumRecommendationRepository).save(outputAlbumRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/album-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteAlbumRecommendation() throws Exception {
        doReturn(Optional.ofNullable(outputAlbumRecommendation)).when(albumRecommendationRepository).findById(1);

        //Act & Assert
        this.mockMvc.perform(delete("/album-recommendation/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentAlbumRecommendationId() throws Exception{
        doReturn(Optional.empty()).when(albumRecommendationRepository).findById(101);

        mockMvc.perform(
                        get("/album-recommendation/101")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyAlbumRecommendationTable() throws Exception{
        List<AlbumRecommendation> emptyList = new ArrayList();

        doReturn(emptyList).when(albumRecommendationRepository).findAll();

        mockMvc.perform(
                        get("/album-recommendation")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateAlbumRecommendationIdNotFound() throws Exception {
        AlbumRecommendation anyAlbumRecommendation = new AlbumRecommendation();
        anyAlbumRecommendation.setAlbumId(5);
        anyAlbumRecommendation.setUserId(20);
        anyAlbumRecommendation.setLiked(true);
        anyAlbumRecommendation.setAlbumRecommendationId(101);

        doReturn(null).when(albumRecommendationRepository).save(anyAlbumRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/album-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteAlbumRecommendationIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(albumRecommendationRepository).findById(101);
        mockMvc.perform(
                        get("/album-recommendation/101")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddAlbumRecommendationDataNotValid() throws Exception{
        AlbumRecommendation newAlbumRecommendation = new AlbumRecommendation();
        newAlbumRecommendation.setAlbumId(null); // Album ID is required
        newAlbumRecommendation.setUserId(null); // User ID is required
        newAlbumRecommendation.setLiked(true);

        outputJson = mapper.writeValueAsString(newAlbumRecommendation);

        doReturn(null).when(albumRecommendationRepository).save(newAlbumRecommendation);

        mockMvc.perform(post("/album-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateAlbumRecommendationDataNotValid() throws Exception{
        AlbumRecommendation updatingAlbumRecommendation = new AlbumRecommendation();
        updatingAlbumRecommendation.setAlbumId(5);
        updatingAlbumRecommendation.setUserId(20);
        updatingAlbumRecommendation.setLiked(true);
        updatingAlbumRecommendation.setAlbumRecommendationId(2);

        doReturn(updatingAlbumRecommendation).when(albumRecommendationRepository).save(updatingAlbumRecommendation);

        updatingAlbumRecommendation.setAlbumId(null); // Album ID is required
        updatingAlbumRecommendation.setUserId(null); // User ID is required

        outputJson = mapper.writeValueAsString(updatingAlbumRecommendation);

        doReturn(null).when(albumRecommendationRepository).save(updatingAlbumRecommendation);

        mockMvc.perform(put("/album-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
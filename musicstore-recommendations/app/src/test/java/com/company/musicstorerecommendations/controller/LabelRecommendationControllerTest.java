package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.LabelRecommendation;
import com.company.musicstorerecommendations.repository.LabelRecommendationRepository;
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
@WebMvcTest(LabelRecommendationController.class)
public class LabelRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelRecommendationRepository labelRecommendationRepository;

    @Autowired
    private ObjectMapper mapper;

    LabelRecommendation inputLabelRecommendation = new LabelRecommendation();
    LabelRecommendation outputLabelRecommendation = new LabelRecommendation();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputLabelRecommendation.setLabelId(33);
        inputLabelRecommendation.setUserId(15);
        inputLabelRecommendation.setLiked(true);

        inputJson = mapper.writeValueAsString(inputLabelRecommendation);

        outputLabelRecommendation.setLabelId(33);
        outputLabelRecommendation.setUserId(15);
        outputLabelRecommendation.setLiked(true);
        outputLabelRecommendation.setLabelRecommendationId(3);

        outputJson = mapper.writeValueAsString(outputLabelRecommendation);
    }

    @Test
    public void shouldAddLabelRecommendation() throws  Exception{
        doReturn(outputLabelRecommendation).when(labelRecommendationRepository).save(inputLabelRecommendation);

        //Act & Assert
        this.mockMvc.perform(post("/label-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllLabelRecommendations() throws Exception {
        List<LabelRecommendation> labelRecommendationList = new ArrayList<>();
        labelRecommendationList.add(outputLabelRecommendation);

        outputJson = mapper.writeValueAsString(labelRecommendationList);

        doReturn(labelRecommendationList).when(labelRecommendationRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/label-recommendation"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getLabelRecommendationById() throws Exception {
        doReturn(Optional.of(outputLabelRecommendation)).when(labelRecommendationRepository).findById(3);

        //Act & Assert
        this.mockMvc.perform(get("/label-recommendation/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateLabelRecommendation() throws Exception {
        doReturn(null).when(labelRecommendationRepository).save(outputLabelRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/label-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteLabelRecommendation() throws Exception {
        doReturn(Optional.ofNullable(outputLabelRecommendation)).when(labelRecommendationRepository).findById(3);

        //Act & Assert
        this.mockMvc.perform(delete("/label-recommendation/3"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentLabelRecommendationId() throws Exception{
        doReturn(Optional.empty()).when(labelRecommendationRepository).findById(303);

        mockMvc.perform(
                        get("/label-recommendation/303")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyLabelRecommendationTable() throws Exception{
        List<LabelRecommendation> emptyList = new ArrayList();

        doReturn(emptyList).when(labelRecommendationRepository).findAll();

        mockMvc.perform(
                        get("/label-recommendation")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateLabelRecommendationIdNotFound() throws Exception {
        LabelRecommendation anyLabelRecommendation = new LabelRecommendation();
        anyLabelRecommendation.setLabelId(5);
        anyLabelRecommendation.setUserId(20);
        anyLabelRecommendation.setLiked(true);
        anyLabelRecommendation.setLabelRecommendationId(303);

        doReturn(null).when(labelRecommendationRepository).save(anyLabelRecommendation);

        //Act & Assert
        this.mockMvc.perform(put("/label-recommendation")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteLabelRecommendationIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(labelRecommendationRepository).findById(303);
        mockMvc.perform(
                        get("/label-recommendation/303")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddLabelRecommendationDataNotValid() throws Exception{
        LabelRecommendation newLabelRecommendation = new LabelRecommendation();
        newLabelRecommendation.setLabelId(null); // Label ID is required
        newLabelRecommendation.setUserId(null); // User ID is required
        newLabelRecommendation.setLiked(true);

        outputJson = mapper.writeValueAsString(newLabelRecommendation);

        doReturn(null).when(labelRecommendationRepository).save(newLabelRecommendation);

        mockMvc.perform(post("/label-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateLabelRecommendationDataNotValid() throws Exception{
        LabelRecommendation updatingLabelRecommendation = new LabelRecommendation();
        updatingLabelRecommendation.setLabelId(5);
        updatingLabelRecommendation.setUserId(20);
        updatingLabelRecommendation.setLiked(true);
        updatingLabelRecommendation.setLabelRecommendationId(2);

        doReturn(updatingLabelRecommendation).when(labelRecommendationRepository).save(updatingLabelRecommendation);

        updatingLabelRecommendation.setLabelId(null); // Label ID is required
        updatingLabelRecommendation.setUserId(null); // User ID is required

        outputJson = mapper.writeValueAsString(updatingLabelRecommendation);

        doReturn(null).when(labelRecommendationRepository).save(updatingLabelRecommendation);

        mockMvc.perform(put("/label-recommendation")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
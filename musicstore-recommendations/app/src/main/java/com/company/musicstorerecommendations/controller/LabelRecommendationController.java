package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.LabelRecommendation;
import com.company.musicstorerecommendations.repository.LabelRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/label-recommendation")
public class LabelRecommendationController {
    @Autowired
    LabelRecommendationRepository labelRecommendationRepository;

    @GetMapping()
    public List<LabelRecommendation> getLabelRecommendations() {
        List<LabelRecommendation> labelList = labelRecommendationRepository.findAll();
        if (labelList.isEmpty() || labelList == null) {
            throw new IllegalArgumentException("LabelRecommendations data is empty!");
        }
        return labelList; }

    @GetMapping("/{id}")
    public LabelRecommendation getLabelRecommendationById(@PathVariable Integer id) {
        Optional<LabelRecommendation> returnVal = labelRecommendationRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No LabelRecommendation was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public LabelRecommendation addLabelRecommendation(@RequestBody @Valid LabelRecommendation label) {
        if (label==null) throw new IllegalArgumentException("No LabelRecommendation data is added! LabelRecommendation object is null!");
        return labelRecommendationRepository.save(label);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabelRecommendation(@RequestBody @Valid LabelRecommendation label) {
        // validate incoming LabelRecommendation data
        if (label==null)
            throw new IllegalArgumentException("No LabelRecommendation data is passed! LabelRecommendation object is null!");

        //make sure the label exists. and if not, throw exception...
        if (label.getLabelRecommendationId()==null)
            throw new IllegalArgumentException("No such LabelRecommendation to update.");

        labelRecommendationRepository.save(label);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabelRecommendation(@PathVariable Integer id) {
        Optional<LabelRecommendation> label = labelRecommendationRepository.findById(id);
        if(label.isPresent()) {
            labelRecommendationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No LabelRecommendation was found with Id: " + id);
        }
    }
}

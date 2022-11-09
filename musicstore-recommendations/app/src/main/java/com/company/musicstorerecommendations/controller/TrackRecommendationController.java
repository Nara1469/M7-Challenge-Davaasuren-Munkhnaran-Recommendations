package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.TrackRecommendation;
import com.company.musicstorerecommendations.repository.TrackRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/track-recommendation")
public class TrackRecommendationController {
    @Autowired
    TrackRecommendationRepository trackRecommendationRepository;

    @GetMapping()
    public List<TrackRecommendation> getTrackRecommendations() {
        List<TrackRecommendation> trackList = trackRecommendationRepository.findAll();
        if (trackList.isEmpty() || trackList == null) {
            throw new IllegalArgumentException("TrackRecommendations data is empty!");
        }
        return trackList; }

    @GetMapping("/{id}")
    public TrackRecommendation getTrackRecommendationById(@PathVariable Integer id) {
        Optional<TrackRecommendation> returnVal = trackRecommendationRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No TrackRecommendation was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TrackRecommendation addTrackRecommendation(@RequestBody @Valid TrackRecommendation track) {
        if (track==null) throw new IllegalArgumentException("No TrackRecommendation data is added! TrackRecommendation object is null!");
        return trackRecommendationRepository.save(track);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrackRecommendation(@RequestBody @Valid TrackRecommendation track) {
        // validate incoming TrackRecommendation data
        if (track==null)
            throw new IllegalArgumentException("No TrackRecommendation data is passed! TrackRecommendation object is null!");

        //make sure the track exists. and if not, throw exception...
        if (track.getTrackRecommendationId()==null)
            throw new IllegalArgumentException("No such TrackRecommendation to update.");

        trackRecommendationRepository.save(track);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrackRecommendation(@PathVariable Integer id) {
        Optional<TrackRecommendation> track = trackRecommendationRepository.findById(id);
        if(track.isPresent()) {
            trackRecommendationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No TrackRecommendation was found with Id: " + id);
        }
    }
}

package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.ArtistRecommendation;
import com.company.musicstorerecommendations.repository.ArtistRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artist-recommendation")
public class ArtistRecommendationController {
    @Autowired
    ArtistRecommendationRepository artistRecommendationRepository;

    @GetMapping()
    public List<ArtistRecommendation> getArtistRecommendations() {
        List<ArtistRecommendation> artistList = artistRecommendationRepository.findAll();
        if (artistList.isEmpty() || artistList == null) {
            throw new IllegalArgumentException("ArtistRecommendations data is empty!");
        }
        return artistList; }

    @GetMapping("/{id}")
    public ArtistRecommendation getArtistRecommendationById(@PathVariable Integer id) {
        Optional<ArtistRecommendation> returnVal = artistRecommendationRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No ArtistRecommendation was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistRecommendation addArtistRecommendation(@RequestBody @Valid ArtistRecommendation artist) {
        if (artist==null) throw new IllegalArgumentException("No ArtistRecommendation data is added! ArtistRecommendation object is null!");
        return artistRecommendationRepository.save(artist);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtistRecommendation(@RequestBody @Valid ArtistRecommendation artist) {
        // validate incoming ArtistRecommendation data
        if (artist==null)
            throw new IllegalArgumentException("No ArtistRecommendation data is passed! ArtistRecommendation object is null!");

        //make sure the artist exists. and if not, throw exception...
        if (artist.getArtistRecommendationId()==null)
            throw new IllegalArgumentException("No such ArtistRecommendation to update.");

        artistRecommendationRepository.save(artist);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtistRecommendation(@PathVariable Integer id) {
        Optional<ArtistRecommendation> artist = artistRecommendationRepository.findById(id);
        if(artist.isPresent()) {
            artistRecommendationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No ArtistRecommendation was found with Id: " + id);
        }
    }
}

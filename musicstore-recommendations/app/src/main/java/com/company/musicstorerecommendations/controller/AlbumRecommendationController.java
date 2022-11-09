package com.company.musicstorerecommendations.controller;

import com.company.musicstorerecommendations.model.AlbumRecommendation;
import com.company.musicstorerecommendations.repository.AlbumRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/album-recommendation")
public class AlbumRecommendationController {
    @Autowired
    AlbumRecommendationRepository albumRecommendationRepository;

    @GetMapping()
    public List<AlbumRecommendation> getAlbumRecommendations() {
        List<AlbumRecommendation> albumList = albumRecommendationRepository.findAll();
        if (albumList.isEmpty() || albumList == null) {
            throw new IllegalArgumentException("AlbumRecommendations data is empty!");
        }
        return albumList; }

    @GetMapping("/{id}")
    public AlbumRecommendation getAlbumRecommendationById(@PathVariable Integer id) {
        Optional<AlbumRecommendation> returnVal = albumRecommendationRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No AlbumRecommendation was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumRecommendation addAlbumRecommendation(@RequestBody @Valid AlbumRecommendation album) {
        if (album==null) throw new IllegalArgumentException("No AlbumRecommendation data is added! AlbumRecommendation object is null!");
        return albumRecommendationRepository.save(album);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbumRecommendation(@RequestBody @Valid AlbumRecommendation album) {
        // validate incoming AlbumRecommendation data
        if (album==null)
            throw new IllegalArgumentException("No AlbumRecommendation data is passed! AlbumRecommendation object is null!");

        //make sure the album exists. and if not, throw exception...
        if (album.getAlbumRecommendationId()==null)
            throw new IllegalArgumentException("No such AlbumRecommendation to update.");

        albumRecommendationRepository.save(album);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbumRecommendation(@PathVariable Integer id) {
        Optional<AlbumRecommendation> album = albumRecommendationRepository.findById(id);
        if(album.isPresent()) {
            albumRecommendationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No AlbumRecommendation was found with Id: " + id);
        }
    }
}

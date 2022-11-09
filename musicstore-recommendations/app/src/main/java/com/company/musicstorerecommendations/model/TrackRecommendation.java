package com.company.musicstorerecommendations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "track_recommendation")
public class TrackRecommendation {
    @Id
    @Column(name = "track_recommendation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trackRecommendationId;
    @NotNull(message = "Track ID is required.")
    @Column(name = "track_id")
    private Integer trackId;
    @NotNull(message = "User ID is required.")
    @Column(name = "user_id")
    private Integer userId;
    private boolean liked;

    public TrackRecommendation() {}

    public Integer getTrackRecommendationId() {
        return trackRecommendationId;
    }

    public void setTrackRecommendationId(Integer trackRecommendationId) {
        this.trackRecommendationId = trackRecommendationId;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackRecommendation that = (TrackRecommendation) o;
        return liked == that.liked && Objects.equals(trackRecommendationId, that.trackRecommendationId) && Objects.equals(trackId, that.trackId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackRecommendationId, trackId, userId, liked);
    }

    @Override
    public String toString() {
        return "TrackRecommendation{" +
                "trackRecommendationId=" + trackRecommendationId +
                ", trackId=" + trackId +
                ", userId=" + userId +
                ", liked=" + liked +
                '}';
    }
}

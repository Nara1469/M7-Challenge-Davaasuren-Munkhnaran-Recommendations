package com.company.musicstorerecommendations.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "label_recommendation")
public class LabelRecommendation {
    @Id
    @Column(name = "label_recommendation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer labelRecommendationId;
    @NotNull(message = "Label ID is required.")
    @Column(name = "label_id")
    private Integer labelId;
    @NotNull(message = "User ID is required.")
    @Column(name = "user_id")
    private Integer userId;
    private boolean liked;

    public LabelRecommendation() {}

    public Integer getLabelRecommendationId() {
        return labelRecommendationId;
    }

    public void setLabelRecommendationId(Integer labelRecommendationId) {
        this.labelRecommendationId = labelRecommendationId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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
        LabelRecommendation that = (LabelRecommendation) o;
        return liked == that.liked && Objects.equals(labelRecommendationId, that.labelRecommendationId) && Objects.equals(labelId, that.labelId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelRecommendationId, labelId, userId, liked);
    }

    @Override
    public String toString() {
        return "LabelRecommendation{" +
                "labelRecommendationId=" + labelRecommendationId +
                ", labelId=" + labelId +
                ", userId=" + userId +
                ", liked=" + liked +
                '}';
    }
}

package softuni.bg.reviews1.service;

import softuni.bg.reviews1.model.dto.AddReviewDto;
import softuni.bg.reviews1.model.dto.ViewReviewDto;

import java.util.List;

public interface ReviewService {
    void addReview(AddReviewDto addReviewDto);

    List<ViewReviewDto> getAllReviews(Long productId);

    void deleteReview(Long reviewId);
}

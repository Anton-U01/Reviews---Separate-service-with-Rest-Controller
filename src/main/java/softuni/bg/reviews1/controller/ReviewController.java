package softuni.bg.reviews1.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import softuni.bg.reviews1.model.dto.AddReviewDto;
import softuni.bg.reviews1.model.dto.ViewReviewDto;
import softuni.bg.reviews1.service.ReviewService;

import java.util.List;


@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ViewReviewDto>> viewReviews(@PathVariable("id") Long productId){
        List<ViewReviewDto> allReviews = reviewService.getAllReviews(productId);
        return ResponseEntity.ok(allReviews);
    }

    @PostMapping
    public ResponseEntity<?> addReview(@Valid @RequestBody AddReviewDto addReviewDto,
                                                   BindingResult bindingResult
                                                   ){
            if(bindingResult.hasErrors()){
                return ResponseEntity.badRequest().build();
            }
            reviewService.addReview(addReviewDto);
            return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }


}

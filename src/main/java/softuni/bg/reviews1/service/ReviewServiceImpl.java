package softuni.bg.reviews1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.reviews1.exceptions.ProductNotFoundException;
import softuni.bg.reviews1.exceptions.UserNotFoundException;
import softuni.bg.reviews1.model.Product;
import softuni.bg.reviews1.model.Review;
import softuni.bg.reviews1.model.User;
import softuni.bg.reviews1.model.dto.AddReviewDto;
import softuni.bg.reviews1.model.dto.ViewReviewDto;
import softuni.bg.reviews1.repository.ProductRepository;
import softuni.bg.reviews1.repository.ReviewRepository;
import softuni.bg.reviews1.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addReview(AddReviewDto addReviewDto) {
        Review review = new Review();
        review.setContent(addReviewDto.getContent());
        review.setCreatedOn(LocalDateTime.now());
        User author = userRepository.findByUsername(addReviewDto.getAuthorUsername())
                .orElseThrow(()-> new UserNotFoundException("User with username " + addReviewDto.getAuthorUsername() + " is not found!"));
        Product product = productRepository.findById(addReviewDto.getProduct())
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + addReviewDto.getProduct() + " is not found!"));
        review.setAuthor(author);
        review.setProduct(product);

        reviewRepository.saveAndFlush(review);
    }

    @Override
    @Transactional
    public List<ViewReviewDto> getAllReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product with id " + productId + " is not found!"));

        List<ViewReviewDto> allReviews = product.getReviews().stream().map(r -> {

                            ViewReviewDto viewDto = new ViewReviewDto();
                            viewDto.setAuthor(r.getAuthor().getFullName());
                            viewDto.setContent(r.getContent());
                            viewDto.setCreatedOn(r.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            return viewDto;
                        }
                )
                .collect(Collectors.toList());
        return allReviews;
    }

    @Override
    public void deleteReview(Long reviewId) {
        this.reviewRepository.deleteById(reviewId);
    }
}

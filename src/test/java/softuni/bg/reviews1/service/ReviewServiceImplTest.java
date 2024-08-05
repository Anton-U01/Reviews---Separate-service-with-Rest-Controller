package softuni.bg.reviews1.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {
    @InjectMocks
    private ReviewServiceImpl reviewService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Captor
    private ArgumentCaptor<Review> reviewCaptor;
    private AddReviewDto addReviewDto;
    private User author;
    private Review review;
    private Product product;
    @BeforeEach
    void init(){
        addReviewDto = new AddReviewDto();
        addReviewDto.setContent("content content content");
        addReviewDto.setAuthorUsername("test");
        addReviewDto.setProduct(1L);
        author = new User();
        author.setUsername("test");
        author.setFullName("test testov");

        product = new Product();
        product.setName("product");

        review = new Review();
        review.setAuthor(author);
        review.setContent("content content content");
        review.setProduct(product);
        review.setCreatedOn(LocalDateTime.now());

    }

    @Test
    void addReviewUserNotFoundTest(){
        when(userRepository.findByUsername(addReviewDto.getAuthorUsername()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, ()-> {
            reviewService.addReview(addReviewDto);
        });

        assertEquals("User with username " + addReviewDto.getAuthorUsername() + " is not found!",exception.getMessage());

    }
    @Test
    void addReviewProductNotFoundTest(){
        when(userRepository.findByUsername(addReviewDto.getAuthorUsername()))
                .thenReturn(Optional.of(author));
        when(productRepository.findById(addReviewDto.getProduct()))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = Assertions.assertThrows(ProductNotFoundException.class, ()-> {
            reviewService.addReview(addReviewDto);
        });

        assertEquals("Product with id " + addReviewDto.getProduct() + " is not found!",exception.getMessage());

    }
    @Test
    void addReviewTest(){
        when(userRepository.findByUsername(addReviewDto.getAuthorUsername()))
                .thenReturn(Optional.of(author));
        when(productRepository.findById(addReviewDto.getProduct()))
                .thenReturn(Optional.of(product));

        reviewService.addReview(addReviewDto);

        verify(reviewRepository).saveAndFlush(reviewCaptor.capture());
        Review actual = reviewCaptor.getValue();
        Assertions.assertEquals(review.getContent(),actual.getContent());
        Assertions.assertEquals(review.getProduct().getName(),actual.getProduct().getName());
        Assertions.assertEquals(review.getAuthor(),actual.getAuthor());

    }
    @Test
    void getAllReviewsProductNotFoundTest(){
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception = Assertions.assertThrows(ProductNotFoundException.class, ()-> {
            reviewService.getAllReviews(1L);
        });

        assertEquals("Product with id " + 1L + " is not found!",exception.getMessage());

    }
    @Test
    void getAllReviewsTest(){
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        product.setReviews(reviews);

        List<ViewReviewDto> allReviews = reviewService.getAllReviews(1L);

        Assertions.assertEquals(1,allReviews.size());
        Assertions.assertEquals("test testov",allReviews.getFirst().getAuthor());
        Assertions.assertEquals("content content content",allReviews.getFirst().getContent());
    }
    @Test
    void deleteReviewTest(){
        Long reviewId = 1L;

        reviewService.deleteReview(reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }
}

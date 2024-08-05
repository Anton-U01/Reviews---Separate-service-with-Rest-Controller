package softuni.bg.reviews1.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import softuni.bg.reviews1.model.dto.AddReviewDto;
import softuni.bg.reviews1.model.dto.ViewReviewDto;
import softuni.bg.reviews1.service.ReviewServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ReviewControllerIT {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ReviewServiceImpl reviewService;

    @Test
    void viewReviewsTest() throws Exception {
        Long productId = 1L;
        List<ViewReviewDto> reviews = new ArrayList<>();
        reviews.add(new ViewReviewDto());
        when(reviewService.getAllReviews(productId)).thenReturn(reviews);

        mockMvc.perform(get("/reviews/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

    }
    @Test
    void addReviewTest() throws Exception {

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"content\":\"Content content content\"}"))

                .andExpect(status().isOk());

    }
    @Test
    void addReviewFailTest() throws Exception {
        BindingResult bindingResult = Mockito.mock();
        when(bindingResult.hasErrors())
                .thenReturn(true);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"content\":\"\"}"))
                .andExpect(status().isBadRequest());

    }
    @Test
    void testDeleteReview() throws Exception {
        Long reviewId = 1L;

        mockMvc.perform(delete("/reviews/{id}", reviewId))
                .andExpect(status().isOk());

    }

}

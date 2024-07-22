package softuni.bg.reviews1.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class AddReviewDto {
    @NotBlank
    @Size(min = 15,max = 150)
    private String content;
    private String authorUsername;
    private long product;

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

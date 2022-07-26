package com.project.shopping.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Review_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private Product productId;

    @Column(nullable = false, length=255)
    private String title;

    @Lob
    private String content;

    @Column(nullable = true, length=255)
    private String imageUrl;

    @Builder
    public Review(User userId, Product productId, String title, String content, String imageUrl) {
        this.userId = userId;
        this.productId = productId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}

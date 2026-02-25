package poly.edu.models.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Entity	
@Getter
@Setter
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name", length = 50)
    @NotBlank
    private String name; //use not blank for string

    @Column(name = "Image")
    @NotBlank
    private String image;

    @Column(name = "Price")
    @Positive
    private Double price; //use positive for double or integer (price > 0)
    
    @Column(name = "Amount")
    @Positive
    private Integer amount; //use positive for double or integer (price > 0)

    @Column(name = "CreateDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate = LocalDate.now();

    @Column(name = "Available")
    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;
}

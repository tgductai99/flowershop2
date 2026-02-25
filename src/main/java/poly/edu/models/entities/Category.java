package poly.edu.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Categories")
public class Category {
	
	@Id
    @Column(name = "Id", length = 4)
	@NotBlank
	private String id;
	
    @Column(name = "Name", length = 50)
    @NotBlank
	private String name;
    
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}

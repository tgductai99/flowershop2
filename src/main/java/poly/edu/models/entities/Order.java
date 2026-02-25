package poly.edu.models.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "CreateDate")
    private LocalDate createDate = LocalDate.now();

    @Column(name = "Status")
    private String status = "PENDING";
    
    @Column(name = "Address", length = 255)
    private String address;
    
    @ManyToOne
    @JoinColumn(name = "Username", nullable = false)
    private Account account;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) //auto save children when parents being saved, allow orderdetail item deletion
    private List<OrderDetail> orderDetails = new ArrayList<>();
    
}


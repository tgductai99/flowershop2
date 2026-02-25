package poly.edu.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @Column(name = "Username", length = 50)
    @NotBlank(message="username can not be blank")
    private String username;
    
    @Column(name = "Password", length = 50)
    @NotBlank(message="password can not be blank")
    private String password;

    @Column(name = "Fullname", length = 50)
    @NotBlank
    private String fullname;

    @Column(name = "Email", length = 50)
    @NotBlank
    private String email;

    @Column(name = "Photo")
    private String photo;
    
    @Column(name = "Address", length = 255)
    private String address;
    
    @Column(name = "Phone", length = 10)
    private String phone;

    @Column(name = "Activated")
    private Boolean activated = true;

    @Column(name = "Admin")
    private Boolean admin = false;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;
}


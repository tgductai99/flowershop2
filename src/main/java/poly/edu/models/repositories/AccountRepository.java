package poly.edu.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.models.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
	
}

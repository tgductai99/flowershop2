package poly.edu.models.services;

import java.util.List;

import poly.edu.models.entities.Account;

public interface AccountServices {

    void save(Account account);
    
    Account delete(String username);    
    
    List<Account> findAll();
    
    Account findById(String username);
    
    List<Account> filter(String keyword, Boolean activated, Boolean admin);
}

package poly.edu.models.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.models.entities.Account;
import poly.edu.models.repositories.AccountRepository;
import poly.edu.models.services.AccountServices;

@Service
public class AccountServiceImpl implements AccountServices {

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public Account findById(String username) {
		// TODO Auto-generated method stub
		return accountRepository.findById(username).orElse(null);
	}

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return accountRepository.findAll();
	}

	@Override
	public void save(Account account) {
		// TODO Auto-generated method stub
		accountRepository.save(account);
	}

	@Override
	public Account delete(String username) {
		// TODO Auto-generated method stub
		accountRepository.deleteById(username);
		return accountRepository.findById(username).orElse(null);
	}

	@Override
	public List<Account> filter(String keyword, Boolean activated, Boolean admin) {
		// TODO Auto-generated method stub
		
		List<Account> accounts = accountRepository.findAll();
		
		return accounts.stream()
		        .filter(a -> {
		            if (keyword != null && !keyword.isBlank()) {
		                boolean matchUsername = a.getUsername() != null && a.getUsername().toLowerCase().contains(keyword.toLowerCase());
		                boolean matchFullname = a.getFullname() != null && a.getFullname().toLowerCase().contains(keyword.toLowerCase());
		                if (!matchUsername && !matchFullname) {
		                    return false;
		                }
		            }

		            if (activated != null) {
		                if (!activated.equals(a.getActivated())) {
		                    return false;
		                }
		            }

		            if (admin != null) {
		                if (!admin.equals(a.getAdmin())) {
		                    return false;
		                }
		            }

		            return true;
		        }).toList();
	}


}

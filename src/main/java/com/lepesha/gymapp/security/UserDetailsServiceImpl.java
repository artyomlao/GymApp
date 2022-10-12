package com.lepesha.gymapp.security;

import com.lepesha.gymapp.model.Person;
import com.lepesha.gymapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public UserDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        Person person = personRepository.findByEmail(email).
                orElseThrow(()-> {
                    System.out.println("такого нет!");
                    return new UsernameNotFoundException("User doesn't exists");
                });
        return SecurityUser.fromUser(person);
    }
}

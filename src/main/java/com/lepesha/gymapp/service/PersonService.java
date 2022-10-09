package com.lepesha.gymapp.service;

import com.lepesha.gymapp.dto.PersonDTO;
import com.lepesha.gymapp.exception.UserAlreadyExistsException;
import com.lepesha.gymapp.model.Person;
import com.lepesha.gymapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person registerNewPersonAccount(PersonDTO personDTO) throws UserAlreadyExistsException {
        if(existsEmail(personDTO.getEmail())==true){
            throw new UserAlreadyExistsException("Account with this email already exists");
        }

        Person person = new Person();
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setEmail(personDTO.getEmail());
        person.setAge(personDTO.getAge());
        person.setPassword(personDTO.getPassword());
        //person.setRoles(Arrays.asList("ROLE_USER"));

        return personRepository.save(person);
    }

    private boolean existsEmail(String email){
        return personRepository.findByEmail(email) !=null;
    }
}

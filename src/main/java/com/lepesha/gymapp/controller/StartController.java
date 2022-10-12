package com.lepesha.gymapp.controller;

import com.lepesha.gymapp.dto.AuthenticationRequestDTO;
import com.lepesha.gymapp.dto.PersonDTO;
import com.lepesha.gymapp.model.Offer;
import com.lepesha.gymapp.model.Person;
import com.lepesha.gymapp.repository.PersonRepository;
import com.lepesha.gymapp.security.JwtTokenProvider;
import com.lepesha.gymapp.service.OfferService;
import com.lepesha.gymapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gym")
public class StartController {
    private final OfferService offerService;
    private final PersonService personService;
    private final AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private final PersonRepository personRepository;

    @Autowired
    public StartController(OfferService offerService, PersonService personService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PersonRepository personRepository) {
        this.offerService = offerService;
        this.personService = personService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.personRepository = personRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('developers:read')")
    public String startWindow(){
//       ModelAndView modelAndView = new ModelAndView();
//       modelAndView.setViewName("start");
//       return modelAndView;
        return "start";
    }

    @GetMapping("/auth/login")
    public String authentication() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("person", new PersonDTO());
        System.out.println("регистрация гет");
        return "reg";
    }

    @ResponseBody
    @GetMapping("/offers")
    @PreAuthorize("hasAuthority('developers:write')")
    public List<Offer> getOffers(){
        return offerService.getGymOffers();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<? extends Object> registration(@ModelAttribute("request") AuthenticationRequestDTO request){
        try {
            System.out.println(request.getEmail() + " " + request.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Person person = personRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            String token = jwtTokenProvider.generateToken(request.getEmail(), person.getRole().name());
            Map<Object, Object> response = new HashMap<>();

            response.put("email", request.getEmail());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }
}

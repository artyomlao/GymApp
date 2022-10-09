package com.lepesha.gymapp.controller;

import com.lepesha.gymapp.dto.PersonDTO;
import com.lepesha.gymapp.exception.UserAlreadyExistsException;
import com.lepesha.gymapp.model.Offer;
import com.lepesha.gymapp.service.OfferService;
import com.lepesha.gymapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/gym")
public class StartController {
    private final OfferService offerService;

    private final PersonService personService;

    @Autowired
    public StartController(OfferService offerService, PersonService personService) {
        this.offerService = offerService;
        this.personService = personService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('developers:read')")
    public String startWindow(){
//       ModelAndView modelAndView = new ModelAndView();
//       modelAndView.setViewName("start");
//       return modelAndView;
        return "start";
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

    @PostMapping("/registration")
    public ModelAndView registration(@ModelAttribute("person") @Valid PersonDTO personDTO, Errors errors){
        System.out.println("регистрация пост");
        if(errors.hasErrors()) {
            return new ModelAndView("reg");
        }

        try {
            personService.registerNewPersonAccount(personDTO);
        } catch (UserAlreadyExistsException e) {
            ModelAndView modelAndView = new ModelAndView("reg");
            modelAndView.addObject("message", "Account with this email already exists");
            return modelAndView;
        }

        return new ModelAndView("start");
    }
}

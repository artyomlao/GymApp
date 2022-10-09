package com.lepesha.gymapp.service;

import com.lepesha.gymapp.model.Offer;
import com.lepesha.gymapp.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {
    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> getGymOffers(){
        return offerRepository.findAll();
    }
}

package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.Category;
import com.pa.asvblrapi.entity.PaymentMode;
import com.pa.asvblrapi.entity.Season;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.exception.CategoryNotFoundException;
import com.pa.asvblrapi.exception.PaymentModeNotFoundException;
import com.pa.asvblrapi.exception.SeasonNotFoundException;
import com.pa.asvblrapi.exception.SubscriptionNotFoundException;
import com.pa.asvblrapi.repository.CategoryRepository;
import com.pa.asvblrapi.repository.PaymentModeRepository;
import com.pa.asvblrapi.repository.SeasonRepository;
import com.pa.asvblrapi.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    public List<Subscription> getAllSubscriptions() {
        return this.subscriptionRepository.findAll();
    }

    public List<Subscription> getSubscriptionsBySeason(Long id ) {
        return this.subscriptionRepository.findSubscriptionBySeason(id);
    }

    public Optional<Subscription> getSubscription(Long id) {
        return this.subscriptionRepository.findById(id);
    }

    public Subscription createSubscription(SubscriptionDto subscriptionDto) throws SeasonNotFoundException, CategoryNotFoundException, PaymentModeNotFoundException{
        Optional<Season> season = this.seasonRepository.findById(subscriptionDto.getIdSeason());
        if(!season.isPresent()) {
            throw new SeasonNotFoundException(subscriptionDto.getIdSeason());
        }

        Optional<Category> category = this.categoryRepository.findById(subscriptionDto.getIdCategory());
        if(!category.isPresent()) {
            throw new CategoryNotFoundException(subscriptionDto.getIdCategory());
        }

        Optional<PaymentMode> paymentMode = this.paymentModeRepository.findById(subscriptionDto.getIdPaymentMode());
        if(!paymentMode.isPresent()) {
            throw new PaymentModeNotFoundException(subscriptionDto.getIdPaymentMode());
        }

        Subscription subscription = new Subscription(
                subscriptionDto.getFirstName(),
                subscriptionDto.getLastName(),
                subscriptionDto.isGender(),
                subscriptionDto.getAddress(),
                subscriptionDto.getPostcode(),
                subscriptionDto.getCity(),
                subscriptionDto.getEmail(),
                subscriptionDto.getPhoneNumber(),
                subscriptionDto.getBirthDate(),
                subscriptionDto.getBirthCountry(),
                subscriptionDto.getTopSize(),
                subscriptionDto.getPantsSize(),
                subscriptionDto.isInsurance(),
                subscriptionDto.isEquipment(),
                subscriptionDto.isReferee(),
                subscriptionDto.isCoach(),
                season.get(),
                category.get(),
                paymentMode.get()
        );
        return this.subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, SubscriptionDto subscriptionDto) throws SubscriptionNotFoundException,
            SeasonNotFoundException, CategoryNotFoundException, PaymentModeNotFoundException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if(!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        Optional<Season> season = this.seasonRepository.findById(subscriptionDto.getIdSeason());
        if(!season.isPresent()) {
            throw new SeasonNotFoundException(subscriptionDto.getIdSeason());
        }
        Optional<Category> category = this.categoryRepository.findById(subscriptionDto.getIdCategory());
        if(!category.isPresent()) {
            throw new CategoryNotFoundException(subscriptionDto.getIdCategory());
        }
        Optional<PaymentMode> paymentMode = this.paymentModeRepository.findById(subscriptionDto.getIdPaymentMode());
        if(!paymentMode.isPresent()) {
            throw new PaymentModeNotFoundException(subscriptionDto.getIdPaymentMode());
        }
        subscription.get().setFirstName(subscriptionDto.getFirstName());
        subscription.get().setLastName(subscriptionDto.getLastName());
        subscription.get().setGender(subscriptionDto.isGender());
        subscription.get().setAddress(subscriptionDto.getAddress());
        subscription.get().setPostcode(subscriptionDto.getPostcode());
        subscription.get().setCity(subscriptionDto.getCity());
        subscription.get().setEmail(subscriptionDto.getEmail());
        subscription.get().setPhoneNumber(subscriptionDto.getPhoneNumber());
        subscription.get().setBirthDate(subscriptionDto.getBirthDate());
        subscription.get().setBirthCountry(subscriptionDto.getBirthCountry());
        subscription.get().setTopSize(subscriptionDto.getTopSize());
        subscription.get().setPantsSize(subscriptionDto.getPantsSize());
        subscription.get().setInsuranceRequested(subscriptionDto.isInsurance());
        subscription.get().setEquipment(subscriptionDto.isEquipment());
        subscription.get().setReferee(subscriptionDto.isReferee());
        subscription.get().setCoach(subscriptionDto.isCoach());
        subscription.get().setSeason(season.get());
        subscription.get().setCategory(category.get());
        subscription.get().setPaymentMode(paymentMode.get());
        return this.subscriptionRepository.save(subscription.get());
    }

    public void updateSubscription(Subscription subscription) {
        this.subscriptionRepository.save(subscription);
    }

    public void deleteSubscription(Long id) throws SubscriptionNotFoundException, AccessDeniedException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);

        if(!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        this.subscriptionRepository.delete(subscription.get());
    }
}

package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
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

    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

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

        Optional<ClothingSize> topSize = this.clothingSizeRepository.findById(subscriptionDto.getIdTopSize());
        if(!topSize.isPresent()) {
            throw new ClothingSizeNotFoundException(subscriptionDto.getIdTopSize());
        }
        Optional<ClothingSize> pantsSize = this.clothingSizeRepository.findById(subscriptionDto.getIdPantsSize());
        if(!pantsSize.isPresent()) {
            throw new ClothingSizeNotFoundException(subscriptionDto.getIdPantsSize());
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
                subscriptionDto.getNationality(),
                topSize.get(),
                pantsSize.get(),
                subscriptionDto.isInsuranceRequested(),
                subscriptionDto.isEquipment(),
                subscriptionDto.isReferee(),
                subscriptionDto.isCoach(),
                subscriptionDto.getComment(),
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
        Optional<ClothingSize> topSize = this.clothingSizeRepository.findById(subscriptionDto.getIdTopSize());
        if(!topSize.isPresent()) {
            throw new ClothingSizeNotFoundException(subscriptionDto.getIdTopSize());
        }
        Optional<ClothingSize> pantsSize = this.clothingSizeRepository.findById(subscriptionDto.getIdPantsSize());
        if(!pantsSize.isPresent()) {
            throw new ClothingSizeNotFoundException(subscriptionDto.getIdPantsSize());
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
        subscription.get().setNationality(subscriptionDto.getNationality());
        subscription.get().setTopSize(topSize.get());
        subscription.get().setPantsSize(pantsSize.get());
        subscription.get().setInsuranceRequested(subscriptionDto.isInsuranceRequested());
        subscription.get().setEquipment(subscriptionDto.isEquipment());
        subscription.get().setReferee(subscriptionDto.isReferee());
        subscription.get().setCoach(subscriptionDto.isCoach());
        subscription.get().setSeason(season.get());
        subscription.get().setCategory(category.get());
        subscription.get().setPaymentMode(paymentMode.get());
        return this.subscriptionRepository.save(subscription.get());
    }

    public void setPlayer(Long id, Player player) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if(subscription.isPresent()) {
            subscription.get().setPlayer(player);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public void addCNI(Long id, Document document) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if(subscription.isPresent()) {
            subscription.get().setCNI(document);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public void addIdentityPhoto(Long id, Document document) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if(subscription.isPresent()) {
            subscription.get().setIdentityPhoto(document);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public void addMedicalCertificate(Long id, Document document) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if(subscription.isPresent()) {
            subscription.get().setMedicalCertificate(document);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public Subscription confirmedSubscription(Long id) throws SubscriptionNotFoundException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);

        if(!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        subscription.get().setConfirmed(true);
        subscription.get().setValidationDate(new Date());
        return this.subscriptionRepository.save(subscription.get());
    }

    public void unconfirmedSubscription(Long id) throws SubscriptionNotFoundException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);

        if(!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        subscription.get().setConfirmed(false);
        this.subscriptionRepository.save(subscription.get());
    }

    public void deleteSubscription(Long id) throws SubscriptionNotFoundException, AccessDeniedException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);

        if(!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        this.subscriptionRepository.delete(subscription.get());
    }
}

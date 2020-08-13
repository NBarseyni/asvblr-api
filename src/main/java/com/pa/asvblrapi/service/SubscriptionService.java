package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.UrlSourceImageDto;
import com.pa.asvblrapi.dto.SubscriptionDto;
import com.pa.asvblrapi.dto.SubscriptionPaidDto;
import com.pa.asvblrapi.dto.ValidityPhotoDto;
import com.pa.asvblrapi.entity.*;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.PlayerMapper;
import com.pa.asvblrapi.mapper.SubscriptionMapper;
import com.pa.asvblrapi.mapper.SubscriptionPaidMapper;
import com.pa.asvblrapi.repository.*;
import com.pa.asvblrapi.spring.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SubscriptionCategoryRepository subscriptionCategoryRepository;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

    @Autowired
    private SubscriptionPaidRepository subscriptionPaidRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private MicrosoftFace1ApiService microsoftFace1ApiService;

    @Value("${asvblrapi.app.documentFolder}")
    private String UPLOADED_FOLDER;

    private String uriMicrosoftApi = "https://microsoft-face1.p.rapidapi.com/detect";

    public List<Subscription> getAllSubscriptions() {
        return this.subscriptionRepository.findAll();
    }

    public List<Subscription> getSubscriptionsBySeason(Long id) {
        return this.subscriptionRepository.findSubscriptionBySeason(id);
    }

    public List<SubscriptionDto> getCurrentSeasonSubscriptions() {
        return SubscriptionMapper.instance.toDto(this.subscriptionRepository.findSubscriptionBySeason(
                this.seasonRepository.findCurrentSeason().get().getId()));
    }

    public List<Subscription> getSubscriptionsByPlayer(Long id) {
        return this.subscriptionRepository.findSubscriptionsByPlayer(id);
    }

    public Optional<Subscription> getSubscription(Long id) {
        return this.subscriptionRepository.findById(id);
    }

    public Subscription createSubscription(SubscriptionDto subscriptionDto) throws SeasonNotFoundException, SubscriptionCategoryNotFoundException, PaymentModeNotFoundException {
        Optional<Season> season = this.seasonRepository.findCurrentSeason();
        if (!season.isPresent()) {
            throw new SeasonNotFoundException(subscriptionDto.getIdSeason());
        }

        Optional<SubscriptionCategory> category = this.subscriptionCategoryRepository.findById(subscriptionDto.getIdSubscriptionCategory());
        if (!category.isPresent()) {
            throw new SubscriptionCategoryNotFoundException(subscriptionDto.getIdSubscriptionCategory());
        }

        List<PaymentMode> paymentModes = new ArrayList<>();
        for (int i = 0; i < subscriptionDto.getIdsPaymentMode().size(); i++) {
            Optional<PaymentMode> paymentMode = this.paymentModeRepository.findById(subscriptionDto.getIdsPaymentMode().get(i));
            if (!paymentMode.isPresent()) {
                throw new PaymentModeNotFoundException(subscriptionDto.getIdsPaymentMode().get(i));
            }
            paymentModes.add(paymentMode.get());
        }

        ClothingSize topSize;
        if (subscriptionDto.getIdTopSize() != null) {
            Optional<ClothingSize> topSizeOptional = this.clothingSizeRepository.findById(subscriptionDto.getIdTopSize());
            if (!topSizeOptional.isPresent()) {
                throw new ClothingSizeNotFoundException(subscriptionDto.getIdTopSize());
            }
            topSize = topSizeOptional.get();
        } else {
            topSize = null;
        }
        ClothingSize pantsSize;
        if (subscriptionDto.getIdPantsSize() != null) {
            Optional<ClothingSize> pantsSizeOptional = this.clothingSizeRepository.findById(subscriptionDto.getIdPantsSize());
            if (!pantsSizeOptional.isPresent()) {
                throw new ClothingSizeNotFoundException(subscriptionDto.getIdPantsSize());
            }
            pantsSize = pantsSizeOptional.get();
        } else {
            pantsSize = null;
        }

        Subscription subscription = new Subscription(
                this.capitalize(subscriptionDto.getFirstName().trim()),
                this.capitalize(subscriptionDto.getLastName().trim()),
                subscriptionDto.isGender(),
                subscriptionDto.getAddress().trim(),
                subscriptionDto.getPostcode(),
                subscriptionDto.getCity(),
                subscriptionDto.getEmail().trim(),
                subscriptionDto.getPhoneNumber(),
                subscriptionDto.getBirthDate(),
                subscriptionDto.getNationality().trim(),
                topSize,
                pantsSize,
                subscriptionDto.getRequestedJerseyNumber(),
                subscriptionDto.isInsuranceRequested(),
                subscriptionDto.isEquipment(),
                subscriptionDto.isReferee(),
                subscriptionDto.isCoach(),
                subscriptionDto.isCalendarRequested(),
                subscriptionDto.getComment().trim(),
                subscriptionDto.isPc_allowToLeaveAlone(),
                subscriptionDto.isPc_allowClubToRescue(),
                subscriptionDto.isPc_allowToTravelWithTeamMate(),
                subscriptionDto.isPc_allowToPublish(),
                subscriptionDto.isPc_unaccountability(),
                subscriptionDto.isPc_allowToWhatsapp(),
                season.get(),
                category.get()
                //paymentModes
        );
        Subscription subscriptionSave = this.subscriptionRepository.save(subscription);
        Set<SubscriptionPaid> subscriptionsPaid = new HashSet<>();
        for (PaymentMode paymentMode :
                paymentModes) {
            SubscriptionPaid subscriptionPaid = new SubscriptionPaid(subscriptionSave, paymentMode);
            subscriptionsPaid.add(subscriptionPaid);
            this.subscriptionPaidRepository.save(subscriptionPaid);
        }
        subscriptionSave.setSubscriptionsPaid(subscriptionsPaid);
        return this.subscriptionRepository.save(subscriptionSave);
    }

    public Subscription createReSubscription(SubscriptionDto subscriptionDto) throws PlayerNotFoundException {
        Optional<Player> player = this.playerRepository.findById(subscriptionDto.getIdPlayer());
        if (!player.isPresent()) {
            throw new PlayerNotFoundException(subscriptionDto.getIdPlayer());
        }
        Subscription subscription = this.createSubscription(subscriptionDto);
        subscription.setPlayer(player.get());
        return this.subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, SubscriptionDto subscriptionDto) throws SubscriptionNotFoundException,
            SeasonNotFoundException, SubscriptionCategoryNotFoundException, PaymentModeNotFoundException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        Optional<SubscriptionCategory> category = this.subscriptionCategoryRepository.findById(subscriptionDto.getIdSubscriptionCategory());
        if (!category.isPresent()) {
            throw new SubscriptionCategoryNotFoundException(subscriptionDto.getIdSubscriptionCategory());
        }

        List<PaymentMode> paymentModes = new ArrayList<>();
        for (int i = 0; i < subscriptionDto.getIdsPaymentMode().size(); i++) {
            Optional<PaymentMode> paymentMode = this.paymentModeRepository.findById(subscriptionDto.getIdsPaymentMode().get(i));
            if (!paymentMode.isPresent()) {
                throw new PaymentModeNotFoundException(subscriptionDto.getIdsPaymentMode().get(i));
            }
            paymentModes.add(paymentMode.get());
        }

        Optional<ClothingSize> topSize = this.clothingSizeRepository.findById(subscriptionDto.getIdTopSize());
        if (!topSize.isPresent()) {
            throw new ClothingSizeNotFoundException(subscriptionDto.getIdTopSize());
        }
        Optional<ClothingSize> pantsSize = this.clothingSizeRepository.findById(subscriptionDto.getIdPantsSize());
        if (!pantsSize.isPresent()) {
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
        subscription.get().setRequestedJerseyNumber(subscriptionDto.getRequestedJerseyNumber());
        subscription.get().setInsuranceRequested(subscriptionDto.isInsuranceRequested());
        subscription.get().setEquipment(subscriptionDto.isEquipment());
        subscription.get().setReferee(subscriptionDto.isReferee());
        subscription.get().setCoach(subscriptionDto.isCoach());
        subscription.get().setCalendarRequested(subscriptionDto.isCalendarRequested());
        subscription.get().setComment(subscriptionDto.getComment());
        subscription.get().setPc_allowToLeaveAlone(subscriptionDto.isPc_allowToLeaveAlone());
        subscription.get().setPc_allowClubToRescue(subscriptionDto.isPc_allowClubToRescue());
        subscription.get().setPc_allowToTravelWithTeamMate(subscriptionDto.isPc_allowToTravelWithTeamMate());
        subscription.get().setPc_allowToPublish(subscriptionDto.isPc_allowToPublish());
        subscription.get().setPc_unaccountability(subscriptionDto.isPc_unaccountability());
        subscription.get().setPc_allowToWhatsapp(subscriptionDto.isPc_allowToWhatsapp());
        subscription.get().setSubscriptionCategory(category.get());
        //subscription.get().setPaymentModes(paymentModes);

        Set<SubscriptionPaid> subscriptionsPaid = new HashSet<>();
        for (PaymentMode paymentMode :
                paymentModes) {
            SubscriptionPaid subscriptionPaid = new SubscriptionPaid(subscription.get(), paymentMode);
            subscriptionsPaid.add(subscriptionPaid);
            this.subscriptionPaidRepository.save(subscriptionPaid);
        }
        subscription.get().setSubscriptionsPaid(subscriptionsPaid);
        return this.subscriptionRepository.save(subscription.get());
    }

    public List<SubscriptionPaidDto> getSubscriptionPaid(Long idSubscription) {
        List<SubscriptionPaid> subscriptionsPaid = this.subscriptionPaidRepository.findByIdSubscription(idSubscription);
        return SubscriptionPaidMapper.instance.toDto(subscriptionsPaid);
    }

    public List<SubscriptionPaidDto> validatedPaymentMode(Long idSubscription, Long idPaymentMode) throws SubscriptionPaidNotFoundException {
        Optional<SubscriptionPaid> subscriptionPaid =
                this.subscriptionPaidRepository.findByIdSubscriptionAndIdPaymentMode(idSubscription, idPaymentMode);
        if (!subscriptionPaid.isPresent()) {
            throw new SubscriptionPaidNotFoundException(idSubscription, idPaymentMode);
        }
        subscriptionPaid.get().setPaid(true);
        this.subscriptionPaidRepository.save(subscriptionPaid.get());
        return SubscriptionPaidMapper.instance.toDto(this.subscriptionPaidRepository.findByIdSubscription(idSubscription));
    }

    public List<SubscriptionPaidDto> unvalidatedPaymentMode(Long idSubscription, Long idPaymentMode) throws SubscriptionPaidNotFoundException {
        Optional<SubscriptionPaid> subscriptionPaid =
                this.subscriptionPaidRepository.findByIdSubscriptionAndIdPaymentMode(idSubscription, idPaymentMode);
        if (!subscriptionPaid.isPresent()) {
            throw new SubscriptionPaidNotFoundException(idSubscription, idPaymentMode);
        }
        subscriptionPaid.get().setPaid(false);
        this.subscriptionPaidRepository.save(subscriptionPaid.get());
        return SubscriptionPaidMapper.instance.toDto(this.subscriptionPaidRepository.findByIdSubscription(idSubscription));
    }

    public void setPlayer(Long id, Player player) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (subscription.isPresent()) {
            subscription.get().setPlayer(player);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public void addCNI(Long id, Document document) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (subscription.isPresent()) {
            subscription.get().setCNI(document);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public void addIdentityPhoto(Long id, Document document) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (subscription.isPresent()) {
            subscription.get().setIdentityPhoto(document);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public ValidityPhotoDto checkValidityPhoto(Long id) throws NoSuchAlgorithmException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        boolean validity = this.microsoftFace1ApiService.checkIfPhotoHasFace(subscription.get().getIdentityPhoto().getName());
        return new ValidityPhotoDto(validity);
    }

    public void addMedicalCertificate(Long id, Document document) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (subscription.isPresent()) {
            subscription.get().setMedicalCertificate(document);
            this.subscriptionRepository.save(subscription.get());
        }
    }

    public SubscriptionDto validatedSubscription(Long id) throws Exception {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        if (subscription.get().isValidated()) {
            throw new SubscriptionAlreadyValidatedException();
        }
        for (SubscriptionPaid subscriptionPaid :
                subscription.get().getSubscriptionsPaid()) {
            if (!subscriptionPaid.isPaid()) {
                throw new SubscriptionHasNotAllPaymentModeValidatedException(id);
            }
        }
        if (subscription.get().getPlayer() == null) {
            return this.validatedNewSubscription(subscription.get());
        } else {
            return this.validatedReSubscription(subscription.get());
        }
    }

    public SubscriptionDto validatedNewSubscription(Subscription subscription) throws Exception {
        subscription.setValidated(true);
        subscription.setValidationDate(new Date());

        User user = this.userService.createUserSubscription(subscription.getFirstName(), subscription.getLastName(),
                subscription.getEmail());
        Player player = this.playerService.createPlayer(subscription, user);
        this.setPlayer(subscription.getId(), player);

        return SubscriptionMapper.instance.toDto(this.subscriptionRepository.save(subscription));
    }

    public SubscriptionDto validatedReSubscription(Subscription subscription) throws SubscriptionNotFoundException, PlayerNotFoundException,
            SubscriptionAlreadyValidatedException, SubscriptionHasNotAllPaymentModeValidatedException, MessagingException {
        subscription.setValidated(true);
        subscription.setValidationDate(new Date());
        Player player = this.playerService.updatePlayer(subscription.getPlayer().getId(), PlayerMapper.instance.toDto(subscription.getPlayer()));
        subscription.setPlayer(player);
        this.emailService.sendMessageValidatedReSubscription(subscription);
        return SubscriptionMapper.instance.toDto(this.subscriptionRepository.save(subscription));
    }

    public void unvalidatedSubscription(Long id) throws SubscriptionNotFoundException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);

        if (!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        subscription.get().setValidated(false);
        this.subscriptionRepository.save(subscription.get());
    }

    public void deleteSubscription(Long id) throws SubscriptionNotFoundException, DocumentNotFoundException, IOException {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);

        if (!subscription.isPresent()) {
            throw new SubscriptionNotFoundException(id);
        }
        this.subscriptionRepository.delete(subscription.get());
        if (subscription.get().getCNI() != null) {
            this.documentService.deleteDocument(subscription.get().getCNI().getId());
        }
        if (subscription.get().getIdentityPhoto() != null) {
            this.documentService.deleteDocument(subscription.get().getIdentityPhoto().getId());
        }
        if (subscription.get().getMedicalCertificate() != null) {
            this.documentService.deleteDocument(subscription.get().getMedicalCertificate().getId());
        }
    }

    private String capitalize(String str) {
        String[] words = str.split("-");

        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        return String.join("-", words);
    }
}

package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.FundraiserException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.CategoryRepository;
import br.com.dbc.devser.colabore.repository.DonationRepository;
import br.com.dbc.devser.colabore.repository.FundraiserRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundraiserService {

    private final ObjectMapper objectMapper;
    private final FundraiserRepository fundraiserRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) throws UserColaboreException {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        FundraiserEntity fundraiserEntity = objectMapper.convertValue(fundraiserCreate, FundraiserEntity.class);

        fundraiserEntity.setCreationDate(LocalDateTime.now());
        fundraiserEntity.setCurrentValue(new BigDecimal("0.0"));
        fundraiserEntity.setStatusActive(true);
        fundraiserEntity.setFundraiserCreator(userRepository.findById(Long.getLong(authUserId))
                .orElseThrow(() -> new UserColaboreException("User not found.")));
        fundraiserEntity.setCategoriesFundraiser(buildCategories(fundraiserCreate.getCategories()));
        /*Seta a foto e grava no banco*/
        fundraiserRepository.save(setPhotoEntity(fundraiserEntity, fundraiserCreate));
    }

    public void updateFundraiser(Long fundraiserId, FundraiserCreateDTO fundraiserUpdate) throws FundraiserException {

        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        if (fundraiserEntity.getDonations().size() != 0) {
            throw new FundraiserException("Fundraiser already has donations.");
        }

        fundraiserEntity.setTitle(fundraiserUpdate.getTitle());
        fundraiserEntity.setGoal(fundraiserUpdate.getGoal());
        fundraiserEntity.setAutomaticClose(fundraiserUpdate.getAutomaticClose());
        fundraiserEntity.setDescription(fundraiserUpdate.getDescription());
        fundraiserEntity.setEndingDate(fundraiserUpdate.getEndingDate());
        fundraiserEntity.setCategoriesFundraiser(buildCategories(fundraiserUpdate.getCategories()));

        fundraiserEntity.setLastUpdate(LocalDateTime.now());

        fundraiserRepository.save(setPhotoEntity(fundraiserEntity, fundraiserUpdate));
    }

    public void updateFundraiserStatus(Long fundraiserId) throws FundraiserException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);
        fundraiserEntity.setStatusActive(!fundraiserEntity.getStatusActive());
    }

    private Set<CategoryEntity> buildCategories(Set<String> categories) {
        return categories.stream().map(category -> {
            //***Testando se existe***
            CategoryEntity categoryReference = categoryRepository.findByName(category);
            if (categoryReference != null) {
                return categoryReference;
            }
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setName(category);

            return categoryRepository.save(categoryEntity);
        }).collect(Collectors.toSet());
    }

    public FundraiserDetailsDTO fundraiserDetails(Long fundraiserId) throws FundraiserException {
        FundraiserEntity fundraiserEntity = findById(fundraiserId);

        FundraiserDetailsDTO details = objectMapper.convertValue(fundraiserEntity, FundraiserDetailsDTO.class);

        details.setCoverPhoto(Base64.getEncoder().encodeToString(fundraiserEntity.getCover()));
        details.setCategories(convertCategories(fundraiserEntity.getCategoriesFundraiser()));
        //TODO: Continuar daqui!!!
        details.setContributors(fundraiserEntity.getDonations().stream().map(donationEntity -> {
            UserEntity donatorEntity = donationEntity.getDonator();

            return UserDTO.builder()
                    .userId(donatorEntity.getUserId())
                    .email(donatorEntity.getEmail())
                    .profilePhoto(Base64.getEncoder().encodeToString(donatorEntity.getPhoto()))
                    .build();
        }).collect(Collectors.toSet()));

        return details;
    }

    public Page<FundraiserGenericDTO> findAllFundraisers(Integer numberPage) {
        return fundraiserRepository
                .findAllFundraisersActive(getPageable(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }


    public Page<FundraiserGenericDTO> findUserFundraisers(Integer numberPage) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return fundraiserRepository
                .findFundraisersOfUser(Long.getLong(userId), getPageable(numberPage, 30))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public Page<FundraiserUserContributionsDTO> userContributions(Integer numberPage) {
        String authId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return donationRepository.findMyDonations(Long.getLong(authId), getPageable(numberPage, 20))
                .map(dEntity -> objectMapper.convertValue(dEntity, FundraiserUserContributionsDTO.class));
    }

//    public Page<FundraiserGenericDTO> filterByCategories(List<String> categories, Integer numberPage) {
//        return fundraiserRepository
//                .findByCategoriesContainsIgnoreCaseAndStatusActive(convertListToString(categories), true
//                        , getPageable(numberPage, 20))
//                .map(fEntity -> {
//                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
//                    return completeFundraiser(generic, fEntity);
//                });
//    }

    public Page<FundraiserGenericDTO> filterByFundraiserComplete(Integer numberPage) {
        return fundraiserRepository.findFundraiserCompleted(getPageable(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public Page<FundraiserGenericDTO> filterByFundraiserIncomplete(Integer numberPage) {
        return fundraiserRepository.findFundraiserIncomplete(getPageable(numberPage, 20))
                .map(fEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fEntity);
                });
    }

    public void deleteFundraiser(Long fundraiserId) {
        fundraiserRepository.deleteById(fundraiserId);
    }

    private FundraiserEntity findById(Long fundraiserId) throws FundraiserException {
        return fundraiserRepository.findById(fundraiserId)
                .orElseThrow(() -> new FundraiserException("Fundraiser not found."));
    }

    private Pageable getPageable(Integer numberPage, Integer numberItems) {
        return PageRequest
                .of(numberPage, numberItems, Sort.by("creationDate").ascending());
    }


    private FundraiserGenericDTO completeFundraiser(FundraiserGenericDTO generic, FundraiserEntity fEntity) {
//        generic.setCategories(convertCategories(fEntity.getCategories()));
        generic.setCurrentValue(calculateTotal(fEntity));
//        generic.setCreationDate(fEntity.getCreationDate());
        generic.setLastUpdate(fEntity.getLastUpdate());
        generic.setFundraiserCreator(objectMapper.convertValue(fEntity.getFundraiserCreator(), UserDTO.class));
        return generic;
    }

    private Set<String> convertCategories(Set<CategoryEntity> categories) {
        return categories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());
    }

    private BigDecimal calculateTotal(FundraiserEntity fEntity) {
        return fEntity.getDonations().stream().map(DonationEntity::getValue)
                .reduce(BigDecimal::add).orElse(null);
    }

    private FundraiserEntity setPhotoEntity(FundraiserEntity ent, FundraiserCreateDTO fundCreate) {
        try {
            MultipartFile coverPhoto = fundCreate.getCoverPhoto();
            if (coverPhoto != null) {
                ent.setCover(coverPhoto.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ent;
    }

    public void checkClosed(Long idRequest) throws BusinessRuleException {
        FundraiserEntity fundraiserEntity = fundraiserRepository.findById(idRequest)
                .orElseThrow(() -> new BusinessRuleException("Fundraiser not found."));

        fundraiserEntity.setAutomaticClose(checkClosedValue(fundraiserEntity.getCurrentValue(), fundraiserEntity.getGoal()));

        fundraiserRepository.save(fundraiserEntity);
    }

    public Boolean checkClosedValue(BigDecimal currentValue, BigDecimal goal) {
        return currentValue.compareTo(goal) > 0;
    }
}

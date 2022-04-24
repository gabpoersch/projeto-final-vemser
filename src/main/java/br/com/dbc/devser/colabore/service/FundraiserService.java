package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.category.CategoryDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.CategoryEntity;
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
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
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
    private final UserService userService;

    public void saveFundraiser(FundraiserCreateDTO fundraiserCreate) throws UserColaboreException, BusinessRuleException {
        FundraiserEntity fundraiserEntity = new FundraiserEntity();
        fundraiserEntity.setTitle(fundraiserCreate.getTitle());
        fundraiserEntity.setDescription(fundraiserCreate.getDescription());
        fundraiserEntity.setGoal(fundraiserCreate.getGoal());
        fundraiserEntity.setCurrentValue(new BigDecimal("0.0"));
        fundraiserEntity.setStatusActive(true);
        fundraiserEntity.setCreationDate(LocalDateTime.now());
        fundraiserEntity.setEndingDate(fundraiserCreate.getEndingDate());
        fundraiserEntity.setLastUpdate(LocalDateTime.now());
        fundraiserEntity.setAutomaticClose(fundraiserCreate.getAutomaticClose());
        fundraiserEntity.setFundraiserCreator(userRepository.findById(userService.getLoggedUserId())
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
                .map(dEntity -> {
                    FundraiserEntity fEntity = dEntity.getFundraiser();
                    FundraiserGenericDTO fundraiserGeneric = objectMapper
                            .convertValue(fEntity, FundraiserGenericDTO.class);
                    FundraiserUserContributionsDTO userContributions = objectMapper
                            .convertValue(completeFundraiser(fundraiserGeneric, fEntity)
                                    , FundraiserUserContributionsDTO.class);
                    userContributions.setStatus(fEntity.getStatusActive());
                    userContributions.setTotalContribution(dEntity.getValue());
                    return userContributions;
                });
    }

    public Page<FundraiserGenericDTO> filterByCategories(List<String> categories, Integer numberPage) {
        List<FundraiserGenericDTO> listFundGeneric = fundraiserRepository
                .findAll(getPageable(numberPage, 20)).stream()
                .filter(fEntity -> fEntity.getCategoriesFundraiser().stream().allMatch(cEntity -> categories.stream()
                        .anyMatch(categoryStr -> categoryStr.equalsIgnoreCase(cEntity.getName()))))
                .map(fundraiserEntity -> {
                    FundraiserGenericDTO generic = objectMapper.convertValue(fundraiserEntity, FundraiserGenericDTO.class);
                    return completeFundraiser(generic, fundraiserEntity);
                }).collect(Collectors.toList());
        return new PageImpl<>(listFundGeneric);
    }

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
                .of(numberPage, numberItems, Sort.by("endingDate").ascending());
    }

    private FundraiserGenericDTO completeFundraiser(FundraiserGenericDTO generic, FundraiserEntity fEntity) {
        generic.setCategories(convertCategories(fEntity.getCategoriesFundraiser()));
        generic.setFundraiserCreator(objectMapper.convertValue(fEntity.getFundraiserCreator(), UserDTO.class));
        generic.setCoverPhoto(Base64.getEncoder().encodeToString(fEntity.getCover()));
        return generic;
    }

    private Set<CategoryDTO> convertCategories(Set<CategoryEntity> categories) {
        return categories.stream()
                .map(cEntity -> objectMapper.convertValue(cEntity, CategoryDTO.class))
                .collect(Collectors.toSet());
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

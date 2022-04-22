package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserUserContributionsDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.service.FundraiserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fundraiser")
@RequiredArgsConstructor
public class FundraiserController {

    private final FundraiserService fundraiserService;

    @PostMapping
    public void saveFundraiser(@RequestBody FundraiserCreateDTO newFund) throws BusinessRuleException {
        fundraiserService.saveFundraiser(newFund);
    }

    @PutMapping("/{fundraiserId}")
    public void updateFundraiser(@PathVariable("fundraiserId") Long fundraiserId, @RequestBody FundraiserCreateDTO fundUpdate) throws BusinessRuleException {
        fundraiserService.updateFundraiser(fundraiserId, fundUpdate);
    }

    @GetMapping("/userFundraisers/{numberPage}")
    public Page<FundraiserGenericDTO> findUserFundraisers(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findUserFundraisers(numberPage);
    }

    @GetMapping("/fundraiserDetails/{fundraiserId}")
    public FundraiserDetailsDTO fundraiserDetails(@PathVariable("fundraiserId") Long fundraiserId) throws BusinessRuleException {
        return fundraiserService.fundraiserDetails(fundraiserId);
    }

    @GetMapping("/userContributions/{numberPage}")
    public Page<FundraiserUserContributionsDTO> userContributions(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findUserContributions(numberPage);
    }

    @GetMapping("/byCategories/{numberPage}")
    public Page<FundraiserGenericDTO> filterByCategories(@RequestBody List<String> categories, @PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByCategories(categories, numberPage);
    }

    @GetMapping("/fundraiserCompleted/{numberPage}")
    public Page<FundraiserGenericDTO> filterByFundraiserComplete(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByFundraiserComplete(numberPage);
    }

    @GetMapping("/fundraiserIncomplete/{numberPage}")
    public Page<FundraiserGenericDTO> filterByFundraiserIncomplete(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.filterByFundraiserIncomplete(numberPage);
    }

    @GetMapping("/findAll/{numberPage}")
    public Page<FundraiserGenericDTO> findAllFundraisers(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findAllFundraisers(numberPage);
    }

    @DeleteMapping("/{fundraiserId}")
    public void deleteFundraiser(@PathVariable("fundraiserId") Long fundraiserId) {
        fundraiserService.deleteFundraiser(fundraiserId);
    }
}

package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserGenericDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.service.FundraiserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fundraiser")
@RequiredArgsConstructor
public class FundraiserController {

    private final FundraiserService fundraiserService;

    @PostMapping("/save")
    public void saveFundraiser(@RequestBody FundraiserCreateDTO newFund) {
        fundraiserService.saveFundraiser(newFund);
    }

    @GetMapping("/user/{numberPage}")
    public Page<FundraiserGenericDTO> findUserFundraisers(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findUserFundraisers(numberPage);
    }

    @GetMapping("/findAll/{numberPage}")
    public Page<FundraiserGenericDTO> findAllFundraisers(@PathVariable("numberPage") Integer numberPage) {
        return fundraiserService.findAllFundraisers(numberPage);
    }

    @DeleteMapping("/delete/{fundraiserId}")
    public void deleteFundraiser(@PathVariable("fundraiserId") Long fundraiserId) throws BusinessRuleException {
        fundraiserService.deleteFundraiser(fundraiserId);
    }
}

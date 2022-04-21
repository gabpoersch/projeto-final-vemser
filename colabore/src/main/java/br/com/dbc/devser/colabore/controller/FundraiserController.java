package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserCreateDTO;
import br.com.dbc.devser.colabore.dto.fundraiser.FundraiserDetailsDTO;
import br.com.dbc.devser.colabore.service.FundraiserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fundraiser")
@RequiredArgsConstructor
public class FundraiserController {

    private final FundraiserService fundraiserService;

    @PostMapping("/save")
    public void saveFundraiser (@RequestBody FundraiserCreateDTO newFund){
        fundraiserService.saveFundraiser(newFund);
    }

    @GetMapping("/user/{numberPage}")
    public Page<FundraiserDetailsDTO> findUserFundraisers(@PathVariable("numberPage") Integer numberPage) {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return fundraiserService.findUserFundraisers(authUserId, numberPage);
    }

    @GetMapping("/findAll/{numberPage}")
    public Page<FundraiserDetailsDTO> findAllFundraisers (@PathVariable("numberPage") Integer numberPage){
        return fundraiserService.findAllFundraisers (numberPage);
    }

    @DeleteMapping("/delete/{fundraiserId}")
    public void deleteFundraiser (@PathVariable("fundraiserId") Long fundraiserId){
        fundraiserService.deleteFundraiser(fundraiserId);
    }
}

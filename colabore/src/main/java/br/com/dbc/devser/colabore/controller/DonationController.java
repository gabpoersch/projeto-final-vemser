package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.donate.DonateCreateDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donation")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/{fundraiserId}")
    public void makeDonation(@PathVariable("fundraiserId") Long fundraiserId, @RequestBody DonateCreateDTO donate) throws BusinessRuleException {
        donationService.makeDonation(fundraiserId, donate);
    }
}

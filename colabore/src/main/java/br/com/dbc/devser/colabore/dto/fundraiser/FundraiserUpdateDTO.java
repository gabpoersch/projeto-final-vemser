package br.com.dbc.devser.colabore.dto.fundraiser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserUpdateDTO extends FundraiserCreateDTO {
    private Long fundraiserId;
}




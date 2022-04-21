package br.com.dbc.devser.colabore.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "donation")
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_DONATION")
    @SequenceGenerator(name = "GEN_DONATION", sequenceName = "sequence_donation", allocationSize = 1)
    @Column(name = "donation_id")
    private Integer donationId;

    private String message;

    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donator_id", referencedColumnName = "user_id")
    private UserEntity donator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraiser_id", referencedColumnName = "fundraiser_id")
    private FundraiserEntity fundraiser;
}

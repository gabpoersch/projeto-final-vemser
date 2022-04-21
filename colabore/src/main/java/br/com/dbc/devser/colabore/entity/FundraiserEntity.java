package br.com.dbc.devser.colabore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "fundraiser")
public class FundraiserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FUNDRAISER")
    @SequenceGenerator(name = "GEN_FUNDRAISER", sequenceName = "sequence_fundraiser", allocationSize = 1)
    @Column(name = "fundraiser_id")
    private Long fundraiserId;

    private String title;

    private String description;

    private BigDecimal goal;

    @Column(name = "current_value")
    private BigDecimal currentValue;

    @Column(name = "cover_photo")
    private String coverPhoto;

    @Column(name = "status")
    private Boolean statusActive;

    @Column(name = "category_list")
    private String categories;

    @Column(name = "creation_date")
    private Long creationDate;

    @Column(name = "automatic_close")
    private Boolean automaticClose;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "fundraiser")
    private Set<DonationEntity> donations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraiser_creator", referencedColumnName = "user_id")
    private UserEntity fundraiserCreator;
}

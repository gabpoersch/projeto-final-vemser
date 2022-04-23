package br.com.dbc.devser.colabore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "goal")
    private BigDecimal goal;

    @Column(name = "current_value")
    private BigDecimal currentValue;

    @Column(name = "cover_photo")
    private byte[] coverPhoto;

    @Column(name = "status")
    private Boolean statusActive;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "automatic_close")
    private Boolean automaticClose;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "fundraiser")
    private Set<DonationEntity> donations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraiser_creator", referencedColumnName = "user_id")
    private UserEntity fundraiserCreator;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "fundraiser_category",
            joinColumns = @JoinColumn(name = "fundraiser_id", referencedColumnName = "fundraiser_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    )
    private Set<CategoryEntity> categories;
}

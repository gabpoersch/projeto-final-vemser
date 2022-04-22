package br.com.dbc.devser.colabore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_CATEGORY")
    @SequenceGenerator(name = "GEN_CATEGORY", sequenceName = "sequence_category", allocationSize = 1)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraiser_id", referencedColumnName = "fundraiser_id")
    private Set<FundraiserEntity> fundraisers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
}

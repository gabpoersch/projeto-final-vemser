package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraiserRepository extends JpaRepository<FundraiserEntity, Long> {
    @Query("select f from fundraiser f where f.fundraiserCreator.userId = :userId and f.statusActive=true")
    Page<FundraiserEntity> findFundraisersOfUser(Long userId, Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true")
    Page<FundraiserEntity> findAllFundraisersActive(Pageable pageable);

    Page<FundraiserEntity> findByCategoriesContainsIgnoreCaseAndStatusActive (String categories, Boolean statusActive, Pageable pageable);

    @Query("select f from fundraiser f where f.currentValue >= f.goal and f.statusActive = true")
    Page<FundraiserEntity> findFundraiserCompleted (Pageable pageable);

    @Query("select f from fundraiser f where f.currentValue < f.goal and f.statusActive = true")
    Page<FundraiserEntity> findFundraiserIncomplete (Pageable pageable);
}

package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FundraiserRepository extends JpaRepository<FundraiserEntity, Long> {
    @Query("select f from fundraiser f where f.fundraiserCreator.userId = :userId and f.statusActive=true")
    Page<FundraiserEntity> findFundraisersOfUser(@Param("userId") Long userId, Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true and f.currentValue < f.goal")
    Page<FundraiserEntity> findFundraisersActiveNotAcchieved(Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true and f.currentValue >= f.goal")
    Page<FundraiserEntity> findFundraisersActiveAcchieved(Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true")
    Page<FundraiserEntity> findAllFundraisersActive(Pageable pageable);

    @Query("select f from fundraiser f where f.endingDate = :end")
    List<FundraiserEntity> finishedFundraisers(@Param("end") LocalDate end);

    @Query("select f from fundraiser f " +
            "join f.categoriesFundraiser fc " +
            "where lower(fc.name) in(:categories) and f.statusActive = true group by f.fundraiserId")
    Page<FundraiserEntity> filterByCategories(List<String> categories, Pageable pageable);

}

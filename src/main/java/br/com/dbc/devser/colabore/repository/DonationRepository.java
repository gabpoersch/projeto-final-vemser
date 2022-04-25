package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.DonationEntity;
import br.com.dbc.devser.colabore.entity.UserContributions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    @Query("select new br.com.dbc.devser.colabore.entity.UserContributions(f, sum (d.value)) " +
            "from donation d join fetch fundraiser f on d.fundraiser.fundraiserId = f.fundraiserId " +
            "where d.donator.userId = :userId group by f.fundraiserId")
    Page<UserContributions> findMyDonations(@Param("userId") Long userId, Pageable pageable);

}

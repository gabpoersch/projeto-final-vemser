package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.DonationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    @Query("select d.fundraiser, sum(d.value) from donation d where d.donator.userId = :userId group by d.fundraiser.fundraiserId, d.value")
//    @Query("select sum (d.value), d.fundraiser from donation d join fetch fundraiser f on d.fundraiser.fundraiserId = f.fundraiserId " +
//            "where d.donator.userId :userId group by d.fundraiser.fundraiserId, f")
    Page<DonationEntity> findMyDonations(Long userId, Pageable pageable);

    /*
        Solução caso dê erro da maneira acima:
            @Repository
            public interface MyRepository extends JpaRepository<MyEntity, ID> {
                @Query("SELECT new com.NewPojo(SUM(m.totalDays)) FROM MyEntity m")
                NewPojo selectTotals();
                }

                class NewPojo {
                    Float days;
                    public NewPojo(Float days) {
                        this.days = days;
                    }
        }
     */
}

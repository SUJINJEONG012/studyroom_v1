package infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import infra.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	

}

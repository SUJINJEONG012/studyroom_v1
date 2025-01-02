package infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import infra.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
	Optional<Room> findByMid(Long mid);
}
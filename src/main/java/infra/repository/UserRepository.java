package infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import infra.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	// 반환 타입을 Optional<User>로 수정
	
    Optional<User> findByUid(String uid);
}
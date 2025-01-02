package infra.service;


import org.springframework.stereotype.Service;

import infra.repository.ReservationRepository;
import infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * admin - 룸 등록 승인/해제,  사용자 차단/해제
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {
	
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;

	
}
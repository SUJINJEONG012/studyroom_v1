package infra.service;


import org.springframework.stereotype.Service;

import infra.entity.Reservation;
import infra.entity.User;
import infra.dto.ReservationDto;
import infra.repository.ReservationRepository;
import infra.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {
	
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public ReservationDto updateReservation(Long rid, ReservationDto reservationDto) {
		
			//User user = userRepository.getReferenceById(reservationDto.getUserDto().getUid());
			Reservation reservation = reservationRepository.getReferenceById(rid);
			
			// Post 수정
			Reservation updateReservation = reservation.updateField(reservationDto.getStartDate(),
					reservationDto.getPeopleNum(),
					reservationDto.getStartTime(),
					reservationDto.getEndTime(),
					reservationDto.getTotalTime()										 
					
					);
			
			return ReservationDto.from(updateReservation);
		
		
	}

	
}

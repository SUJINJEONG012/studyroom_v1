package infra.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import infra.entity.Reservation;
import infra.entity.Room;
import infra.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@AllArgsConstructor
@ToString
@Getter
public class ReservationDto {
	
	private Integer peopleNum;
	private LocalDate startDate;
	private Integer startTime;
	private Integer endTime;
	private Integer totalTime;
	private UserDto userDto;
	private RoomDto roomDto;
	
	
	public static ReservationDto of(Integer peopleNum, LocalDate startDate, Integer startTime, Integer endTime, Integer totalTime) 
	{
    	return new ReservationDto(peopleNum, startDate, startTime, startTime+totalTime, totalTime, null, null);
    }

	public static ReservationDto of(Integer peopleNum, LocalDate startDate, Integer startTime, Integer totalTime,
			UserDto userDto, RoomDto roomDto) {
    	return new ReservationDto(peopleNum, startDate, startTime, startTime+totalTime, totalTime, userDto, roomDto);
    }
	
//	rid, room, user, date, peopleNum, startTime, endTime, totalDuration
	
	public Reservation toEntity(User user, Room room) {
    	return Reservation.of(
						room,
						user,
						startDate,
						peopleNum,
						startTime,
						endTime,
						totalTime
		);
    }
	
	public static ReservationDto from(Reservation reservation) {
		return ReservationDto.of(
							reservation.getPeopleNum(),
							reservation.getDate(),
							reservation.getStartTime(),
							reservation.getEndTime(),
							reservation.getTotalDuration()
						
		);
	}
	
	    

}

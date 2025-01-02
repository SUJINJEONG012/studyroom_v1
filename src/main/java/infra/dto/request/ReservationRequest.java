package infra.dto.request;

import java.time.LocalDate;

import infra.dto.ReservationDto;
import infra.dto.RoomDto;
import infra.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class ReservationRequest {
  
    private Integer peopleNum;
    private LocalDate startDate;
    private Integer startTime;
    private Integer totalTime;
    
    
 
    
    public static ReservationRequest of(Integer peopleNum, LocalDate startDate, Integer startTime, Integer totalTime) {
    	return new ReservationRequest(peopleNum, startDate, startTime, totalTime); 
    }
    
    public ReservationDto toDto(UserDto userDto, RoomDto roomDto) {
    	return ReservationDto.of(peopleNum, startDate, startTime, totalTime, userDto, roomDto);
    }

	
    
}

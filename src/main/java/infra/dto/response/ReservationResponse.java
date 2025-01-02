package infra.dto.response;



import java.time.LocalDate;

import infra.dto.ReservationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ReservationResponse {
	
	private Integer peopleNum;
    private LocalDate starDate;
    private Integer startTime;
    private Integer endTime;    
    private Integer totalTime;


	
	
	public static ReservationResponse from(ReservationDto reservationDto) {
    	
    	return new ReservationResponse(
    			reservationDto.getPeopleNum(),
    			reservationDto.getStartDate(),
    			reservationDto.getStartTime(),
    			reservationDto.getEndTime(),
    			reservationDto.getTotalTime()
    			);
    }
	
	
}

package infra.dto.request;

import infra.dto.RoomDto;
import infra.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class RoomRequest {
    private String name;
    private Integer maxPeople;
    private Integer price;
    private String addr1;
    private String addr2;
    private String addr3;
    private String content;
    private Boolean isAccepted; // isAccepted 필드를 추가
    
    
    public static RoomRequest of(String name, Integer maxPeople, Integer price, String addr1, String addr2, String addr3,
			String content, Boolean isAccepted) {
    	return new RoomRequest(name, maxPeople, price, addr1, addr2, addr3, content,isAccepted); 
    }
    



	public RoomDto toDto() {
		
		return null;
	}
    
//    public RoomDto toDto(UserDto userDto) {
//    	return RoomDto.of(name, maxPeople, price, addr1, addr2, addr3, content, userDto);
//    }

	
    
}

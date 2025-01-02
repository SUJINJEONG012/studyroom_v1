package infra.dto;

import infra.entity.Room;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class RoomDto {
	
	private Long id;
	private String name;
    private Integer maxPeople;
    private Integer price;
    private String addr1;
    private String addr2;
    private String addr3;
    private String content;
    private UserDto userDto;
    
    // 기본 생성자 추가
    public RoomDto() {
        // 빈 생성자
    }


    public static RoomDto of(String name, Integer maxPeople, Integer price, String addr1, String addr2, String addr3,
			String content, UserDto userDto) {
		return RoomDto.of(null, name, maxPeople, price, addr1, addr2, addr3, content, userDto);
	}
    
	public static RoomDto of(Long id, String name, Integer maxPeople, Integer price, String addr1, String addr2, String addr3,
			String content, UserDto userDto) {
		return new RoomDto(id, name, maxPeople, price, addr1, addr2, addr3, content, userDto);
	}
	
	public static RoomDto from(Room room) {
		return new RoomDto(
							room.getMid(),
							room.getName(),
							room.getMaxPeople(),
							room.getPrice(),
							room.getAddr1(),
							room.getAddr2(),
							room.getAddr3(),
							room.getContent(),
							UserDto.fromEntity(room.getUser())
				
		);
	}
	
	 public RoomDto(Long id, String name, Integer maxPeople, Integer price, String addr1, String addr2, String addr3, String content, UserDto userDto) {
	        this.id = id;
		 	this.name = name;
	        this.maxPeople = maxPeople;
	        this.price = price;
	        this.addr1 = addr1;
	        this.addr2 = addr2;
	        this.addr3 = addr3;
	        this.content = content;
	        this.userDto = userDto;
	}
	 

 
    
    public RoomDto(Room room) {
        this.name = room.getName();
        this.maxPeople = room.getMaxPeople();
        this.price = room.getPrice();
        this.addr1 = room.getAddr1();
        this.addr2 = room.getAddr2();
        this.addr3 = room.getAddr3();
        this.content = room.getContent();
        //this.userDto = new UserDto(room.getUser().getUid()); // assuming 'uid' is the user identifier
    }


}

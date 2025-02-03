package infra.dto.request;

import infra.dto.UserDto;
import infra.entity.constant.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * 
 *클라이언트에서 서버로 전송되는 요청 데이터
 * */


@ToString
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
	private Long id;
    private String uid;
	private String password;
    private String email;
    private UserRoleType userRoleType;
    private Long businessNum;
    private Boolean hostMode;
    
  
    public static UserRequest of(Long id, String uid, String password, String email, UserRoleType userRoleType, Long businessNum, boolean hostMode) {
    	return new UserRequest(id,uid, password, email, userRoleType, businessNum,hostMode); 
    }
    
    public UserDto toDto(UserRoleType userRole) {
    	return UserDto.of(id,
    					  uid, 
    					  password, 
    					  email, 
    					  userRole,
    					  businessNum,
    					  hostMode,
    					  null,null);
    }
    
}

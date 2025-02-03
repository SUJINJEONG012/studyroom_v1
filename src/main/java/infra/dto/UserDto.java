package infra.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import infra.entity.User;
import infra.entity.constant.UserRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDto {
	private Long id;
    private String uid;
	private String password;
    private String email;
    private UserRoleType roleType;
    private Long businessNum;
    private Boolean hostMode = false;
    
	private LocalDateTime createdDate;  // 자동으로 변환하여 DB에 저장
	private LocalDateTime modifiedDate;
	 
    
   
    public UserDto(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }
    
    
    // UserRoleType을 문자열로 변환
    public String getRoleTypeString() {
        return roleType != null ? roleType.name() : null;
    }
    
    // 간단한 생성자 추가
    public static UserDto of(Long id, String uid,  String password, String email, UserRoleType roleType) {
    	return UserDto.of(id, uid, password, email, roleType, null, null, null, null);
    }
    
    public static UserDto of(Long id, String uid, String password, String email, UserRoleType roleType,
    		Long businessNum, Boolean hostMode, LocalDateTime createdDate, LocalDateTime modifiedDate) {
    	return new UserDto(id, uid, password, email, roleType, businessNum,hostMode, createdDate, modifiedDate);
    }

  

    
 // Entity -> DTO 변환
    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUid(user.getUid());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setRoleType(user.getRoleType()); // roleType을 설정
        dto.setBusinessNum(user.getBusinessNum()); // businessNum을 설정
        dto.setCreatedDate(user.getCreatedDate()); // createdDate 설정
        dto.setModifiedDate(user.getModifiedDate()); // modifiedDate 설정
        return dto;
    }
    
    
    // DTO -> Entity 변환
    public User toEntity() {
    	return User.of(
    					id,
						uid,
						password, 
							
						email, 
						roleType,
						businessNum);
    	}
    
    
    
}

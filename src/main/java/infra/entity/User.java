package infra.entity;



import infra.common.utils.UserRoleTypeAttributeConverter;
import infra.entity.constant.UserRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Entity
public class User extends AuditingFields {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // id is a Long, not String
    private String uid;
    
	@Column(length = 200)
    private String password;

    private String email;
  
    
    @Column(name="business_num")
	private Long businessNum;
    
    private Boolean hostMode; // ROLE_GUEST면 NULL, ROLE_HOST면 TRUE/FALSE
    
	@Column(name = "role_type", columnDefinition = "VARCHAR(50)")
    @Convert(converter = UserRoleTypeAttributeConverter.class)
	private UserRoleType roleType;

	
	
	 
    protected User() {}
    
	private User(Long id, String uid, String password, String email, UserRoleType roleType, Long businessNum) {
		this.id=id;
		this.uid = uid;
		this.password = password;
		this.email = email;
		this.roleType = roleType;
		this.businessNum = businessNum;
	}
	
	
	// 아래방법이 더 효율적
	public static User of(Long id, String uid, String password, String email, UserRoleType roleType, Long businessNum) {
		return new User(
				id, uid, password, email, roleType, businessNum);
	}
	
	
//	public static User of(String uid, String password, String email, UserRoleType roleType, Long businessNum) {
//        User user = new User();
//        user.setUid(uid);
//        user.setPassword(password);
//        user.setEmail(email);
//        user.setRoleType(roleType);
//        user.setBusinessNum(businessNum);
//        return user;
//    }
	
   
}
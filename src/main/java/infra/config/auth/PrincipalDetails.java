package infra.config.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import infra.dto.UserDto;
import infra.entity.User;
import infra.entity.constant.UserRoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;  // User 엔티티를 직접 사용

    private UserDto userDto;

    // userDto를 초기화하는 생성자 추가
    public void setUserDtoFromEntity() {
        this.userDto = UserDto.fromEntity(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRoleType() == null) {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_GUEST"));
        }
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRoleType().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // user 객체에서 password 가져오기
    }

    @Override
    public String getUsername() {
        return user.getUid();  // user 객체에서 uid 가져오기
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	
}
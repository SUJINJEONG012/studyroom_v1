package infra.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
	private String message;
    private String token;
    private String redirectUrl;
}

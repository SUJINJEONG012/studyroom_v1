package infra.common.utils;

import infra.entity.constant.UserRoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Convert
public class UserRoleTypeAttributeConverter implements AttributeConverter<UserRoleType, String>{

	@Override
	public String convertToDatabaseColumn(UserRoleType attribute) {
		return attribute.getRoleType();
	}

	@Override
	public UserRoleType convertToEntityAttribute(String dbData) {
		return UserRoleType.getInstance(dbData);
	}
}
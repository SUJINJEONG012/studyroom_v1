//package infra.config;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//@Converter(autoApply = true)
//public class LocalDateTimeToStringConverter implements AttributeConverter<LocalDateTime, String> {
//
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    @Override
//    public String convertToDatabaseColumn(LocalDateTime localDateTime) {
//        return (localDateTime == null) ? null : localDateTime.format(FORMATTER);
//    }
//
//    @Override
//    public LocalDateTime convertToEntityAttribute(String dbData) {
//        return (dbData == null || dbData.isEmpty()) ? null : LocalDateTime.parse(dbData, FORMATTER);
//    }
//}

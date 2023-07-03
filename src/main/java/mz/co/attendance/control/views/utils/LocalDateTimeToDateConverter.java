package mz.co.attendance.control.views.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    @Override
    public Result<Date> convertToModel(LocalDateTime localDateTime, ValueContext valueContext) {
        return (localDateTime != null) ? Result.ok(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())) : Result.ok(null);
    }

    @Override
    public LocalDateTime convertToPresentation(Date date, ValueContext valueContext) {
        return Objects.nonNull(date) ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }
}
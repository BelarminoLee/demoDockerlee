package mz.co.attendance.control.views.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class LocalDateToDateConverter implements Converter<LocalDate, Date> {

    @Override
    public Result<Date> convertToModel(LocalDate localDate, ValueContext valueContext) {
        return (localDate != null) ? Result.ok(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())) : Result.ok(null);
    }

    @Override
    public LocalDate convertToPresentation(Date date, ValueContext valueContext) {
        return Objects.nonNull(date) ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }
}
package mz.co.attendance.control.views.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToLongConverter implements Converter<String, Long> {


    @Override
    public Result<Long> convertToModel(String s, ValueContext valueContext) {
        return Result.ok(Long.valueOf(s));
    }

    @Override
    public String convertToPresentation(Long aLong, ValueContext valueContext) {
        return aLong.toString();
    }
}

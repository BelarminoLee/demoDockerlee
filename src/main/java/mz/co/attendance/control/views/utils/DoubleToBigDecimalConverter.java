package mz.co.attendance.control.views.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.math.BigDecimal;

public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {
    @Override
    public Result<BigDecimal> convertToModel(Double aDouble, ValueContext valueContext) {
        return Result.ok(BigDecimal.valueOf(aDouble));
    }

    @Override
    public Double convertToPresentation(BigDecimal bigDecimal, ValueContext valueContext) {
        return bigDecimal.doubleValue();
    }
}

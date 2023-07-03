package mz.co.attendance.control.views.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import mz.co.attendance.control.dao.entities.employee.CellPhone;
import mz.co.attendance.control.service.client.RestClientService;

import java.util.Objects;

public class CellPhoneToStringConverter implements Converter<String , CellPhone> {

    private CellPhone cellPhone;

    public CellPhoneToStringConverter(CellPhone cellPhone){
        this.cellPhone = cellPhone;
    }

    @Override
    public Result<CellPhone> convertToModel(String s, ValueContext valueContext) {
        cellPhone.setNumber(s);
        return Result.ok(cellPhone);
    }

    @Override
    public String convertToPresentation(CellPhone cellPhone, ValueContext valueContext) {
        return Objects.nonNull(cellPhone) ? cellPhone.getNumber() : "";
    }
}

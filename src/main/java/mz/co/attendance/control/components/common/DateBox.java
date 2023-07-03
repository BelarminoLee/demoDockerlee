package mz.co.attendance.control.components.common;

import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import mz.co.attendance.control.views.utils.DatePickerType;

import java.util.ArrayList;
import java.util.List;

public class DateBox extends Div {
    private List<EnhancedDateRangePicker> dateRangePickers;
    private List<DatePicker> datePickers;
    private Button addButton;
    private VerticalLayout datePickerArea;
    private DatePickerType datePickerType;

    public DateBox(String title, DatePickerType datePickerType) {
        this.datePickerType = datePickerType;
        add(new Span(title));
        if (datePickerType == DatePickerType.ENHANCED) {
            enhancedDatePicker();
        }
        if (datePickerType == DatePickerType.DATEPICKER) {
            datePicker();
        }
    }

    private void datePicker() {
        datePickers = new ArrayList<>();
        datePickerArea = new VerticalLayout();
        datePickerArea.setPadding(false);
        addButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
        addButton.setTooltipText("Add");
        HorizontalLayout buttonArea = new HorizontalLayout(addButton);
        buttonArea.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        Div dateBox = new Div();
        dateBox.add(datePickerArea, buttonArea);
        add(dateBox);
    }

    private void enhancedDatePicker() {
        dateRangePickers = new ArrayList<>();
        datePickerArea = new VerticalLayout();
        datePickerArea.setPadding(false);
        addButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
        addButton.setTooltipText("Add");
        HorizontalLayout buttonArea = new HorizontalLayout(addButton);
        buttonArea.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        Div dateBox = new Div();
        dateBox.add(datePickerArea, buttonArea);
        add(dateBox);
    }

    public void addNewDatePicker() {
        if (datePickerType == DatePickerType.ENHANCED) {
            addEnhanced();
        }
        if (datePickerType == DatePickerType.DATEPICKER) {
            addDatePicker();

        }
    }

    private void addDatePicker() {
        HorizontalLayout rangeAreaWAct = new HorizontalLayout();
        Button removeButton = new Button(new Icon(VaadinIcon.MINUS_CIRCLE));
        removeButton.setTooltipText("Remove");
        removeButton.getStyle().set("margin-top", "37px");
        DatePicker dtR = new DatePicker("Date");
        removeButton.addClickListener(buttonClickEvent -> {
            if (datePickers.size() > 1) {
                datePickerArea.remove(rangeAreaWAct);
                datePickers.remove(dtR);
            }
        });
        rangeAreaWAct.add(dtR, removeButton);
        datePickerArea.add(rangeAreaWAct);
        datePickers.add(dtR);
    }


    private void addEnhanced() {
        HorizontalLayout rangeAreaWAct = new HorizontalLayout();
        Button removeButton = new Button(new Icon(VaadinIcon.MINUS_CIRCLE));
        removeButton.setTooltipText("Remove");
        removeButton.getStyle().set("margin-top", "37px");
        EnhancedDateRangePicker dtR = new EnhancedDateRangePicker("From - To");
        removeButton.addClickListener(buttonClickEvent -> {
            if (dateRangePickers.size() > 1) {
                datePickerArea.remove(rangeAreaWAct);
                dateRangePickers.remove(dtR);
            }
        });
        rangeAreaWAct.add(dtR, removeButton);
        datePickerArea.add(rangeAreaWAct);
        dateRangePickers.add(dtR);
    }

    public void setAddRangePickerListener(ComponentEventListener listener) {
        this.addButton.addClickListener(listener);
    }

    public void setPattern(String pattern) {
        if (datePickerType == DatePickerType.ENHANCED) {
            for (EnhancedDateRangePicker picker : dateRangePickers) {
                picker.setPattern(pattern);
            }
        }
        if (datePickerType == DatePickerType.DATEPICKER) {
            for (DatePicker datePicker : datePickers) {
                DatePicker.DatePickerI18n format = new DatePicker.DatePickerI18n();
                format.setDateFormat(pattern);
                datePicker.setI18n(format);
            }
        }
    }

    public void clear() {
        if (datePickerType == DatePickerType.ENHANCED) {
            for (EnhancedDateRangePicker picker : dateRangePickers) {
                picker.clear();
            }
        }
        if (datePickerType == DatePickerType.DATEPICKER) {
            for (DatePicker datePicker : datePickers) {
                datePicker.clear();
            }
        }
    }

    public List<EnhancedDateRangePicker> getDateRangePickers() {
        return this.dateRangePickers;
    }
}

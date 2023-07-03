package mz.co.attendance.control.components.attendance;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import mz.co.attendance.control.components.common.NotificationAlert;
import mz.co.attendance.control.components.utils.DateFormatter;
import mz.co.attendance.control.components.utils.NotificationType;
import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.enums.Status;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.utils.LocalDateTimeToDateConverter;
import org.apache.commons.lang3.StringUtils;

public class AttendanceControlForm extends FormLayout {

    private Attendance attendance;
    private Dialog dialog;
    private TextArea observations;
    private DateTimePicker entryDate;
    private DateTimePicker exitDate;
    private Binder<Attendance> binder;
    private Button saveButton;
    private Button cancelButton;
    private Button approveButton;
    private ComboBox<Status> statusComboBox;
    private boolean entryDateChanged;
    private boolean exitDateChanged;
    private RestClientService restClientService;

    public AttendanceControlForm(RestClientService restClientService) {
        this.restClientService = restClientService;
        binder = new BeanValidationBinder<>(Attendance.class);
        addClassName("contact-form");
        dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Edit Attendance");
        FormLayout formLayout = createDialogLayout(dialog);
        dialog.add(formLayout);
    }

    public void openDialog() {
        this.dialog.open();
    }

    private FormLayout createDialogLayout(Dialog dialog) {
        entryDate = new DateTimePicker("Entry Date");
        exitDate = new DateTimePicker("Exit Date");
        cancelButton = new Button("Cancel", e -> dialog.close());
        saveButton = new Button("Save");
        saveButton.setEnabled(false);
        approveButton = new Button("Approve");
        approveButton.setVisible(false);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        approveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        observations = new TextArea("Observations");
        observations.setEnabled(false);
        observations.setValueChangeMode(ValueChangeMode.LAZY);
        observations.addValueChangeListener(textEvent -> {
            if((entryDateChanged || exitDateChanged) && !StringUtils.isBlank(textEvent.getValue())){
                saveButton.setEnabled(true);
            }else{
                saveButton.setEnabled(false);
            }
        });

        statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItemLabelGenerator(Status::getLabel);
        statusComboBox.setItems(Status.values());
        statusComboBox.setEnabled(false);

        HorizontalLayout buttonLayout = setupButtonsAndLayout();
   //     binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        FormLayout formLayout = new FormLayout();
        formLayout.add(entryDate, exitDate, observations, statusComboBox, buttonLayout);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 2));

        return formLayout;
    }

    private HorizontalLayout setupButtonsAndLayout() {
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);
        saveButton.addClickListener(event -> validateAndSave());
        HorizontalLayout buttonLayout = new HorizontalLayout(approveButton, saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        return buttonLayout;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
        binder.forField(entryDate).withConverter(new LocalDateTimeToDateConverter()).bind(Attendance::getEntryDate, Attendance::setEntryDate);
        binder.forField(exitDate).withConverter(new LocalDateTimeToDateConverter()).bind(Attendance::getExitDate, Attendance::setExitDate);
        binder.forField(observations).bind(Attendance::getObservations, Attendance::setObservations);
        binder.forField(statusComboBox).withValidator(status -> {
            if((exitDateChanged || entryDateChanged) && StringUtils.isBlank(observations.getValue())){
                return false;
            }
            return true;
        },"On Entry/Exit dates change a observation must be specified").bind(Attendance::getStatus, Attendance::setStatus);
        binder.bindInstanceFields(this);
        binder.readBean(attendance);
        entryDate.addValueChangeListener(valueChangeEvent -> {
            if (DateFormatter.asDate(valueChangeEvent.getValue()) != attendance.getEntryDate()) {
                entryDateChanged = true;
                observations.setEnabled(true);
            }else {
                entryDateChanged = false;
                if(!exitDateChanged) {
                    observations.setEnabled(false);
                }
            }
        });
        exitDate.addValueChangeListener(valueChangeEvent -> {
            if (DateFormatter.asDate(valueChangeEvent.getValue()) != attendance.getExitDate()) {
                exitDateChanged = true;
                observations.setEnabled(true);
            }else {
                exitDateChanged = false;
                if(!entryDateChanged) {
                    observations.setEnabled(false);
                }
            }
        });
    }

    private void validateAndSave() {
        try {
            if (binder.isValid()) {
                binder.writeBean(attendance);
                dialog.close();
                new NotificationAlert("Changes on attendance info as been submitted for approval!", 8000);
                fireEvent(new SaveEvent(this, attendance));
            } else {
                new NotificationAlert("On Entry/Exit dates change a observation must be specified", 8000, NotificationType.ERROR );
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class AttendanceFormEvent extends ComponentEvent<AttendanceControlForm> {

        private Attendance attendanceControl;


        protected AttendanceFormEvent(AttendanceControlForm source, Attendance attendanceControl) {
            super(source, false);
            this.attendanceControl = attendanceControl;
        }

        public Attendance getAttendanceControl() {
            return attendanceControl;
        }
    }

    public static class SaveEvent extends AttendanceFormEvent {
        SaveEvent(AttendanceControlForm source, Attendance attendanceControl) {
            super(source, attendanceControl);
        }
    }

    public static class ApproveEvent extends AttendanceFormEvent {
        ApproveEvent(AttendanceControlForm source, Attendance attendanceControl) {
            super(source, attendanceControl);
        }

    }

    public static class CloseEvent extends AttendanceFormEvent {
        CloseEvent(AttendanceControlForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

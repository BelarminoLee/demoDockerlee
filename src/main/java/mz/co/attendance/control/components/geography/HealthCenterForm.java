package mz.co.attendance.control.components.geography;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.service.client.RestClientService;

import java.util.Objects;

public class  HealthCenterForm extends Div {

    private HealthCenter healthCenter;
    private Dialog dialog;
    private Binder<HealthCenter> binder;
    private RestClientService restClientService;
    private ComboBox<Province> provinceComboBox;
    private ComboBox<District> districtComboBox;
    private TextField healthCenterName;
    private Button cancelButton;
    private Button saveButton;
    private Button deleteButton;
    private FormLayout formLayout;

    public HealthCenterForm(RestClientService restClientService) {
        this.restClientService = restClientService;
        binder = new BeanValidationBinder<>(HealthCenter.class);
        dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Edit Health Center");
        formLayout = createDialogLayout(dialog);
        dialog.add(formLayout);
    }

    private FormLayout createDialogLayout(Dialog dialog) {
        healthCenterName = new TextField("Site / Health Facility  Name");
        healthCenterName.setRequiredIndicatorVisible(true);
        healthCenterName.setErrorMessage("This field is required");
        provinceComboBox = new ComboBox<>("Province");
        provinceComboBox.setItemLabelGenerator(Province::getName);
        provinceComboBox.setItems(restClientService.getAllProvinces());
        provinceComboBox.addValueChangeListener(selectedValue -> {
            districtComboBox.clear();
            if (Objects.nonNull(selectedValue.getValue())) {
                districtComboBox.setItems(restClientService.getAllDistrictsByProvince(selectedValue.getValue().getId()));
            }
        });
        districtComboBox = new ComboBox<>("District");
        districtComboBox.setItemLabelGenerator(District::getName);

        cancelButton = new Button("Cancel", e -> dialog.close());
        saveButton = new Button("Save", e -> dialog.close());
        deleteButton = new Button("Delete", e -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton, deleteButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        FormLayout formLayout = new FormLayout(provinceComboBox, districtComboBox, healthCenterName, buttonLayout);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 2));
        return formLayout;
    }
    public void setHealthCenter(HealthCenter healthCenter) {
        this.healthCenter = healthCenter;
        binder.forField(healthCenterName).asRequired("The field Name is required").bind(HealthCenter::getName, HealthCenter::setName);
        binder.forField(districtComboBox).asRequired("The District is required").bind(HealthCenter::getDistrict, HealthCenter::setDistrict);
        provinceComboBox.setValue(healthCenter.getDistrict().getProvince());
        binder.readBean(healthCenter);
    }

    public void initNew(){
        deleteButton.setVisible(false);
        cancelButton.setVisible(false);
        dialog.getElement().setAttribute("aria-label", "Add New Health Center");
        add(formLayout);
    }
    public void openDialog() {
        dialog.open();
    }

    public static abstract class HealthCenterFormEvent extends ComponentEvent<HealthCenterForm> {

        private HealthCenter healthCenter;

        protected HealthCenterFormEvent(HealthCenterForm source, HealthCenter healthCenter) {
            super(source, false);
            this.healthCenter = healthCenter;
        }

        public HealthCenter getHealthCenter() {
            return healthCenter;
        }
    }

    public static class SaveEvent extends HealthCenterFormEvent {
        SaveEvent(HealthCenterForm source, HealthCenter healthCenter) {
            super(source, healthCenter);
        }
    }

    public static class DeleteEvent extends HealthCenterFormEvent {
        DeleteEvent(HealthCenterForm source, HealthCenter healthCenter) {
            super(source, healthCenter);
        }
    }

    public static class CloseEvent extends HealthCenterFormEvent {
        CloseEvent(HealthCenterForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

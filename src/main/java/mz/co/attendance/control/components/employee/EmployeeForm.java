package mz.co.attendance.control.components.employee;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.enums.Bank;
import mz.co.attendance.control.enums.Category;
import mz.co.attendance.control.enums.Currency;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.utils.DoubleToBigDecimalConverter;
import mz.co.attendance.control.views.utils.CellPhoneToStringConverter;
import mz.co.attendance.control.views.utils.StringToLongConverter;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNumeric;


public class EmployeeForm extends Div {

    private Employee employee;
    private Dialog dialog;
    private Binder<Employee> binder;
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");
    private TextField name;
    private TextField cellPhone;
    private TextField nuit;
    private TextField account;
    private NumberField subsidy;
    private ComboBox<Currency> currencies;
    private EmailField email;
    private TextField nib;
    private ComboBox<Bank> bank;
    private ComboBox<Category> category;
    private ComboBox<Province> provinceComboBox;
    private ComboBox<District> districtComboBox;
    private ComboBox<HealthCenter> healthCenterComboBox;
    private Button cancelButton;
    private Button saveButton;
    private RestClientService restClientService;
    private FormLayout formLayout;

    private Crud<Employee> crud;
    public EmployeeForm(RestClientService restClientService) {
        this.restClientService = restClientService;
        binder = new BeanValidationBinder<>(Employee.class);
        dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Edit employee");
        formLayout = createFormLayout(dialog);
        dialog.add(formLayout);
    }

    public void openDialog() {
        dialog.open();
    }

    private FormLayout createFormLayout(Dialog dialog) {
        name = new TextField("Name");
        name.setRequiredIndicatorVisible(true);
        name.setErrorMessage("This field is required");
        cellPhone = new TextField("CellPhone");
        nuit = new TextField("Nuit");
        account = new TextField("Account");
        email = new EmailField("Email");
        nib = new TextField("Nib");
        bank = new ComboBox<>("Bank");
        subsidy = new NumberField("Subsidy");
        bank.setItemLabelGenerator(Bank::getLabel);
        bank.setItems(Bank.values());
        category = new ComboBox<>("Category");
        category.setItemLabelGenerator(Category::getLabel);
        category.setItems(Category.values());
        currencies = new ComboBox<>("Currency");
        currencies.setItemLabelGenerator(Currency::getLabel);
        currencies.setItems(Currency.values());
        provinceComboBox = new ComboBox<>("Province");
        provinceComboBox.setItemLabelGenerator(Province::getName);
        provinceComboBox.setItems(restClientService.getAllProvinces());
        provinceComboBox.addValueChangeListener(selectedValue -> {
            healthCenterComboBox.clear();
            districtComboBox.clear();
            healthCenterComboBox.setEnabled(false);
            if (Objects.nonNull(selectedValue.getValue())) {
                districtComboBox.setItems(restClientService.getAllDistrictsByProvince(selectedValue.getValue().getId()));
                districtComboBox.setEnabled(true);
            }
        });
        districtComboBox = new ComboBox<>("District");
        districtComboBox.setEnabled(false);
        districtComboBox.setItemLabelGenerator(District::getName);
        districtComboBox.addValueChangeListener(selectedValue -> {
            healthCenterComboBox.clear();
            if (Objects.nonNull(selectedValue.getValue())) {
                healthCenterComboBox.setItems(restClientService.getAllHealthCentersByDistrict(selectedValue.getValue().getId()));
                healthCenterComboBox.setEnabled(true);
            }
        });
        healthCenterComboBox = new ComboBox<>("Health Center");
        healthCenterComboBox.setEnabled(false);
        healthCenterComboBox.setItems(restClientService.getAllHealthCenters());
        healthCenterComboBox.setItemLabelGenerator(HealthCenter::getName);

        cancelButton = new Button("Cancel", e -> dialog.close());
        saveButton = new Button("Save", e -> dialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout formLayout = new FormLayout();
        formLayout.add(name, cellPhone, nuit, email, bank, account, subsidy, currencies, nib, category, provinceComboBox, districtComboBox, healthCenterComboBox, buttonLayout);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", 3));

        return formLayout;
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employee)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public void   initNew() {
        delete.setVisible(false);
        cancelButton.setVisible(false);
        dialog.getElement().setAttribute("aria-label", "Add New Employee");
        add(formLayout);
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        binder.forField(name).asRequired("The field Name is required").bind(Employee::getName, Employee::setName);
        binder.forField(nuit).asRequired("The field Nuit is required").withValidator(value -> {
            if (isNumeric(value)) {
                return true;
            }
            return false;
        }, "The field must be a number").withConverter(new StringToLongConverter()).bind(Employee::getNuit, Employee::setNuit);
        binder.forField(email).bind(Employee::getEmail, Employee::setEmail);
        binder.forField(bank).bind(Employee::getBank, Employee::setBank);
        binder.forField(subsidy).asRequired("The field Subsidy is required").withConverter(new DoubleToBigDecimalConverter()).bind(Employee::getSubsidy, Employee::setSubsidy);
        binder.forField(currencies).asRequired("The field Currency is mandatory").bind(Employee::getSubsidyCurrency, Employee::setSubsidyCurrency);
        binder.forField(healthCenterComboBox).asRequired("The field Health Center is mandatory").bind(Employee::getHealthCenter, Employee::setHealthCenter);
        binder.forField(account).withValidator(value -> {
            if (isNumeric(value)) {
                return true;
            }
            return false;
        }, "The field must be a number").withConverter(new StringToLongConverter()).bind(Employee::getAccount, Employee::setAccount);
        binder.forField(nib).withValidator(value -> {
            if (isNumeric(value)) {
                return true;
            }
            return false;
        }, "The field must be a number").withConverter(new StringToLongConverter()).bind(Employee::getNib, Employee::setNib);
        binder.forField(cellPhone).asRequired("The field number is required").withConverter(new CellPhoneToStringConverter(employee.getCellphone())).bind(Employee::getCellphone, Employee::setCellphone);
        binder.forField(category).asRequired("Select a category").bind(Employee::getCategory, Employee::setCategory);
        binder.bindInstanceFields(this);
        provinceComboBox.setValue(employee.getHealthCenter().getDistrict().getProvince());
        districtComboBox.setValue(employee.getHealthCenter().getDistrict());
        healthCenterComboBox.setValue(employee.getHealthCenter());
        binder.readBean(employee);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(employee);
            fireEvent(new SaveEvent(this, employee));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {

        private Employee employee;

        protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
            super(source, false);
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }
    }

    public static class SaveEvent extends EmployeeFormEvent {
        SaveEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class DeleteEvent extends EmployeeFormEvent {
        DeleteEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }

    }

    public static class CloseEvent extends EmployeeFormEvent {
        CloseEvent(EmployeeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}


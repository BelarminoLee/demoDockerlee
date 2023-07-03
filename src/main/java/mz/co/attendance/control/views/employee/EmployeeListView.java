package mz.co.attendance.control.views.employee;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.common.PaginatedGrid;
import mz.co.attendance.control.components.employee.EmployeeForm;
import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.enums.Bank;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static mz.co.attendance.control.views.utils.BankLogoUtil.getBankLogoURL;

@PageTitle("Employee Management")
@Route(value = "management/employee/edit", layout = ManagementLayout.class)
public class EmployeeListView extends HorizontalLayout {

    public static final String TOOLTIPTEXT = "tooltiptext";
    public static final String EDIT_ACTION = "edit-action";
    private Icon editIcon;
    private EmployeeForm form;
    private static final String TOOLTIP = "tooltip";
    private RestClientService restClientService;
    private int currentPage = 0;
    private int maxResults = 10;
    private PaginatedGrid<Employee, ?> grid;
    private TextField cellPhoneFilter;
    private TextField nuitFilter;
    private ComboBox<Bank> bankFilter;
    private ComboBox<HealthCenter> healthCenterFilter;
    private ComboBox<District> districtFilter;
    private ComboBox<Province> provinceFilter;
    private ComboBox<String> statusFilter;

    public EmployeeListView(RestClientService restClientService) {
        this.restClientService = restClientService;
        addClassName("listEmployee-view");
        setSizeFull();
        configureGrid();
        form = new EmployeeForm(restClientService);
        form.setWidth("25em");
        form.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
        form.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmployee);
        form.addListener(EmployeeForm.CloseEvent.class, this::closeEditor);
        VerticalLayout gridBox = new VerticalLayout(buildFilter(), grid);
        add(gridBox);
        updateList();
    }
    private VerticalLayout buildFilter() {
        cellPhoneFilter = new TextField("CellPhone");
        nuitFilter = new TextField("Nuit");
        bankFilter = new ComboBox<>("Bank");
        bankFilter.setItemLabelGenerator(Bank::getLabel);
        bankFilter.setItems(Bank.values());
        provinceFilter = new ComboBox<>("Province");
        provinceFilter.setItems(restClientService.getAllProvinces());
        provinceFilter.setItemLabelGenerator(Province::getName);
        provinceFilter.addValueChangeListener(selectedValue -> {
            healthCenterFilter.clear();
            districtFilter.clear();
            healthCenterFilter.setEnabled(false);
            if (Objects.nonNull(selectedValue.getValue())) {
                districtFilter.setItems(restClientService.getAllDistrictsByProvince(selectedValue.getValue().getId()));
                districtFilter.setEnabled(true);
            }
        });
        districtFilter = new ComboBox<>("District");
        districtFilter.setEnabled(false);
        districtFilter.setItemLabelGenerator(District::getName);
        districtFilter.addValueChangeListener(selectedValue -> {
            healthCenterFilter.clear();
            if (Objects.nonNull(selectedValue.getValue())) {
                healthCenterFilter.setItems(restClientService.getAllHealthCentersByDistrict(selectedValue.getValue().getId()));
                healthCenterFilter.setEnabled(true);
            }
        });
        healthCenterFilter = new ComboBox<>("Health Center");
        healthCenterFilter.setEnabled(false);
        healthCenterFilter.setItems(restClientService.getAllHealthCenters());
        healthCenterFilter.setItemLabelGenerator(HealthCenter::getName);
        statusFilter = new ComboBox<>("Status");
        loadStatusFilter();
        Button filter = new Button("Filter");
        filter.setIcon(new Icon(VaadinIcon.SEARCH));
        Button clear = new Button("Reset");
        clear.addClickListener(buttonClickEvent -> {
            cellPhoneFilter.clear();
            nuitFilter.clear();
            bankFilter.clear();
            bankFilter.setItems(Bank.values());
            statusFilter.clear();
            loadStatusFilter();
            healthCenterFilter.clear();
            healthCenterFilter.setEnabled(false);
            districtFilter.clear();
            districtFilter.setEnabled(false);
            provinceFilter.clear();
            provinceFilter.setItems(restClientService.getAllProvinces());
        });
        clear.setIcon(new Icon(VaadinIcon.ERASER));
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout filters1 = new HorizontalLayout();
        HorizontalLayout filters2 = new HorizontalLayout();
        filters1.add(cellPhoneFilter, nuitFilter, bankFilter, statusFilter);
        filters2.add(provinceFilter, districtFilter, healthCenterFilter);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(filter, clear);
        verticalLayout.add(filters1, filters2, buttons);
        return verticalLayout;
    }

    private void loadStatusFilter() {
        List<String> status = new ArrayList<>();
        status.add("Enabled");
        status.add("Disabled");
        statusFilter.setItems(status);
    }

    private void configureGrid() {
        grid = new PaginatedGrid<>(Employee.class);
        grid.addClassNames("employee-grid");
        grid.setMaxWidth("100%");
        grid.getStyle().set("margin-left", "14px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(this::getActionsContainer).setHeader("Actions").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createEmployeeRenderer()).setHeader("Employee").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createEmployeeBankRenderer()).setHeader("Bank").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createEmployeeLocationRenderer()).setHeader("Location").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(employee -> employee.getCategory().getLabel()).setHeader("Category").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createEmployeeSubsidyRenderer()).setHeader("Subsidy").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setAutoWidth(true).setFlexGrow(0);
        grid.setPageSize(1);
        grid.setPaginatorSize(1);
    }

    private static Renderer<Employee> createEmployeeRenderer() {
        return LitRenderer.<Employee>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "<vaadin-avatar  name=\"${item.name}\" alt=\"User avatar\"></vaadin-avatar>" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.name} </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      Email: ${item.email}" + "    </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      CellPhone: ${item.cellphone}" + "    </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      Nuit: ${item.nuit}" + "    </span>" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("name", Employee::getName).withProperty("email", Employee::getEmail).withProperty("cellphone", Employee::getCellphoneNumber).withProperty("nuit", Employee::getNuit);
    }

    private static Renderer<Employee> createEmployeeLocationRenderer() {
        return LitRenderer.<Employee>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.province} </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      District: ${item.district}" + "    </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      Site / Health Facility: ${item.health_center}" + "    </span>" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("province", employee -> employee.getHealthCenter().getDistrict().getProvince().getName()).withProperty("district", employee -> employee.getHealthCenter().getDistrict().getName()).withProperty("health_center", employee -> employee.getHealthCenter().getName());
    }

    private static Renderer<Employee> createEmployeeSubsidyRenderer() {
        return LitRenderer.<Employee>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.subsidy}  <b>${item.currency}</b>  </span>" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("subsidy", employee -> employee.getSubsidy()).withProperty("currency", employee -> employee.getSubsidyCurrency().getLabel());
    }

    private static Renderer<Employee> createEmployeeBankRenderer() {
        return LitRenderer.<Employee>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "<vaadin-avatar img=\"${item.logo}\" name=\"${item.name}\" alt=\"Bank avatar\"></vaadin-avatar>" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.name} </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      Account: ${item.account}" + "    </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      Nib: ${item.nib}" + "    </span>" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("logo", employee -> getBankLogoURL(Bank.STB)).withProperty("name", Employee::getBankName).withProperty("account", Employee::getAccount).withProperty("nib", Employee::getNib);
    }

    private static final SerializableBiConsumer<Span, Employee> statusComponentUpdater = (span, employee) -> {
        String theme = String.format("badge %s", employee.isEnabled() ? "success" : "error");
        span.getElement().setAttribute("theme", theme);
        span.setText(employee.isEnabled() ? "Enabled" : "Disabled");
    };

    private static ComponentRenderer<Span, Employee> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private Div getActionsContainer(Employee employee) {
        return addEditAction(employee);
    }

    private Div addEditAction(Employee employee) {
        editIcon = new Icon(VaadinIcon.EDIT);
        editIcon.setClassName(EDIT_ACTION);
        editIcon.getStyle().set("margin", "0 auto");
        Button editBtn= new Button(editIcon);
        editBtn.setTooltipText("Edit");
        Div actionsContainer = new Div();
        actionsContainer.add(editBtn);
        actionsContainer.getStyle().set("display", "flex");
        editBtn.addClickListener(click -> this.editEmployee(employee));
        onClickEditAction(click -> this.editEmployee(employee));
        return actionsContainer;
    }

    private void onClickEditAction(ComponentEventListener<ClickEvent<Icon>> listener) {
        this.editIcon.addClickListener(listener);
    }

    private static String getHealthCenterName(Employee employee) {
        return Objects.nonNull(employee.getHealthCenter()) ? employee.getHealthCenter().getName() : "";
    }

    private void saveEmployee(EmployeeForm.SaveEvent event) {
        // restClientService.r(event.getEmployee());
        updateList();
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        //   service.delete(event.getEmployee());
        updateList();
    }


    private void closeEditor(EmployeeForm.CloseEvent event) {
    }

    public void editEmployee(Employee employee) {
        if (employee == null) {
        } else {
            form.setEmployee(employee);
            form.setVisible(true);
            form.openDialog();
            addClassName("editing");
            grid.asSingleSelect().clear();
        }
    }

    private void updateList() {
        grid.setItems(restClientService.getAllEmployees(currentPage, maxResults).toList());
        grid.getDataProvider().refreshAll();
    }


}

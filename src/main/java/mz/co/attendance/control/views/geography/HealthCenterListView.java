package mz.co.attendance.control.views.geography;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.common.PaginatedGrid;
import mz.co.attendance.control.components.geography.HealthCenterForm;
import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;

import java.util.Objects;

@PageTitle("Geography Management")
@Route(value = "management/geography/edit", layout = ManagementLayout.class)
public class HealthCenterListView extends VerticalLayout {

    public static final String TOOLTIPTEXT = "tooltiptext";
    public static final String EDIT_ACTION = "edit-action";
    private static final String TOOLTIP = "tooltip";
    private final RestClientService restClientService;
    private Icon editIcon;
    private final HealthCenterForm form;
    private final int currentPage = 0;
    private final int maxResults = 10;
    private PaginatedGrid<HealthCenter, ?> grid;
    private ComboBox<Province> provinceComboBox;
    private ComboBox<District> districtComboBox;
    private TextField healthCenterName;

    public HealthCenterListView(RestClientService restClientService) {
        this.restClientService = restClientService;
        addClassName("listHealthCenter-view");
        setSizeFull();
        configureGrid();
        form = new HealthCenterForm(restClientService);
        form.setWidth("25em");
        form.addListener(HealthCenterForm.SaveEvent.class, this::saveHealthCenter);
        form.addListener(HealthCenterForm.DeleteEvent.class, this::deleteHealthCenter);
        form.addListener(HealthCenterForm.CloseEvent.class, this::closeEditor);
        VerticalLayout gridBox = new VerticalLayout(buildFilter(), grid);
        add(gridBox);
        updateList();
    }

    private void configureGrid() {
        grid = new PaginatedGrid<>(HealthCenter.class);
        grid.addClassNames("health-center-grid");
        grid.setMaxWidth("100%");
        grid.getStyle().set("margin-left", "14px");
        grid.setMultiSort(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(this::getActionsContainer).setHeader("Actions").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(healthCenter -> healthCenter.getDistrict().getProvince().getName()).setHeader("Province").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(healthCenter -> healthCenter.getDistrict().getName()).setHeader("District").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(HealthCenter::getName).setHeader("Health Center Name").setAutoWidth(true).setFlexGrow(0);
        grid.setPageSize(1);
        grid.setPaginatorSize(1);
    }

    private Div getActionsContainer(HealthCenter healthCenter) {
        return addEditAction(healthCenter);
    }

    private Div addEditAction(HealthCenter healthCenter) {
        editIcon = new Icon(VaadinIcon.EDIT);
        editIcon.setClassName(EDIT_ACTION);
        editIcon.getStyle().set("margin", "0 auto");
        Div actionsContainer = new Div();
        Span span = new Span("Edit");
        span.addClassName(TOOLTIPTEXT);
        actionsContainer.addClassName(TOOLTIP);
        actionsContainer.add(span);
        actionsContainer.add(editIcon);
        actionsContainer.getStyle().set("display", "flex");
        span.addClickListener(click -> this.editHealthCenter(healthCenter));
        onClickEditAction(click -> this.editHealthCenter(healthCenter));
        return actionsContainer;
    }

    private void editHealthCenter(HealthCenter healthCenter) {
        if (healthCenter == null) {
        } else {
            form.setHealthCenter(healthCenter);
            form.setVisible(true);
            form.openDialog();
            addClassName("editing");
            grid.asSingleSelect().clear();
        }
    }

    private void onClickEditAction(ComponentEventListener<ClickEvent<Icon>> listener) {
        this.editIcon.addClickListener(listener);
    }

    private void updateList() {
        grid.setItems(restClientService.getAllHealthCenters(currentPage, maxResults).toList());
        grid.getDataProvider().refreshAll();
    }

    private VerticalLayout buildFilter() {
        healthCenterName = new TextField("Heath Center Name");
        provinceComboBox = new ComboBox<>("Province");
        provinceComboBox.setItems(restClientService.getAllProvinces());
        provinceComboBox.setItemLabelGenerator(Province::getName);
        provinceComboBox.addValueChangeListener(selectedValue -> {
            districtComboBox.clear();
            if (Objects.nonNull(selectedValue.getValue())) {
                districtComboBox.setItems(restClientService.getAllDistrictsByProvince(selectedValue.getValue().getId()));
                districtComboBox.setEnabled(true);
            }
        });
        districtComboBox = new ComboBox<>("District");
        districtComboBox.setEnabled(false);
        districtComboBox.setItemLabelGenerator(District::getName);
        Button filter = new Button("Filter");
        filter.setIcon(new Icon(VaadinIcon.SEARCH));
        Button clear = new Button("Reset");
        clear.addClickListener(buttonClickEvent -> {
            healthCenterName.clear();
            districtComboBox.clear();
            districtComboBox.setEnabled(false);
            provinceComboBox.clear();
            provinceComboBox.setItems(restClientService.getAllProvinces());
        });
        clear.setIcon(new Icon(VaadinIcon.ERASER));

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout filters1 = new HorizontalLayout();
        filters1.add(provinceComboBox, districtComboBox, healthCenterName);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(filter, clear);
        verticalLayout.add(filters1, buttons);
        return verticalLayout;
    }

    private void deleteHealthCenter(HealthCenterForm.DeleteEvent event) {
        updateList();
    }

    private void saveHealthCenter(HealthCenterForm.SaveEvent event) {
        updateList();
    }

    private void closeEditor(HealthCenterForm.CloseEvent event) {

    }

}

package mz.co.attendance.control.views.attendance;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.attendance.AttendanceControlForm;
import mz.co.attendance.control.components.common.ConfirmDialog;
import mz.co.attendance.control.components.common.NotificationAlert;
import mz.co.attendance.control.components.common.PaginatedGrid;
import mz.co.attendance.control.components.utils.DateFormatter;
import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.repositories.AttendanceControlRepository;
import mz.co.attendance.control.enums.Status;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;

import java.util.Objects;
import java.util.Set;

@PageTitle("Attendance Management")
@Route(value = "management/attendance/all", layout = ManagementLayout.class)
public class AttendanceControlView extends VerticalLayout {
    private AttendanceControlForm form;
    private final RestClientService restClientService;
    public static final String TOOLTIPTEXT = "tooltiptext";
    public static final String EDIT_ACTION = "edit-action";
    public static final String APPROVE_ACTION = "approve-action";
    private static final String TOOLTIP = "tooltip";
    private final AttendanceControlRepository service;
    private int currentPage = 0;
    private int maxResults = 10;
    private Icon editIcon;
    private Icon approveIcon;
    private ComboBox<Status> statusFilter;
    private DatePicker entryDateFilter;
    private DatePicker exitDateFilter;
    private Button enableMultiSelect;
    private Button cancelMultiSelect;
    private Button bulkApproval;
    private PaginatedGrid<Attendance, ?> grid;

    public AttendanceControlView(RestClientService restClientService, AttendanceControlRepository service) {
        this.service = service;
        this.restClientService = restClientService;
        addClassName("attendance-view");
        setSizeFull();
        configureGrid();
        form = new AttendanceControlForm(restClientService);
        form.setWidth("25em");
        VerticalLayout gridBox = new VerticalLayout(buildFilter(), multiSelectEnabler(), grid);
        add(gridBox);
        updateList();
    }

    private HorizontalLayout multiSelectEnabler() {
        enableMultiSelect = new Button("Enable Multi Select");
        enableMultiSelect.setIcon(new Icon(VaadinIcon.LIST_SELECT));
        enableMultiSelect.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancelMultiSelect = new Button("Cancel");
        cancelMultiSelect.setVisible(false);
        cancelMultiSelect.setIcon(new Icon(VaadinIcon.BACKSPACE));
        cancelMultiSelect.addThemeVariants(ButtonVariant.LUMO_ERROR);
        bulkApproval = new Button("Approve");
        bulkApproval.setIcon(new Icon(VaadinIcon.CHECK));
        bulkApproval.setVisible(false);
        bulkApproval.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        bulkApproval.addClickListener(buttonClickEvent -> {
            int totalSelected = grid.getSelectedItems().size();
            ConfirmDialog dialog = new ConfirmDialog("Attendance approval", "Are you sure you want to approve" + totalSelected + " selected attendance" + (totalSelected > 1 ? "s" : "") + " ?");
            dialog.addOnConfirmListener(() -> {
                approveBulkAttendance(grid.getSelectedItems(), totalSelected);
            });
            dialog.show();
        });
        enableMultiSelect.addClickListener(buttonClickEvent -> {
            grid.setSelectionMode(Grid.SelectionMode.MULTI);
            grid.asMultiSelect().addSelectionListener(event -> {
                if (event.getAllSelectedItems().size() > 0) {
                    bulkApproval.setVisible(true);
                } else {
                    bulkApproval.setVisible(false);
                }
            });
            cancelMultiSelect.setVisible(true);
            enableMultiSelect.setVisible(false);
        });

        cancelMultiSelect.addClickListener(buttonClickEvent -> {
            grid.deselectAll();
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            cancelMultiSelect.setVisible(false);
            enableMultiSelect.setVisible(true);
        });

        HorizontalLayout multiSelectArea = new HorizontalLayout();
        multiSelectArea.getStyle().set("margin-left", "14px");
        multiSelectArea.add(enableMultiSelect, cancelMultiSelect, bulkApproval);
        return multiSelectArea;
    }

    private void approveBulkAttendance(Set<Attendance> attendances, int total) {
        new NotificationAlert(total + "Attendance" + (total > 1 ? "s" : "") + "approved successfully!", 8000);
    }

    private VerticalLayout buildFilter() {
        statusFilter = new ComboBox<>("Status");
        statusFilter.setItemLabelGenerator(Status::getLabel);
        statusFilter.setItems(Status.values());
        entryDateFilter = new DatePicker("Entry Date");
        exitDateFilter = new DatePicker("Exit Date");
        Button filter = new Button("Filter");
        filter.setIcon(new Icon(VaadinIcon.SEARCH));
        Button clear = new Button("Reset");
        clear.addClickListener(buttonClickEvent -> {
            statusFilter.clear();
            statusFilter.setItems(Status.values());
            entryDateFilter.clear();
            exitDateFilter.clear();
        });
        clear.setIcon(new Icon(VaadinIcon.ERASER));
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout filters1 = new HorizontalLayout();
        filters1.add(entryDateFilter, exitDateFilter, statusFilter);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(filter, clear);
        verticalLayout.add(filters1, buttons);
        return verticalLayout;
    }

    private void configureGrid() {
        grid = new PaginatedGrid<>(Attendance.class);
        grid.addClassNames("attendance-grid");
        grid.setMaxWidth("100%");
        grid.getStyle().set("margin-left", "14px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addComponentColumn(this::getActionsContainer).setHeader("Actions").setWidth("120px").setFlexGrow(0);
        grid.addColumn(createEmployeeRenderer()).setHeader("Employee").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createEntryExitRenderer()).setHeader("Attendance").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createApproverRenderer()).setHeader("Approver").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(attendance -> attendance.getObservations()).setHeader("Observations").setAutoWidth(true).setFlexGrow(0);
        grid.setPageSize(1);
        grid.setPaginatorSize(1);
    }


    private static final SerializableBiConsumer<Span, Attendance> statusComponentUpdater = (span, employee) -> {
        String theme = String.format("badge %s", employee.getStatus() == Status.APPROVED ? "success" : "error");
        span.getElement().setAttribute("theme", theme);
        span.setText(employee.getStatus().getLabel());
    };

    private static ComponentRenderer<Span, Attendance> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private static Renderer<Attendance> createEmployeeRenderer() {
        return LitRenderer.<Attendance>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "<vaadin-avatar  name=\"${item.name}\" alt=\"User avatar\"></vaadin-avatar>" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.name} </span>" + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "      CellPhone: ${item.cellphone}" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("name", attendance -> attendance.getEmployee().getName()).withProperty("cellphone", attendance -> attendance.getEmployee().getCellphone().getNumber());
    }

    private static Renderer<Attendance> createApproverRenderer() {
        return LitRenderer.<Attendance>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "<vaadin-avatar  name=\"${item.name}\" alt=\"User avatar\"></vaadin-avatar>" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.name} </span>" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("name", attendance -> Objects.nonNull(attendance.getApprover()) ? attendance.getApprover().getName() : "No Approver");
    }

    private static Renderer<Attendance> createEntryExitRenderer() {
        return LitRenderer.<Attendance>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> Entry: ${item.entry} </span>" + "    <span>" + "      Exit: ${item.exit}" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("entry", attendance -> DateFormatter.date(attendance.getEntryDate())).withProperty("exit", attendance -> DateFormatter.date(attendance.getExitDate()));
    }

    private Div getActionsContainer(Attendance attendance) {
        Div actionsContainer = new Div(addEditAction(attendance), addApproveAction(attendance));
        actionsContainer.getStyle().set("display", "flex");
        return actionsContainer;
    }

    private Div addEditAction(Attendance attendance) {
        editIcon = new Icon(VaadinIcon.EDIT);
        editIcon.setClassName(EDIT_ACTION);
        Button editBnt= new Button(editIcon);
        editBnt.setTooltipText("Edit");
        Div actionsContainer = new Div();
        actionsContainer.add(editBnt);
        actionsContainer.getStyle().set("display", "flex");
        editBnt.addClickListener(click -> this.editAttendance(attendance));
        onClickEditAction(click -> this.editAttendance(attendance));
        return actionsContainer;
    }

    private Div addApproveAction(Attendance attendance) {
        approveIcon = new Icon(VaadinIcon.CHECK_SQUARE);
        approveIcon.setClassName(EDIT_ACTION);
        Button approveBtn= new Button(editIcon);
        approveBtn.setTooltipText("Approve");
        Div actionsContainer = new Div();
        actionsContainer.add(approveBtn);
        actionsContainer.getStyle().set("display", "flex");
        approveBtn.addClickListener(click -> this.approveAttendance(attendance));
        onClickApproveAction(click -> this.approveAttendance(attendance));
        return actionsContainer;
    }


    private void saveAttendanceControl(AttendanceControlForm.SaveEvent event) {
        service.save(event.getAttendanceControl());
        updateList();
        closeEditor();
    }


    public void editAttendance(Attendance attendance) {
        if (attendance == null) {
            closeEditor();
        } else {
            form.setAttendance(attendance);
            form.setVisible(true);
            form.openDialog();
            addClassName("editing");
        }
    }

    public void approveAttendance(Attendance attendance) {
        if (attendance != null) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setTitle("Attendance approval");
            confirmDialog.setDescription("Are you sure you want to approve this attendance?");
            confirmDialog.addOnConfirmListener(() -> {
                new NotificationAlert("Attendance approved successfully!", 8000);
            });
            confirmDialog.show();
        }
    }

    void addAttendance() {
        grid.asSingleSelect().clear();
        editAttendance(new Attendance());
    }

    private void closeEditor() {
        form.setAttendance(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void onClickEditAction(ComponentEventListener<ClickEvent<Icon>> listener) {
        this.editIcon.addClickListener(listener);
    }

    private void onClickApproveAction(ComponentEventListener<ClickEvent<Icon>> listener) {
        this.approveIcon.addClickListener(listener);
    }


    private void updateList() {
        grid.setItems(restClientService.getAllAttendance(currentPage, maxResults).toList());
    }

}

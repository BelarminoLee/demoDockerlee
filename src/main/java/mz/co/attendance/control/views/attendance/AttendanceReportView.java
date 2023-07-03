package mz.co.attendance.control.views.attendance;

import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.common.DateBox;
import mz.co.attendance.control.components.common.PaginatedGrid;
import mz.co.attendance.control.dao.entities.attendance.AttendanceReport;
import mz.co.attendance.control.dao.entities.attendance.AttendanceReportRaw;
import mz.co.attendance.control.dao.entities.attendance.DateRange;
import mz.co.attendance.control.dao.entities.attendance.ReportRequest;
import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.enums.Status;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;
import mz.co.attendance.control.views.utils.DatePickerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@PageTitle("Attendance Management")
@Route(value = "management/attendance/report", layout = ManagementLayout.class)
public class AttendanceReportView extends VerticalLayout {

    private ComboBox<Status> statusFilter;
    private DateBox dateRangeBox;

    private DateBox excludedDatesBox;

    private ComboBox<HealthCenter> healthCenterFilter;
    private ComboBox<District> districtFilter;
    private ComboBox<Province> provinceFilter;
    private Checkbox includeWeekends;
    private final RestClientService restClientService;
    private ProgressBar spinner;
    private VerticalLayout gridBox;
    private int currentPage = 0;
    private int maxResults = 10;

    public AttendanceReportView(RestClientService restClientService) {
        this.restClientService = restClientService;
        setSizeFull();
        H3 title = new H3("Filters");
        spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        spinner.setVisible(false);
        gridBox = new VerticalLayout(title, buildFilter(), spinner);
        add(gridBox);
    }

    private VerticalLayout buildFilter() {
        statusFilter = new ComboBox<>("Status");
        statusFilter.setItemLabelGenerator(Status::getLabel);
        statusFilter.setItems(Status.values());
        dateRangeBox = new DateBox("Date Ranges", DatePickerType.ENHANCED);
        dateRangeBox.addNewDatePicker();
        dateRangeBox.setPattern("dd/MMM/yyyy");
        dateRangeBox.setAddRangePickerListener(componentEvent -> {
            dateRangeBox.addNewDatePicker();
        });
        excludedDatesBox = new DateBox("Exclude Date", DatePickerType.DATEPICKER);
        excludedDatesBox.addNewDatePicker();
        excludedDatesBox.setPattern("dd/MMM/yyyy");
        excludedDatesBox.setAddRangePickerListener(componentEvent -> {
            excludedDatesBox.addNewDatePicker();
        });
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
        healthCenterFilter = new ComboBox<>("Site / Health Facility");
        healthCenterFilter.setEnabled(false);
        healthCenterFilter.setItems(restClientService.getAllHealthCenters());
        healthCenterFilter.setItemLabelGenerator(HealthCenter::getName);
        Button preview = new Button("Preview", event -> {
            UI ui = UI.getCurrent();
            ui.setPollInterval(500);
            spinner.setVisible(true);
            gridBox.add(genPreviewReport());
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ui.access(() -> {
                    ui.setPollInterval(-1);
                    spinner.setVisible(false);
                });
            });
        });
        preview.setIcon(new Icon(VaadinIcon.SEARCH));

        Button downloadBtn = new Button("Download");
        downloadBtn.setIcon(new Icon(VaadinIcon.DOWNLOAD));
        Button clear = new Button("Reset");
        clear.addClickListener(buttonClickEvent -> {
            statusFilter.clear();
            statusFilter.setItems(Status.values());
            dateRangeBox.clear();
            excludedDatesBox.clear();
            healthCenterFilter.clear();
            healthCenterFilter.setEnabled(false);
            districtFilter.clear();
            districtFilter.setEnabled(false);
            provinceFilter.clear();
            provinceFilter.setItems(restClientService.getAllProvinces());
        });
        includeWeekends = new Checkbox("Include Weekends");
        clear.setIcon(new Icon(VaadinIcon.ERASER));
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout filters2 = new HorizontalLayout();
        filters2.add(statusFilter, provinceFilter, districtFilter, healthCenterFilter);
        filters2.setJustifyContentMode(JustifyContentMode.START);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(preview, downloadBtn, clear);
        verticalLayout.add(filters2, dateRangeBox, excludedDatesBox, includeWeekends, buttons);
        verticalLayout.setPadding(false);
        return verticalLayout;
    }

    private VerticalLayout genPreviewReport() {
        VerticalLayout reportArea = new VerticalLayout();
        reportArea.setPadding(false);
        H3 title = new H3("REGISTO DE ASSIDUIDADE DOS VOLUNTÁRIOS");

        reportArea.add(title);
        reportArea.setSizeFull();
        List<DateRange> dateRanges = new ArrayList<>();
        for (EnhancedDateRangePicker picker : dateRangeBox.getDateRangePickers()) {
            dateRanges.add(new DateRange(picker.getValue().getStartDate(), picker.getValue().getEndDate()));
        }
        ReportRequest request = new ReportRequest();
        request.setDateRanges(dateRanges);
        request.setDistrict(districtFilter.getValue());
        request.setProvince(provinceFilter.getValue());
        request.setStatus(statusFilter.getValue());
        request.setHealthCenter(healthCenterFilter.getValue());
        AttendanceReport attendanceReport = restClientService.getAttendanceReport(request);

        Label periodLbl = new Label("Period: ");
        Label provinceLbl = new Label("Province: " + request.getProvince().getName());
        Label districtLbl = new Label("District: " + request.getDistrict().getName());
        Label healthFacilityLbl = new Label("Site / Health Facility: " + request.getHealthCenter().getName());
        Label countableLbl = new Label("Total Countable Days: " + attendanceReport.getCountableDays());

        reportArea.add(periodLbl, countableLbl, provinceLbl, districtLbl, healthFacilityLbl);

        PaginatedGrid<AttendanceReportRaw, ?> reportRawGrid = new PaginatedGrid<>();
        reportRawGrid.addClassNames("report-grid");
        reportRawGrid.setMaxWidth("100%");
        reportRawGrid.getStyle().set("margin-left", "14px");
        reportRawGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        reportRawGrid.addColumn(AttendanceReportRaw::getId).setHeader("N#").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(AttendanceReportRaw::getName).setHeader("Name").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getDistrict().getName()).setHeader("District").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getHealthCenter().getName()).setHeader("Site / Health Facility").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getCategory().getLabel()).setHeader("Category").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getBank().getLabel()).setHeader("Bank").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getAccount()).setHeader("Account").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getNib()).setHeader("NIB").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getSubsidy()).setHeader("Subsidy").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getEffectiveness()).setHeader("Effectiveness").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.addColumn(attendanceReportRaw -> attendanceReportRaw.getEffectiveness()).setHeader("Total to Pay").setAutoWidth(true).setFlexGrow(0);
        reportRawGrid.setPageSize(1);
        reportRawGrid.setPaginatorSize(1);

        reportArea.add(reportRawGrid);
        return reportArea;
    }


    private void downloadReport() {

    }

}

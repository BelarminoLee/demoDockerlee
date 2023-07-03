package mz.co.attendance.control.views.management;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import mz.co.attendance.control.views.MainLayout;

import java.util.List;

@PageTitle("Management")
@JsModule("./themes/attendance-control/views/management-view.js")
@Tag("management-view")
@ParentLayout(MainLayout.class)
public class ManagementLayout extends PolymerTemplate<TemplateModel> implements RouterLayout, AfterNavigationObserver {
    @Id
    private Element menuGeoMgnt;
    @Id
    private Element menuGeoMgntTitle;
    @Id
    private Element submenuGeo;
    @Id
    private Element menuAllGeo;
    @Id
    private Element menuCreateNewGeography;
    @Id
    private Element menuEmployeeMgnt;
    @Id
    private Element menuEmployeeMgntTitle;
    @Id
    private Element submenuEmployee;
    @Id
    private Element menuCreateNewEmployee;
    @Id
    private Element menuAllEmployee;
    @Id
    private Element menuAttendanceMgnt;
    @Id
    private Element menuAttendanceMgntTitle;
    @Id
    private Element submenuAttendance;
    @Id
    private Element menuReportManagement;
    @Id
    private Element menuAllAttendance;
    @Id
    private Element menuSystemMgnt;
    @Id
    private Element menuSystemMgntTitle;
    @Id
    private Element submenuSystem;
    @Id
    private Element menuUSSD;
    @Id
    private Element menuUsers;

    private void deselectAllMenus() {
        menuGeoMgnt.getClassList().remove("selected");
        submenuGeo.getClassList().remove("selected");
        submenuGeo.getStyle().set("display", "none");
        menuCreateNewGeography.getClassList().remove("selected");
        menuAllGeo.getClassList().remove("selected");
        menuEmployeeMgnt.getClassList().remove("selected");
        submenuEmployee.getClassList().remove("selected");
        submenuEmployee.getStyle().set("display", "none");
        menuCreateNewEmployee.getClassList().remove("selected");
        menuAllEmployee.getClassList().remove("selected");
        menuAttendanceMgnt.getClassList().remove("selected");
        submenuAttendance.getClassList().remove("selected");
        submenuAttendance.getStyle().set("display", "none");
        menuReportManagement.getClassList().remove("selected");
        menuAllAttendance.getClassList().remove("selected");
        menuSystemMgnt.getClassList().remove("selected");
        submenuSystem.getClassList().remove("selected");
        submenuSystem.getStyle().set("display", "none");
        menuUSSD.getClassList().remove("selected");
        menuUsers.getClassList().remove("selected");
    }

    public ManagementLayout() {
        menuGeoMgntTitle.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/geography/edit")));
        menuCreateNewGeography.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/geography/new")));
        menuAllGeo.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/geography/edit")));

        menuEmployeeMgntTitle.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/employee/edit")));
        menuCreateNewEmployee.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/employee/new")));
        menuAllEmployee.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/employee/edit")));

        menuAttendanceMgntTitle.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/attendance/all")));
        menuReportManagement.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/attendance/report")));
        menuAllAttendance.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/attendance/all")));

        menuSystemMgntTitle.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/system/ussd")));
        menuUSSD.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/system/ussd")));
        menuUsers.addEventListener("click", domEvent -> getUI().ifPresent(ui -> ui.navigate("management/system/users")));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        List<String> segments = event.getLocation().getSegments();
        deselectAllMenus();
        if (segments.size() > 1) {
            switch (segments.get(1)) {
                case "geography":
                    menuGeoMgnt.getClassList().add("selected");
                    submenuGeo.getStyle().set("display", "block");
                    selectGeographySubmenu(segments);
                    break;
                case "employee":
                    menuEmployeeMgnt.getClassList().add("selected");
                    submenuEmployee.getStyle().set("display", "block");
                    selectEmployeeSubmenu(segments);
                    break;
                case "attendance":
                    menuAttendanceMgnt.getClassList().add("selected");
                    submenuAttendance.getStyle().set("display", "block");
                    selectAttendanceSubMenu(segments);
                    break;
                case "system":
                    menuSystemMgnt.getClassList().add("selected");
                    submenuSystem.getStyle().set("display", "block");
                    selectSystemSubmenu(segments);
                    break;
                default:
                    break;
            }
        } else {
            getUI().ifPresent(ui -> ui.navigate("management/attendance/all"));
        }
    }

    private void selectAttendanceSubMenu(List<String> segments) {
        menuAllAttendance.getClassList().remove("selected");
        menuReportManagement.getClassList().remove("selected");
        if (segments.size() > 2) {
            switch (segments.get(2)) {
                case "report":
                    menuReportManagement.getClassList().add("selected");
                    break;
                case "all":
                    menuAllAttendance.getClassList().add("selected");
                    break;
                default:
                    break;
            }
        } else {
            menuAllAttendance.getClassList().add("selected");
        }
    }

    private void selectSystemSubmenu(List<String> segments) {
        menuUSSD.getClassList().remove("selected");
        menuUsers.getClassList().remove("selected");
        if (segments.size() > 2) {
            switch (segments.get(2)) {
                case "ussd":
                    menuUSSD.getClassList().add("selected");
                    break;
                case "users":
                    menuUsers.getClassList().add("selected");
                    break;
                default:
                    break;
            }
        } else {
            menuUSSD.getClassList().add("selected");
        }
    }

    private void selectEmployeeSubmenu(List<String> segments) {
        menuAllEmployee.getClassList().remove("selected");
        menuCreateNewEmployee.getClassList().remove("selected");
        if (segments.size() > 2) {
            switch (segments.get(2)) {
                case "new":
                    menuCreateNewEmployee.getClassList().add("selected");
                    break;
                case "edit":
                    menuAllEmployee.getClassList().add("selected");
                    break;
                default:
                    break;
            }
        } else {
            menuAllEmployee.getClassList().add("selected");
        }
    }

    private void selectGeographySubmenu(List<String> segments) {
        menuCreateNewGeography.getClassList().remove("selected");
        menuAllGeo.getClassList().remove("selected");
        if (segments.size() > 2) {
            switch (segments.get(2)) {
                case "new":
                    menuCreateNewGeography.getClassList().add("selected");
                    break;
                case "edit":
                    menuAllGeo.getClassList().add("selected");
                    break;
                default:
                    break;
            }
        } else {
            menuAllGeo.getClassList().add("selected");
        }
    }
}

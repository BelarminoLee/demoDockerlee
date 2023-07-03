package mz.co.attendance.control.views.employee;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.common.NotificationAlert;
import mz.co.attendance.control.components.employee.EmployeeForm;
import mz.co.attendance.control.dao.entities.employee.Employee;
import mz.co.attendance.control.dao.repositories.EmployeeRepository;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;

@PageTitle("Employee Management")
@Route(value = "management/employee/new", layout = ManagementLayout.class)
public class EmployeeNewView extends VerticalLayout {

    private EmployeeForm form;

    private RestClientService restClientService;

    private final EmployeeRepository repository;

    private Employee employee;

    public EmployeeNewView(RestClientService restClientServicem, EmployeeRepository  repository) {
        this.restClientService = restClientService;
        this.repository = repository;
        setSizeFull();
        H3 title = new H3("Create New Employee");
        form = new EmployeeForm(restClientService);
        form.setWidth("25em");
        form.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
        form.addListener(EmployeeForm.CloseEvent.class, this::closeEditor);
        form.initNew();
        add(title, form);
    }

    private void saveEmployee(EmployeeForm.SaveEvent event) {
        if(employee != null) {
            repository.save(employee);
        } else {

        }


    }

    private void closeEditor(EmployeeForm.CloseEvent event) {
    }


}

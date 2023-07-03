package mz.co.attendance.control.views.geography;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.geography.HealthCenterForm;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;

@PageTitle("Geography Management")
@Route(value = "management/geography/new", layout = ManagementLayout.class)
public class HealthCenterNewView extends VerticalLayout {

    private final HealthCenterForm form;

    private final RestClientService restClientService;

    public HealthCenterNewView(RestClientService restClientService) {
        this.restClientService = restClientService;
        setSizeFull();
        H3 title = new H3("Create New Health Center");
        form = new HealthCenterForm(restClientService);
        form.setWidth("25em");
        form.addListener(HealthCenterForm.SaveEvent.class, this::saveHealthCenter);
        form.addListener(HealthCenterForm.CloseEvent.class, this::closeEditor);
        form.initNew();
        add(title, form);
    }

    private void saveHealthCenter(HealthCenterForm.SaveEvent event) {
    }

    private void closeEditor(HealthCenterForm.CloseEvent event) {

    }
}

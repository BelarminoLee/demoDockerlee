package mz.co.attendance.control.views.management;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.views.MainLayout;

@PageTitle("Employee Management")
@Route(value = "management", layout = ManagementLayout.class)
public class ManagementView extends Div {
    public ManagementView() {

    }
}
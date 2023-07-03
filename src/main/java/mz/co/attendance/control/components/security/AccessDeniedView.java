package mz.co.attendance.control.components.security;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import mz.co.attendance.control.views.MainLayout;

@Tag("access-denied-view")
@JsModule("./themes/attendance-control/views/access-denied-view.js")
@Route(value = "access-denied", layout = MainLayout.class)
public class AccessDeniedView extends PolymerTemplate<TemplateModel> {

}

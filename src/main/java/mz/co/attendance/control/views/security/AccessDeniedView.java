package mz.co.attendance.control.views.security;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import mz.co.attendance.control.views.MainLayout;

import javax.servlet.http.HttpServletResponse;

@Tag("access-denied-view.js")
@JsModule("./themes/attendance-control/views/access-denied-view.js")
@ParentLayout(MainLayout.class)
@PageTitle("Acess Denied")
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
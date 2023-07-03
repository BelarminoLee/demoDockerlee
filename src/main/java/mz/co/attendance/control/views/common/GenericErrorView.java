package mz.co.attendance.control.views.common;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.templatemodel.TemplateModel;
import mz.co.attendance.control.views.MainLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

@ParentLayout(MainLayout.class)
@Tag("generic-error-view")
@JsModule("./themes/attendance-control/views/generic-error-view.js")
public class GenericErrorView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<Exception> {

    @Id
    private Div errorLogArea;
    @Id
    private Div errorLogData;

    @Inject
    private ServletContext context;

    private final static Logger logger = LoggerFactory.getLogger(GenericErrorView.class);

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> errorParameter) {

        logger.error("Error while navigating to route " + event.getLocation().getPath(), errorParameter.getException());

        String prodMode = context.getInitParameter("productionMode");

        if (prodMode == null || !prodMode.equals("true")) {
            StringBuilder strBuilder = new StringBuilder();

            if (errorParameter.hasCustomMessage()) {
                strBuilder.append("Custom Message: ");
                strBuilder.append(errorParameter.getCustomMessage());
                strBuilder.append("\n");
            }

            strBuilder.append("<b>Stack Message: ");
            strBuilder.append(errorParameter.getException().getMessage());
            strBuilder.append("</b><br/><br/>");

            strBuilder.append("Stack Trace: ");
            String stackTrace = ExceptionUtils.getStackTrace(errorParameter.getException());
            strBuilder.append(stackTrace);

            errorLogData.getElement().setProperty("innerHTML", strBuilder.toString());
        }

        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
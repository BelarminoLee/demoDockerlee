package mz.co.attendance.control.views.security;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;
import mz.co.attendance.control.views.MainLayout;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(MainLayout.class)
@PageTitle("Página não encontrada")
public class CustomRouteNotFoundError extends RouteNotFoundError {

    public CustomRouteNotFoundError() {
        RouterLink link = Component.from(
                ElementFactory.createRouterLink("", "Voltar para a página principal."),
                RouterLink.class);
        getElement().appendChild(new H1("Oops! Houve um erro ao navegar. ").getElement(), link.getElement());
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
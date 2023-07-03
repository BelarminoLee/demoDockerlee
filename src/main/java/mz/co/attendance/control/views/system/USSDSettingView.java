package mz.co.attendance.control.views.system;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.common.PaginatedGrid;
import mz.co.attendance.control.components.common.ToggleButton;
import mz.co.attendance.control.dao.entities.configuration.Configuration;
import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;
import org.apache.commons.lang3.StringUtils;

@PageTitle("System Management")
@Route(value = "management/system/ussd", layout = ManagementLayout.class)
public class USSDSettingView extends VerticalLayout {
    public static final String USSD_SERVICE_STATUS = "ussd_service_status";
    private final RestClientService restClientService;
    private Grid<UssdSession> grid;
    private final int currentPage = 0;
    private final int maxResults = 10;

    private ToggleButton serviceEnabler;

    private Configuration ussdEnabledConfig;

    public USSDSettingView(RestClientService restClientService) {
        this.restClientService = restClientService;
        this.ussdEnabledConfig = restClientService.getConfigByProperty(USSD_SERVICE_STATUS);
        setSizeFull();
        configureGrid();
        H3 title1 = new H3("Global Configurations");
        serviceEnabler = new ToggleButton("USSD Service Enabled");
        serviceEnabler.checked(StringUtils.equals(ussdEnabledConfig.getValue(), "1") ? true : false);
        serviceEnabler.setTooltipText(StringUtils.equals(ussdEnabledConfig.getValue(), "1") ? "Enabled" : "Disabled");
        H3 title2 = new H3("USSD Sessions");
        add(title1, serviceEnabler, title2, grid);
        updateList();
    }

    private void configureGrid() {
        grid = new PaginatedGrid<>(UssdSession.class);
        grid.addClassNames("ussd-session-grid");
        grid.setMaxWidth("100%");
        grid.getStyle().set("margin-left", "14px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(UssdSession::getSessionId).setHeader("Session Id").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(UssdSession::getMsisdn).setHeader("Mobile Number").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(UssdSession::getPreferredLanguage).setHeader("Language").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createAuthComponentRenderer()).setHeader("Authenticated").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createStatusComponentRenderer()).setHeader("Status").setAutoWidth(true).setFlexGrow(0);
    }

    private static ComponentRenderer<Span, UssdSession> createAuthComponentRenderer() {
        return new ComponentRenderer<>(Span::new, authComponentUpdater);
    }

    private static ComponentRenderer<Span, UssdSession> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private static final SerializableBiConsumer<Span, UssdSession> authComponentUpdater = (span, ussdSession) -> {
        String theme = String.format("badge %s", ussdSession.isAuthenticated() ? "success" : "error");
        span.getElement().setAttribute("theme", theme);
        span.setText(ussdSession.isAuthenticated() ? "Yes" : "No");
    };

    private static final SerializableBiConsumer<Span, UssdSession> statusComponentUpdater = (span, ussdSession) -> {
        String theme = String.format("badge %s", StringUtils.equals(ussdSession.getStatus(), "ACTIVE") ? "success" : "error");
        span.getElement().setAttribute("theme", theme);
        span.setText(ussdSession.getStatus());
    };

    private void updateList() {
        grid.setItems(restClientService.getAllUSSDSessions(currentPage, maxResults).toList());
        grid.getDataProvider().refreshAll();
    }

}

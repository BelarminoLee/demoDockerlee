package mz.co.attendance.control.views.system;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import mz.co.attendance.control.components.common.ConfirmDialog;
import mz.co.attendance.control.components.common.NotificationAlert;
import mz.co.attendance.control.components.common.PaginatedGrid;
import mz.co.attendance.control.components.common.ToggleButton;
import mz.co.attendance.control.dao.entities.security.User;
import mz.co.attendance.control.service.client.RestClientService;
import mz.co.attendance.control.views.management.ManagementLayout;
import org.apache.commons.lang3.StringUtils;

@PageTitle("System Management")
@Route(value = "management/system/users", layout = ManagementLayout.class)
public class UsersView extends VerticalLayout {

    private final RestClientService restClientService;
    private Grid<User> grid;
    private final int currentPage = 0;
    private final int maxResults = 10;

    public UsersView(RestClientService restClientService) {
        this.restClientService = restClientService;
        setSizeFull();
        configureGrid();
        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid = new PaginatedGrid<>(User.class);
        grid.addClassNames("users-grid");
        grid.setMaxWidth("100%");
        grid.getStyle().set("margin-left", "14px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addColumn(createUserInfoRenderer()).setHeader("User Name / Email ").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(user -> user.getRole().getLabel()).setHeader("Role").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createStatusComponentRenderer()).setHeader("Is Locked").setAutoWidth(true).setFlexGrow(0);
    }

    private Renderer<User> createUserInfoRenderer() {
        return LitRenderer.<User>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">" + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">" + "    <span> ${item.username} </span>" + "<span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">" + "${item.email}" + "    </span>" + "  </vaadin-vertical-layout>" + "</vaadin-horizontal-layout>").withProperty("username", User::getUsername).withProperty("email", User::getEmail);
    }


    private void updateList() {
        grid.setItems(restClientService.getAllUsers(currentPage, maxResults).toList());
        grid.getDataProvider().refreshAll();
    }

    private static ComponentRenderer<ToggleButton, User> createStatusComponentRenderer() {
        return new ComponentRenderer<>(ToggleButton::new, statusComponentUpdater);
    }

    private static final SerializableBiConsumer<ToggleButton, User> statusComponentUpdater = (toggleButton, user) -> {
        toggleButton.checked(user.isLocked());
        toggleButton.setTooltipText(user.isLocked() ? "Yes" : "No");
        toggleButton.addClickListener(toggleButtonClickEvent -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setTitle("User Lock");
            confirmDialog.setDescription("Are you sure you want to " + getNextStatus(user) + " this user?");
            confirmDialog.addOnConfirmListener(() -> {
                toggleButtonClickEvent.getSource().checked(user.isLocked());
                new NotificationAlert("User " + getNextStatus(user) + " successfully!", 8000);
            });
            confirmDialog.addOnCancelListener(() -> toggleButtonClickEvent.getSource().checked(user.isLocked()));
            confirmDialog.show();
        });
    };

    private static String getNextStatus(User user) {
        return user.isLocked() ? "unlock" : "lock";
    }
}

package mz.co.attendance.control.views;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import mz.co.attendance.control.security.CurrentUser;
import mz.co.attendance.control.security.SecurityService;
import mz.co.attendance.control.views.dashbaord.DashboardView;
import mz.co.attendance.control.views.management.ManagementView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {


    public static class MenuItemInfo {

        private String text;
        private String iconClass;
        private Class<? extends Component> view;

        public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
            this.text = text;
            this.iconClass = iconClass;
            this.view = view;
        }

        public String getText() {
            return text;
        }

        public String getIconClass() {
            return iconClass;
        }

        public Class<? extends Component> getView() {
            return view;
        }

    }

    private final Tabs menu;
    private H1 viewTitle;
    private final CurrentUser currentUser;

    private final SecurityService securityService;

    public MainLayout(CurrentUser currentUser, SecurityService securityService) {
        this.currentUser = currentUser;
        this.securityService = securityService;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setClassName("sidemenu-header");
        layout.getStyle().set("background-color", "#468d88");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        DrawerToggle toggle = new DrawerToggle();
        toggle.getStyle().set("color", "white");
        layout.add(toggle);
        viewTitle = new H1();
        Button logout = new Button("Logout");
        logout.getElement().getStyle().set("margin-right", "5px");
        logout.getElement().getStyle().set("color", "white");
        logout.getElement().getStyle().set("cursor", "default");
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logout.addClickListener(buttonClickEvent -> securityService.logout());
        layout.add(viewTitle, logout);
        layout.expand(viewTitle);

        Avatar avatar = new Avatar();
        avatar.addClassNames("ms-auto", "me-m");
        avatar.getStyle().set("background-color", "#d68239");
        if (Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getUser())) {
            avatar.setName(currentUser.getUser().getFirstName() + " " + currentUser.getUser().getLastName());
        }
        layout.add(avatar);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("sidemenu-menu");
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("border-style", "inset");
        layout.getStyle().set("border-color", "white");
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logo = new Image("icons/logo.svg", "Elizabeth Glaser");
        logo.setMaxWidth("222px");
        logoLayout.add(logo);
        logoLayout.getStyle().set("background-color", "white");
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        for (Tab menuTab : createMenuItems()) {
            menuTab.getStyle().set("border-radius", "5px");
            tabs.add(menuTab);
        }
        return tabs;
    }

    private List<Tab> createMenuItems() {
        MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
                new MenuItemInfo("Dashboard", "la la-chart-line", DashboardView.class), new MenuItemInfo("Platform Management", "la la-pen-square", ManagementView.class)};
        List<Tab> tabs = new ArrayList<>();
        for (MenuItemInfo menuItemInfo : menuItems) {
            tabs.add(createTab(menuItemInfo));
        }
        return tabs;
    }

    private static Tab createTab(MenuItemInfo menuItemInfo) {
        Tab tab = new Tab();
        RouterLink link = new RouterLink();
        link.setRoute(menuItemInfo.getView());
        Span iconElement = new Span();
        iconElement.addClassNames("text-l", "pr-s");
        if (!menuItemInfo.getIconClass().isEmpty()) {
            iconElement.addClassNames(menuItemInfo.getIconClass());
        }
        link.add(iconElement, new Text(menuItemInfo.getText()));
        tab.add(link);
        ComponentUtil.setData(tab, Class.class, menuItemInfo.getView());
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass())).findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

}

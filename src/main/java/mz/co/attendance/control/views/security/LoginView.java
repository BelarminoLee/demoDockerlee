package mz.co.attendance.control.views.security;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;
import mz.co.attendance.control.security.SecurityUtils;
import mz.co.attendance.control.views.dashbaord.DashboardView;


@Route("login")
@PageTitle("Attendance Control Platform Login")
public class LoginView extends LoginOverlay implements AfterNavigationObserver, BeforeEnterObserver {

    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Attendance Control");
        i18n.setAdditionalInformation(null);
        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setSubmit("Submit");
        i18n.getForm().setTitle("Authentication");
        i18n.getForm().setUsername("Email/User Name");
        i18n.getForm().setPassword("Password");
        i18n.getErrorMessage().setTitle("Invalid Email/User Name or Password");
        i18n.getErrorMessage().setMessage("Make sure you have entered the correct credentials and try again!");
        setI18n(i18n);
        setForgotPasswordButtonVisible(false);
        setAction("login");

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()) {
            getUI().get().navigate(DashboardView.class);
        } else {
            setOpened(true);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
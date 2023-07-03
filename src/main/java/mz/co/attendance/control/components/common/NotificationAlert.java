package mz.co.attendance.control.components.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import mz.co.attendance.control.components.utils.NotificationType;

@Tag("notification-alert")
@JsModule(value = "./themes/attendance-control/components/notification-themes.js")
public class NotificationAlert extends Component {

    private Notification notification = new Notification();
    private Icon icon = new Icon();

    public NotificationAlert(String message, int duration) {
        showInfoNotification(message, duration);
    }

    public NotificationAlert(String message, int duration, NotificationType notificationType) {
        switch (notificationType) {
            case ERROR:
                showErrorNotification(message);
                break;
            case WARNING:
                showWarningNotification(message, duration);
                break;
            case SUCCESS:
                showSuccessNotification(message, duration);
                break;

            default:
                showInfoNotification(message, duration);
                break;
        }
    }

    public NotificationAlert(Div customMessage, int duration, NotificationType notificationType) {
        switch (notificationType) {
            case INFO:
                showCustomInfoNotification(customMessage, duration, "info");
                break;
            case ERROR:
                showCustomInfoNotification(customMessage, duration, "error");
                break;
            case WARNING:
                showCustomInfoNotification(customMessage, duration, "warning");
                break;
            case SUCCESS:
                showCustomInfoNotification(customMessage, duration, "success");
                break;
        }
    }

    private void showCustomInfoNotification(Div message, int duration, String theme) {
        notification = new Notification();
        applyTheme(theme);
        notification.add(message);
        notification.setDuration(duration);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }

    private void showErrorNotification(String message) {

        notification = new Notification();
        icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        icon.setSize("50px");

        H3 iconTitle = new H3(icon);
        iconTitle.getStyle().set("text-align", "center");
        iconTitle.getStyle().set("color", "#fd4007");
        iconTitle.getStyle().set("margin", "margin: 10px 0px 30px 0px");

/*        Button closeBtn = new Button("Close", e -> notification.close());
        closeBtn.getStyle().set("margin-top", "25px");
        closeBtn.getStyle().set("float", "inline-end");
        closeBtn.setThemeName("primary");*/
        notification.add(iconTitle, new Label(message));
        notification.setPosition(Notification.Position.MIDDLE);

        applyTheme("error");
        notification.open();
    }


    private void showWarningNotification(String message, int duration) {
        notification = new Notification();
        icon = new Icon(VaadinIcon.WARNING);
        icon.setSize("50px");

        Button closeBtn = new Button("Close", e -> notification.close());
        closeBtn.getStyle().set("margin-top", "25px");
        closeBtn.getStyle().set("float", "inline-end");
        closeBtn.setThemeName("primary");
        H3 iconTitle = new H3(icon);
        iconTitle.getStyle().set("text-align", "center");
        iconTitle.getStyle().set("color", "#fd7d07");
        iconTitle.getStyle().set("margin", "margin: 10px 0px 30px 0px");

        applyTheme("warning");
        notification.add(iconTitle, new Label(message), closeBtn);
        notification.setDuration(duration > 0 ? duration : 8000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }

    private void showInfoNotification(String message, int duration) {
        notification = new Notification();
        icon = new Icon(VaadinIcon.INFO_CIRCLE);
        icon.setSize("50px");

        H3 iconTitle = new H3(icon);
        iconTitle.getStyle().set("text-align", "center");
        iconTitle.getStyle().set("color", "#fdbb07");
        iconTitle.getStyle().set("margin", "margin: 10px 0px 30px 0px");
        notification.add(iconTitle, new Label(message));

        applyTheme("info");
        notification.setDuration(duration > 0 ? duration : 8000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }

    private void showSuccessNotification(String message, int duration) {
        notification = new Notification();
        icon = new Icon(VaadinIcon.CHECK_CIRCLE);
        icon.setSize("50px");

        H3 iconTitle = new H3(icon);
        iconTitle.getStyle().set("text-align", "center");
        iconTitle.getStyle().set("color", "#5cb85c");
        iconTitle.getStyle().set("margin", "margin: 10px 0px 30px 0px");

        applyTheme("success");
        notification.add(iconTitle, new Label(message));
        notification.setDuration(duration > 0 ? duration : 6000);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.open();
    }

    private void applyTheme(String theme) {
        notification.getElement().getThemeList().add(theme);
        notification.getElement().setProperty("theme", "has-description");
    }
}

package mz.co.attendance.control.components.common;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.templatemodel.TemplateModel;
import mz.co.attendance.control.components.utils.DialogCancelListener;
import mz.co.attendance.control.components.utils.DialogConfirmListener;


import java.util.ArrayList;
import java.util.List;

@Tag("confirm-dialog")
@JsModule("./themes/attendance-control/components/confirm-dialog.js")
public class ConfirmDialog extends PolymerTemplate<TemplateModel> {

    @Id
    private Element elementTitle;
    @Id
    private Element elementDescription;
    @Id
    private Button btnCancel;
    @Id
    private Button btnContinue;

    private List<DialogCancelListener> dialogCancelListenerList = new ArrayList<>();
    private List<DialogConfirmListener> dialogConfirmListeners = new ArrayList<>();

    private Dialog dialog;

    public ConfirmDialog() {
        init();
        processActions();
    }

    public ConfirmDialog(String title, String description) {
        init();
        setTitle(title);
        setDescription(description);
        processActions();
    }

    private void init() {
        this.dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.add(this);
    }

    public void shouldCloseOnly() {
        btnCancel.setVisible(false);
        btnContinue.setText("Close");
    }

    public void setTitle(String title) {
        this.elementTitle.setText(title);
    }

    public void setDescription(String description) {
        this.elementDescription.setText(description);
    }

    public void setOkLabel(String label) {
        this.btnContinue.setText(label);
    }

    public void setCancelLabel(String label) {
        this.btnCancel.setText(label);
    }

    public void show() {
        dialog.open();
    }

    public void addOnCancelListener(DialogCancelListener listener) {
        dialogCancelListenerList.add(listener);
    }

    public void setOnConfirmListener(DialogConfirmListener listener) {
        dialogConfirmListeners.clear();
        dialogConfirmListeners.add(listener);
    }

    public void addOnConfirmListener(DialogConfirmListener listener) {
        dialogConfirmListeners.add(listener);
    }

    private void processActions() {
        btnCancel.addClickListener(click -> {
            dialog.close();
            for (DialogCancelListener cancelListener : dialogCancelListenerList) {
                cancelListener.onCancel();
            }
        });
        btnContinue.addClickListener(click -> {
            dialog.close();
            for (DialogConfirmListener confirmListener : dialogConfirmListeners) {
                confirmListener.onConfirm();
            }
        });
    }

}

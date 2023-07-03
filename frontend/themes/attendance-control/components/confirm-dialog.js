import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';


class ConfirmDialog extends PolymerElement {
    static get template() {
        return html`
        <style include="shared-styles">
            :host{
            }
            .footer-area{
                text-align: right;
            }
        </style>
            <div class="body-area">
                <h3 style="margin-bottom: 0;" id="elementTitle"></h3>
                <p style="margin-top: 0;" id="elementDescription"></p>
            </div>
            <div class="footer-area">
                <vaadin-button id="btnCancel" theme="error tertiary">
                    Cancel
                </vaadin-button>
                <vaadin-button id="btnContinue" theme="" style="margin-left: 10px;">
                    Accept
                </vaadin-button>
            </div>
    `;
    }

    static get is() {
        return 'confirm-dialog'
    }
}
customElements.define(ConfirmDialog.is, ConfirmDialog);
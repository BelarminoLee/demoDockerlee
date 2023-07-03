const documentContainer = document.createElement('template');

documentContainer.innerHTML = `
    <custom-style>
        <dom-module id="notification-theme" theme-for="vaadin-notification-card">
            <template>
                <style>
                    :host([theme~="info"]) [part~="overlay"] {
                        color: var(--lumo-body-text-color);
                    }
    
                    :host([theme~="success"]) [part~="overlay"] {
                        color: var(--lumo-success-text-color);
                    }
    
                    :host([theme~="warning"]) [part~="overlay"] {
                        color: #ff6700;
                    }
    
                    :host([theme~="error"]) [part~="overlay"] {
                        color: var(--lumo-error-text-color);
                    }
                </style>
            </template>
        </dom-module>
    </custom-style>`;

document.head.appendChild(documentContainer.content);
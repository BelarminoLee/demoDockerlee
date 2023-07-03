import {html, PolymerElement} from '@polymer/polymer/polymer-element';
import '@polymer/iron-icon/iron-icon';
import '@vaadin/vaadin-icons/vaadin-icons';
import '@vaadin/vaadin-text-field/vaadin-text-field';

class ManagementView extends PolymerElement {
    static get template() {
        return html`
            <style include="shared-styles">
                :host {
                    width: 100%;
                }

                #nav-area {
                    width: 220px;
                    height: 100%;
                    background-color: rgb(255, 255, 255);
                    overflow: auto;
                    float: left;
                }

                #work-area {
                    width: calc(100% - 220px);
                    height: 100%;
                    overflow: auto;
                    float: left;
                    padding: 0;
                }

                .menu {
                    margin: 0;
                    padding: 0 13px;
                }

                .menu li {
                    list-style: none;
                    padding-left: 8px;
                    border-left: 3px solid transparent;
                    margin: 15px 0;
                    cursor: pointer;
                }

                .menu ul {
                    margin-left: 5px;
                    padding-left: 15px;
                }

                .menu li.selected, .menu ul li.selected {
                    border-left: 3px solid var(--lumo-primary-color);
                    cursor: default;
                    font-weight: bold;
                }

                .menu li:hover:not(.selected) {
                    cursor: pointer;
                    border-left: 3px solid rgba(31, 73, 243, 0.43);
                    color: rgba(44, 44, 44, 0.89);
                }

                .menu ul li {
                    font-weight: initial;
                }

                .menu a {
                    text-decoration: none;
                    color: #2c2c2c;
                    padding-left: 8px;
                }

                .menu iron-icon {
                    width: 12px;
                    color: #2c2c2c;
                }

                .not_enabled {
                    opacity: 0.5;
                    cursor: not-allowed !important;
                }
            </style>
            <div id="nav-area" class="backoffice-menu">
                <ul class="menu">
                    <li id="menuAttendanceMgnt"> <span id="menuAttendanceMgntTitle">
                      <iron-icon icon="vaadin:book"></iron-icon> <a>Attendance</a> </span>
                        <ul id="submenuAttendance">
                            <li id="menuAllAttendance">All Attendances</li>
                            <li id="menuReportManagement">Attendance Report</li>
                        </ul>
                    </li>
                    <li id="menuGeoMgnt"> <span id="menuGeoMgntTitle">
                      <iron-icon icon="vaadin:globe"></iron-icon> <a>Health Center</a> </span>
                        <ul id="submenuGeo">
                            <li id="menuAllGeo">All Health Centers</li>
                            <li id="menuCreateNewGeography">New Health Center</li>
                        </ul>
                    </li>
                    <li id="menuEmployeeMgnt"> <span id="menuEmployeeMgntTitle">
                      <iron-icon icon="vaadin:users"></iron-icon> <a>Employee</a> </span>
                        <ul id="submenuEmployee">
                            <li id="menuAllEmployee">All Employees</li>
                            <li id="menuCreateNewEmployee">New Employee</li>
                        </ul>
                    </li>
                    <li id="menuSystemMgnt"> <span id="menuSystemMgntTitle">
                      <iron-icon icon="vaadin:tools"></iron-icon> <a>System</a> </span>
                        <ul id="submenuSystem">
                            <li id="menuUSSD">USSD Service</li>
                            <li id="menuUsers">All Users</li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div id="work-area">
                <slot></slot>
            </div>
        `;
    }

    static get is() {
        return 'management-view'
    }

    ready() {
        super.ready();
    }
}
customElements.define(ManagementView.is, ManagementView);

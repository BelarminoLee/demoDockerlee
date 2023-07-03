import {html, PolymerElement} from '@polymer/polymer/polymer-element';
import '@vaadin/vaadin-button/vaadin-button';
import '@polymer/iron-icon/iron-icon';
import '@vaadin/vaadin-icons/vaadin-icons';

class GenericErrorView extends PolymerElement {
    static get template() {
        return html`
            <style>

                * {
                    -webkit-box-sizing: border-box;
                    box-sizing: border-box;
                }

                .title-area {
                    display: flex;
                    align-items: center;
                    margin-top: 40px;
                }

                .notfound {
                    max-width: 767px;
                    width: 100%;
                    line-height: 1.4;
                    text-align: center;
                    padding: 15px;
                    margin: 0 auto;
                }

                .notfound .notfound-404 {
                    position: relative;
                    height: 76px;
                    width: 60px;
                    margin-right: 15px;
                    background: url('icons/icon.png') no-repeat;
                    -webkit-background-size: contain;
                    background-size: contain;
                    align-self: center;
                }

                .notfound h2 {
                    font-size: 33px;
                    font-weight: 200;
                    text-transform: uppercase;
                    margin-top: 0px;
                    margin-bottom: 25px;
                    letter-spacing: 3px;
                }

                .notfound p {
                    font-size: 16px;
                    font-weight: 200;
                    margin-top: 0px;
                    margin-bottom: 25px;
                }

                .notfound a {
                    display: inline-block;
                    text-decoration: none;
                    color: #fff;
                    text-transform: uppercase;
                    padding: 12px 19px;
                    background: #458c88;
                    font-size: 16px;
                    -webkit-transition: 0.2s all;
                    transition: 0.2s all;
                    border-radius: 5px;
                }

                .notfound a:hover {
                    color: #458c88;
                    background: #423c34;
                }

                .homepage-icon {
                    height: 24px;
                }

                @media only screen and (max-width: 480px) {
                    .notfound .notfound-404 {
                        position: relative;
                        height: 168px;
                    }

                    .notfound .notfound-404 h1 {
                        font-size: 142px;
                    }

                    .notfound h2 {
                        font-size: 22px;
                    }
                }

                #errorLogArea {
                    background-color: #cecece;
                    border: 1px solid gray;
                    text-align: left;
                    margin-bottom: 30px;
                }

                #errorLogArea > p {
                    background-color: #424242;
                    padding: 5px 0;
                    color: white;
                    text-align: center;
                    margin: 0;
                    font-weight: 600;
                }

                #errorLogData {
                    max-height: 300px;
                    overflow: auto;
                    font-size: 14px;
                    padding: 5px;
                }
            </style>
            <div id="notfound">
                <div class="notfound">
                    <div class="title-area">
                        <div class="notfound-404"></div>
                        <h2>OOPS! An error occurred </h2>
                    </div>
                    <div id="errorLogArea">
                        <p>Technical Information</p>
                        <div id="errorLogData"></div>
                    </div>
                    <a href="/dashboard">
                        <iron-icon class="homepage-icon" icon="vaadin:home-o"></iron-icon>
                        Initial Page </a>
                </div>
            </div> `;
    }

    static get is() {
        return 'generic-error-view'
    }

    ready() {
        super.ready();
    }
}
customElements.define(GenericErrorView.is, GenericErrorView);

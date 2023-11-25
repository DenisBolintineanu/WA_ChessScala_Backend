class BoxBackgroundComponent extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        this.shadowRoot.innerHTML = `
            <div class="box">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
                <div></div>
            </div>

            <style>
            .box div {
                position: absolute;
                width: 60px;
                height: 60px;
                background-color: transparent;
                border: 6px solid rgba(50, 3, 48, 0.358);
              }
              
              .box div:nth-child(1) {
                top: 12%;
                left: 43%;
                animation: background-anim 10s linear infinite;
              }
              
              .box div:nth-child(2) {
                top: 70%;
                left: 50%;
                animation: background-anim 7s linear infinite;
              }
              
              .box div:nth-child(3) {
                top: 83%;
                left: 13%;
                animation: background-anim 12s linear infinite;
              }
              
              .box div:nth-child(4) {
                top: 19%;
                left: 68%;
                animation: background-anim 5s linear infinite;
              }
              
              .box div:nth-child(5) {
                top: 50%;
                left: 93%;
                animation: background-anim 15s linear infinite;
              }
              
              .box div:nth-child(6) {
                top: 37%;
                left: 21%;
                animation: background-anim 19s linear infinite;
              }
              
              .box div:nth-child(7) {
                top: 15%;
                left: 82%;
                animation: background-anim 12s linear infinite;
              }
              
              .box div:nth-child(8) {
                top: 69%;
                left: 12%;
                animation: background-anim 5s linear infinite;
              }
              
              .box div:nth-child(9) {
                top: 23%;
                left: 24%;
                animation: background-anim 15s linear infinite;
              }
              
              .box div:nth-child(10) {
                top: 98%;
                left: 47%;
                animation: background-anim 19s linear infinite;
              }
              
              @keyframes background-anim {
              
                0% {
                  transform: scale(0) translateY(0) rotate(0);
                }
              
                80% {
                  transform: scale(1.3) translateY(-90px) rotate(360deg);
                  border-color: transparent
                }
              
                100% {
                  border-color: transparent;
                }
              }
            </style>
        `;
    }
}

if (!customElements.get('box-component')) {
    customElements.define('box-component', BoxBackgroundComponent);
}

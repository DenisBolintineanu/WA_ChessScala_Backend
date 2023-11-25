export default {
    mounted(el, binding) {
        setTimeout(() => {
            
        })
        let tooltipEl = document.createElement('span');
        tooltipEl.textContent = binding.value.text;
        tooltipEl.style.position = 'absolute';
        tooltipEl.style.padding = '5px';
        tooltipEl.style.borderRadius = '4px';
        tooltipEl.style.visibility = 'hidden';
        tooltipEl.style.opacity = 0;
        tooltipEl.style.transition = 'opacity 0.3s ease';
        tooltipEl.style.width = 'max-content'

        switch (binding.value.position) {
            case 'bottom':
                tooltipEl.style.top = '120%';
                tooltipEl.style.left = '50%';
                tooltipEl.style.transform = 'translateX(-50%)';
                break;
            case 'right':
                tooltipEl.style.left = '120%';
                tooltipEl.style.top = '50%';
                tooltipEl.style.transform = 'translateY(-50%)';
                break;
            case 'left':
                tooltipEl.style.left = '120%';
                tooltipEl.style.top = '50%';
                tooltipEl.style.transform = 'translateY(-50%)';
                break;
            default:
                tooltipEl.style.bottom = '120%';
                tooltipEl.style.left = '50%';
                tooltipEl.style.transform = 'translateX(-50%)';
        }

        // Stylemode
        switch (binding.value.colormode) {
            case 'dark':
                tooltipEl.style.backgroundColor = 'white';
                tooltipEl.style.color = 'black'
                break;
            default:
                tooltipEl.style.backgroundColor = 'rgb(31 41 55)';
                tooltipEl.style.color = 'white'
                break;
        }

        el.appendChild(tooltipEl);
        el.style.position = 'relative';

        el.onmouseenter = () => {
            tooltipEl.style.visibility = 'visible';
            tooltipEl.style.opacity = 1;
        };
        el.onmouseleave = () => {
            tooltipEl.style.opacity = 0;
            setTimeout(() => {
                if (tooltipEl.style.opacity == 0) {
                    tooltipEl.style.visibility = 'hidden';
                }
            }, 300);
        };
    }
};
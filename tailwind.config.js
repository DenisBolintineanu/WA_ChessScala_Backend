/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.vue', './src/**/*.js', './index.html'],
  theme: {
    extend: {
      dropShadow: {
        'icon': '0 4px 6px rgba(0,0,0,.2)', 
        'icon-dark': '0 4px 6px rgba(0,0,0,.8)',
        'icon-dark-bgl': '0 4px 6px rgba(0,0,0,.5)',
        'icon-bulb': '0 4px 6px rgba(194, 187, 9, 0.8)',
        'logo-light-bg': '2px 4drop-shadow(8px 1px 8px black)',
        'logo-dark-bg': '0 4px 6px rgba(0,0,0,.8)',
      },
      backgroundImage: {
        'custom-dark': 'linear-gradient(to right, #24243e, #6f59a0, #0f0c29)',
        'custom-light': 'linear-gradient(to right, #aeaee4, #9f81de, #aeaee4)',
      }
    },
  },
  plugins: [],
}


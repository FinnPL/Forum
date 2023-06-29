/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')

export default {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    colors: {
      bg: '#101010',
      postBG: '#1d1d1f',
      border: '#303030',
      hover: '#5f5f5f',
      ui: '#262629',
      text: '#6b6b6b',
      primary: '#428bc0',
      secondary: '#f6da53',
      white: colors.white,
      black: colors.black
    },
    extend: {}
  },
  plugins: [require('@tailwindcss/forms')]
}

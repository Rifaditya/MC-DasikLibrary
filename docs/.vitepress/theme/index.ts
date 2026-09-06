import DefaultTheme from 'vitepress/theme'
import SisterModsTicker from './components/SisterModsTicker.vue'
import './custom.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }: { app: any }) {
    app.component('SisterModsTicker', SisterModsTicker)
  }
}

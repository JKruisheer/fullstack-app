import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {providePrimeNG} from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import {definePreset} from '@primeuix/themes';

const orangeAura = definePreset(Aura, {
  semantic: {
    primary: {
      50: "#ffe8d1",
      100: "#ffd1a3",
      200: "#ffa65e",
      300: "#ff8c36",
      400: "#ff7418",
      500: "#ff7a00",  // ← your provided orange as base
      600: "#e56e00",
      700: "#bf5c00",
      800: "#994a00",
      900: "#733800"
    }
  }
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    providePrimeNG({
      theme: {
        preset: orangeAura
      }
    })
  ]
};

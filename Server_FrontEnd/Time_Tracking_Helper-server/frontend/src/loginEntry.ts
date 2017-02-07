import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { LoginModule } from './app/login.module';
import 'zone.js';

platformBrowserDynamic().bootstrapModule(LoginModule);


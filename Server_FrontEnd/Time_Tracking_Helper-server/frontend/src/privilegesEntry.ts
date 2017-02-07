import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { PrivilegesModule } from './app/privileges.module';
import 'zone.js';

platformBrowserDynamic().bootstrapModule(PrivilegesModule);

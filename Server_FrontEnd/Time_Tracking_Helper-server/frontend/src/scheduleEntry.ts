import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { ScheduleModule } from './app/schedule.module';
import 'zone.js';

platformBrowserDynamic().bootstrapModule(ScheduleModule);
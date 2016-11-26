import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { TimelineModule } from './app/timeline.module';
import 'zone.js';

platformBrowserDynamic().bootstrapModule(TimelineModule);
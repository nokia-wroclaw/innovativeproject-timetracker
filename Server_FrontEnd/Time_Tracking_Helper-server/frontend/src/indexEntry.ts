import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { IndexModule } from './app/index.module';
import 'zone.js';

platformBrowserDynamic().bootstrapModule(IndexModule);

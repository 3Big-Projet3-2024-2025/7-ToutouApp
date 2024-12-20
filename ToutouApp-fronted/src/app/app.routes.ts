import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { MapComponent } from './component/map/map.component';
import { PersonalProfileComponent } from './component/personal-profile/personal-profile.component';
import { AuthGuard } from '../app/guard/auth.guard';

export const routes: Routes = [
    { path: '', component: HomePageComponent },
    { path: 'map', component: MapComponent, canActivate: [AuthGuard] },
    { path: 'personal-profile', component: PersonalProfileComponent, canActivate: [AuthGuard] },
  ];

import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { MapComponent } from './component/map/map.component';
import { PersonalProfileComponent } from './component/personal-profile/personal-profile.component';

export const routes: Routes = [
    {path:'',component:HomePageComponent},
    { path:'map', component: MapComponent },
    { path:'personal-profile', component: PersonalProfileComponent }
];

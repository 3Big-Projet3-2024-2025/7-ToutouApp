import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { MapComponent } from './component/map/map.component';
import { PersonalProfileComponent } from './component/personal-profile/personal-profile.component';
import { PostFormRequestComponent } from './component/post-form-request/post-form-request.component';
import { HubForRequestsComponent } from './component/hub-for-requests/hub-for-requests.component';
import { EditRequestComponent } from './component/edit-request/edit-request.component';
import { HelperProfileComponent } from './component/helper-profile/helper-profile.component';

export const routes: Routes = [
    {path:'',component:HomePageComponent},
    { path:'map', component: MapComponent },
    { path:'personal-profile', component: PersonalProfileComponent },
    {path:'post-request',component: PostFormRequestComponent},
    {path:'hub-requests',component: HubForRequestsComponent},
    {path:'edit-request/:id',component: EditRequestComponent},
    {path:'helper-profile/:helperId',component: HelperProfileComponent}
];

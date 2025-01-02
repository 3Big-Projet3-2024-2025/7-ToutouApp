import { Routes } from '@angular/router';
import { HomePageComponent } from './component/home-page/home-page.component';
import { MapComponent } from './component/map/map.component';
import { PersonalProfileComponent } from './component/personal-profile/personal-profile.component';
import { PostFormRequestComponent } from './component/post-form-request/post-form-request.component';
import { HubForRequestsComponent } from './component/hub-for-requests/hub-for-requests.component';
import { EditRequestComponent } from './component/edit-request/edit-request.component';
import { HelperProfileComponent } from './component/helper-profile/helper-profile.component';
import { AuthGuard } from '../app/guard/auth.guard';
import { CommentHelperComponent } from './component/comment-helper/comment-helper.component';
import { AcceptedRequestsComponent } from './component/accepted-requests/accepted-requests.component';
import { ChatComponent } from './component/chat/chat.component';

export const routes: Routes = [
    {path:'', component: HomePageComponent},
    { path:'map', component: MapComponent, canActivate: [AuthGuard] },
    { path:'personal-profile', component: PersonalProfileComponent, canActivate: [AuthGuard] },
    {path:'post-request',component: PostFormRequestComponent,canActivate: [AuthGuard]},
    {path:'hub-requests',component: HubForRequestsComponent,canActivate: [AuthGuard]},
    {path:'edit-request/:id',component: EditRequestComponent,canActivate: [AuthGuard]},
    {path:'helper-profile/:helperId',component: HelperProfileComponent, canActivate: [AuthGuard]},
    {path:'comment-helper/:helperId/:requestId',component: CommentHelperComponent, canActivate: [AuthGuard]},
    {path:'my-services',component: AcceptedRequestsComponent,canActivate: [AuthGuard]},
    { path: 'chat/:requestId', component: ChatComponent, canActivate: [AuthGuard] }
];

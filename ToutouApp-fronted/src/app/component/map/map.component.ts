import { Component, AfterViewInit, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import 'leaflet-control-geocoder';
import { NominatimService } from '../../services/nominatim.service';
import { RequestService } from '../../services/request.service';
import { UserIdService } from '../../services/user-id.service';
import { Router } from '@angular/router';
import { ChatService } from '../../services/chat.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  standalone: true,
  styleUrls: ['./map.component.css'],
  imports: [HeaderComponent, FooterComponent, CommonModule, FormsModule]
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private map: L.Map | undefined;
  private addresses: string[] = [];
  private idRequest: any;
  private request: any;
  private error?: string;

  selectedCategory: string = 'all';
  dogCategories: any[] = [];

  constructor(private nominatimService: NominatimService, private requestService: RequestService, private userIdService: UserIdService, private router: Router,
              private chatService: ChatService
  ) {}

  ngAfterViewInit(): void {
    if (!this.map) {
      this.initMap();
      this.loadAddresses();
    }
    this.loadFilters();
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  private initMap(): void {
    const helhaCoords: L.LatLngTuple = [50.4082935863085, 4.481319694900066]; // HELHa contact details
  
    // Initialize the map
    this.map = L.map('map').setView(helhaCoords, 15);
  
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);
  
    // Configure the geocoder with a custom marker
    const geocoder = new (L.Control as any).Geocoder({
      defaultMarkGeocode: false // Disables the automatic addition of a marker by the geocoder
    });
  
    geocoder.on('markgeocode', (e: any) => {
      const customIcon = this.createCustomMarker();
    
      // Get the coordinates of the result
      const latlng = e.geocode.center;
    
      // Add a custom marker to the found location
      L.marker(latlng, { icon: customIcon }).addTo(this.map!)
        .bindPopup(e.geocode.name)
        .openPopup();
    
      // Check if the map is defined before centering the view
      if (this.map) {
        this.map.setView(latlng, this.map.getZoom());
      }
    });
  
    geocoder.addTo(this.map);
  
    // Add custom initial marker
    const customIcon = this.createCustomMarker();
    const marker = L.marker(helhaCoords, { icon: customIcon }).addTo(this.map);
  
    marker.bindPopup('<b>HELHa Montignies-sur-Sambre</b><br>');
  }

    private addAllMarkers(): void {
      this.clearMarkers();
    
      this.requestService.getAllRequests().subscribe(
        (requests: any[]) => {
          const filteredRequests = requests.filter((request) =>
            this.filterRequests(request) && !request.accepted
          );
    
          console.log('Filtered requests (excluding accepted):', filteredRequests);
    
          filteredRequests.forEach((request) => {
            const address = `${request.owner.street}, ${request.owner.city}, ${request.owner.country}`;
            this.nominatimService.getCoordinates(address).subscribe(
              (response) => {
                if (response && response.length > 0) {
                  const lat = parseFloat(response[0].lat);
                  const lon = parseFloat(response[0].lon);
                  const coords: L.LatLngTuple = [lat, lon];
    
                  if (this.map) {
                    const customIcon = this.createCustomMarker();
                    const marker = L.marker(coords, { icon: customIcon }).addTo(this.map);
    
                    marker.bindPopup(`<b>${address}</b>`);
                    marker.on('click', () => {
                      this.updateDetailsPanel(request);
                    });
                  }
                }
              },
              (error) => {
                console.error('Error fetching coordinates:', error);
              }
            );
          });
        },
        (error) => {
          console.error('Error loading requests:', error);
        }
      );
    }
    
  private createCustomMarker(): L.Icon {
    return L.icon({
      iconUrl: 'assets/marker.png',
      iconSize: [40, 60], 
      iconAnchor: [20, 60], 
      popupAnchor: [0, -60] 
    });
  }

  loadAddresses(): void {
    this.requestService.getAllRequests().subscribe(
      (requests) => {
        this.addresses = requests.map((request: any) => {
          return `${request.owner.street}, ${request.owner.city}, ${request.owner.country}`;
        });
        console.log(this.addresses);
        this.addAllMarkers();
      },
      (error) => {
        console.error('Error loading requests:', error);
      }
    );
  }

  updateDetailsPanel(request: any): void {
    this.idRequest = request.requestId;
    const dogName = request.dogName;
    const dogSize = request.dogCategory ? request.dogCategory.category : 'Unknown';
    const requestDate = request.requestDate;
    const startTime = request.startTime;
    const endTime = request.endTime;
    const photoUrl = request.photo ? request.photo : 'assets/default-photo-dog.jpg';
  
    (document.getElementById('dogName') as HTMLInputElement).value = dogName;
    (document.getElementById('dogSize') as HTMLInputElement).value = dogSize;
    (document.getElementById('date') as HTMLInputElement).value = requestDate;
    (document.getElementById('time') as HTMLInputElement).value = `${startTime} to ${endTime}`;
    
    const photoElement = document.getElementById('photo') as HTMLImageElement;
    photoElement.src = photoUrl;
  }

  async acceptRequest(): Promise<void> {
    if (!this.idRequest) {
      console.error('idRequest is undefined!');
      return;
    }

    await this.loadRequestAsync();
    console.log('Request loaded:', this.request);

    if (!this.request) {
      console.error('Request is still undefined after loading!');
      return;
    }

    try {
      const userId = await this.userIdService.getUserId();
      console.log('User ID:', userId);

      if (this.request.owner.id == userId) {
        window.alert('You cannot accept your own request !');
        console.error('You cannot accept your own request.');
        return;
      }

      const updatedRequest = {
        ...this.request,
        helper: { id: userId },
        accepted: true
      };

      const response = await this.requestService.modifyRequest(this.idRequest, updatedRequest).toPromise();
      console.log('Request successfully updated', response);

      await this.chatService.createChat(this.idRequest).toPromise();
      console.log('Chat created successfully');

      this.router.navigate(['/my-services']);
    } catch (error) {
      console.error('Error during request acceptance', error);
    }
  }
  
  async loadRequestAsync(): Promise<void> {
    if (!this.idRequest) {
      console.error('idRequest is undefined!');
      return;
    }
  
    try {
      const requests = await this.requestService.getAllRequests().toPromise();
      this.request = requests.find((req: any) => req.requestId === this.idRequest);
      if (!this.request) {
        console.error('Request not found!');
      }
    } catch (error) {
      console.error('Error loading request', error);
    }
  }

  onFilterChange(): void {
    console.log('selectedCategory:', this.selectedCategory);
    this.addAllMarkers();
  }

  private clearMarkers(): void {
    this.map?.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        this.map?.removeLayer(layer);
      }
    });
  }

  private filterRequests(request: any): boolean {

    if (request.helper != null) {
      return false;
    }
    
    if (this.selectedCategory.toLowerCase() === 'all') {
      return true;
    }
  
    const requestCategory = request.dogCategory?.category.trim().toLowerCase();
    const selectedCategory = this.selectedCategory.trim().toLowerCase();
    
    console.log('Comparing request category:', requestCategory, 'with selected category:', selectedCategory);
  
    return requestCategory === selectedCategory;
  }
  
  private loadFilters(): void {
    this.requestService.getDogCategories().subscribe(
      (categories) => {
        console.log('Loaded categories:', categories);
        this.dogCategories = [{ category: 'All' }, ...categories];
      },
      (error) => {
        console.error('Error loading dog categories:', error);
      }
    );
  }  
}

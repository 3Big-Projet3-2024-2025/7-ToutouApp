import { Component, AfterViewInit, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import 'leaflet-control-geocoder';
import { NominatimService } from '../../services/nominatim.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  standalone: true,
  styleUrls: ['./map.component.css'],
  imports: [HeaderComponent, FooterComponent]
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private map: L.Map | undefined;
  private addresses = [
    "Rue du Nord 123 Charleroi",
    "Grand rue Charleroi",
    "rue d'Accolay Bruxelles"
  ];

  constructor(private nominatimService: NominatimService) {}

  ngAfterViewInit(): void {
    if (!this.map) {
      this.initMap();
      this.addAllMarkers(); // Ajouter tous les marqueurs pour toutes les adresses après l'initialisation
    }
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  private initMap(): void {
    const helhaCoords: L.LatLngTuple = [50.4082935863085, 4.481319694900066]; // Coordonnées HELHa
  
    // Initialiser la carte
    this.map = L.map('map').setView(helhaCoords, 15);
  
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);
  
    // Configurer le géocodeur avec un marqueur personnalisé
    const geocoder = new (L.Control as any).Geocoder({
      defaultMarkGeocode: false // Désactive l'ajout automatique d'un marqueur par le géocodeur
    });
  
    geocoder.on('markgeocode', (e: any) => {
      const customIcon = this.createCustomMarker();
    
      // Obtenez les coordonnées du résultat
      const latlng = e.geocode.center;
    
      // Ajoutez un marqueur personnalisé à l'emplacement trouvé
      L.marker(latlng, { icon: customIcon }).addTo(this.map!)
        .bindPopup(e.geocode.name)
        .openPopup();
    
      // Vérifiez si la carte est définie avant de centrer la vue
      if (this.map) {
        this.map.setView(latlng, this.map.getZoom());
      }
    });
  
    geocoder.addTo(this.map);
  
    // Ajouter le marqueur initial personnalisé
    const customIcon = this.createCustomMarker();
    const marker = L.marker(helhaCoords, { icon: customIcon }).addTo(this.map);
  
    marker.bindPopup('<b>HELHa Montignies-sur-Sambre</b><br>');
  }

  private addAllMarkers(): void {
    this.addresses.forEach(address => {
      this.nominatimService.getCoordinates(address).subscribe(
        (response) => {
          if (response && response.length > 0) {
            const lat = parseFloat(response[0].lat);
            const lon = parseFloat(response[0].lon);
            const coords: L.LatLngTuple = [lat, lon];
  
            if (this.map) {
              const customIcon = this.createCustomMarker();
              L.marker(coords, { icon: customIcon })
                .addTo(this.map)
                .bindPopup(`<b>${address}</b><br>Latitude: ${lat}, Longitude: ${lon}`)
                .openPopup();
            }
          } else {
            console.error('Aucun résultat trouvé pour l\'adresse :', address);
          }
        },
        (error) => {
          console.error('Erreur lors de la récupération des coordonnées pour l\'adresse :', address, error);
        }
      );
    });
  }

  private createCustomMarker(): L.Icon {
    return L.icon({
      iconUrl: 'assets/marker.png',
      iconSize: [40, 60], 
      iconAnchor: [20, 60], 
      popupAnchor: [0, -60] 
    });
  }

  searchAddress(address: string): void {
    if (!address || address.trim() === '') {
      console.error('Adresse vide ou invalide.');
      return;
    }
  
    this.nominatimService.getCoordinates(address).subscribe(
      (response) => {
        if (response && response.length > 0) {
          const lat = parseFloat(response[0].lat);
          const lon = parseFloat(response[0].lon);
          const coords: L.LatLngTuple = [lat, lon];
  
          // Ajoute un marqueur sur la carte
          if (this.map) {
            const customIcon = this.createCustomMarker();
            L.marker(coords, { icon: customIcon })
              .addTo(this.map)
              .bindPopup(`<b>${address}</b><br>Latitude: ${lat}, Longitude: ${lon}`)
              .openPopup();
  
            this.map.setView(coords, 15);
          }
        } else {
          console.error('Aucun résultat trouvé pour cette adresse.');
        }
      },
      (error) => {
        console.error('Erreur lors de la récupération des coordonnées :', error);
      }
    );
  }
}

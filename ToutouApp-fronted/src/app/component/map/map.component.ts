import { Component, AfterViewInit, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  standalone: true,
  styleUrls: ['./map.component.css'],
  imports: [HeaderComponent, FooterComponent]
})
export class MapComponent implements AfterViewInit, OnDestroy {
  private map: L.Map | undefined;

  ngAfterViewInit(): void {
    if (!this.map) {
      this.initMap();
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

    // Ajouter le marqueur personnalisé
    const customIcon = this.createCustomMarker();
    const marker = L.marker(helhaCoords, { icon: customIcon }).addTo(this.map);

    marker.bindPopup('<b>HELHa Montignies-sur-Sambre</b><br>');
  }

  private createCustomMarker(): L.Icon {
    return L.icon({
      iconUrl: 'assets/marker.png',
      iconSize: [40, 60], 
      iconAnchor: [20, 60], 
      popupAnchor: [0, -60] 
    });
  }
}
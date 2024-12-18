declare namespace GeoSearch {
    interface Provider {
      search(options: any): Promise<any>;
    }
  
    class OpenStreetMapProvider implements Provider {
      constructor(options?: any);
      search(options: any): Promise<any>;
    }
  
    interface GeoSearchControlOptions {
      provider: Provider;
      style?: string;
      autoComplete?: boolean;
      autoCompleteDelay?: number;
    }
  
    class GeoSearchControl {
      constructor(options: GeoSearchControlOptions);
    }
  }
  
  interface Window {
    GeoSearch: typeof GeoSearch;
  }
  
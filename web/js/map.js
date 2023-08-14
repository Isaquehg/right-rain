async function fetchDataAndInitializeMap(uId) {
    try {
      const response = await fetch(`http://3.21.168.204:8000/home/${uId}`);
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      const jsonData = await response.json();

      // Initialize the map
      const map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 0, lng: 0 },
        zoom: 2, // Adjust the initial zoom level as needed
      });

      // Add markers for each location
      jsonData.forEach(location => {
        const marker = new google.maps.Marker({
          position: { lat: location.latitude, lng: location.longitude },
          map: map,
          title: location.d_name,
        });
      });
    } catch (error) {
      console.error('Fetching and displaying data failed:', error);
    }
  }

// Retrieve uId from cookies
const cookies = document.cookie.split('; ');
const uIdCookie = cookies.find(cookie => cookie.startsWith('uId='));
const uId = uIdCookie ? uIdCookie.split('=')[1] : null;

if (uId) {
// Fetch data and initialize map when the page loads
    fetchDataAndInitializeMap(uId);
} else {
    console.error('uId not found in cookies');
}
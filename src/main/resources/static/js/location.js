// **************************************************
// For dynamic fetching of location open opening of website.

document.addEventListener("DOMContentLoaded", () => {
  const searchInput = document.getElementById("city");

  // Check local storage for geolocation processing flag
  const geolocationProcessed = localStorage.getItem("geolocationProcessed");

  if (!geolocationProcessed && navigator.geolocation && !searchInput.value) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;

        // Save flag to local storage to prevent infinite reloads
        localStorage.setItem("geolocationProcessed", "true");

        // Redirect with lat and lon
        window.location.href = `/weather?lat=${lat}&lon=${lon}`;
      },
      (error) => {
        console.error("Error fetching location:", error.message);
      }
    );
  } else {
    console.log("Geolocation already processed or city input detected.");
  }
});

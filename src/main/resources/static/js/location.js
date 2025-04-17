// **************************************************
// Geolocation-based redirection logic

document.addEventListener("DOMContentLoaded", () => {
  console.log("JavaScript is loaded and running.");

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;

        console.log(`Geolocation fetched: lat=${lat}, lon=${lon}`);

        // Redirect to the weather endpoint with coordinates
        window.location.href = `/weather?lat=${lat}&lon=${lon}`;
      },
      (error) => {
        console.error("Error fetching location:", error.message);

        console.log("Redirect to UAE");
        window.location.href = `/weather?lat=23.4241&lon=53.8478`;
      }
    );
  } else {
    console.error("Geolocation is not supported by this browser.");

    console.log("Redirect to Alberta, Canada");
    window.location.href = `/weather?lat=53.9333&lon=116.5765`;
  }
});

// **************************************************
// For dynamic fetching of location open opening of website.

document.addEventListener("DOMContentLoaded", () => {
  const searchInput = document.getElementById("city");
  const searchForm = document.querySelector(".weather-search-container");

  searchForm.addEventListener("submit", (event) => {
    // Prevent geolocation redirect if city input is used
    if (searchInput.value) {
      console.log("City input detected. Proceeding with form submission.");
      return;
    }

    // Geolocation redirect logic
    event.preventDefault();
    if (
      !localStorage.getItem("geolocationProcessed") &&
      navigator.geolocation
    ) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;

          localStorage.setItem("geolocationProcessed", "true");
          window.location.href = `/weather?lat=${lat}&lon=${lon}`;
        },
        (error) => {
          console.error("Error fetching location:", error.message);
        }
      );
    }
  });
});

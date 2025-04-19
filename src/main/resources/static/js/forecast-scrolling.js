// **************************************************
// Mousewheel scrolling for scrollbar

const container = document.querySelector(".forecast-cards");

container.addEventListener("wheel", (event) => {
  console.log("Mousewheel detected");
  // event.preventDefault();
  // container.scrollLeft += event.deltaY;
});

document.addEventListener("DOMContentLoaded", () => {
  setTimeout(() => {
    document.body.classList.add("fade-out");
    setTimeout(() => {
      window.location.href = `/weather`;
    }, 500);
  }, 5_000);
});

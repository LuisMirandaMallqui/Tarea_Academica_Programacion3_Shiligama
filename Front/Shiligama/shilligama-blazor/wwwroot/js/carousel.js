// Shiligama – Carousel Auto-advance Helper
window.carouselHelper = (function () {
    let _interval = null;

    function startCarousel(dotnetRef, intervalMs) {
        stopCarousel();
        _interval = setInterval(function () {
            dotnetRef.invokeMethodAsync('NextSlideAuto').catch(function () {
                stopCarousel();
            });
        }, intervalMs || 5000);
    }

    function stopCarousel() {
        if (_interval !== null) {
            clearInterval(_interval);
            _interval = null;
        }
    }

    return { startCarousel, stopCarousel };
})();

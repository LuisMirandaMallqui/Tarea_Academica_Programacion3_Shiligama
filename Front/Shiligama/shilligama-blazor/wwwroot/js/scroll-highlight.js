window.scrollToElement = function (elementId) {
    const el = document.getElementById(elementId);
    if (el) {
        el.scrollIntoView({ behavior: 'smooth', block: 'center' });
        el.classList.add('row-highlight');
        setTimeout(() => el.classList.remove('row-highlight'), 3000);
    }
};

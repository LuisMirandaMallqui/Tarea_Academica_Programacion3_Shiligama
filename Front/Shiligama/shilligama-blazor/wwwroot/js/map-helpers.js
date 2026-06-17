// map-helpers.js — Leaflet + Nominatim para selección de dirección en el mapa

let _map = null;
let _marker = null;
let _dotnetRef = null;

window.mapHelpers = {

    initMap: function (dotnetRef) {
        _dotnetRef = dotnetRef;

        // Destruir instancia previa si existe
        if (_map) {
            _map.remove();
            _map = null;
            _marker = null;
        }

        // Pequeño delay para que el div del modal sea visible y esté dimensionado
        setTimeout(() => {
            _map = L.map('address-map').setView([-12.0464, -77.0428], 13); // Lima, Perú

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
                maxZoom: 19
            }).addTo(_map);

            _map.on('click', async function (e) {
                const { lat, lng } = e.latlng;

                // Colocar o mover marcador
                if (_marker) {
                    _marker.setLatLng([lat, lng]);
                } else {
                    _marker = L.marker([lat, lng]).addTo(_map);
                }

                // Geocodificación inversa con Nominatim
                try {
                    const res = await fetch(
                        `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json`,
                        { headers: { 'Accept-Language': 'es' } }
                    );
                    const data = await res.json();
                    const address = data.display_name || `${lat.toFixed(5)}, ${lng.toFixed(5)}`;
                    _marker.bindPopup(`<b>${address}</b>`).openPopup();
                    // Sincronizar dirección al input de búsqueda
                    const input = document.getElementById('map-search-input') || document.getElementById('map-search-input-checkout');
                    if (input) input.value = address;
                    await _dotnetRef.invokeMethodAsync('OnMapAddressSelected', address);
                } catch {
                    const fallback = `${lat.toFixed(5)}, ${lng.toFixed(5)}`;
                    await _dotnetRef.invokeMethodAsync('OnMapAddressSelected', fallback);
                }
            });

            // Autocompletado: buscar dirección por texto
            const input = document.getElementById('map-search-input') || document.getElementById('map-search-input-checkout');
            if (input) {
                let _suggestTimeout = null;
                let _suggestionsList = null;

                input.addEventListener('input', function () {
                    clearTimeout(_suggestTimeout);
                    const q = input.value.trim();
                    if (q.length < 3) {
                        if (_suggestionsList) { _suggestionsList.remove(); _suggestionsList = null; }
                        return;
                    }
                    _suggestTimeout = setTimeout(async () => {
                        const suggestions = await window.mapHelpers.getSuggestions(q);
                        if (_suggestionsList) { _suggestionsList.remove(); _suggestionsList = null; }
                        if (!suggestions.length) return;

                        _suggestionsList = document.createElement('ul');
                        _suggestionsList.style.cssText = 'position:absolute;z-index:9999;background:#fff;border:1px solid #ccc;border-radius:6px;list-style:none;margin:0;padding:0;max-height:200px;overflow-y:auto;left:0;right:0;box-shadow:0 4px 12px rgba(0,0,0,.15);';

                        suggestions.forEach(s => {
                            const li = document.createElement('li');
                            li.textContent = s.display_name;
                            li.style.cssText = 'padding:8px 12px;cursor:pointer;font-size:.85rem;border-bottom:1px solid #f0f0f0;';
                            li.addEventListener('mouseenter', () => li.style.background = '#f5f5f5');
                            li.addEventListener('mouseleave', () => li.style.background = '');
                            li.addEventListener('click', async () => {
                                const lat = parseFloat(s.lat), lon = parseFloat(s.lon);
                                _map.setView([lat, lon], 16);
                                if (_marker) { _marker.setLatLng([lat, lon]); }
                                else { _marker = L.marker([lat, lon]).addTo(_map); }
                                const address = s.display_name;
                                _marker.bindPopup(`<b>${address}</b>`).openPopup();
                                input.value = address;
                                _suggestionsList.remove(); _suggestionsList = null;
                                await _dotnetRef.invokeMethodAsync('OnMapAddressSelected', address);
                            });
                            _suggestionsList.appendChild(li);
                        });

                        const wrapper = input.parentElement;
                        if (wrapper && getComputedStyle(wrapper).position === 'static') {
                            wrapper.style.position = 'relative';
                        }
                        wrapper.appendChild(_suggestionsList);
                    }, 400);
                });

                // Cerrar sugerencias al hacer clic fuera
                document.addEventListener('click', function (e) {
                    if (_suggestionsList && !_suggestionsList.contains(e.target) && e.target !== input) {
                        _suggestionsList.remove();
                        _suggestionsList = null;
                    }
                });
            }

            // Forzar recálculo de tamaño tras apertura del modal
            _map.invalidateSize();
        }, 300);
    },

    destroyMap: function () {
        if (_map) {
            _map.remove();
            _map = null;
            _marker = null;
        }
    },

    getSuggestions: async function (query) {
        try {
            const res = await fetch(
                `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&limit=5&countrycodes=pe`,
                { headers: { 'Accept-Language': 'es' } }
            );
            return await res.json();
        } catch { return []; }
    },

    clearSearchInput: function () {
        const input = document.getElementById('map-search-input') || document.getElementById('map-search-input-checkout');
        if (input) input.value = '';
        if (_marker) {
            _marker.remove();
            _marker = null;
        }
    }
};

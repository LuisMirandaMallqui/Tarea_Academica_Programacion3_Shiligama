window.posHelper = {
    focusElement: function (id) {
        var el = document.getElementById(id);
        if (el) { el.focus(); el.select(); }
    },

    readInputValue: function (id) {
        var el = document.getElementById(id);
        return el ? el.value : '';
    },

    keepFocus: function (id) {
        document.addEventListener('click', function (e) {
            if (!e.target.closest('input, button, a, .modal, select, textarea, .modal-content, .toast')) {
                var el = document.getElementById(id);
                if (el) el.focus();
            }
        });
    }
};

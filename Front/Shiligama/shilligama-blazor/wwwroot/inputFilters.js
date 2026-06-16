window.inputFilters = {
    onlyDecimal: function (input) {
        input.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9.,]/g, '');
        });
    },

    onlyInteger: function (input) {
        input.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    }
};
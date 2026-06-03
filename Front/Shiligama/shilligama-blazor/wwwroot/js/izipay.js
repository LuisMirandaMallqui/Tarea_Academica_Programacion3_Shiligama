// =====================================================================
// Helper de integración con el SDK Krypton de Izipay (formulario embebido).
// Se carga como módulo ES desde Blazor vía JS interop.
//
// Flujo: el backend (CreatePayment) entrega un formToken; aquí cargamos el
// SDK con la clave pública, configuramos el formToken y renderizamos el
// formulario embebido. Al terminar el pago, KR.onSubmit notifica a .NET.
//
// NOTA: nombres de API según la documentación V4 de Izipay/Lyra (Krypton).
// Verificar al probar con credenciales TEST reales.
// =====================================================================

let _sdkCargado = false;

function cargarScript(src, attrs) {
    return new Promise((resolve, reject) => {
        const s = document.createElement("script");
        s.src = src;
        if (attrs) {
            for (const [k, v] of Object.entries(attrs)) s.setAttribute(k, v);
        }
        s.onload = () => resolve();
        s.onerror = () => reject(new Error("No se pudo cargar " + src));
        document.head.appendChild(s);
    });
}

function cargarCss(href) {
    return new Promise((resolve) => {
        const l = document.createElement("link");
        l.rel = "stylesheet";
        l.href = href;
        l.onload = () => resolve();
        document.head.appendChild(l);
    });
}

// Renderiza el formulario embebido de Izipay dentro de #izipay-form.
// dotNetRef debe exponer [JSInvokable] OnIzipayResult(string orderStatus).
export async function render(jsBase, publicKey, formToken, dotNetRef) {
    try {
        if (!_sdkCargado) {
            await cargarCss(jsBase + "/static/js/krypton-client/V4.0/ext/classic-reset.css");
            await cargarScript(
                jsBase + "/static/js/krypton-client/V4.0/stable/kr-payment-form.min.js",
                { "kr-public-key": publicKey, "kr-language": "es-ES" });
            await cargarScript(jsBase + "/static/js/krypton-client/V4.0/ext/classic.js");
            _sdkCargado = true;
        }

        // eslint-disable-next-line no-undef
        await KR.setFormConfig({ formToken: formToken, "kr-language": "es-ES" });

        // Notifica a Blazor el resultado y evita el POST/redirección por defecto.
        // eslint-disable-next-line no-undef
        KR.onSubmit(async (resp) => {
            const estado = resp && resp.clientAnswer ? resp.clientAnswer.orderStatus : "UNKNOWN";
            try { await dotNetRef.invokeMethodAsync("OnIzipayResult", estado); } catch (e) { console.error(e); }
            return false;
        });

        // eslint-disable-next-line no-undef
        await KR.renderElements("#izipay-form");
        return true;
    } catch (e) {
        console.error("[izipay.js] Error al renderizar el formulario:", e);
        return false;
    }
}

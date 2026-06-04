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
// Si formToken === "DEMO", muestra formulario simulado (modo demo académico).
// dotNetRef debe exponer [JSInvokable] OnIzipayResult(string orderStatus).
export async function render(jsBase, publicKey, formToken, dotNetRef) {

    // ── Modo DEMO ──────────────────────────────────────────────────────────────
    if (formToken === "DEMO") {
        _renderizarFormularioDemo(dotNetRef);
        return true;
    }

    // ── Integración real Krypton SDK ───────────────────────────────────────────
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

// ── Formulario demo ────────────────────────────────────────────────────────
function _renderizarFormularioDemo(dotNetRef) {
    const contenedor = document.getElementById("izipay-form");
    if (!contenedor) return;

    contenedor.innerHTML = `
        <div style="font-family:'Inter',sans-serif;max-width:420px;">
            <div style="background:#fff3cd;border:1px solid #ffc107;border-radius:8px;
                        padding:10px 14px;margin-bottom:18px;font-size:.82rem;color:#856404;
                        display:flex;align-items:center;gap:8px;">
                <span style="font-size:1rem;">⚠️</span>
                <span><strong>Modo demo</strong> — Cuenta Izipay en validación. Datos no procesados.</span>
            </div>

            <div style="margin-bottom:14px;">
                <label style="display:block;font-size:.82rem;font-weight:600;color:#374151;margin-bottom:4px;">
                    Número de tarjeta</label>
                <input id="demo-card" type="text" placeholder="4111 1111 1111 1111" maxlength="19"
                       style="width:100%;padding:10px 12px;border:1.5px solid #d1d5db;border-radius:8px;
                              font-size:.9rem;box-sizing:border-box;letter-spacing:.06em;"
                       oninput="this.value=this.value.replace(/\D/g,'').replace(/(.{4})/g,'$1 ').trim().slice(0,19)" />
            </div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:14px;">
                <div>
                    <label style="display:block;font-size:.82rem;font-weight:600;color:#374151;margin-bottom:4px;">
                        Vencimiento</label>
                    <input id="demo-exp" type="text" placeholder="MM/AA" maxlength="5"
                           style="width:100%;padding:10px 12px;border:1.5px solid #d1d5db;border-radius:8px;
                                  font-size:.9rem;box-sizing:border-box;"
                           oninput="let v=this.value.replace(/\D/g,'');if(v.length>2)v=v.slice(0,2)+'/'+v.slice(2,4);this.value=v;" />
                </div>
                <div>
                    <label style="display:block;font-size:.82rem;font-weight:600;color:#374151;margin-bottom:4px;">
                        CVV</label>
                    <input id="demo-cvv" type="text" placeholder="123" maxlength="4"
                           style="width:100%;padding:10px 12px;border:1.5px solid #d1d5db;border-radius:8px;
                                  font-size:.9rem;box-sizing:border-box;"
                           oninput="this.value=this.value.replace(/\D/g,'').slice(0,4)" />
                </div>
            </div>
            <div style="margin-bottom:18px;">
                <label style="display:block;font-size:.82rem;font-weight:600;color:#374151;margin-bottom:4px;">
                    Nombre en la tarjeta</label>
                <input id="demo-name" type="text" placeholder="NOMBRE APELLIDO"
                       style="width:100%;padding:10px 12px;border:1.5px solid #d1d5db;border-radius:8px;
                              font-size:.9rem;box-sizing:border-box;text-transform:uppercase;"
                       oninput="this.value=this.value.toUpperCase()" />
            </div>

            <div style="display:flex;gap:6px;align-items:center;margin-bottom:16px;font-size:.75rem;color:#6b7280;">
                <span style="background:#e5e7eb;border-radius:4px;padding:2px 7px;font-weight:700;">VISA</span>
                <span style="background:#e5e7eb;border-radius:4px;padding:2px 7px;font-weight:700;">MC</span>
                <span style="background:#e5e7eb;border-radius:4px;padding:2px 7px;font-weight:700;">AMEX</span>
                <span style="margin-left:auto;">🔒 Simulación segura (demo)</span>
            </div>

            <button id="demo-pay-btn"
                    style="width:100%;padding:13px;background:#ee3a3a;color:white;border:none;
                           border-radius:8px;font-size:.95rem;font-weight:700;cursor:pointer;
                           display:flex;align-items:center;justify-content:center;gap:8px;transition:background .2s;"
                    onmouseover="this.style.background='#cc2a2a'"
                    onmouseout="this.style.background='#ee3a3a'">
                <span style="font-weight:900;letter-spacing:.03em;">izipay</span>&nbsp;— Pagar (demo)
            </button>
            <p style="font-size:.72rem;color:#9ca3af;text-align:center;margin-top:10px;">
                Prueba: <code style="background:#f3f4f6;padding:1px 5px;border-radius:3px;">4111 1111 1111 1111</code>
                &nbsp;·&nbsp;12/26 &nbsp;·&nbsp; 123
            </p>
        </div>`;

    document.getElementById("demo-pay-btn").addEventListener("click", async () => {
        const btn = document.getElementById("demo-pay-btn");
        btn.disabled = true;
        btn.innerHTML = `<span style="border:3px solid rgba(255,255,255,.4);border-top-color:white;
                                     border-radius:50%;width:18px;height:18px;animation:spin .8s linear infinite;
                                     display:inline-block;"></span>&nbsp; Procesando...`;

        // Añadir animación de spinner
        if (!document.getElementById("demo-spin-style")) {
            const st = document.createElement("style");
            st.id = "demo-spin-style";
            st.textContent = "@keyframes spin{to{transform:rotate(360deg)}}";
            document.head.appendChild(st);
        }

        await new Promise(r => setTimeout(r, 1800));

        try { await dotNetRef.invokeMethodAsync("OnIzipayResult", "PAID"); }
        catch (e) { console.error("[izipay.js] Error callback demo:", e); }
    });
}

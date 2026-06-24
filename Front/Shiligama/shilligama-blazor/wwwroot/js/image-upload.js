/**
 * shiligamaStartUpload(dotNetRef, inputId, uploadUrl)
 *
 * Patrón fire-and-forget + callback:
 *   1. Esta función NO es async — retorna undefined de inmediato.
 *   2. El upload ocurre en _shiligamaDoUpload (async, en background).
 *   3. Cuando termina, llama a dotNetRef.invokeMethodAsync('OnUploadComplete', url, error)
 *      para notificar al componente Blazor.
 */
window.shiligamaStartUpload = function (dotNetRef, inputId, uploadUrl) {
    console.log('[IMG-UPLOAD] shiligamaStartUpload llamado', { inputId: inputId, uploadUrl: uploadUrl });
    _shiligamaDoUpload(dotNetRef, inputId, uploadUrl).catch(function (ex) {
        console.error('[IMG-UPLOAD] Error no capturado en _shiligamaDoUpload:', ex);
        try { dotNetRef.invokeMethodAsync('OnUploadComplete', null, ex.message || 'Error al subir imagen'); } catch (_) { }
    });
    // Retorna undefined — JS.InvokeVoidAsync completa inmediatamente.
};

async function _shiligamaDoUpload(dotNetRef, inputId, uploadUrl) {
    // ── Verificar que hay un archivo seleccionado ─────────────────────────────
    var input = document.getElementById(inputId);
    console.log('[IMG-UPLOAD] getElementById("' + inputId + '"):', input);
    console.log('[IMG-UPLOAD] files:', input ? input.files : '(input es null)');

    if (!input || !input.files || input.files.length === 0) {
        console.warn('[IMG-UPLOAD] Sin archivo — abortando.');
        await dotNetRef.invokeMethodAsync('OnUploadComplete', null, 'No se seleccionó ningún archivo.');
        return;
    }

    var file = input.files[0];
    console.log('[IMG-UPLOAD] Archivo:', file.name, '| Tamaño:', file.size, 'bytes');

    // ── Validar tamaño (5 MB) ─────────────────────────────────────────────────
    if (file.size > 5 * 1024 * 1024) {
        await dotNetRef.invokeMethodAsync('OnUploadComplete', null, 'La imagen no debe superar 5 MB.');
        return;
    }

    // ── Leer como Base64 (local, sin red) ────────────────────────────────────
    console.log('[IMG-UPLOAD] Leyendo base64…');
    var base64 = await _shiligamaReadBase64(file);
    console.log('[IMG-UPLOAD] Base64 listo, longitud:', base64.length);

    // ── Subir al proxy Blazor (fetch, no pasa por SignalR) ───────────────────
    var tipo = file.type || 'image/jpeg';
    var body = JSON.stringify({ nombreArchivo: file.name, datosBase64: base64, tipoContenido: tipo });
    console.log('[IMG-UPLOAD] Enviando fetch a:', uploadUrl);

    var resp = await fetch(uploadUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: body
    });

    console.log('[IMG-UPLOAD] Respuesta HTTP:', resp.status, resp.ok);
    var data;
    try { data = await resp.json(); } catch (_) { data = {}; }
    console.log('[IMG-UPLOAD] Datos respuesta:', data);

    if (!resp.ok) {
        await dotNetRef.invokeMethodAsync('OnUploadComplete', null, data.error || ('Error del servidor: ' + resp.status));
    } else if (!data.url) {
        await dotNetRef.invokeMethodAsync('OnUploadComplete', null, data.error || 'Respuesta inesperada del servidor');
    } else {
        console.log('[IMG-UPLOAD] URL recibida:', data.url);
        await dotNetRef.invokeMethodAsync('OnUploadComplete', data.url, null);
    }
}

function _shiligamaReadBase64(file) {
    return new Promise(function (resolve, reject) {
        var reader = new FileReader();
        reader.onload = function (e) {
            var dataUrl = e.target.result;           // "data:image/png;base64,AAAA…"
            var comma = dataUrl.indexOf(',');
            resolve(comma >= 0 ? dataUrl.substring(comma + 1) : dataUrl);
        };
        reader.onerror = function () { reject(new Error('Error al leer el archivo')); };
        reader.readAsDataURL(file);
    });
}

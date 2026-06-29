// boleta-helpers.js – Generates a print-ready Boleta Electrónica document
window.boletaHelpers = {

    /**
     * Opens a new window with a formatted boleta and triggers print dialog.
     * @param {object} data - { receiptId, customerName, payMethod, invoiceType, items: [{name, quantity, price, subtotal}], subtotalSinIgv, igv, total, date }
     */
    generateAndPrintBoleta: function (data) {
        const printWindow = window.open('', '_blank', 'width=400,height=700');
        if (!printWindow) {
            alert('Por favor permite las ventanas emergentes para generar la boleta.');
            return;
        }

        const itemsHtml = data.items.map(item => `
            <tr>
                <td style="padding: 4px 0; font-size: 11px; border-bottom: 1px dashed #e0e0e0;">${item.name}</td>
                <td style="padding: 4px 6px; font-size: 11px; text-align: center; border-bottom: 1px dashed #e0e0e0;">${item.quantity}</td>
                <td style="padding: 4px 0; font-size: 11px; text-align: right; border-bottom: 1px dashed #e0e0e0;">S/ ${item.price.toFixed(2)}</td>
                <td style="padding: 4px 0; font-size: 11px; text-align: right; border-bottom: 1px dashed #e0e0e0;">S/ ${item.subtotal.toFixed(2)}</td>
            </tr>
        `).join('');

        const payMethodLabel = {
            'efectivo': 'Efectivo',
            'yape': 'Yape',
            'plin': 'Plin',
            'tarjeta': 'Tarjeta'
        }[data.payMethod] || data.payMethod;

        const html = `
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Boleta Electrónica - ${data.receiptId}</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Inter', 'Segoe UI', sans-serif;
            background: #fff;
            color: #1a1a1a;
            padding: 0;
        }

        .boleta-container {
            width: 80mm;
            max-width: 100%;
            margin: 0 auto;
            padding: 16px 12px;
        }

        .header {
            text-align: center;
            border-bottom: 2px solid #064420;
            padding-bottom: 12px;
            margin-bottom: 12px;
        }

        .logo-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            background: linear-gradient(135deg, #064420, #0a6b35);
            color: #fff;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
            font-weight: 700;
            margin-bottom: 8px;
        }

        .company-name {
            font-size: 16px;
            font-weight: 700;
            color: #064420;
            letter-spacing: 1px;
        }

        .company-ruc {
            font-size: 10px;
            color: #666;
            margin-top: 2px;
        }

        .doc-title {
            text-align: center;
            background: #e8f5e9;
            border: 1px solid #c8e6d0;
            border-radius: 6px;
            padding: 8px;
            margin-bottom: 12px;
        }

        .doc-title h2 {
            font-size: 13px;
            font-weight: 700;
            color: #064420;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .doc-title .receipt-id {
            font-size: 14px;
            font-weight: 700;
            color: #1a1a1a;
            margin-top: 2px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            font-size: 11px;
            padding: 3px 0;
        }

        .info-label { color: #888; }
        .info-value { font-weight: 600; color: #1a1a1a; }

        .divider {
            border: none;
            border-top: 1px dashed #ccc;
            margin: 10px 0;
        }

        .items-table {
            width: 100%;
            border-collapse: collapse;
        }

        .items-table th {
            font-size: 10px;
            font-weight: 600;
            color: #888;
            text-transform: uppercase;
            letter-spacing: 0.3px;
            padding: 6px 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .totals {
            margin-top: 8px;
        }

        .total-row {
            display: flex;
            justify-content: space-between;
            font-size: 11px;
            padding: 3px 0;
        }

        .total-row.grand-total {
            font-size: 15px;
            font-weight: 700;
            color: #064420;
            border-top: 2px solid #064420;
            padding-top: 8px;
            margin-top: 6px;
        }

        .footer {
            text-align: center;
            margin-top: 16px;
            padding-top: 12px;
            border-top: 1px dashed #ccc;
        }

        .footer p {
            font-size: 10px;
            color: #999;
            line-height: 1.5;
        }

        .footer .thanks {
            font-size: 12px;
            font-weight: 600;
            color: #064420;
            margin-bottom: 4px;
        }

        .sunat-badge {
            display: inline-block;
            background: #fff8e1;
            border: 1px solid #ffe082;
            border-radius: 4px;
            padding: 4px 10px;
            font-size: 9px;
            color: #f57f17;
            font-weight: 500;
            margin-top: 8px;
        }

        @media print {
            body { padding: 0; }
            .boleta-container { padding: 8px; }
            .no-print { display: none !important; }
        }
    </style>
</head>
<body>
    <div class="boleta-container">
        <!-- Header -->
        <div class="header">
            <div class="logo-icon">S</div>
            <div class="company-name">SHILIGAMA</div>
            <div class="company-ruc">RUC: 20612345678</div>
            <div style="font-size: 10px; color: #888; margin-top: 2px;">Av. Principal 123, Lima</div>
        </div>

        <!-- Document Title -->
        <div class="doc-title">
            <h2>Boleta de Venta Electrónica</h2>
            <div class="receipt-id">${data.receiptId}</div>
        </div>

        <!-- Sale Info -->
        <div class="info-row">
            <span class="info-label">Fecha:</span>
            <span class="info-value">${data.date}</span>
        </div>
        <div class="info-row">
            <span class="info-label">Cliente:</span>
            <span class="info-value">${data.customerName || 'Público General'}</span>
        </div>
        <div class="info-row">
            <span class="info-label">Método de Pago:</span>
            <span class="info-value">${payMethodLabel}</span>
        </div>

        <hr class="divider">

        <!-- Items Table -->
        <table class="items-table">
            <thead>
                <tr>
                    <th style="text-align: left;">Descripción</th>
                    <th style="text-align: center;">Cant.</th>
                    <th style="text-align: right;">P. Unit.</th>
                    <th style="text-align: right;">Importe</th>
                </tr>
            </thead>
            <tbody>
                ${itemsHtml}
            </tbody>
        </table>

        <!-- Totals -->
        <div class="totals">
            <div class="total-row">
                <span>Op. Gravada</span>
                <span>S/ ${data.subtotalSinIgv.toFixed(2)}</span>
            </div>
            <div class="total-row">
                <span>IGV (18%)</span>
                <span>S/ ${data.igv.toFixed(2)}</span>
            </div>
            <div class="total-row grand-total">
                <span>TOTAL</span>
                <span>S/ ${data.total.toFixed(2)}</span>
            </div>
        </div>

        <hr class="divider">

        <!-- Footer -->
        <div class="footer">
            <p class="thanks">¡Gracias por su compra!</p>
            <p>Representación impresa de la Boleta de Venta Electrónica</p>
            <p>Autorizado mediante Resolución de Superintendencia</p>
            <div class="sunat-badge">Integración APISUNAT en modo mock</div>
        </div>
    </div>

    <script>
        // Auto-trigger print after the page loads
        window.onload = function() {
            setTimeout(function() {
                window.print();
            }, 500);
        };
    </script>
</body>
</html>`;

        printWindow.document.write(html);
        printWindow.document.close();
    }
};

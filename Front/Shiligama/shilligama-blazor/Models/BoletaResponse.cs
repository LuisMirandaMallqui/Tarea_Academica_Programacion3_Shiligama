using System;

namespace shilligama_blazor.Models
{
    public class BoletaResponse
    {
        public long Id { get; set; }
        public int VentaId { get; set; }
        public string Serie { get; set; } = string.Empty;
        public int Numero { get; set; }
        public DateTime? FechaEmision { get; set; }
        public string ClienteTipoDocumento { get; set; } = string.Empty;
        public string ClienteNumeroDocumento { get; set; } = string.Empty;
        public string ClienteDenominacion { get; set; } = string.Empty;
        public string ClienteDireccion { get; set; } = string.Empty;
        public string ClienteEmail { get; set; } = string.Empty;
        public int Moneda { get; set; }
        public double PorcentajeIgv { get; set; }
        public double TotalGravada { get; set; }
        public double TotalIgv { get; set; }
        public double Total { get; set; }
        public string NubefactEnlace { get; set; } = string.Empty;
        public string NubefactEnlacePdf { get; set; } = string.Empty;
        public string NubefactEnlaceXml { get; set; } = string.Empty;
        public string NubefactEnlaceCdr { get; set; } = string.Empty;
        public string NubefactCadenaQr { get; set; } = string.Empty;
        public string NubefactCodigoHash { get; set; } = string.Empty;
        public bool AceptadaPorSunat { get; set; }
        public string SunatResponseCode { get; set; } = string.Empty;
        public string SunatDescription { get; set; } = string.Empty;
        public bool Anulado { get; set; }
        public string AnulacionMotivo { get; set; } = string.Empty;
        public DateTime? FechaRegistro { get; set; }
        public string UsuarioRegistro { get; set; } = string.Empty;
    }
}

"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { SalesKpiCards } from "@/components/admin/sales/sales-kpi-cards"
import { SalesFilters } from "@/components/admin/sales/sales-filters"
import { SalesTable } from "@/components/admin/sales/sales-table"
import { OrderDetailSheet } from "@/components/admin/sales/order-detail-sheet"

export interface SaleOrder {
  id: string
  fecha: string
  cliente: string
  clienteEmail: string
  clienteTelefono: string
  canal: "presencial" | "web" | "whatsapp"
  productos: {
    nombre: string
    cantidad: number
    precioUnitario: number
    subtotal: number
  }[]
  total: number
  metodoPago: "yape" | "plin" | "tarjeta" | "efectivo"
  comprobante: "boleta" | "factura" | "ninguno"
  comprobanteNumero?: string
  estado: "completado" | "pendiente" | "cancelado" | "reembolsado"
  direccionEntrega?: string
  notas?: string
}

const salesData: SaleOrder[] = [
  {
    id: "VTA-001",
    fecha: "2026-05-09T10:30:00",
    cliente: "María García López",
    clienteEmail: "maria.garcia@email.com",
    clienteTelefono: "987654321",
    canal: "web",
    productos: [
      { nombre: "Arroz Costeño 5kg", cantidad: 2, precioUnitario: 24.90, subtotal: 49.80 },
      { nombre: "Aceite Primor 1L", cantidad: 1, precioUnitario: 12.50, subtotal: 12.50 },
      { nombre: "Leche Gloria 1L", cantidad: 6, precioUnitario: 5.20, subtotal: 31.20 },
      { nombre: "Azúcar Rubia 1kg", cantidad: 2, precioUnitario: 4.80, subtotal: 9.60 },
    ],
    total: 103.10,
    metodoPago: "yape",
    comprobante: "boleta",
    comprobanteNumero: "B001-00234",
    estado: "completado",
    direccionEntrega: "Av. Los Pinos 234, San Isidro",
  },
  {
    id: "VTA-002",
    fecha: "2026-05-09T09:15:00",
    cliente: "Carlos Mendoza Ruiz",
    clienteEmail: "carlos.mendoza@email.com",
    clienteTelefono: "912345678",
    canal: "presencial",
    productos: [
      { nombre: "Cerveza Pilsen Six Pack", cantidad: 2, precioUnitario: 32.00, subtotal: 64.00 },
      { nombre: "Doritos 200g", cantidad: 3, precioUnitario: 8.50, subtotal: 25.50 },
    ],
    total: 89.50,
    metodoPago: "efectivo",
    comprobante: "boleta",
    comprobanteNumero: "B001-00233",
    estado: "completado",
  },
  {
    id: "VTA-003",
    fecha: "2026-05-09T08:45:00",
    cliente: "Ana Rodríguez Torres",
    clienteEmail: "ana.rodriguez@empresa.com",
    clienteTelefono: "945678123",
    canal: "whatsapp",
    productos: [
      { nombre: "Papel Higiénico Elite 12und", cantidad: 2, precioUnitario: 28.90, subtotal: 57.80 },
      { nombre: "Detergente Ariel 2kg", cantidad: 1, precioUnitario: 34.50, subtotal: 34.50 },
      { nombre: "Lejía Clorox 1L", cantidad: 2, precioUnitario: 8.90, subtotal: 17.80 },
      { nombre: "Jabón Bolívar Pack", cantidad: 1, precioUnitario: 15.90, subtotal: 15.90 },
      { nombre: "Suavizante Downy 800ml", cantidad: 1, precioUnitario: 18.50, subtotal: 18.50 },
    ],
    total: 144.50,
    metodoPago: "plin",
    comprobante: "factura",
    comprobanteNumero: "F001-00089",
    estado: "completado",
    direccionEntrega: "Jr. Comercio 567, Miraflores",
  },
  {
    id: "VTA-004",
    fecha: "2026-05-09T08:00:00",
    cliente: "Luis Torres Vega",
    clienteEmail: "luis.torres@email.com",
    clienteTelefono: "978123456",
    canal: "presencial",
    productos: [
      { nombre: "Pan Francés (10 und)", cantidad: 1, precioUnitario: 5.00, subtotal: 5.00 },
      { nombre: "Mantequilla Gloria 200g", cantidad: 1, precioUnitario: 7.80, subtotal: 7.80 },
      { nombre: "Queso Fresco 250g", cantidad: 1, precioUnitario: 12.50, subtotal: 12.50 },
    ],
    total: 25.30,
    metodoPago: "efectivo",
    comprobante: "ninguno",
    estado: "completado",
  },
  {
    id: "VTA-005",
    fecha: "2026-05-08T17:30:00",
    cliente: "Rosa Sánchez Díaz",
    clienteEmail: "rosa.sanchez@email.com",
    clienteTelefono: "934567890",
    canal: "web",
    productos: [
      { nombre: "Yogurt Gloria Fresa 1L", cantidad: 4, precioUnitario: 8.90, subtotal: 35.60 },
      { nombre: "Cereal Ángel 500g", cantidad: 2, precioUnitario: 14.50, subtotal: 29.00 },
      { nombre: "Leche Condensada Gloria", cantidad: 3, precioUnitario: 6.80, subtotal: 20.40 },
    ],
    total: 85.00,
    metodoPago: "tarjeta",
    comprobante: "boleta",
    comprobanteNumero: "B001-00232",
    estado: "pendiente",
    direccionEntrega: "Calle Las Flores 123, Surco",
  },
  {
    id: "VTA-006",
    fecha: "2026-05-08T15:20:00",
    cliente: "Pedro Díaz Campos",
    clienteEmail: "pedro.diaz@email.com",
    clienteTelefono: "956789012",
    canal: "whatsapp",
    productos: [
      { nombre: "Agua San Luis 2.5L", cantidad: 6, precioUnitario: 4.50, subtotal: 27.00 },
      { nombre: "Gaseosa Inca Kola 2L", cantidad: 4, precioUnitario: 9.80, subtotal: 39.20 },
    ],
    total: 66.20,
    metodoPago: "yape",
    comprobante: "boleta",
    comprobanteNumero: "B001-00231",
    estado: "cancelado",
    direccionEntrega: "Av. Principal 890, La Molina",
    notas: "Cliente canceló por demora en entrega",
  },
  {
    id: "VTA-007",
    fecha: "2026-05-08T12:45:00",
    cliente: "Carmen Flores Quispe",
    clienteEmail: "carmen.flores@email.com",
    clienteTelefono: "923456789",
    canal: "presencial",
    productos: [
      { nombre: "Pollo Entero 2kg", cantidad: 1, precioUnitario: 28.00, subtotal: 28.00 },
      { nombre: "Papa Blanca 1kg", cantidad: 2, precioUnitario: 3.50, subtotal: 7.00 },
      { nombre: "Cebolla Roja 1kg", cantidad: 1, precioUnitario: 4.00, subtotal: 4.00 },
      { nombre: "Tomate 1kg", cantidad: 1, precioUnitario: 5.50, subtotal: 5.50 },
      { nombre: "Ají Amarillo 250g", cantidad: 1, precioUnitario: 3.00, subtotal: 3.00 },
    ],
    total: 47.50,
    metodoPago: "efectivo",
    comprobante: "boleta",
    comprobanteNumero: "B001-00230",
    estado: "completado",
  },
  {
    id: "VTA-008",
    fecha: "2026-05-08T10:00:00",
    cliente: "Jorge Huamán Pérez",
    clienteEmail: "jorge.huaman@empresa.com",
    clienteTelefono: "967890123",
    canal: "web",
    productos: [
      { nombre: "Café Altomayo 200g", cantidad: 3, precioUnitario: 18.90, subtotal: 56.70 },
      { nombre: "Galletas Oreo Pack", cantidad: 5, precioUnitario: 4.50, subtotal: 22.50 },
      { nombre: "Chocolate Sublime", cantidad: 10, precioUnitario: 2.50, subtotal: 25.00 },
    ],
    total: 104.20,
    metodoPago: "tarjeta",
    comprobante: "factura",
    comprobanteNumero: "F001-00088",
    estado: "completado",
    direccionEntrega: "Av. Javier Prado 456, San Borja",
  },
]

export default function SalesPage() {
  const [selectedOrder, setSelectedOrder] = useState<SaleOrder | null>(null)
  const [isDetailOpen, setIsDetailOpen] = useState(false)
  const [filters, setFilters] = useState({
    dateFrom: undefined as Date | undefined,
    dateTo: undefined as Date | undefined,
    canal: "todos" as string,
    search: "",
  })

  const filteredSales = salesData.filter((sale) => {
    if (filters.canal !== "todos" && sale.canal !== filters.canal) return false
    if (filters.search) {
      const search = filters.search.toLowerCase()
      if (
        !sale.id.toLowerCase().includes(search) &&
        !sale.cliente.toLowerCase().includes(search)
      ) {
        return false
      }
    }
    if (filters.dateFrom) {
      const saleDate = new Date(sale.fecha)
      if (saleDate < filters.dateFrom) return false
    }
    if (filters.dateTo) {
      const saleDate = new Date(sale.fecha)
      const endDate = new Date(filters.dateTo)
      endDate.setHours(23, 59, 59, 999)
      if (saleDate > endDate) return false
    }
    return true
  })

  const handleRowClick = (order: SaleOrder) => {
    setSelectedOrder(order)
    setIsDetailOpen(true)
  }

  // Calculate summary stats
  const totalVentas = filteredSales.length
  const montoTotal = filteredSales.reduce((sum, sale) => sum + sale.total, 0)
  const ticketPromedio = totalVentas > 0 ? montoTotal / totalVentas : 0
  const ventasOnline = filteredSales.filter((s) => s.canal === "web" || s.canal === "whatsapp").length
  const ventasPresencial = filteredSales.filter((s) => s.canal === "presencial").length

  return (
    <div className="min-h-screen bg-background flex">
      <AdminSidebar />

      <div className="flex-1 flex flex-col lg:ml-64">
        <AdminTopbar />

        <main className="flex-1 p-4 lg:p-6 space-y-6">
          {/* Page header */}
          <div>
            <h1 className="text-2xl font-bold text-foreground">Gestión de Ventas</h1>
            <p className="text-muted-foreground">
              Consulta y administra todas las ventas del minimarket
            </p>
          </div>

          {/* KPI Cards */}
          <SalesKpiCards
            totalVentas={totalVentas}
            montoTotal={montoTotal}
            ticketPromedio={ticketPromedio}
            ventasOnline={ventasOnline}
            ventasPresencial={ventasPresencial}
          />

          {/* Filters */}
          <SalesFilters filters={filters} onFiltersChange={setFilters} />

          {/* Sales Table */}
          <SalesTable sales={filteredSales} onRowClick={handleRowClick} />
        </main>
      </div>

      {/* Order Detail Sheet */}
      <OrderDetailSheet
        order={selectedOrder}
        open={isDetailOpen}
        onOpenChange={setIsDetailOpen}
      />
    </div>
  )
}

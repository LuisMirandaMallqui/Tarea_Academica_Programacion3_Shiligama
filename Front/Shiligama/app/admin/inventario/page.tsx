"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { InventoryKpiCards } from "@/components/admin/inventory/inventory-kpi-cards"
import { LowStockAlert } from "@/components/admin/inventory/low-stock-alert"
import { InventoryFilters } from "@/components/admin/inventory/inventory-filters"
import { InventoryTable } from "@/components/admin/inventory/inventory-table"
import { StockAdjustmentDialog } from "@/components/admin/inventory/stock-adjustment-dialog"

export type InventoryItem = {
  id: string
  codigo: string
  nombre: string
  categoria: string
  stockActual: number
  stockMinimo: number
  estado: "ok" | "bajo" | "agotado"
  ultimaActualizacion: string
  imagen: string
}

const initialInventory: InventoryItem[] = [
  {
    id: "1",
    codigo: "ABR001",
    nombre: "Arroz Costeño 1kg",
    categoria: "Abarrotes",
    stockActual: 45,
    stockMinimo: 20,
    estado: "ok",
    ultimaActualizacion: "2024-01-15 14:30",
    imagen: "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=100&h=100&fit=crop",
  },
  {
    id: "2",
    codigo: "BEB001",
    nombre: "Inca Kola 500ml",
    categoria: "Bebidas",
    stockActual: 8,
    stockMinimo: 15,
    estado: "bajo",
    ultimaActualizacion: "2024-01-15 10:15",
    imagen: "https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?w=100&h=100&fit=crop",
  },
  {
    id: "3",
    codigo: "LAC001",
    nombre: "Leche Gloria 1L",
    categoria: "Lácteos",
    stockActual: 0,
    stockMinimo: 25,
    estado: "agotado",
    ultimaActualizacion: "2024-01-14 16:45",
    imagen: "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=100&h=100&fit=crop",
  },
  {
    id: "4",
    codigo: "SNK001",
    nombre: "Papas Lays Original 150g",
    categoria: "Snacks",
    stockActual: 32,
    stockMinimo: 10,
    estado: "ok",
    ultimaActualizacion: "2024-01-15 09:00",
    imagen: "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=100&h=100&fit=crop",
  },
  {
    id: "5",
    codigo: "LIM001",
    nombre: "Detergente Bolivar 2kg",
    categoria: "Limpieza",
    stockActual: 5,
    stockMinimo: 12,
    estado: "bajo",
    ultimaActualizacion: "2024-01-15 11:30",
    imagen: "https://images.unsplash.com/photo-1585441695325-21557f8134b2?w=100&h=100&fit=crop",
  },
  {
    id: "6",
    codigo: "ABR002",
    nombre: "Aceite Primor 1L",
    categoria: "Abarrotes",
    stockActual: 0,
    stockMinimo: 15,
    estado: "agotado",
    ultimaActualizacion: "2024-01-13 08:20",
    imagen: "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=100&h=100&fit=crop",
  },
  {
    id: "7",
    codigo: "BEB002",
    nombre: "Agua San Luis 625ml",
    categoria: "Bebidas",
    stockActual: 60,
    stockMinimo: 30,
    estado: "ok",
    ultimaActualizacion: "2024-01-15 13:00",
    imagen: "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=100&h=100&fit=crop",
  },
  {
    id: "8",
    codigo: "LAC002",
    nombre: "Yogurt Gloria Fresa 1L",
    categoria: "Lácteos",
    stockActual: 3,
    stockMinimo: 10,
    estado: "bajo",
    ultimaActualizacion: "2024-01-15 07:45",
    imagen: "https://images.unsplash.com/photo-1571212515416-fef01fc43637?w=100&h=100&fit=crop",
  },
]

export default function InventarioPage() {
  const [inventory, setInventory] = useState<InventoryItem[]>(initialInventory)
  const [searchQuery, setSearchQuery] = useState("")
  const [categoryFilter, setCategoryFilter] = useState("all")
  const [statusFilter, setStatusFilter] = useState("all")
  const [adjustmentDialogOpen, setAdjustmentDialogOpen] = useState(false)
  const [selectedProduct, setSelectedProduct] = useState<InventoryItem | null>(null)

  // Calculate KPIs
  const totalProducts = inventory.length
  const lowStockItems = inventory.filter((item) => item.estado === "bajo").length
  const outOfStockItems = inventory.filter((item) => item.estado === "agotado").length
  const lastUpdate = "Hoy, 14:30"

  // Get low stock products for alert
  const lowStockProducts = inventory.filter(
    (item) => item.estado === "bajo" || item.estado === "agotado"
  )

  // Filter inventory
  const filteredInventory = inventory.filter((item) => {
    const matchesSearch =
      item.nombre.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.codigo.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesCategory = categoryFilter === "all" || item.categoria === categoryFilter
    const matchesStatus = statusFilter === "all" || item.estado === statusFilter
    return matchesSearch && matchesCategory && matchesStatus
  })

  const handleAdjustStock = (product: InventoryItem) => {
    setSelectedProduct(product)
    setAdjustmentDialogOpen(true)
  }

  const handleStockUpdate = (
    productId: string,
    type: "entrada" | "salida",
    quantity: number
  ) => {
    setInventory((prev) =>
      prev.map((item) => {
        if (item.id === productId) {
          const newStock =
            type === "entrada"
              ? item.stockActual + quantity
              : Math.max(0, item.stockActual - quantity)
          
          let newEstado: "ok" | "bajo" | "agotado" = "ok"
          if (newStock === 0) {
            newEstado = "agotado"
          } else if (newStock < item.stockMinimo) {
            newEstado = "bajo"
          }

          return {
            ...item,
            stockActual: newStock,
            estado: newEstado,
            ultimaActualizacion: new Date().toLocaleString("es-PE", {
              year: "numeric",
              month: "2-digit",
              day: "2-digit",
              hour: "2-digit",
              minute: "2-digit",
            }),
          }
        }
        return item
      })
    )
    setAdjustmentDialogOpen(false)
    setSelectedProduct(null)
  }

  return (
    <div className="flex min-h-screen bg-muted/30">
      <AdminSidebar />
      
      <div className="flex-1 flex flex-col lg:ml-64">
        <AdminTopbar />
        
        <main className="flex-1 p-4 lg:p-6 space-y-6">
          {/* Page Header */}
          <div>
            <h1 className="text-2xl font-bold text-foreground">Inventario</h1>
            <p className="text-muted-foreground">
              Gestiona el stock de productos de tu minimarket
            </p>
          </div>

          {/* KPI Cards */}
          <InventoryKpiCards
            totalProducts={totalProducts}
            lowStockItems={lowStockItems}
            outOfStockItems={outOfStockItems}
            lastUpdate={lastUpdate}
          />

          {/* Low Stock Alert */}
          {lowStockProducts.length > 0 && (
            <LowStockAlert products={lowStockProducts} />
          )}

          {/* Filters */}
          <InventoryFilters
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            categoryFilter={categoryFilter}
            onCategoryChange={setCategoryFilter}
            statusFilter={statusFilter}
            onStatusChange={setStatusFilter}
          />

          {/* Inventory Table */}
          <InventoryTable
            inventory={filteredInventory}
            onAdjustStock={handleAdjustStock}
          />

          {/* Stock Adjustment Dialog */}
          <StockAdjustmentDialog
            open={adjustmentDialogOpen}
            onOpenChange={setAdjustmentDialogOpen}
            product={selectedProduct}
            onSubmit={handleStockUpdate}
          />
        </main>
      </div>
    </div>
  )
}

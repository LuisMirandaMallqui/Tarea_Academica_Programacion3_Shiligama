"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { ReturnsKpiCards } from "@/components/admin/returns/returns-kpi-cards"
import { ReturnsFilters } from "@/components/admin/returns/returns-filters"
import { ReturnsTable } from "@/components/admin/returns/returns-table"
import { ReturnsChart } from "@/components/admin/returns/returns-chart"
import { ReturnDialog } from "@/components/admin/returns/return-dialog"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"

// Sample products for dropdown
const products = [
  { id: "1", name: "Leche Gloria 1L", code: "PRD-001", price: 6.50 },
  { id: "2", name: "Arroz Costeño 1kg", code: "PRD-002", price: 4.80 },
  { id: "3", name: "Aceite Primor 1L", code: "PRD-003", price: 12.90 },
  { id: "4", name: "Azúcar Rubia 1kg", code: "PRD-004", price: 4.20 },
  { id: "5", name: "Fideos Don Vittorio", code: "PRD-005", price: 3.50 },
  { id: "6", name: "Yogurt Gloria Fresa", code: "PRD-006", price: 3.80 },
  { id: "7", name: "Pan de Molde Bimbo", code: "PRD-007", price: 8.50 },
  { id: "8", name: "Galletas Oreo", code: "PRD-008", price: 4.50 },
  { id: "9", name: "Gaseosa Inca Kola 1.5L", code: "PRD-009", price: 7.80 },
  { id: "10", name: "Atún Florida", code: "PRD-010", price: 6.90 },
]

// Sample returns data
const initialReturns = [
  {
    id: "DEV-001",
    date: "2024-01-15",
    product: "Leche Gloria 1L",
    productCode: "PRD-001",
    quantity: 12,
    reason: "Vencido" as const,
    registeredBy: "María García",
    amount: 78.00,
    observations: "Lote completo vencido antes de fecha esperada"
  },
  {
    id: "DEV-002",
    date: "2024-01-14",
    product: "Yogurt Gloria Fresa",
    productCode: "PRD-006",
    quantity: 8,
    reason: "Dañado" as const,
    registeredBy: "Carlos López",
    amount: 30.40,
    observations: "Envases aplastados en transporte"
  },
  {
    id: "DEV-003",
    date: "2024-01-14",
    product: "Pan de Molde Bimbo",
    productCode: "PRD-007",
    quantity: 5,
    reason: "Vencido" as const,
    registeredBy: "María García",
    amount: 42.50,
    observations: "Producto pasó fecha de vencimiento"
  },
  {
    id: "DEV-004",
    date: "2024-01-13",
    product: "Galletas Oreo",
    productCode: "PRD-008",
    quantity: 3,
    reason: "Dañado" as const,
    registeredBy: "Ana Torres",
    amount: 13.50,
    observations: "Paquetes rotos"
  },
  {
    id: "DEV-005",
    date: "2024-01-12",
    product: "Arroz Costeño 1kg",
    productCode: "PRD-002",
    quantity: 2,
    reason: "Error de pedido" as const,
    registeredBy: "Carlos López",
    amount: 9.60,
    observations: "Cliente pidió otra marca"
  },
  {
    id: "DEV-006",
    date: "2024-01-12",
    product: "Aceite Primor 1L",
    productCode: "PRD-003",
    quantity: 1,
    reason: "Dañado" as const,
    registeredBy: "María García",
    amount: 12.90,
    observations: "Botella con fuga"
  },
  {
    id: "DEV-007",
    date: "2024-01-11",
    product: "Gaseosa Inca Kola 1.5L",
    productCode: "PRD-009",
    quantity: 6,
    reason: "Otro" as const,
    registeredBy: "Ana Torres",
    amount: 46.80,
    observations: "Retiro por campaña del fabricante"
  },
  {
    id: "DEV-008",
    date: "2024-01-10",
    product: "Leche Gloria 1L",
    productCode: "PRD-001",
    quantity: 4,
    reason: "Vencido" as const,
    registeredBy: "Carlos López",
    amount: 26.00,
    observations: "Vencimiento próximo, no se vendió a tiempo"
  },
]

export type ReturnReason = "Vencido" | "Dañado" | "Error de pedido" | "Otro"

export interface ReturnItem {
  id: string
  date: string
  product: string
  productCode: string
  quantity: number
  reason: ReturnReason
  registeredBy: string
  amount: number
  observations?: string
}

export default function ReturnsPage() {
  const [returns, setReturns] = useState<ReturnItem[]>(initialReturns)
  const [searchQuery, setSearchQuery] = useState("")
  const [reasonFilter, setReasonFilter] = useState<string>("todos")
  const [dialogOpen, setDialogOpen] = useState(false)
  const [editingReturn, setEditingReturn] = useState<ReturnItem | null>(null)

  // Filter returns
  const filteredReturns = returns.filter((item) => {
    const matchesSearch = 
      item.product.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.productCode.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.registeredBy.toLowerCase().includes(searchQuery.toLowerCase())
    
    const matchesReason = reasonFilter === "todos" || item.reason === reasonFilter

    return matchesSearch && matchesReason
  })

  // Calculate KPIs
  const totalReturns = returns.length
  const totalAmount = returns.reduce((sum, item) => sum + item.amount, 0)
  
  // Find most returned product
  const productCounts = returns.reduce((acc, item) => {
    acc[item.product] = (acc[item.product] || 0) + item.quantity
    return acc
  }, {} as Record<string, number>)
  
  const mostReturnedProduct = Object.entries(productCounts).sort((a, b) => b[1] - a[1])[0]

  // Calculate chart data
  const reasonCounts = returns.reduce((acc, item) => {
    acc[item.reason] = (acc[item.reason] || 0) + item.amount
    return acc
  }, {} as Record<string, number>)

  const chartData = [
    { reason: "Vencido", amount: reasonCounts["Vencido"] || 0 },
    { reason: "Dañado", amount: reasonCounts["Dañado"] || 0 },
    { reason: "Error de pedido", amount: reasonCounts["Error de pedido"] || 0 },
    { reason: "Otro", amount: reasonCounts["Otro"] || 0 },
  ]

  const handleAddReturn = (data: Omit<ReturnItem, "id">) => {
    const newReturn: ReturnItem = {
      ...data,
      id: `DEV-${String(returns.length + 1).padStart(3, "0")}`,
    }
    setReturns([newReturn, ...returns])
    setDialogOpen(false)
  }

  const handleEditReturn = (data: Omit<ReturnItem, "id">) => {
    if (!editingReturn) return
    setReturns(returns.map((r) => 
      r.id === editingReturn.id ? { ...data, id: editingReturn.id } : r
    ))
    setEditingReturn(null)
    setDialogOpen(false)
  }

  const handleDeleteReturn = (id: string) => {
    setReturns(returns.filter((r) => r.id !== id))
  }

  const openEditDialog = (item: ReturnItem) => {
    setEditingReturn(item)
    setDialogOpen(true)
  }

  const openNewDialog = () => {
    setEditingReturn(null)
    setDialogOpen(true)
  }

  return (
    <div className="min-h-screen bg-background">
      <AdminSidebar />
      
      <div className="lg:pl-64">
        <AdminTopbar />
        
        <main className="p-6 space-y-6">
          {/* Header */}
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div>
              <h1 className="text-2xl font-bold text-foreground">Devoluciones y Mermas</h1>
              <p className="text-muted-foreground">Gestiona las devoluciones y pérdidas del inventario</p>
            </div>
            <Button onClick={openNewDialog} className="gap-2">
              <Plus className="h-4 w-4" />
              Registrar Devolución
            </Button>
          </div>

          {/* KPI Cards */}
          <ReturnsKpiCards
            totalReturns={totalReturns}
            totalAmount={totalAmount}
            mostReturnedProduct={mostReturnedProduct ? {
              name: mostReturnedProduct[0],
              count: mostReturnedProduct[1]
            } : null}
          />

          {/* Mermas Chart */}
          <ReturnsChart data={chartData} />

          {/* Filters */}
          <ReturnsFilters
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            reasonFilter={reasonFilter}
            onReasonChange={setReasonFilter}
          />

          {/* Table */}
          <ReturnsTable
            returns={filteredReturns}
            onEdit={openEditDialog}
            onDelete={handleDeleteReturn}
          />

          {/* Dialog */}
          <ReturnDialog
            open={dialogOpen}
            onOpenChange={setDialogOpen}
            products={products}
            editingReturn={editingReturn}
            onSubmit={editingReturn ? handleEditReturn : handleAddReturn}
          />
        </main>
      </div>
    </div>
  )
}

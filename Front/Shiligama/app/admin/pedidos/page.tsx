"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { OrdersHeader } from "@/components/admin/orders/orders-header"
import { OrdersTabs } from "@/components/admin/orders/orders-tabs"
import { OrderCard } from "@/components/admin/orders/order-card"
import { OrderDetailDialog } from "@/components/admin/orders/order-detail-dialog"

export type OrderStatus = "pendiente" | "preparacion" | "listo" | "entregado" | "cancelado"
export type DeliveryMethod = "delivery" | "recojo"
export type PaymentMethod = "yape" | "plin" | "tarjeta" | "efectivo"
export type PaymentStatus = "pendiente" | "pagado"

export interface OrderItem {
  id: string
  name: string
  quantity: number
  price: number
}

export interface Order {
  id: string
  orderNumber: string
  clientName: string
  clientPhone: string
  orderTime: Date
  deliveryMethod: DeliveryMethod
  items: OrderItem[]
  total: number
  paymentMethod: PaymentMethod
  paymentStatus: PaymentStatus
  status: OrderStatus
  address?: string
}

// Sample orders data
const SAMPLE_ORDERS: Order[] = [
  {
    id: "1",
    orderNumber: "ORD-001",
    clientName: "María García",
    clientPhone: "987654321",
    orderTime: new Date(Date.now() - 15 * 60000),
    deliveryMethod: "delivery",
    items: [
      { id: "1", name: "Leche Gloria 1L", quantity: 2, price: 7.50 },
      { id: "2", name: "Pan de Molde Bimbo", quantity: 1, price: 8.90 },
      { id: "3", name: "Arroz Costeño 1kg", quantity: 2, price: 5.20 },
    ],
    total: 34.30,
    paymentMethod: "yape",
    paymentStatus: "pagado",
    status: "pendiente",
    address: "Av. Los Pinos 234, San Isidro"
  },
  {
    id: "2",
    orderNumber: "ORD-002",
    clientName: "Carlos Rodríguez",
    clientPhone: "912345678",
    orderTime: new Date(Date.now() - 25 * 60000),
    deliveryMethod: "recojo",
    items: [
      { id: "4", name: "Coca Cola 1.5L", quantity: 3, price: 8.50 },
      { id: "5", name: "Galletas Oreo", quantity: 2, price: 3.80 },
    ],
    total: 33.10,
    paymentMethod: "efectivo",
    paymentStatus: "pendiente",
    status: "pendiente"
  },
  {
    id: "3",
    orderNumber: "ORD-003",
    clientName: "Ana López",
    clientPhone: "945678123",
    orderTime: new Date(Date.now() - 45 * 60000),
    deliveryMethod: "delivery",
    items: [
      { id: "6", name: "Aceite Primor 1L", quantity: 1, price: 12.90 },
      { id: "7", name: "Fideos Don Vittorio", quantity: 3, price: 3.50 },
      { id: "8", name: "Atún Florida", quantity: 4, price: 6.80 },
    ],
    total: 50.60,
    paymentMethod: "plin",
    paymentStatus: "pagado",
    status: "preparacion",
    address: "Jr. Las Flores 567, Miraflores"
  },
  {
    id: "4",
    orderNumber: "ORD-004",
    clientName: "Pedro Sánchez",
    clientPhone: "978123456",
    orderTime: new Date(Date.now() - 60 * 60000),
    deliveryMethod: "recojo",
    items: [
      { id: "9", name: "Cerveza Pilsen Pack", quantity: 1, price: 42.00 },
      { id: "10", name: "Piqueos Snax", quantity: 2, price: 4.50 },
    ],
    total: 51.00,
    paymentMethod: "tarjeta",
    paymentStatus: "pagado",
    status: "listo"
  },
  {
    id: "5",
    orderNumber: "ORD-005",
    clientName: "Lucía Fernández",
    clientPhone: "934567890",
    orderTime: new Date(Date.now() - 120 * 60000),
    deliveryMethod: "delivery",
    items: [
      { id: "11", name: "Yogurt Gloria Pack", quantity: 2, price: 9.80 },
      { id: "12", name: "Cereal Ángel", quantity: 1, price: 14.50 },
    ],
    total: 34.10,
    paymentMethod: "yape",
    paymentStatus: "pagado",
    status: "entregado",
    address: "Calle Los Álamos 123, Surco"
  },
  {
    id: "6",
    orderNumber: "ORD-006",
    clientName: "Roberto Díaz",
    clientPhone: "956789012",
    orderTime: new Date(Date.now() - 180 * 60000),
    deliveryMethod: "delivery",
    items: [
      { id: "13", name: "Agua San Luis 2.5L", quantity: 6, price: 3.50 },
    ],
    total: 21.00,
    paymentMethod: "efectivo",
    paymentStatus: "pendiente",
    status: "cancelado",
    address: "Av. Arequipa 890, Lince"
  },
  {
    id: "7",
    orderNumber: "ORD-007",
    clientName: "Carmen Vega",
    clientPhone: "923456789",
    orderTime: new Date(Date.now() - 10 * 60000),
    deliveryMethod: "delivery",
    items: [
      { id: "14", name: "Huevos La Calera x30", quantity: 1, price: 18.50 },
      { id: "15", name: "Mantequilla Laive", quantity: 2, price: 7.20 },
      { id: "16", name: "Queso Edam", quantity: 1, price: 15.90 },
    ],
    total: 48.80,
    paymentMethod: "plin",
    paymentStatus: "pagado",
    status: "preparacion",
    address: "Calle San Martín 456, Jesús María"
  },
]

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>(SAMPLE_ORDERS)
  const [activeTab, setActiveTab] = useState<OrderStatus | "todos">("todos")
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null)
  const [detailOpen, setDetailOpen] = useState(false)

  // Filter orders based on tab and search
  const filteredOrders = orders.filter((order) => {
    const matchesTab = activeTab === "todos" || order.status === activeTab
    const matchesSearch = 
      order.orderNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.clientName.toLowerCase().includes(searchQuery.toLowerCase())
    return matchesTab && matchesSearch
  })

  // Count orders by status
  const statusCounts = {
    todos: orders.length,
    pendiente: orders.filter(o => o.status === "pendiente").length,
    preparacion: orders.filter(o => o.status === "preparacion").length,
    listo: orders.filter(o => o.status === "listo").length,
    entregado: orders.filter(o => o.status === "entregado").length,
    cancelado: orders.filter(o => o.status === "cancelado").length,
  }

  // Advance order status
  const advanceStatus = (orderId: string) => {
    setOrders(prev => prev.map(order => {
      if (order.id !== orderId) return order
      
      const statusFlow: Record<OrderStatus, OrderStatus | null> = {
        pendiente: "preparacion",
        preparacion: "listo",
        listo: "entregado",
        entregado: null,
        cancelado: null,
      }
      
      const nextStatus = statusFlow[order.status]
      if (!nextStatus) return order
      
      return { ...order, status: nextStatus }
    }))
  }

  // Cancel order
  const cancelOrder = (orderId: string) => {
    setOrders(prev => prev.map(order => 
      order.id === orderId ? { ...order, status: "cancelado" as OrderStatus } : order
    ))
  }

  // View order details
  const viewDetails = (order: Order) => {
    setSelectedOrder(order)
    setDetailOpen(true)
  }

  return (
    <div className="flex h-screen bg-background">
      <AdminSidebar />
      
      <div className="flex-1 flex flex-col overflow-hidden lg:ml-64">
        <AdminTopbar />
        
        <main className="flex-1 overflow-y-auto p-6">
          <div className="max-w-7xl mx-auto space-y-6">
            {/* Header */}
            <OrdersHeader 
              searchQuery={searchQuery}
              onSearchChange={setSearchQuery}
            />

            {/* Status Tabs */}
            <OrdersTabs 
              activeTab={activeTab}
              onTabChange={setActiveTab}
              counts={statusCounts}
            />

            {/* Orders Grid */}
            {filteredOrders.length > 0 ? (
              <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
                {filteredOrders.map((order) => (
                  <OrderCard
                    key={order.id}
                    order={order}
                    onAdvance={() => advanceStatus(order.id)}
                    onCancel={() => cancelOrder(order.id)}
                    onViewDetails={() => viewDetails(order)}
                  />
                ))}
              </div>
            ) : (
              <div className="flex flex-col items-center justify-center py-12 text-center">
                <div className="h-16 w-16 rounded-full bg-muted flex items-center justify-center mb-4">
                  <svg className="h-8 w-8 text-muted-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                </div>
                <h3 className="text-lg font-medium text-foreground mb-1">No hay pedidos</h3>
                <p className="text-sm text-muted-foreground">
                  {searchQuery 
                    ? "No se encontraron pedidos con ese criterio de búsqueda"
                    : "No hay pedidos en este estado actualmente"
                  }
                </p>
              </div>
            )}
          </div>
        </main>
      </div>

      {/* Order Detail Dialog */}
      <OrderDetailDialog
        order={selectedOrder}
        open={detailOpen}
        onOpenChange={setDetailOpen}
        onAdvance={() => selectedOrder && advanceStatus(selectedOrder.id)}
        onCancel={() => selectedOrder && cancelOrder(selectedOrder.id)}
      />
    </div>
  )
}

"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { NotificationsHeader } from "@/components/admin/notifications/notifications-header"
import { NotificationsFilters } from "@/components/admin/notifications/notifications-filters"
import { NotificationsList } from "@/components/admin/notifications/notifications-list"

export type NotificationType = "stock" | "order" | "payment" | "system"
export type NotificationStatus = "unread" | "read"

export interface Notification {
  id: string
  type: NotificationType
  title: string
  description: string
  timestamp: Date
  status: NotificationStatus
  data?: {
    productId?: string
    productName?: string
    currentStock?: number
    minStock?: number
    orderId?: string
    orderTotal?: number
    clientName?: string
    paymentId?: string
    paymentAmount?: number
    paymentMethod?: string
  }
}

const initialNotifications: Notification[] = [
  {
    id: "1",
    type: "stock",
    title: "Stock bajo: Aceite Primor 1L",
    description: "Solo quedan 3 unidades. Stock mínimo: 10 unidades.",
    timestamp: new Date(2024, 11, 15, 14, 30),
    status: "unread",
    data: {
      productId: "PRD001",
      productName: "Aceite Primor 1L",
      currentStock: 3,
      minStock: 10,
    },
  },
  {
    id: "2",
    type: "order",
    title: "Nuevo pedido #ORD-2024-156",
    description: "María García ha realizado un pedido por S/. 85.50",
    timestamp: new Date(2024, 11, 15, 14, 25),
    status: "unread",
    data: {
      orderId: "ORD-2024-156",
      orderTotal: 85.50,
      clientName: "María García",
    },
  },
  {
    id: "3",
    type: "stock",
    title: "Producto agotado: Leche Gloria 1L",
    description: "El producto está sin stock. Requiere reposición urgente.",
    timestamp: new Date(2024, 11, 15, 14, 15),
    status: "unread",
    data: {
      productId: "PRD002",
      productName: "Leche Gloria 1L",
      currentStock: 0,
      minStock: 15,
    },
  },
  {
    id: "4",
    type: "payment",
    title: "Pago pendiente de confirmación",
    description: "Pedido #ORD-2024-155 - Pago Yape por S/. 45.00 pendiente de verificar",
    timestamp: new Date(2024, 11, 15, 13, 50),
    status: "unread",
    data: {
      paymentId: "PAY-2024-089",
      paymentAmount: 45.00,
      paymentMethod: "Yape",
      orderId: "ORD-2024-155",
    },
  },
  {
    id: "5",
    type: "order",
    title: "Nuevo pedido #ORD-2024-154",
    description: "Carlos Mendoza ha realizado un pedido por S/. 120.00",
    timestamp: new Date(2024, 11, 15, 12, 30),
    status: "read",
    data: {
      orderId: "ORD-2024-154",
      orderTotal: 120.00,
      clientName: "Carlos Mendoza",
    },
  },
  {
    id: "6",
    type: "payment",
    title: "Pago confirmado",
    description: "Pedido #ORD-2024-153 - Pago con tarjeta por S/. 67.50 confirmado",
    timestamp: new Date(2024, 11, 15, 11, 45),
    status: "read",
    data: {
      paymentId: "PAY-2024-088",
      paymentAmount: 67.50,
      paymentMethod: "Tarjeta",
      orderId: "ORD-2024-153",
    },
  },
  {
    id: "7",
    type: "system",
    title: "Respaldo automático completado",
    description: "Se ha realizado el respaldo diario de la base de datos exitosamente.",
    timestamp: new Date(2024, 11, 15, 6, 0),
    status: "read",
  },
  {
    id: "8",
    type: "stock",
    title: "Stock bajo: Arroz Costeño 5kg",
    description: "Solo quedan 5 unidades. Stock mínimo: 20 unidades.",
    timestamp: new Date(2024, 11, 14, 18, 20),
    status: "read",
    data: {
      productId: "PRD003",
      productName: "Arroz Costeño 5kg",
      currentStock: 5,
      minStock: 20,
    },
  },
  {
    id: "9",
    type: "system",
    title: "Actualización del sistema",
    description: "Nueva versión 2.1.0 disponible con mejoras de rendimiento.",
    timestamp: new Date(2024, 11, 14, 10, 0),
    status: "read",
  },
  {
    id: "10",
    type: "system",
    title: "Mantenimiento programado",
    description: "El sistema estará en mantenimiento el 20/12 de 02:00 a 04:00 hrs.",
    timestamp: new Date(2024, 11, 13, 9, 0),
    status: "read",
  },
]

export default function NotificationsPage() {
  const [notifications, setNotifications] = useState<Notification[]>(initialNotifications)
  const [activeFilter, setActiveFilter] = useState<NotificationType | "all">("all")

  const filteredNotifications = activeFilter === "all" 
    ? notifications 
    : notifications.filter(n => n.type === activeFilter)

  const unreadCount = notifications.filter(n => n.status === "unread").length

  const handleMarkAllRead = () => {
    setNotifications(prev => 
      prev.map(n => ({ ...n, status: "read" as NotificationStatus }))
    )
  }

  const handleMarkAsRead = (id: string) => {
    setNotifications(prev =>
      prev.map(n => n.id === id ? { ...n, status: "read" as NotificationStatus } : n)
    )
  }

  const handleDismiss = (id: string) => {
    setNotifications(prev => prev.filter(n => n.id !== id))
  }

  const handleAcceptOrder = (id: string) => {
    handleMarkAsRead(id)
    // In real app, would call API to accept order
  }

  const handleRejectOrder = (id: string) => {
    handleMarkAsRead(id)
    // In real app, would call API to reject order
  }

  const handleRestock = (id: string) => {
    handleMarkAsRead(id)
    // In real app, would navigate to inventory or open restock modal
  }

  const handleConfirmPayment = (id: string) => {
    setNotifications(prev =>
      prev.map(n => {
        if (n.id === id) {
          return {
            ...n,
            status: "read" as NotificationStatus,
            title: "Pago confirmado",
            description: n.description.replace("pendiente de verificar", "confirmado"),
          }
        }
        return n
      })
    )
  }

  return (
    <div className="min-h-screen bg-background">
      <AdminSidebar />
      <div className="flex-1 flex flex-col overflow-hidden lg:ml-64">
        <AdminTopbar />
        <main className="flex-1 overflow-auto p-6">
          <div className="max-w-4xl mx-auto space-y-6">
            <NotificationsHeader 
              unreadCount={unreadCount} 
              onMarkAllRead={handleMarkAllRead} 
            />
            
            <NotificationsFilters 
              activeFilter={activeFilter}
              onFilterChange={setActiveFilter}
              notifications={notifications}
            />
            
            <NotificationsList
              notifications={filteredNotifications}
              onMarkAsRead={handleMarkAsRead}
              onDismiss={handleDismiss}
              onAcceptOrder={handleAcceptOrder}
              onRejectOrder={handleRejectOrder}
              onRestock={handleRestock}
              onConfirmPayment={handleConfirmPayment}
            />
          </div>
        </main>
      </div>
    </div>
  )
}

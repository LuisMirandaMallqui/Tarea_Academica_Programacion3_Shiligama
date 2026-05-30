"use client"

import { 
  Package, 
  ShoppingCart, 
  CreditCard, 
  Settings,
  AlertTriangle,
  CheckCircle2,
  Clock,
  X,
  Check,
  RefreshCw,
  MoreHorizontal,
  Eye
} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { cn } from "@/lib/utils"
import type { Notification, NotificationType } from "@/app/admin/notificaciones/page"

interface NotificationsListProps {
  notifications: Notification[]
  onMarkAsRead: (id: string) => void
  onDismiss: (id: string) => void
  onAcceptOrder: (id: string) => void
  onRejectOrder: (id: string) => void
  onRestock: (id: string) => void
  onConfirmPayment: (id: string) => void
}

const typeConfig: Record<NotificationType, {
  icon: typeof Package
  borderColor: string
  bgColor: string
  iconColor: string
}> = {
  stock: {
    icon: AlertTriangle,
    borderColor: "border-l-red-500",
    bgColor: "bg-red-50",
    iconColor: "text-red-600",
  },
  order: {
    icon: ShoppingCart,
    borderColor: "border-l-green-500",
    bgColor: "bg-green-50",
    iconColor: "text-green-600",
  },
  payment: {
    icon: CreditCard,
    borderColor: "border-l-yellow-500",
    bgColor: "bg-yellow-50",
    iconColor: "text-yellow-600",
  },
  system: {
    icon: Settings,
    borderColor: "border-l-blue-500",
    bgColor: "bg-blue-50",
    iconColor: "text-blue-600",
  },
}

function formatTimestamp(date: Date): string {
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return "Ahora"
  if (minutes < 60) return `Hace ${minutes} min`
  if (hours < 24) return `Hace ${hours}h`
  if (days === 1) return "Ayer"
  if (days < 7) return `Hace ${days} días`
  
  return date.toLocaleDateString("es-PE", {
    day: "numeric",
    month: "short",
  })
}

export function NotificationsList({
  notifications,
  onMarkAsRead,
  onDismiss,
  onAcceptOrder,
  onRejectOrder,
  onRestock,
  onConfirmPayment,
}: NotificationsListProps) {
  if (notifications.length === 0) {
    return (
      <Card className="p-12 text-center">
        <div className="flex flex-col items-center gap-3">
          <div className="flex h-16 w-16 items-center justify-center rounded-full bg-muted">
            <CheckCircle2 className="h-8 w-8 text-muted-foreground" />
          </div>
          <h3 className="text-lg font-medium text-foreground">No hay notificaciones</h3>
          <p className="text-sm text-muted-foreground">
            No tienes notificaciones en esta categoría
          </p>
        </div>
      </Card>
    )
  }

  // Group notifications by date
  const groupedNotifications: { label: string; items: Notification[] }[] = []
  const today = new Date()
  const yesterday = new Date(today)
  yesterday.setDate(yesterday.getDate() - 1)

  const todayItems = notifications.filter(n => 
    n.timestamp.toDateString() === today.toDateString()
  )
  const yesterdayItems = notifications.filter(n => 
    n.timestamp.toDateString() === yesterday.toDateString()
  )
  const olderItems = notifications.filter(n => 
    n.timestamp.toDateString() !== today.toDateString() &&
    n.timestamp.toDateString() !== yesterday.toDateString()
  )

  if (todayItems.length > 0) {
    groupedNotifications.push({ label: "Hoy", items: todayItems })
  }
  if (yesterdayItems.length > 0) {
    groupedNotifications.push({ label: "Ayer", items: yesterdayItems })
  }
  if (olderItems.length > 0) {
    groupedNotifications.push({ label: "Anteriores", items: olderItems })
  }

  return (
    <div className="space-y-6">
      {groupedNotifications.map((group) => (
        <div key={group.label} className="space-y-3">
          <h3 className="text-sm font-medium text-muted-foreground px-1">
            {group.label}
          </h3>
          <div className="space-y-2">
            {group.items.map((notification) => {
              const config = typeConfig[notification.type]
              const Icon = config.icon
              const isUnread = notification.status === "unread"
              const isPendingPayment = notification.type === "payment" && 
                notification.description.includes("pendiente")

              return (
                <Card
                  key={notification.id}
                  className={cn(
                    "relative overflow-hidden border-l-4 transition-all hover:shadow-md",
                    config.borderColor,
                    isUnread && "bg-primary/5"
                  )}
                >
                  <div className="flex items-start gap-4 p-4">
                    {/* Icon */}
                    <div className={cn(
                      "flex h-10 w-10 shrink-0 items-center justify-center rounded-full",
                      config.bgColor
                    )}>
                      <Icon className={cn("h-5 w-5", config.iconColor)} />
                    </div>

                    {/* Content */}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2">
                        <div className="flex-1 min-w-0">
                          <div className="flex items-center gap-2">
                            <h4 className={cn(
                              "text-sm truncate",
                              isUnread ? "font-semibold text-foreground" : "font-medium text-foreground/80"
                            )}>
                              {notification.title}
                            </h4>
                            {isUnread && (
                              <span className="flex h-2 w-2 shrink-0 rounded-full bg-primary" />
                            )}
                          </div>
                          <p className="text-sm text-muted-foreground mt-0.5 line-clamp-2">
                            {notification.description}
                          </p>
                        </div>

                        {/* Timestamp and actions */}
                        <div className="flex items-center gap-2 shrink-0">
                          <span className="text-xs text-muted-foreground flex items-center gap-1">
                            <Clock className="h-3 w-3" />
                            {formatTimestamp(notification.timestamp)}
                          </span>
                          <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                              <Button variant="ghost" size="icon" className="h-8 w-8">
                                <MoreHorizontal className="h-4 w-4" />
                              </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                              {isUnread && (
                                <DropdownMenuItem onClick={() => onMarkAsRead(notification.id)}>
                                  <Eye className="mr-2 h-4 w-4" />
                                  Marcar como leída
                                </DropdownMenuItem>
                              )}
                              <DropdownMenuItem 
                                onClick={() => onDismiss(notification.id)}
                                className="text-destructive"
                              >
                                <X className="mr-2 h-4 w-4" />
                                Descartar
                              </DropdownMenuItem>
                            </DropdownMenuContent>
                          </DropdownMenu>
                        </div>
                      </div>

                      {/* Action buttons based on type */}
                      <div className="flex flex-wrap items-center gap-2 mt-3">
                        {notification.type === "stock" && (
                          <>
                            {notification.data?.currentStock !== undefined && (
                              <span className={cn(
                                "text-xs font-medium px-2 py-0.5 rounded-full",
                                notification.data.currentStock === 0 
                                  ? "bg-red-100 text-red-700"
                                  : "bg-yellow-100 text-yellow-700"
                              )}>
                                Stock: {notification.data.currentStock} / {notification.data.minStock}
                              </span>
                            )}
                            <Button 
                              size="sm" 
                              variant="outline"
                              onClick={() => onRestock(notification.id)}
                              className="h-7 text-xs gap-1"
                            >
                              <RefreshCw className="h-3 w-3" />
                              Reabastecer
                            </Button>
                          </>
                        )}

                        {notification.type === "order" && isUnread && (
                          <>
                            <Button
                              size="sm"
                              onClick={() => onAcceptOrder(notification.id)}
                              className="h-7 text-xs gap-1"
                            >
                              <Check className="h-3 w-3" />
                              Aceptar
                            </Button>
                            <Button
                              size="sm"
                              variant="outline"
                              onClick={() => onRejectOrder(notification.id)}
                              className="h-7 text-xs gap-1 text-destructive hover:text-destructive"
                            >
                              <X className="h-3 w-3" />
                              Rechazar
                            </Button>
                          </>
                        )}

                        {notification.type === "payment" && isPendingPayment && (
                          <Button
                            size="sm"
                            onClick={() => onConfirmPayment(notification.id)}
                            className="h-7 text-xs gap-1"
                          >
                            <Check className="h-3 w-3" />
                            Confirmar pago
                          </Button>
                        )}

                        {notification.type === "payment" && !isPendingPayment && (
                          <span className="text-xs font-medium px-2 py-0.5 rounded-full bg-green-100 text-green-700">
                            Pago confirmado
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                </Card>
              )
            })}
          </div>
        </div>
      ))}
    </div>
  )
}

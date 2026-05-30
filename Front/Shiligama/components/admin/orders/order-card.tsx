"use client"

import { 
  Clock, 
  Phone, 
  Truck, 
  Store, 
  ChevronRight,
  Eye,
  XCircle,
  ChefHat,
  PackageCheck,
  CheckCircle2
} from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"
import type { Order, OrderStatus } from "@/app/admin/pedidos/page"

interface OrderCardProps {
  order: Order
  onAdvance: () => void
  onCancel: () => void
  onViewDetails: () => void
}

const statusConfig: Record<OrderStatus, { label: string; color: string; bgColor: string }> = {
  pendiente: { label: "Pendiente", color: "text-amber-700", bgColor: "bg-amber-100" },
  preparacion: { label: "En preparación", color: "text-blue-700", bgColor: "bg-blue-100" },
  listo: { label: "Listo para entrega", color: "text-primary", bgColor: "bg-primary/10" },
  entregado: { label: "Entregado", color: "text-emerald-700", bgColor: "bg-emerald-100" },
  cancelado: { label: "Cancelado", color: "text-red-700", bgColor: "bg-red-100" },
}

const paymentMethodLabels: Record<string, string> = {
  yape: "Yape",
  plin: "Plin",
  tarjeta: "Tarjeta",
  efectivo: "Efectivo",
}

const nextActionConfig: Record<OrderStatus, { label: string; icon: React.ElementType } | null> = {
  pendiente: { label: "Iniciar preparación", icon: ChefHat },
  preparacion: { label: "Marcar listo", icon: PackageCheck },
  listo: { label: "Marcar entregado", icon: CheckCircle2 },
  entregado: null,
  cancelado: null,
}

function formatTimeAgo(date: Date): string {
  const now = new Date()
  const diffMs = now.getTime() - date.getTime()
  const diffMins = Math.floor(diffMs / 60000)
  
  if (diffMins < 1) return "Hace un momento"
  if (diffMins < 60) return `Hace ${diffMins} min`
  
  const diffHours = Math.floor(diffMins / 60)
  if (diffHours < 24) return `Hace ${diffHours}h`
  
  return date.toLocaleDateString("es-PE", { day: "numeric", month: "short" })
}

export function OrderCard({ order, onAdvance, onCancel, onViewDetails }: OrderCardProps) {
  const status = statusConfig[order.status]
  const nextAction = nextActionConfig[order.status]
  const itemsSummary = order.items.length === 1 
    ? "1 producto" 
    : `${order.items.length} productos`

  const canCancel = order.status === "pendiente" || order.status === "preparacion"

  return (
    <Card className={cn(
      "overflow-hidden transition-all hover:shadow-md",
      order.status === "pendiente" && "border-l-4 border-l-amber-500",
      order.status === "preparacion" && "border-l-4 border-l-blue-500",
      order.status === "listo" && "border-l-4 border-l-primary",
    )}>
      <CardContent className="p-4">
        {/* Header */}
        <div className="flex items-start justify-between mb-3">
          <div>
            <div className="flex items-center gap-2">
              <span className="font-bold text-foreground">{order.orderNumber}</span>
              <Badge className={cn("text-xs", status.bgColor, status.color)}>
                {status.label}
              </Badge>
            </div>
            <div className="flex items-center gap-1.5 mt-1 text-sm text-muted-foreground">
              <Clock className="h-3.5 w-3.5" />
              <span>{formatTimeAgo(order.orderTime)}</span>
            </div>
          </div>
          <Badge 
            variant="outline" 
            className={cn(
              "flex items-center gap-1",
              order.deliveryMethod === "delivery" 
                ? "border-blue-200 bg-blue-50 text-blue-700" 
                : "border-primary/20 bg-primary/5 text-primary"
            )}
          >
            {order.deliveryMethod === "delivery" ? (
              <>
                <Truck className="h-3 w-3" />
                Delivery
              </>
            ) : (
              <>
                <Store className="h-3 w-3" />
                Recojo
              </>
            )}
          </Badge>
        </div>

        {/* Client Info */}
        <div className="mb-3 p-3 bg-muted/50 rounded-lg">
          <p className="font-medium text-foreground">{order.clientName}</p>
          <div className="flex items-center gap-1.5 text-sm text-muted-foreground mt-1">
            <Phone className="h-3.5 w-3.5" />
            <span>{order.clientPhone}</span>
          </div>
          {order.deliveryMethod === "delivery" && order.address && (
            <p className="text-xs text-muted-foreground mt-1.5 line-clamp-1">
              {order.address}
            </p>
          )}
        </div>

        {/* Order Summary */}
        <div className="flex items-center justify-between mb-3 text-sm">
          <span className="text-muted-foreground">{itemsSummary}</span>
          <span className="font-bold text-lg text-foreground">S/. {order.total.toFixed(2)}</span>
        </div>

        {/* Payment Info */}
        <div className="flex items-center gap-2 mb-4 text-sm">
          <Badge variant="secondary" className="font-normal">
            {paymentMethodLabels[order.paymentMethod]}
          </Badge>
          <Badge 
            variant="outline" 
            className={cn(
              order.paymentStatus === "pagado" 
                ? "border-emerald-200 bg-emerald-50 text-emerald-700" 
                : "border-amber-200 bg-amber-50 text-amber-700"
            )}
          >
            {order.paymentStatus === "pagado" ? "Pagado" : "Pago pendiente"}
          </Badge>
        </div>

        {/* Actions */}
        <div className="flex gap-2">
          <Button 
            variant="outline" 
            size="sm" 
            className="flex-1"
            onClick={onViewDetails}
          >
            <Eye className="h-4 w-4 mr-1.5" />
            Ver detalle
          </Button>
          
          {nextAction && (
            <Button 
              size="sm" 
              className="flex-1"
              onClick={onAdvance}
            >
              <nextAction.icon className="h-4 w-4 mr-1.5" />
              {nextAction.label}
              <ChevronRight className="h-4 w-4 ml-1" />
            </Button>
          )}
          
          {canCancel && (
            <Button 
              variant="ghost" 
              size="sm"
              className="text-destructive hover:text-destructive hover:bg-destructive/10"
              onClick={onCancel}
            >
              <XCircle className="h-4 w-4" />
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  )
}

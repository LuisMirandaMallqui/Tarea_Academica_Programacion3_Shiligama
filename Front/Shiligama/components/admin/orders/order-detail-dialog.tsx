"use client"

import { 
  Clock, 
  Phone, 
  MapPin,
  Truck, 
  Store,
  User,
  CreditCard,
  ChevronRight,
  XCircle,
  ChefHat,
  PackageCheck,
  CheckCircle2,
  Package
} from "lucide-react"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { cn } from "@/lib/utils"
import type { Order, OrderStatus } from "@/app/admin/pedidos/page"

interface OrderDetailDialogProps {
  order: Order | null
  open: boolean
  onOpenChange: (open: boolean) => void
  onAdvance: () => void
  onCancel: () => void
}

const statusConfig: Record<OrderStatus, { label: string; color: string; bgColor: string }> = {
  pendiente: { label: "Pendiente", color: "text-amber-700", bgColor: "bg-amber-100" },
  preparacion: { label: "En preparación", color: "text-blue-700", bgColor: "bg-blue-100" },
  listo: { label: "Listo para entrega", color: "text-primary", bgColor: "bg-primary/10" },
  entregado: { label: "Entregado", color: "text-emerald-700", bgColor: "bg-emerald-100" },
  cancelado: { label: "Cancelado", color: "text-red-700", bgColor: "bg-red-100" },
}

const paymentMethodConfig: Record<string, { label: string; color: string }> = {
  yape: { label: "Yape", color: "text-purple-600" },
  plin: { label: "Plin", color: "text-cyan-600" },
  tarjeta: { label: "Tarjeta", color: "text-blue-600" },
  efectivo: { label: "Efectivo", color: "text-green-600" },
}

const nextActionConfig: Record<OrderStatus, { label: string; icon: React.ElementType } | null> = {
  pendiente: { label: "Iniciar preparación", icon: ChefHat },
  preparacion: { label: "Marcar listo", icon: PackageCheck },
  listo: { label: "Marcar entregado", icon: CheckCircle2 },
  entregado: null,
  cancelado: null,
}

export function OrderDetailDialog({ 
  order, 
  open, 
  onOpenChange, 
  onAdvance, 
  onCancel 
}: OrderDetailDialogProps) {
  if (!order) return null

  const status = statusConfig[order.status]
  const paymentMethod = paymentMethodConfig[order.paymentMethod]
  const nextAction = nextActionConfig[order.status]
  const canCancel = order.status === "pendiente" || order.status === "preparacion"

  const subtotal = order.items.reduce((sum, item) => sum + (item.price * item.quantity), 0)
  const deliveryFee = order.deliveryMethod === "delivery" ? 5.00 : 0

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-lg max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <div className="flex items-center justify-between">
            <DialogTitle className="text-xl">
              Pedido {order.orderNumber}
            </DialogTitle>
            <Badge className={cn("text-xs", status.bgColor, status.color)}>
              {status.label}
            </Badge>
          </div>
        </DialogHeader>

        <div className="space-y-5">
          {/* Order Time */}
          <div className="flex items-center gap-2 text-sm text-muted-foreground">
            <Clock className="h-4 w-4" />
            <span>
              {order.orderTime.toLocaleDateString("es-PE", { 
                weekday: "long",
                day: "numeric", 
                month: "long",
                hour: "2-digit",
                minute: "2-digit"
              })}
            </span>
          </div>

          {/* Client Info */}
          <div className="p-4 bg-muted/50 rounded-lg space-y-3">
            <h4 className="font-medium text-sm text-muted-foreground uppercase tracking-wide">
              Información del Cliente
            </h4>
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <User className="h-4 w-4 text-muted-foreground" />
                <span className="font-medium">{order.clientName}</span>
              </div>
              <div className="flex items-center gap-2">
                <Phone className="h-4 w-4 text-muted-foreground" />
                <span>{order.clientPhone}</span>
              </div>
              {order.deliveryMethod === "delivery" && order.address && (
                <div className="flex items-start gap-2">
                  <MapPin className="h-4 w-4 text-muted-foreground mt-0.5" />
                  <span>{order.address}</span>
                </div>
              )}
            </div>
          </div>

          {/* Delivery Method */}
          <div className="flex items-center gap-3 p-3 border rounded-lg">
            <div className={cn(
              "h-10 w-10 rounded-full flex items-center justify-center",
              order.deliveryMethod === "delivery" ? "bg-blue-100" : "bg-primary/10"
            )}>
              {order.deliveryMethod === "delivery" ? (
                <Truck className="h-5 w-5 text-blue-600" />
              ) : (
                <Store className="h-5 w-5 text-primary" />
              )}
            </div>
            <div>
              <p className="font-medium">
                {order.deliveryMethod === "delivery" ? "Delivery a domicilio" : "Recojo en tienda"}
              </p>
              <p className="text-sm text-muted-foreground">
                {order.deliveryMethod === "delivery" ? "Envío: S/. 5.00" : "Sin costo de envío"}
              </p>
            </div>
          </div>

          {/* Products */}
          <div>
            <h4 className="font-medium text-sm text-muted-foreground uppercase tracking-wide mb-3 flex items-center gap-2">
              <Package className="h-4 w-4" />
              Productos ({order.items.length})
            </h4>
            <div className="space-y-3">
              {order.items.map((item) => (
                <div key={item.id} className="flex items-center justify-between py-2 border-b last:border-0">
                  <div className="flex items-center gap-3">
                    <div className="h-10 w-10 bg-muted rounded-lg flex items-center justify-center text-xs font-medium text-muted-foreground">
                      x{item.quantity}
                    </div>
                    <div>
                      <p className="font-medium text-sm">{item.name}</p>
                      <p className="text-xs text-muted-foreground">
                        S/. {item.price.toFixed(2)} c/u
                      </p>
                    </div>
                  </div>
                  <span className="font-medium">
                    S/. {(item.price * item.quantity).toFixed(2)}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <Separator />

          {/* Order Summary */}
          <div className="space-y-2">
            <div className="flex justify-between text-sm">
              <span className="text-muted-foreground">Subtotal</span>
              <span>S/. {subtotal.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-muted-foreground">Envío</span>
              <span>{deliveryFee > 0 ? `S/. ${deliveryFee.toFixed(2)}` : "Gratis"}</span>
            </div>
            <div className="flex justify-between font-bold text-lg pt-2 border-t">
              <span>Total</span>
              <span className="text-primary">S/. {order.total.toFixed(2)}</span>
            </div>
          </div>

          {/* Payment Info */}
          <div className="flex items-center gap-3 p-3 border rounded-lg">
            <div className="h-10 w-10 rounded-full bg-muted flex items-center justify-center">
              <CreditCard className="h-5 w-5 text-muted-foreground" />
            </div>
            <div className="flex-1">
              <p className={cn("font-medium", paymentMethod.color)}>
                {paymentMethod.label}
              </p>
              <p className="text-sm text-muted-foreground">Método de pago</p>
            </div>
            <Badge 
              variant="outline" 
              className={cn(
                order.paymentStatus === "pagado" 
                  ? "border-emerald-200 bg-emerald-50 text-emerald-700" 
                  : "border-amber-200 bg-amber-50 text-amber-700"
              )}
            >
              {order.paymentStatus === "pagado" ? "Pagado" : "Pendiente"}
            </Badge>
          </div>

          {/* Actions */}
          {(nextAction || canCancel) && (
            <div className="flex gap-3 pt-2">
              {canCancel && (
                <Button 
                  variant="outline" 
                  className="flex-1 text-destructive border-destructive/30 hover:bg-destructive/10"
                  onClick={() => {
                    onCancel()
                    onOpenChange(false)
                  }}
                >
                  <XCircle className="h-4 w-4 mr-2" />
                  Cancelar pedido
                </Button>
              )}
              {nextAction && (
                <Button 
                  className="flex-1"
                  onClick={() => {
                    onAdvance()
                    onOpenChange(false)
                  }}
                >
                  <nextAction.icon className="h-4 w-4 mr-2" />
                  {nextAction.label}
                  <ChevronRight className="h-4 w-4 ml-1" />
                </Button>
              )}
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  )
}

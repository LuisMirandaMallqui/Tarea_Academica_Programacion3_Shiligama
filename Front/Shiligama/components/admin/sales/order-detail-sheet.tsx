"use client"

import { useState } from "react"
import { format } from "date-fns"
import { es } from "date-fns/locale"
import {
  Store,
  Globe,
  MessageCircle,
  Smartphone,
  CreditCard,
  Banknote,
  FileText,
  MapPin,
  Phone,
  Mail,
  Printer,
  StickyNote,
  X,
} from "lucide-react"
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Label } from "@/components/ui/label"
import { cn } from "@/lib/utils"
import type { SaleOrder } from "@/app/admin/ventas/page"

interface OrderDetailSheetProps {
  order: SaleOrder | null
  open: boolean
  onOpenChange: (open: boolean) => void
}

const canalConfig = {
  presencial: { label: "Presencial", icon: Store, color: "text-slate-600" },
  web: { label: "Web", icon: Globe, color: "text-emerald-600" },
  whatsapp: { label: "WhatsApp", icon: MessageCircle, color: "text-green-600" },
}

const metodoPagoConfig = {
  yape: { label: "Yape", icon: Smartphone, color: "text-purple-600" },
  plin: { label: "Plin", icon: Smartphone, color: "text-cyan-600" },
  tarjeta: { label: "Tarjeta", icon: CreditCard, color: "text-blue-600" },
  efectivo: { label: "Efectivo", icon: Banknote, color: "text-green-600" },
}

const estadoConfig = {
  completado: { label: "Completado", className: "bg-emerald-100 text-emerald-700" },
  pendiente: { label: "Pendiente", className: "bg-amber-100 text-amber-700" },
  cancelado: { label: "Cancelado", className: "bg-red-100 text-red-700" },
  reembolsado: { label: "Reembolsado", className: "bg-purple-100 text-purple-700" },
}

export function OrderDetailSheet({
  order,
  open,
  onOpenChange,
}: OrderDetailSheetProps) {
  const [isComprobanteDialogOpen, setIsComprobanteDialogOpen] = useState(false)
  const [comprobanteType, setComprobanteType] = useState<"boleta" | "factura">("boleta")

  if (!order) return null

  const canal = canalConfig[order.canal]
  const metodoPago = metodoPagoConfig[order.metodoPago]
  const estado = estadoConfig[order.estado]
  const CanalIcon = canal.icon
  const PagoIcon = metodoPago.icon

  const subtotal = order.productos.reduce((sum, p) => sum + p.subtotal, 0)

  const handleEmitirComprobante = () => {
    // Simulating comprobante emission
    setIsComprobanteDialogOpen(false)
    alert(`Comprobante tipo "${comprobanteType}" emitido para orden ${order.id}`)
  }

  return (
    <>
      <Sheet open={open} onOpenChange={onOpenChange}>
        <SheetContent className="w-full sm:max-w-lg overflow-y-auto">
          <SheetHeader className="space-y-1">
            <div className="flex items-center justify-between">
              <SheetTitle className="text-xl">{order.id}</SheetTitle>
              <Badge className={cn("font-normal", estado.className)}>
                {estado.label}
              </Badge>
            </div>
            <SheetDescription>
              {format(new Date(order.fecha), "EEEE, dd 'de' MMMM 'de' yyyy, HH:mm", {
                locale: es,
              })}
            </SheetDescription>
          </SheetHeader>

          <div className="mt-6 space-y-6">
            {/* Customer Info */}
            <div className="space-y-3">
              <h3 className="font-semibold text-foreground">Información del Cliente</h3>
              <div className="bg-muted/50 rounded-lg p-4 space-y-2">
                <p className="font-medium">{order.cliente}</p>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <Mail className="h-4 w-4" />
                  {order.clienteEmail}
                </div>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <Phone className="h-4 w-4" />
                  {order.clienteTelefono}
                </div>
                {order.direccionEntrega && (
                  <div className="flex items-start gap-2 text-sm text-muted-foreground">
                    <MapPin className="h-4 w-4 mt-0.5 shrink-0" />
                    {order.direccionEntrega}
                  </div>
                )}
              </div>
            </div>

            {/* Order Info */}
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-muted/50 rounded-lg p-3">
                <p className="text-xs text-muted-foreground mb-1">Canal</p>
                <div className="flex items-center gap-2">
                  <CanalIcon className={cn("h-4 w-4", canal.color)} />
                  <span className="font-medium">{canal.label}</span>
                </div>
              </div>
              <div className="bg-muted/50 rounded-lg p-3">
                <p className="text-xs text-muted-foreground mb-1">Método de Pago</p>
                <div className="flex items-center gap-2">
                  <PagoIcon className={cn("h-4 w-4", metodoPago.color)} />
                  <span className="font-medium">{metodoPago.label}</span>
                </div>
              </div>
            </div>

            {/* Products */}
            <div className="space-y-3">
              <h3 className="font-semibold text-foreground">Productos</h3>
              <div className="space-y-2">
                {order.productos.map((producto, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between py-2 border-b last:border-0"
                  >
                    <div className="flex-1">
                      <p className="font-medium text-sm">{producto.nombre}</p>
                      <p className="text-xs text-muted-foreground">
                        {producto.cantidad} x S/. {producto.precioUnitario.toFixed(2)}
                      </p>
                    </div>
                    <p className="font-medium">S/. {producto.subtotal.toFixed(2)}</p>
                  </div>
                ))}
              </div>
            </div>

            {/* Totals */}
            <div className="bg-primary/5 rounded-lg p-4 space-y-2">
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Subtotal</span>
                <span>S/. {subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Delivery</span>
                <span>S/. 0.00</span>
              </div>
              <Separator />
              <div className="flex justify-between font-bold text-lg">
                <span>Total</span>
                <span className="text-primary">S/. {order.total.toFixed(2)}</span>
              </div>
            </div>

            {/* Comprobante Info */}
            <div className="space-y-3">
              <h3 className="font-semibold text-foreground">Comprobante</h3>
              {order.comprobante === "ninguno" ? (
                <div className="bg-amber-50 border border-amber-200 rounded-lg p-4">
                  <p className="text-sm text-amber-700">
                    No se ha emitido comprobante para esta venta.
                  </p>
                </div>
              ) : (
                <div className="bg-muted/50 rounded-lg p-4 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <FileText className="h-5 w-5 text-primary" />
                    <div>
                      <p className="font-medium capitalize">{order.comprobante}</p>
                      <p className="text-sm text-muted-foreground">
                        {order.comprobanteNumero}
                      </p>
                    </div>
                  </div>
                  <Button variant="outline" size="sm" className="gap-2">
                    <Printer className="h-4 w-4" />
                    Reimprimir
                  </Button>
                </div>
              )}
            </div>

            {/* Notes */}
            {order.notas && (
              <div className="space-y-3">
                <h3 className="font-semibold text-foreground flex items-center gap-2">
                  <StickyNote className="h-4 w-4" />
                  Notas
                </h3>
                <div className="bg-muted/50 rounded-lg p-4">
                  <p className="text-sm text-muted-foreground">{order.notas}</p>
                </div>
              </div>
            )}

            {/* Actions */}
            <div className="flex gap-3 pt-4">
              {order.comprobante === "ninguno" && (
                <Button
                  className="flex-1 gap-2"
                  onClick={() => setIsComprobanteDialogOpen(true)}
                >
                  <FileText className="h-4 w-4" />
                  Emitir Comprobante
                </Button>
              )}
              <Button variant="outline" className="flex-1 gap-2">
                <Printer className="h-4 w-4" />
                Imprimir
              </Button>
            </div>
          </div>
        </SheetContent>
      </Sheet>

      {/* Emitir Comprobante Dialog */}
      <Dialog open={isComprobanteDialogOpen} onOpenChange={setIsComprobanteDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Emitir Comprobante</DialogTitle>
            <DialogDescription>
              Selecciona el tipo de comprobante a emitir para la orden {order?.id}
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label>Tipo de Comprobante</Label>
              <Select
                value={comprobanteType}
                onValueChange={(value: "boleta" | "factura") =>
                  setComprobanteType(value)
                }
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="boleta">
                    <div className="flex items-center gap-2">
                      <FileText className="h-4 w-4" />
                      Boleta de Venta
                    </div>
                  </SelectItem>
                  <SelectItem value="factura">
                    <div className="flex items-center gap-2">
                      <FileText className="h-4 w-4" />
                      Factura
                    </div>
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="bg-muted/50 rounded-lg p-4 space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Cliente:</span>
                <span className="font-medium">{order?.cliente}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Total:</span>
                <span className="font-medium">S/. {order?.total.toFixed(2)}</span>
              </div>
            </div>
          </div>

          <DialogFooter className="gap-2">
            <Button
              variant="outline"
              onClick={() => setIsComprobanteDialogOpen(false)}
            >
              Cancelar
            </Button>
            <Button onClick={handleEmitirComprobante} className="gap-2">
              <FileText className="h-4 w-4" />
              Emitir {comprobanteType === "boleta" ? "Boleta" : "Factura"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  )
}

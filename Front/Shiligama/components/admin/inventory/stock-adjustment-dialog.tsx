"use client"

import { useState, useEffect } from "react"
import { ArrowDown, ArrowUp, Package } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import type { InventoryItem } from "@/app/admin/inventario/page"

interface StockAdjustmentDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  product: InventoryItem | null
  onSubmit: (productId: string, type: "entrada" | "salida", quantity: number) => void
}

export function StockAdjustmentDialog({
  open,
  onOpenChange,
  product,
  onSubmit,
}: StockAdjustmentDialogProps) {
  const [adjustmentType, setAdjustmentType] = useState<"entrada" | "salida">("entrada")
  const [quantity, setQuantity] = useState("")
  const [reason, setReason] = useState("")
  const [error, setError] = useState("")

  // Reset form when dialog opens
  useEffect(() => {
    if (open) {
      setAdjustmentType("entrada")
      setQuantity("")
      setReason("")
      setError("")
    }
  }, [open])

  const handleSubmit = () => {
    setError("")
    const qty = parseInt(quantity, 10)

    if (!quantity || isNaN(qty) || qty <= 0) {
      setError("Ingresa una cantidad válida mayor a 0")
      return
    }

    if (adjustmentType === "salida" && product && qty > product.stockActual) {
      setError(`No puedes retirar más de ${product.stockActual} unidades`)
      return
    }

    if (!reason.trim()) {
      setError("Ingresa un motivo para el ajuste")
      return
    }

    if (product) {
      onSubmit(product.id, adjustmentType, qty)
    }
  }

  if (!product) return null

  const newStock =
    adjustmentType === "entrada"
      ? product.stockActual + (parseInt(quantity, 10) || 0)
      : Math.max(0, product.stockActual - (parseInt(quantity, 10) || 0))

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[480px]">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Package className="h-5 w-5 text-primary" />
            Ajuste de Stock
          </DialogTitle>
          <DialogDescription>
            Registra una entrada o salida de inventario para este producto
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6 py-4">
          {/* Product Info */}
          <div className="flex items-center gap-4 p-4 bg-muted/50 rounded-lg">
            <div className="h-16 w-16 rounded-lg overflow-hidden bg-muted">
              <img
                src={product.imagen}
                alt={product.nombre}
                className="h-full w-full object-cover"
              />
            </div>
            <div className="flex-1">
              <p className="font-semibold">{product.nombre}</p>
              <p className="text-sm text-muted-foreground">{product.codigo}</p>
              <div className="mt-1 flex items-center gap-2">
                <span className="text-sm text-muted-foreground">Stock actual:</span>
                <span className="font-semibold text-primary">
                  {product.stockActual} unidades
                </span>
              </div>
            </div>
          </div>

          {/* Adjustment Type */}
          <div className="space-y-2">
            <Label>Tipo de ajuste</Label>
            <div className="grid grid-cols-2 gap-3">
              <Button
                type="button"
                variant={adjustmentType === "entrada" ? "default" : "outline"}
                className={`h-16 flex flex-col items-center gap-1 ${
                  adjustmentType === "entrada"
                    ? "bg-emerald-600 hover:bg-emerald-700 text-white"
                    : ""
                }`}
                onClick={() => setAdjustmentType("entrada")}
              >
                <ArrowDown className="h-5 w-5" />
                <span>Entrada</span>
              </Button>
              <Button
                type="button"
                variant={adjustmentType === "salida" ? "default" : "outline"}
                className={`h-16 flex flex-col items-center gap-1 ${
                  adjustmentType === "salida"
                    ? "bg-amber-600 hover:bg-amber-700 text-white"
                    : ""
                }`}
                onClick={() => setAdjustmentType("salida")}
              >
                <ArrowUp className="h-5 w-5" />
                <span>Salida</span>
              </Button>
            </div>
          </div>

          {/* Quantity */}
          <div className="space-y-2">
            <Label htmlFor="quantity">Cantidad</Label>
            <Input
              id="quantity"
              type="number"
              min="1"
              placeholder="Ingresa la cantidad"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
            />
            {quantity && !isNaN(parseInt(quantity, 10)) && (
              <p className="text-sm text-muted-foreground">
                Nuevo stock:{" "}
                <span
                  className={`font-semibold ${
                    newStock === 0
                      ? "text-red-600"
                      : newStock < product.stockMinimo
                      ? "text-amber-600"
                      : "text-emerald-600"
                  }`}
                >
                  {newStock} unidades
                </span>
              </p>
            )}
          </div>

          {/* Reason */}
          <div className="space-y-2">
            <Label htmlFor="reason">Motivo</Label>
            <Textarea
              id="reason"
              placeholder={
                adjustmentType === "entrada"
                  ? "Ej: Reposición de proveedor, Devolución de cliente..."
                  : "Ej: Venta directa, Producto dañado, Merma..."
              }
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              rows={3}
            />
          </div>

          {/* Error Message */}
          {error && (
            <p className="text-sm text-red-600 font-medium">{error}</p>
          )}
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Cancelar
          </Button>
          <Button onClick={handleSubmit}>
            Confirmar Ajuste
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

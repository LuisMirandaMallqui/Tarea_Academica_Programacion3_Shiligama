"use client"

import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { AlertTriangle } from "lucide-react"
import type { InventoryItem } from "@/app/admin/inventario/page"

interface LowStockAlertProps {
  products: InventoryItem[]
}

export function LowStockAlert({ products }: LowStockAlertProps) {
  const outOfStock = products.filter((p) => p.estado === "agotado")
  const lowStock = products.filter((p) => p.estado === "bajo")

  return (
    <Alert className="border-amber-200 bg-amber-50">
      <AlertTriangle className="h-5 w-5 text-amber-600" />
      <AlertTitle className="text-amber-800 font-semibold">
        Alerta de Stock
      </AlertTitle>
      <AlertDescription className="text-amber-700">
        <div className="mt-2 space-y-2">
          {outOfStock.length > 0 && (
            <div>
              <span className="font-medium text-red-600">
                Agotados ({outOfStock.length}):
              </span>{" "}
              <span className="text-amber-800">
                {outOfStock.map((p) => p.nombre).join(", ")}
              </span>
            </div>
          )}
          {lowStock.length > 0 && (
            <div>
              <span className="font-medium text-amber-600">
                Stock bajo ({lowStock.length}):
              </span>{" "}
              <span className="text-amber-800">
                {lowStock.map((p) => `${p.nombre} (${p.stockActual}/${p.stockMinimo})`).join(", ")}
              </span>
            </div>
          )}
        </div>
      </AlertDescription>
    </Alert>
  )
}

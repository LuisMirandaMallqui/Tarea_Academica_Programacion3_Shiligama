"use client"

import { Plus, Truck } from "lucide-react"
import { Button } from "@/components/ui/button"

interface SuppliersHeaderProps {
  onAddSupplier: () => void
}

export function SuppliersHeader({ onAddSupplier }: SuppliersHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
      <div className="flex items-center gap-3">
        <div className="h-10 w-10 rounded-lg bg-primary/10 flex items-center justify-center">
          <Truck className="h-5 w-5 text-primary" />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-foreground">Proveedores</h1>
          <p className="text-sm text-muted-foreground">
            Gestiona los proveedores de tu minimarket
          </p>
        </div>
      </div>
      <Button onClick={onAddSupplier} className="gap-2">
        <Plus className="h-4 w-4" />
        Nuevo Proveedor
      </Button>
    </div>
  )
}

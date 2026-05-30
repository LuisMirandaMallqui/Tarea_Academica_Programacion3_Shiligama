"use client"

import { Plus, Package } from "lucide-react"
import { Button } from "@/components/ui/button"

interface ProductsHeaderProps {
  onAddProduct: () => void
}

export function ProductsHeader({ onAddProduct }: ProductsHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
      <div className="flex items-center gap-3">
        <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-primary/10">
          <Package className="h-5 w-5 text-primary" />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-foreground">Productos</h1>
          <p className="text-sm text-muted-foreground">
            Gestiona el catálogo de productos del minimarket
          </p>
        </div>
      </div>
      <Button onClick={onAddProduct} className="gap-2">
        <Plus className="h-4 w-4" />
        Nuevo Producto
      </Button>
    </div>
  )
}

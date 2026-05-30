"use client"

import { Search, RefreshCw } from "lucide-react"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"

interface OrdersHeaderProps {
  searchQuery: string
  onSearchChange: (query: string) => void
}

export function OrdersHeader({ searchQuery, onSearchChange }: OrdersHeaderProps) {
  return (
    <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div>
        <h1 className="text-2xl font-bold text-foreground">Gestión de Pedidos</h1>
        <p className="text-muted-foreground mt-1">
          Procesa y gestiona los pedidos de los clientes
        </p>
      </div>

      <div className="flex items-center gap-3">
        <div className="relative w-full sm:w-72">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Buscar por # orden o cliente..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            className="pl-9"
          />
        </div>
        <Button variant="outline" size="icon" title="Actualizar">
          <RefreshCw className="h-4 w-4" />
        </Button>
      </div>
    </div>
  )
}

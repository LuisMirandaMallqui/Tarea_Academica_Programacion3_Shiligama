"use client"

import { Search } from "lucide-react"
import { Input } from "@/components/ui/input"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Card, CardContent } from "@/components/ui/card"

interface ReturnsFiltersProps {
  searchQuery: string
  onSearchChange: (value: string) => void
  reasonFilter: string
  onReasonChange: (value: string) => void
}

export function ReturnsFilters({
  searchQuery,
  onSearchChange,
  reasonFilter,
  onReasonChange,
}: ReturnsFiltersProps) {
  return (
    <Card className="border-border">
      <CardContent className="p-4">
        <div className="flex flex-col sm:flex-row gap-4">
          {/* Search */}
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Buscar por producto, código o responsable..."
              value={searchQuery}
              onChange={(e) => onSearchChange(e.target.value)}
              className="pl-10"
            />
          </div>

          {/* Reason Filter */}
          <Select value={reasonFilter} onValueChange={onReasonChange}>
            <SelectTrigger className="w-full sm:w-48">
              <SelectValue placeholder="Motivo" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="todos">Todos los motivos</SelectItem>
              <SelectItem value="Vencido">Vencido</SelectItem>
              <SelectItem value="Dañado">Dañado</SelectItem>
              <SelectItem value="Error de pedido">Error de pedido</SelectItem>
              <SelectItem value="Otro">Otro</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </CardContent>
    </Card>
  )
}

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
import { Card } from "@/components/ui/card"

interface ProductsFiltersProps {
  searchQuery: string
  onSearchChange: (value: string) => void
  categoryFilter: string
  onCategoryChange: (value: string) => void
  stockFilter: string
  onStockChange: (value: string) => void
}

const categories = [
  { value: "todas", label: "Todas las categorías" },
  { value: "Abarrotes", label: "Abarrotes" },
  { value: "Bebidas", label: "Bebidas" },
  { value: "Lácteos", label: "Lácteos" },
  { value: "Snacks", label: "Snacks" },
  { value: "Limpieza", label: "Limpieza" },
  { value: "Carnes", label: "Carnes" },
  { value: "Frutas y Verduras", label: "Frutas y Verduras" },
  { value: "Dulces", label: "Dulces" },
  { value: "Congelados", label: "Congelados" },
  { value: "Bebés", label: "Bebés" },
]

const stockStatuses = [
  { value: "todos", label: "Todos los estados" },
  { value: "disponible", label: "En stock" },
  { value: "bajo", label: "Stock bajo" },
  { value: "agotado", label: "Agotado" },
]

export function ProductsFilters({
  searchQuery,
  onSearchChange,
  categoryFilter,
  onCategoryChange,
  stockFilter,
  onStockChange,
}: ProductsFiltersProps) {
  return (
    <Card className="p-4 mb-6">
      <div className="flex flex-col sm:flex-row gap-4">
        {/* Search */}
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Buscar por nombre o código..."
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            className="pl-10"
          />
        </div>

        {/* Category filter */}
        <Select value={categoryFilter} onValueChange={onCategoryChange}>
          <SelectTrigger className="w-full sm:w-[200px]">
            <SelectValue placeholder="Categoría" />
          </SelectTrigger>
          <SelectContent>
            {categories.map((category) => (
              <SelectItem key={category.value} value={category.value}>
                {category.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>

        {/* Stock filter */}
        <Select value={stockFilter} onValueChange={onStockChange}>
          <SelectTrigger className="w-full sm:w-[180px]">
            <SelectValue placeholder="Estado de stock" />
          </SelectTrigger>
          <SelectContent>
            {stockStatuses.map((status) => (
              <SelectItem key={status.value} value={status.value}>
                {status.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>
    </Card>
  )
}

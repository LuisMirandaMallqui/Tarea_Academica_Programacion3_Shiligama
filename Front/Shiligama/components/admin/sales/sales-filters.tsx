"use client"

import { format } from "date-fns"
import { es } from "date-fns/locale"
import { CalendarIcon, Search, X } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { Calendar } from "@/components/ui/calendar"
import { cn } from "@/lib/utils"

interface FiltersState {
  dateFrom: Date | undefined
  dateTo: Date | undefined
  canal: string
  search: string
}

interface SalesFiltersProps {
  filters: FiltersState
  onFiltersChange: (filters: FiltersState) => void
}

export function SalesFilters({ filters, onFiltersChange }: SalesFiltersProps) {
  const updateFilter = <K extends keyof FiltersState>(
    key: K,
    value: FiltersState[K]
  ) => {
    onFiltersChange({ ...filters, [key]: value })
  }

  const clearFilters = () => {
    onFiltersChange({
      dateFrom: undefined,
      dateTo: undefined,
      canal: "todos",
      search: "",
    })
  }

  const hasActiveFilters =
    filters.dateFrom ||
    filters.dateTo ||
    filters.canal !== "todos" ||
    filters.search

  return (
    <Card className="border-0 shadow-sm">
      <CardContent className="p-4">
        <div className="flex flex-col lg:flex-row gap-4">
          {/* Search */}
          <div className="relative flex-1 max-w-sm">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Buscar por # orden o cliente..."
              value={filters.search}
              onChange={(e) => updateFilter("search", e.target.value)}
              className="pl-10"
            />
          </div>

          {/* Date From */}
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                className={cn(
                  "w-full lg:w-[180px] justify-start text-left font-normal",
                  !filters.dateFrom && "text-muted-foreground"
                )}
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {filters.dateFrom
                  ? format(filters.dateFrom, "dd/MM/yyyy", { locale: es })
                  : "Desde"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                mode="single"
                selected={filters.dateFrom}
                onSelect={(date) => updateFilter("dateFrom", date)}
              />
            </PopoverContent>
          </Popover>

          {/* Date To */}
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                className={cn(
                  "w-full lg:w-[180px] justify-start text-left font-normal",
                  !filters.dateTo && "text-muted-foreground"
                )}
              >
                <CalendarIcon className="mr-2 h-4 w-4" />
                {filters.dateTo
                  ? format(filters.dateTo, "dd/MM/yyyy", { locale: es })
                  : "Hasta"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <Calendar
                mode="single"
                selected={filters.dateTo}
                onSelect={(date) => updateFilter("dateTo", date)}
              />
            </PopoverContent>
          </Popover>

          {/* Canal Filter */}
          <Select
            value={filters.canal}
            onValueChange={(value) => updateFilter("canal", value)}
          >
            <SelectTrigger className="w-full lg:w-[180px]">
              <SelectValue placeholder="Canal" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="todos">Todos los canales</SelectItem>
              <SelectItem value="presencial">Presencial</SelectItem>
              <SelectItem value="web">Web</SelectItem>
              <SelectItem value="whatsapp">WhatsApp</SelectItem>
            </SelectContent>
          </Select>

          {/* Clear filters */}
          {hasActiveFilters && (
            <Button variant="ghost" onClick={clearFilters} className="gap-2">
              <X className="h-4 w-4" />
              Limpiar
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  )
}

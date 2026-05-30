"use client"

import { Filter } from "lucide-react"
import { Button } from "@/components/ui/button"
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet"
import { CategorySidebar } from "./category-sidebar"

interface MobileFiltersProps {
  selectedCategory: string | null
  onCategoryChange: (category: string | null) => void
  priceRange: [number, number]
  onPriceRangeChange: (range: [number, number]) => void
}

export function MobileFilters({
  selectedCategory,
  onCategoryChange,
  priceRange,
  onPriceRangeChange,
}: MobileFiltersProps) {
  return (
    <Sheet>
      <SheetTrigger asChild>
        <Button variant="outline" className="fixed bottom-4 left-4 z-40 shadow-lg lg:hidden">
          <Filter className="mr-2 h-4 w-4" />
          Filtros
        </Button>
      </SheetTrigger>
      <SheetContent side="left" className="w-80 overflow-y-auto">
        <SheetHeader>
          <SheetTitle>Filtros</SheetTitle>
        </SheetHeader>
        <div className="py-4">
          <CategorySidebar
            selectedCategory={selectedCategory}
            onCategoryChange={onCategoryChange}
            priceRange={priceRange}
            onPriceRangeChange={onPriceRangeChange}
          />
        </div>
      </SheetContent>
    </Sheet>
  )
}

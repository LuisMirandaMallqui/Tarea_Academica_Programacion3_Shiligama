"use client"

import { 
  Package, 
  Wine, 
  Milk, 
  Cookie, 
  SprayCan, 
  Beef, 
  Apple, 
  Candy,
  Snowflake,
  Baby
} from "lucide-react"
import { cn } from "@/lib/utils"
import { Slider } from "@/components/ui/slider"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

const categories = [
  { id: "abarrotes", name: "Abarrotes", icon: Package },
  { id: "bebidas", name: "Bebidas", icon: Wine },
  { id: "lacteos", name: "Lacteos", icon: Milk },
  { id: "snacks", name: "Snacks", icon: Cookie },
  { id: "limpieza", name: "Limpieza", icon: SprayCan },
  { id: "carnes", name: "Carnes y Embutidos", icon: Beef },
  { id: "frutas", name: "Frutas y Verduras", icon: Apple },
  { id: "dulces", name: "Dulces y Golosinas", icon: Candy },
  { id: "congelados", name: "Congelados", icon: Snowflake },
  { id: "bebes", name: "Bebes", icon: Baby },
]

interface CategorySidebarProps {
  selectedCategory: string | null
  onCategoryChange: (category: string | null) => void
  priceRange: [number, number]
  onPriceRangeChange: (range: [number, number]) => void
}

export function CategorySidebar({
  selectedCategory,
  onCategoryChange,
  priceRange,
  onPriceRangeChange,
}: CategorySidebarProps) {
  const handleMinChange = (value: string) => {
    const numValue = Math.max(0, Math.min(Number(value) || 0, priceRange[1]))
    onPriceRangeChange([numValue, priceRange[1]])
  }

  const handleMaxChange = (value: string) => {
    const numValue = Math.max(priceRange[0], Math.min(Number(value) || 100, 100))
    onPriceRangeChange([priceRange[0], numValue])
  }

  return (
    <div className="space-y-6">
      {/* Categories */}
      <div className="rounded-xl bg-card p-4 shadow-sm border">
        <h3 className="mb-4 text-sm font-semibold text-foreground uppercase tracking-wide">
          Categorias
        </h3>
        <nav className="space-y-1">
          <button
            onClick={() => onCategoryChange(null)}
            className={cn(
              "flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
              selectedCategory === null
                ? "bg-primary text-primary-foreground"
                : "text-foreground hover:bg-secondary"
            )}
          >
            <Package className="h-4 w-4" />
            Todos los productos
          </button>
          {categories.map((category) => {
            const Icon = category.icon
            return (
              <button
                key={category.id}
                onClick={() => onCategoryChange(category.id)}
                className={cn(
                  "flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
                  selectedCategory === category.id
                    ? "bg-primary text-primary-foreground"
                    : "text-foreground hover:bg-secondary"
                )}
              >
                <Icon className="h-4 w-4" />
                {category.name}
              </button>
            )
          })}
        </nav>
      </div>

      {/* Price Range */}
      <div className="rounded-xl bg-card p-4 shadow-sm border">
        <h3 className="mb-4 text-sm font-semibold text-foreground uppercase tracking-wide">
          Rango de Precio
        </h3>
        <div className="space-y-4">
          <Slider
            value={priceRange}
            onValueChange={(value) => onPriceRangeChange(value as [number, number])}
            min={0}
            max={100}
            step={1}
            className="w-full"
          />
          <div className="flex items-center gap-2">
            <div className="flex-1">
              <label className="text-xs text-muted-foreground mb-1 block">Min</label>
              <div className="relative">
                <span className="absolute left-2 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">S/.</span>
                <Input
                  type="number"
                  min={0}
                  max={priceRange[1]}
                  value={priceRange[0]}
                  onChange={(e) => handleMinChange(e.target.value)}
                  className="pl-8 h-9 text-sm"
                />
              </div>
            </div>
            <span className="text-muted-foreground mt-5">-</span>
            <div className="flex-1">
              <label className="text-xs text-muted-foreground mb-1 block">Max</label>
              <div className="relative">
                <span className="absolute left-2 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">S/.</span>
                <Input
                  type="number"
                  min={priceRange[0]}
                  max={100}
                  value={priceRange[1]}
                  onChange={(e) => handleMaxChange(e.target.value)}
                  className="pl-8 h-9 text-sm"
                />
              </div>
            </div>
          </div>
          <Button 
            variant="outline" 
            size="sm" 
            className="w-full"
            onClick={() => onPriceRangeChange([0, 100])}
          >
            Limpiar filtros
          </Button>
        </div>
      </div>
    </div>
  )
}

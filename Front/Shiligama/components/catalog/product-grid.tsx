"use client"

import { useMemo } from "react"
import Link from "next/link"
import { ShoppingCart, Plus } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils"
import { products } from "@/lib/products"

interface ProductGridProps {
  selectedCategory: string | null
  priceRange: [number, number]
  searchQuery: string
  onAddToCart: (item: { id: number; name: string; price: number; image: string }) => void
}

export function ProductGrid({
  selectedCategory,
  priceRange,
  searchQuery,
  onAddToCart,
}: ProductGridProps) {
  const filteredProducts = useMemo(() => {
    return products.filter((product) => {
      const matchesCategory = !selectedCategory || product.category === selectedCategory
      const matchesPrice = product.price >= priceRange[0] && product.price <= priceRange[1]
      const matchesSearch = !searchQuery || 
        product.name.toLowerCase().includes(searchQuery.toLowerCase())
      return matchesCategory && matchesPrice && matchesSearch
    })
  }, [selectedCategory, priceRange, searchQuery])

  const getStockBadge = (stock: number) => {
    if (stock === 0) {
      return { label: "Agotado", variant: "destructive" as const }
    }
    if (stock <= 10) {
      return { label: `Quedan ${stock}`, variant: "secondary" as const }
    }
    return { label: "En stock", variant: "default" as const }
  }

  return (
    <div>
      {/* Header */}
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-foreground">
            {selectedCategory
              ? products.find((p) => p.category === selectedCategory)?.category
                  .charAt(0)
                  .toUpperCase() +
                (products.find((p) => p.category === selectedCategory)?.category.slice(1) || "")
              : "Todos los productos"}
          </h2>
          <p className="text-sm text-muted-foreground">
            {filteredProducts.length} productos encontrados
          </p>
        </div>
      </div>

      {/* Grid */}
      {filteredProducts.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-16 text-center">
          <ShoppingCart className="h-16 w-16 text-muted-foreground/50 mb-4" />
          <h3 className="text-lg font-medium text-foreground">No hay productos</h3>
          <p className="text-sm text-muted-foreground mt-1">
            Intenta ajustar los filtros para ver mas productos
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-2 gap-4 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4">
          {filteredProducts.map((product) => {
            const stockInfo = getStockBadge(product.stock)
            const isOutOfStock = product.stock === 0

            return (
              <div
                key={product.id}
                className={cn(
                  "group relative flex flex-col overflow-hidden rounded-xl bg-card border shadow-sm transition-all duration-300 hover:shadow-md",
                  isOutOfStock && "opacity-75"
                )}
              >
                {/* Image - Clickable */}
                <Link href={`/catalogo/${product.id}`}>
                  <div className="relative aspect-square overflow-hidden bg-secondary/30 cursor-pointer">
                    <img
                      src={product.image}
                      alt={product.name}
                      className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
                    />
                    {isOutOfStock && (
                      <div className="absolute inset-0 flex items-center justify-center bg-background/60 backdrop-blur-sm">
                        <span className="font-semibold text-foreground">Agotado</span>
                      </div>
                    )}
                  </div>
                </Link>

                {/* Promo Badge - Below image on mobile to prevent overlap */}
                {product.isPromo && (
                  <div className="sm:absolute sm:top-2 sm:left-2 sm:z-10 px-3 py-1 sm:p-0">
                    <Badge className="bg-destructive text-white font-semibold text-xs">
                      Oferta
                    </Badge>
                  </div>
                )}

                {/* Content */}
                <div className="flex flex-1 flex-col p-3 sm:p-4">
                  {/* Stock Badge */}
                  <Badge
                    variant={stockInfo.variant}
                    className={cn(
                      "mb-2 w-fit text-xs",
                      stockInfo.variant === "default" && "bg-primary/10 text-primary border-0"
                    )}
                  >
                    {stockInfo.label}
                  </Badge>

                  {/* Name - Clickable */}
                  <Link href={`/catalogo/${product.id}`}>
                    <h3 className="mb-2 text-sm font-medium text-foreground line-clamp-2 leading-snug hover:text-primary cursor-pointer transition-colors">
                      {product.name}
                    </h3>
                  </Link>

                  {/* Price */}
                  <div className={cn(
                    "mt-auto",
                    product.originalPrice 
                      ? "flex flex-col sm:flex-row sm:items-baseline sm:gap-2" 
                      : "flex items-baseline gap-2"
                  )}>
                    <span className="text-lg font-bold text-primary">
                      S/. {product.price.toFixed(2)}
                    </span>
                    {product.originalPrice && (
                      <span className="text-xs sm:text-sm text-muted-foreground line-through">
                        S/. {product.originalPrice.toFixed(2)}
                      </span>
                    )}
                  </div>

                  {/* Add to Cart */}
                  <Button
                    className="mt-3 w-full gap-2"
                    size="sm"
                    disabled={isOutOfStock}
                    onClick={(e) => {
                      e.preventDefault()
                      onAddToCart({
                        id: product.id,
                        name: product.name,
                        price: product.price,
                        image: product.image,
                      })
                    }}
                  >
                    <Plus className="h-4 w-4" />
                    <span className="hidden sm:inline">Agregar</span>
                    <span className="sm:hidden">Agregar</span>
                  </Button>
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}

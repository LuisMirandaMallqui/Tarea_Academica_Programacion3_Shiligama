"use client"

import { useState } from "react"
import Link from "next/link"
import { Tag, ShoppingCart, Plus, Percent } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils"
import { getDiscountedProducts } from "@/lib/products"
import { CatalogNavbar } from "@/components/catalog/catalog-navbar"

export default function OfertasPage() {
  const [cartCount, setCartCount] = useState(3)
  const [searchQuery, setSearchQuery] = useState("")
  
  const discountedProducts = getDiscountedProducts()

  const handleAddToCart = () => {
    setCartCount((prev) => prev + 1)
  }

  const getStockBadge = (stock: number) => {
    if (stock === 0) {
      return { label: "Agotado", variant: "destructive" as const }
    }
    if (stock <= 10) {
      return { label: `Quedan ${stock}`, variant: "secondary" as const }
    }
    return { label: "En stock", variant: "default" as const }
  }

  // Filter by search query
  const filteredProducts = discountedProducts.filter(product => 
    !searchQuery || product.name.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="min-h-screen bg-background">
      <CatalogNavbar cartCount={cartCount} searchQuery={searchQuery} onSearchChange={setSearchQuery} />
      
      <main className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-6">
        {/* Hero Banner */}
        <div className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-primary to-accent px-6 py-10 sm:px-10 sm:py-14 mb-8">
          <div className="relative z-10 flex flex-col sm:flex-row items-center justify-between gap-6">
            <div className="flex items-center gap-4 sm:gap-6">
              <div className="flex h-16 w-16 sm:h-20 sm:w-20 items-center justify-center rounded-full bg-white/20 backdrop-blur-sm">
                <Percent className="h-8 w-8 sm:h-10 sm:w-10 text-white" />
              </div>
              <div className="text-center sm:text-left">
                <h1 className="text-2xl sm:text-3xl lg:text-4xl font-bold text-white">
                  Ofertas Especiales
                </h1>
                <p className="text-sm sm:text-base text-white/90 mt-1">
                  Aprovecha los mejores descuentos en productos seleccionados
                </p>
              </div>
            </div>
            <div className="flex items-center gap-2 bg-white/20 backdrop-blur-sm rounded-full px-4 py-2">
              <Tag className="h-5 w-5 text-white" />
              <span className="text-white font-semibold">
                {discountedProducts.length} productos en oferta
              </span>
            </div>
          </div>
          {/* Decorative elements */}
          <div className="absolute top-0 right-0 -translate-y-1/4 translate-x-1/4 w-64 h-64 rounded-full bg-white/10 blur-3xl" />
          <div className="absolute bottom-0 left-0 translate-y-1/4 -translate-x-1/4 w-64 h-64 rounded-full bg-white/10 blur-3xl" />
        </div>

        {/* Products Grid */}
        <div>
          <div className="mb-6 flex items-center justify-between">
            <div>
              <h2 className="text-2xl font-bold text-foreground">
                Productos con descuento
              </h2>
              <p className="text-sm text-muted-foreground">
                {filteredProducts.length} productos encontrados
              </p>
            </div>
          </div>

          {filteredProducts.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-16 text-center">
              <ShoppingCart className="h-16 w-16 text-muted-foreground/50 mb-4" />
              <h3 className="text-lg font-medium text-foreground">No hay ofertas disponibles</h3>
              <p className="text-sm text-muted-foreground mt-1">
                Vuelve pronto para ver nuevas ofertas
              </p>
              <Button asChild className="mt-4">
                <Link href="/catalogo">Ver catálogo completo</Link>
              </Button>
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-4 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4">
              {filteredProducts.map((product) => {
                const stockInfo = getStockBadge(product.stock)
                const isOutOfStock = product.stock === 0
                const discountPercent = product.originalPrice 
                  ? Math.round((1 - product.price / product.originalPrice) * 100)
                  : 0

                return (
                  <div
                    key={product.id}
                    className={cn(
                      "group relative flex flex-col overflow-hidden rounded-xl bg-card border shadow-sm transition-all duration-300 hover:shadow-md",
                      isOutOfStock && "opacity-75"
                    )}
                  >
                    {/* Discount Badge */}
                    <div className="absolute top-2 left-2 z-10 flex flex-col gap-1">
                      <Badge className="bg-destructive text-white font-semibold">
                        -{discountPercent}%
                      </Badge>
                    </div>

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
                      <div className="mt-auto flex items-baseline gap-2">
                        <span className="text-lg font-bold text-primary">
                          S/. {product.price.toFixed(2)}
                        </span>
                        {product.originalPrice && (
                          <span className="text-sm text-muted-foreground line-through">
                            S/. {product.originalPrice.toFixed(2)}
                          </span>
                        )}
                      </div>

                      {/* Savings */}
                      {product.originalPrice && (
                        <p className="text-xs text-green-600 mt-1">
                          Ahorras S/. {(product.originalPrice - product.price).toFixed(2)}
                        </p>
                      )}

                      {/* Add to Cart */}
                      <Button
                        className="mt-3 w-full gap-2"
                        size="sm"
                        disabled={isOutOfStock}
                        onClick={(e) => {
                          e.preventDefault()
                          handleAddToCart()
                        }}
                      >
                        <Plus className="h-4 w-4" />
                        <span>Agregar</span>
                      </Button>
                    </div>
                  </div>
                )
              })}
            </div>
          )}
        </div>
      </main>
    </div>
  )
}

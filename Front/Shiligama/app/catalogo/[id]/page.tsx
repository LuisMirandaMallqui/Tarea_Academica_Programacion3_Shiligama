"use client"

import { use, useState } from "react"
import Link from "next/link"
import { ArrowLeft, ShoppingCart, Plus, Minus, Package } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { cn } from "@/lib/utils"
import { getProductById, getRelatedProducts, getCategoryName, products } from "@/lib/products"
import { CatalogNavbar } from "@/components/catalog/catalog-navbar"

export default function ProductDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params)
  const productId = parseInt(id)
  const product = getProductById(productId)
  
  const [quantity, setQuantity] = useState(1)
  const [cartCount, setCartCount] = useState(3)
  const [searchQuery, setSearchQuery] = useState("")

  if (!product) {
    return (
      <div className="min-h-screen bg-background">
        <CatalogNavbar cartCount={cartCount} searchQuery={searchQuery} onSearchChange={setSearchQuery} />
        <main className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-12">
          <div className="flex flex-col items-center justify-center py-16 text-center">
            <Package className="h-16 w-16 text-muted-foreground/50 mb-4" />
            <h3 className="text-lg font-medium text-foreground">Producto no encontrado</h3>
            <p className="text-sm text-muted-foreground mt-1">
              El producto que buscas no existe o ha sido eliminado.
            </p>
            <Button asChild className="mt-4">
              <Link href="/catalogo">Volver al catálogo</Link>
            </Button>
          </div>
        </main>
      </div>
    )
  }

  const relatedProducts = getRelatedProducts(product, 4)
  const isOutOfStock = product.stock === 0

  const getStockBadge = (stock: number) => {
    if (stock === 0) {
      return { label: "Agotado", variant: "destructive" as const }
    }
    if (stock <= 10) {
      return { label: `Quedan ${stock} unidades`, variant: "secondary" as const }
    }
    return { label: "En stock", variant: "default" as const }
  }

  const stockInfo = getStockBadge(product.stock)

  const handleAddToCart = () => {
    setCartCount((prev) => prev + quantity)
  }

  const incrementQuantity = () => {
    if (quantity < product.stock) {
      setQuantity((prev) => prev + 1)
    }
  }

  const decrementQuantity = () => {
    if (quantity > 1) {
      setQuantity((prev) => prev - 1)
    }
  }

  return (
    <div className="min-h-screen bg-background">
      <CatalogNavbar cartCount={cartCount} searchQuery={searchQuery} onSearchChange={setSearchQuery} />
      
      <main className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-6">
        {/* Breadcrumb */}
        <nav className="mb-6">
          <Link 
            href="/catalogo" 
            className="flex items-center gap-2 text-sm text-muted-foreground hover:text-primary transition-colors"
          >
            <ArrowLeft className="h-4 w-4" />
            Volver al catálogo
          </Link>
        </nav>

        {/* Product Detail */}
        <div className="grid lg:grid-cols-2 gap-8 lg:gap-12">
          {/* Product Image */}
          <div className="relative aspect-square overflow-hidden rounded-2xl bg-secondary/30">
            {product.isPromo && (
              <div className="absolute top-4 left-4 z-10">
                <Badge className="bg-destructive text-white font-semibold text-sm px-3 py-1">
                  Oferta
                </Badge>
              </div>
            )}
            <img
              src={product.image}
              alt={product.name}
              className="h-full w-full object-cover"
            />
            {isOutOfStock && (
              <div className="absolute inset-0 flex items-center justify-center bg-background/60 backdrop-blur-sm">
                <span className="text-xl font-semibold text-foreground">Agotado</span>
              </div>
            )}
          </div>

          {/* Product Info */}
          <div className="flex flex-col">
            {/* Category */}
            <p className="text-sm text-muted-foreground uppercase tracking-wide mb-2">
              {getCategoryName(product.category)}
            </p>

            {/* Name */}
            <h1 className="text-3xl font-bold text-foreground mb-4">
              {product.name}
            </h1>

            {/* Stock Badge */}
            <Badge
              variant={stockInfo.variant}
              className={cn(
                "mb-4 w-fit",
                stockInfo.variant === "default" && "bg-primary/10 text-primary border-0"
              )}
            >
              {stockInfo.label}
            </Badge>

            {/* Price */}
            <div className="flex items-baseline gap-3 mb-6">
              <span className="text-4xl font-bold text-primary">
                S/. {product.price.toFixed(2)}
              </span>
              {product.originalPrice && (
                <span className="text-xl text-muted-foreground line-through">
                  S/. {product.originalPrice.toFixed(2)}
                </span>
              )}
              {product.originalPrice && (
                <Badge variant="destructive" className="ml-2">
                  -{Math.round((1 - product.price / product.originalPrice) * 100)}%
                </Badge>
              )}
            </div>

            {/* Description */}
            <div className="mb-6">
              <h3 className="text-sm font-semibold text-foreground uppercase tracking-wide mb-2">
                Descripción
              </h3>
              <p className="text-muted-foreground leading-relaxed">
                {product.description}
              </p>
            </div>

            <Separator className="my-6" />

            {/* Quantity Selector */}
            <div className="mb-6">
              <h3 className="text-sm font-semibold text-foreground uppercase tracking-wide mb-3">
                Cantidad
              </h3>
              <div className="flex items-center gap-4">
                <div className="flex items-center border rounded-lg">
                  <Button
                    variant="ghost"
                    size="icon"
                    onClick={decrementQuantity}
                    disabled={quantity <= 1 || isOutOfStock}
                    className="h-10 w-10 rounded-r-none"
                  >
                    <Minus className="h-4 w-4" />
                  </Button>
                  <span className="w-12 text-center font-medium">{quantity}</span>
                  <Button
                    variant="ghost"
                    size="icon"
                    onClick={incrementQuantity}
                    disabled={quantity >= product.stock || isOutOfStock}
                    className="h-10 w-10 rounded-l-none"
                  >
                    <Plus className="h-4 w-4" />
                  </Button>
                </div>
                <span className="text-sm text-muted-foreground">
                  {product.stock} disponibles
                </span>
              </div>
            </div>

            {/* Add to Cart Button */}
            <Button
              size="lg"
              className="w-full sm:w-auto gap-2"
              disabled={isOutOfStock}
              onClick={handleAddToCart}
            >
              <ShoppingCart className="h-5 w-5" />
              Agregar al carrito
            </Button>

            {/* Total */}
            {!isOutOfStock && (
              <p className="mt-4 text-sm text-muted-foreground">
                Total: <span className="font-semibold text-foreground">S/. {(product.price * quantity).toFixed(2)}</span>
              </p>
            )}
          </div>
        </div>

        {/* Related Products */}
        {relatedProducts.length > 0 && (
          <section className="mt-16">
            <h2 className="text-2xl font-bold text-foreground mb-6">
              Productos relacionados
            </h2>
            <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
              {relatedProducts.map((relatedProduct) => {
                const relatedStockInfo = getStockBadge(relatedProduct.stock)
                const relatedIsOutOfStock = relatedProduct.stock === 0

                return (
                  <Link
                    key={relatedProduct.id}
                    href={`/catalogo/${relatedProduct.id}`}
                    className={cn(
                      "group relative flex flex-col overflow-hidden rounded-xl bg-card border shadow-sm transition-all duration-300 hover:shadow-md",
                      relatedIsOutOfStock && "opacity-75"
                    )}
                  >
                    {/* Promo Badge */}
                    {relatedProduct.isPromo && (
                      <div className="absolute top-2 left-2 z-10">
                        <Badge className="bg-destructive text-white font-semibold">
                          Oferta
                        </Badge>
                      </div>
                    )}

                    {/* Image */}
                    <div className="relative aspect-square overflow-hidden bg-secondary/30">
                      <img
                        src={relatedProduct.image}
                        alt={relatedProduct.name}
                        className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
                      />
                      {relatedIsOutOfStock && (
                        <div className="absolute inset-0 flex items-center justify-center bg-background/60 backdrop-blur-sm">
                          <span className="font-semibold text-foreground">Agotado</span>
                        </div>
                      )}
                    </div>

                    {/* Content */}
                    <div className="flex flex-1 flex-col p-3 sm:p-4">
                      <Badge
                        variant={relatedStockInfo.variant}
                        className={cn(
                          "mb-2 w-fit text-xs",
                          relatedStockInfo.variant === "default" && "bg-primary/10 text-primary border-0"
                        )}
                      >
                        {relatedStockInfo.label}
                      </Badge>

                      <h3 className="mb-2 text-sm font-medium text-foreground line-clamp-2 leading-snug group-hover:text-primary transition-colors">
                        {relatedProduct.name}
                      </h3>

                      <div className="mt-auto flex items-baseline gap-2">
                        <span className="text-lg font-bold text-primary">
                          S/. {relatedProduct.price.toFixed(2)}
                        </span>
                        {relatedProduct.originalPrice && (
                          <span className="text-sm text-muted-foreground line-through">
                            S/. {relatedProduct.originalPrice.toFixed(2)}
                          </span>
                        )}
                      </div>
                    </div>
                  </Link>
                )
              })}
            </div>
          </section>
        )}
      </main>
    </div>
  )
}

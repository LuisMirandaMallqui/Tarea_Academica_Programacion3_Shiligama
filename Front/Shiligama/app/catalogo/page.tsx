"use client"

import { useState } from "react"
import { useCart } from "@/hooks/use-cart"
import { CatalogNavbar } from "@/components/catalog/catalog-navbar"
import { CategorySidebar } from "@/components/catalog/category-sidebar"
import { ProductGrid } from "@/components/catalog/product-grid"
import { PromotionsBanner } from "@/components/catalog/promotions-banner"
import { MobileFilters } from "@/components/catalog/mobile-filters"

export default function CatalogoPage() {
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null)
  const [priceRange, setPriceRange] = useState<[number, number]>([0, 100])
  const [searchQuery, setSearchQuery] = useState("")
  const { cartCount, addToCart } = useCart()

  return (
    <div className="min-h-screen bg-background">
      <CatalogNavbar 
        cartCount={cartCount} 
        searchQuery={searchQuery}
        onSearchChange={setSearchQuery}
      />
      
      <main className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <PromotionsBanner />
        
        <div className="flex gap-8 py-6">
          {/* Desktop Sidebar */}
          <aside className="hidden lg:block w-64 shrink-0">
            <CategorySidebar
              selectedCategory={selectedCategory}
              onCategoryChange={setSelectedCategory}
              priceRange={priceRange}
              onPriceRangeChange={setPriceRange}
            />
          </aside>
          
          {/* Mobile Filters */}
          <div className="lg:hidden">
            <MobileFilters
              selectedCategory={selectedCategory}
              onCategoryChange={setSelectedCategory}
              priceRange={priceRange}
              onPriceRangeChange={setPriceRange}
            />
          </div>
          
          {/* Product Grid */}
          <div className="flex-1">
            <ProductGrid
              selectedCategory={selectedCategory}
              priceRange={priceRange}
              searchQuery={searchQuery}
              onAddToCart={addToCart}
            />
          </div>
        </div>
      </main>
    </div>
  )
}

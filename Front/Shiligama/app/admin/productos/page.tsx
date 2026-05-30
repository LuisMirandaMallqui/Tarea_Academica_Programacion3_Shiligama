"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { ProductsHeader } from "@/components/admin/products/products-header"
import { ProductsFilters } from "@/components/admin/products/products-filters"
import { ProductsTable } from "@/components/admin/products/products-table"
import { ProductSheet } from "@/components/admin/products/product-sheet"

export type Product = {
  id: string
  codigo: string
  nombre: string
  categoria: string
  precio: number
  stock: number
  stockMinimo: number
  descripcion: string
  imagen: string
  activo: boolean
}

const initialProducts: Product[] = [
  {
    id: "1",
    codigo: "ABR001",
    nombre: "Arroz Costeño 5kg",
    categoria: "Abarrotes",
    precio: 24.90,
    stock: 45,
    stockMinimo: 10,
    descripcion: "Arroz extra graneado de primera calidad",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
  {
    id: "2",
    codigo: "BEB001",
    nombre: "Inca Kola 1.5L",
    categoria: "Bebidas",
    precio: 8.50,
    stock: 120,
    stockMinimo: 20,
    descripcion: "Gaseosa Inca Kola botella 1.5 litros",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
  {
    id: "3",
    codigo: "LAC001",
    nombre: "Leche Gloria Entera 1L",
    categoria: "Lácteos",
    precio: 5.20,
    stock: 8,
    stockMinimo: 15,
    descripcion: "Leche evaporada Gloria lata 1 litro",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
  {
    id: "4",
    codigo: "SNK001",
    nombre: "Papitas Lays Clásicas 200g",
    categoria: "Snacks",
    precio: 12.90,
    stock: 35,
    stockMinimo: 10,
    descripcion: "Papas fritas Lays sabor clásico",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
  {
    id: "5",
    codigo: "LIM001",
    nombre: "Detergente Ace 2kg",
    categoria: "Limpieza",
    precio: 28.50,
    stock: 0,
    stockMinimo: 5,
    descripcion: "Detergente en polvo Ace",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: false,
  },
  {
    id: "6",
    codigo: "ABR002",
    nombre: "Aceite Primor 1L",
    categoria: "Abarrotes",
    precio: 14.90,
    stock: 62,
    stockMinimo: 15,
    descripcion: "Aceite vegetal Primor botella 1 litro",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
  {
    id: "7",
    codigo: "BEB002",
    nombre: "Agua San Luis 2.5L",
    categoria: "Bebidas",
    precio: 4.50,
    stock: 3,
    stockMinimo: 25,
    descripcion: "Agua mineral sin gas San Luis",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
  {
    id: "8",
    codigo: "LAC002",
    nombre: "Yogurt Gloria Fresa 1L",
    categoria: "Lácteos",
    precio: 7.80,
    stock: 28,
    stockMinimo: 10,
    descripcion: "Yogurt Gloria sabor fresa",
    imagen: "/placeholder.svg?height=80&width=80",
    activo: true,
  },
]

export default function ProductosPage() {
  const [products, setProducts] = useState<Product[]>(initialProducts)
  const [isSheetOpen, setIsSheetOpen] = useState(false)
  const [editingProduct, setEditingProduct] = useState<Product | null>(null)
  const [searchQuery, setSearchQuery] = useState("")
  const [categoryFilter, setCategoryFilter] = useState("todas")
  const [stockFilter, setStockFilter] = useState("todos")

  const filteredProducts = products.filter((product) => {
    const matchesSearch =
      product.nombre.toLowerCase().includes(searchQuery.toLowerCase()) ||
      product.codigo.toLowerCase().includes(searchQuery.toLowerCase())
    
    const matchesCategory =
      categoryFilter === "todas" || product.categoria === categoryFilter

    const matchesStock =
      stockFilter === "todos" ||
      (stockFilter === "disponible" && product.stock > product.stockMinimo) ||
      (stockFilter === "bajo" && product.stock > 0 && product.stock <= product.stockMinimo) ||
      (stockFilter === "agotado" && product.stock === 0)

    return matchesSearch && matchesCategory && matchesStock
  })

  const handleAddProduct = () => {
    setEditingProduct(null)
    setIsSheetOpen(true)
  }

  const handleEditProduct = (product: Product) => {
    setEditingProduct(product)
    setIsSheetOpen(true)
  }

  const handleDeleteProduct = (productId: string) => {
    setProducts(products.filter((p) => p.id !== productId))
  }

  const handleSaveProduct = (product: Product) => {
    if (editingProduct) {
      setProducts(products.map((p) => (p.id === product.id ? product : p)))
    } else {
      const newProduct = {
        ...product,
        id: String(Date.now()),
        codigo: `PRD${String(products.length + 1).padStart(3, "0")}`,
      }
      setProducts([...products, newProduct])
    }
    setIsSheetOpen(false)
    setEditingProduct(null)
  }

  return (
    <div className="min-h-screen bg-muted/30">
      <AdminSidebar />
      <div className="lg:pl-64">
        <AdminTopbar />
        <main className="p-6 pt-20 lg:pt-6">
          <ProductsHeader onAddProduct={handleAddProduct} />
          <ProductsFilters
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            categoryFilter={categoryFilter}
            onCategoryChange={setCategoryFilter}
            stockFilter={stockFilter}
            onStockChange={setStockFilter}
          />
          <ProductsTable
            products={filteredProducts}
            onEdit={handleEditProduct}
            onDelete={handleDeleteProduct}
          />
          <ProductSheet
            open={isSheetOpen}
            onOpenChange={setIsSheetOpen}
            product={editingProduct}
            onSave={handleSaveProduct}
          />
        </main>
      </div>
    </div>
  )
}

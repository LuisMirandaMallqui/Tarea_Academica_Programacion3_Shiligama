"use client"

import { useState } from "react"
import { TrabajadorSidebar } from "@/components/trabajador/trabajador-sidebar"
import { TrabajadorTopbar } from "@/components/trabajador/trabajador-topbar"
import { ProductsFilters } from "@/components/admin/products/products-filters"
import { ProductsTable } from "@/components/admin/products/products-table"

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

export default function TrabajadorProductosPage() {
  const [products] = useState<Product[]>(initialProducts)
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

  // Calculate stock stats
  const stockStats = {
    total: products.length,
    disponible: products.filter(p => p.stock > p.stockMinimo).length,
    bajo: products.filter(p => p.stock > 0 && p.stock <= p.stockMinimo).length,
    agotado: products.filter(p => p.stock === 0).length,
  }

  return (
    <div className="min-h-screen bg-muted/30">
      <TrabajadorSidebar />
      <div className="lg:pl-64">
        <TrabajadorTopbar title="Productos" />
        <main className="p-6 pt-20 lg:pt-6">
          {/* Header */}
          <div className="mb-6">
            <h1 className="text-2xl font-bold text-foreground">Catálogo de Productos</h1>
            <p className="text-muted-foreground">Consulta el inventario y disponibilidad de productos</p>
          </div>

          {/* Stock Stats */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <div className="bg-white border rounded-lg p-4">
              <p className="text-sm text-muted-foreground">Total Productos</p>
              <p className="text-2xl font-bold text-foreground">{stockStats.total}</p>
            </div>
            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <p className="text-sm text-green-700">Stock Disponible</p>
              <p className="text-2xl font-bold text-green-800">{stockStats.disponible}</p>
            </div>
            <div className="bg-amber-50 border border-amber-200 rounded-lg p-4">
              <p className="text-sm text-amber-700">Stock Bajo</p>
              <p className="text-2xl font-bold text-amber-800">{stockStats.bajo}</p>
            </div>
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
              <p className="text-sm text-red-700">Agotados</p>
              <p className="text-2xl font-bold text-red-800">{stockStats.agotado}</p>
            </div>
          </div>

          {/* Filters */}
          <ProductsFilters
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            categoryFilter={categoryFilter}
            onCategoryChange={setCategoryFilter}
            stockFilter={stockFilter}
            onStockChange={setStockFilter}
          />

          {/* Table - View only for trabajador (no edit/delete) */}
          <ProductsTable
            products={filteredProducts}
            onEdit={() => {}}
            onDelete={() => {}}
            readOnly
          />
        </main>
      </div>
    </div>
  )
}

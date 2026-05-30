"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { SuppliersHeader } from "@/components/admin/suppliers/suppliers-header"
import { SuppliersFilters } from "@/components/admin/suppliers/suppliers-filters"
import { SupplierCard } from "@/components/admin/suppliers/supplier-card"
import { SupplierDialog } from "@/components/admin/suppliers/supplier-dialog"

export interface Supplier {
  id: string
  razonSocial: string
  ruc: string
  contacto: string
  telefono: string
  correo: string
  direccion: string
  categorias: string[]
  ultimoPedido: string
  estado: "activo" | "inactivo"
}

const initialSuppliers: Supplier[] = [
  {
    id: "1",
    razonSocial: "Distribuidora San Martín S.A.C.",
    ruc: "20456789012",
    contacto: "Carlos Mendoza",
    telefono: "987654321",
    correo: "ventas@sanmartin.com.pe",
    direccion: "Av. Industrial 456, Lima",
    categorias: ["Abarrotes", "Bebidas", "Snacks"],
    ultimoPedido: "2024-01-15",
    estado: "activo"
  },
  {
    id: "2",
    razonSocial: "Lácteos del Norte E.I.R.L.",
    ruc: "20123456789",
    contacto: "María García",
    telefono: "912345678",
    correo: "pedidos@lacteosnorte.com",
    direccion: "Jr. Los Pinos 789, Chiclayo",
    categorias: ["Lácteos"],
    ultimoPedido: "2024-01-18",
    estado: "activo"
  },
  {
    id: "3",
    razonSocial: "Importaciones Lima S.A.",
    ruc: "20567890123",
    contacto: "Roberto Chávez",
    telefono: "945678901",
    correo: "compras@importlima.com",
    direccion: "Calle Comercio 123, Callao",
    categorias: ["Limpieza", "Bebidas"],
    ultimoPedido: "2024-01-10",
    estado: "activo"
  },
  {
    id: "4",
    razonSocial: "Carnes Selectas del Sur S.A.C.",
    ruc: "20890123456",
    contacto: "Ana Quispe",
    telefono: "956789012",
    correo: "ventas@carnessur.com",
    direccion: "Av. Grau 567, Arequipa",
    categorias: ["Carnes", "Congelados"],
    ultimoPedido: "2024-01-20",
    estado: "activo"
  },
  {
    id: "5",
    razonSocial: "Dulcería Nacional E.I.R.L.",
    ruc: "20234567890",
    contacto: "Pedro Vargas",
    telefono: "923456789",
    correo: "info@dulcerianacional.com",
    direccion: "Jr. Azángaro 890, Lima",
    categorias: ["Dulces", "Snacks"],
    ultimoPedido: "2024-01-12",
    estado: "inactivo"
  },
  {
    id: "6",
    razonSocial: "Frutas Tropicales del Perú S.A.",
    ruc: "20345678901",
    contacto: "Lucía Fernández",
    telefono: "934567890",
    correo: "pedidos@frutastropicales.pe",
    direccion: "Mercado Mayorista, Lima",
    categorias: ["Frutas y Verduras"],
    ultimoPedido: "2024-01-19",
    estado: "activo"
  }
]

const categories = [
  "Abarrotes",
  "Bebidas", 
  "Lácteos",
  "Snacks",
  "Limpieza",
  "Carnes",
  "Frutas y Verduras",
  "Dulces",
  "Congelados",
  "Bebés"
]

export default function SuppliersPage() {
  const [suppliers, setSuppliers] = useState<Supplier[]>(initialSuppliers)
  const [searchQuery, setSearchQuery] = useState("")
  const [categoryFilter, setCategoryFilter] = useState("todas")
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingSupplier, setEditingSupplier] = useState<Supplier | null>(null)

  const filteredSuppliers = suppliers.filter(supplier => {
    const matchesSearch = 
      supplier.razonSocial.toLowerCase().includes(searchQuery.toLowerCase()) ||
      supplier.ruc.includes(searchQuery) ||
      supplier.contacto.toLowerCase().includes(searchQuery.toLowerCase())
    
    const matchesCategory = categoryFilter === "todas" || 
      supplier.categorias.includes(categoryFilter)
    
    return matchesSearch && matchesCategory
  })

  const handleAddSupplier = () => {
    setEditingSupplier(null)
    setIsDialogOpen(true)
  }

  const handleEditSupplier = (supplier: Supplier) => {
    setEditingSupplier(supplier)
    setIsDialogOpen(true)
  }

  const handleSaveSupplier = (supplierData: Omit<Supplier, "id" | "ultimoPedido" | "estado">) => {
    if (editingSupplier) {
      setSuppliers(prev => prev.map(s => 
        s.id === editingSupplier.id 
          ? { ...s, ...supplierData }
          : s
      ))
    } else {
      const newSupplier: Supplier = {
        ...supplierData,
        id: Date.now().toString(),
        ultimoPedido: new Date().toISOString().split("T")[0],
        estado: "activo"
      }
      setSuppliers(prev => [...prev, newSupplier])
    }
    setIsDialogOpen(false)
  }

  const handleDeleteSupplier = (id: string) => {
    setSuppliers(prev => prev.filter(s => s.id !== id))
  }

  const handleToggleStatus = (id: string) => {
    setSuppliers(prev => prev.map(s => 
      s.id === id 
        ? { ...s, estado: s.estado === "activo" ? "inactivo" : "activo" }
        : s
    ))
  }

  return (
    <div className="min-h-screen bg-background">
      <AdminSidebar />
      
      <div className="lg:pl-64">
        <AdminTopbar />
        
        <main className="p-6">
          <SuppliersHeader onAddSupplier={handleAddSupplier} />
          
          <SuppliersFilters
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            categoryFilter={categoryFilter}
            onCategoryChange={setCategoryFilter}
            categories={categories}
          />

          {/* Suppliers Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            {filteredSuppliers.map(supplier => (
              <SupplierCard
                key={supplier.id}
                supplier={supplier}
                onEdit={() => handleEditSupplier(supplier)}
                onDelete={() => handleDeleteSupplier(supplier.id)}
                onToggleStatus={() => handleToggleStatus(supplier.id)}
              />
            ))}
          </div>

          {filteredSuppliers.length === 0 && (
            <div className="text-center py-12">
              <p className="text-muted-foreground">
                No se encontraron proveedores con los filtros seleccionados.
              </p>
            </div>
          )}

          <SupplierDialog
            open={isDialogOpen}
            onOpenChange={setIsDialogOpen}
            supplier={editingSupplier}
            onSave={handleSaveSupplier}
            categories={categories}
          />
        </main>
      </div>
    </div>
  )
}

"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { StaffHeader } from "@/components/admin/staff/staff-header"
import { StaffFilters } from "@/components/admin/staff/staff-filters"
import { StaffTable } from "@/components/admin/staff/staff-table"
import { StaffDialog } from "@/components/admin/staff/staff-dialog"

export interface StaffMember {
  id: string
  nombre: string
  dni: string
  rol: "administrador" | "trabajador"
  telefono: string
  correo: string
  fechaIngreso: string
  estado: "activo" | "inactivo"
  avatar?: string
}

const initialStaff: StaffMember[] = [
  {
    id: "1",
    nombre: "Carlos Mendoza García",
    dni: "45678901",
    rol: "administrador",
    telefono: "987654321",
    correo: "carlos.mendoza@shiligama.com",
    fechaIngreso: "2022-03-15",
    estado: "activo",
  },
  {
    id: "2",
    nombre: "María López Rodríguez",
    dni: "12345678",
    rol: "trabajador",
    telefono: "912345678",
    correo: "maria.lopez@shiligama.com",
    fechaIngreso: "2023-01-10",
    estado: "activo",
  },
  {
    id: "3",
    nombre: "José Ramírez Torres",
    dni: "87654321",
    rol: "trabajador",
    telefono: "998877665",
    correo: "jose.ramirez@shiligama.com",
    fechaIngreso: "2023-06-20",
    estado: "activo",
  },
  {
    id: "4",
    nombre: "Ana Sánchez Flores",
    dni: "23456789",
    rol: "trabajador",
    telefono: "976543210",
    correo: "ana.sanchez@shiligama.com",
    fechaIngreso: "2023-08-05",
    estado: "inactivo",
  },
  {
    id: "5",
    nombre: "Pedro Castro Vega",
    dni: "34567890",
    rol: "trabajador",
    telefono: "965432109",
    correo: "pedro.castro@shiligama.com",
    fechaIngreso: "2024-02-12",
    estado: "activo",
  },
  {
    id: "6",
    nombre: "Lucía Paredes Díaz",
    dni: "56789012",
    rol: "administrador",
    telefono: "954321098",
    correo: "lucia.paredes@shiligama.com",
    fechaIngreso: "2022-11-01",
    estado: "activo",
  },
]

export default function StaffPage() {
  const [staff, setStaff] = useState<StaffMember[]>(initialStaff)
  const [searchQuery, setSearchQuery] = useState("")
  const [roleFilter, setRoleFilter] = useState<string>("all")
  const [statusFilter, setStatusFilter] = useState<string>("all")
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingStaff, setEditingStaff] = useState<StaffMember | null>(null)

  // Filter staff
  const filteredStaff = staff.filter((member) => {
    const matchesSearch =
      member.nombre.toLowerCase().includes(searchQuery.toLowerCase()) ||
      member.dni.includes(searchQuery) ||
      member.correo.toLowerCase().includes(searchQuery.toLowerCase())

    const matchesRole = roleFilter === "all" || member.rol === roleFilter
    const matchesStatus = statusFilter === "all" || member.estado === statusFilter

    return matchesSearch && matchesRole && matchesStatus
  })

  const handleAddStaff = () => {
    setEditingStaff(null)
    setIsDialogOpen(true)
  }

  const handleEditStaff = (member: StaffMember) => {
    setEditingStaff(member)
    setIsDialogOpen(true)
  }

  const handleSaveStaff = (data: Omit<StaffMember, "id">) => {
    if (editingStaff) {
      // Update existing
      setStaff((prev) =>
        prev.map((m) => (m.id === editingStaff.id ? { ...data, id: editingStaff.id } : m))
      )
    } else {
      // Add new
      const newMember: StaffMember = {
        ...data,
        id: Date.now().toString(),
      }
      setStaff((prev) => [newMember, ...prev])
    }
    setIsDialogOpen(false)
    setEditingStaff(null)
  }

  const handleDeleteStaff = (id: string) => {
    setStaff((prev) => prev.filter((m) => m.id !== id))
  }

  const handleToggleStatus = (id: string) => {
    setStaff((prev) =>
      prev.map((m) =>
        m.id === id ? { ...m, estado: m.estado === "activo" ? "inactivo" : "activo" } : m
      )
    )
  }

  // Stats
  const totalStaff = staff.length
  const activeStaff = staff.filter((m) => m.estado === "activo").length
  const adminCount = staff.filter((m) => m.rol === "administrador").length

  return (
    <div className="min-h-screen bg-background">
      <AdminSidebar />

      <div className="lg:pl-64">
        <AdminTopbar />

        <main className="p-4 lg:p-6 space-y-6">
          <StaffHeader onAddStaff={handleAddStaff} />

          {/* Stats summary */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div className="bg-card rounded-lg border border-border p-4">
              <p className="text-sm text-muted-foreground">Total Personal</p>
              <p className="text-2xl font-bold text-foreground">{totalStaff}</p>
            </div>
            <div className="bg-card rounded-lg border border-border p-4">
              <p className="text-sm text-muted-foreground">Personal Activo</p>
              <p className="text-2xl font-bold text-primary">{activeStaff}</p>
            </div>
            <div className="bg-card rounded-lg border border-border p-4">
              <p className="text-sm text-muted-foreground">Administradores</p>
              <p className="text-2xl font-bold text-foreground">{adminCount}</p>
            </div>
          </div>

          <StaffFilters
            searchQuery={searchQuery}
            onSearchChange={setSearchQuery}
            roleFilter={roleFilter}
            onRoleFilterChange={setRoleFilter}
            statusFilter={statusFilter}
            onStatusFilterChange={setStatusFilter}
          />

          <StaffTable
            staff={filteredStaff}
            onEdit={handleEditStaff}
            onDelete={handleDeleteStaff}
            onToggleStatus={handleToggleStatus}
          />
        </main>
      </div>

      <StaffDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        editingStaff={editingStaff}
        onSave={handleSaveStaff}
      />
    </div>
  )
}

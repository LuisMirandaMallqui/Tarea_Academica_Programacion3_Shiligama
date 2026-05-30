"use client"

import { UserPlus } from "lucide-react"
import { Button } from "@/components/ui/button"

interface StaffHeaderProps {
  onAddStaff: () => void
}

export function StaffHeader({ onAddStaff }: StaffHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 className="text-2xl lg:text-3xl font-bold text-foreground">
          Gestión de Personal
        </h1>
        <p className="text-muted-foreground mt-1">
          Administra el equipo de trabajo del minimarket
        </p>
      </div>
      <Button onClick={onAddStaff} className="gap-2">
        <UserPlus className="h-4 w-4" />
        Nuevo Trabajador
      </Button>
    </div>
  )
}

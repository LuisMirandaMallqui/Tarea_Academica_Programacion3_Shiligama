"use client"

import { useState, useEffect } from "react"
import { Eye, EyeOff } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Switch } from "@/components/ui/switch"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import type { StaffMember } from "@/app/admin/personal/page"

interface StaffDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  editingStaff: StaffMember | null
  onSave: (data: Omit<StaffMember, "id">) => void
}

export function StaffDialog({ open, onOpenChange, editingStaff, onSave }: StaffDialogProps) {
  const [showPassword, setShowPassword] = useState(false)
  const [formData, setFormData] = useState({
    nombre: "",
    dni: "",
    telefono: "",
    correo: "",
    rol: "trabajador" as "administrador" | "trabajador",
    password: "",
    estado: "activo" as "activo" | "inactivo",
    fechaIngreso: new Date().toISOString().split("T")[0],
  })
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    if (editingStaff) {
      setFormData({
        nombre: editingStaff.nombre,
        dni: editingStaff.dni,
        telefono: editingStaff.telefono,
        correo: editingStaff.correo,
        rol: editingStaff.rol,
        password: "",
        estado: editingStaff.estado,
        fechaIngreso: editingStaff.fechaIngreso,
      })
    } else {
      setFormData({
        nombre: "",
        dni: "",
        telefono: "",
        correo: "",
        rol: "trabajador",
        password: "",
        estado: "activo",
        fechaIngreso: new Date().toISOString().split("T")[0],
      })
    }
    setErrors({})
  }, [editingStaff, open])

  const validateForm = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.nombre.trim()) {
      newErrors.nombre = "El nombre es requerido"
    }

    if (!formData.dni.trim()) {
      newErrors.dni = "El DNI es requerido"
    } else if (!/^\d{8}$/.test(formData.dni)) {
      newErrors.dni = "El DNI debe tener 8 dígitos"
    }

    if (!formData.telefono.trim()) {
      newErrors.telefono = "El teléfono es requerido"
    } else if (!/^\d{9}$/.test(formData.telefono)) {
      newErrors.telefono = "El teléfono debe tener 9 dígitos"
    }

    if (!formData.correo.trim()) {
      newErrors.correo = "El correo es requerido"
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.correo)) {
      newErrors.correo = "Ingrese un correo válido"
    }

    if (!editingStaff && !formData.password.trim()) {
      newErrors.password = "La contraseña es requerida"
    } else if (!editingStaff && formData.password.length < 6) {
      newErrors.password = "Mínimo 6 caracteres"
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()

    if (!validateForm()) return

    onSave({
      nombre: formData.nombre,
      dni: formData.dni,
      telefono: formData.telefono,
      correo: formData.correo,
      rol: formData.rol,
      estado: formData.estado,
      fechaIngreso: formData.fechaIngreso,
    })
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>
            {editingStaff ? "Editar Trabajador" : "Nuevo Trabajador"}
          </DialogTitle>
          <DialogDescription>
            {editingStaff
              ? "Modifica los datos del trabajador"
              : "Ingresa los datos del nuevo miembro del equipo"}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Nombre completo */}
          <div className="space-y-2">
            <Label htmlFor="nombre">Nombre completo *</Label>
            <Input
              id="nombre"
              placeholder="Ej: Juan Pérez García"
              value={formData.nombre}
              onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
              className={errors.nombre ? "border-destructive" : ""}
            />
            {errors.nombre && (
              <p className="text-xs text-destructive">{errors.nombre}</p>
            )}
          </div>

          {/* DNI y Teléfono */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="dni">DNI *</Label>
              <Input
                id="dni"
                placeholder="12345678"
                maxLength={8}
                value={formData.dni}
                onChange={(e) =>
                  setFormData({ ...formData, dni: e.target.value.replace(/\D/g, "") })
                }
                className={errors.dni ? "border-destructive" : ""}
              />
              {errors.dni && (
                <p className="text-xs text-destructive">{errors.dni}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="telefono">Teléfono *</Label>
              <Input
                id="telefono"
                placeholder="987654321"
                maxLength={9}
                value={formData.telefono}
                onChange={(e) =>
                  setFormData({ ...formData, telefono: e.target.value.replace(/\D/g, "") })
                }
                className={errors.telefono ? "border-destructive" : ""}
              />
              {errors.telefono && (
                <p className="text-xs text-destructive">{errors.telefono}</p>
              )}
            </div>
          </div>

          {/* Correo */}
          <div className="space-y-2">
            <Label htmlFor="correo">Correo electrónico *</Label>
            <Input
              id="correo"
              type="email"
              placeholder="correo@ejemplo.com"
              value={formData.correo}
              onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
              className={errors.correo ? "border-destructive" : ""}
            />
            {errors.correo && (
              <p className="text-xs text-destructive">{errors.correo}</p>
            )}
          </div>

          {/* Rol */}
          <div className="space-y-2">
            <Label htmlFor="rol">Rol *</Label>
            <Select
              value={formData.rol}
              onValueChange={(value: "administrador" | "trabajador") =>
                setFormData({ ...formData, rol: value })
              }
            >
              <SelectTrigger id="rol">
                <SelectValue placeholder="Selecciona un rol" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="trabajador">Trabajador</SelectItem>
                <SelectItem value="administrador">Administrador</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Contraseña temporal - solo para nuevos */}
          {!editingStaff && (
            <div className="space-y-2">
              <Label htmlFor="password">Contraseña temporal *</Label>
              <div className="relative">
                <Input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  placeholder="Mínimo 6 caracteres"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className={errors.password ? "border-destructive pr-10" : "pr-10"}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                >
                  {showPassword ? (
                    <EyeOff className="h-4 w-4" />
                  ) : (
                    <Eye className="h-4 w-4" />
                  )}
                </button>
              </div>
              {errors.password && (
                <p className="text-xs text-destructive">{errors.password}</p>
              )}
              <p className="text-xs text-muted-foreground">
                El trabajador deberá cambiar esta contraseña en su primer inicio de sesión
              </p>
            </div>
          )}

          {/* Estado toggle */}
          <div className="flex items-center justify-between rounded-lg border border-border p-4">
            <div className="space-y-0.5">
              <Label htmlFor="estado">Estado del trabajador</Label>
              <p className="text-sm text-muted-foreground">
                {formData.estado === "activo"
                  ? "El trabajador puede acceder al sistema"
                  : "El acceso al sistema está deshabilitado"}
              </p>
            </div>
            <Switch
              id="estado"
              checked={formData.estado === "activo"}
              onCheckedChange={(checked) =>
                setFormData({ ...formData, estado: checked ? "activo" : "inactivo" })
              }
            />
          </div>

          <DialogFooter className="gap-2 sm:gap-0">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancelar
            </Button>
            <Button type="submit">
              {editingStaff ? "Guardar cambios" : "Crear trabajador"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

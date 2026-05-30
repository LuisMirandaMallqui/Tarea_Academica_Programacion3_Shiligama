"use client"

import { useState, useEffect } from "react"
import { Building2, User, Phone, Mail, MapPin, Tag } from "lucide-react"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import type { Supplier } from "@/app/admin/proveedores/page"

interface SupplierDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  supplier: Supplier | null
  onSave: (data: Omit<Supplier, "id" | "ultimoPedido" | "estado">) => void
  categories: string[]
}

export function SupplierDialog({
  open,
  onOpenChange,
  supplier,
  onSave,
  categories
}: SupplierDialogProps) {
  const [formData, setFormData] = useState({
    razonSocial: "",
    ruc: "",
    contacto: "",
    telefono: "",
    correo: "",
    direccion: "",
    categorias: [] as string[]
  })
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    if (supplier) {
      setFormData({
        razonSocial: supplier.razonSocial,
        ruc: supplier.ruc,
        contacto: supplier.contacto,
        telefono: supplier.telefono,
        correo: supplier.correo,
        direccion: supplier.direccion,
        categorias: supplier.categorias
      })
    } else {
      setFormData({
        razonSocial: "",
        ruc: "",
        contacto: "",
        telefono: "",
        correo: "",
        direccion: "",
        categorias: []
      })
    }
    setErrors({})
  }, [supplier, open])

  const validateForm = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.razonSocial.trim()) {
      newErrors.razonSocial = "La razón social es requerida"
    }
    if (!formData.ruc.trim()) {
      newErrors.ruc = "El RUC es requerido"
    } else if (!/^\d{11}$/.test(formData.ruc)) {
      newErrors.ruc = "El RUC debe tener 11 dígitos"
    }
    if (!formData.contacto.trim()) {
      newErrors.contacto = "El nombre de contacto es requerido"
    }
    if (!formData.telefono.trim()) {
      newErrors.telefono = "El teléfono es requerido"
    }
    if (!formData.correo.trim()) {
      newErrors.correo = "El correo es requerido"
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.correo)) {
      newErrors.correo = "Correo electrónico inválido"
    }
    if (!formData.direccion.trim()) {
      newErrors.direccion = "La dirección es requerida"
    }
    if (formData.categorias.length === 0) {
      newErrors.categorias = "Selecciona al menos una categoría"
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (validateForm()) {
      onSave(formData)
    }
  }

  const handleCategoryToggle = (category: string) => {
    setFormData(prev => ({
      ...prev,
      categorias: prev.categorias.includes(category)
        ? prev.categorias.filter(c => c !== category)
        : [...prev.categorias, category]
    }))
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-lg max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Building2 className="h-5 w-5 text-primary" />
            {supplier ? "Editar Proveedor" : "Nuevo Proveedor"}
          </DialogTitle>
          <DialogDescription>
            {supplier 
              ? "Modifica los datos del proveedor" 
              : "Completa los datos para registrar un nuevo proveedor"
            }
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Razón Social */}
          <div className="space-y-2">
            <Label htmlFor="razonSocial">Razón Social *</Label>
            <div className="relative">
              <Building2 className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                id="razonSocial"
                placeholder="Nombre de la empresa"
                value={formData.razonSocial}
                onChange={(e) => setFormData(prev => ({ ...prev, razonSocial: e.target.value }))}
                className="pl-9"
              />
            </div>
            {errors.razonSocial && (
              <p className="text-sm text-destructive">{errors.razonSocial}</p>
            )}
          </div>

          {/* RUC */}
          <div className="space-y-2">
            <Label htmlFor="ruc">RUC *</Label>
            <Input
              id="ruc"
              placeholder="20123456789"
              value={formData.ruc}
              onChange={(e) => setFormData(prev => ({ ...prev, ruc: e.target.value.replace(/\D/g, "").slice(0, 11) }))}
              maxLength={11}
            />
            {errors.ruc && (
              <p className="text-sm text-destructive">{errors.ruc}</p>
            )}
          </div>

          {/* Contacto */}
          <div className="space-y-2">
            <Label htmlFor="contacto">Persona de Contacto *</Label>
            <div className="relative">
              <User className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                id="contacto"
                placeholder="Nombre del contacto"
                value={formData.contacto}
                onChange={(e) => setFormData(prev => ({ ...prev, contacto: e.target.value }))}
                className="pl-9"
              />
            </div>
            {errors.contacto && (
              <p className="text-sm text-destructive">{errors.contacto}</p>
            )}
          </div>

          {/* Teléfono y Correo */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="telefono">Teléfono *</Label>
              <div className="relative">
                <Phone className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  id="telefono"
                  placeholder="987654321"
                  value={formData.telefono}
                  onChange={(e) => setFormData(prev => ({ ...prev, telefono: e.target.value.replace(/\D/g, "").slice(0, 9) }))}
                  className="pl-9"
                  maxLength={9}
                />
              </div>
              {errors.telefono && (
                <p className="text-sm text-destructive">{errors.telefono}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="correo">Correo *</Label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  id="correo"
                  type="email"
                  placeholder="correo@empresa.com"
                  value={formData.correo}
                  onChange={(e) => setFormData(prev => ({ ...prev, correo: e.target.value }))}
                  className="pl-9"
                />
              </div>
              {errors.correo && (
                <p className="text-sm text-destructive">{errors.correo}</p>
              )}
            </div>
          </div>

          {/* Dirección */}
          <div className="space-y-2">
            <Label htmlFor="direccion">Dirección *</Label>
            <div className="relative">
              <MapPin className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                id="direccion"
                placeholder="Av. Principal 123, Lima"
                value={formData.direccion}
                onChange={(e) => setFormData(prev => ({ ...prev, direccion: e.target.value }))}
                className="pl-9"
              />
            </div>
            {errors.direccion && (
              <p className="text-sm text-destructive">{errors.direccion}</p>
            )}
          </div>

          {/* Categorías */}
          <div className="space-y-2">
            <Label className="flex items-center gap-2">
              <Tag className="h-4 w-4 text-muted-foreground" />
              Categorías de Productos *
            </Label>
            <div className="grid grid-cols-2 gap-2 p-3 border rounded-lg bg-muted/30">
              {categories.map(category => (
                <div key={category} className="flex items-center gap-2">
                  <Checkbox
                    id={`cat-${category}`}
                    checked={formData.categorias.includes(category)}
                    onCheckedChange={() => handleCategoryToggle(category)}
                  />
                  <Label 
                    htmlFor={`cat-${category}`} 
                    className="text-sm font-normal cursor-pointer"
                  >
                    {category}
                  </Label>
                </div>
              ))}
            </div>
            {errors.categorias && (
              <p className="text-sm text-destructive">{errors.categorias}</p>
            )}
          </div>

          <DialogFooter className="gap-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancelar
            </Button>
            <Button type="submit">
              {supplier ? "Guardar Cambios" : "Registrar Proveedor"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

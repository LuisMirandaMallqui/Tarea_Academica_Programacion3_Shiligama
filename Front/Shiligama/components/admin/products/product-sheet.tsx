"use client"

import { useState, useEffect } from "react"
import { ImagePlus } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Switch } from "@/components/ui/switch"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetFooter,
} from "@/components/ui/sheet"
import type { Product } from "@/app/admin/productos/page"

interface ProductSheetProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  product: Product | null
  onSave: (product: Product) => void
}

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
  "Bebés",
]

const emptyProduct: Product = {
  id: "",
  codigo: "",
  nombre: "",
  categoria: "",
  precio: 0,
  stock: 0,
  stockMinimo: 5,
  descripcion: "",
  imagen: "/placeholder.svg?height=80&width=80",
  activo: true,
}

export function ProductSheet({ open, onOpenChange, product, onSave }: ProductSheetProps) {
  const [formData, setFormData] = useState<Product>(emptyProduct)
  const [errors, setErrors] = useState<Record<string, string>>({})

  const isEditing = !!product

  useEffect(() => {
    if (product) {
      setFormData(product)
    } else {
      setFormData(emptyProduct)
    }
    setErrors({})
  }, [product, open])

  const validateForm = () => {
    const newErrors: Record<string, string> = {}

    if (!formData.nombre.trim()) {
      newErrors.nombre = "El nombre es requerido"
    }
    if (!formData.categoria) {
      newErrors.categoria = "La categoría es requerida"
    }
    if (formData.precio <= 0) {
      newErrors.precio = "El precio debe ser mayor a 0"
    }
    if (formData.stock < 0) {
      newErrors.stock = "El stock no puede ser negativo"
    }
    if (formData.stockMinimo < 0) {
      newErrors.stockMinimo = "El stock mínimo no puede ser negativo"
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

  const handleChange = (field: keyof Product, value: string | number | boolean) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: "" }))
    }
  }

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent className="w-full sm:max-w-lg overflow-y-auto">
        <SheetHeader>
          <SheetTitle>
            {isEditing ? "Editar Producto" : "Nuevo Producto"}
          </SheetTitle>
          <SheetDescription>
            {isEditing
              ? "Modifica los datos del producto seleccionado"
              : "Completa los datos para agregar un nuevo producto al catálogo"}
          </SheetDescription>
        </SheetHeader>

        <form onSubmit={handleSubmit} className="space-y-6 py-6">
          {/* Image upload placeholder */}
          <div className="space-y-2">
            <Label>Imagen del producto</Label>
            <div className="flex items-center gap-4">
              <div className="w-20 h-20 rounded-lg bg-muted flex items-center justify-center border-2 border-dashed border-border">
                <ImagePlus className="h-8 w-8 text-muted-foreground" />
              </div>
              <div className="flex-1">
                <Button type="button" variant="outline" size="sm">
                  Subir imagen
                </Button>
                <p className="text-xs text-muted-foreground mt-1">
                  PNG, JPG hasta 2MB
                </p>
              </div>
            </div>
          </div>

          {/* Nombre */}
          <div className="space-y-2">
            <Label htmlFor="nombre">Nombre del producto *</Label>
            <Input
              id="nombre"
              placeholder="Ej: Arroz Costeño 5kg"
              value={formData.nombre}
              onChange={(e) => handleChange("nombre", e.target.value)}
              className={errors.nombre ? "border-destructive" : ""}
            />
            {errors.nombre && (
              <p className="text-xs text-destructive">{errors.nombre}</p>
            )}
          </div>

          {/* Categoría */}
          <div className="space-y-2">
            <Label htmlFor="categoria">Categoría *</Label>
            <Select
              value={formData.categoria}
              onValueChange={(value) => handleChange("categoria", value)}
            >
              <SelectTrigger className={errors.categoria ? "border-destructive" : ""}>
                <SelectValue placeholder="Seleccionar categoría" />
              </SelectTrigger>
              <SelectContent>
                {categories.map((category) => (
                  <SelectItem key={category} value={category}>
                    {category}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            {errors.categoria && (
              <p className="text-xs text-destructive">{errors.categoria}</p>
            )}
          </div>

          {/* Precio */}
          <div className="space-y-2">
            <Label htmlFor="precio">Precio (S/.) *</Label>
            <Input
              id="precio"
              type="number"
              step="0.01"
              min="0"
              placeholder="0.00"
              value={formData.precio || ""}
              onChange={(e) => handleChange("precio", parseFloat(e.target.value) || 0)}
              className={errors.precio ? "border-destructive" : ""}
            />
            {errors.precio && (
              <p className="text-xs text-destructive">{errors.precio}</p>
            )}
          </div>

          {/* Stock y Stock Mínimo */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="stock">Stock actual</Label>
              <Input
                id="stock"
                type="number"
                min="0"
                placeholder="0"
                value={formData.stock || ""}
                onChange={(e) => handleChange("stock", parseInt(e.target.value) || 0)}
                className={errors.stock ? "border-destructive" : ""}
              />
              {errors.stock && (
                <p className="text-xs text-destructive">{errors.stock}</p>
              )}
            </div>
            <div className="space-y-2">
              <Label htmlFor="stockMinimo">Stock mínimo</Label>
              <Input
                id="stockMinimo"
                type="number"
                min="0"
                placeholder="5"
                value={formData.stockMinimo || ""}
                onChange={(e) => handleChange("stockMinimo", parseInt(e.target.value) || 0)}
                className={errors.stockMinimo ? "border-destructive" : ""}
              />
              {errors.stockMinimo && (
                <p className="text-xs text-destructive">{errors.stockMinimo}</p>
              )}
            </div>
          </div>

          {/* Descripción */}
          <div className="space-y-2">
            <Label htmlFor="descripcion">Descripción</Label>
            <Textarea
              id="descripcion"
              placeholder="Descripción del producto (opcional)"
              value={formData.descripcion}
              onChange={(e) => handleChange("descripcion", e.target.value)}
              rows={3}
            />
          </div>

          {/* Estado activo */}
          <div className="flex items-center justify-between py-2">
            <div className="space-y-0.5">
              <Label htmlFor="activo">Estado del producto</Label>
              <p className="text-sm text-muted-foreground">
                {formData.activo ? "Visible en el catálogo" : "Oculto del catálogo"}
              </p>
            </div>
            <Switch
              id="activo"
              checked={formData.activo}
              onCheckedChange={(checked) => handleChange("activo", checked)}
            />
          </div>
        </form>

        <SheetFooter className="gap-2 sm:gap-0">
          <Button
            type="button"
            variant="outline"
            onClick={() => onOpenChange(false)}
          >
            Cancelar
          </Button>
          <Button type="submit" onClick={handleSubmit}>
            {isEditing ? "Guardar cambios" : "Crear producto"}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}

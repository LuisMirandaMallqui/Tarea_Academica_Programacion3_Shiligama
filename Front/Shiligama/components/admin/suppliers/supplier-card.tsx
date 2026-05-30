"use client"

import { 
  Building2, 
  User, 
  Phone, 
  Mail, 
  MapPin, 
  Calendar,
  MoreVertical,
  Pencil,
  Trash2,
  Power
} from "lucide-react"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import type { Supplier } from "@/app/admin/proveedores/page"

interface SupplierCardProps {
  supplier: Supplier
  onEdit: () => void
  onDelete: () => void
  onToggleStatus: () => void
}

export function SupplierCard({ 
  supplier, 
  onEdit, 
  onDelete, 
  onToggleStatus 
}: SupplierCardProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("es-PE", {
      day: "2-digit",
      month: "short",
      year: "numeric"
    })
  }

  return (
    <Card className={`relative transition-all hover:shadow-md ${
      supplier.estado === "inactivo" ? "opacity-60" : ""
    }`}>
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between gap-2">
          <div className="flex items-center gap-3 min-w-0">
            <div className="h-10 w-10 rounded-lg bg-primary/10 flex items-center justify-center shrink-0">
              <Building2 className="h-5 w-5 text-primary" />
            </div>
            <div className="min-w-0">
              <h3 className="font-semibold text-foreground truncate" title={supplier.razonSocial}>
                {supplier.razonSocial}
              </h3>
              <p className="text-sm text-muted-foreground">RUC: {supplier.ruc}</p>
            </div>
          </div>
          
          <div className="flex items-center gap-2 shrink-0">
            <Badge 
              variant={supplier.estado === "activo" ? "default" : "secondary"}
              className={supplier.estado === "activo" 
                ? "bg-primary/10 text-primary hover:bg-primary/20" 
                : ""
              }
            >
              {supplier.estado === "activo" ? "Activo" : "Inactivo"}
            </Badge>
            
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="icon" className="h-8 w-8">
                  <MoreVertical className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem onClick={onEdit}>
                  <Pencil className="h-4 w-4 mr-2" />
                  Editar
                </DropdownMenuItem>
                <DropdownMenuItem onClick={onToggleStatus}>
                  <Power className="h-4 w-4 mr-2" />
                  {supplier.estado === "activo" ? "Desactivar" : "Activar"}
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem 
                  onClick={onDelete}
                  className="text-destructive focus:text-destructive"
                >
                  <Trash2 className="h-4 w-4 mr-2" />
                  Eliminar
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </CardHeader>

      <CardContent className="space-y-4">
        {/* Contact Info */}
        <div className="space-y-2">
          <div className="flex items-center gap-2 text-sm">
            <User className="h-4 w-4 text-muted-foreground shrink-0" />
            <span className="truncate">{supplier.contacto}</span>
          </div>
          <div className="flex items-center gap-2 text-sm">
            <Phone className="h-4 w-4 text-muted-foreground shrink-0" />
            <span>{supplier.telefono}</span>
          </div>
          <div className="flex items-center gap-2 text-sm">
            <Mail className="h-4 w-4 text-muted-foreground shrink-0" />
            <span className="truncate text-primary">{supplier.correo}</span>
          </div>
          <div className="flex items-start gap-2 text-sm">
            <MapPin className="h-4 w-4 text-muted-foreground shrink-0 mt-0.5" />
            <span className="text-muted-foreground line-clamp-2">{supplier.direccion}</span>
          </div>
        </div>

        {/* Categories */}
        <div>
          <p className="text-xs text-muted-foreground mb-2">Categorías que provee:</p>
          <div className="flex flex-wrap gap-1.5">
            {supplier.categorias.map(categoria => (
              <Badge 
                key={categoria} 
                variant="outline"
                className="text-xs"
              >
                {categoria}
              </Badge>
            ))}
          </div>
        </div>

        {/* Last Order */}
        <div className="flex items-center gap-2 text-sm pt-2 border-t">
          <Calendar className="h-4 w-4 text-muted-foreground" />
          <span className="text-muted-foreground">Último pedido:</span>
          <span className="font-medium">{formatDate(supplier.ultimoPedido)}</span>
        </div>
      </CardContent>
    </Card>
  )
}

"use client"

import Image from "next/image"
import { MoreHorizontal, ArrowUpDown, Package } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import type { InventoryItem } from "@/app/admin/inventario/page"

interface InventoryTableProps {
  inventory: InventoryItem[]
  onAdjustStock: (product: InventoryItem) => void
}

export function InventoryTable({ inventory, onAdjustStock }: InventoryTableProps) {
  const getStatusBadge = (estado: InventoryItem["estado"]) => {
    switch (estado) {
      case "ok":
        return (
          <Badge className="bg-emerald-100 text-emerald-700 hover:bg-emerald-100">
            OK
          </Badge>
        )
      case "bajo":
        return (
          <Badge className="bg-amber-100 text-amber-700 hover:bg-amber-100">
            Bajo
          </Badge>
        )
      case "agotado":
        return (
          <Badge className="bg-red-100 text-red-700 hover:bg-red-100">
            Agotado
          </Badge>
        )
    }
  }

  return (
    <Card className="bg-card">
      <CardHeader className="pb-3">
        <CardTitle className="text-lg font-semibold flex items-center gap-2">
          <Package className="h-5 w-5 text-primary" />
          Listado de Inventario
        </CardTitle>
      </CardHeader>
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow className="bg-muted/50">
                <TableHead className="w-[80px]">Imagen</TableHead>
                <TableHead>
                  <Button variant="ghost" className="h-8 px-2 -ml-2 font-semibold">
                    Producto
                    <ArrowUpDown className="ml-2 h-4 w-4" />
                  </Button>
                </TableHead>
                <TableHead>Categoría</TableHead>
                <TableHead className="text-center">Stock Actual</TableHead>
                <TableHead className="text-center">Stock Mínimo</TableHead>
                <TableHead className="text-center">Estado</TableHead>
                <TableHead>Última Actualización</TableHead>
                <TableHead className="text-right">Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {inventory.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={8} className="h-24 text-center">
                    <p className="text-muted-foreground">
                      No se encontraron productos
                    </p>
                  </TableCell>
                </TableRow>
              ) : (
                inventory.map((item) => (
                  <TableRow key={item.id} className="group">
                    <TableCell>
                      <div className="relative h-12 w-12 rounded-lg overflow-hidden bg-muted">
                        <Image
                          src={item.imagen}
                          alt={item.nombre}
                          fill
                          className="object-cover"
                        />
                      </div>
                    </TableCell>
                    <TableCell>
                      <div>
                        <p className="font-medium">{item.nombre}</p>
                        <p className="text-sm text-muted-foreground">
                          {item.codigo}
                        </p>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant="outline">{item.categoria}</Badge>
                    </TableCell>
                    <TableCell className="text-center">
                      <span
                        className={`font-semibold ${
                          item.stockActual === 0
                            ? "text-red-600"
                            : item.stockActual < item.stockMinimo
                            ? "text-amber-600"
                            : "text-foreground"
                        }`}
                      >
                        {item.stockActual}
                      </span>
                    </TableCell>
                    <TableCell className="text-center text-muted-foreground">
                      {item.stockMinimo}
                    </TableCell>
                    <TableCell className="text-center">
                      {getStatusBadge(item.estado)}
                    </TableCell>
                    <TableCell className="text-sm text-muted-foreground">
                      {item.ultimaActualizacion}
                    </TableCell>
                    <TableCell className="text-right">
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button
                            variant="ghost"
                            size="icon"
                            className="opacity-0 group-hover:opacity-100 transition-opacity"
                          >
                            <MoreHorizontal className="h-4 w-4" />
                            <span className="sr-only">Acciones</span>
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem onClick={() => onAdjustStock(item)}>
                            Ajustar Stock
                          </DropdownMenuItem>
                          <DropdownMenuItem>Ver Historial</DropdownMenuItem>
                          <DropdownMenuSeparator />
                          <DropdownMenuItem>Editar Producto</DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </div>
      </CardContent>
    </Card>
  )
}

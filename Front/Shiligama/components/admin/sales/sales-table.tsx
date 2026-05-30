"use client"

import { format } from "date-fns"
import { es } from "date-fns/locale"
import {
  Store,
  Globe,
  MessageCircle,
  Smartphone,
  CreditCard,
  Banknote,
  FileText,
  FileMinus,
} from "lucide-react"
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils"
import type { SaleOrder } from "@/app/admin/ventas/page"

interface SalesTableProps {
  sales: SaleOrder[]
  onRowClick: (order: SaleOrder) => void
}

const canalConfig = {
  presencial: {
    label: "Presencial",
    icon: Store,
    className: "bg-slate-100 text-slate-700 border-slate-200",
  },
  web: {
    label: "Web",
    icon: Globe,
    className: "bg-emerald-100 text-emerald-700 border-emerald-200",
  },
  whatsapp: {
    label: "WhatsApp",
    icon: MessageCircle,
    className: "bg-green-100 text-green-700 border-green-200",
  },
}

const metodoPagoConfig = {
  yape: { label: "Yape", icon: Smartphone, color: "text-purple-600" },
  plin: { label: "Plin", icon: Smartphone, color: "text-cyan-600" },
  tarjeta: { label: "Tarjeta", icon: CreditCard, color: "text-blue-600" },
  efectivo: { label: "Efectivo", icon: Banknote, color: "text-green-600" },
}

const estadoConfig = {
  completado: {
    label: "Completado",
    className: "bg-emerald-100 text-emerald-700",
  },
  pendiente: {
    label: "Pendiente",
    className: "bg-amber-100 text-amber-700",
  },
  cancelado: {
    label: "Cancelado",
    className: "bg-red-100 text-red-700",
  },
  reembolsado: {
    label: "Reembolsado",
    className: "bg-purple-100 text-purple-700",
  },
}

export function SalesTable({ sales, onRowClick }: SalesTableProps) {
  return (
    <Card className="border-0 shadow-sm">
      <CardHeader>
        <CardTitle>Registro de Ventas</CardTitle>
        <CardDescription>
          Haz clic en una fila para ver los detalles completos de la orden
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead># Orden</TableHead>
                <TableHead>Fecha</TableHead>
                <TableHead>Cliente</TableHead>
                <TableHead>Canal</TableHead>
                <TableHead className="text-center">Productos</TableHead>
                <TableHead className="text-right">Total</TableHead>
                <TableHead>Pago</TableHead>
                <TableHead>Comprobante</TableHead>
                <TableHead>Estado</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {sales.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={9} className="h-32 text-center">
                    <p className="text-muted-foreground">
                      No se encontraron ventas con los filtros aplicados
                    </p>
                  </TableCell>
                </TableRow>
              ) : (
                sales.map((sale) => {
                  const canal = canalConfig[sale.canal]
                  const metodoPago = metodoPagoConfig[sale.metodoPago]
                  const estado = estadoConfig[sale.estado]
                  const CanalIcon = canal.icon
                  const PagoIcon = metodoPago.icon

                  return (
                    <TableRow
                      key={sale.id}
                      className="cursor-pointer hover:bg-muted/50 transition-colors"
                      onClick={() => onRowClick(sale)}
                    >
                      <TableCell className="font-medium">{sale.id}</TableCell>
                      <TableCell className="text-muted-foreground">
                        {format(new Date(sale.fecha), "dd/MM/yyyy HH:mm", {
                          locale: es,
                        })}
                      </TableCell>
                      <TableCell>{sale.cliente}</TableCell>
                      <TableCell>
                        <Badge
                          variant="outline"
                          className={cn("gap-1", canal.className)}
                        >
                          <CanalIcon className="h-3 w-3" />
                          {canal.label}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-center">
                        {sale.productos.length}
                      </TableCell>
                      <TableCell className="text-right font-medium">
                        S/. {sale.total.toFixed(2)}
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1.5">
                          <PagoIcon
                            className={cn("h-4 w-4", metodoPago.color)}
                          />
                          <span className="text-sm">{metodoPago.label}</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        {sale.comprobante === "ninguno" ? (
                          <span className="text-muted-foreground text-sm flex items-center gap-1">
                            <FileMinus className="h-4 w-4" />
                            Sin comp.
                          </span>
                        ) : (
                          <div className="flex items-center gap-1">
                            <FileText className="h-4 w-4 text-primary" />
                            <span className="text-sm capitalize">
                              {sale.comprobante}
                            </span>
                          </div>
                        )}
                      </TableCell>
                      <TableCell>
                        <Badge
                          className={cn("font-normal", estado.className)}
                        >
                          {estado.label}
                        </Badge>
                      </TableCell>
                    </TableRow>
                  )
                })
              )}
            </TableBody>
          </Table>
        </div>
      </CardContent>
    </Card>
  )
}

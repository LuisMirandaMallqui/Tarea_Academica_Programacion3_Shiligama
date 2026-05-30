"use client"

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
import { Button } from "@/components/ui/button"
import { Eye, MoreHorizontal } from "lucide-react"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { cn } from "@/lib/utils"

const recentOrders = [
  {
    id: "ORD-001",
    customer: "María García",
    date: "09 May 2026, 10:30",
    total: "S/. 125.50",
    items: 8,
    status: "entregado",
    channel: "Online",
  },
  {
    id: "ORD-002",
    customer: "Carlos Mendoza",
    date: "09 May 2026, 09:45",
    total: "S/. 89.00",
    items: 5,
    status: "en_camino",
    channel: "Online",
  },
  {
    id: "ORD-003",
    customer: "Ana Rodríguez",
    date: "09 May 2026, 09:15",
    total: "S/. 234.80",
    items: 12,
    status: "preparando",
    channel: "Presencial",
  },
  {
    id: "ORD-004",
    customer: "Luis Torres",
    date: "09 May 2026, 08:50",
    total: "S/. 56.00",
    items: 3,
    status: "pendiente",
    channel: "Online",
  },
  {
    id: "ORD-005",
    customer: "Rosa Sánchez",
    date: "09 May 2026, 08:30",
    total: "S/. 178.90",
    items: 9,
    status: "entregado",
    channel: "Presencial",
  },
  {
    id: "ORD-006",
    customer: "Pedro Díaz",
    date: "08 May 2026, 18:20",
    total: "S/. 312.40",
    items: 15,
    status: "cancelado",
    channel: "Online",
  },
]

const statusConfig: Record<string, { label: string; className: string }> = {
  pendiente: {
    label: "Pendiente",
    className: "bg-amber-100 text-amber-700 hover:bg-amber-100",
  },
  preparando: {
    label: "Preparando",
    className: "bg-blue-100 text-blue-700 hover:bg-blue-100",
  },
  en_camino: {
    label: "En camino",
    className: "bg-purple-100 text-purple-700 hover:bg-purple-100",
  },
  entregado: {
    label: "Entregado",
    className: "bg-emerald-100 text-emerald-700 hover:bg-emerald-100",
  },
  cancelado: {
    label: "Cancelado",
    className: "bg-red-100 text-red-700 hover:bg-red-100",
  },
}

export function RecentOrdersTable() {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <div>
          <CardTitle>Pedidos Recientes</CardTitle>
          <CardDescription>
            Últimos pedidos realizados en el sistema
          </CardDescription>
        </div>
        <Button variant="outline" size="sm">
          Ver todos
        </Button>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Pedido</TableHead>
              <TableHead>Cliente</TableHead>
              <TableHead className="hidden md:table-cell">Fecha</TableHead>
              <TableHead className="hidden sm:table-cell">Items</TableHead>
              <TableHead>Total</TableHead>
              <TableHead className="hidden lg:table-cell">Canal</TableHead>
              <TableHead>Estado</TableHead>
              <TableHead className="text-right">Acciones</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {recentOrders.map((order) => {
              const status = statusConfig[order.status]
              return (
                <TableRow key={order.id}>
                  <TableCell className="font-medium">{order.id}</TableCell>
                  <TableCell>{order.customer}</TableCell>
                  <TableCell className="hidden md:table-cell text-muted-foreground">
                    {order.date}
                  </TableCell>
                  <TableCell className="hidden sm:table-cell">
                    {order.items}
                  </TableCell>
                  <TableCell className="font-medium">{order.total}</TableCell>
                  <TableCell className="hidden lg:table-cell">
                    <Badge
                      variant="outline"
                      className={cn(
                        order.channel === "Online"
                          ? "border-emerald-200 text-emerald-700"
                          : "border-slate-200 text-slate-700"
                      )}
                    >
                      {order.channel}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Badge className={cn("font-normal", status.className)}>
                      {status.label}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right">
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon" className="h-8 w-8">
                          <MoreHorizontal className="h-4 w-4" />
                          <span className="sr-only">Acciones</span>
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem>
                          <Eye className="h-4 w-4 mr-2" />
                          Ver detalles
                        </DropdownMenuItem>
                        <DropdownMenuItem>Actualizar estado</DropdownMenuItem>
                        <DropdownMenuItem>Imprimir</DropdownMenuItem>
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </TableCell>
                </TableRow>
              )
            })}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  )
}

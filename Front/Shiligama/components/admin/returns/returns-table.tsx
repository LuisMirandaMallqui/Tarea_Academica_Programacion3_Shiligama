"use client"

import { format } from "date-fns"
import { es } from "date-fns/locale"
import { MoreHorizontal, Pencil, Trash2, Calendar, AlertTriangle, Package, AlertCircle, HelpCircle } from "lucide-react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip"
import type { ReturnItem, ReturnReason } from "@/app/admin/devoluciones/page"

interface ReturnsTableProps {
  returns: ReturnItem[]
  onEdit: (item: ReturnItem) => void
  onDelete: (id: string) => void
}

const reasonConfig: Record<ReturnReason, { color: string; icon: React.ElementType }> = {
  "Vencido": { color: "bg-red-100 text-red-700 border-red-200", icon: Calendar },
  "Dañado": { color: "bg-amber-100 text-amber-700 border-amber-200", icon: AlertTriangle },
  "Error de pedido": { color: "bg-blue-100 text-blue-700 border-blue-200", icon: Package },
  "Otro": { color: "bg-gray-100 text-gray-700 border-gray-200", icon: HelpCircle },
}

export function ReturnsTable({ returns, onEdit, onDelete }: ReturnsTableProps) {
  return (
    <Card className="border-border">
      <CardHeader className="pb-3">
        <CardTitle className="text-lg font-semibold">
          Registro de Devoluciones
        </CardTitle>
      </CardHeader>
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow className="bg-muted/50">
                <TableHead className="font-semibold">Fecha</TableHead>
                <TableHead className="font-semibold">Producto</TableHead>
                <TableHead className="font-semibold text-center">Cantidad</TableHead>
                <TableHead className="font-semibold">Motivo</TableHead>
                <TableHead className="font-semibold">Registrado por</TableHead>
                <TableHead className="font-semibold text-right">Monto</TableHead>
                <TableHead className="font-semibold text-right">Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {returns.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={7} className="h-32 text-center">
                    <div className="flex flex-col items-center gap-2 text-muted-foreground">
                      <AlertCircle className="h-8 w-8" />
                      <p>No se encontraron devoluciones</p>
                    </div>
                  </TableCell>
                </TableRow>
              ) : (
                returns.map((item) => {
                  const ReasonIcon = reasonConfig[item.reason].icon
                  return (
                    <TableRow key={item.id} className="hover:bg-muted/30">
                      <TableCell className="font-medium">
                        {format(new Date(item.date), "dd MMM yyyy", { locale: es })}
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <div>
                            <p className="font-medium">{item.product}</p>
                            <p className="text-xs text-muted-foreground">{item.productCode}</p>
                          </div>
                          {item.observations && (
                            <TooltipProvider>
                              <Tooltip>
                                <TooltipTrigger asChild>
                                  <AlertCircle className="h-4 w-4 text-muted-foreground cursor-help" />
                                </TooltipTrigger>
                                <TooltipContent className="max-w-xs">
                                  <p>{item.observations}</p>
                                </TooltipContent>
                              </Tooltip>
                            </TooltipProvider>
                          )}
                        </div>
                      </TableCell>
                      <TableCell className="text-center font-medium">
                        {item.quantity}
                      </TableCell>
                      <TableCell>
                        <Badge 
                          variant="outline" 
                          className={`gap-1 ${reasonConfig[item.reason].color}`}
                        >
                          <ReasonIcon className="h-3 w-3" />
                          {item.reason}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-muted-foreground">
                        {item.registeredBy}
                      </TableCell>
                      <TableCell className="text-right font-semibold text-destructive">
                        -S/. {item.amount.toFixed(2)}
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
                            <DropdownMenuItem onClick={() => onEdit(item)}>
                              <Pencil className="mr-2 h-4 w-4" />
                              Editar
                            </DropdownMenuItem>
                            <DropdownMenuItem 
                              onClick={() => onDelete(item.id)}
                              className="text-destructive focus:text-destructive"
                            >
                              <Trash2 className="mr-2 h-4 w-4" />
                              Eliminar
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
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

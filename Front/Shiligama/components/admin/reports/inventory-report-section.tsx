"use client"

import {
  Bar,
  BarChart,
  XAxis,
  YAxis,
  ResponsiveContainer,
  Tooltip,
  Cell,
} from "recharts"
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
import { Warehouse, AlertTriangle, Package, TrendingDown } from "lucide-react"

const lowStockProducts = [
  { id: 1, codigo: "PRD-001", nombre: "Leche Gloria 1L", categoria: "Lácteos", stockActual: 8, stockMinimo: 20, estado: "critico" },
  { id: 2, codigo: "PRD-015", nombre: "Aceite Primor 1L", categoria: "Abarrotes", stockActual: 12, stockMinimo: 25, estado: "bajo" },
  { id: 3, codigo: "PRD-023", nombre: "Arroz Costeño 5kg", categoria: "Abarrotes", stockActual: 5, stockMinimo: 30, estado: "critico" },
  { id: 4, codigo: "PRD-034", nombre: "Yogurt Gloria 1L", categoria: "Lácteos", stockActual: 15, stockMinimo: 20, estado: "bajo" },
  { id: 5, codigo: "PRD-045", nombre: "Pan de Molde Bimbo", categoria: "Panadería", stockActual: 3, stockMinimo: 15, estado: "critico" },
  { id: 6, codigo: "PRD-056", nombre: "Huevos La Calera x30", categoria: "Lácteos", stockActual: 18, stockMinimo: 25, estado: "bajo" },
]

const mermasByCategoryData = [
  { categoria: "Lácteos", monto: 1250, color: "#EF4444" },
  { categoria: "Panadería", monto: 890, color: "#F97316" },
  { categoria: "Frutas", monto: 650, color: "#EAB308" },
  { categoria: "Carnes", monto: 420, color: "#22C55E" },
  { categoria: "Bebidas", monto: 280, color: "#3B82F6" },
  { categoria: "Snacks", monto: 150, color: "#8B5CF6" },
]

const summaryStats = [
  {
    label: "Total Mermas",
    value: "S/. 3,640",
    subtext: "Este mes",
    icon: TrendingDown,
    color: "text-red-600",
    bgColor: "bg-red-100",
  },
  {
    label: "Productos Bajo Stock",
    value: "24",
    subtext: "Requieren atención",
    icon: AlertTriangle,
    color: "text-amber-600",
    bgColor: "bg-amber-100",
  },
  {
    label: "Productos Agotados",
    value: "3",
    subtext: "Sin stock",
    icon: Package,
    color: "text-red-600",
    bgColor: "bg-red-100",
  },
]

export function InventoryReportSection() {
  const getStatusBadge = (estado: string) => {
    if (estado === "critico") {
      return <Badge variant="destructive">Crítico</Badge>
    }
    return <Badge className="bg-amber-500 hover:bg-amber-600">Bajo</Badge>
  }

  return (
    <div className="space-y-4">
      <h2 className="text-lg font-semibold text-foreground flex items-center gap-2">
        <Warehouse className="h-5 w-5 text-primary" />
        Inventario y Mermas
      </h2>

      {/* Summary Stats */}
      <div className="grid gap-4 sm:grid-cols-3">
        {summaryStats.map((stat) => (
          <Card key={stat.label}>
            <CardContent className="p-4">
              <div className="flex items-center gap-3">
                <div className={`h-10 w-10 rounded-lg ${stat.bgColor} flex items-center justify-center`}>
                  <stat.icon className={`h-5 w-5 ${stat.color}`} />
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">{stat.label}</p>
                  <p className={`text-2xl font-bold ${stat.color}`}>{stat.value}</p>
                  <p className="text-xs text-muted-foreground">{stat.subtext}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-4 lg:grid-cols-2">
        {/* Low Stock Table */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5 text-amber-500" />
              Productos con Bajo Stock
            </CardTitle>
            <CardDescription>Productos que requieren reabastecimiento</CardDescription>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Producto</TableHead>
                  <TableHead className="text-center">Stock</TableHead>
                  <TableHead className="text-center">Mínimo</TableHead>
                  <TableHead className="text-center">Estado</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {lowStockProducts.map((product) => (
                  <TableRow key={product.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium text-sm">{product.nombre}</p>
                        <p className="text-xs text-muted-foreground">{product.categoria}</p>
                      </div>
                    </TableCell>
                    <TableCell className="text-center">
                      <span className={`font-semibold ${
                        product.estado === "critico" ? "text-red-600" : "text-amber-600"
                      }`}>
                        {product.stockActual}
                      </span>
                    </TableCell>
                    <TableCell className="text-center text-muted-foreground">
                      {product.stockMinimo}
                    </TableCell>
                    <TableCell className="text-center">
                      {getStatusBadge(product.estado)}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>

        {/* Mermas by Category Chart */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingDown className="h-5 w-5 text-red-500" />
              Mermas por Categoría
            </CardTitle>
            <CardDescription>Pérdidas del mes actual por categoría de producto</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart
                  data={mermasByCategoryData}
                  layout="vertical"
                  margin={{ top: 5, right: 30, left: 80, bottom: 5 }}
                >
                  <XAxis 
                    type="number" 
                    tick={{ fontSize: 11 }}
                    tickFormatter={(value) => `S/.${value}`}
                  />
                  <YAxis
                    type="category"
                    dataKey="categoria"
                    tick={{ fontSize: 11 }}
                    width={80}
                  />
                  <Tooltip
                    formatter={(value: number) => [`S/. ${value.toFixed(2)}`, "Pérdida"]}
                  />
                  <Bar dataKey="monto" radius={[0, 4, 4, 0]}>
                    {mermasByCategoryData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            </div>

            {/* Legend */}
            <div className="mt-4 flex flex-wrap gap-3 justify-center">
              {mermasByCategoryData.map((item) => (
                <div key={item.categoria} className="flex items-center gap-1.5">
                  <div 
                    className="h-3 w-3 rounded-sm" 
                    style={{ backgroundColor: item.color }}
                  />
                  <span className="text-xs text-muted-foreground">{item.categoria}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

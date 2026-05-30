"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Package, AlertTriangle, XCircle, Clock } from "lucide-react"

interface InventoryKpiCardsProps {
  totalProducts: number
  lowStockItems: number
  outOfStockItems: number
  lastUpdate: string
}

export function InventoryKpiCards({
  totalProducts,
  lowStockItems,
  outOfStockItems,
  lastUpdate,
}: InventoryKpiCardsProps) {
  const kpis = [
    {
      title: "Total Productos",
      value: totalProducts.toString(),
      description: "en inventario",
      icon: Package,
      iconBg: "bg-emerald-100",
      iconColor: "text-emerald-600",
    },
    {
      title: "Stock Bajo",
      value: lowStockItems.toString(),
      description: "requieren reposición",
      icon: AlertTriangle,
      iconBg: "bg-amber-100",
      iconColor: "text-amber-600",
    },
    {
      title: "Agotados",
      value: outOfStockItems.toString(),
      description: "sin existencias",
      icon: XCircle,
      iconBg: "bg-red-100",
      iconColor: "text-red-600",
    },
    {
      title: "Última Actualización",
      value: lastUpdate,
      description: "sincronizado",
      icon: Clock,
      iconBg: "bg-blue-100",
      iconColor: "text-blue-600",
    },
  ]

  return (
    <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
      {kpis.map((kpi, index) => (
        <Card key={index} className="bg-card">
          <CardContent className="p-6">
            <div className="flex items-start justify-between">
              <div className="space-y-2">
                <p className="text-sm font-medium text-muted-foreground">
                  {kpi.title}
                </p>
                <p className="text-2xl font-bold tracking-tight">{kpi.value}</p>
                <p className="text-sm text-muted-foreground">{kpi.description}</p>
              </div>
              <div
                className={`flex items-center justify-center w-12 h-12 rounded-lg ${kpi.iconBg} ${kpi.iconColor}`}
              >
                <kpi.icon className="h-6 w-6" />
              </div>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}

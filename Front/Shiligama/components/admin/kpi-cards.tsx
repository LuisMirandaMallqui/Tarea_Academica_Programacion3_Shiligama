"use client"

import { Card, CardContent } from "@/components/ui/card"
import {
  TrendingUp,
  TrendingDown,
  ShoppingBag,
  Clock,
  AlertTriangle,
  DollarSign,
} from "lucide-react"
import { cn } from "@/lib/utils"

const kpiData = [
  {
    title: "Ventas hoy",
    value: "S/. 3,459.00",
    change: "+12.5%",
    trend: "up" as const,
    icon: ShoppingBag,
    description: "vs. ayer",
  },
  {
    title: "Pedidos pendientes",
    value: "18",
    change: "+3",
    trend: "up" as const,
    icon: Clock,
    description: "nuevos hoy",
  },
  {
    title: "Productos con bajo stock",
    value: "24",
    change: "+5",
    trend: "down" as const,
    icon: AlertTriangle,
    description: "requieren atención",
  },
  {
    title: "Ingresos del mes",
    value: "S/. 87,230.00",
    change: "+8.2%",
    trend: "up" as const,
    icon: DollarSign,
    description: "vs. mes anterior",
  },
]

export function KpiCards() {
  return (
    <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
      {kpiData.map((kpi, index) => (
        <Card key={index} className="bg-card">
          <CardContent className="p-6">
            <div className="flex items-start justify-between">
              <div className="space-y-2">
                <p className="text-sm font-medium text-muted-foreground">
                  {kpi.title}
                </p>
                <p className="text-2xl font-bold tracking-tight">{kpi.value}</p>
                <div className="flex items-center gap-1 text-sm">
                  {kpi.trend === "up" && kpi.title !== "Productos con bajo stock" ? (
                    <TrendingUp className="h-4 w-4 text-emerald-500" />
                  ) : (
                    <TrendingDown className="h-4 w-4 text-red-500" />
                  )}
                  <span
                    className={cn(
                      "font-medium",
                      kpi.trend === "up" && kpi.title !== "Productos con bajo stock"
                        ? "text-emerald-500"
                        : "text-red-500"
                    )}
                  >
                    {kpi.change}
                  </span>
                  <span className="text-muted-foreground">{kpi.description}</span>
                </div>
              </div>
              <div
                className={cn(
                  "flex items-center justify-center w-12 h-12 rounded-lg",
                  kpi.title === "Productos con bajo stock"
                    ? "bg-amber-100 text-amber-600"
                    : "bg-emerald-100 text-emerald-600"
                )}
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

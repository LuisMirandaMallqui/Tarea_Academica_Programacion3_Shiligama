"use client"

import { Card, CardContent } from "@/components/ui/card"
import {
  ShoppingCart,
  DollarSign,
  Receipt,
  Globe,
  Store,
} from "lucide-react"

interface SalesKpiCardsProps {
  totalVentas: number
  montoTotal: number
  ticketPromedio: number
  ventasOnline: number
  ventasPresencial: number
}

export function SalesKpiCards({
  totalVentas,
  montoTotal,
  ticketPromedio,
  ventasOnline,
  ventasPresencial,
}: SalesKpiCardsProps) {
  const kpis = [
    {
      title: "Total Ventas",
      value: totalVentas.toString(),
      subtitle: "transacciones",
      icon: ShoppingCart,
      color: "text-primary",
      bgColor: "bg-primary/10",
    },
    {
      title: "Monto Total",
      value: `S/. ${montoTotal.toFixed(2)}`,
      subtitle: "ingresos brutos",
      icon: DollarSign,
      color: "text-emerald-600",
      bgColor: "bg-emerald-50",
    },
    {
      title: "Ticket Promedio",
      value: `S/. ${ticketPromedio.toFixed(2)}`,
      subtitle: "por transacción",
      icon: Receipt,
      color: "text-blue-600",
      bgColor: "bg-blue-50",
    },
    {
      title: "Online vs Presencial",
      value: `${ventasOnline} / ${ventasPresencial}`,
      subtitle: "web+whatsapp / tienda",
      icon: Globe,
      secondaryIcon: Store,
      color: "text-purple-600",
      bgColor: "bg-purple-50",
    },
  ]

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      {kpis.map((kpi) => (
        <Card key={kpi.title} className="border-0 shadow-sm">
          <CardContent className="p-5">
            <div className="flex items-start justify-between">
              <div className="space-y-1">
                <p className="text-sm text-muted-foreground">{kpi.title}</p>
                <p className="text-2xl font-bold text-foreground">{kpi.value}</p>
                <p className="text-xs text-muted-foreground">{kpi.subtitle}</p>
              </div>
              <div className={`p-3 rounded-xl ${kpi.bgColor}`}>
                {kpi.secondaryIcon ? (
                  <div className="flex items-center gap-1">
                    <kpi.icon className={`h-5 w-5 ${kpi.color}`} />
                    <span className="text-muted-foreground text-xs">/</span>
                    <kpi.secondaryIcon className={`h-5 w-5 ${kpi.color}`} />
                  </div>
                ) : (
                  <kpi.icon className={`h-5 w-5 ${kpi.color}`} />
                )}
              </div>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}

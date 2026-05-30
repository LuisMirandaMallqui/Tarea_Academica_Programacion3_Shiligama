"use client"

import {
  Bar,
  BarChart,
  XAxis,
  YAxis,
  ResponsiveContainer,
  Legend,
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
import { Package, TrendingUp, Store, Globe } from "lucide-react"
import { Badge } from "@/components/ui/badge"

const topProductsData = [
  { name: "Arroz Costeño 5kg", presencial: 145, web: 89, total: 234 },
  { name: "Aceite Primor 1L", presencial: 132, web: 76, total: 208 },
  { name: "Leche Gloria 1L", presencial: 118, web: 82, total: 200 },
  { name: "Azúcar Rubia 1kg", presencial: 98, web: 65, total: 163 },
  { name: "Fideos Don Vittorio", presencial: 87, web: 54, total: 141 },
  { name: "Atún Florida", presencial: 76, web: 58, total: 134 },
  { name: "Papel Higiénico Elite", presencial: 72, web: 45, total: 117 },
  { name: "Detergente Bolivar", presencial: 65, web: 42, total: 107 },
  { name: "Galletas Soda Field", presencial: 58, web: 38, total: 96 },
  { name: "Yogurt Gloria", presencial: 52, web: 35, total: 87 },
]

const channelStats = [
  {
    label: "Ventas Presencial",
    value: "S/. 52,340",
    percentage: "60%",
    icon: Store,
    color: "bg-primary",
  },
  {
    label: "Ventas Web",
    value: "S/. 34,894",
    percentage: "40%",
    icon: Globe,
    color: "bg-blue-500",
  },
]

export function TopProductsSection() {
  const presencialColor = "#0D4525"
  const webColor = "#3B82F6"

  return (
    <div className="space-y-4">
      <h2 className="text-lg font-semibold text-foreground flex items-center gap-2">
        <TrendingUp className="h-5 w-5 text-primary" />
        Productos Más Vendidos
      </h2>

      <div className="grid gap-4 lg:grid-cols-4">
        {/* Channel Stats Cards */}
        <div className="space-y-4">
          {channelStats.map((stat) => (
            <Card key={stat.label}>
              <CardContent className="p-4">
                <div className="flex items-center gap-3">
                  <div className={`h-10 w-10 rounded-lg ${stat.color} flex items-center justify-center`}>
                    <stat.icon className="h-5 w-5 text-white" />
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">{stat.label}</p>
                    <p className="text-xl font-bold">{stat.value}</p>
                    <Badge variant="secondary" className="mt-1 text-xs">
                      {stat.percentage} del total
                    </Badge>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}

          {/* Top Product Card */}
          <Card className="border-primary/20 bg-primary/5">
            <CardContent className="p-4">
              <div className="flex items-center gap-3">
                <div className="h-10 w-10 rounded-lg bg-primary flex items-center justify-center">
                  <Package className="h-5 w-5 text-white" />
                </div>
                <div>
                  <p className="text-sm text-muted-foreground">Producto Estrella</p>
                  <p className="font-semibold">Arroz Costeño 5kg</p>
                  <p className="text-sm text-primary font-medium">234 unidades</p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Horizontal Bar Chart */}
        <Card className="lg:col-span-3">
          <CardHeader>
            <CardTitle>Top 10 Productos por Canal</CardTitle>
            <CardDescription>Comparativa de ventas presencial vs web</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="h-[400px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart
                  data={topProductsData}
                  layout="vertical"
                  margin={{ top: 5, right: 30, left: 100, bottom: 5 }}
                >
                  <XAxis type="number" tick={{ fontSize: 11 }} />
                  <YAxis
                    type="category"
                    dataKey="name"
                    tick={{ fontSize: 11 }}
                    width={100}
                  />
                  <Tooltip
                    formatter={(value: number, name: string) => [
                      `${value} unidades`,
                      name === "presencial" ? "Presencial" : "Web",
                    ]}
                  />
                  <Legend
                    formatter={(value) => (
                      <span className="text-sm">
                        {value === "presencial" ? "Presencial" : "Web"}
                      </span>
                    )}
                  />
                  <Bar dataKey="presencial" stackId="a" fill={presencialColor} radius={[0, 0, 0, 0]} />
                  <Bar dataKey="web" stackId="a" fill={webColor} radius={[0, 4, 4, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

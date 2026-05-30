"use client"

import {
  Bar,
  BarChart,
  XAxis,
  YAxis,
  ResponsiveContainer,
  Tooltip,
  Cell,
  Legend,
} from "recharts"
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Users, UserPlus, UserCheck, Crown, TrendingUp } from "lucide-react"

const customerStatsData = [
  { mes: "Ene", nuevos: 45, recurrentes: 120 },
  { mes: "Feb", nuevos: 52, recurrentes: 135 },
  { mes: "Mar", nuevos: 38, recurrentes: 142 },
  { mes: "Abr", nuevos: 65, recurrentes: 158 },
  { mes: "May", nuevos: 48, recurrentes: 165 },
]

const topBuyers = [
  { 
    id: 1, 
    nombre: "María García Pérez", 
    email: "maria.garcia@email.com",
    compras: 45,
    totalGastado: 2340.50,
    ultimaCompra: "Hace 2 días",
  },
  { 
    id: 2, 
    nombre: "Carlos Rodríguez Luna", 
    email: "carlos.rodriguez@email.com",
    compras: 38,
    totalGastado: 1890.00,
    ultimaCompra: "Hace 1 día",
  },
  { 
    id: 3, 
    nombre: "Ana Torres Mendoza", 
    email: "ana.torres@email.com",
    compras: 32,
    totalGastado: 1650.75,
    ultimaCompra: "Hoy",
  },
  { 
    id: 4, 
    nombre: "José López Vargas", 
    email: "jose.lopez@email.com",
    compras: 28,
    totalGastado: 1420.30,
    ultimaCompra: "Hace 3 días",
  },
  { 
    id: 5, 
    nombre: "Laura Sánchez Díaz", 
    email: "laura.sanchez@email.com",
    compras: 25,
    totalGastado: 1280.00,
    ultimaCompra: "Hace 5 días",
  },
]

const customerKpis = [
  {
    label: "Total Clientes",
    value: "1,247",
    change: "+12%",
    icon: Users,
    color: "text-primary",
    bgColor: "bg-primary/10",
  },
  {
    label: "Nuevos Este Mes",
    value: "48",
    change: "+8%",
    icon: UserPlus,
    color: "text-blue-600",
    bgColor: "bg-blue-100",
  },
  {
    label: "Clientes Recurrentes",
    value: "165",
    change: "+15%",
    icon: UserCheck,
    color: "text-green-600",
    bgColor: "bg-green-100",
  },
  {
    label: "Tasa Retención",
    value: "78%",
    change: "+3%",
    icon: TrendingUp,
    color: "text-amber-600",
    bgColor: "bg-amber-100",
  },
]

export function CustomersReportSection() {
  const nuevosColor = "#3B82F6"
  const recurrentesColor = "#0D4525"

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .substring(0, 2)
      .toUpperCase()
  }

  const getRankBadge = (index: number) => {
    if (index === 0) {
      return (
        <Badge className="bg-amber-500 hover:bg-amber-600 gap-1">
          <Crown className="h-3 w-3" />
          Top 1
        </Badge>
      )
    }
    return (
      <Badge variant="secondary">
        #{index + 1}
      </Badge>
    )
  }

  return (
    <div className="space-y-4">
      <h2 className="text-lg font-semibold text-foreground flex items-center gap-2">
        <Users className="h-5 w-5 text-primary" />
        Reporte de Clientes
      </h2>

      {/* KPI Cards */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {customerKpis.map((kpi) => (
          <Card key={kpi.label}>
            <CardContent className="p-4">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <p className="text-sm text-muted-foreground">{kpi.label}</p>
                  <p className="text-2xl font-bold">{kpi.value}</p>
                  <p className="text-xs text-green-600">{kpi.change} vs. mes anterior</p>
                </div>
                <div className={`h-12 w-12 rounded-full ${kpi.bgColor} flex items-center justify-center`}>
                  <kpi.icon className={`h-6 w-6 ${kpi.color}`} />
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-4 lg:grid-cols-2">
        {/* New vs Returning Customers Chart */}
        <Card>
          <CardHeader>
            <CardTitle>Nuevos vs Recurrentes</CardTitle>
            <CardDescription>Evolución mensual de clientes</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart
                  data={customerStatsData}
                  margin={{ top: 5, right: 20, left: 10, bottom: 5 }}
                >
                  <XAxis dataKey="mes" tick={{ fontSize: 11 }} />
                  <YAxis tick={{ fontSize: 11 }} />
                  <Tooltip
                    formatter={(value: number, name: string) => [
                      value,
                      name === "nuevos" ? "Nuevos" : "Recurrentes",
                    ]}
                  />
                  <Legend
                    formatter={(value) => (
                      <span className="text-sm">
                        {value === "nuevos" ? "Nuevos" : "Recurrentes"}
                      </span>
                    )}
                  />
                  <Bar dataKey="nuevos" fill={nuevosColor} radius={[4, 4, 0, 0]} />
                  <Bar dataKey="recurrentes" fill={recurrentesColor} radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>

        {/* Top Buyers List */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Crown className="h-5 w-5 text-amber-500" />
              Mejores Compradores
            </CardTitle>
            <CardDescription>Clientes con mayor volumen de compras</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {topBuyers.map((buyer, index) => (
                <div
                  key={buyer.id}
                  className="flex items-center gap-4 p-3 rounded-lg bg-muted/50 hover:bg-muted transition-colors"
                >
                  <Avatar className="h-10 w-10 border-2 border-primary/20">
                    <AvatarFallback className="bg-primary/10 text-primary text-sm font-medium">
                      {getInitials(buyer.nombre)}
                    </AvatarFallback>
                  </Avatar>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2">
                      <p className="font-medium text-sm truncate">{buyer.nombre}</p>
                      {getRankBadge(index)}
                    </div>
                    <p className="text-xs text-muted-foreground truncate">{buyer.email}</p>
                  </div>

                  <div className="text-right">
                    <p className="font-semibold text-primary">
                      S/. {buyer.totalGastado.toFixed(2)}
                    </p>
                    <p className="text-xs text-muted-foreground">
                      {buyer.compras} compras
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

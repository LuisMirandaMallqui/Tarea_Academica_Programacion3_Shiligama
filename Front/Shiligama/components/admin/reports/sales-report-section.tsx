"use client"

import {
  Line,
  LineChart,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer,
  Pie,
  PieChart,
  Cell,
  Legend,
  Tooltip,
} from "recharts"
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart"
import { DollarSign, TrendingUp, Receipt, CreditCard } from "lucide-react"

const dailySalesData = [
  { date: "01/05", ventas: 1850 },
  { date: "02/05", ventas: 2340 },
  { date: "03/05", ventas: 1920 },
  { date: "04/05", ventas: 2780 },
  { date: "05/05", ventas: 3100 },
  { date: "06/05", ventas: 4200 },
  { date: "07/05", ventas: 3890 },
  { date: "08/05", ventas: 2450 },
  { date: "09/05", ventas: 2890 },
  { date: "10/05", ventas: 3200 },
  { date: "11/05", ventas: 2650 },
  { date: "12/05", ventas: 3450 },
  { date: "13/05", ventas: 4100 },
  { date: "14/05", ventas: 3780 },
]

const paymentMethodsData = [
  { name: "Yape", value: 42, color: "#7C3AED" },
  { name: "Efectivo", value: 28, color: "#0D4525" },
  { name: "Plin", value: 18, color: "#00D1A0" },
  { name: "Tarjeta", value: 12, color: "#3B82F6" },
]

const salesChartConfig = {
  ventas: {
    label: "Ventas",
    color: "#0D4525",
  },
}

const kpiData = [
  {
    title: "Ingresos Totales",
    value: "S/. 87,234.50",
    change: "+12.5%",
    changeType: "positive" as const,
    icon: DollarSign,
  },
  {
    title: "Ticket Promedio",
    value: "S/. 45.80",
    change: "+3.2%",
    changeType: "positive" as const,
    icon: Receipt,
  },
  {
    title: "Total Transacciones",
    value: "1,904",
    change: "+8.7%",
    changeType: "positive" as const,
    icon: CreditCard,
  },
  {
    title: "Crecimiento",
    value: "+15.3%",
    change: "vs. mes anterior",
    changeType: "neutral" as const,
    icon: TrendingUp,
  },
]

export function SalesReportSection() {
  return (
    <div className="space-y-4">
      <h2 className="text-lg font-semibold text-foreground flex items-center gap-2">
        <DollarSign className="h-5 w-5 text-primary" />
        Reporte de Ventas
      </h2>

      {/* KPI Cards */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {kpiData.map((kpi) => (
          <Card key={kpi.title}>
            <CardContent className="p-4">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <p className="text-sm text-muted-foreground">{kpi.title}</p>
                  <p className="text-2xl font-bold">{kpi.value}</p>
                  <p
                    className={`text-xs ${
                      kpi.changeType === "positive"
                        ? "text-green-600"
                        : kpi.changeType === "negative"
                        ? "text-red-600"
                        : "text-muted-foreground"
                    }`}
                  >
                    {kpi.change}
                  </p>
                </div>
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center">
                  <kpi.icon className="h-6 w-6 text-primary" />
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Charts Row */}
      <div className="grid gap-4 lg:grid-cols-3">
        {/* Daily Sales Line Chart */}
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle>Ventas Diarias</CardTitle>
            <CardDescription>Evolución de ventas en el período seleccionado</CardDescription>
          </CardHeader>
          <CardContent>
            <ChartContainer config={salesChartConfig} className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart
                  data={dailySalesData}
                  margin={{ top: 5, right: 20, left: 10, bottom: 5 }}
                >
                  <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
                  <XAxis
                    dataKey="date"
                    tick={{ fontSize: 11 }}
                    tickLine={false}
                    axisLine={false}
                  />
                  <YAxis
                    tick={{ fontSize: 11 }}
                    tickLine={false}
                    axisLine={false}
                    tickFormatter={(value) => `S/.${value}`}
                  />
                  <ChartTooltip
                    content={
                      <ChartTooltipContent
                        formatter={(value) => [`S/. ${value}`, "Ventas"]}
                      />
                    }
                  />
                  <Line
                    type="monotone"
                    dataKey="ventas"
                    stroke="#0D4525"
                    strokeWidth={2}
                    dot={{ fill: "#0D4525", strokeWidth: 2, r: 3 }}
                    activeDot={{ r: 5, fill: "#0D4525" }}
                  />
                </LineChart>
              </ResponsiveContainer>
            </ChartContainer>
          </CardContent>
        </Card>

        {/* Payment Methods Pie Chart */}
        <Card>
          <CardHeader>
            <CardTitle>Métodos de Pago</CardTitle>
            <CardDescription>Distribución por tipo de pago</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={paymentMethodsData}
                    cx="50%"
                    cy="45%"
                    innerRadius={50}
                    outerRadius={80}
                    paddingAngle={4}
                    dataKey="value"
                  >
                    {paymentMethodsData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip
                    formatter={(value: number) => [`${value}%`, "Porcentaje"]}
                  />
                  <Legend
                    verticalAlign="bottom"
                    height={36}
                    formatter={(value) => (
                      <span className="text-sm text-foreground">{value}</span>
                    )}
                  />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

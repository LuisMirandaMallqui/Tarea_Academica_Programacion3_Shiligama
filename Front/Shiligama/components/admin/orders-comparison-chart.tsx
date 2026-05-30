"use client"

import {
  Bar,
  BarChart,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer,
  Legend,
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

const ordersData = [
  { day: "Lun", presencial: 45, online: 32 },
  { day: "Mar", presencial: 38, online: 28 },
  { day: "Mié", presencial: 52, online: 41 },
  { day: "Jue", presencial: 47, online: 35 },
  { day: "Vie", presencial: 61, online: 58 },
  { day: "Sáb", presencial: 78, online: 65 },
  { day: "Dom", presencial: 55, online: 48 },
]

const chartConfig = {
  presencial: {
    label: "Presencial",
    color: "#0D4525",
  },
  online: {
    label: "Online",
    color: "#22c55e",
  },
}

export function OrdersComparisonChart() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Pedidos: Presencial vs Online</CardTitle>
        <CardDescription>
          Comparación de canales de venta semanal
        </CardDescription>
      </CardHeader>
      <CardContent>
        <ChartContainer config={chartConfig} className="h-[300px] w-full">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart
              data={ordersData}
              margin={{ top: 5, right: 20, left: 10, bottom: 5 }}
            >
              <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
              <XAxis
                dataKey="day"
                tick={{ fontSize: 12 }}
                tickLine={false}
                axisLine={false}
              />
              <YAxis
                tick={{ fontSize: 12 }}
                tickLine={false}
                axisLine={false}
              />
              <ChartTooltip content={<ChartTooltipContent />} />
              <Legend
                wrapperStyle={{ fontSize: "12px", paddingTop: "10px" }}
              />
              <Bar
                dataKey="presencial"
                name="Presencial"
                fill="#0D4525"
                radius={[4, 4, 0, 0]}
              />
              <Bar
                dataKey="online"
                name="Online"
                fill="#22c55e"
                radius={[4, 4, 0, 0]}
              />
            </BarChart>
          </ResponsiveContainer>
        </ChartContainer>
      </CardContent>
    </Card>
  )
}

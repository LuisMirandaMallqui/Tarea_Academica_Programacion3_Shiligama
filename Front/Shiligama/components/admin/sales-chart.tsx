"use client"

import {
  Line,
  LineChart,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer,
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

const salesData = [
  { day: "Lun", ventas: 2400 },
  { day: "Mar", ventas: 1398 },
  { day: "Mié", ventas: 3200 },
  { day: "Jue", ventas: 2780 },
  { day: "Vie", ventas: 4890 },
  { day: "Sáb", ventas: 5390 },
  { day: "Dom", ventas: 3490 },
]

const chartConfig = {
  ventas: {
    label: "Ventas",
    color: "#0D4525",
  },
}

export function SalesChart() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Ventas de la Semana</CardTitle>
        <CardDescription>
          Resumen de ventas diarias en soles (S/.)
        </CardDescription>
      </CardHeader>
      <CardContent>
        <ChartContainer config={chartConfig} className="h-[300px] w-full">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart
              data={salesData}
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
                strokeWidth={3}
                dot={{ fill: "#0D4525", strokeWidth: 2, r: 4 }}
                activeDot={{ r: 6, fill: "#0D4525" }}
              />
            </LineChart>
          </ResponsiveContainer>
        </ChartContainer>
      </CardContent>
    </Card>
  )
}

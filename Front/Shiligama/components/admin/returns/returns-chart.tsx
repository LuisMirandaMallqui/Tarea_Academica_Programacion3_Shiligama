"use client"

import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from "recharts"

interface ReturnsChartProps {
  data: { reason: string; amount: number }[]
}

const COLORS: Record<string, string> = {
  "Vencido": "#ef4444",
  "Dañado": "#f59e0b",
  "Error de pedido": "#3b82f6",
  "Otro": "#6b7280",
}

export function ReturnsChart({ data }: ReturnsChartProps) {
  const total = data.reduce((sum, item) => sum + item.amount, 0)

  return (
    <Card className="border-border">
      <CardHeader>
        <CardTitle className="text-lg font-semibold">Mermas por Motivo</CardTitle>
        <CardDescription>
          Distribución de pérdidas por tipo de devolución este mes
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="grid gap-6 lg:grid-cols-3">
          {/* Chart */}
          <div className="lg:col-span-2 h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={data} layout="vertical" margin={{ left: 20, right: 20 }}>
                <CartesianGrid strokeDasharray="3 3" horizontal={true} vertical={false} />
                <XAxis 
                  type="number" 
                  tickFormatter={(value) => `S/.${value}`}
                  fontSize={12}
                />
                <YAxis 
                  type="category" 
                  dataKey="reason" 
                  width={100}
                  fontSize={12}
                />
                <Tooltip 
                  formatter={(value: number) => [`S/. ${value.toFixed(2)}`, "Monto"]}
                  contentStyle={{
                    backgroundColor: "white",
                    border: "1px solid #e5e7eb",
                    borderRadius: "8px",
                    fontSize: "14px",
                  }}
                />
                <Bar dataKey="amount" radius={[0, 4, 4, 0]}>
                  {data.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[entry.reason] || "#6b7280"} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Legend */}
          <div className="space-y-3">
            <p className="text-sm font-medium text-muted-foreground mb-4">
              Desglose por motivo
            </p>
            {data.map((item) => {
              const percentage = total > 0 ? ((item.amount / total) * 100).toFixed(1) : "0"
              return (
                <div key={item.reason} className="flex items-center justify-between p-3 rounded-lg bg-muted/30">
                  <div className="flex items-center gap-3">
                    <div 
                      className="h-3 w-3 rounded-full" 
                      style={{ backgroundColor: COLORS[item.reason] }}
                    />
                    <span className="text-sm font-medium">{item.reason}</span>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-semibold">S/. {item.amount.toFixed(2)}</p>
                    <p className="text-xs text-muted-foreground">{percentage}%</p>
                  </div>
                </div>
              )
            })}
            <div className="pt-3 mt-3 border-t border-border">
              <div className="flex items-center justify-between">
                <span className="text-sm font-semibold">Total Mermas</span>
                <span className="text-lg font-bold text-destructive">S/. {total.toFixed(2)}</span>
              </div>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

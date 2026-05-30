"use client"

import { cn } from "@/lib/utils"
import { 
  ClipboardList, 
  Clock, 
  ChefHat, 
  PackageCheck, 
  Truck, 
  XCircle 
} from "lucide-react"
import type { OrderStatus } from "@/app/admin/pedidos/page"

interface OrdersTabsProps {
  activeTab: OrderStatus | "todos"
  onTabChange: (tab: OrderStatus | "todos") => void
  counts: Record<OrderStatus | "todos", number>
}

const tabs: { key: OrderStatus | "todos"; label: string; icon: React.ElementType; color: string }[] = [
  { key: "todos", label: "Todos", icon: ClipboardList, color: "text-foreground" },
  { key: "pendiente", label: "Pendiente", icon: Clock, color: "text-amber-600" },
  { key: "preparacion", label: "En preparación", icon: ChefHat, color: "text-blue-600" },
  { key: "listo", label: "Listo", icon: PackageCheck, color: "text-primary" },
  { key: "entregado", label: "Entregado", icon: Truck, color: "text-emerald-600" },
  { key: "cancelado", label: "Cancelado", icon: XCircle, color: "text-destructive" },
]

export function OrdersTabs({ activeTab, onTabChange, counts }: OrdersTabsProps) {
  return (
    <div className="flex gap-2 overflow-x-auto pb-2">
      {tabs.map((tab) => {
        const Icon = tab.icon
        const isActive = activeTab === tab.key
        const count = counts[tab.key]
        
        return (
          <button
            key={tab.key}
            onClick={() => onTabChange(tab.key)}
            className={cn(
              "flex items-center gap-2 px-4 py-2.5 rounded-lg text-sm font-medium whitespace-nowrap transition-all",
              "border",
              isActive 
                ? "bg-primary text-primary-foreground border-primary shadow-sm" 
                : "bg-card text-muted-foreground border-border hover:bg-muted hover:text-foreground"
            )}
          >
            <Icon className={cn("h-4 w-4", !isActive && tab.color)} />
            <span>{tab.label}</span>
            <span className={cn(
              "ml-1 px-2 py-0.5 rounded-full text-xs font-semibold",
              isActive 
                ? "bg-primary-foreground/20 text-primary-foreground" 
                : "bg-muted text-muted-foreground"
            )}>
              {count}
            </span>
          </button>
        )
      })}
    </div>
  )
}

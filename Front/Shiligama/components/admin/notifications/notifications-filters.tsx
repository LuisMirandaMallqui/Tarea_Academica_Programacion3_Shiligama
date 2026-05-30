"use client"

import { 
  Package, 
  ShoppingCart, 
  CreditCard, 
  Settings,
  Inbox
} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils"
import type { Notification, NotificationType } from "@/app/admin/notificaciones/page"

interface NotificationsFiltersProps {
  activeFilter: NotificationType | "all"
  onFilterChange: (filter: NotificationType | "all") => void
  notifications: Notification[]
}

const filters: { key: NotificationType | "all"; label: string; icon: typeof Package }[] = [
  { key: "all", label: "Todas", icon: Inbox },
  { key: "stock", label: "Stock", icon: Package },
  { key: "order", label: "Pedidos", icon: ShoppingCart },
  { key: "payment", label: "Pagos", icon: CreditCard },
  { key: "system", label: "Sistema", icon: Settings },
]

export function NotificationsFilters({ 
  activeFilter, 
  onFilterChange,
  notifications 
}: NotificationsFiltersProps) {
  const getCounts = (type: NotificationType | "all") => {
    if (type === "all") {
      return notifications.filter(n => n.status === "unread").length
    }
    return notifications.filter(n => n.type === type && n.status === "unread").length
  }

  return (
    <div className="flex flex-wrap gap-2 p-1 bg-muted/50 rounded-lg">
      {filters.map((filter) => {
        const unreadCount = getCounts(filter.key)
        const isActive = activeFilter === filter.key
        
        return (
          <Button
            key={filter.key}
            variant={isActive ? "default" : "ghost"}
            size="sm"
            onClick={() => onFilterChange(filter.key)}
            className={cn(
              "gap-2 relative",
              isActive && "shadow-sm"
            )}
          >
            <filter.icon className="h-4 w-4" />
            {filter.label}
            {unreadCount > 0 && (
              <Badge 
                variant={isActive ? "secondary" : "destructive"} 
                className="h-5 min-w-5 px-1.5 text-xs"
              >
                {unreadCount}
              </Badge>
            )}
          </Button>
        )
      })}
    </div>
  )
}

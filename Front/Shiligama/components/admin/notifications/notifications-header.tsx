"use client"

import { Bell, CheckCheck } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"

interface NotificationsHeaderProps {
  unreadCount: number
  onMarkAllRead: () => void
}

export function NotificationsHeader({ unreadCount, onMarkAllRead }: NotificationsHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div className="flex items-center gap-3">
        <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-primary/10">
          <Bell className="h-6 w-6 text-primary" />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-foreground">Centro de Notificaciones</h1>
          <p className="text-sm text-muted-foreground">
            {unreadCount > 0 ? (
              <>
                Tienes <Badge variant="destructive" className="mx-1">{unreadCount}</Badge> 
                notificaciones sin leer
              </>
            ) : (
              "No tienes notificaciones pendientes"
            )}
          </p>
        </div>
      </div>
      
      {unreadCount > 0 && (
        <Button 
          variant="outline" 
          onClick={onMarkAllRead}
          className="gap-2"
        >
          <CheckCheck className="h-4 w-4" />
          Marcar todas como leídas
        </Button>
      )}
    </div>
  )
}

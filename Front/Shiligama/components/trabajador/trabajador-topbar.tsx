"use client"

import { Bell, RefreshCw } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"

interface TrabajadorTopbarProps {
  title: string
  onRefresh?: () => void
}

export function TrabajadorTopbar({ title, onRefresh }: TrabajadorTopbarProps) {
  return (
    <header className="sticky top-0 z-30 bg-background border-b border-border">
      <div className="flex h-16 items-center justify-between px-4 lg:px-6">
        {/* Page title */}
        <div className="flex items-center gap-4 pl-12 lg:pl-0">
          <h1 className="text-xl font-semibold text-foreground">{title}</h1>
          {onRefresh && (
            <Button variant="ghost" size="icon" onClick={onRefresh} className="h-8 w-8">
              <RefreshCw className="h-4 w-4" />
              <span className="sr-only">Actualizar</span>
            </Button>
          )}
        </div>

        {/* Right section */}
        <div className="flex items-center gap-3">
          {/* Notifications */}
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" size="icon" className="relative">
                <Bell className="h-5 w-5" />
                <Badge className="absolute -top-1 -right-1 h-5 w-5 rounded-full p-0 text-xs flex items-center justify-center">
                  3
                </Badge>
                <span className="sr-only">Notificaciones</span>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-72">
              <DropdownMenuLabel>Notificaciones</DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuItem className="flex flex-col items-start gap-1 py-3">
                <span className="font-medium text-sm">Nuevo pedido #1025</span>
                <span className="text-xs text-muted-foreground">Hace 2 minutos</span>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex flex-col items-start gap-1 py-3">
                <span className="font-medium text-sm">Pedido #1023 listo para entregar</span>
                <span className="text-xs text-muted-foreground">Hace 10 minutos</span>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex flex-col items-start gap-1 py-3">
                <span className="font-medium text-sm">Stock bajo: Leche Gloria</span>
                <span className="text-xs text-muted-foreground">Hace 30 minutos</span>
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>

          {/* User menu */}
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="relative h-9 w-9 rounded-full">
                <Avatar className="h-9 w-9">
                  <AvatarImage src="/avatars/worker.jpg" alt="Carlos Mendoza" />
                  <AvatarFallback className="bg-primary text-primary-foreground">CM</AvatarFallback>
                </Avatar>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-56">
              <DropdownMenuLabel>
                <div className="flex flex-col">
                  <span>Carlos Mendoza</span>
                  <span className="text-xs font-normal text-muted-foreground">Trabajador</span>
                </div>
              </DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuItem>Mi perfil</DropdownMenuItem>
              <DropdownMenuItem>Mis turnos</DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem className="text-destructive">Cerrar sesión</DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
    </header>
  )
}

"use client"

import { useState } from "react"
import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet"
import {
  LayoutDashboard,
  Package,
  Warehouse,
  TrendingUp,
  Truck,
  Users,
  FileBarChart,
  RotateCcw,
  Menu,
  ShoppingCart,
  LogOut,
  Settings,
  ClipboardList,
  Bell,
} from "lucide-react"

const navItems = [
  { label: "Dashboard", href: "/admin", icon: LayoutDashboard },
  { label: "Pedidos", href: "/admin/pedidos", icon: ClipboardList },
  { label: "Productos", href: "/admin/productos", icon: Package },
  { label: "Inventario", href: "/admin/inventario", icon: Warehouse },
  { label: "Ventas", href: "/admin/ventas", icon: TrendingUp },
  { label: "Proveedores", href: "/admin/proveedores", icon: Truck },
  { label: "Personal", href: "/admin/personal", icon: Users },
  { label: "Reportes", href: "/admin/reportes", icon: FileBarChart },
  { label: "Devoluciones", href: "/admin/devoluciones", icon: RotateCcw },
  { label: "Notificaciones", href: "/admin/notificaciones", icon: Bell },
]

function SidebarContent() {
  const pathname = usePathname()

  return (
    <div className="flex flex-col h-full bg-[#0D4525] text-white">
      {/* Logo */}
      <div className="p-6 border-b border-white/10">
        <Link href="/admin" className="flex items-center gap-3">
          <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-white/10">
            <ShoppingCart className="h-6 w-6 text-white" />
          </div>
          <div>
            <h1 className="text-xl font-bold tracking-tight">Shiligama</h1>
            <p className="text-xs text-white/60">Panel de Administración</p>
          </div>
        </Link>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
        {navItems.map((item) => {
          const isActive = pathname === item.href
          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                "flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors",
                isActive
                  ? "bg-white/15 text-white"
                  : "text-white/70 hover:text-white hover:bg-white/10"
              )}
            >
              <item.icon className="h-5 w-5" />
              {item.label}
            </Link>
          )
        })}
      </nav>

      {/* Footer */}
      <div className="p-4 border-t border-white/10 space-y-1">
        <Link
          href="/admin/configuracion"
          className="flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium text-white/70 hover:text-white hover:bg-white/10 transition-colors"
        >
          <Settings className="h-5 w-5" />
          Configuración
        </Link>
        <Link
          href="/"
          className="flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium text-white/70 hover:text-white hover:bg-white/10 transition-colors"
        >
          <LogOut className="h-5 w-5" />
          Cerrar sesión
        </Link>
      </div>
    </div>
  )
}

export function AdminSidebar() {
  const [open, setOpen] = useState(false)

  return (
    <>
      {/* Mobile menu button */}
      <Sheet open={open} onOpenChange={setOpen}>
        <SheetTrigger asChild>
          <Button
            variant="ghost"
            size="icon"
            className="fixed top-4 left-4 z-50 lg:hidden bg-[#0D4525] text-white hover:bg-[#0D4525]/90"
          >
            <Menu className="h-5 w-5" />
          </Button>
        </SheetTrigger>
        <SheetContent side="left" className="p-0 w-64 border-0">
          <SidebarContent />
        </SheetContent>
      </Sheet>

      {/* Desktop sidebar */}
      <aside className="hidden lg:block fixed inset-y-0 left-0 w-64 z-40">
        <SidebarContent />
      </aside>
    </>
  )
}

"use client"

import { useState } from "react"
import Link from "next/link"
import { Package, Truck, CheckCircle, Clock, ChevronDown, ChevronUp, ShoppingCart, User, LogOut, Settings, Tag, Menu, XCircle, Calendar } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { cn } from "@/lib/utils"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet"
import { useRouter } from "next/navigation"
import { useCart } from "@/hooks/use-cart"

type OrderStatus = "pendiente" | "en_preparacion" | "en_camino" | "entregado" | "cancelado"

interface Order {
  id: string
  date: string
  status: OrderStatus
  total: number
  items: {
    name: string
    quantity: number
    price: number
    image: string
  }[]
}

const orders: Order[] = [
  {
    id: "ORD-001",
    date: "2024-01-15",
    status: "entregado",
    total: 125.50,
    items: [
      { name: "Arroz Extra Costeño 5kg", quantity: 2, price: 28.90, image: "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=100&h=100&fit=crop" },
      { name: "Aceite Vegetal Primor 1L", quantity: 3, price: 12.90, image: "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=100&h=100&fit=crop" },
    ]
  },
  {
    id: "ORD-002",
    date: "2024-01-20",
    status: "en_camino",
    total: 89.70,
    items: [
      { name: "Leche Gloria Entera 1L", quantity: 6, price: 5.20, image: "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=100&h=100&fit=crop" },
      { name: "Azucar Rubia Casa Grande 1kg", quantity: 2, price: 4.50, image: "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?w=100&h=100&fit=crop" },
    ]
  },
  {
    id: "ORD-003",
    date: "2024-01-22",
    status: "pendiente",
    total: 56.80,
    items: [
      { name: "Fideos Don Vittorio Spaghetti 500g", quantity: 4, price: 3.80, image: "https://images.unsplash.com/photo-1551462147-ff29053bfc14?w=100&h=100&fit=crop" },
    ]
  },
]

const statusConfig: Record<OrderStatus, { label: string; icon: typeof Clock; color: string }> = {
  pendiente: {
    label: "Pendiente",
    icon: Clock,
    color: "bg-yellow-500/10 text-yellow-600 border-yellow-500/20",
  },
  en_preparacion: {
    label: "En preparacion",
    icon: Package,
    color: "bg-orange-500/10 text-orange-600 border-orange-500/20",
  },
  en_camino: {
    label: "En camino",
    icon: Truck,
    color: "bg-blue-500/10 text-blue-600 border-blue-500/20",
  },
  entregado: {
    label: "Entregado",
    icon: CheckCircle,
    color: "bg-primary/10 text-primary border-primary/20",
  },
  cancelado: {
    label: "Cancelado",
    icon: XCircle,
    color: "bg-destructive/10 text-destructive border-destructive/20",
  },
}

export default function MisPedidosPage() {
  const [expandedOrder, setExpandedOrder] = useState<string | null>(null)
  const [statusFilter, setStatusFilter] = useState<string>("todos")
  const [dateFrom, setDateFrom] = useState<string>("")
  const [dateTo, setDateTo] = useState<string>("")
  const router = useRouter()
  const { cartCount } = useCart()

  // Filter orders based on status and date range
  const filteredOrders = orders.filter((order) => {
    // Status filter
    if (statusFilter !== "todos" && order.status !== statusFilter) {
      return false
    }
    // Date range filter
    const orderDate = new Date(order.date)
    if (dateFrom) {
      const fromDate = new Date(dateFrom)
      if (orderDate < fromDate) return false
    }
    if (dateTo) {
      const toDate = new Date(dateTo)
      if (orderDate > toDate) return false
    }
    return true
  })

  const toggleOrder = (orderId: string) => {
    setExpandedOrder(expandedOrder === orderId ? null : orderId)
  }

  const handleLogout = () => {
    localStorage.removeItem("user")
    localStorage.removeItem("token")
    sessionStorage.clear()
    router.push("/")
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Simplified Header */}
      <header className="sticky top-0 z-50 w-full border-b bg-card shadow-sm">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="flex h-16 items-center justify-between gap-4">
            {/* Logo */}
            <Link href="/catalogo" className="flex items-center gap-2 shrink-0">
              <div className="flex h-10 w-10 items-center justify-center rounded-full bg-primary">
                <ShoppingCart className="h-5 w-5 text-primary-foreground" />
              </div>
              <span className="hidden sm:block text-xl font-bold text-foreground">
                Shiligama
              </span>
            </Link>

            {/* Page Title - Center on mobile */}
            <h1 className="text-lg font-semibold text-foreground sm:hidden">Mis Pedidos</h1>

            {/* Right Actions */}
            <div className="flex items-center gap-2">
              {/* Cart */}
              <Link href="/checkout">
                <Button variant="ghost" size="icon" className="relative">
                  <ShoppingCart className="h-5 w-5" />
                  {cartCount > 0 && (
                    <span className="absolute -right-1 -top-1 flex h-5 w-5 items-center justify-center rounded-full bg-primary text-[10px] font-bold text-primary-foreground">
                      {cartCount > 99 ? "99+" : cartCount}
                    </span>
                  )}
                  <span className="sr-only">Carrito</span>
                </Button>
              </Link>

              {/* User Menu */}
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="icon" className="rounded-full">
                    <div className="flex h-8 w-8 items-center justify-center rounded-full bg-primary/10">
                      <User className="h-4 w-4 text-primary" />
                    </div>
                    <span className="sr-only">Menu de usuario</span>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-56">
                  <DropdownMenuLabel>
                    <div className="flex flex-col gap-1">
                      <p className="text-sm font-medium">Juan Perez</p>
                      <p className="text-xs text-muted-foreground">juan@email.com</p>
                    </div>
                  </DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem asChild>
                    <Link href="/mis-pedidos">
                      <Package className="mr-2 h-4 w-4" />
                      Mis Pedidos
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/ofertas">
                      <Tag className="mr-2 h-4 w-4" />
                      Ofertas
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link href="/configuracion">
                      <Settings className="mr-2 h-4 w-4" />
                      Configuracion
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem className="text-destructive cursor-pointer" onClick={handleLogout}>
                    <LogOut className="mr-2 h-4 w-4" />
                    Cerrar Sesion
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>

              {/* Mobile Menu */}
              <Sheet>
                <SheetTrigger asChild>
                  <Button variant="ghost" size="icon" className="lg:hidden">
                    <Menu className="h-5 w-5" />
                    <span className="sr-only">Menu</span>
                  </Button>
                </SheetTrigger>
                <SheetContent side="right">
                  <SheetHeader>
                    <SheetTitle>Menu</SheetTitle>
                  </SheetHeader>
                  <nav className="flex flex-col gap-2 py-6">
                    <Link
                      href="/catalogo"
                      className="flex items-center gap-3 rounded-lg px-3 py-2 text-foreground hover:bg-secondary active:bg-primary/20 active:scale-[0.98] transition-all duration-150"
                    >
                      <ShoppingCart className="h-5 w-5" />
                      Catalogo
                    </Link>
                    <Link
                      href="/ofertas"
                      className="flex items-center gap-3 rounded-lg px-3 py-2 text-foreground hover:bg-secondary active:bg-primary/20 active:scale-[0.98] transition-all duration-150"
                    >
                      <Tag className="h-5 w-5" />
                      Ofertas
                    </Link>
                    <Link
                      href="/mis-pedidos"
                      className="flex items-center gap-3 rounded-lg px-3 py-2 text-foreground hover:bg-secondary active:bg-primary/20 active:scale-[0.98] transition-all duration-150"
                    >
                      <Package className="h-5 w-5" />
                      Mis Pedidos
                    </Link>
                  </nav>
                </SheetContent>
              </Sheet>
            </div>
          </div>
        </div>
      </header>

      {/* Content */}
      <main className="mx-auto max-w-3xl px-4 py-6 sm:px-6 lg:px-8">
        {/* Page Title - Desktop */}
        <h1 className="hidden sm:block text-2xl font-bold text-foreground mb-6">Mis Pedidos</h1>

        {/* Filters */}
        <Card className="mb-6">
          <CardContent className="pt-6">
            <div className="flex flex-col sm:flex-row gap-4">
              {/* Status Filter */}
              <div className="flex-1">
                <Label htmlFor="status-filter" className="text-sm font-medium mb-2 block">
                  Estado
                </Label>
                <Select value={statusFilter} onValueChange={setStatusFilter}>
                  <SelectTrigger id="status-filter">
                    <SelectValue placeholder="Todos los estados" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="todos">Todos los estados</SelectItem>
                    <SelectItem value="pendiente">Pendiente</SelectItem>
                    <SelectItem value="en_preparacion">En preparacion</SelectItem>
                    <SelectItem value="en_camino">En camino</SelectItem>
                    <SelectItem value="entregado">Entregado</SelectItem>
                    <SelectItem value="cancelado">Cancelado</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {/* Date From */}
              <div className="flex-1">
                <Label htmlFor="date-from" className="text-sm font-medium mb-2 block">
                  Desde
                </Label>
                <div className="relative">
                  <Input
                    id="date-from"
                    type="date"
                    value={dateFrom}
                    onChange={(e) => setDateFrom(e.target.value)}
                    className="w-full"
                  />
                </div>
              </div>

              {/* Date To */}
              <div className="flex-1">
                <Label htmlFor="date-to" className="text-sm font-medium mb-2 block">
                  Hasta
                </Label>
                <div className="relative">
                  <Input
                    id="date-to"
                    type="date"
                    value={dateTo}
                    onChange={(e) => setDateTo(e.target.value)}
                    className="w-full"
                  />
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {filteredOrders.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-16 text-center">
            <Package className="h-16 w-16 text-muted-foreground/50 mb-4" />
            <h3 className="text-lg font-medium text-foreground">
              {orders.length === 0 ? "No tienes pedidos" : "No se encontraron pedidos"}
            </h3>
            <p className="text-sm text-muted-foreground mt-1 mb-4">
              {orders.length === 0 
                ? "Cuando realices un pedido, aparecera aqui"
                : "Intenta cambiar los filtros de busqueda"
              }
            </p>
            {orders.length === 0 && (
              <Link href="/catalogo">
                <Button>Ir al catalogo</Button>
              </Link>
            )}
          </div>
        ) : (
          <div className="space-y-4">
            {filteredOrders.map((order) => {
              const status = statusConfig[order.status]
              const StatusIcon = status.icon
              const isExpanded = expandedOrder === order.id

              return (
                <Card key={order.id} className="overflow-hidden">
                  <CardHeader
                    className="cursor-pointer hover:bg-secondary/50 active:bg-secondary transition-colors"
                    onClick={() => toggleOrder(order.id)}
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex flex-col gap-1">
                        <CardTitle className="text-base font-semibold">
                          {order.id}
                        </CardTitle>
                        <p className="text-sm text-muted-foreground">
                          {new Date(order.date).toLocaleDateString("es-PE", {
                            year: "numeric",
                            month: "long",
                            day: "numeric",
                          })}
                        </p>
                      </div>
                      <div className="flex items-center gap-3">
                        <Badge
                          variant="outline"
                          className={cn("flex items-center gap-1", status.color)}
                        >
                          <StatusIcon className="h-3 w-3" />
                          {status.label}
                        </Badge>
                        {isExpanded ? (
                          <ChevronUp className="h-5 w-5 text-muted-foreground" />
                        ) : (
                          <ChevronDown className="h-5 w-5 text-muted-foreground" />
                        )}
                      </div>
                    </div>
                  </CardHeader>

                  {isExpanded && (
                    <CardContent className="pt-0">
                      <Separator className="mb-4" />
                      <div className="space-y-3">
                        {order.items.map((item, index) => (
                          <div key={index} className="flex items-center gap-3">
                            <img
                              src={item.image}
                              alt={item.name}
                              className="h-12 w-12 rounded-lg object-cover"
                            />
                            <div className="flex-1 min-w-0">
                              <p className="text-sm font-medium text-foreground truncate">
                                {item.name}
                              </p>
                              <p className="text-xs text-muted-foreground">
                                Cantidad: {item.quantity}
                              </p>
                            </div>
                            <p className="text-sm font-medium text-foreground">
                              S/. {(item.price * item.quantity).toFixed(2)}
                            </p>
                          </div>
                        ))}
                      </div>
                      <Separator className="my-4" />
                      <div className="flex items-center justify-between">
                        <span className="text-sm font-medium text-muted-foreground">
                          Total
                        </span>
                        <span className="text-lg font-bold text-primary">
                          S/. {order.total.toFixed(2)}
                        </span>
                      </div>
                    </CardContent>
                  )}
                </Card>
              )
            })}
          </div>
        )}
      </main>
    </div>
  )
}

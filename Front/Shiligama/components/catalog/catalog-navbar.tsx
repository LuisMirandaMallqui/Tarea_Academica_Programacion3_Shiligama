"use client"

import { ShoppingCart, Search, User, Menu, LogOut, Settings, Package, Tag } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
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
import Link from "next/link"
import { useRouter } from "next/navigation"

interface CatalogNavbarProps {
  cartCount: number
  searchQuery: string
  onSearchChange: (query: string) => void
}

export function CatalogNavbar({ cartCount, searchQuery, onSearchChange }: CatalogNavbarProps) {
  const router = useRouter()

  const handleLogout = () => {
    // Clear any session/auth data
    localStorage.removeItem("user")
    localStorage.removeItem("token")
    sessionStorage.clear()
    // Redirect to login page
    router.push("/")
  }

  return (
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

          {/* Search Bar - Desktop */}
          <div className="hidden md:flex flex-1 max-w-xl">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                type="search"
                placeholder="Buscar productos..."
                value={searchQuery}
                onChange={(e) => onSearchChange(e.target.value)}
                className="w-full pl-10 pr-4 bg-secondary/50 border-0 focus-visible:ring-2 focus-visible:ring-primary"
              />
            </div>
          </div>

          {/* Right Actions */}
          <div className="flex items-center gap-2">
            {/* Mobile Search */}
            <Sheet>
              <SheetTrigger asChild>
                <Button variant="ghost" size="icon" className="md:hidden">
                  <Search className="h-5 w-5" />
                  <span className="sr-only">Buscar</span>
                </Button>
              </SheetTrigger>
              <SheetContent side="top" className="h-auto">
                <SheetHeader>
                  <SheetTitle>Buscar productos</SheetTitle>
                </SheetHeader>
                <div className="py-4">
                  <div className="relative">
                    <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                    <Input
                      type="search"
                      placeholder="Buscar productos..."
                      value={searchQuery}
                      onChange={(e) => onSearchChange(e.target.value)}
                      className="w-full pl-10"
                      autoFocus
                    />
                  </div>
                </div>
              </SheetContent>
            </Sheet>

            {/* Cart */}
            <Link href="/checkout">
              <Button variant="ghost" size="icon" className="relative">
                <ShoppingCart className="h-5 w-5" />
                {cartCount > 0 && (
                  <Badge className="absolute -top-1 -right-1 h-5 w-5 rounded-full p-0 text-xs flex items-center justify-center">
                    {cartCount}
                  </Badge>
                )}
                <span className="sr-only">Carrito ({cartCount} items)</span>
              </Button>
            </Link>

            {/* User Menu */}
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="secondary" size="icon" className="h-10 w-10 border-0 shadow-sm rounded-md bg-secondary text-foreground hover:bg-secondary/80">
                  <User className="h-5 w-5" />
                  <span className="sr-only">Menu de usuario</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-56">
                {/* Desktop: Full menu */}
                <div className="hidden lg:block">
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
                </div>
                {/* Mobile/Tablet: Only logout */}
                <div className="lg:hidden">
                  <DropdownMenuLabel>
                    <div className="flex flex-col gap-1">
                      <p className="text-sm font-medium">Juan Perez</p>
                      <p className="text-xs text-muted-foreground">juan@email.com</p>
                    </div>
                  </DropdownMenuLabel>
                  <DropdownMenuSeparator />
                </div>
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
                  <Link
                    href="/configuracion"
                    className="flex items-center gap-3 rounded-lg px-3 py-2 text-foreground hover:bg-secondary active:bg-primary/20 active:scale-[0.98] transition-all duration-150"
                  >
                    <Settings className="h-5 w-5" />
                    Configuracion
                  </Link>
                </nav>
              </SheetContent>
            </Sheet>
          </div>
        </div>
      </div>
    </header>
  )
}

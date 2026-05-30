"use client"

import { useState, useMemo } from "react"
import { TrabajadorSidebar } from "@/components/trabajador/trabajador-sidebar"
import { TrabajadorTopbar } from "@/components/trabajador/trabajador-topbar"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import { products, Product } from "@/lib/products"
import { Plus, Trash2, Search, Receipt, CheckCircle, AlertCircle, Banknote, Smartphone, CreditCard } from "lucide-react"
import { toast } from "sonner"

interface LineaVenta {
  id: string
  codigo: string
  nombre: string
  cantidad: number
  precioUnitario: number
  subtotal: number
  stock: number
}

type MetodoPago = "efectivo" | "yape" | "plin" | "tarjeta"

// ✅ Mock sale ID - sin localStorage
const SALE_ID_MOCK = "VTA-0014"

export default function RegistrarVentaPage() {
  const [saleId] = useState(SALE_ID_MOCK)
  const [lineas, setLineas] = useState<LineaVenta[]>([])
  const [metodoPago, setMetodoPago] = useState<MetodoPago | null>(null)
  const [montoRecibido, setMontoRecibido] = useState<string>("")
  const [isAddModalOpen, setIsAddModalOpen] = useState(false)
  const [isConfirmDialogOpen, setIsConfirmDialogOpen] = useState(false)
  const [isBoletaDialogOpen, setIsBoletaDialogOpen] = useState(false)
  const [searchTerm, setSearchTerm] = useState("")
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [cantidad, setCantidad] = useState<number>(1)

  // Calculate totals
  const subtotal = useMemo(() => lineas.reduce((sum, l) => sum + l.subtotal, 0), [lineas])
  const igv = useMemo(() => subtotal * 0.18, [subtotal])
  const total = useMemo(() => subtotal + igv, [subtotal, igv])
  const vuelto = useMemo(() => {
    const monto = parseFloat(montoRecibido) || 0
    return monto > total ? monto - total : 0
  }, [montoRecibido, total])

  // Filter products for modal search
  const filteredProducts = useMemo(() => {
    if (!searchTerm) return products.filter(p => p.stock > 0)
    const term = searchTerm.toLowerCase()
    return products.filter(
      p => p.stock > 0 && (
        p.name.toLowerCase().includes(term) ||
        p.id.toString().includes(term)
      )
    )
  }, [searchTerm])

  // Add line to sale
  const handleAddLine = () => {
    if (!selectedProduct || cantidad < 1) return

    // Check if product already in lines
    const existingLine = lineas.find(l => l.codigo === selectedProduct.id.toString())
    if (existingLine) {
      const newCantidad = existingLine.cantidad + cantidad
      if (newCantidad > selectedProduct.stock) {
        toast.error("Stock insuficiente", {
          description: `Solo hay ${selectedProduct.stock} unidades disponibles`
        })
        return
      }
      setLineas(lineas.map(l =>
        l.codigo === selectedProduct.id.toString()
          ? { ...l, cantidad: newCantidad, subtotal: newCantidad * l.precioUnitario }
          : l
      ))
    } else {
      if (cantidad > selectedProduct.stock) {
        toast.error("Stock insuficiente", {
          description: `Solo hay ${selectedProduct.stock} unidades disponibles`
        })
        return
      }
      const newLine: LineaVenta = {
        id: crypto.randomUUID(),
        codigo: selectedProduct.id.toString(),
        nombre: selectedProduct.name,
        cantidad,
        precioUnitario: selectedProduct.price,
        subtotal: cantidad * selectedProduct.price,
        stock: selectedProduct.stock,
      }
      setLineas([...lineas, newLine])
    }

    // Reset modal
    setSelectedProduct(null)
    setCantidad(1)
    setSearchTerm("")
    setIsAddModalOpen(false)
    toast.success("Producto agregado")
  }

  // Remove line
  const handleRemoveLine = (id: string) => {
    setLineas(lineas.filter(l => l.id !== id))
  }

  // Confirm sale
  const handleConfirmSale = () => {
    if (lineas.length === 0) {
      toast.error("Agregue al menos un producto")
      return
    }
    if (!metodoPago) {
      toast.error("Seleccione un método de pago")
      return
    }
    if (metodoPago === "efectivo" && (!montoRecibido || parseFloat(montoRecibido) < total)) {
      toast.error("El monto recibido debe ser mayor o igual al total")
      return
    }

    // Validate stock (mock)
    const stockErrors = lineas.filter(l => l.cantidad > l.stock)
    if (stockErrors.length > 0) {
      toast.error("Stock insuficiente", {
        description: `Productos sin stock: ${stockErrors.map(l => l.nombre).join(", ")}`
      })
      return
    }

    setIsConfirmDialogOpen(true)
  }

  // Process confirmed sale
  const processSale = () => {
    toast.success("Venta confirmada", {
      description: `${saleId} - Total: S/ ${total.toFixed(2)}`
    })

    setIsConfirmDialogOpen(false)

    // Reset form for next sale
    setLineas([])
    setMetodoPago(null)
    setMontoRecibido("")
  }

  // Generate boleta (mock SUNAT)
  const handleGenerateBoleta = () => {
    if (lineas.length === 0) {
      toast.error("Agregue al menos un producto")
      return
    }
    setIsBoletaDialogOpen(true)
  }

  const processBoletaGeneration = () => {
    // Mock APISUNAT call
    const boletaNumber = `B001-${Math.floor(Math.random() * 99999).toString().padStart(5, "0")}`

    toast.success("Boleta generada", {
      description: `Número: ${boletaNumber} (SUNAT Mock)`
    })

    setIsBoletaDialogOpen(false)
  }

  const metodosPago: { value: MetodoPago; label: string; icon: React.ReactNode }[] = [
    { value: "efectivo", label: "Efectivo", icon: <Banknote className="h-4 w-4" /> },
    { value: "yape", label: "Yape", icon: <Smartphone className="h-4 w-4" /> },
    { value: "plin", label: "Plin", icon: <Smartphone className="h-4 w-4" /> },
    { value: "tarjeta", label: "Tarjeta", icon: <CreditCard className="h-4 w-4" /> },
  ]

  return (
    <div className="flex h-screen bg-background">
      <TrabajadorSidebar />

      <div className="flex-1 flex flex-col overflow-hidden lg:ml-64">
        <TrabajadorTopbar />

        <main className="flex-1 overflow-y-auto p-4 lg:p-6 space-y-6">
          {/* Page header */}
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div>
              <h1 className="text-2xl font-bold text-foreground">Registrar Venta</h1>
              <p className="text-muted-foreground">
                Registra una nueva venta presencial
              </p>
            </div>
            <Badge variant="outline" className="text-lg px-4 py-2 font-mono w-fit">
              {saleId}
            </Badge>
          </div>

          {/* Sales Table Card */}
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle>Líneas de Venta</CardTitle>
              <Button onClick={() => setIsAddModalOpen(true)} className="bg-[#0D4525] hover:bg-[#0D4525]/90">
                <Plus className="h-4 w-4 mr-2" />
                Agregar Producto
              </Button>
            </CardHeader>
            <CardContent>
              {lineas.length === 0 ? (
                <div className="text-center py-12 text-muted-foreground">
                  <Receipt className="h-12 w-12 mx-auto mb-4 opacity-50" />
                  <p>No hay productos agregados</p>
                  <p className="text-sm">Haz clic en "Agregar Producto" para comenzar</p>
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Código</TableHead>
                        <TableHead>Nombre</TableHead>
                        <TableHead className="text-center">Cantidad</TableHead>
                        <TableHead className="text-right">Precio Unit.</TableHead>
                        <TableHead className="text-right">Subtotal</TableHead>
                        <TableHead className="w-[60px]"></TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {lineas.map((linea) => (
                        <TableRow key={linea.id}>
                          <TableCell className="font-mono">{linea.codigo}</TableCell>
                          <TableCell className="font-medium">{linea.nombre}</TableCell>
                          <TableCell className="text-center">{linea.cantidad}</TableCell>
                          <TableCell className="text-right">S/ {linea.precioUnitario.toFixed(2)}</TableCell>
                          <TableCell className="text-right font-medium">S/ {linea.subtotal.toFixed(2)}</TableCell>
                          <TableCell>
                            <Button
                              variant="ghost"
                              size="icon"
                              className="h-8 w-8 text-destructive hover:text-destructive hover:bg-destructive/10"
                              onClick={() => handleRemoveLine(linea.id)}
                            >
                              <Trash2 className="h-4 w-4" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </div>
              )}
            </CardContent>
          </Card>

          {/* Summary and Payment */}
          <div className="grid gap-6 lg:grid-cols-2">
            {/* Summary Card */}
            <Card>
              <CardHeader>
                <CardTitle>Resumen</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">Subtotal</span>
                  <span>S/ {subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">IGV (18%)</span>
                  <span>S/ {igv.toFixed(2)}</span>
                </div>
                <div className="border-t pt-4 flex justify-between text-lg font-bold">
                  <span>Total</span>
                  <span className="text-[#0D4525]">S/ {total.toFixed(2)}</span>
                </div>
              </CardContent>
            </Card>

            {/* Payment Method Card */}
            <Card>
              <CardHeader>
                <CardTitle>Método de Pago</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex flex-wrap gap-2">
                  {metodosPago.map((metodo) => (
                    <Badge
                      key={metodo.value}
                      variant={metodoPago === metodo.value ? "default" : "outline"}
                      className={`cursor-pointer px-4 py-2 text-sm flex items-center gap-2 transition-colors ${metodoPago === metodo.value
                          ? "bg-[#0D4525] hover:bg-[#0D4525]/90"
                          : "hover:bg-muted"
                        }`}
                      onClick={() => setMetodoPago(metodo.value)}
                    >
                      {metodo.icon}
                      {metodo.label}
                    </Badge>
                  ))}
                </div>

                {metodoPago === "efectivo" && (
                  <div className="space-y-4 pt-4 border-t">
                    <div className="space-y-2">
                      <Label htmlFor="montoRecibido">Monto Recibido</Label>
                      <div className="relative">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground">S/</span>
                        <Input
                          id="montoRecibido"
                          type="number"
                          step="0.01"
                          min="0"
                          value={montoRecibido}
                          onChange={(e) => setMontoRecibido(e.target.value)}
                          className="pl-10"
                          placeholder="0.00"
                        />
                      </div>
                    </div>
                    <div className="flex justify-between items-center p-3 bg-muted rounded-lg">
                      <span className="font-medium">Vuelto</span>
                      <span className="text-lg font-bold text-[#0D4525]">
                        S/ {vuelto.toFixed(2)}
                      </span>
                    </div>
                  </div>
                )}
              </CardContent>
            </Card>
          </div>

          {/* Action Buttons */}
          <div className="flex flex-col sm:flex-row gap-4 justify-end">
            <Button
              variant="outline"
              onClick={handleGenerateBoleta}
              disabled={lineas.length === 0}
              className="sm:order-1"
            >
              <Receipt className="h-4 w-4 mr-2" />
              Generar Boleta
            </Button>
            <Button
              onClick={handleConfirmSale}
              disabled={lineas.length === 0 || !metodoPago}
              className="bg-[#0D4525] hover:bg-[#0D4525]/90 sm:order-2"
            >
              <CheckCircle className="h-4 w-4 mr-2" />
              Confirmar Venta
            </Button>
          </div>
        </main>
      </div>

      {/* Add Product Modal */}
      <Dialog open={isAddModalOpen} onOpenChange={setIsAddModalOpen}>
        <DialogContent className="sm:max-w-lg">
          <DialogHeader>
            <DialogTitle>Agregar Producto</DialogTitle>
            <DialogDescription>
              Busca por nombre o código de barras
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-4">
            {/* Search */}
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Buscar producto..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>

            {/* Product List */}
            <div className="max-h-[200px] overflow-y-auto border rounded-lg">
              {filteredProducts.length === 0 ? (
                <div className="p-4 text-center text-muted-foreground text-sm">
                  No se encontraron productos
                </div>
              ) : (
                filteredProducts.map((product) => (
                  <div
                    key={product.id}
                    onClick={() => setSelectedProduct(product)}
                    className={`p-3 border-b last:border-b-0 cursor-pointer transition-colors ${selectedProduct?.id === product.id
                        ? "bg-[#0D4525]/10"
                        : "hover:bg-muted"
                      }`}
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <p className="font-medium text-sm">{product.name}</p>
                        <p className="text-xs text-muted-foreground">
                          Código: {product.id} | Stock: {product.stock}
                        </p>
                      </div>
                      <span className="font-medium text-sm">
                        S/ {product.price.toFixed(2)}
                      </span>
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* Selected Product Details */}
            {selectedProduct && (
              <div className="p-4 bg-muted rounded-lg space-y-3">
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">Producto seleccionado</span>
                  <span className="font-medium text-sm">{selectedProduct.name}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">Código</span>
                  <span className="font-mono text-sm">{selectedProduct.id}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">Precio unitario</span>
                  <span className="font-medium text-sm">S/ {selectedProduct.price.toFixed(2)}</span>
                </div>
                <div className="flex justify-between items-center">
                  <Label htmlFor="cantidad" className="text-sm text-muted-foreground">Cantidad</Label>
                  <Input
                    id="cantidad"
                    type="number"
                    min="1"
                    max={selectedProduct.stock}
                    value={cantidad}
                    onChange={(e) => setCantidad(Math.max(1, parseInt(e.target.value) || 1))}
                    className="w-24 text-center"
                  />
                </div>
                <div className="flex justify-between pt-2 border-t">
                  <span className="font-medium">Subtotal</span>
                  <span className="font-bold text-[#0D4525]">
                    S/ {(cantidad * selectedProduct.price).toFixed(2)}
                  </span>
                </div>
              </div>
            )}
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setIsAddModalOpen(false)}>
              Cancelar
            </Button>
            <Button
              onClick={handleAddLine}
              disabled={!selectedProduct || cantidad < 1}
              className="bg-[#0D4525] hover:bg-[#0D4525]/90"
            >
              Agregar Línea
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Confirm Sale Dialog */}
      <AlertDialog open={isConfirmDialogOpen} onOpenChange={setIsConfirmDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle className="flex items-center gap-2">
              <CheckCircle className="h-5 w-5 text-[#0D4525]" />
              Confirmar Venta
            </AlertDialogTitle>
            <AlertDialogDescription>
              Se registrará la venta <strong>{saleId}</strong> por un total de{" "}
              <strong>S/ {total.toFixed(2)}</strong>. Se actualizará el inventario
              automáticamente.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction
              onClick={processSale}
              className="bg-[#0D4525] hover:bg-[#0D4525]/90"
            >
              Confirmar
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Generate Boleta Dialog */}
      <AlertDialog open={isBoletaDialogOpen} onOpenChange={setIsBoletaDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle className="flex items-center gap-2">
              <Receipt className="h-5 w-5 text-[#0D4525]" />
              Generar Boleta Electrónica
            </AlertDialogTitle>
            <AlertDialogDescription>
              <div className="space-y-2">
                <p>Se enviará la boleta a SUNAT por un total de <strong>S/ {total.toFixed(2)}</strong>.</p>
                <div className="flex items-center gap-2 text-amber-600 bg-amber-50 p-2 rounded text-sm">
                  <AlertCircle className="h-4 w-4" />
                  <span>Integración APISUNAT en modo mock</span>
                </div>
              </div>
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction
              onClick={processBoletaGeneration}
              className="bg-[#0D4525] hover:bg-[#0D4525]/90"
            >
              Generar Boleta
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  )
}

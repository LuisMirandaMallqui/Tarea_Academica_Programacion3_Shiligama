"use client"

import { useState, useEffect } from "react"
import { format } from "date-fns"
import { es } from "date-fns/locale"
import { CalendarIcon, Search, Check, ChevronsUpDown } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command"
import { Calendar } from "@/components/ui/calendar"
import { cn } from "@/lib/utils"
import type { ReturnItem, ReturnReason } from "@/app/admin/devoluciones/page"

interface Product {
  id: string
  name: string
  code: string
  price: number
}

interface ReturnDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  products: Product[]
  editingReturn: ReturnItem | null
  onSubmit: (data: Omit<ReturnItem, "id">) => void
}

export function ReturnDialog({
  open,
  onOpenChange,
  products,
  editingReturn,
  onSubmit,
}: ReturnDialogProps) {
  const [productOpen, setProductOpen] = useState(false)
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [quantity, setQuantity] = useState("")
  const [reason, setReason] = useState<ReturnReason | "">("")
  const [observations, setObservations] = useState("")
  const [date, setDate] = useState<Date>(new Date())
  const [errors, setErrors] = useState<Record<string, string>>({})

  // Reset form when dialog opens/closes or editing changes
  useEffect(() => {
    if (open) {
      if (editingReturn) {
        const product = products.find(p => p.code === editingReturn.productCode)
        setSelectedProduct(product || null)
        setQuantity(String(editingReturn.quantity))
        setReason(editingReturn.reason)
        setObservations(editingReturn.observations || "")
        setDate(new Date(editingReturn.date))
      } else {
        setSelectedProduct(null)
        setQuantity("")
        setReason("")
        setObservations("")
        setDate(new Date())
      }
      setErrors({})
    }
  }, [open, editingReturn, products])

  const calculateAmount = () => {
    if (!selectedProduct || !quantity) return 0
    return selectedProduct.price * parseInt(quantity)
  }

  const validate = () => {
    const newErrors: Record<string, string> = {}
    
    if (!selectedProduct) newErrors.product = "Selecciona un producto"
    if (!quantity || parseInt(quantity) < 1) newErrors.quantity = "Ingresa una cantidad válida"
    if (!reason) newErrors.reason = "Selecciona un motivo"
    if (!date) newErrors.date = "Selecciona una fecha"

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!validate() || !selectedProduct) return

    onSubmit({
      date: format(date, "yyyy-MM-dd"),
      product: selectedProduct.name,
      productCode: selectedProduct.code,
      quantity: parseInt(quantity),
      reason: reason as ReturnReason,
      registeredBy: "Admin Usuario", // In a real app, get from auth context
      amount: calculateAmount(),
      observations: observations || undefined,
    })
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>
            {editingReturn ? "Editar Devolución" : "Registrar Devolución"}
          </DialogTitle>
          <DialogDescription>
            {editingReturn 
              ? "Modifica los datos de la devolución registrada"
              : "Ingresa los datos de la devolución o merma"
            }
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Product Selector */}
          <div className="space-y-2">
            <Label>Producto *</Label>
            <Popover open={productOpen} onOpenChange={setProductOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={productOpen}
                  className={cn(
                    "w-full justify-between",
                    !selectedProduct && "text-muted-foreground",
                    errors.product && "border-destructive"
                  )}
                >
                  {selectedProduct ? (
                    <span className="truncate">
                      {selectedProduct.name} ({selectedProduct.code})
                    </span>
                  ) : (
                    "Buscar producto..."
                  )}
                  <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-full p-0" align="start">
                <Command>
                  <CommandInput placeholder="Buscar producto..." />
                  <CommandList>
                    <CommandEmpty>No se encontró el producto.</CommandEmpty>
                    <CommandGroup>
                      {products.map((product) => (
                        <CommandItem
                          key={product.id}
                          value={product.name}
                          onSelect={() => {
                            setSelectedProduct(product)
                            setProductOpen(false)
                          }}
                        >
                          <Check
                            className={cn(
                              "mr-2 h-4 w-4",
                              selectedProduct?.id === product.id ? "opacity-100" : "opacity-0"
                            )}
                          />
                          <div className="flex flex-col">
                            <span>{product.name}</span>
                            <span className="text-xs text-muted-foreground">
                              {product.code} - S/. {product.price.toFixed(2)}
                            </span>
                          </div>
                        </CommandItem>
                      ))}
                    </CommandGroup>
                  </CommandList>
                </Command>
              </PopoverContent>
            </Popover>
            {errors.product && (
              <p className="text-sm text-destructive">{errors.product}</p>
            )}
          </div>

          {/* Quantity and Reason */}
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="quantity">Cantidad *</Label>
              <Input
                id="quantity"
                type="number"
                min="1"
                value={quantity}
                onChange={(e) => setQuantity(e.target.value)}
                placeholder="0"
                className={cn(errors.quantity && "border-destructive")}
              />
              {errors.quantity && (
                <p className="text-sm text-destructive">{errors.quantity}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label>Motivo *</Label>
              <Select value={reason} onValueChange={(v) => setReason(v as ReturnReason)}>
                <SelectTrigger className={cn(errors.reason && "border-destructive")}>
                  <SelectValue placeholder="Seleccionar" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Vencido">Vencido</SelectItem>
                  <SelectItem value="Dañado">Dañado</SelectItem>
                  <SelectItem value="Error de pedido">Error de pedido</SelectItem>
                  <SelectItem value="Otro">Otro</SelectItem>
                </SelectContent>
              </Select>
              {errors.reason && (
                <p className="text-sm text-destructive">{errors.reason}</p>
              )}
            </div>
          </div>

          {/* Date */}
          <div className="space-y-2">
            <Label>Fecha *</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn(
                    "w-full justify-start text-left font-normal",
                    !date && "text-muted-foreground",
                    errors.date && "border-destructive"
                  )}
                >
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {date ? format(date, "PPP", { locale: es }) : "Seleccionar fecha"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar
                  mode="single"
                  selected={date}
                  onSelect={(d) => d && setDate(d)}
                  initialFocus
                />
              </PopoverContent>
            </Popover>
            {errors.date && (
              <p className="text-sm text-destructive">{errors.date}</p>
            )}
          </div>

          {/* Observations */}
          <div className="space-y-2">
            <Label htmlFor="observations">Observaciones</Label>
            <Textarea
              id="observations"
              value={observations}
              onChange={(e) => setObservations(e.target.value)}
              placeholder="Detalles adicionales sobre la devolución..."
              rows={3}
            />
          </div>

          {/* Amount Preview */}
          {selectedProduct && quantity && parseInt(quantity) > 0 && (
            <div className="p-4 rounded-lg bg-muted/50 border border-border">
              <div className="flex justify-between items-center">
                <span className="text-sm text-muted-foreground">Monto afectado:</span>
                <span className="text-lg font-bold text-destructive">
                  -S/. {calculateAmount().toFixed(2)}
                </span>
              </div>
              <p className="text-xs text-muted-foreground mt-1">
                {quantity} x S/. {selectedProduct.price.toFixed(2)}
              </p>
            </div>
          )}

          <DialogFooter className="gap-2">
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
              Cancelar
            </Button>
            <Button type="submit">
              {editingReturn ? "Guardar Cambios" : "Registrar Devolución"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

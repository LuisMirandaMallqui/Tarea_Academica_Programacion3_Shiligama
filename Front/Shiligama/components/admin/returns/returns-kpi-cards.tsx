import { Card, CardContent } from "@/components/ui/card"
import { RotateCcw, TrendingDown, Package } from "lucide-react"

interface ReturnsKpiCardsProps {
  totalReturns: number
  totalAmount: number
  mostReturnedProduct: { name: string; count: number } | null
}

export function ReturnsKpiCards({
  totalReturns,
  totalAmount,
  mostReturnedProduct,
}: ReturnsKpiCardsProps) {
  return (
    <div className="grid gap-4 md:grid-cols-3">
      {/* Total Returns */}
      <Card className="border-border">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-muted-foreground">
                Devoluciones este mes
              </p>
              <p className="text-3xl font-bold text-foreground mt-1">
                {totalReturns}
              </p>
              <p className="text-xs text-muted-foreground mt-1">
                registros totales
              </p>
            </div>
            <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center">
              <RotateCcw className="h-6 w-6 text-primary" />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Total Amount Lost */}
      <Card className="border-border">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-muted-foreground">
                Total Mermas
              </p>
              <p className="text-3xl font-bold text-foreground mt-1">
                S/. {totalAmount.toFixed(2)}
              </p>
              <p className="text-xs text-destructive mt-1">
                pérdida acumulada
              </p>
            </div>
            <div className="h-12 w-12 rounded-full bg-destructive/10 flex items-center justify-center">
              <TrendingDown className="h-6 w-6 text-destructive" />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Most Returned Product */}
      <Card className="border-border">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-muted-foreground">
                Producto más devuelto
              </p>
              {mostReturnedProduct ? (
                <>
                  <p className="text-lg font-bold text-foreground mt-1 truncate">
                    {mostReturnedProduct.name}
                  </p>
                  <p className="text-xs text-amber-600 mt-1">
                    {mostReturnedProduct.count} unidades
                  </p>
                </>
              ) : (
                <p className="text-lg font-medium text-muted-foreground mt-1">
                  Sin datos
                </p>
              )}
            </div>
            <div className="h-12 w-12 rounded-full bg-amber-100 flex items-center justify-center flex-shrink-0 ml-4">
              <Package className="h-6 w-6 text-amber-600" />
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

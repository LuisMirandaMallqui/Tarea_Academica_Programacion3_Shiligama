import { ShoppingCart, Truck, Shield } from "lucide-react"

export function BrandedPanel() {
  return (
    <div className="hidden lg:flex lg:w-[480px] xl:w-[540px] bg-[#1A6B3C] flex-col justify-between p-10 text-white relative overflow-hidden">
      {/* Background pattern */}
      <div className="absolute inset-0 opacity-10">
        <div className="absolute top-10 left-10 w-32 h-32 rounded-full border-2 border-white" />
        <div className="absolute bottom-20 right-10 w-24 h-24 rounded-full border-2 border-white" />
        <div className="absolute top-1/2 left-1/4 w-16 h-16 rounded-full border-2 border-white" />
      </div>

      {/* Logo and brand */}
      <div className="relative z-10">
        <div className="flex items-center gap-3 mb-2">
          <div className="w-12 h-12 bg-white/20 rounded-xl flex items-center justify-center backdrop-blur-sm">
            <ShoppingCart className="w-7 h-7 text-white" />
          </div>
          <div>
            <h1 className="text-3xl font-bold tracking-tight">Shiligama</h1>
            <p className="text-white/80 text-sm">Sistema de Gestión</p>
          </div>
        </div>
      </div>

      {/* Feature highlights */}
      <div className="relative z-10 space-y-6">
        <h2 className="text-xl font-semibold mb-6">
          Tu minimarket, bajo control total
        </h2>

        <FeatureItem
          icon={<ShoppingCart className="w-5 h-5" />}
          title="Gestión de Inventario"
          description="Control completo de tus productos, stock y precios en tiempo real"
        />

        <FeatureItem
          icon={<Truck className="w-5 h-5" />}
          title="Delivery Integrado"
          description="Gestiona pedidos y entregas a domicilio de manera eficiente"
        />

        <FeatureItem
          icon={<Shield className="w-5 h-5" />}
          title="Seguridad Avanzada"
          description="Roles y permisos personalizados para cada tipo de usuario"
        />
      </div>

      {/* Footer */}
      <div className="relative z-10">
        <p className="text-white/60 text-sm">
          © 2026 Shiligama. Todos los derechos reservados.
        </p>
      </div>
    </div>
  )
}

function FeatureItem({
  icon,
  title,
  description,
}: {
  icon: React.ReactNode
  title: string
  description: string
}) {
  return (
    <div className="flex gap-4 items-start">
      <div className="w-10 h-10 bg-white/15 rounded-lg flex items-center justify-center flex-shrink-0 backdrop-blur-sm">
        {icon}
      </div>
      <div>
        <h3 className="font-medium text-white">{title}</h3>
        <p className="text-white/70 text-sm leading-relaxed">{description}</p>
      </div>
    </div>
  )
}

"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Switch } from "@/components/ui/switch"
import { Separator } from "@/components/ui/separator"
import { 
  Settings, 
  Store, 
  Bell, 
  Shield, 
  Palette, 
  Save, 
  Check,
  Clock,
  MapPin,
  Phone
} from "lucide-react"

export default function AdminConfiguracionPage() {
  const [saved, setSaved] = useState(false)
  
  const [storeConfig, setStoreConfig] = useState({
    storeName: "Minimarket Shiligama",
    address: "Av. Principal 123, Lima",
    phone: "987654321",
    openTime: "08:00",
    closeTime: "22:00",
  })

  const [notifications, setNotifications] = useState({
    newOrders: true,
    lowStock: true,
    returns: true,
    dailyReport: false,
  })

  const handleStoreChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setStoreConfig(prev => ({ ...prev, [name]: value }))
    setSaved(false)
  }

  const handleNotificationToggle = (key: keyof typeof notifications) => {
    setNotifications(prev => ({ ...prev, [key]: !prev[key] }))
    setSaved(false)
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setSaved(true)
    setTimeout(() => setSaved(false), 3000)
  }

  return (
    <div className="flex min-h-screen bg-muted/30">
      <AdminSidebar />
      
      <div className="flex-1 flex flex-col lg:ml-64">
        <AdminTopbar />
        
        <main className="flex-1 p-4 lg:p-6 space-y-6">
          {/* Header */}
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-[#0D4525]/10">
              <Settings className="h-6 w-6 text-[#0D4525]" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-foreground">Configuracion</h1>
              <p className="text-muted-foreground">Administra la configuracion del sistema</p>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Store Information */}
            <Card>
              <CardHeader>
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#0D4525]/10">
                    <Store className="h-5 w-5 text-[#0D4525]" />
                  </div>
                  <div>
                    <CardTitle>Informacion de la Tienda</CardTitle>
                    <CardDescription>Datos generales del minimarket</CardDescription>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid gap-4 sm:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="storeName">Nombre de la tienda</Label>
                    <Input
                      id="storeName"
                      name="storeName"
                      value={storeConfig.storeName}
                      onChange={handleStoreChange}
                      placeholder="Nombre del minimarket"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="phone">Telefono</Label>
                    <div className="relative">
                      <Phone className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                      <Input
                        id="phone"
                        name="phone"
                        value={storeConfig.phone}
                        onChange={handleStoreChange}
                        placeholder="987654321"
                        className="pl-10"
                      />
                    </div>
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="address">Direccion</Label>
                  <div className="relative">
                    <MapPin className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                    <Input
                      id="address"
                      name="address"
                      value={storeConfig.address}
                      onChange={handleStoreChange}
                      placeholder="Direccion completa"
                      className="pl-10"
                    />
                  </div>
                </div>
                <div className="grid gap-4 sm:grid-cols-2">
                  <div className="space-y-2">
                    <Label htmlFor="openTime">Hora de apertura</Label>
                    <div className="relative">
                      <Clock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                      <Input
                        id="openTime"
                        name="openTime"
                        type="time"
                        value={storeConfig.openTime}
                        onChange={handleStoreChange}
                        className="pl-10"
                      />
                    </div>
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="closeTime">Hora de cierre</Label>
                    <div className="relative">
                      <Clock className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                      <Input
                        id="closeTime"
                        name="closeTime"
                        type="time"
                        value={storeConfig.closeTime}
                        onChange={handleStoreChange}
                        className="pl-10"
                      />
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Notifications */}
            <Card>
              <CardHeader>
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#0D4525]/10">
                    <Bell className="h-5 w-5 text-[#0D4525]" />
                  </div>
                  <div>
                    <CardTitle>Notificaciones</CardTitle>
                    <CardDescription>Configura las alertas del sistema</CardDescription>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Nuevos pedidos</Label>
                    <p className="text-sm text-muted-foreground">
                      Recibir alerta cuando llegue un nuevo pedido
                    </p>
                  </div>
                  <Switch
                    checked={notifications.newOrders}
                    onCheckedChange={() => handleNotificationToggle("newOrders")}
                  />
                </div>
                <Separator />
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Stock bajo</Label>
                    <p className="text-sm text-muted-foreground">
                      Alertar cuando un producto tenga stock bajo
                    </p>
                  </div>
                  <Switch
                    checked={notifications.lowStock}
                    onCheckedChange={() => handleNotificationToggle("lowStock")}
                  />
                </div>
                <Separator />
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Devoluciones</Label>
                    <p className="text-sm text-muted-foreground">
                      Notificar sobre solicitudes de devolucion
                    </p>
                  </div>
                  <Switch
                    checked={notifications.returns}
                    onCheckedChange={() => handleNotificationToggle("returns")}
                  />
                </div>
                <Separator />
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Reporte diario</Label>
                    <p className="text-sm text-muted-foreground">
                      Enviar resumen de ventas al final del dia
                    </p>
                  </div>
                  <Switch
                    checked={notifications.dailyReport}
                    onCheckedChange={() => handleNotificationToggle("dailyReport")}
                  />
                </div>
              </CardContent>
            </Card>

            {/* Security */}
            <Card>
              <CardHeader>
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#0D4525]/10">
                    <Shield className="h-5 w-5 text-[#0D4525]" />
                  </div>
                  <div>
                    <CardTitle>Seguridad</CardTitle>
                    <CardDescription>Opciones de seguridad del sistema</CardDescription>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Autenticacion de dos factores</Label>
                    <p className="text-sm text-muted-foreground">
                      Requiere verificacion adicional al iniciar sesion
                    </p>
                  </div>
                  <Switch />
                </div>
                <Separator />
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Sesiones activas</Label>
                    <p className="text-sm text-muted-foreground">
                      Cerrar sesion automaticamente despues de inactividad
                    </p>
                  </div>
                  <Switch defaultChecked />
                </div>
              </CardContent>
            </Card>

            {/* Appearance */}
            <Card>
              <CardHeader>
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#0D4525]/10">
                    <Palette className="h-5 w-5 text-[#0D4525]" />
                  </div>
                  <div>
                    <CardTitle>Apariencia</CardTitle>
                    <CardDescription>Personaliza la interfaz del sistema</CardDescription>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label>Modo oscuro</Label>
                    <p className="text-sm text-muted-foreground">
                      Activar tema oscuro en la interfaz
                    </p>
                  </div>
                  <Switch />
                </div>
              </CardContent>
            </Card>

            {/* Save Button */}
            <div className="flex justify-end">
              <Button type="submit" className="gap-2 bg-[#0D4525] hover:bg-[#0D4525]/90" disabled={saved}>
                {saved ? (
                  <>
                    <Check className="h-4 w-4" />
                    Guardado
                  </>
                ) : (
                  <>
                    <Save className="h-4 w-4" />
                    Guardar cambios
                  </>
                )}
              </Button>
            </div>
          </form>
        </main>
      </div>
    </div>
  )
}

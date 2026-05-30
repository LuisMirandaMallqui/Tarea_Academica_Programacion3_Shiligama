"use client"

import { useState } from "react"
import Link from "next/link"
import { ArrowLeft, User, Phone, MapPin, CreditCard, Save, Check } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { CatalogNavbar } from "@/components/catalog/catalog-navbar"

export default function ConfiguracionPage() {
  const [cartCount] = useState(3)
  const [searchQuery, setSearchQuery] = useState("")
  const [saved, setSaved] = useState(false)
  
  const [formData, setFormData] = useState({
    nombre: "Juan Pérez",
    telefono: "987654321",
    direccion: "Av. Los Olivos 123, Lima",
    dni: "12345678",
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
    setSaved(false)
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Simulate saving
    setSaved(true)
    setTimeout(() => setSaved(false), 3000)
  }

  return (
    <div className="min-h-screen bg-background">
      <CatalogNavbar cartCount={cartCount} searchQuery={searchQuery} onSearchChange={setSearchQuery} />
      
      <main className="mx-auto max-w-3xl px-4 sm:px-6 lg:px-8 py-6">
        {/* Breadcrumb */}
        <nav className="mb-6">
          <Link 
            href="/catalogo" 
            className="flex items-center gap-2 text-sm text-muted-foreground hover:text-primary transition-colors"
          >
            <ArrowLeft className="h-4 w-4" />
            Volver al catálogo
          </Link>
        </nav>

        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground">Configuración</h1>
          <p className="text-muted-foreground mt-1">
            Administra tu información personal y preferencias de entrega
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          {/* Personal Information */}
          <Card className="mb-6">
            <CardHeader>
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-primary/10">
                  <User className="h-5 w-5 text-primary" />
                </div>
                <div>
                  <CardTitle>Información Personal</CardTitle>
                  <CardDescription>Tu nombre y documento de identidad</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="space-y-2">
                  <Label htmlFor="nombre">Nombre completo</Label>
                  <Input
                    id="nombre"
                    name="nombre"
                    value={formData.nombre}
                    onChange={handleChange}
                    placeholder="Ingresa tu nombre completo"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="dni">DNI</Label>
                  <div className="relative">
                    <CreditCard className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                    <Input
                      id="dni"
                      name="dni"
                      value={formData.dni}
                      onChange={handleChange}
                      placeholder="12345678"
                      maxLength={8}
                      className="pl-10"
                    />
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Contact Information */}
          <Card className="mb-6">
            <CardHeader>
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-primary/10">
                  <Phone className="h-5 w-5 text-primary" />
                </div>
                <div>
                  <CardTitle>Contacto</CardTitle>
                  <CardDescription>Tu número de teléfono para coordinar entregas</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                <Label htmlFor="telefono">Teléfono</Label>
                <div className="relative">
                  <Phone className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    id="telefono"
                    name="telefono"
                    type="tel"
                    value={formData.telefono}
                    onChange={handleChange}
                    placeholder="987654321"
                    maxLength={9}
                    className="pl-10"
                  />
                </div>
                <p className="text-xs text-muted-foreground">
                  Usaremos este número para contactarte sobre tus pedidos
                </p>
              </div>
            </CardContent>
          </Card>

          {/* Delivery Address */}
          <Card className="mb-6">
            <CardHeader>
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-primary/10">
                  <MapPin className="h-5 w-5 text-primary" />
                </div>
                <div>
                  <CardTitle>Dirección de Entrega</CardTitle>
                  <CardDescription>La dirección donde recibirás tus pedidos</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                <Label htmlFor="direccion">Dirección completa</Label>
                <Textarea
                  id="direccion"
                  name="direccion"
                  value={formData.direccion}
                  onChange={handleChange}
                  placeholder="Ingresa tu dirección completa incluyendo distrito y referencias"
                  rows={3}
                />
                <p className="text-xs text-muted-foreground">
                  Incluye referencias para facilitar la entrega (cerca de, frente a, etc.)
                </p>
              </div>
            </CardContent>
          </Card>

          <Separator className="my-6" />

          {/* Save Button */}
          <div className="flex flex-col sm:flex-row gap-4 sm:items-center sm:justify-between">
            <p className="text-sm text-muted-foreground">
              Los cambios se guardarán automáticamente en tu perfil
            </p>
            <Button type="submit" className="gap-2" disabled={saved}>
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
  )
}

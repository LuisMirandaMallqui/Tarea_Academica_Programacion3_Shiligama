"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator"
import { Badge } from "@/components/ui/badge"
import { 
  User, 
  Mail, 
  Phone, 
  Shield, 
  Calendar,
  Save, 
  Check,
  Camera,
  Key
} from "lucide-react"

export default function AdminPerfilPage() {
  const [saved, setSaved] = useState(false)
  
  const [profileData, setProfileData] = useState({
    nombre: "Admin Principal",
    email: "test1234@admin.com",
    telefono: "987654321",
    cargo: "Administrador General",
  })

  const [passwordData, setPasswordData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  })

  const handleProfileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setProfileData(prev => ({ ...prev, [name]: value }))
    setSaved(false)
  }

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setPasswordData(prev => ({ ...prev, [name]: value }))
  }

  const handleProfileSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    setSaved(true)
    setTimeout(() => setSaved(false), 3000)
  }

  const handlePasswordSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Password change logic here
    setPasswordData({
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    })
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
              <User className="h-6 w-6 text-[#0D4525]" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-foreground">Mi Perfil</h1>
              <p className="text-muted-foreground">Administra tu informacion personal</p>
            </div>
          </div>

          <div className="grid gap-6 lg:grid-cols-3">
            {/* Profile Card */}
            <Card className="lg:col-span-1">
              <CardContent className="pt-6">
                <div className="flex flex-col items-center text-center">
                  <div className="relative">
                    <Avatar className="h-24 w-24">
                      <AvatarImage src="/placeholder-avatar.jpg" alt="Admin" />
                      <AvatarFallback className="bg-[#0D4525] text-white text-2xl">
                        AD
                      </AvatarFallback>
                    </Avatar>
                    <Button 
                      size="icon" 
                      variant="outline" 
                      className="absolute -bottom-1 -right-1 h-8 w-8 rounded-full"
                    >
                      <Camera className="h-4 w-4" />
                    </Button>
                  </div>
                  <h2 className="mt-4 text-xl font-semibold">{profileData.nombre}</h2>
                  <p className="text-muted-foreground">{profileData.email}</p>
                  <Badge className="mt-2 bg-[#0D4525]">{profileData.cargo}</Badge>
                  
                  <Separator className="my-4 w-full" />
                  
                  <div className="w-full space-y-3 text-left">
                    <div className="flex items-center gap-3 text-sm">
                      <Shield className="h-4 w-4 text-muted-foreground" />
                      <span className="text-muted-foreground">Rol:</span>
                      <span className="font-medium">Administrador</span>
                    </div>
                    <div className="flex items-center gap-3 text-sm">
                      <Calendar className="h-4 w-4 text-muted-foreground" />
                      <span className="text-muted-foreground">Miembro desde:</span>
                      <span className="font-medium">Enero 2024</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Edit Profile Form */}
            <div className="lg:col-span-2 space-y-6">
              <Card>
                <CardHeader>
                  <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#0D4525]/10">
                      <User className="h-5 w-5 text-[#0D4525]" />
                    </div>
                    <div>
                      <CardTitle>Informacion Personal</CardTitle>
                      <CardDescription>Actualiza tus datos de contacto</CardDescription>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <form onSubmit={handleProfileSubmit} className="space-y-4">
                    <div className="grid gap-4 sm:grid-cols-2">
                      <div className="space-y-2">
                        <Label htmlFor="nombre">Nombre completo</Label>
                        <div className="relative">
                          <User className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                          <Input
                            id="nombre"
                            name="nombre"
                            value={profileData.nombre}
                            onChange={handleProfileChange}
                            placeholder="Tu nombre"
                            className="pl-10"
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="cargo">Cargo</Label>
                        <Input
                          id="cargo"
                          name="cargo"
                          value={profileData.cargo}
                          onChange={handleProfileChange}
                          placeholder="Tu cargo"
                          disabled
                        />
                      </div>
                    </div>
                    <div className="grid gap-4 sm:grid-cols-2">
                      <div className="space-y-2">
                        <Label htmlFor="email">Correo electronico</Label>
                        <div className="relative">
                          <Mail className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                          <Input
                            id="email"
                            name="email"
                            type="email"
                            value={profileData.email}
                            onChange={handleProfileChange}
                            placeholder="tu@email.com"
                            className="pl-10"
                          />
                        </div>
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="telefono">Telefono</Label>
                        <div className="relative">
                          <Phone className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                          <Input
                            id="telefono"
                            name="telefono"
                            value={profileData.telefono}
                            onChange={handleProfileChange}
                            placeholder="987654321"
                            className="pl-10"
                          />
                        </div>
                      </div>
                    </div>
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
                </CardContent>
              </Card>

              {/* Change Password */}
              <Card>
                <CardHeader>
                  <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#0D4525]/10">
                      <Key className="h-5 w-5 text-[#0D4525]" />
                    </div>
                    <div>
                      <CardTitle>Cambiar Contrasena</CardTitle>
                      <CardDescription>Actualiza tu contrasena de acceso</CardDescription>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <form onSubmit={handlePasswordSubmit} className="space-y-4">
                    <div className="space-y-2">
                      <Label htmlFor="currentPassword">Contrasena actual</Label>
                      <Input
                        id="currentPassword"
                        name="currentPassword"
                        type="password"
                        value={passwordData.currentPassword}
                        onChange={handlePasswordChange}
                        placeholder="Ingresa tu contrasena actual"
                      />
                    </div>
                    <div className="grid gap-4 sm:grid-cols-2">
                      <div className="space-y-2">
                        <Label htmlFor="newPassword">Nueva contrasena</Label>
                        <Input
                          id="newPassword"
                          name="newPassword"
                          type="password"
                          value={passwordData.newPassword}
                          onChange={handlePasswordChange}
                          placeholder="Nueva contrasena"
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor="confirmPassword">Confirmar contrasena</Label>
                        <Input
                          id="confirmPassword"
                          name="confirmPassword"
                          type="password"
                          value={passwordData.confirmPassword}
                          onChange={handlePasswordChange}
                          placeholder="Confirma tu contrasena"
                        />
                      </div>
                    </div>
                    <div className="flex justify-end">
                      <Button type="submit" variant="outline" className="gap-2">
                        <Key className="h-4 w-4" />
                        Cambiar contrasena
                      </Button>
                    </div>
                  </form>
                </CardContent>
              </Card>
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}

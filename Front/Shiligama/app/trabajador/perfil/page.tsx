"use client"

import { useState } from "react"
import { TrabajadorSidebar } from "@/components/trabajador/trabajador-sidebar"
import { TrabajadorTopbar } from "@/components/trabajador/trabajador-topbar"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import { Mail, Phone, User, CreditCard, Pencil, Save, X } from "lucide-react"

interface WorkerProfile {
  nombres: string
  apellidos: string
  email: string
  telefono: string
  dni: string
  rol: string
  fechaIngreso: string
  avatar: string
}

const initialProfile: WorkerProfile = {
  nombres: "Carlos",
  apellidos: "Mendoza",
  email: "carlos.mendoza@shiligama.com",
  telefono: "987654321",
  dni: "45678912",
  rol: "Trabajador",
  fechaIngreso: "2023-06-15",
  avatar: "/avatars/worker.jpg",
}

export default function TrabajadorPerfilPage() {
  const [profile, setProfile] = useState<WorkerProfile>(initialProfile)
  const [isEditing, setIsEditing] = useState(false)
  const [editedProfile, setEditedProfile] = useState<WorkerProfile>(initialProfile)

  const handleEdit = () => {
    setEditedProfile(profile)
    setIsEditing(true)
  }

  const handleCancel = () => {
    setEditedProfile(profile)
    setIsEditing(false)
  }

  const handleSave = () => {
    setProfile(editedProfile)
    setIsEditing(false)
  }

  const handleInputChange = (field: keyof WorkerProfile, value: string) => {
    setEditedProfile((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <div className="min-h-screen bg-muted/30">
      <TrabajadorSidebar />
      <div className="lg:pl-64">
        <TrabajadorTopbar title="Mi Perfil" />
        <main className="p-6 pt-20 lg:pt-6">
          <div className="max-w-3xl mx-auto space-y-6">
            {/* Profile Header Card */}
            <Card>
              <CardContent className="pt-6">
                <div className="flex flex-col sm:flex-row items-center gap-6">
                  <Avatar className="h-24 w-24 border-4 border-primary/20">
                    <AvatarImage src={profile.avatar} alt={`${profile.nombres} ${profile.apellidos}`} />
                    <AvatarFallback className="text-2xl bg-primary text-primary-foreground">
                      {profile.nombres[0]}{profile.apellidos[0]}
                    </AvatarFallback>
                  </Avatar>
                  <div className="text-center sm:text-left">
                    <h2 className="text-2xl font-bold text-foreground">
                      {profile.nombres} {profile.apellidos}
                    </h2>
                    <div className="flex items-center justify-center sm:justify-start gap-2 mt-1">
                      <Badge variant="secondary" className="bg-primary/10 text-primary border-0">
                        {profile.rol}
                      </Badge>
                      <span className="text-sm text-muted-foreground">
                        Desde {new Date(profile.fechaIngreso).toLocaleDateString("es-PE", { 
                          month: "long", 
                          year: "numeric" 
                        })}
                      </span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Profile Information Card */}
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-4">
                <div>
                  <CardTitle className="text-lg">Información Personal</CardTitle>
                  <CardDescription>
                    {isEditing 
                      ? "Edita tu email y teléfono de contacto" 
                      : "Tu información de contacto y datos personales"
                    }
                  </CardDescription>
                </div>
                {!isEditing ? (
                  <Button onClick={handleEdit} variant="outline" size="sm" className="gap-2">
                    <Pencil className="h-4 w-4" />
                    Editar perfil
                  </Button>
                ) : (
                  <div className="flex gap-2">
                    <Button onClick={handleCancel} variant="outline" size="sm" className="gap-2">
                      <X className="h-4 w-4" />
                      Cancelar
                    </Button>
                    <Button onClick={handleSave} size="sm" className="gap-2">
                      <Save className="h-4 w-4" />
                      Guardar cambios
                    </Button>
                  </div>
                )}
              </CardHeader>
              <CardContent className="space-y-6">
                {/* Email - Editable */}
                <div className="space-y-2">
                  <Label htmlFor="email" className="flex items-center gap-2 text-sm font-medium">
                    <Mail className="h-4 w-4 text-muted-foreground" />
                    Correo electrónico
                  </Label>
                  {isEditing ? (
                    <Input
                      id="email"
                      type="email"
                      value={editedProfile.email}
                      onChange={(e) => handleInputChange("email", e.target.value)}
                      className="max-w-md"
                    />
                  ) : (
                    <p className="text-foreground pl-6">{profile.email}</p>
                  )}
                </div>

                {/* Phone - Editable */}
                <div className="space-y-2">
                  <Label htmlFor="telefono" className="flex items-center gap-2 text-sm font-medium">
                    <Phone className="h-4 w-4 text-muted-foreground" />
                    Teléfono
                  </Label>
                  {isEditing ? (
                    <Input
                      id="telefono"
                      type="tel"
                      value={editedProfile.telefono}
                      onChange={(e) => handleInputChange("telefono", e.target.value)}
                      className="max-w-md"
                    />
                  ) : (
                    <p className="text-foreground pl-6">{profile.telefono}</p>
                  )}
                </div>

                <div className="border-t pt-6">
                  <p className="text-xs text-muted-foreground mb-4">
                    Los siguientes campos no pueden ser editados. Contacta al administrador para realizar cambios.
                  </p>

                  {/* Names - Not Editable */}
                  <div className="space-y-4">
                    <div className="space-y-2">
                      <Label className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                        <User className="h-4 w-4" />
                        Nombres
                      </Label>
                      <p className="text-foreground pl-6">{profile.nombres}</p>
                    </div>

                    {/* Last Names - Not Editable */}
                    <div className="space-y-2">
                      <Label className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                        <User className="h-4 w-4" />
                        Apellidos
                      </Label>
                      <p className="text-foreground pl-6">{profile.apellidos}</p>
                    </div>

                    {/* DNI - Not Editable */}
                    <div className="space-y-2">
                      <Label className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                        <CreditCard className="h-4 w-4" />
                        DNI
                      </Label>
                      <p className="text-foreground pl-6">{profile.dni}</p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </main>
      </div>
    </div>
  )
}

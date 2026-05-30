"use client"

import { useState } from "react"
import {
  User,
  CreditCard,
  Phone,
  Mail,
  MapPin,
  Lock,
  Eye,
  EyeOff,
  ArrowLeft,
  ShoppingCart,
} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

interface RegisterFormProps {
  onBackToLogin: () => void
}

export function RegisterForm({ onBackToLogin }: RegisterFormProps) {
  const [showPassword, setShowPassword] = useState(false)
  const [formData, setFormData] = useState({
    fullName: "",
    dni: "",
    phone: "",
    email: "",
    address: "",
    password: "",
    confirmPassword: "",
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("[v0] Registration attempt:", formData)
  }

  return (
    <div className="space-y-6">
      {/* Back button */}
      <button
        onClick={onBackToLogin}
        className="flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors group"
      >
        <ArrowLeft className="w-4 h-4 group-hover:-translate-x-1 transition-transform" />
        <span className="text-sm font-medium">Volver al inicio</span>
      </button>

      {/* Mobile logo */}
      <div className="flex items-center gap-3 lg:hidden">
        <div className="w-10 h-10 bg-primary rounded-xl flex items-center justify-center">
          <ShoppingCart className="w-6 h-6 text-primary-foreground" />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-foreground">Shiligama</h1>
          <p className="text-muted-foreground text-sm">Sistema de Gestión</p>
        </div>
      </div>

      {/* Header */}
      <div>
        <h2 className="text-2xl font-semibold text-foreground">
          Crear cuenta
        </h2>
        <p className="text-muted-foreground mt-1">
          Completa tus datos para registrarte como cliente
        </p>
      </div>

      {/* Register form */}
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Full name */}
        <div className="space-y-2">
          <Label htmlFor="fullName" className="text-foreground font-medium">
            Nombre completo
          </Label>
          <div className="relative">
            <User className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              id="fullName"
              name="fullName"
              type="text"
              placeholder="Juan Pérez García"
              value={formData.fullName}
              onChange={handleChange}
              className="pl-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
              required
            />
          </div>
        </div>

        {/* DNI and Phone row */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="space-y-2">
            <Label htmlFor="dni" className="text-foreground font-medium">
              DNI
            </Label>
            <div className="relative">
              <CreditCard className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
              <Input
                id="dni"
                name="dni"
                type="text"
                placeholder="12345678"
                value={formData.dni}
                onChange={handleChange}
                className="pl-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
                required
                maxLength={8}
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="phone" className="text-foreground font-medium">
              Teléfono / WhatsApp
            </Label>
            <div className="relative">
              <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
              <Input
                id="phone"
                name="phone"
                type="tel"
                placeholder="987 654 321"
                value={formData.phone}
                onChange={handleChange}
                className="pl-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
                required
              />
            </div>
          </div>
        </div>

        {/* Email */}
        <div className="space-y-2">
          <Label htmlFor="registerEmail" className="text-foreground font-medium">
            Correo electrónico
          </Label>
          <div className="relative">
            <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              id="registerEmail"
              name="email"
              type="email"
              placeholder="tu@correo.com"
              value={formData.email}
              onChange={handleChange}
              className="pl-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
              required
            />
          </div>
        </div>

        {/* Delivery address */}
        <div className="space-y-2">
          <Label htmlFor="address" className="text-foreground font-medium">
            Dirección de entrega
          </Label>
          <div className="relative">
            <MapPin className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              id="address"
              name="address"
              type="text"
              placeholder="Av. Principal 123, Distrito"
              value={formData.address}
              onChange={handleChange}
              className="pl-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
              required
            />
          </div>
        </div>

        {/* Password */}
        <div className="space-y-2">
          <Label htmlFor="registerPassword" className="text-foreground font-medium">
            Contraseña
          </Label>
          <div className="relative">
            <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              id="registerPassword"
              name="password"
              type={showPassword ? "text" : "password"}
              placeholder="Mínimo 8 caracteres"
              value={formData.password}
              onChange={handleChange}
              className="pl-10 pr-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
              required
              minLength={8}
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
              aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
            >
              {showPassword ? (
                <EyeOff className="w-5 h-5" />
              ) : (
                <Eye className="w-5 h-5" />
              )}
            </button>
          </div>
        </div>

        {/* Confirm Password */}
        <div className="space-y-2">
          <Label htmlFor="confirmPassword" className="text-foreground font-medium">
            Confirmar contraseña
          </Label>
          <div className="relative">
            <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-muted-foreground" />
            <Input
              id="confirmPassword"
              name="confirmPassword"
              type={showPassword ? "text" : "password"}
              placeholder="Repite tu contraseña"
              value={formData.confirmPassword}
              onChange={handleChange}
              className="pl-10 pr-10 h-11 bg-card border-border focus:border-primary focus:ring-primary"
              required
              minLength={8}
            />
          </div>
        </div>

        <Button
          type="submit"
          className="w-full h-12 bg-primary hover:bg-primary/90 text-primary-foreground font-medium text-base mt-2"
        >
          Crear cuenta
        </Button>
      </form>

      {/* Login link */}
      <div className="text-center">
        <p className="text-muted-foreground">
          ¿Ya tienes una cuenta?{" "}
          <button
            onClick={onBackToLogin}
            className="text-primary hover:text-primary/80 font-medium transition-colors"
          >
            Inicia sesión
          </button>
        </p>
      </div>
    </div>
  )
}

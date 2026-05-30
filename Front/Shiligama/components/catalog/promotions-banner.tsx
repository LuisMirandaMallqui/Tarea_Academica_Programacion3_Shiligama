"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { ChevronLeft, ChevronRight, Truck, Percent, Gift } from "lucide-react"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"

const promotions = [
  {
    id: 1,
    title: "Delivery Gratis",
    description: "En compras mayores a S/. 50",
    icon: Truck,
    gradient: "from-primary to-accent",
  },
  {
    id: 2,
    title: "20% de Descuento",
    description: "En todos los lacteos esta semana",
    icon: Percent,
    gradient: "from-accent to-primary",
  },
  {
    id: 3,
    title: "2x1 en Snacks",
    description: "Lleva 2 y paga 1 en snacks seleccionados",
    icon: Gift,
    gradient: "from-primary/90 to-accent/90",
  },
]

export function PromotionsBanner() {
  const [currentSlide, setCurrentSlide] = useState(0)

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % promotions.length)
    }, 5000)
    return () => clearInterval(timer)
  }, [])

  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % promotions.length)
  }

  const prevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + promotions.length) % promotions.length)
  }

  return (
    <div className="relative mt-6 overflow-hidden rounded-2xl">
      <div
        className="flex transition-transform duration-500 ease-out"
        style={{ transform: `translateX(-${currentSlide * 100}%)` }}
      >
        {promotions.map((promo) => {
          const Icon = promo.icon
          return (
            <Link
              key={promo.id}
              href="/ofertas"
              className={cn(
                "min-w-full bg-gradient-to-r px-6 py-8 sm:px-10 sm:py-10 cursor-pointer block",
                promo.gradient
              )}
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-4 sm:gap-6">
                  <div className="flex h-14 w-14 sm:h-16 sm:w-16 items-center justify-center rounded-full bg-white/20 backdrop-blur-sm">
                    <Icon className="h-7 w-7 sm:h-8 sm:w-8 text-white" />
                  </div>
                  <div>
                    <h3 className="text-xl sm:text-2xl font-bold text-white">
                      {promo.title}
                    </h3>
                    <p className="text-sm sm:text-base text-white/90">
                      {promo.description}
                    </p>
                  </div>
                </div>
                <span className="hidden sm:flex bg-white text-primary hover:bg-white/90 font-semibold px-4 py-2 rounded-md text-sm">
                  Ver ofertas
                </span>
              </div>
            </Link>
          )
        })}
      </div>

      {/* Navigation */}
      <Button
        variant="ghost"
        size="icon"
        className="absolute left-2 top-1/2 -translate-y-1/2 h-8 w-8 rounded-full bg-white/20 text-white hover:bg-white/30 backdrop-blur-sm"
        onClick={prevSlide}
      >
        <ChevronLeft className="h-4 w-4" />
        <span className="sr-only">Anterior</span>
      </Button>
      <Button
        variant="ghost"
        size="icon"
        className="absolute right-2 top-1/2 -translate-y-1/2 h-8 w-8 rounded-full bg-white/20 text-white hover:bg-white/30 backdrop-blur-sm"
        onClick={nextSlide}
      >
        <ChevronRight className="h-4 w-4" />
        <span className="sr-only">Siguiente</span>
      </Button>

      {/* Dots */}
      <div className="absolute bottom-3 left-1/2 flex -translate-x-1/2 gap-2">
        {promotions.map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentSlide(index)}
            className={cn(
              "h-2 rounded-full transition-all duration-300",
              currentSlide === index
                ? "w-6 bg-white"
                : "w-2 bg-white/50 hover:bg-white/70"
            )}
          >
            <span className="sr-only">Slide {index + 1}</span>
          </button>
        ))}
      </div>
    </div>
  )
}

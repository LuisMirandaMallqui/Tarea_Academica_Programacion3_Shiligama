"use client"

import useSWR from "swr"

interface CartItem {
  id: number
  name: string
  price: number
  image: string
  quantity: number
}

const CART_KEY = "shiligama-cart"

// Initialize with some default items
const defaultCartItems: CartItem[] = [
  {
    id: 1,
    name: "Arroz Extra Costeño 5kg",
    price: 28.90,
    image: "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=100&h=100&fit=crop",
    quantity: 2
  },
  {
    id: 5,
    name: "Leche Gloria Entera 1L",
    price: 5.20,
    image: "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=100&h=100&fit=crop",
    quantity: 1
  }
]

// Fetcher that reads from localStorage
const cartFetcher = (): CartItem[] => {
  if (typeof window === "undefined") return defaultCartItems
  
  const stored = localStorage.getItem(CART_KEY)
  if (stored) {
    try {
      return JSON.parse(stored)
    } catch {
      return defaultCartItems
    }
  }
  // Initialize with default items
  localStorage.setItem(CART_KEY, JSON.stringify(defaultCartItems))
  return defaultCartItems
}

export function useCart() {
  const { data: cartItems = [], mutate } = useSWR<CartItem[]>(CART_KEY, cartFetcher, {
    fallbackData: defaultCartItems,
    revalidateOnFocus: true,
    revalidateOnReconnect: true,
  })

  const cartCount = cartItems.reduce((sum, item) => sum + item.quantity, 0)

  const addToCart = (item: Omit<CartItem, "quantity">) => {
    const newItems = [...cartItems]
    const existingIndex = newItems.findIndex((i) => i.id === item.id)
    
    if (existingIndex >= 0) {
      newItems[existingIndex].quantity += 1
    } else {
      newItems.push({ ...item, quantity: 1 })
    }
    
    localStorage.setItem(CART_KEY, JSON.stringify(newItems))
    mutate(newItems, false)
  }

  const removeFromCart = (id: number) => {
    const newItems = cartItems.filter((item) => item.id !== id)
    localStorage.setItem(CART_KEY, JSON.stringify(newItems))
    mutate(newItems, false)
  }

  const updateQuantity = (id: number, quantity: number) => {
    if (quantity <= 0) {
      removeFromCart(id)
      return
    }
    
    const newItems = cartItems.map((item) =>
      item.id === id ? { ...item, quantity } : item
    )
    localStorage.setItem(CART_KEY, JSON.stringify(newItems))
    mutate(newItems, false)
  }

  const clearCart = () => {
    localStorage.removeItem(CART_KEY)
    mutate([], false)
  }

  return {
    cartItems,
    cartCount,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
  }
}

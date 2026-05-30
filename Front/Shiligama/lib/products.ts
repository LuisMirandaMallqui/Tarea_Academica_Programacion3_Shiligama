export interface Product {
  id: number
  name: string
  price: number
  originalPrice?: number
  image: string
  category: string
  stock: number
  isPromo?: boolean
  description: string
}

export const products: Product[] = [
  {
    id: 1,
    name: "Arroz Extra Costeño 5kg",
    price: 24.90,
    image: "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=300&h=300&fit=crop",
    category: "abarrotes",
    stock: 45,
    description: "Arroz de grano largo de la más alta calidad. Ideal para todo tipo de preparaciones. Rinde más y queda suelto.",
  },
  {
    id: 2,
    name: "Aceite Primor 1L",
    price: 12.50,
    originalPrice: 15.90,
    image: "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=300&h=300&fit=crop",
    category: "abarrotes",
    stock: 32,
    isPromo: true,
    description: "Aceite vegetal premium para cocinar. Bajo en grasas saturadas. Ideal para freír, saltear y hornear.",
  },
  {
    id: 3,
    name: "Inca Kola 1.5L",
    price: 6.50,
    image: "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&h=300&fit=crop",
    category: "bebidas",
    stock: 78,
    description: "La bebida del sabor nacional. Refrescante y con ese sabor único que todos conocemos. Botella familiar de 1.5 litros.",
  },
  {
    id: 4,
    name: "Leche Gloria Entera 1L",
    price: 5.20,
    image: "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300&h=300&fit=crop",
    category: "lacteos",
    stock: 120,
    description: "Leche entera evaporada. Rica en calcio y vitaminas. Perfecta para toda la familia.",
  },
  {
    id: 5,
    name: "Yogurt Laive Fresa 1L",
    price: 8.90,
    originalPrice: 10.50,
    image: "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=300&h=300&fit=crop",
    category: "lacteos",
    stock: 25,
    isPromo: true,
    description: "Yogurt cremoso sabor fresa con probióticos. Ideal para el desayuno o como snack saludable.",
  },
  {
    id: 6,
    name: "Galletas Oreo x6",
    price: 4.50,
    image: "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=300&h=300&fit=crop",
    category: "snacks",
    stock: 65,
    description: "Galletas de chocolate con relleno de crema de vainilla. El snack favorito de todos. Pack de 6 unidades.",
  },
  {
    id: 7,
    name: "Detergente Bolivar 2.6kg",
    price: 28.90,
    image: "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=300&h=300&fit=crop",
    category: "limpieza",
    stock: 18,
    description: "Detergente en polvo de alta eficiencia. Remueve las manchas más difíciles y deja la ropa con un aroma fresco.",
  },
  {
    id: 8,
    name: "Jabon Bolivar Pack x3",
    price: 9.90,
    originalPrice: 12.90,
    image: "https://images.unsplash.com/photo-1600857544200-b2f666a9a2ec?w=300&h=300&fit=crop",
    category: "limpieza",
    stock: 42,
    isPromo: true,
    description: "Jabón de lavar ropa de alta calidad. Elimina manchas difíciles y cuida tus prendas. Pack económico de 3 barras.",
  },
  {
    id: 9,
    name: "Pechuga de Pollo 1kg",
    price: 16.90,
    image: "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=300&h=300&fit=crop",
    category: "carnes",
    stock: 8,
    description: "Pechuga de pollo fresca sin hueso. Alta en proteínas y baja en grasa. Perfecta para dietas saludables.",
  },
  {
    id: 10,
    name: "Manzana Roja 1kg",
    price: 5.90,
    image: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=300&h=300&fit=crop",
    category: "frutas",
    stock: 35,
    description: "Manzanas rojas frescas y crujientes. Ricas en fibra y vitaminas. Ideales para consumir como snack.",
  },
  {
    id: 11,
    name: "Platano de Seda 1kg",
    price: 3.50,
    image: "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=300&h=300&fit=crop",
    category: "frutas",
    stock: 50,
    description: "Plátanos de seda maduros y dulces. Excelente fuente de potasio y energía natural.",
  },
  {
    id: 12,
    name: "Chocolate Sublime x6",
    price: 7.90,
    image: "https://images.unsplash.com/photo-1511381939415-e44015466834?w=300&h=300&fit=crop",
    category: "dulces",
    stock: 88,
    description: "Chocolate con leche y maní. El clásico peruano que todos aman. Pack de 6 barras.",
  },
  {
    id: 13,
    name: "Helado Donofrio 1L",
    price: 18.90,
    originalPrice: 22.90,
    image: "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?w=300&h=300&fit=crop",
    category: "congelados",
    stock: 12,
    isPromo: true,
    description: "Helado cremoso de vainilla y chocolate. El postre perfecto para compartir en familia. Envase de 1 litro.",
  },
  {
    id: 14,
    name: "Pañales Huggies M x40",
    price: 45.90,
    image: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=300&h=300&fit=crop",
    category: "bebes",
    stock: 15,
    description: "Pañales ultra absorbentes talla M. Protección por hasta 12 horas. Suaves con la piel del bebé.",
  },
  {
    id: 15,
    name: "Agua San Luis 2.5L",
    price: 3.20,
    image: "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300&h=300&fit=crop",
    category: "bebidas",
    stock: 200,
    description: "Agua mineral natural de manantial. Pureza garantizada. Hidratación para toda la familia.",
  },
  {
    id: 16,
    name: "Papas Lays Original 150g",
    price: 6.90,
    image: "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=300&h=300&fit=crop",
    category: "snacks",
    stock: 0,
    description: "Papas fritas crujientes con sal. El snack clásico para cualquier ocasión. Bolsa de 150g.",
  },
]

export const categories = [
  { id: "abarrotes", name: "Abarrotes" },
  { id: "bebidas", name: "Bebidas" },
  { id: "lacteos", name: "Lacteos" },
  { id: "snacks", name: "Snacks" },
  { id: "limpieza", name: "Limpieza" },
  { id: "carnes", name: "Carnes y Embutidos" },
  { id: "frutas", name: "Frutas y Verduras" },
  { id: "dulces", name: "Dulces y Golosinas" },
  { id: "congelados", name: "Congelados" },
  { id: "bebes", name: "Bebes" },
]

export function getProductById(id: number): Product | undefined {
  return products.find(p => p.id === id)
}

export function getRelatedProducts(product: Product, limit: number = 4): Product[] {
  return products
    .filter(p => p.category === product.category && p.id !== product.id)
    .slice(0, limit)
}

export function getDiscountedProducts(): Product[] {
  return products.filter(p => p.isPromo && p.originalPrice)
}

export function getCategoryName(categoryId: string): string {
  const category = categories.find(c => c.id === categoryId)
  return category?.name || categoryId
}

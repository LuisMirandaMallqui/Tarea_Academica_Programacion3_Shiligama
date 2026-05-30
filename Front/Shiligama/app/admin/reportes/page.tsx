"use client"

import { useState } from "react"
import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { ReportsHeader } from "@/components/admin/reports/reports-header"
import { SalesReportSection } from "@/components/admin/reports/sales-report-section"
import { TopProductsSection } from "@/components/admin/reports/top-products-section"
import { InventoryReportSection } from "@/components/admin/reports/inventory-report-section"
import { CustomersReportSection } from "@/components/admin/reports/customers-report-section"

export default function ReportsPage() {
  const [dateRange, setDateRange] = useState<{ from: Date; to: Date }>({
    from: new Date(new Date().setDate(new Date().getDate() - 30)),
    to: new Date(),
  })

  return (
    <div className="flex min-h-screen bg-muted/30">
      <AdminSidebar />
      
      <div className="flex-1 flex flex-col lg:ml-64">
        <AdminTopbar />
        
        <main className="flex-1 p-4 lg:p-6 space-y-6">
          <ReportsHeader 
            dateRange={dateRange} 
            onDateRangeChange={setDateRange} 
          />

          {/* Section 1: Ventas */}
          <SalesReportSection />

          {/* Section 2: Productos más vendidos */}
          <TopProductsSection />

          {/* Section 3: Inventario y mermas */}
          <InventoryReportSection />

          {/* Section 4: Reporte de clientes */}
          <CustomersReportSection />
        </main>
      </div>
    </div>
  )
}

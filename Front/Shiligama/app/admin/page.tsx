"use client"

import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminTopbar } from "@/components/admin/admin-topbar"
import { KpiCards } from "@/components/admin/kpi-cards"
import { SalesChart } from "@/components/admin/sales-chart"
import { OrdersComparisonChart } from "@/components/admin/orders-comparison-chart"
import { RecentOrdersTable } from "@/components/admin/recent-orders-table"

export default function AdminDashboard() {
  return (
    <div className="flex min-h-screen bg-muted/30">
      {/* Sidebar */}
      <AdminSidebar />
      
      {/* Main content */}
      <div className="flex-1 flex flex-col lg:ml-64">
        <AdminTopbar />
        
        <main className="flex-1 p-4 lg:p-6 space-y-6">
          {/* KPI Cards */}
          <KpiCards />
          
          {/* Charts Row */}
          <div className="grid gap-6 lg:grid-cols-2">
            <SalesChart />
            <OrdersComparisonChart />
          </div>
          
          {/* Recent Orders Table */}
          <RecentOrdersTable />
        </main>
      </div>
    </div>
  )
}

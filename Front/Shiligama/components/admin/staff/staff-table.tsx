"use client"

import { MoreHorizontal, Edit, Trash2, UserCheck, UserX, Shield, User } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Card, CardContent } from "@/components/ui/card"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import type { StaffMember } from "@/app/admin/personal/page"

interface StaffTableProps {
  staff: StaffMember[]
  onEdit: (member: StaffMember) => void
  onDelete: (id: string) => void
  onToggleStatus: (id: string) => void
}

export function StaffTable({ staff, onEdit, onDelete, onToggleStatus }: StaffTableProps) {
  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .slice(0, 2)
      .join("")
      .toUpperCase()
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("es-PE", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    })
  }

  if (staff.length === 0) {
    return (
      <Card>
        <CardContent className="p-12 text-center">
          <User className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
          <h3 className="text-lg font-medium text-foreground">No se encontró personal</h3>
          <p className="text-muted-foreground mt-1">
            Intenta ajustar los filtros o agrega un nuevo trabajador
          </p>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card>
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-[250px]">Trabajador</TableHead>
                <TableHead>DNI</TableHead>
                <TableHead>Rol</TableHead>
                <TableHead>Teléfono</TableHead>
                <TableHead className="hidden lg:table-cell">Correo</TableHead>
                <TableHead className="hidden md:table-cell">Fecha Ingreso</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="w-[70px]">Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {staff.map((member) => (
                <TableRow key={member.id}>
                  {/* Avatar & Name */}
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <Avatar className="h-10 w-10">
                        <AvatarImage src={member.avatar} alt={member.nombre} />
                        <AvatarFallback className="bg-primary/10 text-primary font-medium">
                          {getInitials(member.nombre)}
                        </AvatarFallback>
                      </Avatar>
                      <div>
                        <p className="font-medium text-foreground">{member.nombre}</p>
                        <p className="text-sm text-muted-foreground lg:hidden">
                          {member.correo}
                        </p>
                      </div>
                    </div>
                  </TableCell>

                  {/* DNI */}
                  <TableCell className="font-mono text-sm">{member.dni}</TableCell>

                  {/* Role */}
                  <TableCell>
                    <Badge
                      variant={member.rol === "administrador" ? "default" : "secondary"}
                      className="gap-1"
                    >
                      {member.rol === "administrador" ? (
                        <Shield className="h-3 w-3" />
                      ) : (
                        <User className="h-3 w-3" />
                      )}
                      {member.rol === "administrador" ? "Admin" : "Trabajador"}
                    </Badge>
                  </TableCell>

                  {/* Phone */}
                  <TableCell className="font-mono text-sm">{member.telefono}</TableCell>

                  {/* Email - hidden on mobile */}
                  <TableCell className="hidden lg:table-cell text-sm text-muted-foreground">
                    {member.correo}
                  </TableCell>

                  {/* Join Date - hidden on mobile */}
                  <TableCell className="hidden md:table-cell text-sm">
                    {formatDate(member.fechaIngreso)}
                  </TableCell>

                  {/* Status */}
                  <TableCell>
                    <Badge
                      variant="outline"
                      className={
                        member.estado === "activo"
                          ? "border-primary/50 bg-primary/10 text-primary"
                          : "border-muted-foreground/50 bg-muted text-muted-foreground"
                      }
                    >
                      {member.estado === "activo" ? "Activo" : "Inactivo"}
                    </Badge>
                  </TableCell>

                  {/* Actions */}
                  <TableCell>
                    <DropdownMenu>
                      <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon" className="h-8 w-8">
                          <MoreHorizontal className="h-4 w-4" />
                          <span className="sr-only">Acciones</span>
                        </Button>
                      </DropdownMenuTrigger>
                      <DropdownMenuContent align="end">
                        <DropdownMenuItem onClick={() => onEdit(member)}>
                          <Edit className="h-4 w-4 mr-2" />
                          Editar
                        </DropdownMenuItem>
                        <DropdownMenuItem onClick={() => onToggleStatus(member.id)}>
                          {member.estado === "activo" ? (
                            <>
                              <UserX className="h-4 w-4 mr-2" />
                              Desactivar
                            </>
                          ) : (
                            <>
                              <UserCheck className="h-4 w-4 mr-2" />
                              Activar
                            </>
                          )}
                        </DropdownMenuItem>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem
                          onClick={() => onDelete(member.id)}
                          className="text-destructive focus:text-destructive"
                        >
                          <Trash2 className="h-4 w-4 mr-2" />
                          Eliminar
                        </DropdownMenuItem>
                      </DropdownMenuContent>
                    </DropdownMenu>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </CardContent>
    </Card>
  )
}

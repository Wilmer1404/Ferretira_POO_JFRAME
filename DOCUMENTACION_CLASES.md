# Documentación de Clases - Sistema de Ferretería

## Resumen General del Sistema

Este es un sistema de gestión para una ferretería desarrollado en Java con Swing, que implementa un patrón de arquitectura en capas (Presentación, Negocio, Datos) y utiliza PostgreSQL como base de datos.

---

## 📋 **CAPA DE ENTIDADES** (com.ferreteria.entidades)

### **Entidades Principales**

#### **ItemVendible** (Clase Abstracta)
- **Propósito**: Clase base para todos los productos/servicios vendibles
- **Patrón**: Strategy Pattern - define diferentes estrategias de venta
- **Subclases**: ProductoUnitario, ProductoAGranel, Servicio
- **Métodos Abstractos**: 
  - `calcularSubtotal()`: Calcula precio según tipo de producto
  - `obtenerStock()`: Obtiene stock disponible
  - `obtenerUnidadParaGUI()`: Retorna unidad de medida

#### **ProductoUnitario**
- **Propósito**: Productos vendidos por unidades enteras (tornillos, herramientas)
- **Características**: Stock en enteros, precio por unidad
- **Cálculo**: Precio × cantidad (redondeada hacia abajo)

#### **ProductoAGranel**
- **Propósito**: Productos vendidos por peso/medida (cemento, arena, pintura)
- **Características**: Stock decimal, precio por unidad de medida
- **Cálculo**: Precio por medida × cantidad exacta

#### **Servicio**
- **Propósito**: Servicios ofrecidos por la ferretería (instalaciones, reparaciones)
- **Características**: Stock infinito, tarifa fija
- **Cálculo**: Tarifa × número de servicios

### **Entidades de Personas**

#### **Cliente**
- **Propósito**: Clientes que realizan compras
- **Autenticación**: Email + contraseña encriptada (BCrypt)
- **Datos**: DNI, nombre, apellidos, dirección, teléfono, fecha de registro

#### **Empleado**
- **Propósito**: Personal que opera el sistema
- **Roles**: ADMIN (acceso completo), VENDEDOR (solo ventas)
- **Autenticación**: Email + contraseña encriptada (BCrypt)
- **Estado**: Campo "activo" para desactivar sin eliminar

#### **Proveedor**
- **Propósito**: Empresas que suministran productos
- **Identificación**: RUC, razón social
- **Contacto**: Email, teléfono, dirección
- **Estado**: Campo "activo" para gestión lógica

### **Entidades de Clasificación**

#### **Categoria**
- **Propósito**: Clasificar productos (Herramientas, Plomería, etc.)
- **Uso**: Facilitar búsqueda y organización del inventario

### **Entidades de Transacciones**

#### **Venta**
- **Propósito**: Registro de transacciones de venta
- **Participantes**: Cliente + Empleado (opcional)
- **Datos**: Fecha, total, método de pago, referencia
- **Detalle**: Lista de items vendidos (DetalleVenta)

#### **DetalleVenta**
- **Propósito**: Items específicos dentro de una venta
- **Datos**: Producto, cantidad, precio histórico, subtotal
- **Integridad**: Preserva precios al momento de la venta

#### **Compra**
- **Propósito**: Registro de adquisiciones a proveedores
- **Participantes**: Proveedor + Empleado responsable
- **Datos**: Fecha, total, observaciones
- **Detalle**: Lista de items comprados (DetalleCompra)

#### **DetalleCompra**
- **Propósito**: Items específicos dentro de una compra
- **Datos**: Producto, cantidad, precio de compra, subtotal
- **Uso**: Control de costos y márgenes de ganancia

### **Entidades de Configuración**

#### **Configuracion**
- **Propósito**: Parámetros configurables del sistema
- **Uso**: Almacenar configuraciones globales

#### **Soporte**
- **Propósito**: Gestión de tickets o solicitudes de soporte
- **Uso**: Canal de comunicación para problemas técnicos

---

## 🔌 **CAPA DE CONEXIÓN** (com.ferreteria.conexion)

#### **Conexion**
- **Propósito**: Gestión centralizada de conexiones a PostgreSQL
- **Tecnología**: HikariCP (pool de conexiones)
- **Configuración**: 
  - Máximo 10 conexiones simultáneas
  - Mínimo 2 conexiones inactivas
  - Timeouts configurados para optimización
- **Patrón**: Singleton para instancia única del DataSource

---

## 💾 **CAPA DE DATOS** (com.ferreteria.datos)

### **Interfaces** (com.ferreteria.datos.interfaces)

#### **ICrudDAO<T, ID>**
- **Propósito**: Interfaz genérica para operaciones CRUD
- **Operaciones**: listar, buscarPorId, insertar, actualizar, eliminar
- **Patrón**: DAO (Data Access Object)

#### **Interfaces Específicas**
- **IClienteDAO**: Búsquedas adicionales por DNI y email
- **IEmpleadoDAO**: Búsquedas por email y términos
- **IProductoDAO**: Búsquedas por nombre y stock bajo
- **IProveedorDAO**: Búsquedas por RUC y términos
- **IVentaDAO**: Búsquedas por fechas y cliente
- **ICompraDAO**: Búsquedas por proveedor y empleado
- **ICategoriaDAO**: Operaciones básicas CRUD
- **IInventarioDAO**: Movimientos de stock para ventas/compras

### **Implementaciones** (com.ferreteria.datos.impl)

#### **Características Comunes de DAOs**
- **Manejo de Errores**: Try-with-resources para gestión de conexiones
- **Logging**: Registro detallado de errores con java.util.logging
- **Mapeo**: Métodos privados `mapearResultSet()` para conversión BD → Entidad
- **Transacciones**: Soporte para operaciones dentro de transacciones existentes

#### **DAOs Principales**
- **ClienteDAOImpl**: Gestión completa de clientes con autenticación
- **EmpleadoDAOImpl**: Gestión de empleados con control de roles
- **ProductoDAOImpl**: Manejo polimórfico de ProductoUnitario/AGranel/Servicio
- **ProveedorDAOImpl**: Gestión de proveedores con validación RUC
- **VentaDAOImpl**: Transacciones de venta con detalles
- **CompraDAOImpl**: Transacciones de compra con gestión de inventario
- **InventarioDAOImpl**: Actualización automática de stock

---

## 🏪 **CAPA DE NEGOCIO** (com.ferreteria.negocio)

### **Clases de Negocio Principales**

#### **ClienteNegocio**
- **Propósito**: Lógica de negocio para clientes
- **Funciones**:
  - Login con validación BCrypt
  - Registro con validaciones (DNI único, email único)
  - Búsquedas por DNI

#### **EmpleadoNegocio**
- **Propósito**: Gestión de empleados y autenticación
- **Funciones**:
  - Login con validación BCrypt y estado activo
  - CRUD con validaciones de negocio
  - Encriptación automática de contraseñas

#### **ProductoNegocio**
- **Propósito**: Gestión integral del inventario
- **Funciones**:
  - CRUD de productos (unitarios, a granel, servicios)
  - Validaciones específicas por tipo de producto
  - Control de stock y alertas de stock bajo
  - Actualizaciones transaccionales de inventario

#### **VentaNegocio**
- **Propósito**: Procesamiento completo de ventas
- **Funciones**:
  - Validación de stock antes de venta
  - Cálculo automático de totales
  - Transacciones atómicas (venta + actualización inventario)
  - Reportes de ventas por fechas y cliente

#### **CompraNegocio**
- **Propósito**: Gestión de compras a proveedores
- **Funciones**:
  - Procesamiento de compras
  - Actualización automática de inventario
  - Validaciones de negocio

#### **ProveedorNegocio**
- **Propósito**: Gestión de proveedores
- **Funciones**:
  - CRUD con validación RUC único
  - Búsquedas flexibles

#### **CategoriaNegocio**
- **Propósito**: Gestión de categorías de productos
- **Funciones**: Operaciones básicas de consulta

---

## 🖥️ **CAPA DE PRESENTACIÓN** (com.ferreteria.presentacion)

### **Formularios Principales**

#### **FerreteriaApp**
- **Propósito**: Clase principal que inicia la aplicación
- **Función**: Punto de entrada del sistema (método main)

#### **FrmLogin**
- **Propósito**: Formulario de autenticación dual
- **Usuarios**: Clientes y empleados
- **Redirección**: 
  - Clientes → FrmDashboardCliente
  - Empleados → FrmDashboardAdmin
- **Funciones**: Validación de credenciales, enlace a registro

#### **FrmDashboardCliente**
- **Propósito**: Dashboard principal para clientes
- **Funciones**:
  - Catálogo de productos con búsqueda
  - Carrito de compras interactivo
  - Procesamiento de pedidos
  - Validación de stock en tiempo real

#### **FrmDashboardAdmin**
- **Propósito**: Panel administrativo para empleados
- **Funciones**:
  - Acceso a todos los módulos de gestión
  - Panel de navegación hacia otros formularios
  - Control según rol del empleado

### **Formularios de Gestión**

#### **FrmGestionEmpleados**
- **Propósito**: CRUD completo de empleados
- **Funciones**:
  - Alta, modificación y desactivación
  - Gestión de roles y permisos
  - Búsqueda y filtrado

#### **FrmGestionProductos**
- **Propósito**: Gestión integral del inventario
- **Funciones**:
  - CRUD de productos (unitarios, a granel, servicios)
  - Control de stock y alertas
  - Categorización de productos

#### **FrmGestionProveedores**
- **Propósito**: Gestión de proveedores
- **Funciones**:
  - CRUD completo de proveedores
  - Validación de datos comerciales
  - Búsqueda y filtrado

### **Formularios de Transacciones**

#### **FrmRegistroCompra**
- **Propósito**: Registro de compras a proveedores
- **Funciones**:
  - Selección de proveedor
  - Agregado de productos al detalle
  - Cálculo automático de totales

#### **FrmReporteVentas**
- **Propósito**: Generación de reportes de ventas
- **Funciones**:
  - Filtros por fecha y cliente
  - Exportación de datos
  - Estadísticas de ventas

#### **FrmRegistroCliente**
- **Propósito**: Registro de nuevos clientes
- **Funciones**:
  - Formulario de alta de clientes
  - Validaciones de DNI y email únicos
  - Encriptación de contraseñas

---

## 🔧 **PATRONES DE DISEÑO IMPLEMENTADOS**

### **1. DAO (Data Access Object)**
- **Ubicación**: Capa de datos
- **Propósito**: Abstrae el acceso a la base de datos
- **Implementación**: Interfaces + implementaciones concretas

### **2. Strategy Pattern**
- **Ubicación**: ItemVendible y subclases
- **Propósito**: Diferentes estrategias de cálculo según tipo de producto
- **Implementación**: Métodos abstractos implementados diferente

### **3. MVC (Model-View-Controller)**
- **Model**: Entidades + Capa de datos
- **View**: Formularios Swing (capa de presentación)
- **Controller**: Clases de negocio

### **4. Singleton**
- **Ubicación**: Clase Conexion
- **Propósito**: Una única instancia del pool de conexiones
- **Implementación**: Instanciación estática del DataSource

### **5. Template Method**
- **Ubicación**: ICrudDAO
- **Propósito**: Define estructura común para operaciones CRUD
- **Implementación**: Interfaz con operaciones estándar

---

## 🔒 **ASPECTOS DE SEGURIDAD**

### **Encriptación de Contraseñas**
- **Librería**: BCrypt de jBCrypt
- **Uso**: Hash + salt para contraseñas de clientes y empleados
- **Validación**: Comparación segura de hashes

### **Gestión de Conexiones**
- **Pool**: HikariCP para prevenir agotamiento de conexiones
- **Try-with-resources**: Cierre automático de recursos
- **Timeouts**: Configurados para prevenir bloqueos

### **Validación de Datos**
- **Capa de Negocio**: Validaciones antes de persistir
- **Unicidad**: Control de DNI, email y RUC únicos
- **Integridad**: Validación de stock antes de ventas

---

## 📊 **BASE DE DATOS**

### **Motor**: PostgreSQL
### **Esquema Principal**: ferreteria_db

### **Tablas Principales**:
- **Cliente**: Datos de clientes con autenticación
- **Empleado**: Personal con roles y permisos
- **Proveedor**: Empresas suministradoras
- **Categoria**: Clasificación de productos
- **Producto**: Productos unitarios, a granel y servicios
- **Venta/DetalleVenta**: Transacciones de venta
- **Compra/DetalleCompra**: Transacciones de compra
- **MovimientoInventario**: Historial de cambios de stock

---

## 🚀 **CARACTERÍSTICAS TÉCNICAS**

### **Tecnologías**
- **Lenguaje**: Java
- **UI**: Swing con Forms de NetBeans
- **Base de Datos**: PostgreSQL
- **Pool de Conexiones**: HikariCP
- **Encriptación**: BCrypt
- **Logging**: java.util.logging
- **Build**: Ant (NetBeans project)

### **Arquitectura**
- **Patrón**: Multicapa (Presentación → Negocio → Datos)
- **Abstracción**: Interfaces para desacoplamiento
- **Transacciones**: Control transaccional para integridad
- **Polimorfismo**: Manejo uniforme de diferentes tipos de productos

Este sistema proporciona una solución completa para la gestión de una ferretería, desde el control de inventario hasta las transacciones de venta, con una arquitectura robusta y escalable.

#  FerreteriaApp: Sistema de Gestión de Ferretería
![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14%2B-336791?style=for-the-badge&logo=postgresql)
![Arquitectura](https://img.shields.io/badge/Arquitectura-4_Capas-blue?style=for-the-badge)

Sistema de escritorio para la gestión integral de una ferretería, desarrollado en Java con una interfaz gráfica Swing. El proyecto implementa una robusta arquitectura de 4 capas y aplica conceptos avanzados de Programación Orientada a Objetos (POO), incluyendo herencia y polimorfismo para el manejo de productos.

---

## 🌟 Características Principales

* **Módulo de Autenticación:** Sistema de Login seguro para empleados (`FrmLogin.java`) y un portal de registro/login para clientes (`FrmRegistroCliente.java`).
* **Dashboards por Rol:** Dos paneles de control separados:
    * `FrmDashboardAdmin`: Para administradores, con acceso a todos los módulos de gestión.
    * `FrmDashboardCliente`: Para clientes, permitiéndoles ver productos y gestionar su perfil.
* **Gestión de Productos (Polimórfico):**
    * Módulo CRUD (`FrmGestionProductos.java`) para crear, editar y desactivar (Soft-Delete) productos.
    * Manejo de **3 tipos de productos** usando Herencia: `ProductoUnitario`, `ProductoAGranel` y `Servicio`, todos derivados de una clase abstracta `ItemVendible`.
* **Gestión de Inventario (Compras):**
    * Módulo para registrar compras a proveedores (`FrmRegistroCompra.java`).
    * Actualización automática del stock de productos al confirmar una compra.
* **Gestión de Entidades:** Módulos CRUD completos para administrar:
    * Proveedores (`FrmGestionProveedores.java`)
    * Empleados (`FrmGestionEmpleados.java`)
    * Clientes (integrado en el registro)
* **Reportes de Ventas:** Visualizador de todas las ventas registradas (`FrmReporteVentas.java`), con capacidad de filtrado y visualización de detalles.
* **Refresco Automático de Datos:** Todas las ventanas de gestión (formularios `JInternalFrame`) implementan un `InternalFrameListener` que recarga automáticamente los datos de la base de datos cada vez que la ventana recibe foco. Esto asegura que la información (nuevo stock, proveedores, etc.) esté siempre actualizada en tiempo real sin necesidad de reiniciar la aplicación.

---

## 🏗️ Arquitectura del Software

El proyecto está construido siguiendo una **Arquitectura de 4 Capas (N-Tier)**, lo que garantiza una clara separación de responsabilidades, alta cohesión y bajo acoplamiento.

### Estructura de Archivos (Árbol de Arquitectura)

La estructura del código fuente (`src/com/ferreteria/`) refleja esta separación:

```

src/com/ferreteria/
│
├── 📁 conexion/
│   └── Conexion.java         \# (Patrón Singleton) Gestiona la conexión a PostgreSQL.
│
├── 📁 datos/
│   ├── 📁 impl/
│   │   ├── CategoriaDAOImpl.java
│   │   ├── ClienteDAOImpl.java
│   │   ├── CompraDAOImpl.java
│   │   ├── EmpleadoDAOImpl.java
│   │   ├── ProductoDAOImpl.java
│   │   ├── ProveedorDAOImpl.java
│   │   └── VentaDAOImpl.java     \# Implementaciones DAO con código SQL (INSERT, SELECT, UPDATE).
│   │
│   └── 📁 interfaces/
│       ├── ICrudDAO.java
│       ├── IProductoDAO.java
│       └── ...                 \# Contratos (Interfaces) que definen qué operaciones son posibles.
│
├── 📁 entidades/
│   ├── ItemVendible.java     \# (Modelo) Clase abstracta (Herencia).
│   ├── ProductoUnitario.java \# (Modelo) Clase hija.
│   ├── ProductoAGranel.java  \# (Modelo) Clase hija.
│   ├── Servicio.java         \# (Modelo) Clase hija.
│   ├── Empleado.java
│   ├── Cliente.java
│   ├── Proveedor.java
│   ├── Venta.java
│   └── ...                   \# Clases POJO que modelan los datos.
│
├── 📁 negocio/
│   ├── ProductoNegocio.java
│   ├── EmpleadoNegocio.java
│   ├── VentaNegocio.java
│   └── ...                   \# (BLL) Lógica de negocio, validaciones y orquestación.
│
└── 📁 presentacion/
├── FrmLogin.java
├── FrmDashboardAdmin.java
├── FrmGestionProductos.java
├── FrmRegistroCompra.java
└── ...                   \# (UI) Todas las ventanas y formularios Java Swing (JFrames, JInternalFrames).

````

### Explicación de las Capas

1.  **Capa de Presentación (UI) - `...presentacion/`**
    * Responsable de toda la interacción con el usuario.
    * No contiene lógica de negocio; solo captura eventos (clics, texto) y delega las tareas a la capa de negocio.
    * Utiliza `JInternalFrame` para crear una aplicación de múltiples ventanas internas (MDI).

2.  **Capa de Negocio (BLL) - `...negocio/`**
    * Actúa como un **Facade** (Fachada) para la capa de presentación.
    * Contiene todas las reglas de negocio (ej. `validar(producto)`, `registrarCompra(compra)`).
    * Coordina la capa de datos para ejecutar transacciones complejas (ej. registrar una venta y actualizar el stock al mismo tiempo).

3.  **Capa de Acceso a Datos (DAL) - `...datos/`**
    * Implementa el **Patrón DAO (Data Access Object)**.
    * Es la única capa que "sabe" cómo hablar con la base de datos (PostgreSQL).
    * Maneja todas las sentencias SQL, `PreparedStatement` y `ResultSet`.
    * Las interfaces (`ICrudDAO`) permiten que la capa de negocio dependa de abstracciones, no de implementaciones concretas.

4.  **Capa de Entidades (Modelo) - `...entidades/`**
    * Contiene los POJOs (Plain Old Java Objects) que modelan los datos.
    * Estas clases se utilizan para transferir información entre todas las capas.

---

## 🧠 Conceptos Clave de POO Aplicados

Este proyecto sirve como una demostración práctica de principios fundamentales de POO:

* **Herencia y Polimorfismo:** Es el núcleo del módulo de productos. Una clase abstracta `ItemVendible` define el contrato base. Las clases `ProductoUnitario`, `ProductoAGranel` y `Servicio` heredan de ella, cada una con sus propios atributos (ej. `stockActual` vs `stockMedido`) y lógica. El sistema maneja listas de `ItemVendible` (`List<ItemVendible>`) sin necesidad de saber el tipo específico de producto, demostrando polimorfismo en tiempo de ejecución.
* **Abstracción y Encapsulamiento:** El patrón DAO utiliza interfaces genéricas (`ICrudDAO<T>`) para abstraer la implementación de la base de datos. Las entidades encapsulan sus datos a través de métodos `get` y `set`.
* **Patrones de Diseño:**
    * **DAO (Data Access Object):** Separa la lógica de persistencia.
    * **Singleton:** Utilizado en `Conexion.java` para garantizar una única instancia de conexión a la base de datos.
    * **Facade:** La capa de `negocio` actúa como una fachada simple para la capa de presentación, ocultando la complejidad de las operaciones de datos.
    * **Observer (Listener):** El uso de `InternalFrameListener` en los `JInternalFrame` para refrescar datos es una implementación de este patrón.

---

## 🚀 Configuración y Puesta en Marcha

Sigue estos pasos para ejecutar el proyecto localmente.

### 1. Prerrequisitos
* **Java:** JDK 11 o superior.
* **IDE:** NetBeans 12.0 o superior (recomendado, ya que el proyecto incluye archivos `.form`).
* **Base de Datos:** PostgreSQL 14 o superior (con un gestor como pgAdmin).

### 2. Base de Datos
1.  Abre pgAdmin y crea una nueva base de datos llamada `ferreteria_db`.
    ```sql
    CREATE DATABASE ferreteria_db;
    ```
2.  Conéctate a `ferreteria_db` y ejecuta el script SQL optimizado (basado en tu esquema de la rama `main`) para crear todas las tablas, relaciones y datos iniciales:

    ```sql
    -- 1. CATEGORIA
    CREATE TABLE Categoria (
        categoria_id SERIAL PRIMARY KEY,
        nombre VARCHAR(100) NOT NULL UNIQUE,
        descripcion TEXT
    );

    -- 2. PROVEEDOR (Optimizado)
    CREATE TABLE Proveedor (
        proveedor_id SERIAL PRIMARY KEY,
        razon_social VARCHAR(150) NOT NULL,
        ruc VARCHAR(11) UNIQUE NOT NULL,
        email VARCHAR(100),
        telefono VARCHAR(15),
        direccion TEXT,
        activo BOOLEAN DEFAULT TRUE
    );

    -- 3. CLIENTE (Optimizado)
    CREATE TABLE Cliente (
        cliente_id SERIAL PRIMARY KEY,
        dni CHAR(8) UNIQUE NOT NULL,
        nombre VARCHAR(100) NOT NULL,
        apellidos VARCHAR(100) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        telefono VARCHAR(15),
        direccion TEXT,
        password_hash VARCHAR(255) NOT NULL,
        fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    -- 4. EMPLEADO (Optimizado)
    CREATE TABLE Empleado (
        empleado_id SERIAL PRIMARY KEY,
        dni CHAR(8) UNIQUE NOT NULL,
        nombre VARCHAR(100) NOT NULL,
        apellidos VARCHAR(100) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        rol VARCHAR(10) NOT NULL CHECK (rol IN ('ADMIN', 'VENDEDOR')),
        activo BOOLEAN DEFAULT TRUE
    );

    -- 5. PRODUCTO (Polimórfico y Optimizado)
    CREATE TABLE Producto (
        producto_id SERIAL PRIMARY KEY,
        sku VARCHAR(50) UNIQUE NOT NULL,
        nombre VARCHAR(200) NOT NULL,
        descripcion TEXT,
        categoria_id INT REFERENCES Categoria(categoria_id),
        activo BOOLEAN DEFAULT TRUE,
        tipo_producto VARCHAR(10) NOT NULL CHECK (tipo_producto IN ('UNITARIO', 'GRANEL', 'SERVICIO')),
        precio_unitario NUMERIC(10,2),
        stock_actual INTEGER,
        precio_por_medida NUMERIC(10,2),
        stock_medido NUMERIC(10,2),    
        unidad_medida VARCHAR(10),
        tarifa_servicio NUMERIC(10,2),
        CONSTRAINT chk_unitario CHECK (tipo_producto != 'UNITARIO' OR (precio_unitario IS NOT NULL AND stock_actual IS NOT NULL)),
        CONSTRAINT chk_granel CHECK (tipo_producto != 'GRANEL' OR (precio_por_medida IS NOT NULL AND stock_medido IS NOT NULL AND unidad_medida IS NOT NULL)),
        CONSTRAINT chk_servicio CHECK (tipo_producto != 'SERVICIO' OR (tarifa_servicio IS NOT NULL))
    );

    -- 6. VENTA (Optimizado)
    CREATE TABLE Venta (
        venta_id SERIAL PRIMARY KEY,
        cliente_id INT NOT NULL REFERENCES Cliente(cliente_id),
        empleado_id INT REFERENCES Empleado(empleado_id),
        fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        total NUMERIC(10,2) NOT NULL,
        metodo_pago VARCHAR(20) NOT NULL,
        referencia_transaccion VARCHAR(100), 
        estado_venta VARCHAR(15) DEFAULT 'COMPLETADA' CHECK (estado_venta IN ('COMPLETADA', 'ANULADA', 'PENDIENTE'))
    );

    -- 7. DETALLE_VENTA
    CREATE TABLE DetalleVenta (
        detalle_id SERIAL PRIMARY KEY,
        venta_id INT NOT NULL REFERENCES Venta(venta_id) ON DELETE CASCADE,
        producto_id INT NOT NULL REFERENCES Producto(producto_id),
        cantidad NUMERIC(10,2) NOT NULL CHECK (cantidad > 0),
        precio_historico NUMERIC(10,2) NOT NULL, 
        subtotal NUMERIC(10,2) NOT NULL
    );

    -- 8. COMPRA
    CREATE TABLE Compra (
        compra_id SERIAL PRIMARY KEY,
        proveedor_id INT NOT NULL REFERENCES Proveedor(proveedor_id),
        empleado_id INT NOT NULL REFERENCES Empleado(empleado_id),
        fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        total NUMERIC(10,2) NOT NULL,
        observaciones TEXT
    );

    -- 9. DETALLE_COMPRA
    CREATE TABLE DetalleCompra (
        detalle_compra_id SERIAL PRIMARY KEY,
        compra_id INT NOT NULL REFERENCES Compra(compra_id) ON DELETE CASCADE,
        producto_id INT NOT NULL REFERENCES Producto(producto_id),
        cantidad NUMERIC(10,2) NOT NULL CHECK (cantidad > 0),
        precio_compra NUMERIC(10,2) NOT NULL,
        subtotal NUMERIC(10,2) NOT NULL
    );
    
    -- DATOS INICIALES (ADMIN)
    -- Contraseña es 'admin123' (sin encriptar, ya que el login actual compara texto plano)
    INSERT INTO Empleado (dni, nombre, apellidos, email, password_hash, rol, activo)
    VALUES ('00000001', 'Admin', 'Principal', 'admin@ferreteria.com', 'admin123', 'ADMIN', TRUE);
    ```

### 3. Configuración del Proyecto

1.  **Conexión DB:** Abre el archivo `src/com/ferreteria/conexion/Conexion.java` y asegúrate de que el `USER` y `PASS` coincidan con tu configuración de PostgreSQL.
    ```java
    // src/com/ferreteria/conexion/Conexion.java
    private static final String URL = "jdbc:postgresql://localhost:5432/ferreteria_db";
    private static final String USER = "postgres"; // Tu usuario de Postgres
    private static final String PASS = "200414";   // Tu contraseña de Postgres
    ```
2.  **Librerías:** Asegúrate de que tu IDE (NetBeans) haya añadido la librería JDBC de PostgreSQL al Build Path. El archivo `postgresql-42.7.3.jar` debe estar en la carpeta de librerías del proyecto.

### 4. Ejecución
1.  Compila el proyecto.
2.  Ejecuta la clase principal `com.ferreteria.presentacion.FerreteriaApp.java`.
3.  Inicia sesión con las credenciales por defecto:
    * **Usuario:** `admin@ferreteria.com`
    * **Contraseña:** `admin123`

> **Nota de Seguridad:** El sistema actual compara contraseñas en texto plano (`EmpleadoNegocio.java`). El campo `password_hash` está nombrado para una futura implementación de hashing (ej. Bcrypt), pero actualmente funciona con texto plano.

---

## 👥 Autores

Este proyecto fue desarrollado por:

* **Jharol Stiwar Ruidias Mendoza** - `U23246718`
* **Edwin Siguas Astorga** - `U24309044`
* **Mauricio Nureña Diaz** - `U23255598`
* **Juan Useca Aguilar** - `U24256429`
* **Jose Fernando Sánchez Flores** - `U21217580`
````

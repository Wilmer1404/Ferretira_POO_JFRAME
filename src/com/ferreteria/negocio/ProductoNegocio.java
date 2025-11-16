package com.ferreteria.negocio;

import com.ferreteria.conexion.Conexion;
import com.ferreteria.datos.impl.ProductoDAOImpl;
import com.ferreteria.datos.interfaces.IProductoDAO;
import com.ferreteria.entidades.ItemVendible;
import com.ferreteria.entidades.ProductoAGranel;
import com.ferreteria.entidades.ProductoUnitario;
import com.ferreteria.entidades.Servicio;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de negocio para la gestión de productos e items vendibles.
 * Maneja toda la lógica relacionada con el inventario de productos:
 * - Gestión de productos unitarios, a granel y servicios
 * - Control de stock y validaciones
 * - Búsquedas y filtros de productos
 * - Actualización de inventario
 * 
 * Esta clase es fundamental para el funcionamiento del sistema de ventas
 * y gestión de inventario de la ferretería.
 */
public class ProductoNegocio {

    private final IProductoDAO DATOS_PROD;                              // DAO para acceso a datos de productos
    private static final Logger LOGGER = Logger.getLogger(ProductoNegocio.class.getName());

    /**
     * Constructor que inicializa el DAO de productos
     */
    public ProductoNegocio() {
        this.DATOS_PROD = new ProductoDAOImpl();
    }

    /**
     * Lista todos los items vendibles (productos y servicios) disponibles.
     * @return Lista de items vendibles
     */
    public List<ItemVendible> listar() {
        return this.DATOS_PROD.listarTodos();
    }

    /**
     * Busca items vendibles por nombre. Si el nombre es vacío o nulo,
     * se retornan todos los items.
     * @param nombre Nombre del item a buscar
     * @return Lista de items que coinciden con el nombre dado
     */
    public List<ItemVendible> buscar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return this.listar();
        }
        return this.DATOS_PROD.buscarPorNombre(nombre);
    }

    /**
     * Busca un item vendible por su ID.
     * @param id ID del item a buscar
     * @return Item vendible correspondiente al ID, o null si no existe
     */
    public ItemVendible buscarPorId(int id) {
        return this.DATOS_PROD.buscarPorId(id);
    }

    /**
     * Lista los items cuyo stock está por debajo del nivel mínimo establecido.
     * @param nivelMinimo Nivel mínimo de stock
     * @return Lista de items con stock bajo
     */
    public List<ItemVendible> listarStockBajo(int nivelMinimo) {
        return this.DATOS_PROD.listarStockBajo(nivelMinimo);
    }

    /**
     * Inserta un nuevo item vendible en el sistema.
     * @param item Item a insertar
     * @return Mensaje de error si ocurre un problema, o null si se inserta correctamente
     */
    public String insertar(ItemVendible item) {
        String error = validar(item);
        if (error != null) {
            return error;
        }
        if (this.DATOS_PROD.insertar(item)) {
            return null;
        } else {
            return "Error desconocido al guardar el producto en la base de datos.";
        }
    }

    /**
     * Actualiza la información de un item vendible existente.
     * @param item Item con la información actualizada
     * @return Mensaje de error si ocurre un problema, o null si se actualiza correctamente
     */
    public String actualizar(ItemVendible item) {
        if (item.getProductoId() <= 0) {
            return "No se ha seleccionado ningún producto para actualizar.";
        }
        String error = validar(item);
        if (error != null) {
            return error;
        }
        if (this.DATOS_PROD.actualizar(item)) {
            return null;
        } else {
            return "Error desconocido al actualizar el producto.";
        }
    }

    /**
     * Actualiza el stock de un producto de tipo unitario o a granel.
     * @param productoId ID del producto a actualizar
     * @param cantidad Cantidad a agregar o quitar del stock
     * @param tipoProducto Tipo de producto (Unitario o A Granel)
     * @param conn Conexión a la base de datos
     * @return true si se actualiza correctamente, false en caso contrario
     */
    public boolean actualizarStock(int productoId, double cantidad, String tipoProducto, Connection conn) {
        return this.DATOS_PROD.actualizarStock(productoId, cantidad, tipoProducto, conn);
    }

    /**
     * Desactiva un producto, impidiendo que siga siendo utilizado en el sistema.
     * Un producto sólo puede ser desactivado si su stock es cero.
     * @param id ID del producto a desactivar
     * @return Mensaje de error si ocurre un problema, o null si se desactiva correctamente
     */
    public String desactivar(int id) {
        ItemVendible item = this.DATOS_PROD.buscarPorId(id);
        if (item == null) {
            return "El producto que intenta desactivar no existe.";
        }
        if (item.obtenerStock() > 0) {
            return "Error: No se puede desactivar un producto que aún tiene stock (" + item.obtenerStock() + " " + item.obtenerUnidadParaGUI() + ").";
        }
        if (this.DATOS_PROD.eliminar(id)) {
            return null;
        } else {
            return "Error desconocido al desactivar el producto.";
        }
    }
    
    /**
     * Valida que un item vendible tenga toda la información necesaria y correcta.
     * @param item Item a validar
     * @return Mensaje de error si falta información o es incorrecta, o null si es válido
     */
    private String validar(ItemVendible item) {
        if (item.getNombre() == null || item.getNombre().trim().isEmpty()) {
            return "El nombre del producto no puede estar vacío.";
        }
        if (item.getSku() == null || item.getSku().trim().isEmpty()) {
            return "El SKU del producto no puede estar vacío.";
        }
        if (item instanceof ProductoUnitario) {
            ProductoUnitario pu = (ProductoUnitario) item;
            if (pu.getPrecioUnitario() <= 0) {
                return "El precio unitario debe ser mayor a 0.";
            }
            if (pu.getStockActual() < 0) {
                return "El stock no puede ser negativo.";
            }
        } else if (item instanceof ProductoAGranel) {
            ProductoAGranel pg = (ProductoAGranel) item;
            if (pg.getPrecioPorMedida() <= 0) {
                return "El precio por medida debe ser mayor a 0.";
            }
            if (pg.getStockMedido() < 0) {
                return "El stock medido no puede ser negativo.";
            }
            if (pg.getUnidadMedida() == null || pg.getUnidadMedida().trim().isEmpty()) {
                return "La unidad de medida (Kilo, Metro...) es obligatoria para productos a granel.";
            }
        } else if (item instanceof Servicio) {
            Servicio s = (Servicio) item;
            if (s.getTarifaServicio() <= 0) {
                return "La tarifa del servicio debe ser mayor a 0.";
            }
        }
        return null;
    }
}
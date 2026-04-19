DROP PROCEDURE IF EXISTS insertar_mascota;
DROP PROCEDURE IF EXISTS modificar_mascota;
DROP PROCEDURE IF EXISTS eliminar_mascota;
DROP PROCEDURE IF EXISTS buscar_mascota_por_id;
DROP PROCEDURE IF EXISTS listar_mascotas_activas;

--Procedures de MASCOTA
DELIMITER $$

CREATE PROCEDURE insertar_mascota(
    IN p_nombre VARCHAR(100),
    IN p_especie VARCHAR(50),
    IN p_raza VARCHAR(50),
    IN p_fecha_nacimiento DATE,
    IN p_esterilizado TINYINT,
    IN p_activo TINYINT,
    IN p_id_cliente INT,
    IN p_created_on DATETIME,
    IN p_modified_on DATETIME,
    IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO mascota(
        nombre,
        especie,
        raza,
        fecha_nacimiento,
        esterilizado,
        activo,
        id_cliente,
        created_on,
        modified_on,
        modified_by
    )
    VALUES(
        p_nombre,
        p_especie,
        p_raza,
        p_fecha_nacimiento,
        p_esterilizado,
        p_activo,
        p_id_cliente,
        p_created_on,
        p_modified_on,
        p_modified_by
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

CREATE PROCEDURE modificar_mascota(
    IN p_id_mascota INT,
    IN p_nombre VARCHAR(100),
    IN p_especie VARCHAR(50),
    IN p_raza VARCHAR(50),
    IN p_fecha_nacimiento DATE,
    IN p_esterilizado TINYINT,
    IN p_id_cliente INT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
    UPDATE mascota
    SET nombre = p_nombre,
        especie = p_especie,
        raza = p_raza,
        fecha_nacimiento = p_fecha_nacimiento,
        esterilizado = p_esterilizado,
        id_cliente = p_id_cliente,
        modified_on = p_modified_on,
        modified_by = p_modified_by
    WHERE id_mascota = p_id_mascota;
END$$

CREATE PROCEDURE eliminar_mascota(
    IN p_id_mascota INT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
    UPDATE mascota
    SET activo = 0,
        modified_on = p_modified_on,
        modified_by = p_modified_by
    WHERE id_mascota = p_id_mascota;
END$$

CREATE PROCEDURE buscar_mascota_por_id(
    IN p_id_mascota INT
)
BEGIN
    SELECT 
        m.id_mascota,
        m.nombre,
        m.especie,
        m.raza,
        m.fecha_nacimiento,
        m.esterilizado,
        m.activo,
        m.id_cliente,
        m.created_on,
        m.modified_on,
        m.modified_by
    FROM mascota m
    WHERE m.id_mascota = p_id_mascota;
END$$

CREATE PROCEDURE listar_mascotas_activas()
BEGIN
    SELECT 
        m.id_mascota,
        m.nombre,
        m.especie,
        m.raza,
        m.fecha_nacimiento,
        m.esterilizado,
        m.activo,
        m.id_cliente,
        m.created_on,
        m.modified_on,
        m.modified_by
    FROM mascota m
    WHERE m.activo = 1;
END$$

DELIMITER ;
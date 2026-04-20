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





DROP PROCEDURE IF EXISTS insertar_cliente;
DROP PROCEDURE IF EXISTS modificar_cliente;
DROP PROCEDURE IF EXISTS eliminar_cliente;
DROP PROCEDURE IF EXISTS buscar_cliente_por_id;
DROP PROCEDURE IF EXISTS listar_clientes_activos;

-- Procedures de CLIENTE
DELIMITER $$

CREATE PROCEDURE insertar_cliente(
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_observaciones VARCHAR(255),
    IN p_activo TINYINT,
    IN p_created_on DATETIME,
    IN p_modified_on DATETIME,
    IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
INSERT INTO cliente(
    nombres,
    apellidos,
    telefono,
    observaciones,
    activo,
    created_on,
    modified_on,
    modified_by
)
VALUES(
          p_nombres,
          p_apellidos,
          p_telefono,
          p_observaciones,
          p_activo,
          p_created_on,
          p_modified_on,
          p_modified_by
      );

SET p_id_generado = LAST_INSERT_ID();
END$$


CREATE PROCEDURE modificar_cliente(
    IN p_id_cliente INT,
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_observaciones VARCHAR(255),
    IN p_activo TINYINT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
UPDATE cliente
SET nombres = p_nombres,
    apellidos = p_apellidos,
    telefono = p_telefono,
    observaciones = p_observaciones,
    activo = p_activo,
    modified_on = p_modified_on,
    modified_by = p_modified_by
WHERE id_cliente = p_id_cliente;
END$$


CREATE PROCEDURE eliminar_cliente(
    IN p_id_cliente INT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
UPDATE cliente
SET activo = 0,
    modified_on = p_modified_on,
    modified_by = p_modified_by
WHERE id_cliente = p_id_cliente;
END$$


CREATE PROCEDURE buscar_cliente_por_id(
    IN p_id_cliente INT
)
BEGIN
SELECT
    c.id_cliente,
    c.nombres,
    c.apellidos,
    c.telefono,
    c.observaciones,
    c.activo,
    c.created_on,
    c.modified_on,
    c.modified_by
FROM cliente c
WHERE c.id_cliente = p_id_cliente;
END$$


CREATE PROCEDURE listar_clientes_activos()
BEGIN
SELECT
    c.id_cliente,
    c.nombres,
    c.apellidos,
    c.telefono,
    c.observaciones,
    c.activo,
    c.created_on,
    c.modified_on,
    c.modified_by
FROM cliente c
WHERE c.activo = 1;
END$$

DELIMITER ;









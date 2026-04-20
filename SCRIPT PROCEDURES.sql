DROP PROCEDURE IF EXISTS insertar_mascota;
DROP PROCEDURE IF EXISTS modificar_mascota;
DROP PROCEDURE IF EXISTS eliminar_mascota;
DROP PROCEDURE IF EXISTS buscar_mascota_por_id;
DROP PROCEDURE IF EXISTS listar_mascotas_activas;


DROP PROCEDURE IF EXISTS insertar_cita;
DROP PROCEDURE IF EXISTS modificar_cita;
DROP PROCEDURE IF EXISTS eliminar_cita;
DROP PROCEDURE IF EXISTS buscar_cita_por_id;
DROP PROCEDURE IF EXISTS listar_citas;

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
DELIMITER $$

CREATE PROCEDURE insertar_cita(
    IN p_fecha_hora_inicio DATETIME,
    IN p_fecha_hora_fin DATETIME,
    IN p_estado VARCHAR(20),
    IN p_id_mascota INT,
    IN p_id_veterinario INT,
    IN p_id_servicio INT,
    IN p_created_on DATETIME,
    OUT p_id_generado INT
)
BEGIN
INSERT INTO cita(
    fecha_hora_inicio,
    fecha_hora_fin,
    estado,
    id_mascota,
    id_veterinario,
    id_servicio,
    created_on
)
VALUES(
          p_fecha_hora_inicio,
          p_fecha_hora_fin,
          p_estado,
          p_id_mascota,
          p_id_veterinario,
          p_id_servicio,
          p_created_on
      );
SET p_id_generado = LAST_INSERT_ID();
END$$

CREATE PROCEDURE modificar_cita(
    IN p_id_cita INT,
    IN p_fecha_hora_inicio DATETIME,
    IN p_fecha_hora_fin DATETIME,
    IN p_estado VARCHAR(20),
    IN p_id_mascota INT,
    IN p_id_veterinario INT,
    IN p_id_servicio INT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
UPDATE cita
SET fecha_hora_inicio = p_fecha_hora_inicio,
    fecha_hora_fin = p_fecha_hora_fin,
    estado = p_estado,
    id_mascota = p_id_mascota,
    id_veterinario = p_id_veterinario,
    id_servicio = p_id_servicio,
    modified_on = p_modified_on,
    modified_by = p_modified_by
WHERE id_cita = p_id_cita;
END$$

CREATE PROCEDURE eliminar_cita(
    IN p_id_cita INT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
UPDATE cita
SET estado = 'CANCELADA',
    modified_on = p_modified_on,
    modified_by = p_modified_by
WHERE id_cita = p_id_cita;
END$$

CREATE PROCEDURE buscar_cita_por_id(
    IN p_id_cita INT
)
BEGIN
SELECT
    -- Atributos de Cita
    c.id_cita,
    c.fecha_hora_inicio,
    c.fecha_hora_fin,
    c.estado,
    c.created_on,
    c.modified_on,
    c.modified_by,
    m.id_mascota, m.nombre AS mascota_nombre, m.especie, m.raza,
    m.fecha_nacimiento, m.esterilizado, m.activo AS mascota_activo,
    m.id_cliente,
    v.id_veterinario, v.cmpv, v.especialidad,
    u.username, u.contrasena_hash, u.nombres AS vet_nombres,
    u.apellidos AS vet_apellidos, u.telefono AS vet_telefono,
    u.activo AS vet_activo, u.rol,
    s.id_servicio, s.nombre AS servicio_nombre, s.tipo_servicio,
    s.duracion_minutos, s.precio_referencial, s.activo AS servicio_activo
FROM cita c
         INNER JOIN mascota m ON c.id_mascota = m.id_mascota
         INNER JOIN veterinario v ON c.id_veterinario = v.id_veterinario
         INNER JOIN usuario u ON v.id_veterinario = u.id_usuario
         INNER JOIN servicio s ON c.id_servicio = s.id_servicio
WHERE c.id_cita = p_id_cita;
END$$

CREATE PROCEDURE listar_citas()
BEGIN
SELECT
    c.id_cita,
    c.fecha_hora_inicio,
    c.fecha_hora_fin,
    c.estado,
    c.created_on,
    c.modified_on,
    c.modified_by,
    m.id_mascota, m.nombre AS mascota_nombre, m.especie, m.raza,
    m.fecha_nacimiento, m.esterilizado, m.activo AS mascota_activo,
    m.id_cliente,
    v.id_veterinario, v.cmpv, v.especialidad,
    u.username, u.contrasena_hash, u.nombres AS vet_nombres,
    u.apellidos AS vet_apellidos, u.telefono AS vet_telefono,
    u.activo AS vet_activo, u.rol,
    s.id_servicio, s.nombre AS servicio_nombre, s.tipo_servicio,
    s.duracion_minutos, s.precio_referencial, s.activo AS servicio_activo
FROM cita c
         INNER JOIN mascota m ON c.id_mascota = m.id_mascota
         INNER JOIN veterinario v ON c.id_veterinario = v.id_veterinario
         INNER JOIN usuario u ON v.id_veterinario = u.id_usuario
         INNER JOIN servicio s ON c.id_servicio = s.id_servicio;
END$$


DROP PROCEDURE IF EXISTS insertar_veterinario;
DROP PROCEDURE IF EXISTS modificar_veterinario;
DROP PROCEDURE IF EXISTS eliminar_veterinario;
DROP PROCEDURE IF EXISTS buscar_veterinario_por_id;
DROP PROCEDURE IF EXISTS listar_veterinarios;

DELIMITER $$

CREATE PROCEDURE insertar_veterinario(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_rol ENUM('ADMINISTRADOR', 'VETERINARIO'),
    IN p_cmpv VARCHAR(30),
    IN p_especialidad VARCHAR(100),
    IN p_modified_by INT,
    OUT p_id_usuario INT
)
BEGIN
    INSERT INTO usuario(username, contrasena_hash, nombres, apellidos, telefono, activo, rol, created_on, modified_on, modified_by)
    VALUES(p_username, p_contrasena_hash, p_nombres, p_apellidos, p_telefono, 1, p_rol, NOW(), NOW(), p_modified_by);
    SET p_id_usuario = LAST_INSERT_ID();
    INSERT INTO veterinario(id_veterinario, cmpv, especialidad)
    VALUES(p_id_usuario, p_cmpv, p_especialidad);
END$$

CREATE PROCEDURE modificar_veterinario(
    IN p_id_usuario INT,
    IN p_username VARCHAR(50),
    IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_cmpv VARCHAR(30),
    IN p_especialidad VARCHAR(100),
    IN p_modified_by INT
)
BEGIN
    UPDATE usuario SET username = p_username, contrasena_hash = p_contrasena_hash,
        nombres = p_nombres, apellidos = p_apellidos, telefono = p_telefono,
        modified_on = NOW(), modified_by = p_modified_by
    WHERE id_usuario = p_id_usuario;
    UPDATE veterinario SET cmpv = p_cmpv, especialidad = p_especialidad
    WHERE id_veterinario = p_id_usuario;
END$$

CREATE PROCEDURE eliminar_veterinario(
    IN p_id_usuario INT
)
BEGIN
    UPDATE usuario SET activo = 0, modified_on = NOW()
    WHERE id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE buscar_veterinario_por_id(
    IN p_id_usuario INT
)
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos,
        u.telefono, u.activo, u.rol, u.created_on, u.modified_on, u.modified_by,
        v.cmpv, v.especialidad
    FROM usuario u
    INNER JOIN veterinario v ON u.id_usuario = v.id_veterinario
    WHERE u.id_usuario = p_id_usuario;
END$$

CREATE PROCEDURE listar_veterinarios()
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos,
        u.telefono, u.activo, u.rol, u.created_on, u.modified_on, u.modified_by,
        v.cmpv, v.especialidad
    FROM usuario u
    INNER JOIN veterinario v ON u.id_usuario = v.id_veterinario
    WHERE u.activo = 1;

END$$

DELIMITER ;









-- =========================================================
-- Procedures de RECORDATORIO
-- =========================================================

DROP PROCEDURE IF EXISTS insertar_recordatorio;
DROP PROCEDURE IF EXISTS modificar_recordatorio;
DROP PROCEDURE IF EXISTS eliminar_recordatorio;
DROP PROCEDURE IF EXISTS buscar_recordatorio_por_id;
DROP PROCEDURE IF EXISTS listar_recordatorios;

DELIMITER $$

CREATE PROCEDURE insertar_recordatorio(
    IN p_fecha_programada DATETIME,
    IN p_canal ENUM('WHATSAPP'),
    IN p_estado_seguimiento ENUM('PENDIENTE', 'ENVIADO', 'CONTACTADO', 'CERRADO'),
    IN p_mensaje VARCHAR(255),
    IN p_id_cita INT,
    IN p_modified_by INT,
    OUT p_id_recordatorio INT
)
BEGIN
    INSERT INTO recordatorio(fecha_programada, canal, estado_seguimiento, mensaje, id_cita, created_on, modified_on, modified_by)
    VALUES(p_fecha_programada, p_canal, p_estado_seguimiento, p_mensaje, p_id_cita, NOW(), NOW(), p_modified_by);
    SET p_id_recordatorio = LAST_INSERT_ID();
END$$

CREATE PROCEDURE modificar_recordatorio(
    IN p_id_recordatorio INT,
    IN p_fecha_programada DATETIME,
    IN p_canal ENUM('WHATSAPP'),
    IN p_estado_seguimiento ENUM('PENDIENTE', 'ENVIADO', 'CONTACTADO', 'CERRADO'),
    IN p_mensaje VARCHAR(255),
    IN p_id_cita INT,
    IN p_modified_by INT
)
BEGIN
    UPDATE recordatorio SET fecha_programada = p_fecha_programada, canal = p_canal,
        estado_seguimiento = p_estado_seguimiento, mensaje = p_mensaje,
        id_cita = p_id_cita, modified_on = NOW(), modified_by = p_modified_by
    WHERE id_recordatorio = p_id_recordatorio;
END$$

CREATE PROCEDURE eliminar_recordatorio(
    IN p_id_recordatorio INT
)
BEGIN
    DELETE FROM recordatorio WHERE id_recordatorio = p_id_recordatorio;
END$$

CREATE PROCEDURE buscar_recordatorio_por_id(
    IN p_id_recordatorio INT
)
BEGIN
    SELECT id_recordatorio, fecha_programada, canal, estado_seguimiento, mensaje,
        id_cita, created_on, modified_on, modified_by
    FROM recordatorio
    WHERE id_recordatorio = p_id_recordatorio;
END$$

CREATE PROCEDURE listar_recordatorios()
BEGIN
    SELECT id_recordatorio, fecha_programada, canal, estado_seguimiento, mensaje,
        id_cita, created_on, modified_on, modified_by
    FROM recordatorio;
END$$

DELIMITER ;

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

DELIMITER $$
--Procedures de CITA
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
    -- Atributos de Mascota
    m.id_mascota, m.nombre AS mascota_nombre, m.especie, m.raza,
    m.fecha_nacimiento, m.esterilizado, m.activo AS mascota_activo,
    m.id_cliente,
    -- Atributos de Veterinario (Cruce Veterinario + Usuario)
    v.id_veterinario, v.cmpv, v.especialidad,
    u.username, u.contrasena_hash, u.nombres AS vet_nombres,
    u.apellidos AS vet_apellidos, u.telefono AS vet_telefono,
    u.activo AS vet_activo, u.rol,
    -- Atributos de Servicio
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
DELIMITER ;
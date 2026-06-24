USE vetcitas_db;
DELIMITER $$

DROP PROCEDURE IF EXISTS insertar_usuario_base $$
DROP PROCEDURE IF EXISTS asignar_rol_a_usuario $$
DROP PROCEDURE IF EXISTS revocar_rol_de_usuario $$
DROP PROCEDURE IF EXISTS listar_roles_de_usuario $$
DROP PROCEDURE IF EXISTS listar_permisos_por_rol $$
DROP PROCEDURE IF EXISTS listar_permisos_por_usuario $$

DROP PROCEDURE IF EXISTS insertar_administrador $$
DROP PROCEDURE IF EXISTS modificar_administrador $$
DROP PROCEDURE IF EXISTS eliminar_administrador_logico $$
DROP PROCEDURE IF EXISTS buscar_administrador_por_id $$
DROP PROCEDURE IF EXISTS listar_administradores $$

DROP PROCEDURE IF EXISTS insertar_veterinario $$
DROP PROCEDURE IF EXISTS modificar_veterinario $$
DROP PROCEDURE IF EXISTS buscar_veterinario_por_id $$
DROP PROCEDURE IF EXISTS listar_veterinarios $$
DROP PROCEDURE IF EXISTS listar_veterinarios_disponibles $$

DROP PROCEDURE IF EXISTS insertar_recepcionista $$
DROP PROCEDURE IF EXISTS modificar_recepcionista $$
DROP PROCEDURE IF EXISTS buscar_recepcionista_por_id $$
DROP PROCEDURE IF EXISTS listar_recepcionistas $$
DROP PROCEDURE IF EXISTS eliminar_usuario_logico $$
DROP PROCEDURE IF EXISTS modificar_usuario_basico $$

DROP PROCEDURE IF EXISTS insertar_permiso $$
DROP PROCEDURE IF EXISTS modificar_permiso $$
DROP PROCEDURE IF EXISTS eliminar_permiso_logico $$
DROP PROCEDURE IF EXISTS buscar_permiso_por_id $$
DROP PROCEDURE IF EXISTS listar_permisos $$

DROP PROCEDURE IF EXISTS insertar_cliente $$
DROP PROCEDURE IF EXISTS modificar_cliente $$
DROP PROCEDURE IF EXISTS eliminar_cliente $$
DROP PROCEDURE IF EXISTS buscar_cliente_por_id $$
DROP PROCEDURE IF EXISTS listar_clientes_activos $$

DROP PROCEDURE IF EXISTS insertar_mascota $$
DROP PROCEDURE IF EXISTS modificar_mascota $$
DROP PROCEDURE IF EXISTS eliminar_mascota $$
DROP PROCEDURE IF EXISTS buscar_mascota_por_id $$
DROP PROCEDURE IF EXISTS listar_mascotas_activas $$

DROP PROCEDURE IF EXISTS insertar_servicio $$
DROP PROCEDURE IF EXISTS modificar_servicio $$
DROP PROCEDURE IF EXISTS eliminar_servicio $$
DROP PROCEDURE IF EXISTS deshabilitar_servicio $$
DROP PROCEDURE IF EXISTS buscar_servicio_por_id $$
DROP PROCEDURE IF EXISTS listar_servicios $$

DROP PROCEDURE IF EXISTS insertar_horario_veterinario $$
DROP PROCEDURE IF EXISTS modificar_horario_veterinario $$
DROP PROCEDURE IF EXISTS eliminar_horario_veterinario $$
DROP PROCEDURE IF EXISTS buscar_horario_veterinario_por_id $$
DROP PROCEDURE IF EXISTS listar_horarios_veterinario $$
DROP PROCEDURE IF EXISTS listar_horarios_veterinario_todos $$

DROP PROCEDURE IF EXISTS existe_solapamiento_cita $$
DROP PROCEDURE IF EXISTS validar_disponibilidad_cita $$
DROP PROCEDURE IF EXISTS insertar_cita $$
DROP PROCEDURE IF EXISTS modificar_cita $$
DROP PROCEDURE IF EXISTS cancelar_cita $$
DROP PROCEDURE IF EXISTS confirmar_cita $$
DROP PROCEDURE IF EXISTS marcar_cita_atendida $$
DROP PROCEDURE IF EXISTS marcar_cita_no_asistio $$
DROP PROCEDURE IF EXISTS marcar_cita_en_consulta $$

DROP PROCEDURE IF EXISTS listar_citas_por_veterinario_fecha $$
DROP PROCEDURE IF EXISTS buscar_cita_por_id $$
DROP PROCEDURE IF EXISTS listar_citas $$

DROP PROCEDURE IF EXISTS insertar_atencion $$
DROP PROCEDURE IF EXISTS modificar_atencion $$
DROP PROCEDURE IF EXISTS eliminar_atencion $$
DROP PROCEDURE IF EXISTS buscar_atencion_por_id $$
DROP PROCEDURE IF EXISTS buscar_atencion_por_cita $$
DROP PROCEDURE IF EXISTS listar_atenciones $$

DROP PROCEDURE IF EXISTS insertar_recordatorio $$
DROP PROCEDURE IF EXISTS modificar_recordatorio $$
DROP PROCEDURE IF EXISTS eliminar_recordatorio $$
DROP PROCEDURE IF EXISTS eliminar_recordatorios_por_cita $$
DROP PROCEDURE IF EXISTS buscar_recordatorio_por_id $$
DROP PROCEDURE IF EXISTS listar_recordatorios $$

DROP PROCEDURE IF EXISTS validar_disponibilidad_slot $$
DROP PROCEDURE IF EXISTS reprogramar_cita $$
DROP PROCEDURE IF EXISTS cambiar_veterinario_cita $$
DROP PROCEDURE IF EXISTS contar_citas_por_estado_en_rango $$
DROP PROCEDURE IF EXISTS contar_citas_por_veterinario_en_rango $$

DROP PROCEDURE IF EXISTS listar_ultimas_atenciones_por_veterinario $$
DROP PROCEDURE IF EXISTS contar_atenciones_por_veterinario_en_mes $$
DROP PROCEDURE IF EXISTS sumar_montos_netos_atenciones_por_mes $$
DROP PROCEDURE IF EXISTS top_servicios_por_veterinario $$
DROP PROCEDURE IF EXISTS top_veterinarios_por_atenciones $$
DROP PROCEDURE IF EXISTS top_servicios_mas_demandados $$

DROP PROCEDURE IF EXISTS contar_clientes_activos $$
DROP PROCEDURE IF EXISTS contar_clientes_nuevos_en_mes $$
DROP PROCEDURE IF EXISTS contar_mascotas_activas $$

DROP PROCEDURE IF EXISTS marcar_recordatorio_enviado $$
DROP PROCEDURE IF EXISTS contar_recordatorios_pendientes $$

DROP PROCEDURE IF EXISTS autenticar_usuario $$
DROP PROCEDURE IF EXISTS cambiar_contrasena_usuario $$

DROP PROCEDURE IF EXISTS usuario_tiene_rol $$
DROP PROCEDURE IF EXISTS restablecer_contrasena_usuario $$



CREATE PROCEDURE insertar_usuario_base(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash CHAR(64),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_modified_by INT,
    OUT p_id_usuario INT
)
BEGIN
    INSERT INTO usuario(
        username,
        contrasena_hash,
        nombres,
        apellidos,
        telefono,
        email,
        activo,
        created_on,
        modified_on,
        modified_by
    )
    VALUES(
        p_username,
        p_contrasena_hash,
        p_nombres,
        p_apellidos,
        p_telefono,
        p_email,
        1,
        DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        p_modified_by
    );

    SET p_id_usuario = LAST_INSERT_ID();
END $$

CREATE PROCEDURE asignar_rol_a_usuario(IN p_id_usuario INT, IN p_codigo_rol VARCHAR(30))
BEGIN
    DECLARE v_id_rol INT;
    DECLARE v_cantidad_roles INT DEFAULT 0;

    SELECT id_rol INTO v_id_rol
    FROM rol_sistema
    WHERE codigo = p_codigo_rol;

    IF v_id_rol IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Rol no existe';
    END IF;

    IF EXISTS (SELECT 1 FROM usuario_rol WHERE id_usuario = p_id_usuario AND id_rol = v_id_rol) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario ya tiene ese rol';
    END IF;

    SELECT COUNT(*) INTO v_cantidad_roles
    FROM usuario_rol WHERE id_usuario = p_id_usuario;

    IF v_cantidad_roles >= 3 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se permite mas de 3 roles por usuario';
    END IF;

    INSERT INTO usuario_rol(id_usuario, id_rol) VALUES(p_id_usuario, v_id_rol);
END $$

CREATE PROCEDURE revocar_rol_de_usuario(IN p_id_usuario INT, IN p_codigo_rol VARCHAR(30))
BEGIN
    DECLARE v_id_rol INT;
    DECLARE v_total_roles INT;
    DECLARE v_es_super_admin TINYINT DEFAULT 0;

    SELECT id_rol INTO v_id_rol FROM rol_sistema WHERE codigo = p_codigo_rol;
    SELECT COUNT(*) INTO v_total_roles FROM usuario_rol WHERE id_usuario = p_id_usuario;

    IF v_total_roles <= 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede dejar al usuario sin roles';
    END IF;

    IF p_codigo_rol = 'ADMINISTRADOR' THEN
        SELECT COALESCE(es_super_admin, 0) INTO v_es_super_admin
        FROM administrador
        WHERE id_administrador = p_id_usuario;

        IF v_es_super_admin = 1 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede revocar el rol ADMINISTRADOR a un SuperAdmin';
        END IF;
    END IF;

    DELETE FROM usuario_rol WHERE id_usuario = p_id_usuario AND id_rol = v_id_rol;
END $$

CREATE PROCEDURE listar_roles_de_usuario(IN p_id_usuario INT)
BEGIN
    SELECT r.id_rol, r.codigo, r.descripcion
    FROM usuario_rol ur
    JOIN rol_sistema r ON r.id_rol = ur.id_rol
    WHERE ur.id_usuario = p_id_usuario
    ORDER BY r.codigo;
END $$

CREATE PROCEDURE listar_permisos_por_rol(IN p_codigo_rol VARCHAR(30))
BEGIN
    SELECT p.id_permiso, p.nombre, p.descripcion
    FROM rol_sistema r
    JOIN rol_permiso rp ON rp.id_rol = r.id_rol
    JOIN permiso p ON p.id_permiso = rp.id_permiso
    WHERE r.codigo = p_codigo_rol AND p.activo = 1
    ORDER BY p.nombre;
END $$

CREATE PROCEDURE listar_permisos_por_usuario(IN p_id_usuario INT)
BEGIN
    SELECT DISTINCT p.id_permiso, p.nombre, p.descripcion
    FROM usuario_rol ur
    JOIN rol_permiso rp ON rp.id_rol = ur.id_rol
    JOIN permiso p ON p.id_permiso = rp.id_permiso
    WHERE ur.id_usuario = p_id_usuario AND p.activo = 1
    ORDER BY p.nombre;
END $$

CREATE PROCEDURE eliminar_usuario_logico(IN p_id_usuario INT, IN p_modified_by INT)
BEGIN
    DECLARE v_es_super_admin TINYINT DEFAULT 0;

    SELECT COALESCE(es_super_admin, 0) INTO v_es_super_admin
    FROM administrador
    WHERE id_administrador = p_id_usuario;

    IF v_es_super_admin = 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar a un SuperAdmin';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM usuario_rol ur
        INNER JOIN rol_sistema r ON r.id_rol = ur.id_rol
        INNER JOIN cita c ON c.id_veterinario = ur.id_usuario
        WHERE ur.id_usuario = p_id_usuario
          AND r.codigo = 'VETERINARIO'
          AND c.estado IN ('CONFIRMADA', 'EN_CONSULTA')
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede inactivar al veterinario porque tiene citas confirmadas o en consulta';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM usuario_rol ur
        INNER JOIN rol_sistema r ON r.id_rol = ur.id_rol
        INNER JOIN cita c ON c.id_veterinario = ur.id_usuario
        WHERE ur.id_usuario = p_id_usuario
          AND r.codigo = 'VETERINARIO'
          AND c.estado = 'PENDIENTE'
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede inactivar al veterinario porque tiene citas pendientes';
    END IF;

    UPDATE usuario
    SET activo = 0, modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), modified_by = p_modified_by
    WHERE id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE modificar_usuario_basico(
    IN p_id_usuario INT,
    IN p_username VARCHAR(50),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_activo TINYINT,
    IN p_modified_by INT
)
BEGIN
    IF p_activo = 0 AND EXISTS (
        SELECT 1
        FROM usuario u
        INNER JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario
        INNER JOIN rol_sistema r ON r.id_rol = ur.id_rol
        INNER JOIN cita c ON c.id_veterinario = u.id_usuario
        WHERE u.id_usuario = p_id_usuario
          AND u.activo = 1
          AND r.codigo = 'VETERINARIO'
          AND c.estado IN ('CONFIRMADA', 'EN_CONSULTA')
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede inactivar al veterinario porque tiene citas confirmadas o en consulta';
    END IF;

    IF p_activo = 0 AND EXISTS (
        SELECT 1
        FROM usuario u
        INNER JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario
        INNER JOIN rol_sistema r ON r.id_rol = ur.id_rol
        INNER JOIN cita c ON c.id_veterinario = u.id_usuario
        WHERE u.id_usuario = p_id_usuario
          AND u.activo = 1
          AND r.codigo = 'VETERINARIO'
          AND c.estado = 'PENDIENTE'
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede inactivar al veterinario porque tiene citas pendientes';
    END IF;

    UPDATE usuario
    SET username = p_username,
        nombres = p_nombres,
        apellidos = p_apellidos,
        telefono = p_telefono,
        email = p_email,
        activo = p_activo,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE insertar_administrador(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_area VARCHAR(100),
    IN p_es_super_admin TINYINT,
    IN p_modified_by INT,
    OUT p_id_usuario INT
)
BEGIN
    DECLARE v_super_existente INT DEFAULT 0;

    IF p_es_super_admin = 1 THEN
        SELECT COUNT(*) INTO v_super_existente
        FROM administrador
        WHERE es_super_admin = 1;

        IF v_super_existente > 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe un SuperAdmin';
        END IF;
    END IF;

    START TRANSACTION;

    CALL insertar_usuario_base(
        p_username,
        p_contrasena_hash,
        p_nombres,
        p_apellidos,
        p_telefono,
        p_email,
        p_modified_by,
        p_id_usuario
    );

    INSERT INTO administrador(id_administrador, area, es_super_admin)
    VALUES(p_id_usuario, p_area, COALESCE(p_es_super_admin, 0));

    CALL asignar_rol_a_usuario(p_id_usuario, 'ADMINISTRADOR');

    COMMIT;
END $$

CREATE PROCEDURE modificar_administrador(
    IN p_id_administrador INT,
    IN p_username VARCHAR(50),
    IN p_contrasena_hash CHAR(64),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_activo TINYINT,
    IN p_area VARCHAR(100),
    IN p_modified_by INT
)
BEGIN
    DECLARE v_es_super_admin TINYINT DEFAULT 0;

    SELECT COALESCE(es_super_admin, 0) INTO v_es_super_admin
    FROM administrador
    WHERE id_administrador = p_id_administrador;

    IF v_es_super_admin = 1 AND p_activo = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede desactivar a un SuperAdmin';
    END IF;

    UPDATE usuario
    SET username = p_username,
        contrasena_hash = p_contrasena_hash,
        nombres = p_nombres,
        apellidos = p_apellidos,
        telefono = p_telefono,
        email = p_email,
        activo = p_activo,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_usuario = p_id_administrador;

    UPDATE administrador
    SET area = p_area
    WHERE id_administrador = p_id_administrador;
END $$


CREATE PROCEDURE eliminar_administrador_logico(IN p_id_administrador INT, IN p_modified_by INT)
BEGIN
    DECLARE v_es_super_admin TINYINT DEFAULT 0;

    SELECT COALESCE(es_super_admin, 0) INTO v_es_super_admin
    FROM administrador
    WHERE id_administrador = p_id_administrador;

    IF v_es_super_admin = 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar a un SuperAdmin';
    END IF;

    CALL eliminar_usuario_logico(p_id_administrador, p_modified_by);
END $$

CREATE PROCEDURE buscar_administrador_por_id(IN p_id_usuario INT)
BEGIN
    SELECT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        a.area,
        a.es_super_admin
    FROM usuario u
    JOIN administrador a ON a.id_administrador = u.id_usuario
    WHERE u.id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE listar_administradores()
BEGIN
    SELECT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        a.area,
        a.es_super_admin
    FROM usuario u
    JOIN administrador a ON a.id_administrador = u.id_usuario
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_veterinario(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_cmpv VARCHAR(30),
    IN p_especialidad VARCHAR(100),
    IN p_modified_by INT,
    OUT p_id_usuario INT
)
BEGIN
    START TRANSACTION;

    CALL insertar_usuario_base(
        p_username,
        p_contrasena_hash,
        p_nombres,
        p_apellidos,
        p_telefono,
        p_email,
        p_modified_by,
        p_id_usuario
    );

    INSERT INTO veterinario(id_veterinario, cmpv, especialidad)
    VALUES(p_id_usuario, p_cmpv, p_especialidad);

    CALL asignar_rol_a_usuario(p_id_usuario, 'VETERINARIO');

    COMMIT;
END $$

CREATE PROCEDURE modificar_veterinario(
    IN p_id_veterinario INT,
    IN p_username VARCHAR(50),
    IN p_contrasena_hash CHAR(64),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_activo TINYINT,
    IN p_cmpv VARCHAR(30),
    IN p_especialidad VARCHAR(100),
    IN p_modified_by INT
)
BEGIN
    UPDATE usuario
    SET username = p_username,
        contrasena_hash = p_contrasena_hash,
        nombres = p_nombres,
        apellidos = p_apellidos,
        telefono = p_telefono,
        email = p_email,
        activo = p_activo,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_usuario = p_id_veterinario;

    UPDATE veterinario
    SET cmpv = p_cmpv,
        especialidad = p_especialidad
    WHERE id_veterinario = p_id_veterinario;
END $$

CREATE PROCEDURE buscar_veterinario_por_id(IN p_id_usuario INT)
BEGIN
    SELECT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        v.cmpv,
        v.especialidad
    FROM usuario u
    JOIN veterinario v ON v.id_veterinario = u.id_usuario
    WHERE u.id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE listar_veterinarios()
BEGIN
    SELECT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        v.cmpv,
        v.especialidad
    FROM usuario u
    JOIN veterinario v ON v.id_veterinario = u.id_usuario
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_recepcionista(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_area VARCHAR(100),
    IN p_modified_by INT,
    OUT p_id_usuario INT
)
BEGIN
    START TRANSACTION;

    CALL insertar_usuario_base(
        p_username,
        p_contrasena_hash,
        p_nombres,
        p_apellidos,
        p_telefono,
        p_email,
        p_modified_by,
        p_id_usuario
    );

    INSERT INTO recepcionista(id_recepcionista, area)
    VALUES(p_id_usuario, p_area);

    CALL asignar_rol_a_usuario(p_id_usuario, 'RECEPCIONISTA');

    COMMIT;
END $$

CREATE PROCEDURE modificar_recepcionista(
    IN p_id_recepcionista INT,
    IN p_username VARCHAR(50),
    IN p_contrasena_hash CHAR(64),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_activo TINYINT,
    IN p_area VARCHAR(100),
    IN p_modified_by INT
)
BEGIN
    UPDATE usuario
    SET username = p_username,
        contrasena_hash = p_contrasena_hash,
        nombres = p_nombres,
        apellidos = p_apellidos,
        telefono = p_telefono,
        email = p_email,
        activo = p_activo,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_usuario = p_id_recepcionista;

    UPDATE recepcionista
    SET area = p_area
    WHERE id_recepcionista = p_id_recepcionista;
END $$

CREATE PROCEDURE buscar_recepcionista_por_id(IN p_id_usuario INT)
BEGIN
    SELECT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        r.area
    FROM usuario u
    JOIN recepcionista r ON r.id_recepcionista = u.id_usuario
    WHERE u.id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE listar_recepcionistas()
BEGIN
    SELECT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        r.area
    FROM usuario u
    JOIN recepcionista r ON r.id_recepcionista = u.id_usuario
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_permiso(IN p_nombre VARCHAR(100), IN p_descripcion VARCHAR(255), OUT p_id_generado INT)
BEGIN
    INSERT INTO permiso(nombre, descripcion, activo) VALUES(p_nombre, p_descripcion, 1);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_permiso(IN p_id_permiso INT, IN p_nombre VARCHAR(100), IN p_descripcion VARCHAR(255), IN p_activo TINYINT)
BEGIN
    UPDATE permiso SET nombre = p_nombre, descripcion = p_descripcion, activo = p_activo WHERE id_permiso = p_id_permiso;
END $$

CREATE PROCEDURE eliminar_permiso_logico(IN p_id_permiso INT)
BEGIN
    UPDATE permiso SET activo = 0 WHERE id_permiso = p_id_permiso;
END $$

CREATE PROCEDURE buscar_permiso_por_id(IN p_id_permiso INT)
BEGIN
    SELECT id_permiso, nombre, descripcion FROM permiso WHERE id_permiso = p_id_permiso;
END $$

CREATE PROCEDURE listar_permisos()
BEGIN
    SELECT id_permiso, nombre, descripcion FROM permiso WHERE activo = 1 ORDER BY nombre;
END $$

CREATE PROCEDURE insertar_cliente(
    IN p_dni VARCHAR(8),IN p_nombres VARCHAR(100),IN p_apellidos VARCHAR(100),IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),IN p_observaciones VARCHAR(255),IN p_activo TINYINT,
    IN p_created_on DATETIME,IN p_modified_on DATETIME,IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO cliente(
        dni,nombres,apellidos,telefono,email,observaciones,
        activo,created_on,modified_on,modified_by
    )
    VALUES(
        p_dni,p_nombres,p_apellidos,p_telefono,p_email,p_observaciones,p_activo,p_created_on,
        p_modified_on,p_modified_by
    );

    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_cliente(
    IN p_id_cliente INT,
    IN p_dni VARCHAR(8),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(150),
    IN p_observaciones VARCHAR(255),
    IN p_activo TINYINT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
    UPDATE cliente
    SET dni = p_dni,
        nombres = p_nombres,
        apellidos = p_apellidos,
        telefono = p_telefono,
        email = p_email,
        observaciones = p_observaciones,
        activo = p_activo,
        modified_on = p_modified_on,
        modified_by = p_modified_by
    WHERE id_cliente = p_id_cliente;
END $$

CREATE PROCEDURE eliminar_cliente(IN p_id_cliente INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    DECLARE v_confirmadas INT DEFAULT 0;

    -- Bloqueo: no se puede eliminar si el cliente tiene una cita CONFIRMADA en el futuro
    SELECT COUNT(*) INTO v_confirmadas
    FROM cita c
    INNER JOIN mascota m ON m.id_mascota = c.id_mascota
    WHERE m.id_cliente = p_id_cliente
      AND c.estado = 'CONFIRMADA'
      AND c.fecha_hora_inicio > DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR);

    IF v_confirmadas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar: el cliente tiene una cita confirmada en el futuro.';
    END IF;

    -- Cancelar automáticamente las citas pendientes del cliente
    UPDATE cita c
    INNER JOIN mascota m ON m.id_mascota = c.id_mascota
    SET c.estado = 'CANCELADA',
        c.motivo_cancelacion = 'Cliente eliminado',
        c.fecha_cancelacion = p_modified_on,
        c.id_usuario_cancelacion = p_modified_by,
        c.modified_on = p_modified_on,
        c.modified_by = p_modified_by
    WHERE m.id_cliente = p_id_cliente
      AND c.estado = 'PENDIENTE';

    -- Al eliminar un cliente, también se desactivan (eliminan lógicamente) sus mascotas
    UPDATE mascota
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_cliente = p_id_cliente;

    UPDATE cliente
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_cliente = p_id_cliente;
END $$

CREATE PROCEDURE buscar_cliente_por_id(
    IN p_id_cliente INT
)
BEGIN
    SELECT
        id_cliente,
        dni,
        nombres,
        apellidos,
        telefono,
        email,
        observaciones,
        activo
    FROM cliente
    WHERE id_cliente = p_id_cliente;
END $$

CREATE PROCEDURE listar_clientes_activos()
BEGIN
    SELECT
        id_cliente,
        dni,
        nombres,
        apellidos,
        telefono,
        email,
        observaciones,
        activo
    FROM cliente
    WHERE activo = 1
    ORDER BY nombres, apellidos;
END $$

DROP PROCEDURE IF EXISTS listar_clientes_todos $$
CREATE PROCEDURE listar_clientes_todos()
BEGIN
    SELECT
        id_cliente,
        dni,
        nombres,
        apellidos,
        telefono,
        email,
        observaciones,
        activo
    FROM cliente
    ORDER BY nombres, apellidos;
END $$

CREATE PROCEDURE insertar_mascota(
    IN p_nombre VARCHAR(100),
    IN p_especie VARCHAR(50),
    IN p_raza VARCHAR(50),
    IN p_fecha_nacimiento DATE,
    IN p_peso DECIMAL(6,2),
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
        peso,
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
        p_peso,
        p_esterilizado,
        p_activo,
        p_id_cliente,
        p_created_on,
        p_modified_on,
        p_modified_by
    );

    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_mascota(
    IN p_id_mascota INT,
    IN p_nombre VARCHAR(100),
    IN p_especie VARCHAR(50),
    IN p_raza VARCHAR(50),
    IN p_fecha_nacimiento DATE,
    IN p_peso DECIMAL(6,2),
    IN p_esterilizado TINYINT,
    IN p_activo TINYINT,
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
        peso = p_peso,
        esterilizado = p_esterilizado,
        activo = p_activo,
        id_cliente = p_id_cliente,
        modified_on = p_modified_on,
        modified_by = p_modified_by
    WHERE id_mascota = p_id_mascota;
END $$

CREATE PROCEDURE eliminar_mascota(IN p_id_mascota INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE mascota
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_mascota = p_id_mascota;
END $$

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
        m.peso,
        m.esterilizado,
        m.activo,
        c.id_cliente,
        c.dni,
        c.nombres AS nombres_cliente,
        c.apellidos AS apellidos_cliente,
        c.telefono,
        c.email
    FROM mascota m
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    WHERE m.id_mascota = p_id_mascota;
END $$

CREATE PROCEDURE listar_mascotas_activas()
BEGIN
    SELECT
        id_mascota,
        nombre,
        especie,
        raza,
        fecha_nacimiento,
        peso,
        esterilizado,
        activo,
        id_cliente
    FROM mascota
    WHERE activo = 1
    ORDER BY nombre;
END $$

DROP PROCEDURE IF EXISTS listar_mascotas_todos $$
CREATE PROCEDURE listar_mascotas_todos()
BEGIN
    SELECT
        id_mascota,
        nombre,
        especie,
        raza,
        fecha_nacimiento,
        peso,
        esterilizado,
        activo,
        id_cliente
    FROM mascota
    ORDER BY nombre;
END $$

CREATE PROCEDURE insertar_servicio(
    IN p_nombre VARCHAR(100),
    IN p_descripcion VARCHAR(255),
    IN p_tipo_servicio VARCHAR(20),
    IN p_duracion_minutos INT,
    IN p_precio_referencial DECIMAL(10,2),
    IN p_activo TINYINT(1),
    IN p_created_on DATETIME,
    IN p_modified_on DATETIME,
    IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO servicio(
        nombre,
        descripcion,
        tipo_servicio,
        duracion_minutos,
        precio_referencial,
        activo,
        created_on,
        modified_on,
        modified_by
    )
    VALUES(
        p_nombre,
        p_descripcion,
        p_tipo_servicio,
        p_duracion_minutos,
        p_precio_referencial,
        p_activo,
        p_created_on,
        p_modified_on,
        p_modified_by
    );

    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_servicio(
    IN p_id_servicio INT,
    IN p_nombre VARCHAR(100),
    IN p_descripcion VARCHAR(255),
    IN p_tipo_servicio VARCHAR(20),
    IN p_duracion_minutos INT,
    IN p_precio_referencial DECIMAL(10,2),
    IN p_activo TINYINT(1),
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
    UPDATE servicio
    SET nombre = p_nombre,
        descripcion = p_descripcion,
        tipo_servicio = p_tipo_servicio,
        duracion_minutos = p_duracion_minutos,
        precio_referencial = p_precio_referencial,
        activo = p_activo,
        modified_on = p_modified_on,
        modified_by = p_modified_by
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE eliminar_servicio(IN p_id_servicio INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    -- Borrado lógico: el servicio puede estar referenciado por citas/atenciones (FK),
    -- por lo que no se elimina físicamente.
    UPDATE servicio
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE deshabilitar_servicio(IN p_id_servicio INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE servicio
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE buscar_servicio_por_id(
    IN p_id_servicio INT
)
BEGIN
    SELECT
        id_servicio,
        nombre,
        descripcion,
        tipo_servicio,
        duracion_minutos,
        precio_referencial,
        activo
    FROM servicio
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE listar_servicios()
BEGIN
    SELECT
        id_servicio,
        nombre,
        descripcion,
        tipo_servicio,
        duracion_minutos,
        precio_referencial,
        activo
    FROM servicio
    ORDER BY nombre;
END $$

CREATE PROCEDURE insertar_horario_veterinario(
    IN p_id_veterinario INT, IN p_dia_semana TINYINT, IN p_hora_inicio TIME, IN p_hora_fin TIME,
    IN p_hora_descanso_inicio TIME, IN p_hora_descanso_fin TIME, IN p_modified_by INT, OUT p_id_generado INT
)
BEGIN
    IF EXISTS (
        SELECT 1
        FROM horario_veterinario
        WHERE id_veterinario = p_id_veterinario
          AND dia_semana = p_dia_semana
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario ya tiene un horario registrado para ese dia';
    END IF;

    INSERT INTO horario_veterinario(id_veterinario, dia_semana, hora_inicio, hora_fin, hora_descanso_inicio, hora_descanso_fin, activo, created_on, modified_on, modified_by)
    VALUES(p_id_veterinario, p_dia_semana, p_hora_inicio, p_hora_fin, p_hora_descanso_inicio, p_hora_descanso_fin, 1, DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), p_modified_by);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_horario_veterinario(
    IN p_id_horario INT, IN p_id_veterinario INT, IN p_dia_semana TINYINT,
    IN p_hora_inicio TIME, IN p_hora_fin TIME,
    IN p_hora_descanso_inicio TIME, IN p_hora_descanso_fin TIME,
    IN p_activo TINYINT, IN p_modified_by INT
)
BEGIN
    IF EXISTS (
        SELECT 1
        FROM horario_veterinario
        WHERE id_veterinario = p_id_veterinario
          AND dia_semana = p_dia_semana
          AND id_horario <> p_id_horario
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario ya tiene un horario registrado para ese dia';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM horario_veterinario hv
        INNER JOIN cita c ON c.id_veterinario = hv.id_veterinario
        WHERE hv.id_horario = p_id_horario
          AND hv.activo = 1
          AND (p_activo = 0 OR hv.id_veterinario <> p_id_veterinario OR hv.dia_semana <> p_dia_semana)
          AND c.estado IN ('PENDIENTE', 'CONFIRMADA', 'EN_CONSULTA')
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
          AND WEEKDAY(c.fecha_hora_inicio) + 1 = hv.dia_semana
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede desactivar o mover el horario porque tiene citas pendientes o confirmadas';
    END IF;

    IF p_activo = 1 AND EXISTS (
        SELECT 1
        FROM cita c
        WHERE c.id_veterinario = p_id_veterinario
          AND c.estado IN ('PENDIENTE', 'CONFIRMADA', 'EN_CONSULTA')
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
          AND WEEKDAY(c.fecha_hora_inicio) + 1 = p_dia_semana
          AND (
              TIME(c.fecha_hora_inicio) < p_hora_inicio
              OR TIME(c.fecha_hora_fin) > p_hora_fin
              OR (
                  p_hora_descanso_inicio IS NOT NULL
                  AND p_hora_descanso_fin IS NOT NULL
                  AND TIME(c.fecha_hora_inicio) < p_hora_descanso_fin
                  AND TIME(c.fecha_hora_fin) > p_hora_descanso_inicio
              )
          )
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede modificar el horario porque deja citas fuera del rango disponible';
    END IF;

    UPDATE horario_veterinario
    SET id_veterinario = p_id_veterinario, dia_semana = p_dia_semana,
        hora_inicio = p_hora_inicio, hora_fin = p_hora_fin,
        hora_descanso_inicio = p_hora_descanso_inicio, hora_descanso_fin = p_hora_descanso_fin,
        activo = p_activo, modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), modified_by = p_modified_by
    WHERE id_horario = p_id_horario;
END $$

CREATE PROCEDURE eliminar_horario_veterinario(IN p_id_horario INT, IN p_modified_by INT)
BEGIN
    IF EXISTS (
        SELECT 1
        FROM horario_veterinario hv
        INNER JOIN cita c ON c.id_veterinario = hv.id_veterinario
        WHERE hv.id_horario = p_id_horario
          AND c.estado IN ('PENDIENTE', 'CONFIRMADA', 'EN_CONSULTA')
          AND DATE(c.fecha_hora_inicio) >= DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))
          AND WEEKDAY(c.fecha_hora_inicio) + 1 = hv.dia_semana
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede desactivar el horario porque tiene citas pendientes o confirmadas';
    END IF;

    UPDATE horario_veterinario
    SET activo = 0, modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), modified_by = p_modified_by
    WHERE id_horario = p_id_horario;
END $$

CREATE PROCEDURE buscar_horario_veterinario_por_id(IN p_id_horario INT)
BEGIN
    SELECT id_horario, id_veterinario, dia_semana, hora_inicio, hora_fin, hora_descanso_inicio, hora_descanso_fin, activo
    FROM horario_veterinario
    WHERE id_horario = p_id_horario;
END $$

CREATE PROCEDURE listar_horarios_veterinario(IN p_id_veterinario INT)
BEGIN
    SELECT id_horario, id_veterinario, dia_semana, hora_inicio, hora_fin, hora_descanso_inicio, hora_descanso_fin, activo
    FROM horario_veterinario
    WHERE id_veterinario = p_id_veterinario
    ORDER BY dia_semana, hora_inicio;
END $$

CREATE PROCEDURE listar_horarios_veterinario_todos()
BEGIN
    SELECT id_horario, id_veterinario, dia_semana, hora_inicio, hora_fin, hora_descanso_inicio, hora_descanso_fin, activo
    FROM horario_veterinario
    WHERE activo = 1
    ORDER BY id_veterinario, dia_semana;
END $$

CREATE PROCEDURE existe_solapamiento_cita(
    IN p_id_veterinario INT, IN p_fecha_hora_inicio DATETIME, IN p_fecha_hora_fin DATETIME,
    IN p_id_cita_excluir INT, OUT p_existe TINYINT
)
BEGIN
    SELECT EXISTS(
        SELECT 1
        FROM cita c
        WHERE c.id_veterinario = p_id_veterinario
          AND c.estado IN ('PENDIENTE','CONFIRMADA','EN_CONSULTA')
          AND (p_id_cita_excluir IS NULL OR c.id_cita <> p_id_cita_excluir)
          AND p_fecha_hora_inicio < c.fecha_hora_fin
          AND p_fecha_hora_fin > c.fecha_hora_inicio
    ) INTO p_existe;
END $$

CREATE PROCEDURE validar_disponibilidad_cita(
    IN p_id_veterinario INT, IN p_id_servicio INT, IN p_fecha_hora_inicio DATETIME, IN p_id_mascota INT, IN p_id_cita_excluir INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;
    DECLARE v_dia_semana TINYINT;
    DECLARE v_hora_inicio TIME;
    DECLARE v_hora_fin TIME;
    DECLARE v_horario_count INT DEFAULT 0;
    DECLARE v_solapa TINYINT DEFAULT 0;

    SELECT duracion_minutos INTO v_duracion
    FROM servicio
    WHERE id_servicio = p_id_servicio AND activo = 1;

    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);
    SET v_dia_semana = WEEKDAY(p_fecha_hora_inicio) + 1;
    SET v_hora_inicio = TIME(p_fecha_hora_inicio);
    SET v_hora_fin = TIME(v_fecha_hora_fin);

    SELECT COUNT(*) INTO v_horario_count
    FROM horario_veterinario hv
    WHERE hv.id_veterinario = p_id_veterinario
      AND hv.dia_semana = v_dia_semana
      AND hv.activo = 1
      AND v_hora_inicio >= hv.hora_inicio
      AND v_hora_fin <= hv.hora_fin
      AND (
          hv.hora_descanso_inicio IS NULL
          OR hv.hora_descanso_fin IS NULL
          OR NOT (v_hora_inicio < hv.hora_descanso_fin AND v_hora_fin > hv.hora_descanso_inicio)
      );

    IF v_horario_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario no trabaja en ese horario o cruza su descanso';
    END IF;

    CALL existe_solapamiento_cita(p_id_veterinario, p_fecha_hora_inicio, v_fecha_hora_fin, p_id_cita_excluir, v_solapa);

    IF v_solapa = 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El veterinario ya tiene una cita en ese rango';
    END IF;

    IF p_id_mascota IS NOT NULL AND EXISTS (
        SELECT 1
        FROM cita c
        WHERE c.id_mascota = p_id_mascota
          AND c.estado IN ('PENDIENTE','CONFIRMADA','EN_CONSULTA')
          AND (p_id_cita_excluir IS NULL OR c.id_cita <> p_id_cita_excluir)
          AND p_fecha_hora_inicio < c.fecha_hora_fin
          AND v_fecha_hora_fin > c.fecha_hora_inicio
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La mascota ya tiene una cita en ese rango';
    END IF;

    IF p_id_mascota IS NOT NULL AND EXISTS (
        SELECT 1
        FROM mascota m_nueva
        INNER JOIN mascota m_existente ON m_existente.id_cliente = m_nueva.id_cliente
        INNER JOIN cita c ON c.id_mascota = m_existente.id_mascota
        WHERE m_nueva.id_mascota = p_id_mascota
          AND c.estado IN ('PENDIENTE','CONFIRMADA','EN_CONSULTA')
          AND (p_id_cita_excluir IS NULL OR c.id_cita <> p_id_cita_excluir)
          AND p_fecha_hora_inicio < c.fecha_hora_fin
          AND v_fecha_hora_fin > c.fecha_hora_inicio
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente ya tiene una cita en ese rango';
    END IF;
END $$

CREATE PROCEDURE listar_veterinarios_disponibles(
    IN p_fecha_hora_inicio DATETIME,
    IN p_id_servicio INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;
    DECLARE v_dia_semana TINYINT;
    DECLARE v_hora_inicio TIME;
    DECLARE v_hora_fin TIME;

    SELECT duracion_minutos INTO v_duracion
    FROM servicio
    WHERE id_servicio = p_id_servicio
      AND activo = 1;

    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);
    SET v_dia_semana = WEEKDAY(p_fecha_hora_inicio) + 1;
    SET v_hora_inicio = TIME(p_fecha_hora_inicio);
    SET v_hora_fin = TIME(v_fecha_hora_fin);

    SELECT
        u.id_usuario,
        u.username,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        v.cmpv,
        v.especialidad
    FROM usuario u
    JOIN veterinario v ON v.id_veterinario = u.id_usuario
    JOIN horario_veterinario hv ON hv.id_veterinario = v.id_veterinario
    WHERE u.activo = 1
      AND hv.activo = 1
      AND hv.dia_semana = v_dia_semana
      AND v_hora_inicio >= hv.hora_inicio
      AND v_hora_fin <= hv.hora_fin
      AND (
          hv.hora_descanso_inicio IS NULL
          OR hv.hora_descanso_fin IS NULL
          OR NOT (v_hora_inicio < hv.hora_descanso_fin AND v_hora_fin > hv.hora_descanso_inicio)
      )
      AND NOT EXISTS (
          SELECT 1
          FROM cita c
          WHERE c.id_veterinario = v.id_veterinario
            AND c.estado IN ('PENDIENTE','CONFIRMADA','EN_CONSULTA')
            AND p_fecha_hora_inicio < c.fecha_hora_fin
            AND v_fecha_hora_fin > c.fecha_hora_inicio
      )
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_cita(
    IN p_fecha_hora_inicio DATETIME, IN p_estado VARCHAR(20), IN p_id_mascota INT,
    IN p_id_veterinario INT, IN p_id_servicio INT, IN p_created_on DATETIME,
    IN p_modified_by INT, OUT p_id_generado INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;

    CALL validar_disponibilidad_cita(p_id_veterinario, p_id_servicio, p_fecha_hora_inicio, p_id_mascota, NULL);

    SELECT duracion_minutos INTO v_duracion FROM servicio WHERE id_servicio = p_id_servicio;
    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);

    INSERT INTO cita(fecha_hora_inicio, fecha_hora_fin, estado, id_mascota, id_veterinario, id_servicio, created_on, modified_on, modified_by)
    VALUES(p_fecha_hora_inicio, v_fecha_hora_fin, p_estado, p_id_mascota, p_id_veterinario, p_id_servicio, p_created_on, p_created_on, p_modified_by);

    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_cita(
    IN p_id_cita INT, IN p_fecha_hora_inicio DATETIME, IN p_estado VARCHAR(20), IN p_id_mascota INT,
    IN p_id_veterinario INT, IN p_id_servicio INT, IN p_modified_on DATETIME, IN p_modified_by INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;

    CALL validar_disponibilidad_cita(p_id_veterinario, p_id_servicio, p_fecha_hora_inicio, p_id_mascota, p_id_cita);

    SELECT duracion_minutos INTO v_duracion FROM servicio WHERE id_servicio = p_id_servicio;
    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);

    UPDATE cita
    SET fecha_hora_inicio = p_fecha_hora_inicio, fecha_hora_fin = v_fecha_hora_fin, estado = p_estado,
        id_mascota = p_id_mascota, id_veterinario = p_id_veterinario, id_servicio = p_id_servicio,
        modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE cancelar_cita(
    IN p_id_cita INT,
    IN p_motivo_cancelacion VARCHAR(255),
    IN p_modified_by INT
)
BEGIN
    UPDATE cita
    SET estado = 'CANCELADA',
        motivo_cancelacion = p_motivo_cancelacion,
        fecha_cancelacion = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        id_usuario_cancelacion = p_modified_by,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado IN ('PENDIENTE', 'CONFIRMADA', 'EN_CONSULTA');

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no se puede cancelar en su estado actual';
    END IF;
END $$

CREATE PROCEDURE confirmar_cita(IN p_id_cita INT, IN p_modified_by INT)
BEGIN
    UPDATE cita
    SET estado = 'CONFIRMADA',
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado = 'PENDIENTE';

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no se puede confirmar en su estado actual';
    END IF;
END $$

CREATE PROCEDURE marcar_cita_en_consulta(
    IN p_id_cita INT,
    IN p_modified_by INT
)
BEGIN
    UPDATE cita
    SET estado = 'EN_CONSULTA',
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado = 'CONFIRMADA';

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no puede pasar a consulta desde su estado actual';
    END IF;
END $$

CREATE PROCEDURE marcar_cita_atendida(
    IN p_id_cita INT,
    IN p_modified_by INT
)
BEGIN
    UPDATE cita
    SET estado = 'ATENDIDA',
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado = 'EN_CONSULTA';

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no puede marcarse como atendida desde su estado actual';
    END IF;
END $$

CREATE PROCEDURE marcar_cita_no_asistio(IN p_id_cita INT, IN p_modified_by INT)
BEGIN
    UPDATE cita
    SET estado = 'NO_ASISTIO',
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado IN ('PENDIENTE', 'CONFIRMADA');

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no puede marcarse como no asistio desde su estado actual';
    END IF;
END $$

DROP PROCEDURE IF EXISTS buscar_cita_por_id $$

CREATE PROCEDURE buscar_cita_por_id(IN p_id_cita INT)
BEGIN
    SELECT
        c.id_cita,
        c.fecha_hora_inicio,
        c.fecha_hora_fin,
        c.estado,
        c.motivo_cancelacion,
        c.motivo_reprogramacion,
        c.fecha_cancelacion,
        c.id_usuario_cancelacion,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        c.id_mascota,
        c.id_veterinario,
        c.id_servicio,

        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,

        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio
    FROM cita c
    JOIN mascota m ON m.id_mascota = c.id_mascota
    JOIN servicio s ON s.id_servicio = c.id_servicio
    LEFT JOIN usuario uc ON uc.id_usuario = c.id_usuario_cancelacion
    WHERE c.id_cita = p_id_cita;
END $$

CREATE PROCEDURE listar_citas()
BEGIN
    SELECT
        c.id_cita,
        c.fecha_hora_inicio,
        c.fecha_hora_fin,
        c.estado,
        c.motivo_cancelacion,
        c.motivo_reprogramacion,
        c.fecha_cancelacion,
        c.id_usuario_cancelacion,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        c.id_mascota,
        c.id_veterinario,
        c.id_servicio,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,
        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio
    FROM cita c
    JOIN mascota m ON m.id_mascota = c.id_mascota
    JOIN servicio s ON s.id_servicio = c.id_servicio
    LEFT JOIN usuario uc ON uc.id_usuario = c.id_usuario_cancelacion
    ORDER BY c.fecha_hora_inicio;
END $$

CREATE PROCEDURE listar_citas_por_veterinario_fecha(
    IN p_id_veterinario INT,
    IN p_fecha DATE
)
BEGIN
    SELECT
        c.id_cita,
        c.fecha_hora_inicio,
        c.fecha_hora_fin,
        c.estado,
        c.motivo_cancelacion,
        c.motivo_reprogramacion,
        c.fecha_cancelacion,
        c.id_usuario_cancelacion,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        c.id_mascota,
        c.id_veterinario,
        c.id_servicio,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,
        cl.nombres AS cliente_nombres,
        cl.apellidos AS cliente_apellidos,
        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio
    FROM cita c
    JOIN mascota m ON m.id_mascota = c.id_mascota
    JOIN cliente cl ON cl.id_cliente = m.id_cliente
    JOIN servicio s ON s.id_servicio = c.id_servicio
    LEFT JOIN usuario uc ON uc.id_usuario = c.id_usuario_cancelacion
    WHERE c.id_veterinario = p_id_veterinario
      AND DATE(c.fecha_hora_inicio) = p_fecha
    ORDER BY c.fecha_hora_inicio;
END $$

CREATE PROCEDURE insertar_atencion(
    IN p_fecha_hora DATETIME,
    IN p_nota_clinica VARCHAR(255),
    IN p_diagnostico VARCHAR(255),
    IN p_nota_pre_operatoria VARCHAR(255),
    IN p_nota_post_operatoria VARCHAR(255),
    IN p_recomendacion_control VARCHAR(255),
    IN p_monto_referencial DECIMAL(10,2),
    IN p_descuento_aplicado DECIMAL(10,2),
    IN p_id_cita INT,
    IN p_created_on DATETIME,
    IN p_modified_on DATETIME,
    IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO atencion(
        fecha_hora,
        nota_clinica,
        diagnostico,
        nota_pre_operatoria,
        nota_post_operatoria,
        recomendacion_control,
        monto_referencial,
        descuento_aplicado,
        activo,
        id_cita,
        created_on,
        modified_on,
        modified_by
    )
    VALUES(
        p_fecha_hora,
        p_nota_clinica,
        p_diagnostico,
        p_nota_pre_operatoria,
        p_nota_post_operatoria,
        p_recomendacion_control,
        p_monto_referencial,
        p_descuento_aplicado,
        1,
        p_id_cita,
        p_created_on,
        p_modified_on,
        p_modified_by
    );

    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_atencion(
    IN p_id_atencion INT,
    IN p_fecha_hora DATETIME,
    IN p_nota_clinica VARCHAR(255),
    IN p_diagnostico VARCHAR(255),
    IN p_nota_pre_operatoria VARCHAR(255),
    IN p_nota_post_operatoria VARCHAR(255),
    IN p_recomendacion_control VARCHAR(255),
    IN p_monto_referencial DECIMAL(10,2),
    IN p_descuento_aplicado DECIMAL(10,2),
    IN p_id_cita INT,
    IN p_modified_on DATETIME,
    IN p_modified_by INT
)
BEGIN
    UPDATE atencion
    SET fecha_hora = p_fecha_hora,
        nota_clinica = p_nota_clinica,
        diagnostico = p_diagnostico,
        nota_pre_operatoria = p_nota_pre_operatoria,
        nota_post_operatoria = p_nota_post_operatoria,
        recomendacion_control = p_recomendacion_control,
        monto_referencial = p_monto_referencial,
        descuento_aplicado = p_descuento_aplicado,
        id_cita = p_id_cita,
        modified_on = p_modified_on,
        modified_by = p_modified_by
    WHERE id_atencion = p_id_atencion;
END $$

CREATE PROCEDURE eliminar_atencion(IN p_id_atencion INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE atencion
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_atencion = p_id_atencion;
END $$

CREATE PROCEDURE buscar_atencion_por_id(
    IN p_id_atencion INT
)
BEGIN
    SELECT
        a.id_atencion,
        a.fecha_hora,
        a.nota_clinica,
        a.diagnostico,
        a.nota_pre_operatoria,
        a.nota_post_operatoria,
        a.recomendacion_control,
        a.monto_referencial,
        a.descuento_aplicado,
        a.id_cita
    FROM atencion a
    WHERE a.id_atencion = p_id_atencion
      AND a.activo = 1;
END $$

CREATE PROCEDURE buscar_atencion_por_cita(
    IN p_id_cita INT
)
BEGIN
    SELECT
        a.id_atencion,
        a.fecha_hora,
        a.nota_clinica,
        a.diagnostico,
        a.nota_pre_operatoria,
        a.nota_post_operatoria,
        a.recomendacion_control,
        a.monto_referencial,
        a.descuento_aplicado,
        a.id_cita
    FROM atencion a
    WHERE a.id_cita = p_id_cita
      AND a.activo = 1;
END $$

CREATE PROCEDURE listar_atenciones()
BEGIN
    SELECT
    a.id_atencion,
    a.fecha_hora,
    a.nota_clinica,
    a.diagnostico,
    a.nota_pre_operatoria,
    a.nota_post_operatoria,
    a.recomendacion_control,
    a.monto_referencial,
    a.descuento_aplicado,
    a.id_cita
    FROM atencion a
    WHERE a.activo = 1
    ORDER BY a.fecha_hora DESC;
END $$

CREATE PROCEDURE insertar_recordatorio(
    IN p_fecha_programada DATETIME, IN p_canal VARCHAR(20), IN p_estado_seguimiento VARCHAR(20),
    IN p_mensaje VARCHAR(255), IN p_id_cita INT, IN p_modified_by INT, OUT p_id_generado INT
)
BEGIN
    IF p_estado_seguimiento NOT IN ('PENDIENTE', 'ENVIADO') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Estado de seguimiento invalido. Valores permitidos: PENDIENTE, ENVIADO.';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM cita
        WHERE id_cita = p_id_cita
          AND estado = 'CONFIRMADA'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo se pueden crear recordatorios para citas confirmadas.';
    END IF;

    DELETE FROM recordatorio
    WHERE id_cita = p_id_cita
      AND estado_seguimiento = 'PENDIENTE';

    INSERT INTO recordatorio(fecha_programada, canal, estado_seguimiento, mensaje, id_cita, created_on, modified_on, modified_by)
    VALUES(p_fecha_programada, p_canal, p_estado_seguimiento, p_mensaje, p_id_cita, DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), p_modified_by);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_recordatorio(
    IN p_id_recordatorio INT, IN p_fecha_programada DATETIME, IN p_canal VARCHAR(20), IN p_estado_seguimiento VARCHAR(20),
    IN p_mensaje VARCHAR(255), IN p_id_cita INT, IN p_modified_by INT
)
BEGIN
    IF p_estado_seguimiento NOT IN ('PENDIENTE', 'ENVIADO') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Estado de seguimiento invalido. Valores permitidos: PENDIENTE, ENVIADO.';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM cita
        WHERE id_cita = p_id_cita
          AND estado = 'CONFIRMADA'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo se pueden modificar recordatorios de citas confirmadas.';
    END IF;

    UPDATE recordatorio
    SET fecha_programada = p_fecha_programada, canal = p_canal, estado_seguimiento = p_estado_seguimiento,
        mensaje = p_mensaje, id_cita = p_id_cita, modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR), modified_by = p_modified_by
    WHERE id_recordatorio = p_id_recordatorio;
END $$

CREATE PROCEDURE eliminar_recordatorio(IN p_id_recordatorio INT)
BEGIN
    DELETE FROM recordatorio WHERE id_recordatorio = p_id_recordatorio;
END $$

CREATE PROCEDURE eliminar_recordatorios_por_cita(IN p_id_cita INT)
BEGIN
    DELETE FROM recordatorio WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE buscar_recordatorio_por_id(IN p_id_recordatorio INT)
BEGIN
    SELECT *
    FROM recordatorio
    WHERE id_recordatorio = p_id_recordatorio;
END $$

CREATE PROCEDURE listar_recordatorios()
BEGIN
    SELECT r.*
    FROM recordatorio r
    INNER JOIN cita ci ON ci.id_cita = r.id_cita
    WHERE ci.estado = 'CONFIRMADA'
    ORDER BY r.fecha_programada;
END $$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS verificar_username_existe$$
CREATE PROCEDURE verificar_username_existe(
    IN p_username VARCHAR(50),
    IN p_id_excluir INT, /* Permite modificar el mismo usuario sin que dé falso positivo */
    OUT p_existe TINYINT
)
BEGIN
    SELECT IF(COUNT(*) > 0, 1, 0) INTO p_existe
    FROM usuario
    WHERE username = p_username
      AND (id_usuario != p_id_excluir OR p_id_excluir IS NULL);
END$$

DELIMITER ;
USE vetcitas_db;
DELIMITER $$

/* =========================================================
   DROPS DE PROCEDURES NUEVOS
   ========================================================= */

DROP PROCEDURE IF EXISTS listar_clientes_por_nombre_apellido_dni $$
DROP PROCEDURE IF EXISTS listar_mascotas_por_nombre_o_dueno $$
DROP PROCEDURE IF EXISTS listar_mascotas_por_cliente $$
DROP PROCEDURE IF EXISTS listar_servicios_por_nombre_o_tipo $$
DROP PROCEDURE IF EXISTS listar_servicios_por_estado $$
DROP PROCEDURE IF EXISTS listar_recordatorios_por_mascota_o_cliente $$
DROP PROCEDURE IF EXISTS listar_recordatorios_por_estado_fecha $$
DROP PROCEDURE IF EXISTS listar_atenciones_filtradas $$
DROP PROCEDURE IF EXISTS listar_historial_visitas_por_mascota $$
DROP PROCEDURE IF EXISTS listar_horario_semanal_por_veterinario $$
DROP PROCEDURE IF EXISTS listar_usuarios_filtrados $$
DROP PROCEDURE IF EXISTS listar_citas_filtradas $$


/* =========================================================
   1. CLIENTES
   Buscar por nombre, apellido o dni
   Requiere que exista cliente.dni
   ========================================================= */
CREATE PROCEDURE listar_clientes_por_nombre_apellido_dni(
    IN p_texto VARCHAR(100)
)
BEGIN
    SELECT
        c.id_cliente,
        c.dni,
        c.nombres,
        c.apellidos,
        c.telefono,
        c.email,
        c.observaciones,
        c.activo
    FROM cliente c
    WHERE c.activo = 1
      AND (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR c.nombres LIKE CONCAT('%', p_texto, '%')
            OR c.apellidos LIKE CONCAT('%', p_texto, '%')
            OR c.dni LIKE CONCAT('%', p_texto, '%')
            OR c.email LIKE CONCAT('%', p_texto, '%')
          )
    ORDER BY c.apellidos, c.nombres;
END $$


/* =========================================================
   2. MASCOTAS
   Buscar por nombre de mascota o dueño (nombre/apellido)
   ========================================================= */
CREATE PROCEDURE listar_mascotas_por_nombre_o_dueno(
    IN p_texto VARCHAR(100)
)
BEGIN
    SELECT
        m.id_mascota,
        m.nombre AS nombre_mascota,
        m.especie,
        m.raza,
        m.fecha_nacimiento,
        m.peso,
        m.esterilizado,
        m.activo,
        c.id_cliente,
        c.nombres AS nombres_dueno,
        c.apellidos AS apellidos_dueno
    FROM mascota m
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    WHERE m.activo = 1
      AND c.activo = 1
      AND (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR m.nombre LIKE CONCAT('%', p_texto, '%')
            OR c.nombres LIKE CONCAT('%', p_texto, '%')
            OR c.apellidos LIKE CONCAT('%', p_texto, '%')
            OR CONCAT(c.nombres, ' ', c.apellidos) LIKE CONCAT('%', p_texto, '%')
          )
    ORDER BY m.nombre;
END $$


/* =========================================================
   3. MASCOTAS DE UN SOLO CLIENTE
   Para perfil del cliente / mascotas asociadas
   ========================================================= */
CREATE PROCEDURE listar_mascotas_por_cliente(
    IN p_id_cliente INT
)
BEGIN
    SELECT
        m.id_mascota,
        m.nombre AS nombre_mascota,
        m.especie,
        m.raza,
        m.fecha_nacimiento,
        m.peso,
        TIMESTAMPDIFF(YEAR, m.fecha_nacimiento, DATE(DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR))) AS edad_aprox,
        m.esterilizado,
        m.activo,
        c.id_cliente,
        c.nombres AS nombres_dueno,
        c.apellidos AS apellidos_dueno
    FROM mascota m
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    WHERE m.id_cliente = p_id_cliente
    ORDER BY m.nombre;
END $$


/* =========================================================
   4. SERVICIOS
   Buscar por nombre o tipo
   ========================================================= */
CREATE PROCEDURE listar_servicios_por_nombre_o_tipo(
    IN p_texto VARCHAR(100)
)
BEGIN
    SELECT
        s.id_servicio,
        s.nombre,
        s.descripcion,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial,
        s.activo
    FROM servicio s
    WHERE (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR s.nombre LIKE CONCAT('%', p_texto, '%')
            OR s.descripcion LIKE CONCAT('%', p_texto, '%')
            OR s.tipo_servicio LIKE CONCAT('%', p_texto, '%')
          )
    ORDER BY s.nombre;
END $$


/* =========================================================
   5. SERVICIOS POR ESTADO
   ========================================================= */
CREATE PROCEDURE listar_servicios_por_estado(
    IN p_activo TINYINT
)
BEGIN
    SELECT
        s.id_servicio,
        s.nombre,
        s.descripcion,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial,
        s.activo
    FROM servicio s
    WHERE s.activo = p_activo
    ORDER BY s.nombre;
END $$

/* =========================================================
   6. RECORDATORIOS
   Buscar por mascota o cliente
   ========================================================= */
CREATE PROCEDURE listar_recordatorios_por_mascota_o_cliente(
    IN p_texto VARCHAR(100)
)
BEGIN
    SELECT
        r.id_recordatorio,
        r.fecha_programada,
        r.canal,
        r.estado_seguimiento,
        r.mensaje,
        ci.id_cita,
        m.id_mascota,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,
        c.id_cliente,
        c.nombres AS nombres_cliente,
        c.apellidos AS apellidos_cliente
    FROM recordatorio r
    INNER JOIN cita ci ON ci.id_cita = r.id_cita
    INNER JOIN mascota m ON m.id_mascota = ci.id_mascota
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    WHERE ci.estado = 'CONFIRMADA'
      AND (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR m.nombre LIKE CONCAT('%', p_texto, '%')
            OR c.nombres LIKE CONCAT('%', p_texto, '%')
            OR c.apellidos LIKE CONCAT('%', p_texto, '%')
            OR CONCAT(c.nombres, ' ', c.apellidos) LIKE CONCAT('%', p_texto, '%')
          )
    ORDER BY r.fecha_programada DESC;
END $$


/* =========================================================
   7. RECORDATORIOS POR ESTADO Y/O FECHA
   ========================================================= */
CREATE PROCEDURE listar_recordatorios_por_estado_fecha(
    IN p_estado VARCHAR(20),
    IN p_fecha DATE
)
BEGIN
    SELECT
        r.id_recordatorio,
        r.fecha_programada,
        r.canal,
        r.estado_seguimiento,
        r.mensaje,
        ci.id_cita,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,
        c.nombres AS nombres_cliente,
        c.apellidos AS apellidos_cliente
    FROM recordatorio r
    INNER JOIN cita ci ON ci.id_cita = r.id_cita
    INNER JOIN mascota m ON m.id_mascota = ci.id_mascota
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    WHERE ci.estado = 'CONFIRMADA'
      AND (
            p_estado IS NULL
            OR TRIM(p_estado) = ''
            OR r.estado_seguimiento = p_estado
          )
      AND (
            p_fecha IS NULL
            OR DATE(r.fecha_programada) = p_fecha
          )
    ORDER BY r.fecha_programada DESC;
END $$


/* =========================================================
   8. ATENCIONES FILTRADAS
   Soporta:
   - veterinario
   - estado de cita
   - fecha
   - texto de búsqueda por mascota o dueño
   ========================================================= */
CREATE PROCEDURE listar_atenciones_filtradas(
    IN p_id_veterinario INT,
    IN p_estado_cita VARCHAR(20),
    IN p_fecha DATE,
    IN p_texto VARCHAR(100)
)
BEGIN
    SELECT
        a.id_atencion,
        a.fecha_hora,
        a.nota_clinica,
        a.diagnostico,
        a.nota_pre_operatoria,
        a.nota_post_operatoria,
        a.recomendacion_control,
        a.monto_referencial,
        a.descuento_aplicado,
        a.activo,

        ci.id_cita,
        ci.estado AS estado_cita,
        ci.motivo_cancelacion,
        ci.fecha_cancelacion,
        ci.id_usuario_cancelacion,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        ci.fecha_hora_inicio,
        ci.fecha_hora_fin,
        ci.motivo_reprogramacion,

        m.id_mascota,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,

        c.id_cliente,
        c.nombres AS nombres_cliente,
        c.apellidos AS apellidos_cliente,

        s.id_servicio,
        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio,

        v.id_veterinario,
        u.nombres AS nombres_veterinario,
        u.apellidos AS apellidos_veterinario
    FROM atencion a
    INNER JOIN cita ci ON ci.id_cita = a.id_cita
    INNER JOIN mascota m ON m.id_mascota = ci.id_mascota
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    INNER JOIN servicio s ON s.id_servicio = ci.id_servicio
    INNER JOIN veterinario v ON v.id_veterinario = ci.id_veterinario
    INNER JOIN usuario u ON u.id_usuario = v.id_veterinario
    LEFT JOIN usuario uc ON uc.id_usuario = ci.id_usuario_cancelacion
    WHERE a.activo = 1
      AND (
            p_id_veterinario IS NULL
            OR ci.id_veterinario = p_id_veterinario
          )
      AND (
            p_estado_cita IS NULL
            OR TRIM(p_estado_cita) = ''
            OR ci.estado = p_estado_cita
          )
      AND (
            p_fecha IS NULL
            OR DATE(a.fecha_hora) = p_fecha
          )
      AND (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR m.nombre LIKE CONCAT('%', p_texto, '%')
            OR c.nombres LIKE CONCAT('%', p_texto, '%')
            OR c.apellidos LIKE CONCAT('%', p_texto, '%')
            OR CONCAT(c.nombres, ' ', c.apellidos) LIKE CONCAT('%', p_texto, '%')
          )
    ORDER BY a.fecha_hora DESC;
END $$


/* =========================================================
   9. HISTORIAL / VISITAS DE UNA MASCOTA
   Para pantalla detalle mascota
   ========================================================= */
CREATE PROCEDURE listar_historial_visitas_por_mascota(
    IN p_id_mascota INT
)
BEGIN
    SELECT
        ci.id_cita,
        ci.fecha_hora_inicio,
        ci.fecha_hora_fin,
        ci.estado,
        ci.motivo_cancelacion,
        ci.motivo_reprogramacion,
        ci.fecha_cancelacion,
        ci.id_usuario_cancelacion,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio,

        a.id_atencion,
        a.fecha_hora AS fecha_atencion,
        a.nota_clinica,
        a.diagnostico,
        a.recomendacion_control,
        a.monto_referencial,
        a.descuento_aplicado,
        (a.monto_referencial - a.descuento_aplicado) AS monto_final
    FROM cita ci
    INNER JOIN servicio s ON s.id_servicio = ci.id_servicio
    INNER JOIN atencion a ON a.id_cita = ci.id_cita AND a.activo = 1
    LEFT JOIN usuario uc ON uc.id_usuario = ci.id_usuario_cancelacion
    WHERE ci.id_mascota = p_id_mascota
    ORDER BY ci.fecha_hora_inicio DESC;
END $$


/* =========================================================
   10. HORARIO SEMANAL POR VETERINARIO
   Para vista de recepcionista / resumen semanal
   ========================================================= */
CREATE PROCEDURE listar_horario_semanal_por_veterinario(
    IN p_id_veterinario INT
)
BEGIN
    SELECT
        hv.id_horario,
        hv.id_veterinario,
        hv.dia_semana,
        CASE hv.dia_semana
            WHEN 1 THEN 'Lunes'
            WHEN 2 THEN 'Martes'
            WHEN 3 THEN 'Miércoles'
            WHEN 4 THEN 'Jueves'
            WHEN 5 THEN 'Viernes'
            WHEN 6 THEN 'Sábado'
            WHEN 7 THEN 'Domingo'
        END AS nombre_dia,
        hv.hora_inicio,
        hv.hora_fin,
        hv.hora_descanso_inicio,
        hv.hora_descanso_fin,
        hv.activo,
        u.nombres,
        u.apellidos
    FROM horario_veterinario hv
    INNER JOIN veterinario v ON v.id_veterinario = hv.id_veterinario
    INNER JOIN usuario u ON u.id_usuario = v.id_veterinario
    WHERE hv.id_veterinario = p_id_veterinario
    ORDER BY hv.dia_semana;
END $$


/* =========================================================
   11. USUARIOS FILTRADOS
   Por username, nombre/apellido, rol y estado
   ========================================================= */
CREATE PROCEDURE listar_usuarios_filtrados(
    IN p_texto VARCHAR(100),
    IN p_codigo_rol VARCHAR(30),
    IN p_activo TINYINT
)
BEGIN
    SELECT DISTINCT
        u.id_usuario,
        u.username,
        u.contrasena_hash,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        u.activo,
        rs.id_rol,
        rs.codigo AS codigo_rol,
        rs.descripcion AS descripcion_rol
    FROM usuario u
    LEFT JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario
    LEFT JOIN rol_sistema rs ON rs.id_rol = ur.id_rol
    WHERE (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR LOWER(u.username) LIKE CONCAT('%', LOWER(TRIM(p_texto)), '%')
            OR LOWER(u.nombres) LIKE CONCAT('%', LOWER(TRIM(p_texto)), '%')
            OR LOWER(u.apellidos) LIKE CONCAT('%', LOWER(TRIM(p_texto)), '%')
            OR LOWER(CONCAT(u.nombres, ' ', u.apellidos)) LIKE CONCAT('%', LOWER(TRIM(p_texto)), '%')
            OR LOWER(u.email) LIKE CONCAT('%', LOWER(TRIM(p_texto)), '%')
            OR u.telefono LIKE CONCAT('%', TRIM(p_texto), '%')
          )
      AND (
            p_codigo_rol IS NULL
            OR TRIM(p_codigo_rol) = ''
            OR rs.codigo = UPPER(TRIM(p_codigo_rol))
          )
      AND (
            p_activo IS NULL
            OR u.activo = p_activo
          )
    ORDER BY u.apellidos, u.nombres, u.username;
END $$

CREATE PROCEDURE validar_disponibilidad_slot(
    IN p_id_veterinario INT,
    IN p_fecha_hora_inicio DATETIME,
    IN p_fecha_hora_fin DATETIME,
    IN p_id_cita_excluir INT,
    OUT p_disponible TINYINT
)
BEGIN
    DECLARE v_dia_semana TINYINT;
    DECLARE v_hora_inicio TIME;
    DECLARE v_hora_fin TIME;
    DECLARE v_horario_count INT DEFAULT 0;
    DECLARE v_solapa TINYINT DEFAULT 0;

    SET v_dia_semana = WEEKDAY(p_fecha_hora_inicio) + 1;
    SET v_hora_inicio = TIME(p_fecha_hora_inicio);
    SET v_hora_fin = TIME(p_fecha_hora_fin);

    SELECT COUNT(*) INTO v_horario_count
    FROM horario_veterinario hv
    WHERE hv.id_veterinario = p_id_veterinario
      AND hv.dia_semana = v_dia_semana
      AND hv.activo = 1
      AND v_hora_inicio >= hv.hora_inicio
      AND v_hora_fin <= hv.hora_fin
      AND (
          hv.hora_descanso_inicio IS NULL
          OR hv.hora_descanso_fin IS NULL
          OR NOT (v_hora_inicio < hv.hora_descanso_fin AND v_hora_fin > hv.hora_descanso_inicio)
      );

    CALL existe_solapamiento_cita(
        p_id_veterinario,
        p_fecha_hora_inicio,
        p_fecha_hora_fin,
        p_id_cita_excluir,
        v_solapa
    );

    SET p_disponible = (v_horario_count > 0 AND v_solapa = 0);
END $$

CREATE PROCEDURE listar_ultimas_atenciones_por_veterinario(
    IN p_id_veterinario INT,
    IN p_limite INT
)
BEGIN
    SELECT
        a.id_atencion,
        a.fecha_hora,
        a.nota_clinica,
        a.diagnostico,
        a.nota_pre_operatoria,
        a.nota_post_operatoria,
        a.recomendacion_control,
        a.monto_referencial,
        a.descuento_aplicado,
        a.activo,

        ci.id_cita,
        ci.estado AS estado_cita,
        ci.motivo_cancelacion,
        ci.fecha_cancelacion,
        ci.id_usuario_cancelacion,
        ci.motivo_reprogramacion,
        ci.fecha_hora_inicio,
        ci.fecha_hora_fin,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        m.id_mascota,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,

        c.id_cliente,
        c.nombres AS nombres_cliente,
        c.apellidos AS apellidos_cliente,

        s.id_servicio,
        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial,

        v.id_veterinario,
        u.nombres AS nombres_veterinario,
        u.apellidos AS apellidos_veterinario
    FROM atencion a
    INNER JOIN cita ci ON ci.id_cita = a.id_cita
    INNER JOIN mascota m ON m.id_mascota = ci.id_mascota
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    INNER JOIN servicio s ON s.id_servicio = ci.id_servicio
    INNER JOIN veterinario v ON v.id_veterinario = ci.id_veterinario
    INNER JOIN usuario u ON u.id_usuario = v.id_veterinario
    LEFT JOIN usuario uc ON uc.id_usuario = ci.id_usuario_cancelacion
    WHERE a.activo = 1
      AND ci.id_veterinario = p_id_veterinario
    ORDER BY a.fecha_hora DESC
    LIMIT p_limite;
END $$

CREATE PROCEDURE contar_atenciones_por_veterinario_en_mes(
    IN p_id_veterinario INT,
    IN p_anio INT,
    IN p_mes INT,
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM atencion a
    INNER JOIN cita c ON c.id_cita = a.id_cita
    WHERE a.activo = 1
      AND c.id_veterinario = p_id_veterinario
      AND YEAR(a.fecha_hora) = p_anio
      AND MONTH(a.fecha_hora) = p_mes;
END $$

CREATE PROCEDURE sumar_montos_netos_atenciones_por_mes(
    IN p_anio INT,
    IN p_mes INT,
    OUT p_total DECIMAL(10,2)
)
BEGIN
    SELECT COALESCE(SUM(a.monto_referencial - a.descuento_aplicado), 0)
    INTO p_total
    FROM atencion a
    WHERE a.activo = 1
      AND YEAR(a.fecha_hora) = p_anio
      AND MONTH(a.fecha_hora) = p_mes;
END $$

CREATE PROCEDURE top_servicios_por_veterinario(
    IN p_id_veterinario INT,
    IN p_anio INT,
    IN p_mes INT,
    IN p_limite INT
)
BEGIN
    SELECT
        s.id_servicio,
        s.nombre,
        s.descripcion,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial,
        COUNT(a.id_atencion) AS total_atenciones,
        COALESCE(SUM(a.monto_referencial - a.descuento_aplicado), 0) AS monto_neto_total
    FROM atencion a
    INNER JOIN cita c ON c.id_cita = a.id_cita
    INNER JOIN servicio s ON s.id_servicio = c.id_servicio
    WHERE a.activo = 1
      AND c.id_veterinario = p_id_veterinario
      AND YEAR(a.fecha_hora) = p_anio
      AND MONTH(a.fecha_hora) = p_mes
    GROUP BY
        s.id_servicio,
        s.nombre,
        s.descripcion,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial
    ORDER BY total_atenciones DESC, monto_neto_total DESC
    LIMIT p_limite;
END $$

CREATE PROCEDURE usuario_tiene_rol(
    IN p_id_usuario INT,
    IN p_codigo_rol VARCHAR(30),
    OUT p_tiene_rol INT
)
BEGIN
    SELECT COUNT(*) INTO p_tiene_rol
    FROM usuario_rol ur
    INNER JOIN rol_sistema r ON r.id_rol = ur.id_rol
    INNER JOIN usuario u ON u.id_usuario = ur.id_usuario
    WHERE ur.id_usuario = p_id_usuario
      AND r.codigo = p_codigo_rol
      AND u.activo = 1;
END $$

CREATE PROCEDURE restablecer_contrasena_usuario(
    IN p_id_usuario_objetivo INT,
    IN p_nueva_contrasena_hash CHAR(64),
    IN p_id_admin INT,
    OUT p_actualizado INT
)
BEGIN
    DECLARE v_es_super_admin TINYINT DEFAULT 0;

    -- No se permite restablecer la contrasena del SuperAdmin.
    SELECT COALESCE(es_super_admin, 0) INTO v_es_super_admin
    FROM administrador
    WHERE id_administrador = p_id_usuario_objetivo;

    IF v_es_super_admin = 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede restablecer la contrasena del SuperAdmin';
    END IF;

    UPDATE usuario
    SET contrasena_hash = p_nueva_contrasena_hash,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_id_admin
    WHERE id_usuario = p_id_usuario_objetivo
      AND activo = 1;

    SET p_actualizado = ROW_COUNT();
END $$

CREATE PROCEDURE top_servicios_mas_demandados(
    IN p_desde DATETIME,
    IN p_hasta DATETIME,
    IN p_limite INT
)
BEGIN
    SELECT
        s.id_servicio,
        s.nombre,
        s.descripcion,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial,
        s.activo,
        COUNT(a.id_atencion) AS total_atenciones,
        COALESCE(SUM(a.monto_referencial - a.descuento_aplicado), 0) AS monto_neto_total
    FROM atencion a
    INNER JOIN cita c ON c.id_cita = a.id_cita
    INNER JOIN servicio s ON s.id_servicio = c.id_servicio
    WHERE a.activo = 1
      AND a.fecha_hora >= p_desde
      AND a.fecha_hora <= p_hasta
    GROUP BY
        s.id_servicio,
        s.nombre,
        s.descripcion,
        s.tipo_servicio,
        s.duracion_minutos,
        s.precio_referencial,
        s.activo
    ORDER BY total_atenciones DESC, monto_neto_total DESC
    LIMIT p_limite;
END $$

CREATE PROCEDURE top_veterinarios_por_atenciones(
    IN p_anio INT,
    IN p_mes INT,
    IN p_limite INT
)
BEGIN
    SELECT
        v.id_veterinario,
        u.username,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        v.cmpv,
        v.especialidad,
        COUNT(a.id_atencion) AS total_atenciones,
        COALESCE(SUM(a.monto_referencial - a.descuento_aplicado), 0) AS monto_neto_total
    FROM atencion a
    INNER JOIN cita c ON c.id_cita = a.id_cita
    INNER JOIN veterinario v ON v.id_veterinario = c.id_veterinario
    INNER JOIN usuario u ON u.id_usuario = v.id_veterinario
    WHERE a.activo = 1
      AND YEAR(a.fecha_hora) = p_anio
      AND MONTH(a.fecha_hora) = p_mes
    GROUP BY
        v.id_veterinario,
        u.username,
        u.nombres,
        u.apellidos,
        u.telefono,
        u.email,
        v.cmpv,
        v.especialidad
    ORDER BY total_atenciones DESC, monto_neto_total DESC
    LIMIT p_limite;
END $$



CREATE PROCEDURE reprogramar_cita(
    IN p_id_cita INT,
    IN p_fecha_hora_inicio DATETIME,
    IN p_fecha_hora_fin DATETIME,
    IN p_motivo_reprogramacion VARCHAR(255),
    IN p_modified_by INT
)
BEGIN
    DECLARE v_id_veterinario INT DEFAULT NULL;
    DECLARE v_id_mascota INT DEFAULT NULL;
    DECLARE v_id_servicio INT DEFAULT NULL;
    DECLARE v_duracion INT DEFAULT 0;
    DECLARE v_fecha_hora_fin DATETIME;

    SELECT id_veterinario, id_mascota, id_servicio
    INTO v_id_veterinario, v_id_mascota, v_id_servicio
    FROM cita
    WHERE id_cita = p_id_cita;

    IF v_id_veterinario IS NULL OR v_id_mascota IS NULL OR v_id_servicio IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o esta incompleta';
    END IF;

    CALL validar_disponibilidad_cita(v_id_veterinario, v_id_servicio, p_fecha_hora_inicio, v_id_mascota, p_id_cita);

    SELECT duracion_minutos INTO v_duracion
    FROM servicio
    WHERE id_servicio = v_id_servicio AND activo = 1;

    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);

    UPDATE cita
    SET fecha_hora_inicio = p_fecha_hora_inicio,
        fecha_hora_fin = v_fecha_hora_fin,
        motivo_reprogramacion = p_motivo_reprogramacion,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado IN ('PENDIENTE', 'CONFIRMADA');

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no se puede reprogramar en su estado actual';
    END IF;
END $$

CREATE PROCEDURE cambiar_veterinario_cita(
    IN p_id_cita INT,
    IN p_id_nuevo_veterinario INT,
    IN p_modified_by INT
)
BEGIN
    DECLARE v_id_mascota INT DEFAULT NULL;
    DECLARE v_id_servicio INT DEFAULT NULL;
    DECLARE v_fecha_hora_inicio DATETIME DEFAULT NULL;

    SELECT id_mascota, id_servicio, fecha_hora_inicio
    INTO v_id_mascota, v_id_servicio, v_fecha_hora_inicio
    FROM cita
    WHERE id_cita = p_id_cita;

    IF v_id_mascota IS NULL OR v_id_servicio IS NULL OR v_fecha_hora_inicio IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o esta incompleta';
    END IF;

    CALL validar_disponibilidad_cita(p_id_nuevo_veterinario, v_id_servicio, v_fecha_hora_inicio, v_id_mascota, p_id_cita);

    UPDATE cita
    SET id_veterinario = p_id_nuevo_veterinario,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_cita = p_id_cita
      AND estado IN ('PENDIENTE', 'CONFIRMADA');

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cita no existe o no se puede cambiar de veterinario en su estado actual';
    END IF;
END $$

CREATE PROCEDURE marcar_recordatorio_enviado(
    IN p_id_recordatorio INT,
    IN p_modified_by INT
)
BEGIN
    UPDATE recordatorio
    SET estado_seguimiento = 'ENVIADO',
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_modified_by
    WHERE id_recordatorio = p_id_recordatorio
      AND estado_seguimiento = 'PENDIENTE';
END $$

CREATE PROCEDURE contar_recordatorios_pendientes(
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM recordatorio r
    INNER JOIN cita ci ON ci.id_cita = r.id_cita
    WHERE r.estado_seguimiento = 'PENDIENTE'
      AND ci.estado = 'CONFIRMADA';
END $$

CREATE PROCEDURE contar_citas_por_estado_en_rango(
    IN p_estado VARCHAR(20),
    IN p_desde DATETIME,
    IN p_hasta DATETIME,
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM cita c
    WHERE c.estado = p_estado
      AND c.fecha_hora_inicio >= p_desde
      AND c.fecha_hora_inicio <= p_hasta;
END $$

CREATE PROCEDURE contar_citas_por_veterinario_en_rango(
    IN p_id_veterinario INT,
    IN p_desde DATETIME,
    IN p_hasta DATETIME,
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM cita c
    WHERE c.id_veterinario = p_id_veterinario
      AND c.fecha_hora_inicio >= p_desde
      AND c.fecha_hora_inicio <= p_hasta;
END $$

CREATE PROCEDURE contar_clientes_activos(
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM cliente
    WHERE activo = 1;
END $$

CREATE PROCEDURE contar_clientes_nuevos_en_mes(
    IN p_anio INT,
    IN p_mes INT,
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM cliente
    WHERE YEAR(created_on) = p_anio
      AND MONTH(created_on) = p_mes;
END $$

CREATE PROCEDURE autenticar_usuario(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash CHAR(64)
)
BEGIN
    -- Estado devuelto en la primera columna:
    --   0 = credenciales invalidas (mensaje generico)
    --   1 = autenticacion correcta (incluye los datos del usuario)
    --   2 = cuenta bloqueada temporalmente por demasiados intentos
    DECLARE v_id INT DEFAULT NULL;
    DECLARE v_hash CHAR(64);
    DECLARE v_activo TINYINT DEFAULT 0;
    DECLARE v_bloqueado_hasta DATETIME DEFAULT NULL;
    DECLARE v_intentos INT DEFAULT 0;
    DECLARE v_ahora DATETIME;
    DECLARE v_estado INT DEFAULT 0;
    DECLARE v_restantes INT DEFAULT NULL;

    DECLARE v_max_intentos INT DEFAULT 5;
    DECLARE v_minutos_bloqueo INT DEFAULT 15;

    SET v_ahora = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR);

    SELECT id_usuario, contrasena_hash, activo, bloqueado_hasta, COALESCE(intentos_fallidos, 0)
      INTO v_id, v_hash, v_activo, v_bloqueado_hasta, v_intentos
    FROM usuario
    WHERE username = p_username
    LIMIT 1;

    IF v_id IS NULL THEN
        -- Usuario inexistente: no se revela, estado generico invalido.
        SET v_estado = 0;
    ELSEIF v_bloqueado_hasta IS NOT NULL AND v_bloqueado_hasta > v_ahora THEN
        SET v_estado = 2;
        SET v_restantes = 0;
    ELSEIF v_hash = p_contrasena_hash THEN
        IF v_activo = 1 THEN
            -- Credenciales correctas: se reinicia el contador.
            UPDATE usuario
               SET intentos_fallidos = 0, bloqueado_hasta = NULL
             WHERE id_usuario = v_id;
            SET v_estado = 1;
        ELSE
            -- Clave correcta pero cuenta inactiva: invalido generico, sin contar intento.
            SET v_estado = 0;
        END IF;
    ELSE
        -- Contrasena incorrecta: se cuenta el intento.
        SET v_intentos = v_intentos + 1;
        IF v_intentos >= v_max_intentos THEN
            UPDATE usuario
               SET intentos_fallidos = 0,
                   bloqueado_hasta = DATE_ADD(v_ahora, INTERVAL v_minutos_bloqueo MINUTE)
             WHERE id_usuario = v_id;
            SET v_estado = 2;
            SET v_restantes = 0;
        ELSE
            UPDATE usuario
               SET intentos_fallidos = v_intentos
             WHERE id_usuario = v_id;
            SET v_estado = 0;
            SET v_restantes = v_max_intentos - v_intentos;
        END IF;
    END IF;

    IF v_estado = 1 THEN
        SELECT
            1 AS estado,
            NULL AS intentos_restantes,
            u.id_usuario,
            u.username,
            u.contrasena_hash,
            u.nombres,
            u.apellidos,
            u.telefono,
            u.email,
            u.activo
        FROM usuario u
        WHERE u.id_usuario = v_id;
    ELSE
        SELECT
            v_estado AS estado,
            v_restantes AS intentos_restantes,
            NULL AS id_usuario,
            NULL AS username,
            NULL AS contrasena_hash,
            NULL AS nombres,
            NULL AS apellidos,
            NULL AS telefono,
            NULL AS email,
            NULL AS activo;
    END IF;
END $$

CREATE PROCEDURE cambiar_contrasena_usuario(
    IN p_id_usuario INT,
    IN p_contrasena_actual_hash CHAR(64),
    IN p_nueva_contrasena_hash CHAR(64),
    OUT p_actualizado INT
)
BEGIN
    UPDATE usuario
    SET contrasena_hash = p_nueva_contrasena_hash,
        modified_on = DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
        modified_by = p_id_usuario
    WHERE id_usuario = p_id_usuario
      AND contrasena_hash = p_contrasena_actual_hash
      AND activo = 1;

    SET p_actualizado = ROW_COUNT();
END $$

CREATE PROCEDURE contar_mascotas_activas(
    OUT p_total INT
)
BEGIN
    SELECT COUNT(*) INTO p_total
    FROM mascota
    WHERE activo = 1;
END $$




/* =========================================================
   12. CITAS FILTRADAS
   Para agenda/lista diaria/semana y búsquedas
   ========================================================= */
CREATE PROCEDURE listar_citas_filtradas(
    IN p_id_veterinario INT,
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE,
    IN p_estado VARCHAR(20),
    IN p_texto VARCHAR(100)
)
BEGIN
    SELECT
        ci.id_cita,
        ci.fecha_hora_inicio,
        ci.fecha_hora_fin,
        ci.estado,
        ci.motivo_cancelacion,
        ci.motivo_reprogramacion,
        ci.fecha_cancelacion,
        ci.id_usuario_cancelacion,

        uc.username AS username_usuario_cancelacion,
        uc.nombres AS nombres_usuario_cancelacion,
        uc.apellidos AS apellidos_usuario_cancelacion,
        uc.email AS email_usuario_cancelacion,

        m.id_mascota,
        m.nombre AS nombre_mascota,
        m.especie AS especie_mascota,
        m.raza AS raza_mascota,
        m.peso AS peso_mascota,

        c.id_cliente,
        c.nombres AS nombres_cliente,
        c.apellidos AS apellidos_cliente,

        v.id_veterinario,
        u.nombres AS nombres_veterinario,
        u.apellidos AS apellidos_veterinario,

        s.id_servicio,
        s.nombre AS nombre_servicio,
        s.descripcion AS descripcion_servicio,
        s.tipo_servicio AS tipo_servicio
    FROM cita ci
    INNER JOIN mascota m ON m.id_mascota = ci.id_mascota
    INNER JOIN cliente c ON c.id_cliente = m.id_cliente
    INNER JOIN veterinario v ON v.id_veterinario = ci.id_veterinario
    INNER JOIN usuario u ON u.id_usuario = v.id_veterinario
    INNER JOIN servicio s ON s.id_servicio = ci.id_servicio
    LEFT JOIN usuario uc ON uc.id_usuario = ci.id_usuario_cancelacion
    WHERE (
            p_id_veterinario IS NULL
            OR ci.id_veterinario = p_id_veterinario
          )
      AND (
            p_fecha_inicio IS NULL
            OR DATE(ci.fecha_hora_inicio) >= p_fecha_inicio
          )
      AND (
            p_fecha_fin IS NULL
            OR DATE(ci.fecha_hora_inicio) <= p_fecha_fin
          )
      AND (
            p_estado IS NULL
            OR TRIM(p_estado) = ''
            OR ci.estado = p_estado
          )
      AND (
            p_texto IS NULL
            OR TRIM(p_texto) = ''
            OR m.nombre LIKE CONCAT('%', p_texto, '%')
            OR c.nombres LIKE CONCAT('%', p_texto, '%')
            OR c.apellidos LIKE CONCAT('%', p_texto, '%')
            OR CONCAT(c.nombres, ' ', c.apellidos) LIKE CONCAT('%', p_texto, '%')
          )
    ORDER BY ci.fecha_hora_inicio ASC;
END $$

DELIMITER ;

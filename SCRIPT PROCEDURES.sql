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

DROP PROCEDURE IF EXISTS buscar_configuracion_por_id $$
DROP PROCEDURE IF EXISTS listar_configuraciones $$
DROP PROCEDURE IF EXISTS modificar_configuracion $$

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
DROP PROCEDURE IF EXISTS buscar_recordatorio_por_id $$
DROP PROCEDURE IF EXISTS listar_recordatorios $$

CREATE PROCEDURE insertar_usuario_base(
    IN p_username VARCHAR(50),
    IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_modified_by INT,
    OUT p_id_usuario INT
)
BEGIN
    INSERT INTO usuario(username, contrasena_hash, nombres, apellidos, telefono, activo, created_on, modified_on, modified_by)
    VALUES(p_username, p_contrasena_hash, p_nombres, p_apellidos, p_telefono, 1, NOW(), NOW(), p_modified_by);
    SET p_id_usuario = LAST_INSERT_ID();
END $$

CREATE PROCEDURE asignar_rol_a_usuario(IN p_id_usuario INT, IN p_codigo_rol VARCHAR(30))
BEGIN
    DECLARE v_id_rol INT;
    DECLARE v_tiene_admin INT DEFAULT 0;
    DECLARE v_tiene_vet INT DEFAULT 0;
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

    SELECT COUNT(*) INTO v_tiene_admin
    FROM usuario_rol ur JOIN rol_sistema r ON r.id_rol = ur.id_rol
    WHERE ur.id_usuario = p_id_usuario AND r.codigo = 'ADMINISTRADOR';

    SELECT COUNT(*) INTO v_tiene_vet
    FROM usuario_rol ur JOIN rol_sistema r ON r.id_rol = ur.id_rol
    WHERE ur.id_usuario = p_id_usuario AND r.codigo = 'VETERINARIO';

    SELECT COUNT(*) INTO v_cantidad_roles
    FROM usuario_rol WHERE id_usuario = p_id_usuario;

    IF p_codigo_rol = 'ADMINISTRADOR' AND v_tiene_vet > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se permite ADMINISTRADOR + VETERINARIO';
    END IF;

    IF p_codigo_rol = 'VETERINARIO' AND v_tiene_admin > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se permite VETERINARIO + ADMINISTRADOR';
    END IF;

    IF v_cantidad_roles >= 2 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se permite mas de 2 roles por usuario';
    END IF;

    INSERT INTO usuario_rol(id_usuario, id_rol) VALUES(p_id_usuario, v_id_rol);
END $$

CREATE PROCEDURE revocar_rol_de_usuario(IN p_id_usuario INT, IN p_codigo_rol VARCHAR(30))
BEGIN
    DECLARE v_id_rol INT;
    DECLARE v_total_roles INT;

    SELECT id_rol INTO v_id_rol FROM rol_sistema WHERE codigo = p_codigo_rol;
    SELECT COUNT(*) INTO v_total_roles FROM usuario_rol WHERE id_usuario = p_id_usuario;

    IF v_total_roles <= 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede dejar al usuario sin roles';
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
    UPDATE usuario
    SET activo = 0, modified_on = NOW(), modified_by = p_modified_by
    WHERE id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE insertar_administrador(
    IN p_username VARCHAR(50), IN p_contrasena_hash VARCHAR(255), IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20), IN p_area VARCHAR(100),
    IN p_modified_by INT, OUT p_id_usuario INT
)
BEGIN
    START TRANSACTION;
    CALL insertar_usuario_base(p_username, p_contrasena_hash, p_nombres, p_apellidos, p_telefono, p_modified_by, p_id_usuario);
    INSERT INTO administrador(id_administrador, area) VALUES(p_id_usuario, p_area);
    CALL asignar_rol_a_usuario(p_id_usuario, 'ADMINISTRADOR');
    COMMIT;
END $$

CREATE PROCEDURE modificar_administrador(
    IN p_id_administrador INT, IN p_username VARCHAR(50), IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100), IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20),
    IN p_activo TINYINT, IN p_area VARCHAR(100), IN p_modified_by INT
)
BEGIN
    UPDATE usuario
    SET username = p_username, contrasena_hash = p_contrasena_hash, nombres = p_nombres,
        apellidos = p_apellidos, telefono = p_telefono, activo = p_activo,
        modified_on = NOW(), modified_by = p_modified_by
    WHERE id_usuario = p_id_administrador;

    UPDATE administrador SET area = p_area WHERE id_administrador = p_id_administrador;
END $$

CREATE PROCEDURE eliminar_administrador_logico(IN p_id_administrador INT, IN p_modified_by INT)
BEGIN
    CALL eliminar_usuario_logico(p_id_administrador, p_modified_by);
END $$

CREATE PROCEDURE buscar_administrador_por_id(IN p_id_usuario INT)
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos, u.telefono, u.activo, a.area
    FROM usuario u
    JOIN administrador a ON a.id_administrador = u.id_usuario
    WHERE u.id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE listar_administradores()
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos, u.telefono, u.activo, a.area
    FROM usuario u
    JOIN administrador a ON a.id_administrador = u.id_usuario
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_veterinario(
    IN p_username VARCHAR(50), IN p_contrasena_hash VARCHAR(255), IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20), IN p_cmpv VARCHAR(30),
    IN p_especialidad VARCHAR(100), IN p_modified_by INT, OUT p_id_usuario INT
)
BEGIN
    START TRANSACTION;
    CALL insertar_usuario_base(p_username, p_contrasena_hash, p_nombres, p_apellidos, p_telefono, p_modified_by, p_id_usuario);
    INSERT INTO veterinario(id_veterinario, cmpv, especialidad) VALUES(p_id_usuario, p_cmpv, p_especialidad);
    CALL asignar_rol_a_usuario(p_id_usuario, 'VETERINARIO');
    COMMIT;
END $$

CREATE PROCEDURE modificar_veterinario(
    IN p_id_veterinario INT, IN p_username VARCHAR(50), IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100), IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20),
    IN p_activo TINYINT, IN p_cmpv VARCHAR(30), IN p_especialidad VARCHAR(100), IN p_modified_by INT
)
BEGIN
    UPDATE usuario
    SET username = p_username, contrasena_hash = p_contrasena_hash, nombres = p_nombres,
        apellidos = p_apellidos, telefono = p_telefono, activo = p_activo,
        modified_on = NOW(), modified_by = p_modified_by
    WHERE id_usuario = p_id_veterinario;

    UPDATE veterinario
    SET cmpv = p_cmpv, especialidad = p_especialidad
    WHERE id_veterinario = p_id_veterinario;
END $$

CREATE PROCEDURE buscar_veterinario_por_id(IN p_id_usuario INT)
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos, u.telefono, u.activo, v.cmpv, v.especialidad
    FROM usuario u
    JOIN veterinario v ON v.id_veterinario = u.id_usuario
    WHERE u.id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE listar_veterinarios()
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos, u.telefono, u.activo, v.cmpv, v.especialidad
    FROM usuario u
    JOIN veterinario v ON v.id_veterinario = u.id_usuario
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_recepcionista(
    IN p_username VARCHAR(50), IN p_contrasena_hash VARCHAR(255), IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20), IN p_area VARCHAR(100),
    IN p_modified_by INT, OUT p_id_usuario INT
)
BEGIN
    START TRANSACTION;
    CALL insertar_usuario_base(p_username, p_contrasena_hash, p_nombres, p_apellidos, p_telefono, p_modified_by, p_id_usuario);
    INSERT INTO recepcionista(id_recepcionista, area) VALUES(p_id_usuario, p_area);
    CALL asignar_rol_a_usuario(p_id_usuario, 'RECEPCIONISTA');
    COMMIT;
END $$

CREATE PROCEDURE modificar_recepcionista(
    IN p_id_recepcionista INT, IN p_username VARCHAR(50), IN p_contrasena_hash VARCHAR(255),
    IN p_nombres VARCHAR(100), IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20),
    IN p_activo TINYINT, IN p_area VARCHAR(100), IN p_modified_by INT
)
BEGIN
    UPDATE usuario
    SET username = p_username, contrasena_hash = p_contrasena_hash, nombres = p_nombres,
        apellidos = p_apellidos, telefono = p_telefono, activo = p_activo,
        modified_on = NOW(), modified_by = p_modified_by
    WHERE id_usuario = p_id_recepcionista;

    UPDATE recepcionista SET area = p_area WHERE id_recepcionista = p_id_recepcionista;
END $$

CREATE PROCEDURE buscar_recepcionista_por_id(IN p_id_usuario INT)
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos, u.telefono, u.activo, r.area
    FROM usuario u
    JOIN recepcionista r ON r.id_recepcionista = u.id_usuario
    WHERE u.id_usuario = p_id_usuario;
END $$

CREATE PROCEDURE listar_recepcionistas()
BEGIN
    SELECT u.id_usuario, u.username, u.contrasena_hash, u.nombres, u.apellidos, u.telefono, u.activo, r.area
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
    IN p_nombres VARCHAR(100), IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20), IN p_observaciones VARCHAR(255),
    IN p_activo TINYINT, IN p_created_on DATETIME, IN p_modified_on DATETIME, IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO cliente(nombres, apellidos, telefono, observaciones, activo, created_on, modified_on, modified_by)
    VALUES(p_nombres, p_apellidos, p_telefono, p_observaciones, p_activo, p_created_on, p_modified_on, p_modified_by);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_cliente(
    IN p_id_cliente INT, IN p_nombres VARCHAR(100), IN p_apellidos VARCHAR(100), IN p_telefono VARCHAR(20),
    IN p_observaciones VARCHAR(255), IN p_activo TINYINT, IN p_modified_on DATETIME, IN p_modified_by INT
)
BEGIN
    UPDATE cliente
    SET nombres = p_nombres, apellidos = p_apellidos, telefono = p_telefono, observaciones = p_observaciones,
        activo = p_activo, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_cliente = p_id_cliente;
END $$

CREATE PROCEDURE eliminar_cliente(IN p_id_cliente INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE cliente
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_cliente = p_id_cliente;
END $$

CREATE PROCEDURE buscar_cliente_por_id(IN p_id_cliente INT)
BEGIN
    SELECT id_cliente, nombres, apellidos, telefono, observaciones, activo
    FROM cliente
    WHERE id_cliente = p_id_cliente;
END $$

CREATE PROCEDURE listar_clientes_activos()
BEGIN
    SELECT id_cliente, nombres, apellidos, telefono, observaciones, activo
    FROM cliente
    WHERE activo = 1
    ORDER BY nombres, apellidos;
END $$

CREATE PROCEDURE insertar_mascota(
    IN p_nombre VARCHAR(100), IN p_especie VARCHAR(50), IN p_raza VARCHAR(50), IN p_fecha_nacimiento DATE,
    IN p_esterilizado TINYINT, IN p_activo TINYINT, IN p_id_cliente INT,
    IN p_created_on DATETIME, IN p_modified_on DATETIME, IN p_modified_by INT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO mascota(nombre, especie, raza, fecha_nacimiento, esterilizado, activo, id_cliente, created_on, modified_on, modified_by)
    VALUES(p_nombre, p_especie, p_raza, p_fecha_nacimiento, p_esterilizado, p_activo, p_id_cliente, p_created_on, p_modified_on, p_modified_by);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_mascota(
    IN p_id_mascota INT, IN p_nombre VARCHAR(100), IN p_especie VARCHAR(50), IN p_raza VARCHAR(50),
    IN p_fecha_nacimiento DATE, IN p_esterilizado TINYINT, IN p_id_cliente INT,
    IN p_modified_on DATETIME, IN p_modified_by INT
)
BEGIN
    UPDATE mascota
    SET nombre = p_nombre, especie = p_especie, raza = p_raza, fecha_nacimiento = p_fecha_nacimiento,
        esterilizado = p_esterilizado, id_cliente = p_id_cliente, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_mascota = p_id_mascota;
END $$

CREATE PROCEDURE eliminar_mascota(IN p_id_mascota INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE mascota
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_mascota = p_id_mascota;
END $$

CREATE PROCEDURE buscar_mascota_por_id(IN p_id_mascota INT)
BEGIN
    SELECT id_mascota, nombre, especie, raza, fecha_nacimiento, esterilizado, activo, id_cliente
    FROM mascota
    WHERE id_mascota = p_id_mascota;
END $$

CREATE PROCEDURE listar_mascotas_activas()
BEGIN
    SELECT id_mascota, nombre, especie, raza, fecha_nacimiento, esterilizado, activo, id_cliente
    FROM mascota
    WHERE activo = 1
    ORDER BY nombre;
END $$

CREATE PROCEDURE insertar_servicio(
    IN p_nombre VARCHAR(100), IN p_tipo_servicio VARCHAR(20), IN p_duracion_minutos INT,
    IN p_precio_referencial DECIMAL(10,2), IN p_created_on DATETIME, OUT p_id_generado INT
)
BEGIN
    INSERT INTO servicio(nombre, tipo_servicio, duracion_minutos, precio_referencial, activo, created_on)
    VALUES(p_nombre, p_tipo_servicio, p_duracion_minutos, p_precio_referencial, 1, p_created_on);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_servicio(
    IN p_id_servicio INT, IN p_nombre VARCHAR(100), IN p_tipo_servicio VARCHAR(20), IN p_duracion_minutos INT,
    IN p_precio_referencial DECIMAL(10,2), IN p_modified_on DATETIME, IN p_modified_by INT
)
BEGIN
    UPDATE servicio
    SET nombre = p_nombre, tipo_servicio = p_tipo_servicio, duracion_minutos = p_duracion_minutos,
        precio_referencial = p_precio_referencial, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE eliminar_servicio(IN p_id_servicio INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    DELETE FROM servicio WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE deshabilitar_servicio(IN p_id_servicio INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE servicio
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE buscar_servicio_por_id(IN p_id_servicio INT)
BEGIN
    SELECT id_servicio, nombre, tipo_servicio, duracion_minutos, precio_referencial, activo
    FROM servicio
    WHERE id_servicio = p_id_servicio;
END $$

CREATE PROCEDURE listar_servicios()
BEGIN
    SELECT id_servicio, nombre, tipo_servicio, duracion_minutos, precio_referencial, activo
    FROM servicio
    ORDER BY nombre;
END $$

CREATE PROCEDURE buscar_configuracion_por_id(IN p_id_configuracion INT)
BEGIN
    SELECT id_configuracion, umbral_cliente_frecuente, descuento_maximo_permitido
    FROM configuracion
    WHERE id_configuracion = p_id_configuracion;
END $$

CREATE PROCEDURE listar_configuraciones()
BEGIN
    SELECT id_configuracion, umbral_cliente_frecuente, descuento_maximo_permitido
    FROM configuracion
    ORDER BY id_configuracion;
END $$

CREATE PROCEDURE modificar_configuracion(IN p_id_configuracion INT, IN p_umbral_cliente_frecuente INT, IN p_descuento_maximo_permitido DECIMAL(10,2))
BEGIN
    UPDATE configuracion
    SET umbral_cliente_frecuente = p_umbral_cliente_frecuente,
        descuento_maximo_permitido = p_descuento_maximo_permitido
    WHERE id_configuracion = p_id_configuracion;
END $$

CREATE PROCEDURE insertar_horario_veterinario(
    IN p_id_veterinario INT, IN p_dia_semana TINYINT, IN p_hora_inicio TIME, IN p_hora_fin TIME,
    IN p_hora_descanso_inicio TIME, IN p_hora_descanso_fin TIME, IN p_modified_by INT, OUT p_id_generado INT
)
BEGIN
    INSERT INTO horario_veterinario(id_veterinario, dia_semana, hora_inicio, hora_fin, hora_descanso_inicio, hora_descanso_fin, activo, created_on, modified_on, modified_by)
    VALUES(p_id_veterinario, p_dia_semana, p_hora_inicio, p_hora_fin, p_hora_descanso_inicio, p_hora_descanso_fin, 1, NOW(), NOW(), p_modified_by);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_horario_veterinario(
    IN p_id_horario INT, IN p_hora_inicio TIME, IN p_hora_fin TIME,
    IN p_hora_descanso_inicio TIME, IN p_hora_descanso_fin TIME, IN p_modified_by INT
)
BEGIN
    UPDATE horario_veterinario
    SET hora_inicio = p_hora_inicio, hora_fin = p_hora_fin,
        hora_descanso_inicio = p_hora_descanso_inicio, hora_descanso_fin = p_hora_descanso_fin,
        modified_on = NOW(), modified_by = p_modified_by
    WHERE id_horario = p_id_horario;
END $$

CREATE PROCEDURE eliminar_horario_veterinario(IN p_id_horario INT, IN p_modified_by INT)
BEGIN
    UPDATE horario_veterinario
    SET activo = 0, modified_on = NOW(), modified_by = p_modified_by
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
    WHERE id_veterinario = p_id_veterinario AND activo = 1
    ORDER BY dia_semana;
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
          AND c.estado IN ('PENDIENTE','CONFIRMADA')
          AND (p_id_cita_excluir IS NULL OR c.id_cita <> p_id_cita_excluir)
          AND p_fecha_hora_inicio < c.fecha_hora_fin
          AND p_fecha_hora_fin > c.fecha_hora_inicio
    ) INTO p_existe;
END $$

CREATE PROCEDURE validar_disponibilidad_cita(
    IN p_id_veterinario INT, IN p_id_servicio INT, IN p_fecha_hora_inicio DATETIME, IN p_id_cita_excluir INT
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
END $$

CREATE PROCEDURE listar_veterinarios_disponibles(IN p_fecha_hora_inicio DATETIME, IN p_id_servicio INT)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;
    DECLARE v_dia_semana TINYINT;
    DECLARE v_hora_inicio TIME;
    DECLARE v_hora_fin TIME;

    SELECT duracion_minutos INTO v_duracion
    FROM servicio WHERE id_servicio = p_id_servicio AND activo = 1;

    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);
    SET v_dia_semana = WEEKDAY(p_fecha_hora_inicio) + 1;
    SET v_hora_inicio = TIME(p_fecha_hora_inicio);
    SET v_hora_fin = TIME(v_fecha_hora_fin);

    SELECT u.id_usuario, u.username, u.nombres, u.apellidos, u.telefono, v.cmpv, v.especialidad
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
          SELECT 1 FROM cita c
          WHERE c.id_veterinario = v.id_veterinario
            AND c.estado IN ('PENDIENTE','CONFIRMADA')
            AND p_fecha_hora_inicio < c.fecha_hora_fin
            AND v_fecha_hora_fin > c.fecha_hora_inicio
      )
    ORDER BY u.nombres, u.apellidos;
END $$

CREATE PROCEDURE insertar_cita(
    IN p_fecha_hora_inicio DATETIME, IN p_estado VARCHAR(20), IN p_id_mascota INT,
    IN p_id_veterinario INT, IN p_id_servicio INT, IN p_created_on DATETIME, OUT p_id_generado INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;

    CALL validar_disponibilidad_cita(p_id_veterinario, p_id_servicio, p_fecha_hora_inicio, NULL);

    SELECT duracion_minutos INTO v_duracion FROM servicio WHERE id_servicio = p_id_servicio;
    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);

    INSERT INTO cita(fecha_hora_inicio, fecha_hora_fin, estado, id_mascota, id_veterinario, id_servicio, created_on)
    VALUES(p_fecha_hora_inicio, v_fecha_hora_fin, p_estado, p_id_mascota, p_id_veterinario, p_id_servicio, p_created_on);

    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_cita(
    IN p_id_cita INT, IN p_fecha_hora_inicio DATETIME, IN p_estado VARCHAR(20), IN p_id_mascota INT,
    IN p_id_veterinario INT, IN p_id_servicio INT, IN p_modified_on DATETIME, IN p_modified_by INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE v_fecha_hora_fin DATETIME;

    CALL validar_disponibilidad_cita(p_id_veterinario, p_id_servicio, p_fecha_hora_inicio, p_id_cita);

    SELECT duracion_minutos INTO v_duracion FROM servicio WHERE id_servicio = p_id_servicio;
    SET v_fecha_hora_fin = DATE_ADD(p_fecha_hora_inicio, INTERVAL v_duracion MINUTE);

    UPDATE cita
    SET fecha_hora_inicio = p_fecha_hora_inicio, fecha_hora_fin = v_fecha_hora_fin, estado = p_estado,
        id_mascota = p_id_mascota, id_veterinario = p_id_veterinario, id_servicio = p_id_servicio,
        modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE cancelar_cita(IN p_id_cita INT, IN p_modified_by INT)
BEGIN
    UPDATE cita SET estado = 'CANCELADA', modified_on = NOW(), modified_by = p_modified_by WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE confirmar_cita(IN p_id_cita INT, IN p_modified_by INT)
BEGIN
    UPDATE cita SET estado = 'CONFIRMADA', modified_on = NOW(), modified_by = p_modified_by WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE marcar_cita_atendida(IN p_id_cita INT, IN p_modified_by INT)
BEGIN
    UPDATE cita SET estado = 'ATENDIDA', modified_on = NOW(), modified_by = p_modified_by WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE marcar_cita_no_asistio(IN p_id_cita INT, IN p_modified_by INT)
BEGIN
    UPDATE cita SET estado = 'NO_ASISTIO', modified_on = NOW(), modified_by = p_modified_by WHERE id_cita = p_id_cita;
END $$

CREATE PROCEDURE buscar_cita_por_id(IN p_id_cita INT)
BEGIN
    SELECT c.id_cita, c.fecha_hora_inicio, c.fecha_hora_fin, c.estado,
           c.id_mascota, c.id_veterinario, c.id_servicio,
           m.nombre AS nombre_mascota, s.nombre AS nombre_servicio
    FROM cita c
    JOIN mascota m ON m.id_mascota = c.id_mascota
    JOIN servicio s ON s.id_servicio = c.id_servicio
    WHERE c.id_cita = p_id_cita;
END $$

CREATE PROCEDURE listar_citas()
BEGIN
    SELECT c.id_cita, c.fecha_hora_inicio, c.fecha_hora_fin, c.estado,
           c.id_mascota, c.id_veterinario, c.id_servicio,
           m.nombre AS nombre_mascota, s.nombre AS nombre_servicio
    FROM cita c
    JOIN mascota m ON m.id_mascota = c.id_mascota
    JOIN servicio s ON s.id_servicio = c.id_servicio
    ORDER BY c.fecha_hora_inicio;
END $$

CREATE PROCEDURE listar_citas_por_veterinario_fecha(IN p_id_veterinario INT, IN p_fecha DATE)
BEGIN
    SELECT c.id_cita, c.fecha_hora_inicio, c.fecha_hora_fin, c.estado,
           c.id_mascota, c.id_veterinario, c.id_servicio,
           m.nombre AS nombre_mascota, cl.nombres AS cliente_nombres, cl.apellidos AS cliente_apellidos,
           s.nombre AS nombre_servicio
    FROM cita c
    JOIN mascota m ON m.id_mascota = c.id_mascota
    JOIN cliente cl ON cl.id_cliente = m.id_cliente
    JOIN servicio s ON s.id_servicio = c.id_servicio
    WHERE c.id_veterinario = p_id_veterinario AND DATE(c.fecha_hora_inicio) = p_fecha
    ORDER BY c.fecha_hora_inicio;
END $$

CREATE PROCEDURE insertar_atencion(
    IN p_fecha_hora DATETIME, IN p_nota_clinica TEXT, IN p_nota_pre_operatoria TEXT, IN p_nota_post_operatoria TEXT,
    IN p_recomendacion_control TEXT, IN p_monto_referencial DECIMAL(10,2), IN p_descuento_aplicado DECIMAL(10,2),
    IN p_id_cita INT, IN p_created_on DATETIME, OUT p_id_generado INT
)
BEGIN
    INSERT INTO atencion(fecha_hora, nota_clinica, nota_pre_operatoria, nota_post_operatoria, recomendacion_control,
                         monto_referencial, descuento_aplicado, id_cita, created_on, activo)
    VALUES(p_fecha_hora, p_nota_clinica, p_nota_pre_operatoria, p_nota_post_operatoria, p_recomendacion_control,
           p_monto_referencial, p_descuento_aplicado, p_id_cita, p_created_on, 1);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_atencion(
    IN p_id_atencion INT, IN p_fecha_hora DATETIME, IN p_nota_clinica TEXT, IN p_nota_pre_operatoria TEXT, IN p_nota_post_operatoria TEXT,
    IN p_recomendacion_control TEXT, IN p_monto_referencial DECIMAL(10,2), IN p_descuento_aplicado DECIMAL(10,2),
    IN p_modified_on DATETIME, IN p_modified_by INT
)
BEGIN
    UPDATE atencion
    SET fecha_hora = p_fecha_hora, nota_clinica = p_nota_clinica, nota_pre_operatoria = p_nota_pre_operatoria,
        nota_post_operatoria = p_nota_post_operatoria, recomendacion_control = p_recomendacion_control,
        monto_referencial = p_monto_referencial, descuento_aplicado = p_descuento_aplicado,
        modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_atencion = p_id_atencion;
END $$

CREATE PROCEDURE eliminar_atencion(IN p_id_atencion INT, IN p_modified_on DATETIME, IN p_modified_by INT)
BEGIN
    UPDATE atencion
    SET activo = 0, modified_on = p_modified_on, modified_by = p_modified_by
    WHERE id_atencion = p_id_atencion;
END $$

CREATE PROCEDURE buscar_atencion_por_id(IN p_id_atencion INT)
BEGIN
    SELECT id_atencion, fecha_hora, nota_clinica, nota_pre_operatoria, nota_post_operatoria,
           recomendacion_control, monto_referencial, descuento_aplicado, id_cita
    FROM atencion
    WHERE id_atencion = p_id_atencion AND activo = 1;
END $$

CREATE PROCEDURE buscar_atencion_por_cita(IN p_id_cita INT)
BEGIN
    SELECT id_atencion, fecha_hora, nota_clinica, nota_pre_operatoria, nota_post_operatoria,
           recomendacion_control, monto_referencial, descuento_aplicado, id_cita
    FROM atencion
    WHERE id_cita = p_id_cita AND activo = 1;
END $$

CREATE PROCEDURE listar_atenciones()
BEGIN
    SELECT id_atencion, fecha_hora, nota_clinica, nota_pre_operatoria, nota_post_operatoria,
           recomendacion_control, monto_referencial, descuento_aplicado, id_cita
    FROM atencion
    WHERE activo = 1
    ORDER BY fecha_hora DESC;
END $$

CREATE PROCEDURE insertar_recordatorio(
    IN p_fecha_programada DATETIME, IN p_canal VARCHAR(20), IN p_estado_seguimiento VARCHAR(20),
    IN p_mensaje VARCHAR(255), IN p_id_cita INT, IN p_modified_by INT, OUT p_id_generado INT
)
BEGIN
    INSERT INTO recordatorio(fecha_programada, canal, estado_seguimiento, mensaje, id_cita, created_on, modified_on, modified_by)
    VALUES(p_fecha_programada, p_canal, p_estado_seguimiento, p_mensaje, p_id_cita, NOW(), NOW(), p_modified_by);
    SET p_id_generado = LAST_INSERT_ID();
END $$

CREATE PROCEDURE modificar_recordatorio(
    IN p_id_recordatorio INT, IN p_fecha_programada DATETIME, IN p_canal VARCHAR(20), IN p_estado_seguimiento VARCHAR(20),
    IN p_mensaje VARCHAR(255), IN p_id_cita INT, IN p_modified_by INT
)
BEGIN
    UPDATE recordatorio
    SET fecha_programada = p_fecha_programada, canal = p_canal, estado_seguimiento = p_estado_seguimiento,
        mensaje = p_mensaje, id_cita = p_id_cita, modified_on = NOW(), modified_by = p_modified_by
    WHERE id_recordatorio = p_id_recordatorio;
END $$

CREATE PROCEDURE eliminar_recordatorio(IN p_id_recordatorio INT)
BEGIN
    DELETE FROM recordatorio WHERE id_recordatorio = p_id_recordatorio;
END $$

CREATE PROCEDURE buscar_recordatorio_por_id(IN p_id_recordatorio INT)
BEGIN
    SELECT *
    FROM recordatorio
    WHERE id_recordatorio = p_id_recordatorio;
END $$

CREATE PROCEDURE listar_recordatorios()
BEGIN
    SELECT *
    FROM recordatorio
    ORDER BY fecha_programada;
END $$

DELIMITER ;
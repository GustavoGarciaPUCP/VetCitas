-- =====================================================================
--  VetCitas - DATOS DE PRUEBA (seed coherente)
-- ---------------------------------------------------------------------
--  Ejecutar DESPUES de "DDL VetCitas.sql" (que ya siembra roles,
--  permisos, rol_permiso y el usuario superadmin id=1).
--
--  Este script NO toca: rol_sistema, permiso, rol_permiso ni el
--  superadmin (id_usuario = 1). Los permisos quedan como ya estan
--  predefinidos en el DDL.
--
--  Coherencia garantizada:
--   - Toda FK apunta a un registro existente.
--   - Estados de cita validos: PENDIENTE, CONFIRMADA, EN_CONSULTA,
--     ATENDIDA, NO_ASISTIO, CANCELADA.
--   - Cada cita cae en un dia/hora dentro del horario del veterinario
--     (Lun-Vie 08:00-17:00, refrigerio 13:00-14:00) y NO se solapa con
--     otra cita activa del mismo veterinario.
--   - fecha_hora_fin = fecha_hora_inicio + duracion del servicio.
--   - dia_semana sigue la convencion del backend: WEEKDAY()+1
--     (Lunes=1 ... Domingo=7).
--   - Fecha de referencia "hoy": 2026-06-15 (lunes).
--
--  NOTA sobre contrasena_hash: se usan valores placeholder (texto plano
--  con prefijo) siguiendo el estilo del DDL. Ajustalos al formato real
--  de hash que espere tu backend si vas a probar el login.
-- =====================================================================

USE vetcitas_db;

-- ---------------------------------------------------------------------
-- 0) LIMPIEZA (hace el script re-ejecutable; conserva roles, permisos,
--    rol_permiso y el superadmin id=1)
-- ---------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

DELETE FROM recordatorio;
DELETE FROM atencion;
DELETE FROM cita;
DELETE FROM horario_veterinario;
DELETE FROM mascota;
DELETE FROM cliente;
DELETE FROM servicio;

DELETE FROM usuario_rol  WHERE id_usuario <> 1;
DELETE FROM veterinario;
DELETE FROM recepcionista;
DELETE FROM administrador WHERE id_administrador <> 1;
DELETE FROM usuario       WHERE id_usuario <> 1;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================================
-- 1) USUARIOS (base) + SUBTIPOS + ROLES
--    superadmin = id 1 (ya creado por el DDL)
-- =====================================================================
INSERT INTO usuario
    (id_usuario, username, contrasena_hash, nombres, apellidos, telefono, email, activo, created_on, modified_on, modified_by)
VALUES
    (2, 'rvega',     'hash_rvega',     'Roberto', 'Vega Campos',     '51987100002', 'rvega@vetcitas.com',     1, NOW(), NOW(), 1),
    (3, 'csanchez',  'hash_csanchez',  'Carlos',  'Sanchez Rios',    '51987100003', 'csanchez@vetcitas.com',  1, NOW(), NOW(), 1),
    (4, 'mtorres',   'hash_mtorres',   'Maria',   'Torres Leon',     '51987100004', 'mtorres@vetcitas.com',   1, NOW(), NOW(), 1),
    (5, 'jramirez',  'hash_jramirez',  'Jorge',   'Ramirez Paz',     '51987100005', 'jramirez@vetcitas.com',  1, NOW(), NOW(), 1),
    (6, 'lfernandez','hash_lfernandez','Lucia',   'Fernandez Sosa',  '51987100006', 'lfernandez@vetcitas.com',1, NOW(), NOW(), 1),
    (7, 'agomez',    'hash_agomez',    'Ana',     'Gomez Mora',      '51987100007', 'agomez@vetcitas.com',    1, NOW(), NOW(), 1),
    (8, 'pdiaz',     'hash_pdiaz',     'Pedro',   'Diaz Nunez',      '51987100008', 'pdiaz@vetcitas.com',     0, NOW(), NOW(), 1);

-- Subtipo ADMINISTRADOR (id 2). El superadmin (id 1) ya existe en administrador.
INSERT INTO administrador (id_administrador, area, es_super_admin) VALUES
    (2, 'Administracion', 0);

-- Subtipo VETERINARIO (id 3..6)
INSERT INTO veterinario (id_veterinario, cmpv, especialidad) VALUES
    (3, 'CMP-12345', 'Medicina General'),
    (4, 'CMP-23456', 'Cirugia'),
    (5, 'CMP-34567', 'Dermatologia'),
    (6, 'CMP-45678', 'Medicina General');

-- Subtipo RECEPCIONISTA (id 7..8)
INSERT INTO recepcionista (id_recepcionista, area) VALUES
    (7, 'Recepcion'),
    (8, 'Recepcion');

-- Asignacion de roles (sin asumir ids de rol_sistema; se resuelve por codigo).
-- El superadmin (id 1) ya tiene ADMINISTRADOR asignado por el DDL.
INSERT INTO usuario_rol (id_usuario, id_rol)
SELECT x.id_usuario, r.id_rol
FROM (
    SELECT 2 AS id_usuario, 'ADMINISTRADOR' AS codigo
    UNION ALL SELECT 3, 'VETERINARIO'
    UNION ALL SELECT 4, 'VETERINARIO'
    UNION ALL SELECT 5, 'VETERINARIO'
    UNION ALL SELECT 6, 'VETERINARIO'
    UNION ALL SELECT 7, 'RECEPCIONISTA'
    UNION ALL SELECT 8, 'RECEPCIONISTA'
) x
JOIN rol_sistema r ON r.codigo = x.codigo;

-- =====================================================================
-- 2) CLIENTES (id 7 = inactivo, para probar filtros de "activos")
-- =====================================================================
INSERT INTO cliente
    (id_cliente, dni, nombres, apellidos, telefono, email, observaciones, activo, created_on, modified_on, modified_by)
VALUES
    (1, '40123456', 'Juan',   'Perez Garcia',   '951200001', 'juan.perez@gmail.com',   'Cliente frecuente.',                1, NOW(), NOW(), 7),
    (2, '41234567', 'Maria',  'Lopez Ramos',    '951200002', 'maria.lopez@gmail.com',  NULL,                                1, NOW(), NOW(), 7),
    (3, '42345678', 'Carlos', 'Quispe Mamani',  '951200003', 'carlos.quispe@gmail.com','Prefiere atencion por la manana.',  1, NOW(), NOW(), 7),
    (4, '43456789', 'Ana',    'Flores Diaz',    '951200004', 'ana.flores@gmail.com',   NULL,                                1, NOW(), NOW(), 7),
    (5, '44567890', 'Luis',   'Rodriguez Soto', '951200005', 'luis.rodriguez@gmail.com','Dos mascotas registradas.',        1, NOW(), NOW(), 7),
    (6, '45678901', 'Rosa',   'Huaman Vargas',  '951200006', 'rosa.huaman@gmail.com',  NULL,                                1, NOW(), NOW(), 7),
    (7, '46789012', 'Pedro',  'Castillo Rojas', '951200007', NULL,                     'Inactivo: ya no asiste.',           0, NOW(), NOW(), 7),
    (8, '47890123', 'Carmen', 'Salazar Ruiz',   '951200008', 'carmen.salazar@gmail.com','Tres mascotas.',                   1, NOW(), NOW(), 7);

-- =====================================================================
-- 3) MASCOTAS  (especie ENUM: 'PERRO' | 'GATO')
--    m11 inactiva (pertenece al cliente inactivo).
-- =====================================================================
INSERT INTO mascota
    (id_mascota, nombre, especie, raza, fecha_nacimiento, peso, esterilizado, activo, id_cliente, created_on, modified_on, modified_by)
VALUES
    (1,  'Firulais', 'PERRO', 'Labrador',        '2019-04-10', 28.50, 1, 1, 1, NOW(), NOW(), 7),
    (2,  'Michi',    'GATO',  'Siames',          '2021-08-22',  4.20, 0, 1, 1, NOW(), NOW(), 7),
    (3,  'Rocky',    'PERRO', 'Bulldog Frances', '2020-01-15', 12.00, 1, 1, 2, NOW(), NOW(), 7),
    (4,  'Luna',     'GATO',  'Persa',           '2022-03-05',  3.80, 1, 1, 3, NOW(), NOW(), 7),
    (5,  'Toby',     'PERRO', 'Beagle',          '2018-11-30', 14.30, 0, 1, 3, NOW(), NOW(), 7),
    (6,  'Max',      'PERRO', 'Pastor Aleman',   '2017-06-18', 34.00, 1, 1, 4, NOW(), NOW(), 7),
    (7,  'Nina',     'GATO',  'Angora',          '2023-02-12',  3.10, 0, 1, 5, NOW(), NOW(), 7),
    (8,  'Zeus',     'PERRO', 'Rottweiler',      '2019-09-09', 40.00, 1, 1, 5, NOW(), NOW(), 7),
    (9,  'Coco',     'PERRO', 'Poodle',          '2021-12-01',  7.50, 1, 1, 6, NOW(), NOW(), 7),
    (10, 'Pelusa',   'GATO',  'Criollo',         '2020-07-07',  4.60, 0, 1, 6, NOW(), NOW(), 7),
    (11, 'Bobby',    'PERRO', 'Criollo',         '2016-05-20', 18.00, 1, 0, 7, NOW(), NOW(), 7),
    (12, 'Simba',    'GATO',  'Maine Coon',      '2022-10-10',  6.90, 0, 1, 8, NOW(), NOW(), 7),
    (13, 'Lola',     'PERRO', 'Schnauzer',       '2021-05-14',  8.20, 1, 1, 8, NOW(), NOW(), 7),
    (14, 'Duque',    'PERRO', 'Boxer',           '2019-02-28', 30.00, 0, 1, 8, NOW(), NOW(), 7);

-- =====================================================================
-- 4) SERVICIOS  (tipo_servicio: 'CLINICA' | 'NO_CLINICA')
-- =====================================================================
INSERT INTO servicio
    (id_servicio, nombre, descripcion, tipo_servicio, duracion_minutos, precio_referencial, activo, created_on, modified_on, modified_by)
VALUES
    (1, 'Consulta General',         'Evaluacion clinica general del paciente.',     'CLINICA',    30,  80.00, 1, NOW(), NOW(), 1),
    (2, 'Vacunacion',               'Aplicacion de vacunas segun calendario.',      'CLINICA',    20,  60.00, 1, NOW(), NOW(), 1),
    (3, 'Cirugia de Esterilizacion','Procedimiento quirurgico de esterilizacion.',  'CLINICA',    90, 350.00, 1, NOW(), NOW(), 1),
    (4, 'Control Post-Operatorio',  'Control y curacion luego de cirugia.',         'CLINICA',    20,  50.00, 1, NOW(), NOW(), 1),
    (5, 'Desparasitacion',          'Tratamiento antiparasitario interno/externo.', 'CLINICA',    15,  40.00, 1, NOW(), NOW(), 1),
    (6, 'Bano y Peluqueria',        'Bano completo, secado y corte de pelo.',       'NO_CLINICA', 60,  70.00, 1, NOW(), NOW(), 1),
    (7, 'Corte de Unas',            'Corte y limado de unas.',                      'NO_CLINICA', 15,  25.00, 1, NOW(), NOW(), 1),
    (8, 'Limpieza Dental',          'Profilaxis dental bajo sedacion ligera.',      'CLINICA',    45, 200.00, 1, NOW(), NOW(), 1);

-- =====================================================================
-- 5) HORARIOS DE VETERINARIO
--    Vets 3,4,5,6: Lun-Vie 08:00-17:00, refrigerio 13:00-14:00.
--    Vet 6 ademas atiende sabado 09:00-13:00 (sin refrigerio).
--    dia_semana: 1=Lun ... 6=Sab.
-- =====================================================================
INSERT INTO horario_veterinario
    (id_veterinario, dia_semana, hora_inicio, hora_fin, hora_descanso_inicio, hora_descanso_fin, activo, created_on, modified_on, modified_by)
VALUES
    -- Vet 3 (Carlos Sanchez)
    (3, 1, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (3, 2, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (3, 3, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (3, 4, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (3, 5, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    -- Vet 4 (Maria Torres)
    (4, 1, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (4, 2, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (4, 3, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (4, 4, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (4, 5, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    -- Vet 5 (Jorge Ramirez)
    (5, 1, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (5, 2, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (5, 3, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (5, 4, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (5, 5, '08:00:00', '17:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    -- Vet 6 (Lucia Fernandez)
    (6, 1, '09:00:00', '18:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (6, 2, '09:00:00', '18:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (6, 3, '09:00:00', '18:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (6, 4, '09:00:00', '18:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (6, 5, '09:00:00', '18:00:00', '13:00:00', '14:00:00', 1, NOW(), NOW(), 1),
    (6, 6, '09:00:00', '13:00:00', NULL,        NULL,       1, NOW(), NOW(), 1);

-- =====================================================================
-- 6) CITAS  (id explicito; fecha_hora_fin = inicio + duracion servicio)
--    Estados distribuidos: pasadas (ATENDIDA/NO_ASISTIO/CANCELADA),
--    de hoy (EN_CONSULTA/CONFIRMADA/PENDIENTE) y futuras.
--    Referencia "hoy" = 2026-06-15.
-- =====================================================================
INSERT INTO cita
    (id_cita, fecha_hora_inicio, fecha_hora_fin, estado, motivo_cancelacion, fecha_cancelacion, id_usuario_cancelacion,
     id_mascota, id_veterinario, id_servicio, created_on, modified_on, modified_by)
VALUES
    -- ----- SEMANA PASADA (08-12 jun) -----
    (1,  '2026-06-08 09:00:00', '2026-06-08 09:30:00', 'ATENDIDA',   NULL, NULL, NULL,  1, 3, 1, '2026-06-05 10:00:00', '2026-06-08 09:30:00', 3),
    (2,  '2026-06-08 10:00:00', '2026-06-08 10:20:00', 'ATENDIDA',   NULL, NULL, NULL,  3, 3, 2, '2026-06-05 10:05:00', '2026-06-08 10:20:00', 3),
    (3,  '2026-06-09 09:00:00', '2026-06-09 10:30:00', 'ATENDIDA',   NULL, NULL, NULL,  6, 4, 3, '2026-06-04 11:00:00', '2026-06-09 10:30:00', 4),
    (4,  '2026-06-10 11:00:00', '2026-06-10 11:45:00', 'ATENDIDA',   NULL, NULL, NULL,  4, 5, 8, '2026-06-06 09:30:00', '2026-06-10 11:45:00', 5),
    (5,  '2026-06-11 15:00:00', '2026-06-11 15:30:00', 'ATENDIDA',   NULL, NULL, NULL,  8, 3, 1, '2026-06-08 12:00:00', '2026-06-11 15:30:00', 3),
    (6,  '2026-06-12 09:30:00', '2026-06-12 09:50:00', 'NO_ASISTIO', NULL, NULL, NULL,  9, 4, 2, '2026-06-09 16:00:00', '2026-06-12 10:00:00', 4),
    (7,  '2026-06-12 10:00:00', '2026-06-12 10:30:00', 'CANCELADA',
         'El cliente solicito cancelar por viaje.', '2026-06-11 18:20:00', 7,
         12, 5, 1, '2026-06-08 14:00:00', '2026-06-11 18:20:00', 7),

    -- ----- HOY (15 jun, lunes) -----
    (8,  '2026-06-15 09:00:00', '2026-06-15 09:30:00', 'EN_CONSULTA',NULL, NULL, NULL,  2, 3, 1, '2026-06-12 09:00:00', '2026-06-15 09:00:00', 7),
    (9,  '2026-06-15 10:00:00', '2026-06-15 10:20:00', 'CONFIRMADA', NULL, NULL, NULL,  5, 3, 2, '2026-06-12 09:05:00', '2026-06-13 10:00:00', 7),
    (10, '2026-06-15 11:00:00', '2026-06-15 12:30:00', 'CONFIRMADA', NULL, NULL, NULL, 13, 4, 3, '2026-06-10 15:00:00', '2026-06-13 11:00:00', 7),
    (11, '2026-06-15 14:30:00', '2026-06-15 15:00:00', 'PENDIENTE',  NULL, NULL, NULL,  7, 5, 1, '2026-06-13 17:00:00', '2026-06-13 17:00:00', 7),
    (12, '2026-06-15 15:00:00', '2026-06-15 15:15:00', 'PENDIENTE',  NULL, NULL, NULL, 10, 3, 5, '2026-06-14 11:00:00', '2026-06-14 11:00:00', 7),

    -- ----- PROXIMOS DIAS -----
    (13, '2026-06-16 10:00:00', '2026-06-16 10:20:00', 'CONFIRMADA', NULL, NULL, NULL,  6, 4, 4, '2026-06-09 11:00:00', '2026-06-13 12:00:00', 7),
    (14, '2026-06-16 09:00:00', '2026-06-16 09:30:00', 'CONFIRMADA', NULL, NULL, NULL,  1, 3, 1, '2026-06-13 09:00:00', '2026-06-13 09:00:00', 7),
    (15, '2026-06-17 11:00:00', '2026-06-17 11:45:00', 'PENDIENTE',  NULL, NULL, NULL,  4, 5, 8, '2026-06-14 10:00:00', '2026-06-14 10:00:00', 7),
    (16, '2026-06-18 15:00:00', '2026-06-18 16:00:00', 'CONFIRMADA', NULL, NULL, NULL, 14, 3, 6, '2026-06-13 16:00:00', '2026-06-13 16:00:00', 7),
    (17, '2026-06-19 09:00:00', '2026-06-19 10:30:00', 'PENDIENTE',  NULL, NULL, NULL, 13, 4, 3, '2026-06-14 12:00:00', '2026-06-14 12:00:00', 7),
    (18, '2026-06-16 09:00:00', '2026-06-16 09:30:00', 'CONFIRMADA', NULL, NULL, NULL,  5, 6, 1, '2026-06-13 13:00:00', '2026-06-13 13:00:00', 7);

-- =====================================================================
-- 7) ATENCIONES  (solo para citas ATENDIDA: 1,2,3,4,5)
--    monto_referencial ~ precio del servicio; descuento variable.
-- =====================================================================
INSERT INTO atencion
    (fecha_hora, nota_clinica, diagnostico, nota_pre_operatoria, nota_post_operatoria, recomendacion_control,
     monto_referencial, descuento_aplicado, id_cita, created_on, modified_on, modified_by, activo)
VALUES
    ('2026-06-08 09:30:00',
     'Paciente activo y reactivo. Mucosas rosadas, temperatura 38.5 C. Sin hallazgos relevantes.',
     'Paciente sano',
     NULL, NULL,
     'Mantener calendario de vacunacion al dia.',
      80.00,  0.00, 1, '2026-06-08 09:30:00', '2026-06-08 09:30:00', 3, 1),

    ('2026-06-08 10:20:00',
     'Se aplica vacuna sextuple. Tolera bien el procedimiento.',
     'Inmunizacion de rutina',
     NULL, NULL,
     'Refuerzo en 21 dias.',
      60.00,  0.00, 2, '2026-06-08 10:20:00', '2026-06-08 10:20:00', 3, 1),

    ('2026-06-09 10:30:00',
     'Cirugia de esterilizacion sin complicaciones. Anestesia y recuperacion normales.',
     'Esterilizacion electiva',
     'Ayuno de 8 horas confirmado. Examenes prequirurgicos dentro de rango.',
     'Herida limpia, sin sangrado. Se indica analgesico por 3 dias.',
     'Control post-operatorio en 7 dias para retiro de puntos.',
     350.00, 50.00, 3, '2026-06-09 10:30:00', '2026-06-09 10:30:00', 4, 1),

    ('2026-06-10 11:45:00',
     'Profilaxis dental completa. Se retira sarro. Encias levemente inflamadas.',
     'Enfermedad periodontal leve',
     NULL, NULL,
     'Cepillado dental 2 veces por semana. Control en 6 meses.',
     200.00, 20.00, 4, '2026-06-10 11:45:00', '2026-06-10 11:45:00', 5, 1),

    ('2026-06-11 15:30:00',
     'Consulta por chequeo general. Peso adecuado. Sin signos de dolor.',
     'Paciente sano',
     NULL, NULL,
     'Dieta balanceada y ejercicio diario.',
      80.00, 10.00, 5, '2026-06-11 15:30:00', '2026-06-11 15:30:00', 3, 1);

-- =====================================================================
-- 8) RECORDATORIOS  (canal WHATSAPP; estado_seguimiento PENDIENTE/ENVIADO)
-- =====================================================================
INSERT INTO recordatorio
    (fecha_programada, canal, estado_seguimiento, mensaje, id_cita, created_on, modified_on, modified_by)
VALUES
    ('2026-06-14 18:00:00', 'WHATSAPP', 'ENVIADO',
     'Hola, le recordamos su cita de manana 15/06 a las 10:00 con el Dr. Sanchez.', 9,  NOW(), NOW(), 7),
    ('2026-06-14 18:00:00', 'WHATSAPP', 'ENVIADO',
     'Hola, le recordamos la cirugia de Lola el 15/06 a las 11:00. Ayuno de 8 horas.', 10, NOW(), NOW(), 7),
    ('2026-06-15 18:00:00', 'WHATSAPP', 'PENDIENTE',
     'Recordatorio: control post-operatorio de Max el 16/06 a las 10:00.', 13, NOW(), NOW(), 7),
    ('2026-06-15 18:00:00', 'WHATSAPP', 'PENDIENTE',
     'Recordatorio: consulta de Firulais el 16/06 a las 09:00 con el Dr. Sanchez.', 14, NOW(), NOW(), 7),
    ('2026-06-15 18:00:00', 'WHATSAPP', 'PENDIENTE',
     'Recordatorio: consulta de Toby el 16/06 a las 09:00 con la Dra. Fernandez.', 18, NOW(), NOW(), 7),
    ('2026-06-18 18:00:00', 'WHATSAPP', 'PENDIENTE',
     'Recordatorio: cirugia de Lola el 19/06 a las 09:00. Ayuno de 8 horas.', 17, NOW(), NOW(), 7);

-- =====================================================================
-- FIN DEL SCRIPT DE DATOS DE PRUEBA
-- =====================================================================
SET SQL_SAFE_UPDATES = 1;
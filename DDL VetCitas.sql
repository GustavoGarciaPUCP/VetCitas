DROP schema if exists vetcitas_db;
CREATE SCHEMA vetcitas_db;
USE vetcitas_db;


SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS rol_permiso;
DROP TABLE IF EXISTS usuario_rol;
DROP TABLE IF EXISTS recordatorio;
DROP TABLE IF EXISTS atencion;
DROP TABLE IF EXISTS cita;
DROP TABLE IF EXISTS horario_veterinario;
DROP TABLE IF EXISTS recepcionista;
DROP TABLE IF EXISTS veterinario;
DROP TABLE IF EXISTS administrador;
DROP TABLE IF EXISTS mascota;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS servicio;
DROP TABLE IF EXISTS permiso;
DROP TABLE IF EXISTS rol_sistema;
DROP TABLE IF EXISTS usuario;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    contrasena_hash CHAR(64) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(150) NOT NULL UNIQUE,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    intentos_fallidos INT NOT NULL DEFAULT 0,
    bloqueado_hasta DATETIME NULL,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL
) ENGINE=InnoDB;

CREATE TABLE rol_sistema (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE permiso (
    id_permiso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE usuario_rol (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (id_rol) REFERENCES rol_sistema(id_rol)
) ENGINE=InnoDB;

CREATE TABLE rol_permiso (
    id_rol INT NOT NULL,
    id_permiso INT NOT NULL,
    PRIMARY KEY (id_rol, id_permiso),
    CONSTRAINT fk_rol_permiso_rol FOREIGN KEY (id_rol) REFERENCES rol_sistema(id_rol),
    CONSTRAINT fk_rol_permiso_permiso FOREIGN KEY (id_permiso) REFERENCES permiso(id_permiso)
) ENGINE=InnoDB;

CREATE TABLE administrador (
    id_administrador INT PRIMARY KEY,
    area VARCHAR(100) NOT NULL,
    es_super_admin TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_administrador_usuario FOREIGN KEY (id_administrador) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE veterinario (
    id_veterinario INT PRIMARY KEY,
    cmpv VARCHAR(30) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    CONSTRAINT fk_veterinario_usuario FOREIGN KEY (id_veterinario) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE recepcionista (
    id_recepcionista INT PRIMARY KEY,
    area VARCHAR(100) NOT NULL,
    CONSTRAINT fk_recepcionista_usuario FOREIGN KEY (id_recepcionista) REFERENCES usuario(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(150) NULL,
    observaciones VARCHAR(255),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL
) ENGINE=InnoDB;

CREATE TABLE mascota (
    id_mascota INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie ENUM('PERRO', 'GATO') NOT NULL,
    raza VARCHAR(50),
    fecha_nacimiento DATE,
    peso DECIMAL(6,2) NULL,
    esterilizado TINYINT(1) NOT NULL DEFAULT 0,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_cliente INT NOT NULL,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL,
    CONSTRAINT fk_mascota_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
) ENGINE=InnoDB;

CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    tipo_servicio VARCHAR(20) NOT NULL,
    duracion_minutos INT NOT NULL,
    precio_referencial DECIMAL(10,2) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL
) ENGINE=InnoDB;

CREATE TABLE horario_veterinario (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_veterinario INT NOT NULL,
    dia_semana TINYINT NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    hora_descanso_inicio TIME NULL,
    hora_descanso_fin TIME NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL,
    CONSTRAINT fk_horario_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario)
) ENGINE=InnoDB;

CREATE TABLE cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora_inicio DATETIME NOT NULL,
    fecha_hora_fin DATETIME NOT NULL,
    estado VARCHAR(20) NOT NULL,
    motivo_cancelacion VARCHAR(255) NULL,
    fecha_cancelacion DATETIME NULL,
    id_usuario_cancelacion INT NULL,
    motivo_reprogramacion VARCHAR(255) NULL,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    id_servicio INT NOT NULL,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL,
    CONSTRAINT fk_cita_usuario_cancelacion FOREIGN KEY (id_usuario_cancelacion) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota),
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario),
    CONSTRAINT fk_cita_servicio FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
) ENGINE=InnoDB;

CREATE TABLE atencion (
    id_atencion INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    nota_clinica TEXT,
    diagnostico VARCHAR(255) NULL,
    nota_pre_operatoria TEXT,
    nota_post_operatoria TEXT,
    recomendacion_control TEXT,
    monto_referencial DECIMAL(10,2) NOT NULL DEFAULT 0,
    descuento_aplicado DECIMAL(10,2) NOT NULL DEFAULT 0,
    id_cita INT NOT NULL,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_atencion_cita FOREIGN KEY (id_cita) REFERENCES cita(id_cita)
) ENGINE=InnoDB;

CREATE TABLE recordatorio (
    id_recordatorio INT AUTO_INCREMENT PRIMARY KEY,
    fecha_programada DATETIME NOT NULL,
    canal VARCHAR(20) NOT NULL,
    estado_seguimiento VARCHAR(20) NOT NULL,
    mensaje VARCHAR(255),
    id_cita INT NOT NULL,
    created_on DATETIME,
    modified_on DATETIME,
    modified_by INT NULL,
    CONSTRAINT fk_recordatorio_cita FOREIGN KEY (id_cita) REFERENCES cita(id_cita)
) ENGINE=InnoDB;

INSERT INTO rol_sistema(codigo, descripcion) VALUES
('ADMINISTRADOR', 'Gestiona usuarios, roles y configuracion'),
('VETERINARIO', 'Atiende pacientes y registra atenciones'),
('RECEPCIONISTA', 'Gestiona agenda, clientes y mascotas');

INSERT INTO permiso(nombre, descripcion, activo) VALUES
('USUARIO_GESTIONAR', 'Crear, modificar y desactivar usuarios', 1),
('ROL_ASIGNAR', 'Asignar y revocar roles', 1),
('CLIENTE_GESTIONAR', 'Registrar y actualizar clientes', 1),
('MASCOTA_GESTIONAR', 'Registrar y actualizar mascotas', 1),
('CITA_CREAR', 'Crear citas', 1),
('CITA_REPROGRAMAR', 'Reprogramar citas', 1),
('CITA_CANCELAR', 'Cancelar citas', 1),
('CITA_ASIGNAR_VETERINARIO', 'Asignar veterinario disponible', 1),
('AGENDA_CONSULTAR_GENERAL', 'Ver agenda general', 1),
('AGENDA_CONSULTAR_PROPIA', 'Ver agenda propia', 1),
('ATENCION_REGISTRAR', 'Registrar atenciones', 1),
('HISTORIAL_CONSULTAR', 'Consultar historial del paciente', 1),
('RECORDATORIO_GESTIONAR', 'Programar recordatorios', 1),
('REPORTE_GENERAR', 'Generar reportes', 1);

INSERT INTO rol_permiso(id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM rol_sistema r
JOIN permiso p
WHERE
    (r.codigo = 'ADMINISTRADOR' AND p.nombre IN ('USUARIO_GESTIONAR','ROL_ASIGNAR','REPORTE_GENERAR'))
 OR (r.codigo = 'RECEPCIONISTA' AND p.nombre IN ('CLIENTE_GESTIONAR','MASCOTA_GESTIONAR','CITA_CREAR','CITA_REPROGRAMAR','CITA_CANCELAR','CITA_ASIGNAR_VETERINARIO','AGENDA_CONSULTAR_GENERAL'))
 OR (r.codigo = 'VETERINARIO' AND p.nombre IN ('AGENDA_CONSULTAR_PROPIA','ATENCION_REGISTRAR','HISTORIAL_CONSULTAR','RECORDATORIO_GESTIONAR'));

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
    'superadmin',
    SHA2('SuperAdmin123',256),
    'Super',
    'Admin',
    '999999999',
    'superadmin@vetcitas.com',
    1,
    DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
    DATE_SUB(UTC_TIMESTAMP(), INTERVAL 5 HOUR),
    NULL
);

INSERT INTO administrador(id_administrador, area, es_super_admin)
SELECT id_usuario, 'Sistema', 1 FROM usuario WHERE username = 'superadmin';

INSERT INTO usuario_rol(id_usuario, id_rol)
SELECT u.id_usuario, r.id_rol
FROM usuario u JOIN rol_sistema r ON r.codigo = 'ADMINISTRADOR'
WHERE u.username = 'superadmin';
DROP DATABASE IF EXISTS vetcitas_db;
CREATE DATABASE vetcitas_db;
USE vetcitas_db;

-- =========================================================
-- TABLA USUARIO
-- =========================================================
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    contrasena_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    rol ENUM('ADMINISTRADOR', 'VETERINARIO') NOT NULL,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_username (username)
) ENGINE=InnoDB;

-- =========================================================
-- TABLA PERMISO
-- =========================================================
CREATE TABLE permiso (
    id_permiso INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    PRIMARY KEY (id_permiso),
    UNIQUE KEY uk_permiso_nombre (nombre)
) ENGINE=InnoDB;

-- =========================================================
-- TABLA USUARIO_PERMISO
-- =========================================================
CREATE TABLE usuario_permiso (
    id_usuario INT NOT NULL,
    id_permiso INT NOT NULL,
    PRIMARY KEY (id_usuario, id_permiso),
    CONSTRAINT fk_usuario_permiso_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_permiso_permiso
        FOREIGN KEY (id_permiso)
        REFERENCES permiso(id_permiso)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA VETERINARIO
-- hereda de usuario
-- =========================================================
CREATE TABLE veterinario (
    id_veterinario INT NOT NULL,
    cmpv VARCHAR(30) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_veterinario),
    UNIQUE KEY uk_veterinario_cmpv (cmpv),
    CONSTRAINT fk_veterinario_usuario
        FOREIGN KEY (id_veterinario)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA ADMINISTRADOR
-- hereda de usuario
-- =========================================================
CREATE TABLE administrador (
    id_administrador INT NOT NULL,
    area VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_administrador),
    CONSTRAINT fk_administrador_usuario
        FOREIGN KEY (id_administrador)
        REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA CONFIGURACION
-- =========================================================
CREATE TABLE configuracion (
    id_configuracion INT AUTO_INCREMENT,
    umbral_cliente_frecuente INT NOT NULL,
    descuento_maximo_permitido DECIMAL(5,2) NOT NULL,
    PRIMARY KEY (id_configuracion)
) ENGINE=InnoDB;

-- =========================================================
-- TABLA CLIENTE
-- =========================================================
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    observaciones VARCHAR(255),
    activo TINYINT(1) NOT NULL DEFAULT 1,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_cliente),
    CONSTRAINT fk_cliente_modified_by
        FOREIGN KEY (modified_by)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA MASCOTA
-- =========================================================
CREATE TABLE mascota (
    id_mascota INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    fecha_nacimiento DATE,
    esterilizado TINYINT(1) NOT NULL DEFAULT 0,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_cliente INT NOT NULL,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_mascota),
    CONSTRAINT fk_mascota_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_mascota_modified_by
        FOREIGN KEY (modified_by)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA SERVICIO
-- =========================================================
CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo_servicio ENUM('CLINICA', 'NO_CLINICA') NOT NULL,
    duracion_minutos INT NOT NULL,
    precio_referencial DECIMAL(10,2) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_servicio),
    CONSTRAINT fk_servicio_modified_by
        FOREIGN KEY (modified_by)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA CITA
-- =========================================================
CREATE TABLE cita (
    id_cita INT AUTO_INCREMENT,
    fecha_hora_inicio DATETIME NOT NULL,
    fecha_hora_fin DATETIME NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'ATENDIDA', 'NO_ASISTIO') NOT NULL,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    id_servicio INT NOT NULL,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_cita),
    CONSTRAINT fk_cita_mascota
        FOREIGN KEY (id_mascota)
        REFERENCES mascota(id_mascota)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_cita_veterinario
        FOREIGN KEY (id_veterinario)
        REFERENCES veterinario(id_veterinario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_cita_servicio
        FOREIGN KEY (id_servicio)
        REFERENCES servicio(id_servicio)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_cita_modified_by
        FOREIGN KEY (modified_by)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA ATENCION
-- una cita puede tener 0 o 1 atención
-- =========================================================
CREATE TABLE atencion (
    id_atencion INT AUTO_INCREMENT,
    fecha_hora DATETIME NOT NULL,
    nota_clinica TEXT,
    nota_pre_operatoria TEXT,
    nota_post_operatoria TEXT,
    recomendacion_control TEXT,
    monto_referencial DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    descuento_aplicado DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    id_cita INT NOT NULL,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_atencion),
    UNIQUE KEY uk_atencion_id_cita (id_cita),
    CONSTRAINT fk_atencion_cita
        FOREIGN KEY (id_cita)
        REFERENCES cita(id_cita)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_atencion_modified_by
        FOREIGN KEY (modified_by)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- TABLA RECORDATORIO
-- =========================================================
CREATE TABLE recordatorio (
    id_recordatorio INT AUTO_INCREMENT,
    fecha_programada DATETIME NOT NULL,
    canal ENUM('WHATSAPP') NOT NULL,
    estado_seguimiento ENUM('PENDIENTE', 'ENVIADO', 'CONTACTADO', 'CERRADO') NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    id_cita INT NOT NULL,
    created_on DATETIME NOT NULL,
    modified_on DATETIME,
    modified_by INT,
    PRIMARY KEY (id_recordatorio),
    CONSTRAINT fk_recordatorio_cita
        FOREIGN KEY (id_cita)
        REFERENCES cita(id_cita)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_recordatorio_modified_by
        FOREIGN KEY (modified_by)
        REFERENCES usuario(id_usuario)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================================================
-- REPORTE
-- No se almacena en la base de datos.
-- Se maneja a nivel de lógica/aplicación y se genera bajo demanda.
-- =========================================================

-- =========================================================
-- TABLA REPORTE
-- =========================================================

/*
CREATE TABLE reporte (
    id_reporte INT AUTO_INCREMENT,
    tipo_reporte ENUM('CITAS', 'CLIENTES_FRECUENTES', 'SERVICIOS_MAS_USADOS') NOT NULL,
    periodo_inicio DATETIME NOT NULL,
    periodo_fin DATETIME NOT NULL,
    fecha_generacion DATETIME NOT NULL,
    descripcion VARCHAR(255),
    formato VARCHAR(20) NOT NULL,
    id_administrador INT NOT NULL,
    PRIMARY KEY (id_reporte),
    CONSTRAINT fk_reporte_administrador
        FOREIGN KEY (id_administrador)
        REFERENCES administrador(id_administrador)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;
*/

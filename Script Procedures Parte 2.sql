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

    CREATE TABLE aluno (
        id INT AUTO_INCREMENT PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        matricula VARCHAR(20) UNIQUE NOT NULL,
        data_nascimento DATE NOT NULL
    );


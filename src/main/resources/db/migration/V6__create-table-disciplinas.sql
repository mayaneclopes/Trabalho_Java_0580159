CREATE TABLE Disciplinas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    curso_id INT NOT NULL,
    professor_id INT NOT NULL,
    CONSTRAINT FK_curso_disciplina FOREIGN KEY (curso_id) REFERENCES Cursos(id),
    CONSTRAINT FK_professor_disciplina FOREIGN KEY (professor_id) REFERENCES Professores(id)
);

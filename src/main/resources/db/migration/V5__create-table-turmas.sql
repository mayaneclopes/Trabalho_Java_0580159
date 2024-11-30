CREATE TABLE Turmas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ano INT NOT NULL,
    semestre INT NOT NULL,
    curso_id INT NOT NULL,
    CONSTRAINT FK_curso_turma FOREIGN KEY (curso_id) REFERENCES Cursos(id)
);

CREATE TABLE Notas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricula_id INT NOT NULL,
    disciplina_id INT NOT NULL,
    nota DECIMAL(5, 2) NOT NULL,
    data_lancamento DATE NOT NULL,
    CONSTRAINT FK_matricula_nota FOREIGN KEY (matricula_id) REFERENCES Matriculas(id),
    CONSTRAINT FK_disciplina_nota FOREIGN KEY (disciplina_id) REFERENCES Disciplinas(id)
);

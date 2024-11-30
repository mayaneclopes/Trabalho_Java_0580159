ALTER TABLE aluno
ADD CONSTRAINT FK_matricula
FOREIGN KEY (matricula_id) REFERENCES matriculas(id);


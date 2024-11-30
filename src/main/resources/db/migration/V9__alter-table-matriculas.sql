ALTER TABLE matriculas
ADD aluno_id INT,
ADD turma_id INT,
ADD CONSTRAINT FK_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id),
ADD CONSTRAINT FK_turma FOREIGN KEY (turma_id) REFERENCES turmas(id);

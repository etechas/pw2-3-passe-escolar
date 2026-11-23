package br.com.etec.passe_escolar.repository;
import br.com.etec.passe_escolar.model.PasseEscolarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasseEscolarRepository extends JpaRepository<PasseEscolarModel, Long> {
}
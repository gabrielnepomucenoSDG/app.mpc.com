package com.ongreport.narraterpro.service.imports;

import com.ongreport.narraterpro.importer.AgendamentoImporter;
import com.ongreport.narraterpro.importer.ImportResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Orquestra o processo de importação.
 * Utiliza o padrão Strategy/Factory para selecionar o importador correto.
 */
@Service
public class AgendamentoImportService {

    // Graças à injeção de dependência do Spring, esta lista conterá
    // todas as classes que implementam AgendamentoImporter (no nosso caso, CsvAgendamentoImporter).
    private final List<AgendamentoImporter> importers;

    public AgendamentoImportService(List<AgendamentoImporter> importers) {
        this.importers = importers;
    }

    public ImportResult importarArquivo(MultipartFile file, Principal principal) throws IOException {
        String fileName = file.getOriginalFilename();

        // Encontra o importador correto que suporta o tipo de arquivo
        AgendamentoImporter importer = importers.stream()
                .filter(imp -> imp.supports(fileName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de arquivo não suportado: " + fileName));

        // Delega a tarefa de importação para o adaptador/importador correto
        return importer.importar(file, principal);
    }
}

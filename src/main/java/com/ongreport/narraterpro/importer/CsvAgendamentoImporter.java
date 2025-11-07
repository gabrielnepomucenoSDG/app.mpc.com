package com.ongreport.narraterpro.importer;

import com.ongreport.narraterpro.adapter.AgendamentoImportAdapter;
import com.ongreport.narraterpro.dto.LegacyAgendamentoCsvDto;
import com.ongreport.narraterpro.dto.StandardAgendamentoCsvDto;
import com.ongreport.narraterpro.entity.Usuario;
import com.ongreport.narraterpro.repository.AgendamentoRepository;
import com.ongreport.narraterpro.repository.UsuarioRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Principal;
import java.util.List;


@Component
public class CsvAgendamentoImporter implements AgendamentoImporter {

    private final AgendamentoImportAdapter adapter;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public CsvAgendamentoImporter(AgendamentoImportAdapter adapter, AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository) {
        this.adapter = adapter;
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean supports(String fileName) {
        return fileName != null && fileName.toLowerCase().endsWith(".csv");
    }

    @Override
    public ImportResult importar(MultipartFile file, Principal principal) throws IOException {
        Usuario usuarioLogado = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário logado não encontrado."));

        ImportResult result = new ImportResult();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.mark(1024);
            String header = reader.readLine();
            reader.reset();

            if (header == null) throw new IllegalArgumentException("Arquivo CSV vazio ou sem cabeçalho.");

            if (header.contains("idLegado")) {
                processLegacyCsv(reader, usuarioLogado, result);
            } else if (header.contains("data_fim") && header.contains("custos")) {
                processStandardCsv(reader, usuarioLogado, result);
            } else {
                throw new IllegalArgumentException("Formato de CSV não reconhecido.");
            }
        }
        return result;
    }

    private void processLegacyCsv(Reader reader, Usuario usuario, ImportResult result) {
        List<LegacyAgendamentoCsvDto> dtos = new CsvToBeanBuilder<LegacyAgendamentoCsvDto>(reader)
                .withType(LegacyAgendamentoCsvDto.class).build().parse();

        for (LegacyAgendamentoCsvDto dto : dtos) {
            try {
                agendamentoRepository.save(adapter.fromLegacyDto(dto, usuario));
                result.addSuccess();
            } catch (Exception e) {
                result.addError("Linha Legada (ID " + dto.getIdLegado() + "): " + e.getMessage());
            }
        }
    }

    private void processStandardCsv(Reader reader, Usuario usuario, ImportResult result) {
        List<StandardAgendamentoCsvDto> dtos = new CsvToBeanBuilder<StandardAgendamentoCsvDto>(reader)
                .withType(StandardAgendamentoCsvDto.class).build().parse();

        for (StandardAgendamentoCsvDto dto : dtos) {
            try {
                agendamentoRepository.save(adapter.fromStandardDto(dto, usuario));
                result.addSuccess();
            } catch (Exception e) {
                result.addError("Linha Padrão (Data " + dto.getData_inicio() + "): " + e.getMessage());
            }
        }
    }
}

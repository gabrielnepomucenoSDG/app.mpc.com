package com.ongreport.narraterpro.importer;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;

/**
 * Interface alvo para o padrão Adapter.
 * Define o contrato para todas as classes que importam agendamentos,
 * independentemente do formato do arquivo (CSV, JSON, etc.).
 */
public interface AgendamentoImporter {

    /**
     * Verifica se este importador é capaz de processar o arquivo fornecido.
     * @param fileName O nome do arquivo a ser verificado.
     * @return true se o importador suporta o tipo de arquivo, false caso contrário.
     */
    boolean supports(String fileName);

    /**
     * Realiza a importação dos dados do arquivo.
     * @param file O arquivo enviado pelo usuário.
     * @param principal O usuário autenticado que está realizando a importação.
     * @return Um objeto ImportResult contendo o resumo da operação.
     * @throws IOException Se ocorrer um erro de leitura do arquivo.
     */
    ImportResult importar(MultipartFile file, Principal principal) throws IOException;
}

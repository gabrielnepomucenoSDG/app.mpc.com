package com.ongreport.narraterpro.controller;

import com.ongreport.narraterpro.importer.ImportResult;
import com.ongreport.narraterpro.service.AgendamentoFacade;
import com.ongreport.narraterpro.service.imports.AgendamentoImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/importar")
public class ImportacaoController {

    private final AgendamentoImportService agendamentoImportService;
    private final AgendamentoFacade agendamentoFacade;

    @Autowired
    public ImportacaoController(AgendamentoImportService agendamentoImportService,  AgendamentoFacade agendamentoFacade) {
        this.agendamentoImportService = agendamentoImportService;
        this.agendamentoFacade = agendamentoFacade;
    }

    @GetMapping("/agendamentos")
    public String exibirFormularioImportacao() {
        return "importar-form";
    }

    @PostMapping("/agendamentos/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // A chamada agora é para o novo método orquestrador
            ImportResult result = agendamentoFacade.importarAgendamentos(file, principal);

            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Importação finalizada! " + result.getSuccessCount() + " registros processados com sucesso.");

            if (result.hasErrors()) {
                String errorDetails = String.join("<br>", result.getErrors());
                redirectAttributes.addFlashAttribute("mensagemErro",
                        "Ocorreram " + result.getErrors().size() + " erros:<br>" + errorDetails);
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro crítico ao processar arquivo: " + ex.getMessage());
        }

        return "redirect:/importar/agendamentos";
    }
}

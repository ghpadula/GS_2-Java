package br.com.fiap.gs.config;

import br.com.fiap.gs.model.Usuario;
import br.com.fiap.gs.model.Trilha;
import br.com.fiap.gs.model.Competencia;
import br.com.fiap.gs.model.Matricula;
import br.com.fiap.gs.repository.UsuarioRepository;
import br.com.fiap.gs.repository.TrilhaRepository;
import br.com.fiap.gs.repository.CompetenciaRepository;
import br.com.fiap.gs.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Override
    public void run(String... args) throws Exception {
        matriculaRepository.deleteAll();
        usuarioRepository.deleteAll();
        trilhaRepository.deleteAll();
        competenciaRepository.deleteAll();


        criarCompetencias();
        criarUsuarios();
        criarTrilhas();
        criarMatriculas();

        System.out.println("Dados iniciais carregados com sucesso!");
    }

    private void criarCompetencias() {
        Competencia ia = new Competencia("Inteligência Artificial", "Tecnologia",
                "Desenvolvimento e aplicação de sistemas de IA e machine learning");
        Competencia dados = new Competencia("Análise de Dados", "Tecnologia",
                "Coleta, processamento e interpretação de dados para tomada de decisão");
        Competencia cloud = new Competencia("Cloud Computing", "Tecnologia",
                "Gestão e implantação de soluções em nuvem");
        Competencia empatia = new Competencia("Empatia", "Humana",
                "Capacidade de compreender e compartilhar sentimentos dos outros");
        Competencia colaboracao = new Competencia("Colaboração", "Humana",
                "Trabalho em equipe e cooperação para objetivos comuns");
        Competencia lideranca = new Competencia("Liderança", "Gestão",
                "Gestão de equipes e tomada de decisão estratégica");

        competenciaRepository.saveAll(Arrays.asList(ia, dados, cloud, empatia, colaboracao, lideranca));
    }

    private void criarUsuarios() {
        Usuario usuario1 = new Usuario("João Silva", "joao.silva@email.com", "Desenvolvimento", "PLENO");
        usuario1.setDataCadastro(LocalDate.of(2024, 1, 15));

        Usuario usuario2 = new Usuario("Maria Santos", "maria.santos@email.com", "Análise de Dados", "SENIOR");
        usuario2.setDataCadastro(LocalDate.of(2023, 8, 22));

        Usuario usuario3 = new Usuario("Pedro Oliveira", "pedro.oliveira@email.com", "Marketing", "JUNIOR");
        usuario3.setDataCadastro(LocalDate.of(2024, 3, 10));

        Usuario usuario4 = new Usuario("Ana Costa", "ana.costa@email.com", "RH", "EM_TRANSICAO");
        usuario4.setDataCadastro(LocalDate.of(2024, 2, 5));

        usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2, usuario3, usuario4));
    }

    private void criarTrilhas() {
        Competencia ia = competenciaRepository.findByNome("Inteligência Artificial").get(0);
        Competencia dados = competenciaRepository.findByNome("Análise de Dados").get(0);
        Competencia cloud = competenciaRepository.findByNome("Cloud Computing").get(0);
        Competencia empatia = competenciaRepository.findByNome("Empatia").get(0);
        Competencia colaboracao = competenciaRepository.findByNome("Colaboração").get(0);

        Trilha trilhaIA = new Trilha(
                "Fundamentos de IA e Machine Learning",
                "Domine os conceitos básicos de Inteligência Artificial e aprendizado de máquina para aplicações reais",
                "INICIANTE",
                40,
                "IA"
        );
        trilhaIA.setCompetencias(Arrays.asList(ia, dados));

        Trilha trilhaDados = new Trilha(
                "Data Science para Negócios",
                "Aprenda a transformar dados em insights valiosos para tomada de decisão empresarial",
                "INTERMEDIARIO",
                60,
                "Dados"
        );
        trilhaDados.setCompetencias(Arrays.asList(dados, cloud));

        Trilha trilhaSoftSkills = new Trilha(
                "Competências Humanas para Liderança",
                "Desenvolva habilidades interpessoais essenciais para liderança no século XXI",
                "AVANCADO",
                30,
                "Soft Skills"
        );
        trilhaSoftSkills.setCompetencias(Arrays.asList(empatia, colaboracao));

        Trilha trilhaCloud = new Trilha(
                "AWS Cloud Practitioner",
                "Preparação para certificação AWS com foco em soluções em nuvem",
                "INTERMEDIARIO",
                50,
                "Cloud"
        );
        trilhaCloud.setCompetencias(Arrays.asList(cloud));

        trilhaRepository.saveAll(Arrays.asList(trilhaIA, trilhaDados, trilhaSoftSkills, trilhaCloud));
    }

    private void criarMatriculas() {
        Usuario joao = usuarioRepository.findByEmail("joao.silva@email.com").orElseThrow();
        Usuario maria = usuarioRepository.findByEmail("maria.santos@email.com").orElseThrow();
        Usuario pedro = usuarioRepository.findByEmail("pedro.oliveira@email.com").orElseThrow();

        // Use findFirstByNome que retorna Optional<Trilha>
        Trilha trilhaIA = trilhaRepository.findFirstByNome("Fundamentos de IA e Machine Learning").orElseThrow();
        Trilha trilhaDados = trilhaRepository.findFirstByNome("Data Science para Negócios").orElseThrow();
        Trilha trilhaSoftSkills = trilhaRepository.findFirstByNome("Competências Humanas para Liderança").orElseThrow();

        Matricula matricula1 = new Matricula(joao, trilhaIA, "ATIVA");
        Matricula matricula2 = new Matricula(maria, trilhaDados, "ATIVA");
        Matricula matricula3 = new Matricula(pedro, trilhaSoftSkills, "CONCLUIDA");
        Matricula matricula4 = new Matricula(joao, trilhaDados, "ATIVA");

        matriculaRepository.saveAll(Arrays.asList(matricula1, matricula2, matricula3, matricula4));
    }
}
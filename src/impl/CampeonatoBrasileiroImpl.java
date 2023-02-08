package impl;

import dominio.DataDoJogo;
import dominio.Jogo;
import dominio.PosicaoTabela;
import dominio.Resultado;
import dominio.Time;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CampeonatoBrasileiroImpl {

    private Map<Integer, List<Jogo>> brasileirao;
    private List<Jogo> jogos;
    private Predicate<Jogo> filtro;

    public CampeonatoBrasileiroImpl(Path arquivo, Predicate<Jogo> filtro) throws IOException {
        this.jogos = lerArquivo(arquivo);
        this.filtro = filtro;
        this.brasileirao = jogos.stream()
                .filter(filtro) //filtrar por ano
                .collect(Collectors.groupingBy(
                        Jogo::rodada,
                        Collectors.mapping(Function.identity(), Collectors.toList())));

    }

    public List<Jogo> lerArquivo(Path file) throws IOException {
        Path pathOfFile = Paths.get("campeonato-brasileiro.csv");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<String> lines = Files.readAllLines(pathOfFile);
        List<Jogo> listaJogos = new ArrayList<>();

        for (String line : lines.stream().skip(1).toList()) {
            String[] infos = line.split(";");

            Integer rodada = Integer.parseInt(infos[0]);
            LocalDate data = LocalDate.parse(infos[1], dateFormatter);
            if(infos[2].equals("")) infos[2] = "00h00";
            LocalTime horario = LocalTime.parse(infos[2].replace("h", ":"), timeFormatter);
            
            DayOfWeek diaSemana = data.getDayOfWeek();
            DataDoJogo dataDoJogo = new DataDoJogo(data, horario, diaSemana);

            Time timeMandante = new Time(infos[4]);
            Time timeVisitante = new Time(infos[5]);
            Time timeVencedor = new Time(infos[6]);
            
            String arena = infos[7];
            Integer placarMandante = Integer.parseInt(infos[8]);
            Integer placarVisitante = Integer.parseInt(infos[9]);

            String estadoMandante = infos[10];
            String estadoVisitante = infos[11];
            String estadoVencedor = infos[12];

            Jogo jogo = new Jogo(rodada, dataDoJogo, timeMandante, timeVisitante, timeVencedor, arena, placarMandante, placarVisitante, estadoMandante, estadoVisitante, estadoVencedor);

            listaJogos.add(jogo);
        }

        return listaJogos;
    }

    public IntSummaryStatistics getEstatisticasPorJogo() {
        return null;
    }

    public Map<Jogo, Integer> getMediaGolsPorJogo() {
        return null;
    }

    public IntSummaryStatistics GetEstatisticasPorJogo() {
        return null;
    }

    public List<Jogo> todosOsJogos() {
        return null;
    }

    public Long getTotalVitoriasEmCasa() {
        return null;
    }

    public Long getTotalVitoriasForaDeCasa() {
        return null;
    }

    public Long getTotalEmpates() {
        return null;
    }

    public Long getTotalJogosComMenosDe3Gols() {
        return null;
    }

    public Long getTotalJogosCom3OuMaisGols() {
        return null;
    }

    public Map<Resultado, Long> getTodosOsPlacares() {
        return null;
    }

    public Map.Entry<Resultado, Long> getPlacarMaisRepetido() {
        return null;
    }

    public Map.Entry<Resultado, Long> getPlacarMenosRepetido() {
        return null;
    }

    private List<Time> getTodosOsTimes() {
        return null;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() {
        return null;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() {
        return null;
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        return null;
    }

    public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
        return null;
    }

    public Set<PosicaoTabela> getTabela() {
        return null;
    }

    private DayOfWeek getDayOfWeek(String dia) {
        return null;
    }

    private Map<Integer, Integer> getTotalGolsPorRodada() {
        return null;
    }

    private Map<Time, Integer> getTotalDeGolsPorTime() {
        return null;
    }

    private Map<Integer, Double> getMediaDeGolsPorRodada() {
        return null;
    }


}
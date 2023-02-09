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

        List<Jogo> listaJogos = Files.readAllLines(pathOfFile)
            .stream()
            .skip(1)
            .map(infoString -> {
                String[] informacoes = infoString.split(";");

                Integer rodada = Integer.valueOf(informacoes[0]);
                LocalDate data = LocalDate.parse(informacoes[1], dateFormatter);
                if(informacoes[2].equals("")) informacoes[2] = "00h00";
                LocalTime horario = LocalTime.parse(informacoes[2].replace("h", ":"), timeFormatter);
                
                DayOfWeek diaSemana = data.getDayOfWeek();
                DataDoJogo dataDoJogo = new DataDoJogo(data, horario, diaSemana);

                Time timeMandante = new Time(informacoes[4]);
                Time timeVisitante = new Time(informacoes[5]);
                Time timeVencedor = new Time(informacoes[6]);
                
                String arena = informacoes[7];
                Integer placarMandante = Integer.parseInt(informacoes[8]);
                Integer placarVisitante = Integer.parseInt(informacoes[9]);

                String estadoMandante = informacoes[10];
                String estadoVisitante = informacoes[11];
                String estadoVencedor = informacoes[12];

                Jogo jogo = new Jogo(rodada, dataDoJogo, timeMandante, timeVisitante, timeVencedor, arena, placarMandante, placarVisitante, estadoMandante, estadoVisitante, estadoVencedor);

                return jogo;
            })
            .collect(Collectors.toList());

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
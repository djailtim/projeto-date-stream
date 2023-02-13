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
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        IntSummaryStatistics statistics = jogos.stream()
            .filter(this.filtro)
            .mapToInt(jogo -> jogo.visitantePlacar() + jogo.mandantePlacar())
            .summaryStatistics();
        return statistics;
    }

    public Map<Jogo, Integer> getMediaGolsPorJogo() {
        Map<Jogo, Integer> mediaGolsPorJogo = todosOsJogos()
            .stream()
            .collect(Collectors.toMap(
                partida -> partida,
                partida -> (partida.mandantePlacar() + partida.visitantePlacar()) / 2)
            );
        return mediaGolsPorJogo;
    }

    public IntSummaryStatistics GetEstatisticasPorJogo() {
        var estatisticasPorJogo = todosOsJogos()
                .stream()
                .mapToInt(jogo-> jogo.visitantePlacar()+ jogo.mandantePlacar())
                .summaryStatistics();
        return estatisticasPorJogo;
    }

    public List<Jogo> todosOsJogos() {
        List<Jogo> todosJogos = brasileirao.values().stream().flatMap(campeonato -> campeonato.stream()).toList();
        return todosJogos;
    }

    public Long getTotalVitoriasEmCasa() {
        Long totalVitoriasEmCasa = todosOsJogos()
            .stream()
            .filter(partida -> partida.mandantePlacar() > partida.visitantePlacar())
            .count();
        return totalVitoriasEmCasa;
    }

    public Long getTotalVitoriasForaDeCasa() {
        Long totalVitoriasForaDeCasa = todosOsJogos()
            .stream()
            .filter(partida -> partida.visitantePlacar() > partida.mandantePlacar())
            .count();
        return totalVitoriasForaDeCasa;
    }

    public Long getTotalEmpates() {
        Long totalEmpates = todosOsJogos()
            .stream()
            .filter(partida -> partida.mandantePlacar() == partida.visitantePlacar())
            .count();
        return totalEmpates;
    }

    public Long getTotalJogosComMenosDe3Gols() {
        Long totalJogosComMenosDe3Gols = todosOsJogos()
            .stream()
            .filter(partida -> (partida.mandantePlacar() + partida.visitantePlacar() < 3))
            .count();
        return totalJogosComMenosDe3Gols;
    }

    public Long getTotalJogosCom3OuMaisGols() {
        Long totalJogosCom3OuMaisGols = todosOsJogos()
            .stream()
            .filter(partida -> (partida.mandantePlacar() + partida.visitantePlacar() > 3))
            .count();
        return totalJogosCom3OuMaisGols;
    }

    public Map<Resultado, Long> getTodosOsPlacares() {
        Map<Resultado, Long> todosOsPlacares = todosOsJogos()
            .stream()
            .collect(
                Collectors.groupingBy(partida -> new Resultado(
                    partida.mandantePlacar(),
                    partida.visitantePlacar()
                ), Collectors.counting())
            );
        return todosOsPlacares;
    }

    public Map.Entry<Resultado, Long> getPlacarMaisRepetido() {
        Entry<Resultado, Long> placarMaisRepetido = getTodosOsPlacares()
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .stream()
            .toList().get(0);
        return placarMaisRepetido;
    }

    public Map.Entry<Resultado, Long> getPlacarMenosRepetido() {
        Entry<Resultado, Long> placarMenosRepetido = getTodosOsPlacares()
            .entrySet()
            .stream()
            .min(Map.Entry.comparingByValue())
            .stream()
            .toList().get(0);
        return placarMenosRepetido;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoMandantes() {
        Map<Time, List<Jogo>> todosJogosTimeMandante = todosOsJogos().stream().collect(Collectors.groupingBy(Jogo::mandante));
        return todosJogosTimeMandante;
    }

    private Map<Time, List<Jogo>> getTodosOsJogosPorTimeComoVisitante() {
        Map<Time, List<Jogo>> todosJogosTimeVisitante = todosOsJogos().stream().collect(Collectors.groupingBy(Jogo::visitante));
        return todosJogosTimeVisitante;
    }

    public Map<Time, List<Jogo>> getTodosOsJogosPorTime() {
        Map<Time, List<Jogo>> todosOsJogosPorTimeComoMandantes = getTodosOsJogosPorTimeComoMandantes();
        Map<Time, List<Jogo>> todosOsJogosPorTimeComoVisitante = getTodosOsJogosPorTimeComoVisitante();
        Map<Time, List<Jogo>> todosOsJogosPorTime = Stream.of(todosOsJogosPorTimeComoMandantes, todosOsJogosPorTimeComoVisitante)
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(
                time -> time.getKey(),
                jogos -> jogos.getValue(),
                (time, jogos) -> {
                    time.addAll(jogos);
                    return time;
                }
            ));
        return todosOsJogosPorTime;
    }

    // public Map<Time, Map<Boolean, List<Jogo>>> getJogosParticionadosPorMandanteTrueVisitanteFalse() {
    //     return null;
    // }

    public Set<PosicaoTabela> getTabela() {
        LinkedHashSet<PosicaoTabela> tabelaPontuacao = todosOsJogos().stream()
            .map(Jogo::mandante)
            .distinct()
            .map(time -> new PosicaoTabela(
                time,
                getVitoriasPorTime(time),
                getDerrotasPorTime(time),
                getEmpatesPorTime(time),
                getGolsPositivosPorTime(time),
                getGolsSofridosPorTime(time),
                getSaldoDeGolsPorTime(time),
                getQuantidadeJogosPorTime(time)
            ))
            .sorted(Comparator.comparing(PosicaoTabela::pontos).reversed()
                .thenComparing(PosicaoTabela::vitorias).reversed()
                .thenComparing(PosicaoTabela::saldoDeGols).reversed()
            )
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return tabelaPontuacao;
    }

    // private DayOfWeek getDayOfWeek(String dia) {
    //     return null;
    // }

    private long getDerrotasPorTime(Time time) {
        long derrotasPorTime = todosOsJogos().stream()
                .filter(jogo -> (jogo.mandante().equals(time) && jogo.mandantePlacar() < jogo.visitantePlacar()) ||
                        (jogo.visitante().equals(time) && (jogo.visitantePlacar() < jogo.mandantePlacar())))
                .count();

        return derrotasPorTime;
    }

    private long getEmpatesPorTime(Time time) {
        long empatesPorTime = todosOsJogos().stream()
                .filter(jogo -> jogo.mandante().equals(time) || jogo.visitante().equals(time))
                .filter(jogo -> jogo.mandantePlacar().equals(jogo.visitantePlacar()))
                .count();

        return empatesPorTime;
    }

    private long getQuantidadeJogosPorTime(Time time) {
        long quantidadeJogosPorTime = todosOsJogos().stream()
                .filter(jogo -> jogo.mandante().equals(time) || jogo.visitante().equals(time))
                .count();

        return quantidadeJogosPorTime;
    }

    private long getGolsPositivosPorTime(Time time) {
        long golsPositivosPorTime = todosOsJogos().stream()
                .filter(filtro)
                .filter(jogo -> jogo.mandante().equals(time) || jogo.visitante().equals(time))
                .map(jogo -> {
                    if (jogo.mandante().equals(time)) {
                        return jogo.mandantePlacar();
                    }
                    return jogo.visitantePlacar();
                })
                .reduce(0, Integer::sum);

        return golsPositivosPorTime;
    }

    private long getGolsSofridosPorTime(Time time) {
        long golsSofridosPorTime = todosOsJogos().stream()
                .filter(jogo -> jogo.mandante().equals(time) || jogo.visitante().equals(time))
                .map(jogo -> {
                    if (jogo.mandante().equals(time)) {
                        return jogo.visitantePlacar();
                    }
                    return jogo.mandantePlacar();
                })
                .reduce(0, Integer::sum);

        return golsSofridosPorTime;
    }

    private long getSaldoDeGolsPorTime(Time time) {
        long saldoDeGolsPorTime = todosOsJogos().stream()
                .filter(jogo -> jogo.mandante().equals(time) || jogo.visitante().equals(time))
                .map(jogo -> {
                    if (jogo.mandante().equals(time)) {
                        return jogo.mandantePlacar() - jogo.visitantePlacar();
                    }
                    return jogo.visitantePlacar() - jogo.mandantePlacar();
                })
                .reduce(0, Integer::sum);

        return saldoDeGolsPorTime;
    }

    private long getVitoriasPorTime(Time time) {
        long vitoriasPorTime = todosOsJogos().stream()
                .filter(jogo -> (jogo.mandante().equals(time) && jogo.mandantePlacar() > jogo.visitantePlacar()) ||
                        (jogo.visitante().equals(time) && (jogo.visitantePlacar() > jogo.mandantePlacar())))
                .count();

        return vitoriasPorTime;
    }
}